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
public class TypeSession implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idtypesession;
    private String nom;
    private List<Sessions> sessionsList;

    public TypeSession() {
    }

    public TypeSession(Integer idtypesession) {
        this.idtypesession = idtypesession;
    }

    public Integer getIdtypesession() {
        return idtypesession;
    }

    public void setIdtypesession(Integer idtypesession) {
        this.idtypesession = idtypesession;
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
        s.setIdtypesession(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtypesession != null ? idtypesession.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TypeSession)) {
            return false;
        }
        TypeSession other = (TypeSession) object;
        if ((this.idtypesession == null && other.idtypesession != null) || (this.idtypesession != null && !this.idtypesession.equals(other.idtypesession))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Typesession[idtypesession=" + idtypesession + "]";
        return "[" + idtypesession + "]";
    }
}
