
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package net.liuxuan.OSMC.client;

//~--- non-JDK imports --------------------------------------------------------

import net.liuxuan.OSMC.common.FuncDefine;
import net.liuxuan.OSMC.message.Message;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
public class SwingClientHandler extends IoHandlerAdapter {
    public static final String SEQUENCE_NUM = "seq_num";
    private final static Logger LOGGER       = LoggerFactory.getLogger(SwingClientHandler.class);
    private final Callback      callback;

    /**
     * Constructs ...
     *
     *
     * @param callback
     */
    public SwingClientHandler(Callback callback) {
        this.callback = callback;
    }

    /**
     * Method description
     *
     *
     * @param session
     *
     * @throws Exception
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {

        // initial Session Parameters
        session.setAttribute(SEQUENCE_NUM, 0);
//        Integer seq_client = 0;
//        session.setAttribute(SEQUENCE_NUM, ++seq_client);
//
//        Message m = new Message(seq_client, FuncDefine.FUNC_LOGIN, "我胡汉三来了".getBytes());
//
//        session.write(m);
        callback.connected();
    }

    /**
     * Method description
     *
     *
     * @param session
     * @param message
     *
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

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
        case FuncDefine.FUNC_LOGIN_RESPONSE :
//            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
//            retMessage.setContent("欢迎登陆！".getBytes());
            callback.loggedIn("Server Says:"+new String(content));
            break;

        case FuncDefine.FUNC_LOGOUT_RESPONSE :
//            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
//            retMessage.setContent("欢迎再来！".getBytes());
            callback.loggedOut("Server Says:"+new String(content));
            break;
            
        case FuncDefine.FUNC_BROADCAST_SERVER :
//            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
//            retMessage.setContent("欢迎再来！".getBytes());
            callback.messageBroadCasted(content);
            break;

        case FuncDefine.FUNC_SHOWTEXT :
            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
            System.out.println("SeverSays: " + seq_server + " - " + new String(content));
            retMessage.setContent("再试一次！".getBytes());

            if (seq_client <= 10) {
                session.write(retMessage);
            }

            callback.messageReceived(content);

            break;

        default :
            retMessage.setFunc(FuncDefine.FUNC_SHOWTEXT);
            retMessage.setContent("不明通讯！".getBytes());
            callback.error(content);

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

    /**
     * Method description
     *
     *
     * @param session
     *
     * @throws Exception
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        callback.disconnected();
    }

    public interface Callback {

        /**
         * Method description
         *
         */
        void connected();

        /**
         * Method description
         *
         */
        void loggedIn(String msg);

        /**
         * Method description
         *
         */
        void loggedOut(String msg);

        /**
         * Method description
         *
         */
        void disconnected();

        /**
         * Method description
         *
         *
         * @param message
         */
        void messageReceived(byte[] message);

        /**
         * Method description
         *
         *
         * @param message
         */
        void messageBroadCasted(byte[] message);

        /**
         * Method description
         *
         *
         * @param message
         */
        void error(byte[] message);
    }
}
