/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.Inscription;
import org.formation.model.Reponse;

/**
 *
 * @author jean-laurent
 */
public interface ReponseDao extends Serializable {

    void create(Reponse formateur);

    void edit(Reponse formateur);

    void destroy(Integer id);

    Reponse findReponse(Integer id);

    List<Reponse> findReponseEntities(int maxResults, int firstResult);

    List<Reponse> findReponseEntities();

    List<Reponse> findReponseByInscription(Inscription i);

    int getReponseCount();

}
