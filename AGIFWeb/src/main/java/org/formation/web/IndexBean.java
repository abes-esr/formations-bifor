/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.formation.model.TypeFormation;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name = "indexBean")
@RequestScoped
public class IndexBean {

    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{demande}")
    private DemandeBean demande;
    // champ d'affichage
    private String redirection;


    @PostConstruct
    public void init() {
       //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("demandeBean");
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionSessionsBean");

       redirection ="faces/inscription-sessions.xhtml";
       getServletRequest();
       
    }
     private void getServletRequest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().getRequest();
        if (request != null) {
            // on vide la session courante
            // on recupère l'identifiant de la formation
            String idformation = request.getParameter("idformation");
            if (idformation != null) {
                TypeFormation tf = service.getTypeFormationById(Short.parseShort(idformation));
                if (tf != null) {
                    // on affecte le type de formation à la demande
                    demande.setTypeFormation(tf);
                }
            }
        }
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }

    public DemandeBean getDemande() {
        return demande;
    }

    public void setDemande(DemandeBean demande) {
        this.demande = demande;
    }

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }

    
    public String back() {
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
          return "back";
    }
    
}
