/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import org.formation.constant.Constant;
import org.formation.model.Etablissement;
import org.formation.model.Question;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="inscriptionStagiaireBean")
@SessionScoped
public class InscriptionStagiaireBean extends AbstractBackingBean {
	private static final long serialVersionUID = 5608995186644946766L;
	
	// Service metier
    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{demande}")
    private DemandeBean demande;
    // Regle d'affichage des champs
    private boolean renderedFonction = true;
    private boolean renderedRCR = true;
    private boolean requiredRCR = true;
    private boolean renderedService = false;
    private boolean renderedCoordinateur = true;
    private String labelCoordinateur = "Adresse mail du correspondant catalogage (à défaut, mail du coordinateur)";
    // champs du formulaire
    private List<SelectItem> etablissementItems = new ArrayList<SelectItem>();
    private List<SelectItem> rcrItems = new ArrayList<SelectItem>();

    @PostConstruct
    public void init() {

        List<TypeFormation> typeFormationsStar = service.listTypeFormationWithoutRCR();
        renderedRCR = !typeFormationsStar.contains(demande.getTypeFormation());
       
        // RCR non obligatoire pour les formations correspondants autorites
        int typeFormation = demande.getTypeFormation().getIdtypeformation().intValue();
        if (Constant.getFormationsAutorites().contains(typeFormation)) {
            requiredRCR = false;
        }

        List<TypeFormation> typeFormations = service.listTypeFormationWithoutFonction();
        renderedFonction = !typeFormations.contains(demande.getTypeFormation());

        List<TypeFormation> typeFormationsStar1 = service.listTypeFormationWithService();
        renderedService = typeFormationsStar1.contains(demande.getTypeFormation());

        List<TypeFormation> typeFormationsStar12 = service.listTypeFormationWithoutCoordinateur();
        renderedCoordinateur = !typeFormationsStar12.contains(demande.getTypeFormation());
        // si il s'agit d'une formation CALAMES alors on change le label
        if (demande.getTypeFormation().getIdtypeformation().equals(Short.parseShort("7"))) {
            labelCoordinateur = "Adresse mail du correspondant Calames";
        }
         if (Constant.getFormationsAutorites().contains(typeFormation)) {
            renderedCoordinateur = false;
        }

        List<Etablissement> etablissements = service.listEtablissements(demande.getTypeFormation());
        etablissementItems.add(new SelectItem("", "--Selectionnez un établissement--"));
        etablissementItems.add(new SelectItem(" ", "Sans ILN"));
        String iln = "";
        for (Etablissement e : etablissements) {
            if (e.getIln() != 0) {
                iln = java.lang.String.valueOf(e.getIln()) + "-";
            }
            String short_name = e.getShort_name();
            if (e.getIln() == 300) {
                short_name = "Autres établissements Calames";
            }
            etablissementItems.add(new SelectItem(e.getShort_name(), iln + short_name));
            iln = "";
        }
        // construction de la liste des RCR
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));

        if (demande.getStagiaire().getEtablissement() != null) {
            //System.out.println("iln "+iln);
            List<Etablissement> rcrList = service.getRCRByName(demande.getStagiaire().getEtablissement());

            for (Etablissement r : rcrList) {
                rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
            }
        }

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

    public boolean isRequiredRCR() {
        return requiredRCR;
    }

    public void setRequiredRCR(boolean requiredRCR) {
        this.requiredRCR = requiredRCR;
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


    public String next() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();



        // Comparaison des mails du stagiaire
        UIComponent emailConfirmationComponent = FacesContext.getCurrentInstance().getViewRoot().findComponent("iForm:mailInput");

        try {
            validateEmailConfirmation();
        } catch (ValidatorException e) {
//            // Gérer l'erreur de validation
            FacesContext.getCurrentInstance().addMessage(emailConfirmationComponent.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les adresses mails ne correspondent pas", e.getMessage()));
            return null; // Rediriger vers la même page pour afficher les erreurs
        }

        // declaration d'un message
        demande.setEstConnecte(true);
        // on passe à la validation si il n'y a pas de questions
        List<Question> questions = service.listQuestionsByTypeFormation(demande.getTypeFormation());
        if (questions.isEmpty()) {
            return "validation";
        }

        return "success";
    }

    public void searchStagiaire() {
        // recuperation du stagiaire
        System.out.println(demande.getStagiaire().getNom());
        System.out.println(demande.getStagiaire().getPrenom());

        if (!(demande.getStagiaire().getNom().equals("")
                && demande.getStagiaire().getPrenom().equals(""))) {
            Stagiaire s = service.searchStagiaireByName(demande.getStagiaire().getNom(),
                    demande.getStagiaire().getPrenom());
            if (s != null) {
                demande.setStagiaire(s);
            }
        }
    }

    public void changeEtablissement(ValueChangeEvent event) {
        this.rcrItems.clear();
        String name = event.getNewValue().toString();
        List<Etablissement> rcrList = service.getRCRByName(name);
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));

        for (Etablissement r : rcrList) {
            rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
        }
        
        // on reinitialise les champs adresses du stagiaires
        Stagiaire s = demande.getStagiaire();
        s.setAdresse("");
        s.setAdresse2("");
        s.setCodepostal("");
        s.setVille("");
        demande.setStagiaire(s);
    }

    public void changeRCR(ValueChangeEvent event) {
        String rcr = event.getNewValue().toString();
        Etablissement address = service.getAddressByRCR(rcr);

        Stagiaire s = demande.getStagiaire();
        if (address != null) {
            s.setAdresse(address.getAddress());
            s.setAdresse2(address.getAddress2());
            s.setAdresse3(address.getAddress3());
            s.setCodepostal(address.getPostcode());
            s.setVille(address.getCity());
        }
        else {
            // on remet les champ à vide
            s.setAdresse("");
            s.setAdresse2("");
            s.setAdresse3("");
            s.setCodepostal("");
            s.setVille("");
        }
        
        demande.setStagiaire(s);
    }

    public void validateEmailConfirmation() throws ValidatorException {
        Stagiaire stagiaire = demande.getStagiaire(); // Récupération de l'objet stagiaire
        String confirmEmail = stagiaire.getConfirmMail(); // Valeur du champ confirmMail
        String email = stagiaire.getMail(); // Valeur du champ email principal

        if (email == null || confirmEmail == null || !email.equals(confirmEmail)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur: les adresses e-mail ne correspondent pas.", "Les adresses e-mail ne correspondent pas."));
        }
    }

}
