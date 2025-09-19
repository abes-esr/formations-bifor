/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.formation.model.Formateur;
import org.formation.model.FormateurUser;
import org.formation.model.Sessions;
import org.formation.model.Statut;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.web.components.FormateurManageSessions;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="formateurListSessionsBean")
@SessionScoped
public class FormateurListSessionsBean {

    // Service metier
    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<FormateurManageSessions> sessionsItems = new ArrayList<FormateurManageSessions>();
    // champs de filtre
    private String filterStatut;
    private ArrayList<SelectItem> filterStatuts = new ArrayList<SelectItem>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private FormateurManageSessions session;

    @PostConstruct
    public void init() {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    String currentUserName = authentication.getName();
    	    
    	    Logger.getLogger(FormateurListSessionsBean.class.getName()).log(Level.SEVERE,
                    "user logueee: " + currentUserName);
    	    // impossible de faire passer le formateurUser, il faut retourner chercher ici
    	    //dans la base le refformateur grace au mail
    	}
    	Formateur f = new Formateur();
    	f.setIdformateur(9250482);
        initialisation(f);
    }

    private void initialisation(Formateur f) {
        sessionsItems = new ArrayList<FormateurManageSessions>();

        List<Sessions> sessions = service.listSessionsByFormateur(f);
        Logger.getLogger(FormateurListSessionsBean.class.getName()).log(Level.SEVERE,
                "nb de sessions : " + sessions.size());
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

            sessionsItems.add(new FormateurManageSessions(intitule, nbInscrits, s));
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

    public List<FormateurManageSessions> getSessionsItems() {

        return sessionsItems;
    }

    public void setSessionsItems(List<FormateurManageSessions> sessionsItems) {
        this.sessionsItems = sessionsItems;
    }

    public FormateurManageSessions getSession() {

        return session;
    }

    public void setSession(FormateurManageSessions session) {
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("formateurListInscriptionsBean");
        session = ((FormateurManageSessions) dataTable.getRowData());
        return "show";
    }

    /**
     * Rafraichissement de la liste des sessions
     */
    public String refresh() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "refresh";
    }
}
