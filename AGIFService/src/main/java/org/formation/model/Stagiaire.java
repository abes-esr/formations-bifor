/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.model;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jean-laurent
 */
public class Stagiaire implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer idstagiaire;
    private String civilite;
    private String nom;
    private String prenom;
    private String fonction;
    private String etablissement;
    private String numrcr;
    private String adresse;
    private String adresse2;
    private String adresse3;
    private String service;
    private String codepostal;
    private String ville;
    private String pays;
    private String telephone;
    private String mail;
    private String confirmMail;
    private String mailcoordinateur;

    private List<Inscription> inscriptionList;

    public Stagiaire() {
    }

    public Stagiaire(Integer idstagiaire) {
        this.idstagiaire = idstagiaire;
    }

    public Stagiaire(Integer idstagiaire, String civilite, String nom, String prenom, String adresse, String codepostal, String ville, String mail) {
        this.idstagiaire = idstagiaire;
        this.civilite = civilite;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.codepostal = codepostal;
        this.ville = ville;
        this.mail = mail;
    }

    public Integer getIdstagiaire() {
        return idstagiaire;
    }

    public void setIdstagiaire(Integer idstagiaire) {
        this.idstagiaire = idstagiaire;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
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

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getNumrcr() {
        return numrcr;
    }

    public void setNumrcr(String numrcr) {
        this.numrcr = numrcr;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
   

    public String getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }


    public void setMail(String mail) { this.mail = mail; }
    public String getConfirmMail() { return confirmMail; }
    public void setConfirmMail(String confirmMail) { this.confirmMail = confirmMail; }

    public String getMailcoordinateur() {
        return mailcoordinateur;
    }
    
    public List<String> getArrayOfCoordinateur() {
    	List<String> coordinateurs = new ArrayList<>();
        String[] mailsBySemiColon = this.mailcoordinateur.split(";");
            
        for (String mailBySemiColon : mailsBySemiColon) {
        	String[] mailsBySemiComma = mailBySemiColon.split(",");
        	for (String mailBySemiComma : mailsBySemiComma) {
        		coordinateurs.add(mailBySemiComma);
        	}
        }
        
        return coordinateurs;
    }

    public void setMailcoordinateur(String mailcoordinateur) {
        this.mailcoordinateur = mailcoordinateur;
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
        i.setIdstagiaire(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idstagiaire != null ? idstagiaire.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Stagiaire)) {
            return false;
        }
        Stagiaire other = (Stagiaire) object;
        if ((this.idstagiaire == null && other.idstagiaire != null) || (this.idstagiaire != null && !this.idstagiaire.equals(other.idstagiaire))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
       // return "org.formation.model.Stagiaire[idstagiaire=" + idstagiaire + "]";
        return "[" + idstagiaire + "]";
    }
}
