package net.liuxuan.OSMC.disuse;

//~--- non-JDK imports --------------------------------------------------------

import net.liuxuan.OSMC.common.FuncDefine;
import net.liuxuan.OSMC.message.Message;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class description
 *
 *
 * @version        Enter version here..., 09/09/29
 * @author         Enter your name here...
 */
public class ClientSessionHandler extends IoHandlerAdapter {

    // 客户发送编号
    private static final String SEQUENCE_NUM = "seq_num";
    private final static Logger LOGGER       = LoggerFactory.getLogger(ClientSessionHandler.class);
    private boolean             finished;

    /**
     * Constructs ...
     *
     */
    public ClientSessionHandler() {}

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Method description
     *
     *
     * @param session
     */
    @Override
    public void sessionOpened(IoSession session) {

        // initial Session Parameters
        session.setAttribute(SEQUENCE_NUM, 0);

        Integer seq_client = (Integer) session.getAttribute(SEQUENCE_NUM);

        session.setAttribute(SEQUENCE_NUM, ++seq_client);

        Message m = new Message(seq_client, FuncDefine.FUNC_LOGIN, "我胡汉三来了".getBytes());

        session.write(m);
    }

    /**
     * Method description
     *
     *
     * @param session
     * @param message
     */
    @Override
    public void messageReceived(IoSession session, Object message) {

        // 更新服务端发送序号
        Integer seq_client = (Integer) session.getAttribute(SEQUENCE_NUM);

        session.setAttribute(SEQUENCE_NUM, ++seq_client);

        Message msg        = (Message) message;
        short   func       = msg.getFunc();
        int     seq_server = msg.getSequence();
        int     length     = msg.getLength();
        byte[]  content    = msg.getContent();
        Message retMessage = new Message(seq_client);

        switch (func) {
        case FuncDefine.FUNC_LOGIN :
            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
            retMessage.setContent("欢迎登陆！".getBytes());
        case FuncDefine.FUNC_LOGOUT :
            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
            retMessage.setContent("欢迎再来！".getBytes());

            break;

        case FuncDefine.FUNC_SHOWTEXT :
            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
            System.out.println("SeverSays: " + seq_server + " - " + new String(content));
            retMessage.setContent("再试一次！".getBytes());

            if (seq_client <= 10) {
                session.write(retMessage);
            }

            break;

        default :
            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
            retMessage.setContent("不明通讯！".getBytes());

            break;
        }
    }

    /**
     * Method description
     *
     *
     * @param session
     * @param cause
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.close(true);
    }
}
