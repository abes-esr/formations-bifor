/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author jean-laurent
 */
public class Encryption {
    public final static int KEY_SIZE = 128;  // [32..448]
    
    private static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static int charLength = chars.length();
  
    public static String encrypt(String key) throws NoSuchAlgorithmException {
        byte[] uniqueKey = key.getBytes();
        byte[] hash = null;
        hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        StringBuffer hashString = new StringBuffer();
        for (int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }
    
     public static String decrypt(String ss) {
         return ss;
     }
     
     public static String generatePassword() {
         StringBuilder  pass = new StringBuilder (charLength);
        for (int x = 0; x < 5; x++) {
            int i = (int) (Math.random() * charLength);
            pass.append(chars.charAt(i));
        }
        return pass.toString();
     }
    
}
