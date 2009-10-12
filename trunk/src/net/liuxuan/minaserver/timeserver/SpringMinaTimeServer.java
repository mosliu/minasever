package net.liuxuan.minaserver.timeserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.liuxuan.minaPackage.protocal.TimeProtocalCodecFactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.core.joran.spi.JoranException;

public class SpringMinaTimeServer implements InitializingBean {
    final Logger log = LoggerFactory.getLogger(SpringMinaTimeServer.class);

    private int port;

    // ∂®“Âacceptor
    @Autowired
    private NioSocketAcceptor acceptor;


    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private ProtocolCodecFilter protocolFilter;
    
    @Autowired 
    private TimeServerHandler handler;

    public void afterPropertiesSet() throws Exception {
	doPreSet();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	acceptor.getFilterChain().addLast("logger", loggingFilter);
//	cfg.getFilterChain().addLast("codec", protocolFilter);
	acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TimeProtocalCodecFactory(Charset.forName("GBK"))));
	acceptor.setHandler(handler);   
	acceptor.bind(new InetSocketAddress(port));  
	System.out.println("************************************");
	System.out.println("*******springwith mina server*******");
	System.out.println("*******" + sdf.format(new Date()) + "***");
	System.out.println("************************************");
    }

    private void doPreSet() throws JoranException, IOException {

	
	IoBuffer.setUseDirectBuffer(false);
	IoBuffer.setAllocator(new SimpleBufferAllocator());
	acceptor.setReuseAddress(true);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
