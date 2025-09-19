/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.formation.model.Inscription;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="listInscriptionsMonCompteBean")
@SessionScoped
public class ListInscriptionsMonCompteBean {

    // Service metier
    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{stagiaireBean}")
    private StagiaireBean stagiaire;
    
     // champ d'affichage
    private List<Inscription> inscriptionItems = new ArrayList<>();
    
     // Binding sur la data table + item selectionne
    private UIData dataTable;
    
    @PostConstruct
    public void init() {
        inscriptionItems = service.listInscription(stagiaire.getStagiaire());
    }

    public List<Inscription> getInscriptionItems() {
        return inscriptionItems;
    }

    public void setInscriptionItems(List<Inscription> inscriptionItems) {
        this.inscriptionItems = inscriptionItems;
    }

    public StagiaireBean getStagiaire() {
        return stagiaire;
    }

    public void setStagiaire(StagiaireBean stagiaire) {
        this.stagiaire = stagiaire;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

   

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }
    
    public String export() {
        try {
            Inscription inscription = (Inscription) dataTable.getRowData();
            inscription.setIdstagiaire(stagiaire.getStagiaire());
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response = configureResponse(response, "Export_Inscription_" + inscription.getIdinscription());

            // création du document.
            ByteArrayOutputStream baos = service.exportInscription(inscription);
            // préparation de la reponse
            response.setContentLength(baos.size());

            // retour du ByteArrayOutputStream en ServletOutputStream
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            baos.flush();

            context.responseComplete();
        }
        catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            String msg = "Erreur dans la génération du PDF : " + e.getMessage();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            return "error";
        }

        return null;

    }

    private HttpServletResponse configureResponse(HttpServletResponse response, String fileName) {

        response.setHeader("Expires", "0");

        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");

        response.setHeader("Pragma", "public");

        response.setContentType("application/pdf");

        response.addHeader("Content-disposition", "attachment; filename=\"" + fileName + ".pdf\"");

        return response;

    }

   
    
}
