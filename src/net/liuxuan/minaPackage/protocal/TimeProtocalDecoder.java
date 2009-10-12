package net.liuxuan.minaPackage.protocal;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import net.liuxuan.minaPackage.TimePacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class TimeProtocalDecoder implements ProtocolDecoder {
    
    // private final AttributeKey CONTEXT = new AttributeKey(getClass(),
    // "context");
    String CONTEXT = "context";
    private final Charset charset;
    private int maxPackLength = 100;

    public TimeProtocalDecoder() {
	this(Charset.defaultCharset());	
	
    }

    public TimeProtocalDecoder(Charset charset) {
	this.charset = charset;
    }

    public int getMaxLineLength() {
	return maxPackLength;
    }

    public void setMaxLineLength(int maxLineLength) {
	if (maxLineLength <= 0) {
	    throw new IllegalArgumentException("maxLineLength: "
		    + maxLineLength);
	}
	this.maxPackLength = maxLineLength;
    }

    private Context getContext(IoSession session) {
	
	Context ctx;
	ctx = (Context) session.getAttribute(CONTEXT);
	if (ctx == null) {
	    ctx = new Context();
	    session.setAttribute(CONTEXT, ctx);
	}
	return ctx;
    }

    public void decode(IoSession session, IoBuffer in,
	    ProtocolDecoderOutput out) throws Exception {
	final int packHeadLength = 5;
	// �Ȼ�ȡ�ϴεĴ��������ģ����п�����δ�����������
	Context ctx = getContext(session);
	// �Ȱѵ�ǰbuffer�е�����׷�ӵ�Context��buffer����
	ctx.append(in);
	// ��positionָ��0λ�ã���limitָ��ԭ����positionλ��
	IoBuffer buf = ctx.getBuffer();
	buf.flip();
	// Ȼ�����ݰ���Э����ж�ȡ
	while (buf.remaining() >= packHeadLength) {
	    buf.mark();
	    // ��ȡ��Ϣͷ����
	    int length = buf.getInt();
	    byte flag = buf.get();
	    // ����ȡ�İ�ͷ�Ƿ��������������Ļ����buffer
	    if (length < 0 || length > maxPackLength) {
		buf.clear();
		break;
	    }
	    // ��ȡ��������Ϣ������д��������У��Ա�IoHandler���д���
	    else if (length >= packHeadLength
		    && length - packHeadLength <= buf.remaining()) {
		int oldLimit2 = buf.limit();
		buf.limit(buf.position() + length - packHeadLength);
		String content = buf.getString(ctx.getDecoder());
		buf.limit(oldLimit2);
		TimePacket pack = new TimePacket(flag, content);
		out.write(pack);
	    } else {
		// �����Ϣ��������
		// ��ָ�������ƶ���Ϣͷ����ʼλ��
		buf.reset();
		break;
	    }
	}
	if (buf.hasRemaining()) {
	    // �������Ƶ�buffer����ǰ��
	    IoBuffer temp = IoBuffer.allocate(maxPackLength).setAutoExpand(
		    true);
	    temp.put(buf);
	    temp.flip();
	    buf.clear();
	    buf.put(temp);

	} else {// ��������Ѿ�������ϣ��������
	    buf.clear();
	}

    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
	    throws Exception {
    }

    public void dispose(IoSession session) throws Exception {
	Context ctx = (Context) session.getAttribute(CONTEXT);
	if (ctx != null) {
	    session.removeAttribute(CONTEXT);
	}
    }

    // ��¼�����ģ���Ϊ���ݴ���û�й�ģ���ܿ���ֻ�յ����ݰ���һ��
    // ���ԣ���Ҫ������ƴ�������������Ĵ���
    private class Context {
	private final CharsetDecoder decoder;
	private IoBuffer buf;
	private int matchCount = 0;
	private int overflowPosition = 0;

	private Context() {
	    decoder = charset.newDecoder();
	    buf = IoBuffer.allocate(80).setAutoExpand(true);
	}

	public CharsetDecoder getDecoder() {
	    return decoder;
	}

	public IoBuffer getBuffer() {
	    return buf;
	}

	public int getOverflowPosition() {
	    return overflowPosition;
	}

	public int getMatchCount() {
	    return matchCount;
	}

	public void setMatchCount(int matchCount) {
	    this.matchCount = matchCount;
	}

	public void reset() {
	    overflowPosition = 0;
	    matchCount = 0;
	    decoder.reset();
	}

	public void append(IoBuffer in) {
	    getBuffer().put(in);

	}

    }

}
