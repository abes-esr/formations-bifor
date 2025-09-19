/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.formation.model.Reponse;

/**
 *
 * @author jean-laurent
 */
public class Reponses extends Reponse {
	private static final long serialVersionUID = 2774220126532421457L;
	
	private List<String> listeChoix = new ArrayList<String>();
    private Date dateSpecification;
    private Questions idquestion;

    public Date getDateSpecification() {
        return dateSpecification;
    }

    public void setDateSpecification(Date dateSpecification) {
        this.dateSpecification = dateSpecification;
    }

    @Override
    public Questions getIdquestion() {
        return idquestion;
    }

    public void setIdquestion(Questions idquestion) {
        this.idquestion = idquestion;
    }

    public List<String> getListeChoix() {
        return listeChoix;
    }

    public void setListeChoix(List<String> listeChoix) {
        this.listeChoix = listeChoix;
    }
}
