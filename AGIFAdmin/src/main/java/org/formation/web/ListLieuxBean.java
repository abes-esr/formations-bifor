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
import org.formation.model.Lieu;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ListLieuxBean {
    // Service metier

    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<Lieu> lieuItems = new ArrayList<Lieu>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private Lieu lieu;

    @PostConstruct
    public void init() {
        initialisation();
    }

    public void initialisation() {
        lieuItems = service.listLieux();
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

    public List<Lieu> getLieuItems() {
        return lieuItems;
    }

    public void setLieuItems(List<Lieu> lieuItems) {
        this.lieuItems = lieuItems;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String modify() {
         // suppression du lieu en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editLieuBean");

        // recuperation du lieu à modifier
        lieu = (Lieu) dataTable.getRowData();
        return "show";
    }

     public String create() {
         // suppression du lieu en session
         this.lieu = null;
        return "new";
    }


    public String delete() {

        // recuperation du lieu à supprimer
        lieu = (Lieu) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Le lieu " + lieu.toString() + " a été supprimé";
        if (service.deleteLieu(lieu)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des lieux
            initialisation();
        } else {
            msg = "Erreur dans la suppression du lieu " + lieu.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }
}
