package net.liuxuan.OSMC.codec.base;

import net.liuxuan.OSMC.common.CommonParameters;
import net.liuxuan.OSMC.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * A {@link MessageEncoder} that encodes message header and forwards the
 * encoding of body to a subclass.
 * 
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public abstract class AbstractPacketEncoder<T extends AbstractPacket>
	implements MessageEncoder<T> {
    private final short header;

    protected AbstractPacketEncoder(short type) {
	this.header = type;
    }

    public AbstractPacketEncoder() {
	this.header = CommonParameters.PACKET_HEADER;
    }

    public void encode(IoSession session, T message, ProtocolEncoderOutput out)
	    throws Exception {
	IoBuffer buf = IoBuffer.allocate(16);
	buf.setAutoExpand(true); // Enable auto-expand for easier encoding

	// Encode a header
	buf.putShort(header);
	buf.putInt(message.getSequence());
	buf.putShort(message.getFunc());
	
	// Encode a body
	encodeBody(session, message, buf);
	buf.flip();
	out.write(buf);
    }

    protected abstract void encodeBody(IoSession session, T message,
	    IoBuffer out);
}
