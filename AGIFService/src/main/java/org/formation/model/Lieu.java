/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jean-laurent
 */
public class Lieu implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idlieu;
    private String nom;
    private String adresse;
    private String adresse2;
    private String adresse3;
    private String ville;
    private String codepostal;
    private String pays;
    private List<Sessions> sessionsList;

    public Lieu() {
    }

    public Lieu(BigDecimal idlieu) {
        this.idlieu = idlieu;
    }

    public BigDecimal getIdlieu() {
        return idlieu;
    }

    public void setIdlieu(BigDecimal idlieu) {
        this.idlieu = idlieu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getAdresse3() {
        return adresse3;
    }

    public void setAdresse3(String adresse3) {
        this.adresse3 = adresse3;
    }

    
    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public List<Sessions> getSessionsList() {
        return sessionsList;
    }

    public void setSessionsList(List<Sessions> sessionsList) {
        this.sessionsList = sessionsList;
    }

    public void addSessions(Sessions s) {
        if (sessionsList == null) {
            sessionsList = new ArrayList<Sessions>();
        }
        sessionsList.add(s);
        s.setIdlieu(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idlieu != null ? idlieu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Lieu)) {
            return false;
        }
        Lieu other = (Lieu) object;
        if ((this.idlieu == null && other.idlieu != null) || (this.idlieu != null && !this.idlieu.equals(other.idlieu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // return "org.formation.model.Lieu[idlieu=" + idlieu + "]";
        return "[" + idlieu + "]";
    }
}
