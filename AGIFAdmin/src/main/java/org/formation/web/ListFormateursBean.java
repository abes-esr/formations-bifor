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
import org.formation.model.Formateur;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ListFormateursBean {
    // Service metier
    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<Formateur> formateurItems = new ArrayList<Formateur>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private Formateur formateur;

     @PostConstruct
    public void init() {
       initialisation();
    }

     public void initialisation() {
        formateurItems = service.listFormateurs();
     }


    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public List<Formateur> getFormateurItems() {
        return formateurItems;
    }

    public void setFormateurItems(List<Formateur> formateurItems) {
        this.formateurItems = formateurItems;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String modify() {
        // suppression du formateur en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editFormateurBean");
        // recuperation du formateur à modifier
       formateur = (Formateur) dataTable.getRowData();
        return "show";
    }

    public String delete() {

        // recuperation du formateur à supprimer
        formateur = (Formateur) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Le formateur " + formateur.toString() + " a été supprimé";
        if (service.deleteFormateur(formateur)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la suppression du formateur " + formateur.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);
       
        return "success";
    }
}
