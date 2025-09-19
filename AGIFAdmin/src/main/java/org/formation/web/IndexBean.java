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
public class IndexBean {

    // champ d'affichage
    private String redirection;


    @PostConstruct
    public void init() {
       //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listSessionsBean");
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listInscriptionsBean");
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editInscriptionBean");
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("confirmInscriptionBean");
       

       redirection ="faces/liste-sessions.xhtml";
       
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }

    
}
