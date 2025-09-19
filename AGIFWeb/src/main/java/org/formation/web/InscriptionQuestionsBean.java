/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import org.formation.model.Question;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;
import org.formation.web.components.Questions;
import org.formation.web.components.Reponses;

/**
 *
 * @author jean-laurent
 */
@ManagedBean(name="inscriptionQuestionsBean")
@SessionScoped
public class InscriptionQuestionsBean extends AbstractBackingBean {
	private static final long serialVersionUID = -3222472699746926507L;
	
	// Service metier
    private AgifService service = new AgifServiceImpl();
    @ManagedProperty(value="#{demande}")
    private DemandeBean demande;

    // champ d'affichage
    private List<Reponses> reponseItems = new ArrayList<Reponses>();
    private List<Question> questions = new ArrayList<Question>();

    // Binding sur la data table + item selectionne
    private UIData dataTable;

    @PostConstruct
    public void init() {        
        questions = service.listQuestionsByTypeFormation(demande.getTypeFormation());
        for (Question x : questions) {
            Reponses reponse = new Reponses();
            Questions q = new Questions(x);
            reponse.setIdquestion(q);
            reponseItems.add(reponse);
        }
        demande.setReponses(reponseItems);

    }

    public List<Reponses> getReponseItems() {
        reponseItems.clear();
        return reponseItems;
    }

    public void setReponseItems(List<Reponses> reponseItems) {
        this.reponseItems = reponseItems;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
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

    public String next() {

        return "success";
    }
}
