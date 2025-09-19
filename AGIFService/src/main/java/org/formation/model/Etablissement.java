/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.formation.model;

import java.util.List;

/**
 *
 * @author jean-laurent
 */
public class Etablissement {
    private String library;
    private int iln;
    private String short_name;
    private String full_name;
    private String sub_name;
    private String address;
    private String address2;
    private String address3;
    private String postcode;
    private String city;
    private String telephone;
    private String email_address;
    private String fax_address;
    
    private List<Sessions> sessionsList;

    public Etablissement() {
           
    }
    public Etablissement(String name) {
            this.short_name=name;
            this.iln=0;
    }
    
    public Etablissement(int numero,String name) {
            this.short_name=name;
            this.iln=numero;
            this.library="Y";
            
    }
    public int getIln() {
        return iln;
    }

    public void setIln(int iln) {
        this.iln = iln;
    }

   
    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public List<Sessions> getSessionsList() {
        return sessionsList;
    }

    public void setSessionsList(List<Sessions> sessionsList) {
        this.sessionsList = sessionsList;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFax_address() {
        return fax_address;
    }

    public void setFax_address(String fax_address) {
        this.fax_address = fax_address;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }
    
}
