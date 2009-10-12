package net.liuxuan.OSMC.disuse;

//~--- non-JDK imports --------------------------------------------------------

import net.liuxuan.OSMC.codec.RProtocolCodecFactory;
import net.liuxuan.OSMC.common.PropertiesReader;
import net.liuxuan.OSMC.server.Server;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.net.InetSocketAddress;

/**
 * (<strong>Entry Point</strong>) Starts SumUp client.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class Client {
    private static final long   CONNECT_TIMEOUT = 30 * 1000L;    // 30 seconds
    private static final String HOSTNAME        = "localhost";
    private static final int    SERVER_PORT     = PropertiesReader.readInt("server_port");

    // Set this to false to use object serialization instead of custom codec.
    private static final boolean USE_CUSTOM_CODEC = PropertiesReader.readBoolean("USE_CUSTOM_CODEC");
    final Logger                 log              = LoggerFactory.getLogger(Server.class);

    /**
     * Method description
     *
     *
     * @param args
     *
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        NioSocketConnector connector = new NioSocketConnector();

        // Configure the service.
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

        if (USE_CUSTOM_CODEC) {
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new RProtocolCodecFactory()));
        } else {
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        }

        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.setHandler(new ClientSessionHandler());
        System.out.println("����Client......");

        ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, SERVER_PORT));

        future.awaitUninterruptibly();

        IoSession session;

        session = future.getSession();

        // for (;;) {
        // try {
        // ConnectFuture future = connector.connect(new InetSocketAddress(
        // HOSTNAME, SERVER_PORT));
        // future.awaitUninterruptibly();
        // session = future.getSession();
        // break;
        // } catch (RuntimeIoException e) {
        // System.err.println("Failed to connect.");
        // e.printStackTrace();
        // Thread.sleep(5000);
        // }
        // }
        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
