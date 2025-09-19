/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.io.Serializable;
import org.formation.web.components.ManageSessions;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Filter;

/**
 *
 * @author jean-laurent
 */
public class SessionsSortingFilteringBean implements Serializable {
     /*
     */
    private static final long serialVersionUID = -6237417487105926855L;
    private SortOrder typeFormationOrder = SortOrder.unsorted;
    private SortOrder intituleOrder = SortOrder.unsorted;
    private SortOrder statutOrder = SortOrder.unsorted;
    private SortOrder dateClotureOrder = SortOrder.unsorted;
    private SortOrder nbInscritsOrder = SortOrder.unsorted;
    
    private String typeFormationFilter;
    private String intituleFilter;
    private String statutFilter;
    private String dateClotureFilter;
    private Long nbInscritsFilter;
    
 
 public Filter<?> getNbInscritsFilterImpl() {
        return new Filter<ManageSessions>() {
            public boolean accept(ManageSessions item) {
                Long nbInscrits = getNbInscritsFilter();
                
                if ((nbInscrits == null)|| 
                        (nbInscrits == 0)||                        
                         (nbInscrits!=item.getNbInscrits())) {                       
                    return true;
                }
                return false;
            }
        };
    }
 
    public Filter<?> getFilterTypeFormation() {
        return new Filter<ManageSessions>() {
            public boolean accept(ManageSessions s) {
                String typeFormation = getTypeFormationFilter();
                if (typeFormation == null || typeFormation.length() == 0 || typeFormation.equals(s.getIdtypeformation().getNom())) {
                    return true;
                }
                return false;
            }
        };
    }
    
    public Filter<?> getFilterStatut() {
        return new Filter<ManageSessions>() {
            public boolean accept(ManageSessions s) {
                String statut = getStatutFilter();
                if (statut == null || statut.length() == 0 || statut.equals(s.getIdstatut().getNom())) {
                    return true;
                }
                return false;
            }
        };
    }
    public void sortByTypeFormation() {
        dateClotureOrder = SortOrder.unsorted;
        intituleOrder = SortOrder.unsorted;
        nbInscritsOrder = SortOrder.unsorted;
        statutOrder= SortOrder.unsorted;
        if (typeFormationOrder.equals(SortOrder.ascending)) {
            setTypeFormationOrder(SortOrder.descending);
        } else {
            setTypeFormationOrder(SortOrder.ascending);
        }
    }
    public void sortByStatut() {
        typeFormationOrder= SortOrder.unsorted;
        dateClotureOrder = SortOrder.unsorted;
        intituleOrder = SortOrder.unsorted;
        nbInscritsOrder = SortOrder.unsorted;
        if (statutOrder.equals(SortOrder.ascending)) {
            setStatutOrder(SortOrder.descending);
        } else {
            setStatutOrder(SortOrder.ascending);
        }
    }
 
     public void sortByIntitule() {
        dateClotureOrder = SortOrder.unsorted;
        typeFormationOrder = SortOrder.unsorted;
        nbInscritsOrder = SortOrder.unsorted;
        statutOrder = SortOrder.unsorted;
        if (intituleOrder.equals(SortOrder.ascending)) {
            setIntituleOrder(SortOrder.descending);
        } else {
            setIntituleOrder(SortOrder.ascending);
        }
    }
     
      public void sortByDateCloture() {
        typeFormationOrder = SortOrder.unsorted;
        intituleOrder = SortOrder.unsorted;
        nbInscritsOrder = SortOrder.unsorted;
        statutOrder = SortOrder.unsorted;
        if (dateClotureOrder.equals(SortOrder.ascending)) {
            setDateClotureOrder(SortOrder.descending);
        } else {
            setDateClotureOrder(SortOrder.ascending);
        }
    }
      
       public void sortByNbInscrits() {
        dateClotureOrder = SortOrder.unsorted;
        intituleOrder = SortOrder.unsorted;
        typeFormationOrder = SortOrder.unsorted;
        statutOrder = SortOrder.unsorted;
        if (nbInscritsOrder.equals(SortOrder.ascending)) {
            setNbInscritsOrder(SortOrder.descending);
        } else {
            setNbInscritsOrder(SortOrder.ascending);
        }
    }
   
    public SortOrder getTypeFormationOrder() {
        return typeFormationOrder;
    }

    public void setTypeFormationOrder(SortOrder typeFormationOrder) {
        this.typeFormationOrder = typeFormationOrder;
    }

    public SortOrder getIntituleOrder() {
        return intituleOrder;
    }

    public void setIntituleOrder(SortOrder intituleOrder) {
        this.intituleOrder = intituleOrder;
    }

    public SortOrder getDateClotureOrder() {
        return dateClotureOrder;
    }

    public void setDateClotureOrder(SortOrder dateClotureOrder) {
        this.dateClotureOrder = dateClotureOrder;
    }

    public SortOrder getNbInscritsOrder() {
        return nbInscritsOrder;
    }

    public void setNbInscritsOrder(SortOrder nbInscritsOrder) {
        this.nbInscritsOrder = nbInscritsOrder;
    }

    public String getTypeFormationFilter() {
        return typeFormationFilter;
    }

    public void setTypeFormationFilter(String typeFormationFilter) {
        this.typeFormationFilter = typeFormationFilter;
    }

    public String getIntituleFilter() {
        return intituleFilter;
    }

    public void setIntituleFilter(String intituleFilter) {
        this.intituleFilter = intituleFilter;
    }

    public String getDateClotureFilter() {
        return dateClotureFilter;
    }

    public void setDateClotureFilter(String dateClotureFilter) {
        this.dateClotureFilter = dateClotureFilter;
    }

    public Long getNbInscritsFilter() {
        return nbInscritsFilter;
    }

    public void setNbInscritsFilter(Long nbInscritsFilter) {
        this.nbInscritsFilter = nbInscritsFilter;
    }

    public SortOrder getStatutOrder() {
        return statutOrder;
    }

    public void setStatutOrder(SortOrder statutOrder) {
        this.statutOrder = statutOrder;
    }

    public String getStatutFilter() {
        return statutFilter;
    }

    public void setStatutFilter(String statutFilter) {
        this.statutFilter = statutFilter;
    }
 
   
}
