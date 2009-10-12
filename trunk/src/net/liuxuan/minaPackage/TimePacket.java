package net.liuxuan.minaPackage;

import net.liuxuan.utils.BytePlus;

public class TimePacket {
    private int length;
    private byte flag;
    private String timestr;

    public TimePacket() {

    }

    public TimePacket(int flag, String content) {
	this.flag = (byte) flag;
	this.timestr = content;
	int len1 = content == null ? 0 : content.getBytes().length;
	this.length = 5 + len1;
    }

    
    public TimePacket(byte flag, String content) {
	this.flag = flag;
	this.timestr = content;
	int len1 = content == null ? 0 : content.getBytes().length;
	this.length = 5 + len1;
    }

    public TimePacket(byte[] bs) {
	if (bs != null && bs.length >= 5) {
	    length = BytePlus.bytes2int(BytePlus.getPartBytes(bs, 0, 4));
	    flag = bs[4];
	    timestr = new String(BytePlus.getPartBytes(bs, 5, length - 5));
	}
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(" Len:").append(length);
	sb.append(" flag:").append(flag);
	sb.append(" Time:").append(timestr);
	return sb.toString();
    }

    public int getLength() {
	return length;
    }

    public void setLength(int length) {
	this.length = length;
    }

    public byte getFlag() {
	return flag;
    }

    public void setFlag(byte flag) {
	this.flag = flag;
    }

    public String getTimestr() {
	return timestr;
    }

    public void setTimestr(String timestr) {
	this.timestr = timestr;
    }
}
