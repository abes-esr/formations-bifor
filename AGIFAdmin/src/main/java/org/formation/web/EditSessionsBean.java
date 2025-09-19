/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.formation.model.Formateur;
import org.formation.model.Lieu;
import org.formation.model.Sessions;
import org.formation.model.Statut;
import org.formation.model.TypeFormation;
import org.formation.model.TypeSession;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class EditSessionsBean {

    // Service metier
    private AgifManageService service = new AgifManageServiceImpl();
    // Injections
    private ListSessionBean listSessions;
    // champ d'affichage
    private Sessions sessions;
    private ArrayList<SelectItem> typeFormationItems = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> typeSessionItems = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> formateurItems = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> lieuItems = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> statutItems = new ArrayList<SelectItem>();
    // gestion de la duree
    private int dureeHeure;
    private int dureeMinute;
    // champ ajax
    private Formateur formateur;
    private Lieu lieu;

    public ListSessionBean getListSessions() {
        return listSessions;
    }

    public void setListSessions(ListSessionBean listSessions) {
        this.listSessions = listSessions;
    }

    public ArrayList<SelectItem> getStatutItems() {
        return statutItems;
    }

    public void setStatutItems(ArrayList<SelectItem> statutItems) {
        this.statutItems = statutItems;
    }

    @PostConstruct
    public void init() {
        // Liste des types de formation
        List<TypeFormation> typeFormations = service.listTypeFormations();
        for (TypeFormation tf : typeFormations) {
            typeFormationItems.add(new SelectItem(tf,
                    tf.getNom()));
        }
        // Liste des types de session
        List<TypeSession> typeSessions = service.listTypeSessions();
        for (TypeSession ts : typeSessions) {
            typeSessionItems.add(new SelectItem(ts,
                    ts.getNom()));
        }
        // Liste des formateurs
        List<Formateur> formateurs = service.listFormateurs();
        for (Formateur f : formateurs) {
            formateurItems.add(new SelectItem(f,
                    f.getPrenom() + " " + f.getNom()));
        }
        // Liste des lieux de formation
        List<Lieu> lieux = service.listLieux();
        for (Lieu l : lieux) {
            lieuItems.add(new SelectItem(l,
                    l.getNom()));
        }

        // Liste des statuts
        List<Statut> statuts = service.listStatuts();
        for (Statut st : statuts) {
            statutItems.add(new SelectItem(st,
                    st.getNom()));
        }

        // initialisation AJAX
        formateur = new Formateur();
        lieu = new Lieu();

        // recuperation du lieu en cours
        sessions = listSessions.getSession();
        if (sessions == null) {
            // initilisations

            GregorianCalendar calendarDebut = new java.util.GregorianCalendar();
            GregorianCalendar calendarFin = new java.util.GregorianCalendar();
            GregorianCalendar calendarCloture = new java.util.GregorianCalendar();
            Calendar dateJour = Calendar.getInstance();

            // heure de début : 9h00
            calendarDebut.set(dateJour.get(Calendar.YEAR), dateJour.get(Calendar.MONTH),
                    dateJour.get(Calendar.DATE), 9, 0);
            // heure de fin : 16h30
            calendarFin.set(dateJour.get(Calendar.YEAR), dateJour.get(Calendar.MONTH),
                    dateJour.get(Calendar.DATE), 16, 30);
            // heure de cloture : 20h00
            calendarCloture.set(dateJour.get(Calendar.YEAR), dateJour.get(Calendar.MONTH),
                    dateJour.get(Calendar.DATE), 20, 0);



            sessions = new Sessions();
            sessions.setNbpersonnemax(10);
            sessions.setDatedebut(calendarDebut.getTime());
            sessions.setDatefin(calendarFin.getTime());
            sessions.setDatecloture(calendarCloture.getTime());
            sessions.setDuree(600);
            sessions.setIdstatut(new Statut(1));
        }
        // recupertion de la duree
        dureeHeure = sessions.getDuree() / 60;
        dureeMinute = sessions.getDuree() % 60;
    }

    /**
     * Get the value of sessions
     *
     * @return the value of sessions
     */
    public Sessions getSessions() {
        return sessions;
    }

    /**
     * Set the value of sessions
     *
     * @param sessions new value of sessions
     */
    public void setSessions(Sessions sessions) {
        this.sessions = sessions;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public ArrayList<SelectItem> getTypeFormationItems() {
        return typeFormationItems;
    }

    public void setTypeFormationItems(ArrayList<SelectItem> typeFormationItems) {
        this.typeFormationItems = typeFormationItems;
    }

    public ArrayList<SelectItem> getTypeSessionItems() {
        return typeSessionItems;
    }

    public void setTypeSessionItems(ArrayList<SelectItem> typeSessionItems) {
        this.typeSessionItems = typeSessionItems;
    }

    public ArrayList<SelectItem> getFormateurItems() {
        return formateurItems;
    }

    public void setFormateurItems(ArrayList<SelectItem> formateurItems) {
        this.formateurItems = formateurItems;
    }

    public ArrayList<SelectItem> getLieuItems() {
        return lieuItems;
    }

    public void setLieuItems(ArrayList<SelectItem> lieuItems) {
        this.lieuItems = lieuItems;
    }

    public Formateur getFormateur() {
        return formateur;
    }

    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public int getDureeHeure() {
        return dureeHeure;
    }

    public void setDureeHeure(int dureeHeure) {
        this.dureeHeure = dureeHeure;
    }

    public int getDureeMinute() {
        return dureeMinute;
    }

    public void setDureeMinute(int dureeMinute) {
        this.dureeMinute = dureeMinute;
    }

    public String valide() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La session " + sessions.toString() + " a bien été enregistrée";
        Date dateJour = new Date();
        // tests de cohérence des dates

        
        // on fait des tests sur la date de clôture qu'en création
        if (sessions.getIdsessions() == null) {
            // Date de debut de session supérieure a la date du jour
        if (sessions.getDatedebut().getTime() < dateJour.getTime()) {
            msg = "La date de debut de session doit être supérieure a la date du jour ";
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            return "error";
        }
            // Date de cloture supérieure a la date du jour
            if (sessions.getDatecloture().getTime() < dateJour.getTime()) {
                msg = "La date de clôture doit être supérieure a la date du jour ";
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
                context.addMessage(null, message);
                return "error";
            }

            // // date de cloture < date de debut
            if (sessions.getDatecloture().getTime() >= sessions.getDatedebut().getTime()) {
                msg = "La date de clotûre doit être inférieure a la date de début ";
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
                context.addMessage(null, message);
                return "error";
            }
        }
        // date de debut < date de fin
        if (sessions.getDatedebut().getTime() >= sessions.getDatefin().getTime()) {
            msg = "La date de début doit être inférieure a la date de fin ";
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            return "error";
        }
        // gestion de la duree
        sessions.setDuree(dureeHeure * 60 + dureeMinute);
        // envoi du mail de confirmation
        if (service.saveSession(sessions)) {
            msg = "La session " + sessions.toString() + " a bien été enregistrée";
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'enregistrement de la session ";
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }

        context.addMessage(null, message);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();

        return "success";
    }

    public String back() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "back";
    }

    public String saveFormateur() {
        System.out.println("Nom du formateur" + formateur.getNom());
        System.out.println("PRenom du formateur" + formateur.getPrenom());


        // validation de l'inscription
        if (service.saveFormateur(formateur)) {
            // remise à jour de la liste des formateurs
            formateurItems = new ArrayList<SelectItem>();
            List<Formateur> formateurs = service.listFormateurs();
            sessions.setIdformateur(formateur);
            for (Formateur f : formateurs) {
                formateurItems.add(new SelectItem(f,
                        f.getPrenom() + " " + f.getNom()));
            }
        }
        return null;

    }

    public String saveLieu() {
        System.out.println("Nom du lieu" + lieu.getNom());
        System.out.println("Ville du lieu" + lieu.getVille());


        // validation de l'inscription
        if (service.saveLieu(lieu)) {
            // remise à jour de la liste des formateurs
            lieuItems = new ArrayList<SelectItem>();
            List<Lieu> lieux = service.listLieux();
            sessions.setIdlieu(lieu);
            for (Lieu l : lieux) {
                lieuItems.add(new SelectItem(l,
                        l.getNom()));
            }
        }
        return null;

    }
}
