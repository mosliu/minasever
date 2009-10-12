
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liuxuan.OSMC.client;

//~--- non-JDK imports --------------------------------------------------------
import net.liuxuan.OSMC.codec.RProtocolCodecFactory;
import net.liuxuan.OSMC.common.FuncDefine;
import net.liuxuan.OSMC.message.Message;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

//~--- JDK imports ------------------------------------------------------------

import java.net.SocketAddress;
import net.liuxuan.OSMC.message.UserAndPass;
import net.liuxuan.utils.ByteObject;

/**
 *
 * @author Administrator
 */
public class SwingClientSupport {

    private final IoHandler handler;
    private IoSession session;

    /**
     * Constructs ...
     *
     *
     * @param handler
     */
    public SwingClientSupport(IoHandler handler) {
        this.handler = handler;
    }

    /**
     * Method description
     *
     *
     * @param connector
     * @param address
     *
     * @return
     */
    public boolean connect(NioSocketConnector connector, SocketAddress address) {
        if ((session != null) && session.isConnected()) {
            throw new IllegalStateException("Already connected. Disconnect first.");
        }

        try {
            IoFilter LOGGING_FILTER = new LoggingFilter();
            IoFilter CODEC_FILTER = new ProtocolCodecFilter(new RProtocolCodecFactory());

            connector.getFilterChain().addLast("codec", CODEC_FILTER);
            connector.getFilterChain().addLast("logger", LOGGING_FILTER);
            connector.setHandler(handler);

            ConnectFuture future1 = connector.connect(address);

            future1.awaitUninterruptibly();

            if (!future1.isConnected()) {
                return false;
            }

            session = future1.getSession();

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * 附带用户名密码登陆的联系
     *
     *
     * @param connector
     * @param address
     *
     * @return
     */
    public boolean connect(NioSocketConnector connector, SocketAddress address, String username, String userpass) {
        if ((session != null) && session.isConnected()) {
            throw new IllegalStateException("Already connected. Disconnect first.");
        }

        try {
            IoFilter LOGGING_FILTER = new LoggingFilter();
            IoFilter CODEC_FILTER = new ProtocolCodecFilter(new RProtocolCodecFactory());

            connector.getFilterChain().addLast("codec", CODEC_FILTER);
            connector.getFilterChain().addLast("logger", LOGGING_FILTER);
            connector.setHandler(handler);

            ConnectFuture future1 = connector.connect(address);

            future1.awaitUninterruptibly();

            if (!future1.isConnected()) {
                return false;
            }

            session = future1.getSession();
            login(username, userpass);


            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Method description
     *
     */
    public void login(String username,String userpass) {
        byte[] bs = ByteObject.ObjectToByte(new UserAndPass(username, userpass));
        Message m = new Message(0, FuncDefine.FUNC_LOGIN, bs);
        session.write(m);
    }

    /**
     * Method description
     *
     *
     * @param message
     */
    public void broadcast(String message) {
        SendSingleStringTypeMessage(FuncDefine.FUNC_BROADCAST_CLIENT, message);

//        session.write("BROADCAST " + message);
    }

    /**
     * Method description
     *
     */
    public void quit() {
        if (session != null) {
            if (session.isConnected()) {
//                Integer seq_client = (Integer) session.getAttribute(ClientSessionHandler.SEQUENCE_NUM);
//                Message m = new Message(seq_client, FuncDefine.FUNC_LOGOUT, "轻轻的俺走了，不留下一分钱".getBytes());
//                session.write(m);
//                session.write("QUIT");

                SendSingleStringTypeMessage(FuncDefine.FUNC_LOGOUT, "轻轻的俺走了，不留下一分钱");
                // Wait until the chat ends.

                session.getCloseFuture().awaitUninterruptibly();
            }

            session.close(true);
        }
    }

    /**
     * 使用前请确保Session为正常状态。
     */
    private void SendSingleStringTypeMessage(short func, String msg) {
        Integer seq_client = (Integer) session.getAttribute(SwingClientHandler.SEQUENCE_NUM);
        Message m = new Message(seq_client, func, msg.getBytes());
        session.write(m);
        session.setAttribute(SwingClientHandler.SEQUENCE_NUM, ++seq_client);
    }
}
