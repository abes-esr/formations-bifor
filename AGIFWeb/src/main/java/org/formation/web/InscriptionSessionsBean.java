/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.formation.model.Sessions;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="inscriptionSessionsBean")
@SessionScoped
public class InscriptionSessionsBean extends AbstractBackingBean {
	private static final long serialVersionUID = 688934455938736004L;
	
    // Service metier
	private AgifService service = new AgifServiceImpl();
	@ManagedProperty(value="#{demande}")
    private DemandeBean demande;
    // champs du formulaire
    private ArrayList<SelectItem> sessionsPrioritaires = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> sessionsSecondaires = new ArrayList<SelectItem>();
    // Regle d'affichage des champs
    private boolean renderedSessionsSecondaires = false;

    @PostConstruct
    public void init() {
        // Liste des sessions prioritaires
        initialisation();
    }

    private void initialisation() {
        List<Sessions> sessions = service.listAvalaibleSessions(demande.getTypeFormation());

//        sessionsPrioritaires.add(new SelectItem("", "Sélectionnez une session"));
        if (sessions != null) {
            for (Sessions s : sessions) {
                sessionsPrioritaires.add(new SelectItem(s, libelleSessions(s)));
            }
        }
    }

    /**
     * Get the libelle of sessions
     *
     * @return the value of sessions
     */
    private String libelleSessions(Sessions s) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

        /*  return s.getIdlieu().getNom() + " [" + s.getTitre() + ", "
        + " du "
        + simpleFormat.format(s.getDatedebut())
        + " au "
        + simpleFormat.format(s.getDatefin())
        + "] ";*/
        /* return s.getIdlieu().getVille().toUpperCase() + " ["
        + " du "
        + simpleFormat.format(s.getDatedebut())
        + " au "
        + simpleFormat.format(s.getDatefin())
        + "] ";*/
        return s.getIdlieu().getNom().toUpperCase() + " ["
                + " du "
                + simpleFormat.format(s.getDatedebut())
                + " au "
                + simpleFormat.format(s.getDatefin())
                + "] ";
    }

    public DemandeBean getDemande() {
        //getServletRequest();
        return demande;
    }

    public void setDemande(DemandeBean demande) {
        this.demande = demande;
    }

    public boolean isRenderedSessionsSecondaires() {
        return renderedSessionsSecondaires;
    }

    public void setRenderedSessionsSecondaires(boolean renderedSessionsSecondaires) {
        this.renderedSessionsSecondaires = renderedSessionsSecondaires;
    }

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }

    public ArrayList<SelectItem> getSessionsPrioritaires() {

        // Liste des sessions prioritaires
        //initialisation();
        return sessionsPrioritaires;
    }

    public void setSessionsPrioritaires(ArrayList<SelectItem> sessionsPrioritaires) {
        this.sessionsPrioritaires = sessionsPrioritaires;
    }

    public ArrayList<SelectItem> getSessionsSecondaires() {
        return sessionsSecondaires;
    }

    public void setSessionsSecondaires(ArrayList<SelectItem> sessionsSecondaires) {
        this.sessionsSecondaires = sessionsSecondaires;
    }

    public String next() {
    	return "register";
    }

    public String back() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listFormationsBean");

        return "back";
    }

    public void changeChoixPrioritaire(ValueChangeEvent event) {
        this.sessionsSecondaires.clear();
        if (this.sessionsPrioritaires.size() > 2) {
            this.renderedSessionsSecondaires = true;
            Sessions s = (Sessions) event.getNewValue();
            for (SelectItem item : this.sessionsPrioritaires) {
                if (item.getLabel().compareTo(libelleSessions(s)) != 0) {
                    this.sessionsSecondaires.add(item);
                }
            }

        }
    }
}
