/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jean-laurent
 */
public class Reponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idreponse;
    private String choix;
    private String specification;
    private Date datereponse;
    private Inscription idinscription;
    private Question idquestion;

    public Reponse() {
    }

    public Reponse(Integer idreponse) {
        this.idreponse = idreponse;
    }

    public Integer getIdreponse() {
        return idreponse;
    }

    public void setIdreponse(Integer idreponse) {
        this.idreponse = idreponse;
    }

    public String getChoix() {
        return choix;
    }

    public void setChoix(String choix) {
        this.choix = choix;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Date getDatereponse() {
        return datereponse;
    }

    public void setDatereponse(Date datereponse) {
        this.datereponse = datereponse;
    }

    public Inscription getIdinscription() {
        return idinscription;
    }

    public void setIdinscription(Inscription idinscription) {
        this.idinscription = idinscription;
    }

    public Question getIdquestion() {
        return idquestion;
    }

    public void setIdquestion(Question idquestion) {
        this.idquestion = idquestion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idreponse != null ? idreponse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reponse)) {
            return false;
        }
        Reponse other = (Reponse) object;
        if ((this.idreponse == null && other.idreponse != null) || (this.idreponse != null && !this.idreponse.equals(other.idreponse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Reponse[idreponse=" + idreponse + "]";
        return "[" + idreponse + "]";
    }
}
