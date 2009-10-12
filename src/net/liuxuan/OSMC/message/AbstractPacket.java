package net.liuxuan.OSMC.message;

import java.io.Serializable;

/**
 * A base Packet for protocol messages.
 * 
 * @author Moses
 */
public abstract class AbstractPacket implements Serializable {

    int sequence;
    short func;

    public int getSequence() {
	return sequence;
    }

    public void setSequence(int sequence) {
	this.sequence = sequence;
    }

    public short getFunc() {
	return func;
    }

    public void setFunc(short func) {
	this.func = func;
    }
}