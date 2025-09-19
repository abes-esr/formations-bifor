/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jean-laurent
 */
public class BufferedRW {

    final public static String UTF8 = "UTF-8";
    final public static String USASCII = "US-ASCII";
    final public static String ISOLATIN1 = "ISO-8859-1";
    final public static String XMLVERSION = "1.0";
    final public static String XMLPROLOG = "<?xml version=\"" + XMLVERSION
            + "\" encoding=\"" + UTF8 + "\"?>"; // !!!noblanks
    final private static String REGEX = "(.+)\\.(.+)";
    final private static Pattern PATTERN = Pattern.compile(REGEX);

    public static Reader getBufferedReader(String fileName, String charsetName)
            throws FileNotFoundException, UnsupportedEncodingException {

        File file = new File(fileName);
        return getBufferedReader(file, charsetName);
    }

    public static Reader getBufferedReader(File file, String charsetName)
            throws FileNotFoundException, UnsupportedEncodingException {

        if (!file.exists()) {
            Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                    "File does not exist" + file.getPath());

        }
        InputStream input = new FileInputStream(file);
        if (charsetName == null) {
            return new BufferedReader(new InputStreamReader(input));
        } else {
            return new BufferedReader(new InputStreamReader(input, charsetName));
        }
    }

    public static Writer getBufferedWriter(File file, String charsetName)
            throws FileNotFoundException, UnsupportedEncodingException {
        Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                "Writing " + file.getPath());
        OutputStream out = new FileOutputStream(file);
        if (charsetName == null) {
            return new BufferedWriter(new OutputStreamWriter(out));
        } else {
            return new BufferedWriter(new OutputStreamWriter(out, charsetName));
        }
    }

    /**
     * utility method to provide buffered reader on an UTF-8 file and log
     * exception before re-throwing it
     */
    public static Reader UTF8Reader(String fileName)
            throws FileNotFoundException, UnsupportedEncodingException {
        Reader br = null;
        try {
            br = BufferedRW.getBufferedReader(fileName, BufferedRW.UTF8);
        } catch (FileNotFoundException e) {
            Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                    e.getMessage());
            throw e;
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                    e.getMessage());
            throw e;
        }

        return br;

    }

    /**
     *
     * Copy a File
     * @param fromFile
     * @param toFile
     * @return <code>true</code> if and only if the renaming succeeded;
     *         <code>false</code> otherwise
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    protected final static boolean copy(File fromFile, File toFile, String charsetName)
            throws FileNotFoundException, UnsupportedEncodingException, IOException {

        if (toFile.exists()) {
            return false;
        }
            Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                toFile.getName()+ " is not exist yet");
            
        Reader inBuffer = getBufferedReader(fromFile, charsetName);
        Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                "inBuffer:"+inBuffer.toString());
        Writer outBuffer = getBufferedWriter(toFile, charsetName);
        Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                "outBuffer:"+outBuffer.toString());

        try {
            while (true) {
                int data = inBuffer.read();
                if (data == -1) {
                    break;
                }
                outBuffer.write(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                    "Error while copying : " + e.getMessage());
            return false;
        } finally {

            if (inBuffer != null) {
                try {
                    inBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                            "Error while copying : " + e.getMessage());
                    return false;

                }
            }

            if (outBuffer != null) {
                try {
                    outBuffer.flush();
                    outBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                     Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                "Error while copying : " + e.getMessage());
                    return false;

                }
            }

        }

        // cleanupif files are not the same length
        if (fromFile.length() != toFile.length()) {
             Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                "file " + toFile.getAbsoluteFile().getName() +
                " (" + toFile.length() +
                " bytes) is not the same length than file " +
                fromFile.getAbsoluteFile().getName() +
                " (" + fromFile.length() + " bytes) !!!");
            toFile.delete();
            return false;
        }

          Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                "copy is OK");
        return true;
    }

    /**
     * The renameTo method does not allow action across NFS mounted filesystems
     * this method is the workaround
     *
     *
     * @param fromFile
     * @param toFile
     * @return <code>true</code> if and only if the renaming succeeded;
     *         <code>false</code> otherwise
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public final static boolean moveNFSProof(File fromFile, File toFile, String charsetName)
            throws FileNotFoundException, UnsupportedEncodingException, IOException {
        if (fromFile.renameTo(toFile)) {
            Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                "standard Java File.renameTo OK !");
            return true;
        }

         Logger.getLogger(BufferedRW.class.getName()).log(Level.WARNING,
                "standard Java File.renameTo FAILED !");

        // delete if copy was successful, otherwise move will fail
        if (copy(fromFile, toFile, charsetName)) {
            Logger.getLogger(BufferedRW.class.getName()).log(Level.INFO,
                "moveNFSProof OK !");
            
            return fromFile.delete();
        }

        Logger.getLogger(BufferedRW.class.getName()).log(Level.SEVERE,
                "moveNFSProof FAILED !");

        return false;
    }

    public static void validateDirectory(File aDirectory)
            throws FileNotFoundException {
        if (aDirectory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!aDirectory.exists()) {
            throw new FileNotFoundException("Directory does not exist: "
                    + aDirectory);
        }
        if (!aDirectory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: "
                    + aDirectory);
        }
        if (!aDirectory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: "
                    + aDirectory);
        }
    }

    /**
     *
     * return last group matching (.+).(.+) pattern = part after the dot
     *
     * @param filename
     * @return
     */
    public static String extension(String filename) {

        Matcher matcher = PATTERN.matcher(filename);


        String ext = null;

        if (matcher.find()) {
            //last group
            int last = matcher.groupCount();
            ext = matcher.group(last);
        }

        return ext;


    }

    /**
     * Generate MD5 hash for the given String. MD5 is kind of an one-way encryption. Very useful for
     * hashing passwords before saving in database.
     * @param string The String to generate the MD5 hash for.
     * @return The 32-char hexadecimal MD5 hash of the given String, if necessary padded left with
     * one zero.
     */
    public static String hashMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            // Unexpected exception. "MD5" is just hardcoded and supported.
            throw new RuntimeException("MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            // Unexpected exception. "UTF-8" is just hardcoded and supported.
            throw new RuntimeException("UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xff));
        }
        return hex.toString();
    }
}
