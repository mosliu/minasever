package net.liuxuan.minaPackage.protocal;

import java.nio.charset.Charset;

import net.liuxuan.minaPackage.TimePacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class TimeProtocalEncoder extends ProtocolEncoderAdapter {


    private final Charset charset;   
    public TimeProtocalEncoder(Charset charset) {   
        this.charset = charset;   
    }   
    //在此处实现对TimePacket包的编码工作，并把它写入输出流中   
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {   
	TimePacket value = (TimePacket) message;   
	IoBuffer buf = IoBuffer.allocate(value.getLength());   
        buf.setAutoExpand(true);   
        buf.putInt(value.getLength());   
        buf.put(value.getFlag());   
        if (value.getTimestr() != null)   
            buf.put(value.getTimestr().getBytes());   
        buf.flip();   
        out.write(buf);   
    }   
    public void dispose() throws Exception {   
    }

}
