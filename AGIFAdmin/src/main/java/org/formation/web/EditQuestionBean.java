/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.formation.model.Question;
import org.formation.model.TypeFormation;
import org.formation.service.AgifManageService;

/**
 *
 * @author jean-laurent
 */
public class EditQuestionBean {

    // Service metier
    private AgifManageService service;
    // Injections
    private ListQuestionsBean listQuestions;
    // Question en edition
    private Question question ;
    private ArrayList<SelectItem> typeFormationItems = new ArrayList<SelectItem>();
   
    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {
         // recuperation de du formateur en cours
        question = listQuestions.getQuestion();
        if (question == null) {
            question = new Question();
            // initialisations
            question.setUniquemultiple("unique");
            question.setSpecification("false");
        }

        // Liste des types de formation
        List<TypeFormation> typeFormations = service.listTypeFormations();
        for (TypeFormation tf : typeFormations) {
            typeFormationItems.add(new SelectItem(tf,
                    tf.getNom()));
        }
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public ListQuestionsBean getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(ListQuestionsBean listQuestions) {
        this.listQuestions = listQuestions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public ArrayList<SelectItem> getTypeFormationItems() {
        return typeFormationItems;
    }

    public void setTypeFormationItems(ArrayList<SelectItem> typeFormationItems) {
        this.typeFormationItems = typeFormationItems;
    }
      

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String save() {
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La question a bien été enregistré";

        // affectation du type de question
        if ( question.getUniquemultiple().equals("unique") ) {
            question.setTypequestion("selectOneRadio");
        }else {
            question.setTypequestion("selectManyCheckbox");
        }
        // validation de l'enresgitrement
        if (service.saveQuestion(question)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        } else {
            msg = "Erreur dans l'enregistrement de la question ";
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
}
