/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="oublieMotdepasseMonCompteBean")
@SessionScoped
public class OublieMotdepasseMonCompteBean extends AbstractBackingBean {
	private static final long serialVersionUID = 1136806454484000380L;
	
	// Service metier
    private AgifService service = new AgifServiceImpl();
    // Attributs
    private String mail;

    @PostConstruct
    public void init() {
    }

    /**
     * Get the value of mail
     *
     * @return the value of mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Set the value of mail
     *
     * @param mail new value of mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    } 

    public AgifService getService() {
        return service;
    }

    public void setService(AgifService service) {
        this.service = service;
    }
   

    public String retrieve() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Il n'y a pas de compte correspondant à l'adresse"
                + " e-mail que vous avez déclarée";

        // recuperation des identifiants de l'utilisateur
        boolean result = service.retreivePassword(mail);
        // si la recuperation a fonctionnée
        if (result==true) {
            return "success";
        }

        message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        context.addMessage(null, message);
        return "failure";

    }
}
