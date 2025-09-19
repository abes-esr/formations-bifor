/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.formation.constant.Constant;
import org.formation.mail.AgifMail;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.service.AgifManageService;

/**
 *
 * @author jean-laurent
 */
public class ConfirmInscriptionBean {
    // Service metier
    private AgifManageService service;
    
    // Injections
    private ListInscriptionsBean listInscriptions;
    
    // champ d'affichage
    private String mailConfirmation;
    private List<String> idStagiaires = new ArrayList<>();
    private List<SelectItem> stagiaires = new ArrayList<>();
    private boolean first = true;
    
    @PostConstruct
    public void init() {
        // Session courante
        Sessions sessions = (Sessions) listInscriptions.getManageSessions();

        // texte par defaut du mail de confirmation
        this.mailConfirmation = AgifMail.getMailConfirmation(sessions);

        // construction de la liste des stagiaires
        for (Inscription i : listInscriptions.getInscriptionItems()) {
            // recuperation de l'etat
            String etat = "";
            if (i.getIdsessions1().equals(sessions)) {
                etat = i.getEtat();
            } else if (i.getIdsessions2().equals(sessions)) {
                etat = i.getEtat2();
            }
            
            // mail a envoyer
            if (etat.equals(Constant.VALIDE)) {
                stagiaires.add(new SelectItem(i.getIdstagiaire().getIdstagiaire().toString(), i.getIdstagiaire().getPrenom() + " " + i.getIdstagiaire().getNom().toUpperCase()));
                
                // affectation des valeurs par defaut
                idStagiaires.add(i.getIdstagiaire().getIdstagiaire().toString());
            }
        }
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getMailConfirmation() {
        return mailConfirmation;
    }

    public void setMailConfirmation(String mailConfirmation) {
        this.mailConfirmation = mailConfirmation;
    }

    public List<String> getIdStagiaires() {
        return idStagiaires;
    }

    public void setIdStagiaires(List<String> idStagiaires) {
        this.idStagiaires = idStagiaires;
    }

    public List<SelectItem> getStagiaires() {
        return stagiaires;
    }

    public void setStagiaires(List<SelectItem> stagiaires) {
        this.stagiaires = stagiaires;
    }

    public ListInscriptionsBean getListInscriptions() {
        return listInscriptions;
    }

    public void setListInscriptions(ListInscriptionsBean listInscriptions) {
        this.listInscriptions = listInscriptions;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String envoyer() {
        // declaration d'un message
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Le mail de confirmation de la session a été envoyé";
        
        // envoi du mail de confirmation
        if (service.confirmSession((Sessions) listInscriptions.getManageSessions(), mailConfirmation, idStagiaires)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'envoi du mail de confirmation";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }

        context.addMessage(null, message);

        return "success";
    }
}
