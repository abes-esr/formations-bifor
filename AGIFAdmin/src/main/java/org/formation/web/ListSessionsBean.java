/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.formation.model.Sessions;
import org.formation.model.Statut;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.web.components.ManageSessions;

/**
 *
 * @author jean-laurent
 */
public class ListSessionsBean {

    // Service metier
    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<ManageSessions> sessionsItems = new ArrayList<ManageSessions>();
    // champs de filtre
    private String filterStatut;
    private ArrayList<SelectItem> filterStatuts = new ArrayList<SelectItem>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private ManageSessions session;

    @PostConstruct
    public void init() {
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        initialisation();
    }

    private void initialisation() {
        sessionsItems = new ArrayList<ManageSessions>();

        List<Sessions> sessions = service.listSessions();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Sessions s : sessions) {
            String intitule = s.getIdlieu().getNom() + " [" + s.getTitre() + ", "
                    + " du "
                    + simpleFormat.format(s.getDatedebut())
                    + " au "
                    + simpleFormat.format(s.getDatefin())
                    + "] ";
            int nbInscrits = 0;
            nbInscrits = service.getCountInscriptionsBySession(s);

            sessionsItems.add(new ManageSessions(intitule, nbInscrits, s));
        }
        
          List<Statut> statuts = service.listStatuts();
         filterStatuts.add(new SelectItem("","Tous"));
        for (Statut st : statuts) {
            filterStatuts.add(new SelectItem(st.getNom(),
                    st.getNom()));
        }

    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public List<ManageSessions> getSessionsItems() {

        return sessionsItems;
    }

    public void setSessionsItems(List<ManageSessions> sessionsItems) {
        this.sessionsItems = sessionsItems;
    }

    public ManageSessions getSession() {

        return session;
    }

    public void setSession(ManageSessions session) {
        this.session = session;
    }

    public String getFilterStatut() {
        return filterStatut;
    }

    public void setFilterStatut(String filterStatut) {
        this.filterStatut = filterStatut;
    }

    public ArrayList<SelectItem> getFilterStatuts() {
        return filterStatuts;
    }

    public void setFilterStatuts(ArrayList<SelectItem> filterStatuts) {
        this.filterStatuts = filterStatuts;
    }

    /**
     * Selection de la session pour l'affichage des inscriptions
     */
    public String select() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listInscriptionsBean");
        //     FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        session = ((ManageSessions) dataTable.getRowData());
        return "show";
    }

    /**
     * Rafraichissement de la liste des sessions
     */
    public String refresh() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "refresh";
    }

    /**
     * Paiement de la session
     */
    public String paid() {
        // recuperation de la session a updateSessionsr
        session = ((ManageSessions) dataTable.getRowData());
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La session " + session.getIntitule() + " a été payée";
        if (service.paidSession(session)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans le paiement de la session " + session.getIntitule();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }

    /**
     * Suppression de la session
     */
    public String delete() {
        // recuperation de la session a updateSessionsr
        session = ((ManageSessions) dataTable.getRowData());
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La session " + session.getIntitule() + " a été supprimée";
        if (service.deleteSession(session)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la suppression de la session " + session.getIntitule();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }

    /**
     * Archivage de la session
     */
    public String archive() {
        // recuperation de la session a updateSessionsr
        session = ((ManageSessions) dataTable.getRowData());
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La session " + session.getIntitule() + " a été archivée";
        if (service.archiveSession(session)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans l'archivage de la session " + session.getIntitule();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }
}
