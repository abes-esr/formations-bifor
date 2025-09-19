/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.formation.web.components;

/**
 *
 * @author jean-laurent
 */
public class File {

    private String Name;
    private String mime;
    private long length;
    private byte[] data;
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
        int extDot = name.lastIndexOf('.');
        if(extDot > 0){
            String extension = name.substring(extDot +1);
            if("doc".equals(extension)){
                mime="application/msword";
            } else if("pdf".equals(extension)){
                mime="application/pdf";
            } else {
                mime = "image/unknown";
            }
        }
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }

    public String getMime(){
        return mime;
    }
}
