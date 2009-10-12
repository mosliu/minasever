package net.liuxuan.OSMC.codec;

import net.liuxuan.OSMC.codec.base.AbstractPacketEncoder;
import net.liuxuan.OSMC.common.CommonParameters;
import net.liuxuan.OSMC.message.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * A {@link MessageEncoder} that encodes {@link AddMessage}.
 * 
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class RMessageEncoder<T extends Message> extends AbstractPacketEncoder<T> {
    public RMessageEncoder() {
        super(CommonParameters.PACKET_HEADER);//Ìí¼Ó°üÍ·
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
	out.putInt(message.getLength());
	out.put(message.getContent());
    }

    public void dispose() throws Exception {
    }
}
