package net.liuxuan.minaserver.timeserver;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.liuxuan.minaPackage.TimePacket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

public class TimeServerHandler extends IoHandlerAdapter {
    public void exceptionCaught(IoSession session, Throwable t)
	    throws Exception {
	t.printStackTrace();
	session.close();
    }

    public void messageReceived(IoSession session, Object msg) throws Exception {
	String str = msg.toString();
	// 如果是quit就关闭session退出
	if (str.trim().equalsIgnoreCase("quit")) {
	    session.close();
	    return;
	}
	// 否则打印当前日期
	Date date = new Date();
	// session.write( date.toString() );
	session.write(new TimePacket(3, date.toString()));
	System.out.println("Message written");
    }

    public void sessionCreated(IoSession session) throws Exception {
	System.out.println("Session created");

	Set<IoSession> sets = new HashSet<IoSession>();
	sets.addAll(session.getService().getManagedSessions().values());

	for (Iterator iterator = sets.iterator(); iterator.hasNext();) {
	    IoSession ioSession = (IoSession) iterator.next();

	    ioSession.write(new TimePacket(1, session.getRemoteAddress()
		    .toString()
		    + " registered!"));

	}
	((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(2048);
	session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }
}