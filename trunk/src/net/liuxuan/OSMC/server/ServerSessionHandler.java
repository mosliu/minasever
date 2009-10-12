package net.liuxuan.OSMC.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.liuxuan.OSMC.common.FuncDefine;
import net.liuxuan.OSMC.message.Message;
import net.liuxuan.OSMC.message.UserAndPass;
import net.liuxuan.utils.ByteObject;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.FDIV;

public class ServerSessionHandler extends IoHandlerAdapter {

    // 服务端发送编号
    private static final String SEQUENCE_NUM = "seq_num";

    private final static Logger LOGGER = LoggerFactory
	    .getLogger(ServerSessionHandler.class);

    private final Set<IoSession> sessions = Collections
	    .synchronizedSet(new HashSet<IoSession>());

    private final Set<String> users = Collections
	    .synchronizedSet(new HashSet<String>());

    @Override
    public void sessionOpened(IoSession session) {
	// set idle time to 60 seconds
	session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);

	// initial Session Parameters
	session.setAttribute(SEQUENCE_NUM, 0);
	sessions.add(session);
	String user = session.getRemoteAddress().toString();
	session.setAttribute("user", user);
	users.add(user);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
	// 更新服务端发送序号
	Integer seq_server = (Integer) session.getAttribute(SEQUENCE_NUM);
	session.setAttribute(SEQUENCE_NUM, ++seq_server);

	Message msg = (Message) message;
	short func = msg.getFunc();
	int seq_client = msg.getSequence();
	int length = msg.getLength();
	byte[] content = msg.getContent();

	Message retMessage = new Message(seq_server);
	switch (func) {
	case FuncDefine.FUNC_LOGIN:
	    UserAndPass up =(UserAndPass) ByteObject.ByteToObject(content);;
	    
	    System.out.println("Client Logedin - " + seq_client + " - "
		    + up.username+":"+up.password);
	    retMessage.setFunc(FuncDefine.FUNC_LOGIN_RESPONSE);
	    retMessage.setContent("欢迎登陆！".getBytes());
	    break;
	case FuncDefine.FUNC_LOGOUT:
	    retMessage.setFunc(FuncDefine.FUNC_LOGOUT_RESPONSE);
	    retMessage.setContent("欢迎再来！".getBytes());
	    break;
	case FuncDefine.FUNC_BROADCAST_CLIENT:
	    retMessage.setFunc(FuncDefine.FUNC_BROADCAST_SERVER);
	    retMessage.setContent(content);
	    session.setAttribute(SEQUENCE_NUM, --seq_server);
	    broadcast(retMessage);
	    return;

	case FuncDefine.FUNC_SHOWTEXT:
	    retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
	    System.out.println("ClientSays: " + seq_client + " - "
		    + new String(content));
	    retMessage.setContent("再试一次！".getBytes());
	    break;
	default:
	    retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
	    retMessage.setContent("不明通讯！".getBytes());
	    break;
	}
	session.write(retMessage);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
	LOGGER.info("Disconnecting the idle.");
	// disconnect an idle client
	// session.close(true);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
	String user = (String) session.getAttribute("user");
	users.remove(user);
	sessions.remove(session);

	Integer seq_server = (Integer) session.getAttribute(SEQUENCE_NUM);
	// session.setAttribute(SEQUENCE_NUM, ++seq_server);
	Message retMessage = new Message(seq_server,
		FuncDefine.FUNC_BROADCAST_SERVER,
		("The user " + user + " has left the chat session.").getBytes());
	broadcast(retMessage);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
	LOGGER.warn("Unexpected exception.", cause);
	// Close connection when unexpected exception is caught.
	session.close(true);
    }

    public void broadcast(Message message) {
	synchronized (sessions) {
	    for (IoSession session : sessions) {
		if (session.isConnected()) {
		    Integer seq_server = (Integer) session
			    .getAttribute(SEQUENCE_NUM);
		    session.setAttribute(SEQUENCE_NUM, ++seq_server);
		    message.setSequence(seq_server);
		    session.write(message);
		}
	    }
	}
    }

    public boolean isChatUser(String name) {
	return users.contains(name);
    }

    public int getNumberOfUsers() {
	return users.size();
    }

    public void kick(String name) {
	synchronized (sessions) {
	    for (IoSession session : sessions) {
		if (name.equals(session.getAttribute("user"))) {
		    session.close(true);
		    break;
		}
	    }
	}
    }

}