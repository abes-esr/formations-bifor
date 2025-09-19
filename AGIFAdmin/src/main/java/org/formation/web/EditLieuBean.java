/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import org.formation.model.Lieu;
import org.formation.service.AgifManageService;

/**
 *
 * @author jean-laurent
 */
public class EditLieuBean {

    // Service metier
    private AgifManageService service;
    // Injections
    private ListLieuxBean listLieux;
    // Lieu en edition
    private Lieu lieu;
    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {
        // recuperation du lieu en cours
        lieu = listLieux.getLieu();
        if (lieu == null) {
            lieu = new Lieu();
        }
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public ListLieuxBean getListLieux() {
        return listLieux;
    }

    public void setListLieux(ListLieuxBean listLieux) {
        this.listLieux = listLieux;
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
        String msg = "Le lieu a bien été enregistré";
        // initlisation du pays a FRANCE pour le moment
        lieu.setPays("FRANCE");
        // validation de l'inscription
        if (service.saveLieu(lieu)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'enregistrement du lieu ";
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
