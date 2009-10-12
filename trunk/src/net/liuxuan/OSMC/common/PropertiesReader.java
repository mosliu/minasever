package net.liuxuan.OSMC.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    static boolean ready = false;
    static Properties p = new Properties();

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

    public static String read(String key) {
	return getPropertie().getProperty(key);
    }
    
    public static int readInt(String key) {
	return Integer.parseInt(getPropertie().getProperty(key));
    }
    
    public static boolean readBoolean(String key) {
	String value = getPropertie().getProperty(key);
	if(value.equalsIgnoreCase("TRUE")){
	    return true;
	}else{
	    return false;
	}
    }

    private static Properties getPropertie(){
	try {
	    init();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return p;
    }
    
    private static void init() throws IOException {
	
	if (ready == false) {
	    String name = "bin/net/liuxuan/OSMC/common/config.properties";
	    String name1 = "bin/net/liuxuan/OSMC/common/config2.properties";
	    InputStream in = new BufferedInputStream(new FileInputStream(name));
	    InputStream in1 = new BufferedInputStream(new FileInputStream(name1));
	    p.load(in);
	    p.load(in1);
	    ready = true;
	}
	// ResourceBundle rb =
	// ResourceBundle.getBundle(name,Locale.getDefault());//method2

	// ResourceBundle rb = new PropertyResourceBundle(in);//method3

    }
}
