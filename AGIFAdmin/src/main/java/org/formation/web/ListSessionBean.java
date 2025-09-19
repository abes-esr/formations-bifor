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
import org.formation.model.Sessions;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ListSessionBean {

    // Service metier
    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<Sessions> sessionsItems = new ArrayList<Sessions>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private Sessions session;

    @PostConstruct
    public void init() {
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
       
        initialisation();
    }

    private void initialisation() {
        sessionsItems = service.listSessions();
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

    public List<Sessions> getSessionsItems() {
        //  initialisation();
        return sessionsItems;
    }

    public void setSessionsItems(List<Sessions> sessionsItems) {
        this.sessionsItems = sessionsItems;
    }

    public Sessions getSession() {
        return session;
    }

    public void setSession(Sessions session) {
        this.session = session;
    }

    /**
     * Creation d'une nouvelle session
     */
    public String create() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "new";
    }

    /**
     * Selection de la session pour l'affichage des inscriptions
     */
    public String modify() {
        // suppression de la session de formation en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editSessionsBean");

        session = ((Sessions) dataTable.getRowData());
        return "show";
    }

    /**
     * Suppression de la session
     */
    public String delete() {
        // recuperation de la session a updateSessionsr
        session = ((Sessions) dataTable.getRowData());
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La session " + session.getTitre() + " a été supprimée";
        if (service.destroySession(session)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la suppression de la session " + session.getTitre();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }

     /**
     * Clonage  de la session
     */
    public String duplicate() {
        // recuperation de la session a updateSessionsr
        session = ((Sessions) dataTable.getRowData());
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La session " + session.getTitre() + " a été dupliquée";
        if (service.cloneSession(session)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la duplication de la session " + session.getTitre();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }
    
}
