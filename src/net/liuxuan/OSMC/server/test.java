package net.liuxuan.OSMC.server;

import net.liuxuan.OSMC.message.UserAndPass;
import net.liuxuan.utils.ByteObject;
import net.liuxuan.utils.BytePlus;

public class test {

    /**
     * @param args
     */
    public static void main(String[] args) {
//	System.out.println(PropertiesReader.read("port2"));
//	System.out.println(PropertiesReader.read("author"));

        byte[] bs = ByteObject.ObjectToByte(new UserAndPass("aaaa", "2222"));
        String a = BytePlus.byteArray2String(bs);
        System.out.println(a);
    }
}
