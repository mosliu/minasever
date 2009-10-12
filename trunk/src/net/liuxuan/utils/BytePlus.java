package net.liuxuan.utils;

public class BytePlus {
    public static int bytes2int(byte[] b) {
	// byte[] b=new byte[]{1,2,3,4};
	int mask = 0xff;
	int temp = 0;
	int res = 0;
	for (int i = 0; i < 4; i++) {
	    res <<= 8;
	    temp = b[i] & mask;
	    res |= temp;
	}
	return res;
    }

    public static byte[] int2bytes(int num) {
	byte[] b = new byte[4];
	for (int i = 0; i < 4; i++) {
	    b[i] = (byte) (num >>> (24 - i * 8));
	}
	return b;
    }

    public static byte[] getPartBytes(byte[] source, int start, int length) {
	byte[] dest = new byte[length];
	System.arraycopy(source, 0, dest, start, length);
	return dest;
    }

    /**
     * 将指定byte数组以16进制字符串的形式返回
     * 
     * @param ba
     * @return
     */
    public static String byteArray2String(byte[] ba) {
	StringBuilder sb = new StringBuilder(ba.length * 2);
	for (int i = 0; i < ba.length; i++) {
	    String hex = Integer.toHexString(ba[i] & 0xFF);
	    if (hex.length() == 1) {
		sb.append('0');
	    }
	    sb.append(hex.toUpperCase());
	    sb.append(" ");
	}
	return sb.toString();
    }

}
