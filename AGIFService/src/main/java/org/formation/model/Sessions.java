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
public class Sessions implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idsessions;
    private String titre;
    private Integer nbpersonnemax;
    private Date datedebut;
    private Date datefin;
    private Date datecloture;
    private Integer duree;
    private String etat;
    private List<Inscription> inscriptionList;
    private List<Inscription> inscriptionList2;
    private Formateur idformateur;
    private Lieu idlieu;
    private Statut idstatut;    
    private TypeFormation idtypeformation;
    private TypeSession idtypesession;
    private Date dateconfirmation;
    private Double nbJours;

    public Sessions() {
    }

    public Sessions(Integer idsessions) {
        this.idsessions = idsessions;
    }

    public Sessions(Integer idsessions, String titre) {
        this.idsessions = idsessions;
        this.titre = titre;
    }

    public Integer getIdsessions() {
        return idsessions;
    }

    public void setIdsessions(Integer idsessions) {
        this.idsessions = idsessions;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getNbpersonnemax() {
        return nbpersonnemax;
    }

    public void setNbpersonnemax(Integer nbpersonnemax) {
        this.nbpersonnemax = nbpersonnemax;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public Date getDatefin() {
        return datefin;
    }

    public void setDatefin(Date datefin) {
        this.datefin = datefin;
    }

    public Date getDatecloture() {
        return datecloture;
    }

    public void setDatecloture(Date datecloture) {
        this.datecloture = datecloture;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Double getNbJours() {
        return nbJours;
    }

    public void setNbJours(Double nbJours) {
        this.nbJours = nbJours;
    }
    

    public List<Inscription> getInscriptionList() {
        return inscriptionList;
    }

    public void setInscriptionList(List<Inscription> inscriptionList) {
        this.inscriptionList = inscriptionList;
    }

    public void addInscription(Inscription i) {
        if (inscriptionList == null) {
            inscriptionList = new ArrayList<Inscription>();
        }
        inscriptionList.add(i);
        i.setIdsessions1(this);
    }

    public List<Inscription> getInscriptionList2() {
        return inscriptionList2;
    }

    public void setInscriptionList2(List<Inscription> inscriptionList2) {
        this.inscriptionList2 = inscriptionList2;
    }

    public void addInscription2(Inscription i) {
        if (inscriptionList2 == null) {
            inscriptionList2 = new ArrayList<Inscription>();
        }
        inscriptionList2.add(i);
        i.setIdsessions2(this);
    }

    public Formateur getIdformateur() {
        return idformateur;
    }

    public void setIdformateur(Formateur idformateur) {
        this.idformateur = idformateur;
    }

    public Lieu getIdlieu() {
        return idlieu;
    }

    public void setIdlieu(Lieu idlieu) {
        this.idlieu = idlieu;
    }

    public Statut getIdstatut() {
        return idstatut;
    }

    public void setIdstatut(Statut idstatut) {
        this.idstatut = idstatut;
    }

    public TypeFormation getIdtypeformation() {
        return idtypeformation;
    }

    public void setIdtypeformation(TypeFormation idtypeformation) {
        this.idtypeformation = idtypeformation;
    }

    public TypeSession getIdtypesession() {
        return idtypesession;
    }

    public void setIdtypesession(TypeSession idtypesession) {
        this.idtypesession = idtypesession;
    }

    public Date getDateconfirmation() {
        return dateconfirmation;
    }

    public void setDateconfirmation(Date dateconfirmation) {
        this.dateconfirmation = dateconfirmation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsessions != null ? idsessions.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sessions)) {
            return false;
        }
        Sessions other = (Sessions) object;
        if ((this.idsessions == null && other.idsessions != null) || (this.idsessions != null && !this.idsessions.equals(other.idsessions))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Sessions[idsessions=" + idsessions + "]";
        return "[" + idsessions + "]";
    }
}
