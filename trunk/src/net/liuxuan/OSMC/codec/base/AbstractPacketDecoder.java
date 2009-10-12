package net.liuxuan.OSMC.codec.base;

import net.liuxuan.OSMC.common.CommonParameters;
import net.liuxuan.OSMC.message.AbstractPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * A {@link MessageDecoder} that decodes message header and forwards
 * the decoding of body to a subclass.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public abstract class AbstractPacketDecoder implements MessageDecoder {
    private final short header;

    private int sequence;
    
    private short func;

    private boolean readHeader;

    protected AbstractPacketDecoder(short type) {
        this.header = type;
    }

    public AbstractPacketDecoder() {
        this.header = CommonParameters.PACKET_HEADER;
    }
    
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        if (in.remaining() < CommonParameters.HEADER_LEN) {
            return MessageDecoderResult.NEED_DATA;
        }

        // Return OK if type and bodyLength matches.
        if (header == in.getShort()) {
            return MessageDecoderResult.OK;
        }

        // Return NOT_OK if not matches.
        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
        // Try to skip header if not read.
        if (!readHeader) {
            in.getShort(); // Ìø¹ý 'header'.
            sequence = in.getInt(); // Get 'sequence'.
            func = in.getShort(); // Get 'func'.
            readHeader = true;
        }

        // Try to decode body
        AbstractPacket pkt = decodeBody(session, in);
        // Return NEED_DATA if the body is not fully read.
        if (pkt == null) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            readHeader = false; // reset readHeader for the next decode
        }
        pkt.setSequence(sequence);
        pkt.setFunc(func);
        out.write(pkt);

        return MessageDecoderResult.OK;
    }

    /**
     * @return <tt>null</tt> if the whole body is not read yet
     */
    protected abstract AbstractPacket decodeBody(IoSession session,
            IoBuffer in);
}
