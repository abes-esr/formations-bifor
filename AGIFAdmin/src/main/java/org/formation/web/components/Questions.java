/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import java.util.ArrayList;
import javax.faces.model.SelectItem;
import org.formation.model.Question;

/**
 *
 * @author jean-laurent
 */
public class Questions extends Question {
	private static final long serialVersionUID = -829502052885830100L;
	private ArrayList<SelectItem> selectChoix = new ArrayList<SelectItem>();

    public Questions(Question q) {
        this.setIdquestion(q.getIdquestion());
        this.setIdtypeformation(q.getIdtypeformation());
        this.setLibellechoix1(q.getLibellechoix1());
        this.setLibellechoix2(q.getLibellechoix2());
        this.setLibellechoix3(q.getLibellechoix3());
        this.setLibellechoix4(q.getLibellechoix4());
        this.setLibellequestion(q.getLibellequestion());
        this.setReponseList(q.getReponseList());
        this.setSpecification(q.getSpecification());
        this.setTypequestion(q.getTypequestion());
        this.setUniquemultiple(q.getUniquemultiple());
    }

    public void setSelectChoix(ArrayList<SelectItem> selectChoix) {
        this.selectChoix = selectChoix;
    }

    public ArrayList<SelectItem> getSelectChoix() {
        selectChoix.clear();
        selectChoix.add(new SelectItem(super.getLibellechoix1(), 
                super.getLibellechoix1()));
        selectChoix.add(new SelectItem(super.getLibellechoix2(), 
                super.getLibellechoix2()));
        if (super.getLibellechoix3() != null) {
            if (!super.getLibellechoix3().equalsIgnoreCase("vide")) {
                selectChoix.add(new SelectItem(super.getLibellechoix3(),
                        super.getLibellechoix3()));
            }
        }
        if (super.getLibellechoix4() != null) {
            if (!super.getLibellechoix4().equalsIgnoreCase("vide")) {
                selectChoix.add(new SelectItem(super.getLibellechoix4(),
                        super.getLibellechoix4()));
            }
        }
        return selectChoix;
    }
}
