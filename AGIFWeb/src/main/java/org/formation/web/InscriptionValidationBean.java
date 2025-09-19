/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.formation.model.Inscription;
import org.formation.model.Question;
import org.formation.model.Reponse;
import org.formation.model.TypeFormation;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;
import org.formation.web.components.Reponses;

/**
 *
 * @author jean-laurent
 */

@ManagedBean(name="inscriptionValidationBean")
@SessionScoped
public class InscriptionValidationBean {
    // Service metier

    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{demande}")
    private DemandeBean demande;
    // Regle d'affichage des champs
    private boolean renderedFonction = true;
    private boolean renderedRCR = true;
    private boolean renderedService = false;
    private boolean renderedCoordinateur = true;

    @PostConstruct
    public void init() {

        List<TypeFormation> typeFormationsStar = service.listTypeFormationWithoutRCR();
        renderedRCR = !typeFormationsStar.contains(demande.getTypeFormation());


        List<TypeFormation> typeFormations = service.listTypeFormationWithoutFonction();
        renderedFonction = !typeFormations.contains(demande.getTypeFormation());


        List<TypeFormation> typeFormationsStar12 = service.listTypeFormationWithoutCoordinateur();
        renderedCoordinateur = !typeFormationsStar12.contains(demande.getTypeFormation());

        List<TypeFormation> typeFormationsStar1 = service.listTypeFormationWithService();
        renderedService = typeFormationsStar1.contains(demande.getTypeFormation());

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

    public boolean isRenderedCoordinateur() {
        return renderedCoordinateur;
    }

    public void setRenderedCoordinateur(boolean renderedCoordinateur) {
        this.renderedCoordinateur = renderedCoordinateur;
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

    public String Retour() {
        // on vide la session utilisateur
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "back";
    }

    public String Validation() {

        // insertion de l'inscription en base de données
        Inscription i = new Inscription();
        i.setIdinscription(demande.getIdentifiant());

        // etape 1

        // nom en majuscule
        String nom = demande.getStagiaire().getNom().toUpperCase();
        demande.getStagiaire().setNom(nom);

        // premier lettre du prenom en majuscule
        String prenom = demande.getStagiaire().getPrenom();
        String prenomCapitalize = prenom.substring(0, 1).toUpperCase()
                + prenom.substring(1, prenom.length());
        demande.getStagiaire().setPrenom(prenomCapitalize);

        i.setIdstagiaire(demande.getStagiaire());

        // etape 2
        i.setIdsessions1(demande.getChoixPrioritaire());
        if (demande.getChoixSecondaire() == null) {
            i.setIdsessions2(demande.getChoixPrioritaire());
        } else {
            i.setIdsessions2(demande.getChoixSecondaire());
        }
        //etape 3
        List<Reponse> reponses = new ArrayList<Reponse>();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Reponses r : demande.getReponses()) {
            // construction de la reponse
            Reponse reponse = new Reponse();
            reponse.setIdinscription(r.getIdinscription());
            reponse.setIdquestion((Question) r.getIdquestion());
            if (r.getDateSpecification() != null) {
                reponse.setSpecification(simpleFormat.format(r.getDateSpecification()));
            }
            // traitement du choix multiple en base
            if (r.getListeChoix().isEmpty()) {
                reponse.setChoix(r.getChoix());
            } else {
                reponse.setChoix(r.getListeChoix().toString());
            }
            reponses.add(reponse);
        }
        i.setReponseList(reponses);
        // validation de l'inscription
        if (!service.saveInscription(i)) {
            return "failure";
        }
        // on vide la session utilisateur
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "success";
    }

    public String export() {
        try {
            Inscription i = new Inscription();   
            i.setIdstagiaire(demande.getStagiaire());
            i.setIdinscription(demande.getIdentifiant());
            i.setIdsessions1(demande.getChoixPrioritaire());
            
            if (demande.getChoixSecondaire() == null) {
                i.setIdsessions2(demande.getChoixPrioritaire());
            }
            else {
                i.setIdsessions2(demande.getChoixSecondaire());
            }
            
            i.setEtat("ENCOURS");
            i.setEtat2("ENCOURS");
            
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response = configureResponse(response, "Export_Inscription_" + demande.getIdentifiant());

            // création du document.
            ByteArrayOutputStream baos = service.exportInscription(i);
            
            // préparation de la reponse
            response.setContentLength(baos.size());

            // retour du ByteArrayOutputStream en ServletOutputStream
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            baos.flush();

            context.responseComplete();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage();
            FacesContext context = FacesContext.getCurrentInstance();

            String msg = "Erreur dans la génération du PDF : " + e.getMessage();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
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
