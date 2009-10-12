package net.liuxuan.minaPackage.protocal;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class TimeProtocalCodecFactory implements ProtocolCodecFactory {
    private final TimeProtocalEncoder encoder;
    private final TimeProtocalDecoder decoder;

    public TimeProtocalCodecFactory(Charset charset) {
	encoder = new TimeProtocalEncoder(charset);
	decoder = new TimeProtocalDecoder(charset);
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
	return decoder;
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
	return encoder;
    }

}
