/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jean-laurent
 */
public class ValidInscriptionBean {
    // champ d'affichage

    private String redirection;

    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listInscriptionsBean");
        redirection = "faces/index.xhtml";
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().getRequest();
        if (request != null) {
            // on recupère l'identifiant de la session
            String idsession = request.getParameter("idsessions");
            if (idsession != null) {
                redirection = "faces/liste-inscriptions.xhtml?idsessions=" + idsession;
            }
        }
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }
}
