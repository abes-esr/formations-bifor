/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.formation.model.Stagiaire;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="connexionMonCompteBean")
@SessionScoped
public class ConnexionMonCompteBean extends AbstractBackingBean {
	private static final long serialVersionUID = -5289109682610273075L;
	
	// Service metier
    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{stagiaireBean}")
    private StagiaireBean stagiaire;
    // Attributs
    private int nbTentatives = 3;

    /**
     * Get the value of nbTentatives
     *
     * @return the value of nbTentatives
     */
    public int getNbTentatives() {
        return nbTentatives;
    }

    /**
     * Set the value of nbTentatives
     *
     * @param nbTentatives new value of nbTentatives
     */
    public void setNbTentatives(int nbTentatives) {
        this.nbTentatives = nbTentatives;
    }

    @PostConstruct
    public void init() {
    }

    public StagiaireBean getStagiaire() {
        return stagiaire;
    }

    public void setStagiaire(StagiaireBean stagiaire) {
        this.stagiaire = stagiaire;
    }
    

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }

    public String back() {
        return "back";
    }
     public String login() {
        nbTentatives=3;
         
        return "login";
    }

    /**
     * Gestion de l'affichage de la zone d'information
     * Cette methode n'est accedée qu'après la phase de validation
     */
    public void passwordChanged(ValueChangeEvent e) {
        if (nbTentatives>0) {
            nbTentatives--;
        }
    }

    public String loginStagiaire() {
    	
    	return "navig faces : à modifier pour formateur";
        // declaration d'un message
//        FacesMessage message = new FacesMessage();
//        FacesContext context = FacesContext.getCurrentInstance();
//        String msg = "Votre authentification a échouée. Merci de vérifier vos identifiants ";
//
//        // authetification de l'utilisateur
//        Stagiaire s = service.getStagiaireByLogin(stagiaire.getStagiaire().getIdentifiant(),
//                stagiaire.getStagiaire().getMotdepasse());
//
//        // si le nombre de tentative est superieur a trois
//        if (nbTentatives < 1) {
//            nbTentatives=3;
//            return "forgot";
//        }
//        // si l'authentification a fonctionnée
//        if (s != null) {
//            stagiaire.setStagiaire(s);
//            msg = "Votre authentification a reussie";
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
//            context.addMessage(null, message);
//            stagiaire.setEstConnecte(true);
//            return "success";
//        }
//
//
//
//        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
//        context.addMessage(null, message);
//        return "failure";

    }
}
