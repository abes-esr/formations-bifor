/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.formation.model.Stagiaire;
import org.formation.service.AgifManageService;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.service.impl.AgifServiceImpl;
import org.formation.tools.Encryption;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="editIdentifiantsMonCompteBean")
@SessionScoped
public class EditIdentifiantsMonCompteBean extends AbstractBackingBean {
	private static final long serialVersionUID = -993461077030946075L;
	
	// Service metier
    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{stagiaireBean}")
    private StagiaireBean stagiaire;
    // Inscription en edition
    private String newPassword="";
    private String confirmPassword="";

    @PostConstruct
    public void init() {
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
        return "back";
    }

    public void checkPassword(FacesContext context, UIComponent component,
            Object value) {
        String confirmPassword = (String) value;
        String msg;
        FacesMessage message;
        // traitement de la validité de la saisie
        String expression = "^[a-zA-Z0-9]+$";
        Pattern p = Pattern.compile(expression);

        //Match the given string with the pattern
        Matcher m = p.matcher(confirmPassword);
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

        System.out.println("Mot de passe confirmé : " + confirmPassword);
        UIComponent monComponent = (UIComponent) FacesContext.getCurrentInstance().
                getViewRoot().findComponent("iForm:passwordInput");
        HtmlInputSecret inputPassword = (HtmlInputSecret) monComponent;
        String password = (String) inputPassword.getValue();
        System.out.println("Mot de passe trouvé : " + password);
        if (confirmPassword.compareTo(password) != 0) {
            ((UIInput) component).setValid(false);
            msg = "La confirmation doit être identique au mot de passe";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    msg, msg);
            context.addMessage(component.getClientId(context),
                    message);
            throw new ValidatorException(message);
        }

    }

    /**
     * à modifier pour adapter à formateur
     * @param context
     * @param component
     * @param value
     */
    public void checkLogin(FacesContext context, UIComponent component,
            Object value) {
//        String identifiant = (String) value;
//        String msg;
//        FacesMessage message;
//        // traitement de la validité de la saisie
//        String expression = "^[a-zA-Z0-9]+$";
//        Pattern p = Pattern.compile(expression);
//
//        //Match the given string with the pattern
//        Matcher m = p.matcher(identifiant);
//        //Check whether match is found
//        boolean matchFound = m.matches();
//        if (!matchFound) {
//            msg = "Le format du nom d'utilisateur est incorrect";
//            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    msg, msg);
//            context.addMessage(component.getClientId(context),
//                    message);
//            throw new ValidatorException(message);
//        }
//
//        // traitement de l'unicité du login
//        if ((identifiant.compareTo(stagiaire.getStagiaire().getIdentifiant()) != 0)) {
//            if (service.checkLoginStagiaire(identifiant) == false) {
//                msg = "Cet identifiant est déjà utilisé";
//                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                        msg, msg);
//                context.addMessage(component.getClientId(context),
//                        message);
//                throw new ValidatorException(message);
//            }
//        }
    }

    /**
     * à modifier pour adapter à formateur
     * @return
     */
    public String next() {
    	return "à modifier";
        // declaration d'un message
//        FacesMessage message = new FacesMessage();
//        FacesContext context = FacesContext.getCurrentInstance();
//        AgifManageService manageService = new AgifManageServiceImpl();
//        String msg = "Modification des identifiants du compte effectuée ";
//        Stagiaire s = stagiaire.getStagiaire();
//
//        if (!newPassword.isEmpty()) {
//            try {
//                s.setMotdepasse(Encryption.encrypt(newPassword));
//            } catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(EditIdentifiantsMonCompteBean.class.getName()).log(Level.SEVERE, null, ex);
//                msg = "Impossible de modifier les identifiants du compte ";
//                message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                        msg, msg);
//                context.addMessage(null, message);
//                return "failure";
//            }
//        }
//
//        if (manageService.saveStagiaire(s) == false) {
//
//            msg = "Impossible de modifier les identifiants du compte ";
//            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    msg, msg);
//            context.addMessage(null, message);
//            return "failure";
//        }
//        stagiaire.setStagiaire(s);
//        // declaration d'un message        
//        message = new FacesMessage(FacesMessage.SEVERITY_INFO,
//                msg, msg);
//        context.addMessage(null, message);
//        return "success";
    }
}
