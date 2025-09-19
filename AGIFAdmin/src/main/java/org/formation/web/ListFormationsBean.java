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
import org.formation.model.TypeFormation;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ListFormationsBean {
    // Service metier

    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<TypeFormation> formationItems = new ArrayList<TypeFormation>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private TypeFormation formation;

    @PostConstruct
    public void init() {
        initialisation();
    }

    public void initialisation() {
        formationItems = service.listTypeFormations();
    }

    public TypeFormation getFormation() {
        return formation;
    }

    public void setFormation(TypeFormation formation) {
        this.formation = formation;
    }

    public List<TypeFormation> getFormationItems() {
        return formationItems;
    }

    public void setFormationItems(List<TypeFormation> formationItems) {
        this.formationItems = formationItems;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String modify() {
        // suppression de la formation en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editFormationBean");

        // recuperation du formateur à modifier
        formation = (TypeFormation) dataTable.getRowData();
        return "show";
    }

    /**
     * Creation d'un nouveau type de formation
     */
    public String create() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "new";
    }

    public String delete() {

        // recuperation du formateur à supprimer
        formation = (TypeFormation) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La formation " + formation.toString() + " a été supprimé";
        if (service.deleteTypeFormation(formation)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la suppression de la formation " + formation.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }
}
