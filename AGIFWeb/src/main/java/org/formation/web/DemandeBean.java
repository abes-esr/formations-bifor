/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;
import org.formation.web.components.Reponses;

/**
 *
 * @author jean-laurent
 */
/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="demande")
@SessionScoped
public class DemandeBean {

    private int identifiant = System.identityHashCode(this);
    private Stagiaire stagiaire = new Stagiaire(System.identityHashCode(this));
    private TypeFormation typeFormation;
    private Sessions choixPrioritaire;
    private Sessions choixSecondaire;
    private List<Reponses> reponses = new ArrayList<Reponses>();
    private boolean estConnecte = false;

    public int getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(int identifiant) {
        this.identifiant = identifiant;
    }

    public List<Reponses> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponses> reponses) {
        this.reponses = new ArrayList<Reponses>(reponses);
    }

    public Sessions getChoixPrioritaire() {
        return choixPrioritaire;
    }

    public void setChoixPrioritaire(Sessions choixPrioritaire) {
        this.choixPrioritaire = choixPrioritaire;
    }

    public Sessions getChoixSecondaire() {
        return choixSecondaire;
    }

    public void setChoixSecondaire(Sessions choixSecondaire) {
        this.choixSecondaire = choixSecondaire;
    }

    /**
     * Get the value of stagiaire
     *
     * @return the value of stagiaire
     */
    public Stagiaire getStagiaire() {
        return stagiaire;
    }

    /**
     * Set the value of stagiaire
     *
     * @param stagiaire new value of stagiaire
     */
    public void setStagiaire(Stagiaire stagiaire) {
        this.stagiaire = stagiaire;
    }

    /**
     * Get the value of typeformation
     *
     * @return the value of typeformation
     */
    public TypeFormation getTypeFormation() {
        return typeFormation;
    }

    /**
     * Set the value of typeformation
     *
     * @param typeformation new value of typeformation
     */
    public void setTypeFormation(TypeFormation typeFormation) {
        this.typeFormation = typeFormation;
    }

    /**
     * Get the value of estConnecte
     *
     * @return the value of estConnecte
     */
    public boolean isEstConnecte() {
        return estConnecte;
    }

    /**
     * Set the value of estConnecte
     *
     * @param estConnecte new value of estConnecte
     */
    public void setEstConnecte(boolean estConnecte) {
        this.estConnecte = estConnecte;
    }
}
