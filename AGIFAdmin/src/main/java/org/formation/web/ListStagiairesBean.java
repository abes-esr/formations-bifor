/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import org.formation.model.Stagiaire;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ListStagiairesBean {
    // Service metier
    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<Stagiaire> stagiairesItems = new ArrayList<Stagiaire>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private Stagiaire stagiaire;

     @PostConstruct
    public void init() {
       initialisation();
    }

     public void initialisation() {
        stagiairesItems = service.listStagiaires();
     }


    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public Stagiaire getStagiaire() {
        return stagiaire;
    }

    public void setStagiaire(Stagiaire stagiaire) {
        this.stagiaire = stagiaire;
    }

    public List<Stagiaire> getStagiairesItems() {
        return stagiairesItems;
    }

    public void setStagiairesItems(List<Stagiaire> stagiairesItems) {
        this.stagiairesItems = stagiairesItems;
    }

   

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String modify() {
        // suppression du stagiaire en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editStagiaireBean");
        // recuperation du stagiaire à modifier
       stagiaire = (Stagiaire) dataTable.getRowData();
        return "show";
    }

    public String delete() {

        // recuperation du formateur à supprimer
        stagiaire = (Stagiaire) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Le stagiaire " + stagiaire.toString() + " a été supprimé";
        if (service.deleteStagiaire(stagiaire)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la suppression du stagiaire " + stagiaire.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);
       
        return "success";
    }
}
