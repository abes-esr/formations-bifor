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
public class Statut implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idstatut;
    private String nom;
    private List<Sessions> sessionsList;

    public Statut() {
    }

    public Statut(Integer idstatut) {
        this.idstatut = idstatut;
    }

    public Integer getIdstatut() {
        return idstatut;
    }

    public void setIdstatut(Integer idstatut) {
        this.idstatut = idstatut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
        s.setIdstatut(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idstatut != null ? idstatut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Statut)) {
            return false;
        }
        Statut other = (Statut) object;
        if ((this.idstatut == null && other.idstatut != null) || (this.idstatut != null && !this.idstatut.equals(other.idstatut))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Statut[idstatut=" + idstatut + "]";
        return "[" + idstatut + "]";
    }
}
