package net.liuxuan.minaserver.timeserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaTimeServer {

    private static final int PORT = 9123;

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
	// ����buffer
	IoBuffer.setUseDirectBuffer(false);
	IoBuffer.setAllocator(new SimpleBufferAllocator());
	// ����acceptor
	SocketAcceptor acceptor = new NioSocketAcceptor();
	// ����config
	acceptor.setReuseAddress(true);
	LoggingFilter loggingFilter = new LoggingFilter();
	acceptor.getFilterChain().addLast("logger", loggingFilter);
	acceptor.getFilterChain().addLast(
		"codec",
		new ProtocolCodecFilter(new TextLineCodecFactory(Charset
			.forName("UTF-8"))));
	acceptor.setHandler(new TimeServerHandler());   
	acceptor.bind(new InetSocketAddress(PORT));   
//	acceptor.bind(localAddress)
//		.bind(new InetSocketAddress(PORT), new TimeServerHandler());
	// acceptor.bind(new InetSocketAddress(PORT), new HelloHandler(), cfg);
	System.out.println("MINA Time server started.");
    }
}