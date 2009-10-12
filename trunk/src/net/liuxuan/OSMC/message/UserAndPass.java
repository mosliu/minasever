package net.liuxuan.OSMC.message;

import java.io.Serializable;

/**
 *
 * @author Moses
 */
public class UserAndPass implements Serializable{
    
    public String username="";
    public String password="";

    public UserAndPass(String username,String password) {
        this.username = username;
        this.password = password;
    }

}
