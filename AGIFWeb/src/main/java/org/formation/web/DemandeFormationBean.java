/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.formation.model.TypeFormation;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="demandeFormationBean")
@SessionScoped
public class DemandeFormationBean {
    // Service metier

    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{demande}")
    private DemandeBean demande;
    //champ du bean
    private String redirection;

    @PostConstruct
    public void init() {
        // suppression du contexte de l'inscription en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionSessionsBean");
        redirection = "inscription-sessions.xhtml";
        // en cas d'erreur on redirige sur la page de liste des formations
        if ( getServletRequest() ==  false ) {
            redirection="liste-formations.xhtml";
        }
        

    }

    private boolean getServletRequest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().getRequest();
        if (request == null) {
            System.out.println("getServletRequest :"
                    + " probleme de recuperation la requete");
            return false;
        }
        // on recupère l'identifiant de la formation
        String idformation = request.getParameter("idformation");
        // si l'identifiant de formation est nul
        if (idformation == null) {
            System.out.println("getServletRequest :"
                    + " probleme de recuperation de l'idformation");
            return false;
        }
        TypeFormation tf = service.getTypeFormationById(Short.parseShort(idformation));
        // si on n'a pas recuperer de type de formation
        if (tf == null) {
            System.out.println("getServletRequest :"
                    + " probleme de recuperation du type de formation");
            return false;
        }
        // on affecte le type de formation à la demande
        demande.setTypeFormation(tf);
        return true;
    }

    public DemandeBean getDemande() {
        return demande;
    }

    public void setDemande(DemandeBean demande) {
        this.demande = demande;
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }
}
