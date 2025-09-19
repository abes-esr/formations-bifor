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
import org.formation.model.Question;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ListQuestionsBean {
    // Service metier

    private AgifManageService service = new AgifManageServiceImpl();
    // champ d'affichage
    private List<Question> questionItems = new ArrayList<Question>();
    // Binding sur la data table + item selectionne
    private UIData dataTable;
    private Question question;

    @PostConstruct
    public void init() {
        initialisation();
    }

    public void initialisation() {
        questionItems = service.listQuestions();
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Question> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(List<Question> questionItems) {
        this.questionItems = questionItems;
    }

    

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public String modify() {
          // suppression des questions de formation en session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("editQuestionBean");

        // recuperation de la formation à modifier
        question = (Question) dataTable.getRowData();
        return "show";
    }

    /**
     * Creation d'un nouveau type de formation
     */
    public String create() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "new";
    }

    public String delete() {

        // recuperation du formateur à supprimer
        question = (Question) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message = new FacesMessage();
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "La question " + question.toString() + " a été supprimé";
        if (service.deleteQuestion(question)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des sessions
            initialisation();
        } else {
            msg = "Erreur dans la suppression de la question " + question.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }
}
