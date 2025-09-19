/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.formation.model.Stagiaire;

/**
 *
 * @author jean-laurent
 */
/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="stagiaireBean")
@SessionScoped
public class StagiaireBean {

    private Stagiaire stagiaire = new Stagiaire(System.identityHashCode(this));
    private boolean estConnecte = false;

   
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
    
    /**
     * Logout function for the stagiaire
     *
     */
    public String logout() {
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        ((HttpSession) context.getExternalContext().getSession(true)).invalidate();
        String msg = "Deconnexion effectuée";
         message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        msg, msg);
         context.addMessage(null, message);
        return "logout";
    }
}
