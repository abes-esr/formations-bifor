/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import org.formation.model.Formateur;
import org.formation.service.AgifManageService;

/**
 *
 * @author jean-laurent
 */
public class EditFormateurBean {

    // Service metier
    private AgifManageService service;
    // Injections
    private ListFormateursBean listFormateurs;
    // Inscription en edition
    private Formateur formateur ;
    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {
         // recuperation de du formateur en cours
        formateur = listFormateurs.getFormateur();
        if (formateur == null) {
            formateur = new Formateur();
        }
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

    public ListFormateursBean getListFormateurs() {
        return listFormateurs;
    }

    public void setListFormateurs(ListFormateursBean listFormateurs) {
        this.listFormateurs = listFormateurs;
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
        String msg = "Le formateur  a bien été enregistré";

        // validation de l'inscription
        if (service.saveFormateur(formateur)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'enregistrement du formateur ";
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
