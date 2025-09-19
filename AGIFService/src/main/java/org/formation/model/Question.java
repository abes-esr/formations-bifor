/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jean-laurent
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idquestion;
    private String libellequestion;
    private String libellechoix1;
    private String libellechoix2;
    private String libellechoix3;
    private String libellechoix4;
    private String uniquemultiple;
    private String specification;
    private String typequestion;
    private List<Reponse> reponseList;
    private TypeFormation idtypeformation;

    public Question() {
    }

    public Question(BigDecimal idquestion) {
        this.idquestion = idquestion;
    }

    public Question(BigDecimal idquestion, String libellequestion,
            String libellechoix1, String libellechoix2) {
        this.idquestion = idquestion;
        this.libellequestion = libellequestion;
        this.libellechoix1 = libellechoix1;
        this.libellechoix2 = libellechoix2;
    }

    public BigDecimal getIdquestion() {
        return idquestion;
    }

    public void setIdquestion(BigDecimal idquestion) {
        this.idquestion = idquestion;
    }

    public String getLibellequestion() {
        return libellequestion;
    }

    public void setLibellequestion(String libellequestion) {
        this.libellequestion = libellequestion;
    }

    public String getLibellechoix1() {
        return libellechoix1;
    }

    public void setLibellechoix1(String libellechoix1) {
        this.libellechoix1 = libellechoix1;
    }

    public String getLibellechoix2() {
        return libellechoix2;
    }

    public void setLibellechoix2(String libellechoix2) {
        this.libellechoix2 = libellechoix2;
    }

    public String getLibellechoix3() {
        return libellechoix3;
    }

    public void setLibellechoix3(String libellechoix3) {
        this.libellechoix3 = libellechoix3;
    }

    public String getLibellechoix4() {
        return libellechoix4;
    }

    public void setLibellechoix4(String libellechoix4) {
        this.libellechoix4 = libellechoix4;
    }

    public String getUniquemultiple() {
        return uniquemultiple;
    }

    public void setUniquemultiple(String uniquemultiple) {
        this.uniquemultiple = uniquemultiple;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getTypequestion() {
        return typequestion;
    }

    public void setTypequestion(String typequestion) {
        this.typequestion = typequestion;
    }

    public List<Reponse> getReponseList() {
        return reponseList;
    }

    public void setReponseList(List<Reponse> reponseList) {
        this.reponseList = reponseList;
    }

    public void addReponse(Reponse r) {
        if (reponseList == null) {
            reponseList = new ArrayList<Reponse>();
        }
        reponseList.add(r);
        r.setIdquestion(this);
    }

    public TypeFormation getIdtypeformation() {
        return idtypeformation;
    }

    public void setIdtypeformation(TypeFormation idtypeformation) {
        this.idtypeformation = idtypeformation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idquestion != null ? idquestion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.idquestion == null && other.idquestion != null)
                || (this.idquestion != null && !this.idquestion.equals(other.idquestion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "org.formation.model.Question[idquestion=" + idquestion + "]";
        return "[" + idquestion + "]";
    }
}
