package net.liuxuan.OSMC.common;

public class CommonParameters {
    public static final short PACKET_HEADER = (short) 0XAABB;

    public static final int PACKET_HEADER_LEN = 2;

    public static final int FUNC_LEN = 2;

    public static final int SEQUENCE_LEN = 4;

    public static final int HEADER_LEN = PACKET_HEADER_LEN + FUNC_LEN
	    + SEQUENCE_LEN;

    public static final int BYTE_LENGTH = 1;

    public static final int SHORT_LENGTH = 2;

    public static final int INTEGER_LENGTH = 4;

    public static final int LONG_LENGTH = 8;

    public static final String NEWLINE=System.getProperty("line.separator");
    /**
     * not used put below
     */

    public static final int BODY_LEN = 12;

    public static final int RESULT = 0;

    public static final int RESULT_CODE_LEN = 2;

    public static final int RESULT_VALUE_LEN = 4;

    public static final int ADD_BODY_LEN = 4;

    public static final int RESULT_OK = 0;

    public static final int RESULT_ERROR = 1;

}
