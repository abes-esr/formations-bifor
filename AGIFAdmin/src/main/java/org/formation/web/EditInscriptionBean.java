/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import org.formation.model.Etablissement;
import org.formation.model.Inscription;
import org.formation.model.Question;
import org.formation.model.Reponse;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;
import org.formation.service.AgifManageService;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;
import org.formation.web.components.Questions;
import org.formation.web.components.Reponses;

/**
 *
 * @author jean-laurent
 */
public class EditInscriptionBean {

    // Service metier
    private AgifManageService service;
    private AgifService agifService = new AgifServiceImpl();
    // Injections
    private ListInscriptionsBean listInscriptions;
    // Inscription en edition
    private Inscription inscription;
    // champs du formulaire
    private List<SelectItem> etablissementItems = new ArrayList<SelectItem>();
    private List<SelectItem> rcrItems = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> sessionsPrioritaires = new ArrayList<SelectItem>();
    private ArrayList<SelectItem> sessionsSecondaires = new ArrayList<SelectItem>();
    // champ d'affichage
    private List<Reponses> reponseItems = new ArrayList<Reponses>();
    private List<Question> questions = new ArrayList<Question>();
    private String inputRCR;
    // Regle d'affichage des champs    
    private boolean renderedInputRCR;
    private boolean renderedRCR = true;
    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {
        // recuperation de l'inscription en cours
        if (listInscriptions.getInscription() != null) {
            inscription = listInscriptions.getInscription();
        } else {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            Integer idinscription = Integer.valueOf(params.get("iForm:idinscription").toString());
            inscription = service.getInscriptionById(idinscription);
        }
        
        // recuperation des données de la session
        Sessions sessionInscription = service.getSessionsById(inscription.getIdsessions1().getIdsessions());
        TypeFormation tf = sessionInscription.getIdtypeformation();

        List<TypeFormation> typeFormationsStar = agifService.listTypeFormationWithoutRCR();
        renderedRCR = !typeFormationsStar.contains(tf);

        // list des etablissements
        etablissementItems.add(new SelectItem(" ", "Sans ILN"));
        List<Etablissement> etablissements = agifService.listEtablissements(tf);
        for (Etablissement e : etablissements) {
            etablissementItems.add(new SelectItem(e.getShort_name(), e.getShort_name()));
        }
        Collections.sort(etablissementItems,new Comparator<SelectItem>(){
        	   @Override
        	   public int compare(final SelectItem a,SelectItem b) {
        		   return a.getValue().toString().compareToIgnoreCase(b.getValue().toString());
        	     }
        	 });
        //Liste des RCR
        List<Etablissement> rcrList = agifService.getRCRByName(inscription.getIdstagiaire().getEtablissement());
        rcrItems.clear();
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));
        rcrItems.add(new SelectItem(inscription.getIdstagiaire().getNumrcr(),
                inscription.getIdstagiaire().getNumrcr()));
        for (Etablissement r : rcrList) {
            rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
        }

        // liste des sessions prioritaires et secondaires
        List<Sessions> sessions = service.listSessionsByTypeFormation(tf);

        sessionsPrioritaires.add(new SelectItem("", "---"));
        for (Sessions s : sessions) {
            sessionsPrioritaires.add(new SelectItem(s, libelleSessions(s)));
        }
        sessionsSecondaires.addAll(sessionsPrioritaires);

        // questions complementaires
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

        // regle d 'affichage du champ RCR
        renderedInputRCR = false;
        
        String numeroRCR = inscription.getIdstagiaire().getNumrcr();
        if (numeroRCR != null) {
            if (numeroRCR.compareTo("Autre") == 0) {
                renderedInputRCR = true;
            }
        }
        else {
        inscription.getIdstagiaire().setNumrcr("");
        }


        for (Reponse r : service.listReponsesByInscription(inscription)) {
            Reponses reponse = new Reponses();
            Questions q = new Questions(r.getIdquestion());
            reponse.setChoix(r.getChoix());
            reponse.setIdinscription(r.getIdinscription());
            reponse.setIdreponse(r.getIdreponse());
            reponse.setSpecification(r.getSpecification());
            reponse.setIdquestion(q);
            reponse.setDatereponse(r.getDatereponse());
            // si il s'agit d'un choix multiple
            if (r.getIdquestion().getUniquemultiple().equals("multiple")) {
                if (r.getChoix() != null) {
                    // formattage de la liste des choix
                    String choix = r.getChoix().substring(1);
                    choix = choix.substring(0, choix.length() - 1);

                    // decoupage en tableau
                    String[] tabString = choix.split(",");
                    // consitutaion de la liste de choix
                    List<String> listeChoix = new ArrayList<String>();
                    for (String str : tabString) {
                        listeChoix.add(str.trim());
                    }
                    reponse.setListeChoix(listeChoix);
                }
            }

            if (r.getSpecification() != null) {
                try {
                    reponse.setDateSpecification(simpleFormat.parse(r.getSpecification()));
                } catch (Exception ex) {
                    Logger.getLogger(EditInscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            reponseItems.add(reponse);
        }
    }

    public boolean isRenderedRCR() {
        return renderedRCR;
    }

    public void setRenderedRCR(boolean renderedRCR) {
        this.renderedRCR = renderedRCR;
    }

    /**
     * Get the libelle of sessions
     *
     * @return the value of sessions
     */
    private String libelleSessions(Sessions s) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

        return s.getIdlieu().getNom() + " [" + s.getTitre() + ", "
                + " du "
                + simpleFormat.format(s.getDatedebut())
                + " au "
                + simpleFormat.format(s.getDatefin())
                + "] ";
    }

    public List<SelectItem> getEtablissementItems() {

        return etablissementItems;
    }

    public void setEtablissementItems(List<SelectItem> etablissementItems) {
        this.etablissementItems = etablissementItems;
    }

    public ArrayList<SelectItem> getSessionsPrioritaires() {
        //     sessionsPrioritaires.clear();
        return sessionsPrioritaires;
    }

    public void setSessionsPrioritaires(ArrayList<SelectItem> sessionsPrioritaires) {
        this.sessionsPrioritaires = sessionsPrioritaires;
    }

    public ArrayList<SelectItem> getSessionsSecondaires() {
//        sessionsSecondaires.clear();
        return sessionsSecondaires;
    }

    public void setSessionsSecondaires(ArrayList<SelectItem> sessionsSecondaires) {
        this.sessionsSecondaires = sessionsSecondaires;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public List<Reponses> getReponseItems() {

        return reponseItems;
    }

    public void setReponseItems(List<Reponses> reponseItems) {
        this.reponseItems = reponseItems;
    }

    public Inscription getInscription() {

        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public ListInscriptionsBean getListInscriptions() {
        return listInscriptions;
    }

    public void setListInscriptions(ListInscriptionsBean listInscriptions) {
        this.listInscriptions = listInscriptions;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<SelectItem> getRcrItems() {
        return rcrItems;
    }

    public void setRcrItems(List<SelectItem> rcrItems) {
        this.rcrItems = rcrItems;
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

    public String modify() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "L'inscription " + inscription.toString() + " a été modifiée";

        // ajout des reponses aux questions
        List<Reponse> reponseList = new ArrayList<Reponse>();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Reponses r : reponseItems) {
            if (r.getDateSpecification() != null) {
                r.setSpecification(simpleFormat.format(r.getDateSpecification()));
            }
            // traitement du choix multiple en base
            if (r.getListeChoix().isEmpty()) {
                r.setChoix(r.getChoix());
            } else {
                r.setChoix(r.getListeChoix().toString());
            }
            reponseList.add(r);
        }
        inscription.setReponseList(reponseList);
        // Affectation du numero de RCR saisi
        if ((inscription.getIdstagiaire().getNumrcr().compareTo("Autre") == 0)
                && (inputRCR != null) && (inputRCR.compareTo("") != 0)) {
            inscription.getIdstagiaire().setNumrcr(inputRCR);
        }
        // validation de l'inscription
        if (service.updateInscription(inscription)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans la modification de l'inscription " + inscription.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);

        }
        context.addMessage(null, message);
        //  FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "success";
    }

    public void changeEtablissement(AjaxBehaviorEvent event) {
        this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        //String name = inscription.getIdstagiaire().getEtablissement();
         //String name = event.getNewValue().toString();
        String name = (String) ((HtmlSelectOneMenu)event.getSource()).getValue();
        //System.out.println("iln "+iln);
        
        //System.out.println("iln "+iln);
        List<Etablissement> rcrList = agifService.getRCRByName(name);
        rcrItems.add(new SelectItem("", "--Selectionnez un RCR--"));
        rcrItems.add(new SelectItem("Autre", "Autre"));
       /* rcrItems.add(new SelectItem(inscription.getIdstagiaire().getNumrcr(),
                inscription.getIdstagiaire().getNumrcr()));*/
        for (Etablissement r : rcrList) {
            rcrItems.add(new SelectItem(r.getLibrary(), r.getLibrary()));
        }
        Stagiaire s = inscription.getIdstagiaire();
        this.renderedInputRCR = true;
            s.setAdresse("");
            s.setAdresse2("");
            s.setAdresse3("");
            s.setCodepostal("");
            s.setVille("");
            inscription.setIdstagiaire(s);
    }

    public void changeRCR(AjaxBehaviorEvent event) {
        //this.rcrItems.clear();
        //System.out.println("event "+event.toString());
        //String rcr = inscription.getIdstagiaire().getNumrcr();
        // String rcr = event.getNewValue().toString();
        String rcr = (String) ((HtmlSelectOneMenu)event.getSource()).getValue();
        //System.out.println("iln "+iln);


        if (rcr.compareTo("Autre") == 0) {
            this.renderedInputRCR = true;
            inscription.getIdstagiaire().setAdresse("");
            inscription.getIdstagiaire().setAdresse2("");
            inscription.getIdstagiaire().setAdresse3("");
            inscription.getIdstagiaire().setCodepostal("");
            inscription.getIdstagiaire().setVille("");
        } else {
            Etablissement address = agifService.getAddressByRCR(rcr);
            this.renderedInputRCR = false;
            if (address != null) {
                inscription.getIdstagiaire().setAdresse(address.getAddress());
                inscription.getIdstagiaire().setAdresse2(address.getAddress2());
                inscription.getIdstagiaire().setAdresse3(address.getAddress3());
                inscription.getIdstagiaire().setCodepostal(address.getPostcode());
                inscription.getIdstagiaire().setVille(address.getCity());
                //s.setTelephone((address.getTelephone()).replace(".", ""));

            } else {
                // on remet les champ à vide
                inscription.getIdstagiaire().setAdresse("");
                inscription.getIdstagiaire().setAdresse2("");
                inscription.getIdstagiaire().setAdresse3("");
                inscription.getIdstagiaire().setCodepostal("");
                inscription.getIdstagiaire().setVille("");
            }
        }

    }

    /**
     * liste des sessions
     */
    public String back() {
        return "back";
    }
}
