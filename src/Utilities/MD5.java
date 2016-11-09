/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import managers.DBManager;

/**
 *
 * @author michaelgoode
 */
public class MD5 {

    private static final MD5 instance = new MD5();

    public static MD5 getInstance() {
        return instance;
    }

    public MD5() {
    }

    public String hash(String s) {

        MessageDigest md = null;

        try {

            md = MessageDigest.getInstance("MD5");

            md.update(s.getBytes());

            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
}
