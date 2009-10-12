package net.liuxuan.OSMC.codec;

//~--- non-JDK imports --------------------------------------------------------

import net.liuxuan.OSMC.message.Message;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * A {@link ProtocolCodecFactory} that provides a protocol codec for SumUp
 * protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class RProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    /**
     * Constructs ...
     *
     */
    public RProtocolCodecFactory() {
        super.addMessageDecoder(RMessageDecoder.class);
        super.addMessageEncoder(Message.class, RMessageEncoder.class);
    }
}
