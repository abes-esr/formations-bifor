/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jean-laurent
 */
public class Formateur implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idformateur;
    private String nom;
    private String prenom;
    private List<Sessions> sessionsList;

    public Formateur() {
    }

    public Formateur(Integer idformateur) {
        this.idformateur = idformateur;
    }

    public Formateur(Integer idformateur, String nom) {
        this.idformateur = idformateur;
        this.nom = nom;
    }

    public Integer getIdformateur() {
        return idformateur;
    }

    public void setIdformateur(Integer idformateur) {
        this.idformateur = idformateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
        s.setIdformateur(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idformateur != null ? idformateur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Formateur)) {
            return false;
        }
        Formateur other = (Formateur) object;
        if ((this.idformateur == null && other.idformateur != null) || (this.idformateur != null && !this.idformateur.equals(other.idformateur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Formateur[idformateur=" + idformateur + "]";
        return "[" + idformateur + "]";
    }
}
