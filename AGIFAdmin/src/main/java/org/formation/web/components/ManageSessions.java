/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import org.formation.model.Sessions;
import java.io.File;
import org.formation.path.PathFile;

/**
 *
 * @author jean-laurent
 */
public class ManageSessions extends Sessions {
	private static final long serialVersionUID = 2088140882231070167L;
	
	private String intitule;
    private int nbInscrits;
    private File convocation;
    private File emargement;
    private File attestation;
    private File listeStagiaires;
    private File chevalets;
    
    private String linkConvocation;
    private String linkEmargement;
    private String linkAttestation;
    private String linklisteStagiaires;
    private String linkChevalets;

    public ManageSessions() {
    }

    public ManageSessions(String intitule, int nbInscrits, Sessions s) {
        this.intitule = intitule;
        this.nbInscrits = nbInscrits;
        this.setDatecloture(s.getDatecloture());
        this.setDateconfirmation(s.getDateconfirmation());
        this.setDatedebut(s.getDatedebut());
        this.setDatefin(s.getDatefin());
        this.setDuree(s.getDuree());
        this.setEtat(s.getEtat());
        this.setNbpersonnemax(s.getNbpersonnemax());
        this.setIdformateur(s.getIdformateur());
        this.setIdlieu(s.getIdlieu());
        this.setIdsessions(s.getIdsessions());
        this.setIdstatut(s.getIdstatut());
        this.setIdtypeformation(s.getIdtypeformation());
        this.setIdtypesession(s.getIdtypesession());
        this.setInscriptionList(s.getInscriptionList());
        this.setInscriptionList2(s.getInscriptionList2());
        this.setTitre(s.getTitre());
        this.setNbJours(s.getNbJours());
        
        this.setConvocation(new File(PathFile.CONVOCATION + s.getTitre() + ".rtf"));
        this.setListeStagiaires(new File(PathFile.LISTE_STAGIAIRES + s.getTitre() + ".rtf"));
        this.setEmargement(new File(PathFile.EMARGEMENT + s.getTitre() + ".rtf"));
        this.setAttestation(new File(PathFile.ATTESTATION + s.getTitre() + ".rtf"));
        this.setChevalets(new File(PathFile.CHEVALETS + s.getTitre() + ".rtf"));

        this.setLinkConvocation(PathFile.PATH_CONVOCATION+ s.getTitre() + ".rtf");
        this.setLinklisteStagiaires(PathFile.PATH_LISTE_STAGIAIRES + s.getTitre() + ".rtf");
        this.setLinkEmargement(PathFile.PATH_EMARGEMENT + s.getTitre() + ".rtf");
        this.setLinkAttestation(PathFile.PATH_ATTESTATION + s.getTitre() + ".rtf");
        this.setLinkChevalets(PathFile.PATH_CHEVALETS + s.getTitre() + ".rtf");
    }

    /**
     * Get the value of nbInscrits
     *
     * @return the value of nbInscrits
     */
    public int getNbInscrits() {
        return nbInscrits;
    }

    /**
     * Set the value of nbInscrits
     *
     * @param nbInscrits new value of nbInscrits
     */
    public void setNbInscrits(int nbInscrits) {
        this.nbInscrits = nbInscrits;
    }

    /**
     * Get the value of intitule
     *
     * @return the value of intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Set the value of intitule
     *
     * @param intitule new value of intitule
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public File getAttestation() {
        if (attestation.exists()) {
            return attestation;
        }
        return null;

    }

    public void setAttestation(File attestation) {
        this.attestation = attestation;
    }

    public File getConvocation() {
        if (convocation.exists()) {
            return convocation;
        }
        return null;
    }

    public void setConvocation(File convocation) {
        this.convocation = convocation;
    }

    public File getEmargement() {
        if (emargement.exists()) {
            return emargement;
        }
        return null;
    }

    public void setEmargement(File emargement) {
        this.emargement = emargement;
    }

    public String getLinkAttestation() {
        return linkAttestation;
    }

    public void setLinkAttestation(String linkAttestation) {
        this.linkAttestation = linkAttestation;
    }

    public String getLinkConvocation() {
        return linkConvocation;
    }

    public void setLinkConvocation(String linkConvocation) {
        this.linkConvocation = linkConvocation;
    }

    public String getLinkEmargement() {
        return linkEmargement;
    }

    public void setLinkEmargement(String linkEmargement) {
        this.linkEmargement = linkEmargement;
    }

    public String getLinklisteStagiaires() {
        return linklisteStagiaires;
    }

    public void setLinklisteStagiaires(String linklisteStagiaires) {
        this.linklisteStagiaires = linklisteStagiaires;
    }

    public File getListeStagiaires() {
        if (listeStagiaires.exists()) {
            return listeStagiaires;
        }
        return null;
    }

    public void setListeStagiaires(File listeStagiaires) {
        this.listeStagiaires = listeStagiaires;
    }

    public File getChevalets() {
       if (chevalets.exists()) {
            return chevalets;
        }
         return null;
    }

    public void setChevalets(File chevalets) {
        this.chevalets = chevalets;
    }
    
    
    public String getLinkChevalets() {
        return linkChevalets;
    }

    public void setLinkChevalets(String linkChevalets) {
        this.linkChevalets = linkChevalets;
    }
    
    


}
