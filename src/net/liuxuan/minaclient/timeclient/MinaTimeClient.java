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
	// �����ͻ���������.
	
	SocketConnector connector = new NioSocketConnector();
	connector.setConnectTimeout(30);
	
	connector.getFilterChain().addLast("logger", new LoggingFilter());
	connector.getFilterChain().addLast(
		"codec",
		new ProtocolCodecFilter(new TextLineCodecFactory(Charset
			.forName("UTF-8")))); // ���ñ��������
	connector.setHandler(new TimeClientHandler());
	ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1",
		1984));// ��������
	cf.awaitUninterruptibly();// �ȴ����Ӵ������
	IoSession session = cf.getSession();
	session.write("hello");// ������Ϣ
	session.write("quit");// ������Ϣ
	session.getCloseFuture().awaitUninterruptibly();// �ȴ����ӶϿ�
    }
}