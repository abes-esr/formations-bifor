/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import org.formation.model.TypeFormation;
import org.formation.service.AgifManageService;

/**
 *
 * @author jean-laurent
 */
public class EditFormationBean {

    // Service metier
    private AgifManageService service;
    // Injections
    private ListFormationsBean listFormations;
    // Inscription en edition
    private TypeFormation formation ;
    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {
         // recuperation de du formateur en cours
        formation = listFormations.getFormation();
        if (formation == null) {
            formation = new TypeFormation();
        }
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public TypeFormation getFormation() {
        return formation;
    }

    public void setFormation(TypeFormation formation) {
        this.formation = formation;
    }

    public ListFormationsBean getListFormations() {
        return listFormations;
    }

    public void setListFormations(ListFormationsBean listFormations) {
        this.listFormations = listFormations;
    }

   

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String save() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La formation a bien été enregistré";

        // validation de l'inscription
        if (service.saveTypeFormatation(formation)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'enregistrement de la formation ";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "success";
    }

    public String back() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "back";
    }
}
