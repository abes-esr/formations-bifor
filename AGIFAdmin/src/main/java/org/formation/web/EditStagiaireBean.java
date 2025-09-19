/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import org.formation.model.Etablissement;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;
import org.formation.service.AgifManageService;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;
import org.formation.tools.Encryption;

/**
 *
 * @author jean-laurent
 */
public class EditStagiaireBean {

    // Service metier
    private AgifManageService service;
    private AgifService agifService = new AgifServiceImpl();
    // Injections
    private ListStagiairesBean listStagiaires;
    // Inscription en edition
    private Stagiaire stagiaire;
    private String newPassword;
    private String confirmPassword;
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    // champs du formulaire
    private List<SelectItem> etablissementItems = new ArrayList<SelectItem>();
    private List<SelectItem> rcrItems = new ArrayList<SelectItem>();
    private String inputRCR;
    // Regle d'affichage des champs   
    private boolean renderedRCR;
    private boolean renderedInputRCR;

    @PostConstruct
    public void init() {
        // recuperation de du formateur en cours
        stagiaire = listStagiaires.getStagiaire();
        if (stagiaire == null) {
            stagiaire = new Stagiaire();
        }

        // list des etablissements
        List<TypeFormation> typeFormations = service.listTypeFormations();
        for (TypeFormation tf : typeFormations) {
            List<Etablissement> etablissements = agifService.listEtablissements(tf);
            for (Etablissement e : etablissements) {
                SelectItem si = new SelectItem(e.getShort_name(), e.getShort_name());
                if (!etablissementItems.contains(si)) {
                    etablissementItems.add(si);
                }
            }
        }


        //Liste des RCR
        if (stagiaire.getEtablissement() != null) {
            List<Etablissement> rcrList = agifService.getRCRByName(stagiaire.getEtablissement());
            rcrItems.clear();
            rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
            rcrItems.add(new SelectItem("Autre", "Autre"));
            if (stagiaire.getNumrcr() != null) {
                rcrItems.add(new SelectItem(stagiaire.getNumrcr(),
                        stagiaire.getNumrcr()));
            }
            for (Etablissement r : rcrList) {
                rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
            }
        }
        // regle d 'affichage du champ RCR
        renderedInputRCR = false;
        renderedRCR = false;
        String numeroRCR = stagiaire.getNumrcr();
        if (numeroRCR != null) {
            renderedRCR = true;
            if (numeroRCR.compareTo("Autre") == 0) {
                renderedInputRCR = true;
            }
        } else {
            stagiaire.setNumrcr("");
        }

    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public List<SelectItem> getEtablissementItems() {
        return etablissementItems;
    }

    public void setEtablissementItems(List<SelectItem> etablissementItems) {
        this.etablissementItems = etablissementItems;
    }

    public ListStagiairesBean getListStagiaires() {
        return listStagiaires;
    }

    public void setListStagiaires(ListStagiairesBean listStagiaires) {
        this.listStagiaires = listStagiaires;
    }

    public List<SelectItem> getRcrItems() {
        return rcrItems;
    }

    public void setRcrItems(List<SelectItem> rcrItems) {
        this.rcrItems = rcrItems;
    }

    public Stagiaire getStagiaire() {
        return stagiaire;
    }

    public void setStagiaire(Stagiaire stagiaire) {
        this.stagiaire = stagiaire;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String getInputRCR() {
        return inputRCR;
    }

    public void setInputRCR(String inputRCR) {
        this.inputRCR = inputRCR;
    }

    public boolean isRenderedInputRCR() {
        return renderedInputRCR;
    }

    public void setRenderedInputRCR(boolean renderedInputRCR) {
        this.renderedInputRCR = renderedInputRCR;
    }

    public void changeEtablissement(AjaxBehaviorEvent  event) {
        rcrItems.clear();
        //System.out.println("event "+event.toString());
        //String name = stagiaire.getEtablissement();
        String name = (String) ((HtmlSelectOneMenu)event.getSource()).getValue();
        //System.out.println("iln "+iln);
        List<Etablissement> rcrList = agifService.getRCRByName(name);
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));
        /*if (stagiaire.getNumrcr() != null) {
            rcrItems.add(new SelectItem(stagiaire.getNumrcr(),
                    stagiaire.getNumrcr()));
        }*/
        for (Etablissement r : rcrList) {
            rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
        }
        
        
        stagiaire.setAdresse("");
        stagiaire.setAdresse2("");
        stagiaire.setAdresse3("");
        stagiaire.setCodepostal("");
        stagiaire.setVille("");
    }

    public void changeRCR(AjaxBehaviorEvent event) {
        //this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        //String rcr = stagiaire.getNumrcr();
        //String rcr = event.getNewValue().toString();
        String rcr = (String) ((HtmlSelectOneMenu)event.getSource()).getValue();
        //System.out.println("iln "+iln);
        if (rcr != null) {
            if (rcr.compareTo("Autre") == 0) {
                this.renderedInputRCR = true;
                stagiaire.setAdresse("");
                stagiaire.setAdresse2("");
                stagiaire.setAdresse3("");
                stagiaire.setCodepostal("");
                stagiaire.setVille("");
            } else {
                Etablissement address = agifService.getAddressByRCR(rcr);
                this.renderedInputRCR = false;
                if (address != null) {
                    stagiaire.setAdresse(address.getAddress());
                    stagiaire.setAdresse2(address.getAddress2());
                    stagiaire.setAdresse3(address.getAddress3());
                    stagiaire.setCodepostal(address.getPostcode());
                    stagiaire.setVille(address.getCity());
                    //s.setTelephone((address.getTelephone()).replace(".", ""));

                } else {
                    // on remet les champ à vide
                    stagiaire.setAdresse("");
                    stagiaire.setAdresse2("");
                    stagiaire.setAdresse3("");
                    stagiaire.setCodepostal("");
                    stagiaire.setVille("");
                }
            }
        }

    }

    public void changeRCRInput(AjaxBehaviorEvent event) {
        //this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        //String rcr = (String) event.getNewValue();
        String rcr = (String) ((UIInput)event.getSource()).getValue();
        //System.out.println("iln "+iln);
        if (rcr != null) {
            if (rcr.compareTo("Autre") == 0) {
                this.renderedInputRCR = true;
                this.stagiaire.setAdresse("");
                this.stagiaire.setAdresse2("");
                this.stagiaire.setAdresse3("");
                this.stagiaire.setCodepostal("");
                this.stagiaire.setVille("");
            } else {
                this.renderedInputRCR = false;
            }
        }

    }

    public String save() throws NoSuchAlgorithmException {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Le stagiaire  a bien été enregistré";

        // Affectation du numero de RCR saisi
        if ((stagiaire.getNumrcr().compareTo("Autre") == 0)
                && (inputRCR != null) && (inputRCR.compareTo("") != 0)) {
            stagiaire.setNumrcr(inputRCR);
        }
        // validation de l'inscription
        if (service.saveStagiaire(stagiaire)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'enregistrement du stagiaire ";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            return "error";
        }
        context.addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "success";
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String back() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "back";
    }

    public void checkLogin(FacesContext context, UIComponent component,
            Object value) {
        String identifiant = (String) value;
        String msg;
        FacesMessage message;
        // traitement de la validité de la saisie
        String expression = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(expression);

        //Match the given string with the pattern
        Matcher m = p.matcher(identifiant);
        //Check whether match is found
        boolean matchFound = m.matches();
        if (!matchFound) {
            msg = "Le format du nom d'utilisateur est incorrect";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    msg, msg);
            context.addMessage(component.getClientId(context),
                    message);
            throw new ValidatorException(message);
        }

        // traitement de l'unicité du login
        if (agifService.checkLoginStagiaire(identifiant) == false) {
            msg = "Cet identifiant est déjà utilisé";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    msg, msg);
            context.addMessage(component.getClientId(context),
                    message);
            throw new ValidatorException(message);
        }

    }

    public void checkPassword(FacesContext context, UIComponent component,
            Object value) {
        String confirmationPassword = (String) value;
        String msg;
        FacesMessage message;
        // traitement de la validité de la saisie
        String expression = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(expression);

        //Match the given string with the pattern
        Matcher m = p.matcher(confirmationPassword);
        //Check whether match is found
        boolean matchFound = m.matches();
        if (!matchFound) {
            msg = "Le format du mot de passe est incorrect";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    msg, msg);
            context.addMessage(component.getClientId(context),
                    message);
            throw new ValidatorException(message);
        }

        // traitement de la cohérence des mots de passes

        System.out.println("Mot de passe confirmé : " + confirmationPassword);
        if (confirmationPassword.compareTo(newPassword) != 0) {
            ((UIInput) component).setValid(false);
            msg = "La confirmation doit être identique au mot de passe";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    msg, msg);
            context.addMessage(component.getClientId(context),
                    message);
            throw new ValidatorException(message);
        }

    }

    public boolean isRenderedRCR() {
        return renderedRCR;
    }

    public void setRenderedRCR(boolean renderedRCR) {
        this.renderedRCR = renderedRCR;
    }

    public void valueChanged(ValueChangeEvent event) {
        this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        //String name = stagiaire.getEtablissement();
        if (null != event.getNewValue()) {
            String name = event.getNewValue().toString();
            //System.out.println("iln "+iln);
            List<Etablissement> rcrList = agifService.getRCRByName(name);
            rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
            rcrItems.add(new SelectItem("Autre", "Autre"));
            if (stagiaire.getNumrcr() != null) {
                rcrItems.add(new SelectItem(stagiaire.getNumrcr(),
                        stagiaire.getNumrcr()));
            }
            for (Etablissement r : rcrList) {
                rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
            }
            stagiaire.setAdresse("");
            stagiaire.setAdresse2("");
            stagiaire.setAdresse3("");
            stagiaire.setCodepostal("");
            stagiaire.setVille("");
        }

    }
}
