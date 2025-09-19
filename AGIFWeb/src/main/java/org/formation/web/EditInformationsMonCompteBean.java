/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import org.formation.model.Etablissement;
import org.formation.model.Stagiaire;
import org.formation.service.AgifManageService;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.service.impl.AgifServiceImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="editInformationsMonCompteBean")
@SessionScoped
public class EditInformationsMonCompteBean extends AbstractBackingBean {
	private static final long serialVersionUID = -7553853666163928755L;
	
	// Service metier
    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{stagiaireBean}")
    private StagiaireBean stagiaire;
    // Regle d'affichage des champs
    private boolean renderedFonction = true;
    private boolean renderedRCR = true;
    private boolean renderedService = false;
    private boolean renderedCoordinateur = true;
    private String labelCoordinateur = "Adresse mail du correspondant catalogage (à défaut, mail du coordinateur)";
    // champs du formulaire
    private List<SelectItem> etablissementItems = new ArrayList<SelectItem>();
    private List<SelectItem> rcrItems = new ArrayList<SelectItem>();

    @PostConstruct
    public void init() {

        // recuperation de la liste des établissements
        List<Etablissement> etablissements = service.listEtablissements();
        etablissementItems.add(new SelectItem("", "--Selectionnez un établissement--"));
        String iln = "";
        for (Etablissement e : etablissements) {
            if (e.getIln() != 0) {
                iln = java.lang.String.valueOf(e.getIln()) + "-";
            }
            etablissementItems.add(new SelectItem(e.getShort_name(), iln + e.getShort_name()));
            iln = "";
        }
        // construction de la liste des RCR
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));

        if (stagiaire.getStagiaire().getEtablissement() != null) {
            //System.out.println("iln "+iln);
            List<Etablissement> rcrList = service.getRCRByName(stagiaire.getStagiaire().getEtablissement());

            for (Etablissement r : rcrList) {
                rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
            }
        }

    }

    public StagiaireBean getStagiaire() {
        return stagiaire;
    }

    public void setStagiaire(StagiaireBean stagiaire) {
        this.stagiaire = stagiaire;
    }

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }

    public String getLabelCoordinateur() {

        return labelCoordinateur;
    }

    public void setLabelCoordinateur(String labelCoordinateur) {
        this.labelCoordinateur = labelCoordinateur;
    }

    public boolean isRenderedFonction() {

        return renderedFonction;
    }

    public void setRenderedFonction(boolean renderedFonction) {

        this.renderedFonction = renderedFonction;
    }

    public boolean isRenderedRCR() {

        return renderedRCR;
    }

    public void setRenderedRCR(boolean renderedRCR) {
        this.renderedRCR = renderedRCR;
    }

    public boolean isRenderedService() {
        return renderedService;
    }

    public void setRenderedService(boolean renderedService) {
        this.renderedService = renderedService;
    }

    public List<SelectItem> getEtablissementItems() {


        return etablissementItems;
    }

    public void setEtablissementItems(List<SelectItem> etablissementItems) {
        this.etablissementItems = etablissementItems;
    }

    public boolean isRenderedCoordinateur() {

        return renderedCoordinateur;
    }

    public void setRenderedCoordinateur(boolean renderedCoordinateur) {
        this.renderedCoordinateur = renderedCoordinateur;
    }

    public List<SelectItem> getRcrItems() {
        return rcrItems;
    }

    public void setRcrItems(List<SelectItem> rcrItems) {
        this.rcrItems = rcrItems;
    }

    public String back() {
        return "back";
    }


    public String save() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        AgifManageService manageService = new AgifManageServiceImpl();
        String msg = "Modification des informations du compte effectuée ";
        Stagiaire s = stagiaire.getStagiaire();
        if (manageService.saveStagiaire(s) == false) {

            msg = "Impossible de modifier les informations du compte ";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    msg, msg);
            context.addMessage(null, message);
            return "failure";
        }
        stagiaire.setStagiaire(s);
        // declaration d'un message        
        message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                msg, msg);
        context.addMessage(null, message);
        return "success";
    }

    public void searchStagiaire() {
        // recuperation du stagiaire
        System.out.println(stagiaire.getStagiaire().getNom());
        System.out.println(stagiaire.getStagiaire().getPrenom());

        if (!(stagiaire.getStagiaire().getNom().equals("")
                && stagiaire.getStagiaire().getPrenom().equals(""))) {
            Stagiaire s = service.searchStagiaireByName(stagiaire.getStagiaire().getNom(),
                    stagiaire.getStagiaire().getPrenom());
            if (s != null) {
                stagiaire.setStagiaire(s);
            }
        }
    }

    public void changeEtablissement(ValueChangeEvent event) {
        this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        String name = event.getNewValue().toString();
        //System.out.println("iln "+iln);
        List<Etablissement> rcrList = service.getRCRByName(name);
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));

        for (Etablissement r : rcrList) {
            rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
        }
        // on reinitialise les champs adresses du stagiaires
        Stagiaire s = stagiaire.getStagiaire();
        s.setAdresse("");
        s.setAdresse2("");
        s.setCodepostal("");
        s.setVille("");
        stagiaire.setStagiaire(s);
    }

    public void changeRCR(ValueChangeEvent event) {
        //this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        String rcr = event.getNewValue().toString();
        //System.out.println("iln "+iln);
        Etablissement address = service.getAddressByRCR(rcr);

        Stagiaire s = stagiaire.getStagiaire();
        if (address != null) {
            s.setAdresse(address.getAddress());
            s.setAdresse2(address.getAddress2());
            s.setAdresse3(address.getAddress3());
            s.setCodepostal(address.getPostcode());
            s.setVille(address.getCity());
            //s.setTelephone((address.getTelephone()).replace(".", ""));

        } else {
            // on remet les champ à vide
            s.setAdresse("");
            s.setAdresse2("");
            s.setAdresse3("");
            s.setCodepostal("");
            s.setVille("");
        }
        stagiaire.setStagiaire(s);

    }
}
