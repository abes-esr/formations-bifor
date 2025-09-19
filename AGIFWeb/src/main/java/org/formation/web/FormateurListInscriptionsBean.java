/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.formation.constant.Constant;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.web.components.FormateurManageSessions;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jean-laurent
 */

@ManagedBean(name="formateurListInscriptionsBean")
@SessionScoped
public class FormateurListInscriptionsBean {
    // Service metier

    private AgifManageService service = new AgifManageServiceImpl();
    // Injections
    @ManagedProperty(value="#{formateurListSessionsBean}")
    private FormateurListSessionsBean listSessions;
    // Sessions en edition
    private FormateurManageSessions FormateurManageSessions;
    // champ d'affichage
    private List<Inscription> inscriptionItems = new ArrayList<Inscription>();
    private Integer nbPlacesDisponibles = 0;
    private String valueEtat;
    private String lienConvocation;
    private String lienEmargement;
    private String lienAttestation;
    private String lienListeStagiaires;
    private String lienChevalets;
    // Binding sur la data table + item selectionne
    private Inscription inscription;
    private UIData dataTable;
    private HtmlOutputText outputEtat;

    @PostConstruct
    public void init() {
    	Logger.getLogger(FormateurListInscriptionsBean.class.getName()).log(Level.SEVERE,
                "debut du log");

        getServletRequest();
    	Logger.getLogger(FormateurListInscriptionsBean.class.getName()).log(Level.SEVERE,
                "apres getServletRequest");

        initialisation();
    	Logger.getLogger(FormateurListInscriptionsBean.class.getName()).log(Level.SEVERE,
                "apres initialisation");

    }

    private void getServletRequest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
        if (request != null) {
            String idsession = request.getParameter("idsessions");
            if (idsession != null) {
                Sessions s = service.getSessionsById(Integer.parseInt(idsession));
                FacesContext context = FacesContext.getCurrentInstance();
                listSessions = (FormateurListSessionsBean) context.getExternalContext().
                        getSessionMap().get("formateurListSessionsBean");
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
                String intitule = s.getIdlieu().getNom() + " [" + s.getTitre() + ", "
                        + " du "
                        + simpleFormat.format(s.getDatedebut())
                        + " au "
                        + simpleFormat.format(s.getDatefin())
                        + "] ";
                int nbInscrits = 0;
                listSessions.setSession(new FormateurManageSessions(intitule, nbInscrits, s));
            }
        }
    }

    private void initialisation() {
        FormateurManageSessions = listSessions.getSession();
        inscriptionItems = service.listInscription((Sessions) FormateurManageSessions);
        
        // calcul du nombre d'inscrits
        int nbInscrits = 0;
        for (Inscription i : inscriptionItems) {
            if (Constant.VALIDE.equals(i.getEtat())
                    || Constant.VALIDE.equals(i.getEtat2())) {
                nbInscrits++;
            }
        }
        
        this.nbPlacesDisponibles = FormateurManageSessions.getNbpersonnemax() - nbInscrits;
    }

    public FormateurListSessionsBean getListSessions() {
        return listSessions;
    }

    public void setListSessions(FormateurListSessionsBean listSessions) {
        this.listSessions = listSessions;
    }

    public FormateurManageSessions getFormateurManageSessions() {
        return FormateurManageSessions;
    }

    public void setFormateurManageSessions(FormateurManageSessions FormateurManageSessions) {
        this.FormateurManageSessions = FormateurManageSessions;
    }

    public List<Inscription> getInscriptionItems() {
        return inscriptionItems;
    }

    public void setInscriptionItems(List<Inscription> inscriptionItems) {
        this.inscriptionItems = inscriptionItems;
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

    public Integer getNbPlacesDisponibles() {
        return nbPlacesDisponibles;
    }

    public void setNbPlacesDisponibles(Integer nbPlacesDisponibles) {
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }

    public HtmlOutputText getOutputEtat() {
        return outputEtat;
    }

    public void setOutputEtat(HtmlOutputText outputEtat) {
        this.outputEtat = outputEtat;
    }

    public String getValueEtat() {
        return valueEtat;
    }

    public void setValueEtat(String valueEtat) {
        this.valueEtat = valueEtat;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public String getLienAttestation() {
        return lienAttestation;
    }

    public void setLienAttestation(String lienAttestation) {
        this.lienAttestation = lienAttestation;
    }

    public String getLienConvocation() {
        return lienConvocation;
    }

    public void setLienConvocation(String lienConvocation) {
        this.lienConvocation = lienConvocation;
    }

    public String getLienEmargement() {
        return lienEmargement;
    }

    public void setLienEmargement(String lienEmargement) {
        this.lienEmargement = lienEmargement;
    }

    public String getLienListeStagiaires() {
        return lienListeStagiaires;
    }

    public void setLienListeStagiaires(String lienListeStagiaires) {
        this.lienListeStagiaires = lienListeStagiaires;
    }

    public String getLienChevalets() {
        return lienChevalets;
    }

    public void setLienChevalets(String lienChevalets) {
        this.lienChevalets = lienChevalets;
    }


    /**
     * liste des sessions
     */
    public String back() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "back";
    }

	
}
