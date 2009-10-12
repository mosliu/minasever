package net.liuxuan.minaclient.timeclient;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaTimeClient {
    public static void main(String[] args) {
	// 创建客户端连接器.
	
	SocketConnector connector = new NioSocketConnector();
	connector.setConnectTimeout(30);
	
	connector.getFilterChain().addLast("logger", new LoggingFilter());
	connector.getFilterChain().addLast(
		"codec",
		new ProtocolCodecFilter(new TextLineCodecFactory(Charset
			.forName("UTF-8")))); // 设置编码过滤器
	connector.setHandler(new TimeClientHandler());
	ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1",
		1984));// 建立连接
	cf.awaitUninterruptibly();// 等待连接创建完成
	IoSession session = cf.getSession();
	session.write("hello");// 发送消息
	session.write("quit");// 发送消息
	session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
    }
}