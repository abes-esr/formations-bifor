/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jean-laurent
 */
public class Inscription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer idinscription;
    private Date dateinscription;
    private String etat;
    private String etat2;
    private Sessions idsessions1;
    private Sessions idsessions2;
    private Stagiaire idstagiaire;
    private List<Reponse> reponseList;

    public Inscription() {
    }

    public Inscription(Integer idinscription) {
        this.idinscription = idinscription;
    }

    public Integer getIdinscription() {
        return idinscription;
    }

    public void setIdinscription(Integer idinscription) {
        this.idinscription = idinscription;
    }

    public Date getDateinscription() {
        return dateinscription;
    }

    public void setDateinscription(Date dateinscription) {
        this.dateinscription = dateinscription;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getEtat2() {
        return etat2;
    }

    public void setEtat2(String etat2) {
        this.etat2 = etat2;
    }

    public Sessions getIdsessions1() {
        return idsessions1;
    }

    public void setIdsessions1(Sessions idsessions1) {
        this.idsessions1 = idsessions1;
    }

    public Sessions getIdsessions2() {
        return idsessions2;
    }

    public void setIdsessions2(Sessions idsessions2) {
        this.idsessions2 = idsessions2;
    }

    public Stagiaire getIdstagiaire() {
        return idstagiaire;
    }

    public void setIdstagiaire(Stagiaire idstagiaire) {
        this.idstagiaire = idstagiaire;
    }

    public List<Reponse> getReponseList() {
        return reponseList;
    }

    public void setReponseList(List<Reponse> reponseList) {
        this.reponseList = reponseList;
    }

    public void addReponse(Reponse r) {
        if (reponseList == null) {
            reponseList = new ArrayList<Reponse>();
        }
        reponseList.add(r);
        r.setIdinscription(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idinscription != null ? idinscription.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Inscription)) {
            return false;
        }
        Inscription other = (Inscription) object;
        if ((this.idinscription == null && other.idinscription != null) || (this.idinscription != null && !this.idinscription.equals(other.idinscription))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + idinscription + "]";
    }
}
