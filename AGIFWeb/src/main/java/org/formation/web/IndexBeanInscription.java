/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author jean-laurent
 */
public class IndexBeanInscription {

    // champ d'affichage
    private String redirection;

    @PostConstruct
    public void init() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("demandeBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionSessionsBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionStagiaireBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionQuestionsBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionValidationBean");

        redirection = "faces/liste-formations.xhtml";


    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }
}
