/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.Statut;

/**
 *
 * @author jean-laurent
 */
public interface StatutDao extends Serializable {

    void create(Statut Statut);

    void edit(Statut Statut);

    void destroy(Integer id);

    Statut findStatut(Integer id);

    List<Statut> findStatutEntities(int maxResults, int firstResult);

    List<Statut> findStatutEntities();

    int getStatutCount();

}
