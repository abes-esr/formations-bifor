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
public class TypeFormation implements Serializable {

    private static final long serialVersionUID = 1L;
    private Short idtypeformation;
    private String nom;
    private String description;
    private List<Question> questionList;
    private List<Sessions> sessionsList;

    public TypeFormation() {
    }

    public TypeFormation(Short idtypeformation) {
        this.idtypeformation = idtypeformation;
    }

    public Short getIdtypeformation() {
        return idtypeformation;
    }

    public void setIdtypeformation(Short idtypeformation) {
        this.idtypeformation = idtypeformation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
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
        s.setIdtypeformation(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtypeformation != null ? idtypeformation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TypeFormation)) {
            return false;
        }
        TypeFormation other = (TypeFormation) object;
        if ((this.idtypeformation == null && other.idtypeformation != null) || (this.idtypeformation != null && !this.idtypeformation.equals(other.idtypeformation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Typeformation[idtypeformation=" + idtypeformation + "]";
        return "[" + idtypeformation + "]";
    }
}
