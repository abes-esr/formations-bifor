/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import org.formation.model.TypeFormation;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name = "listFormationsBean")
@RequestScoped
public class ListFormationsBean {

    // Service metier
    private AgifService service = new AgifServiceImpl();
    
    @ManagedProperty(value="#{demande}")
    private DemandeBean demande;
    
    private String clean;
    // Liste des formations dans le catalogue
    private List<TypeFormation> typeFormations;
    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {
        typeFormations = service.listTypeFormation();
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

    public List<TypeFormation> getTypeFormations() {
        
        return typeFormations;
    }

    public void setTypeFormations(List<TypeFormation> typeFormations) {
        this.typeFormations = typeFormations;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public String getClean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return clean;
    }

    public void setClean(String clean) {
        this.clean = clean;
    }

    /**
     * Selection de la formation pour inscription
     */
    public String register() {
        // suppression de tous les bean
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("demandeBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionSessionsBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionStagiaireBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionQuestionsBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("inscriptionValidationBean");

        demande.setTypeFormation((TypeFormation) dataTable.getRowData());
        return "success";
    }
}
