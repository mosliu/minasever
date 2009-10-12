package net.liuxuan.OSMC.message;

import net.liuxuan.utils.BytePlus;

/**
 * <code>ADD</code> message in SumUp protocol.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class Message extends AbstractPacket {

    /**
     * 
     */
    private static final long serialVersionUID = 7536443427763202727L;

    private int length;

    private byte[] content;
    
    public Message() {
    }

    public Message(int _seq) {
	this.sequence = _seq;
    }
    
    public Message(int _seq,short _func,byte[] _content) {
	this.func = _func;
	this.sequence = _seq;
	this.content = _content;
	length = content.length;
    }
    
    public Message(short _func,byte[] _content) {
	this.func = _func;
	this.sequence = -888;
	this.content = _content;
	length = content.length;
    }
    
    public Message(byte[] _content) {
	this.content = _content;
	length = content.length;
    }
    
    @Override
    public String toString() {
        return getSequence() + ":(" + BytePlus.byteArray2String(content) + ')';
    }

    public int getLength() {
        return length;
    }

//    public void setLength(int length) {
//        this.length = length;
//    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
        length = content.length;
    }
}
