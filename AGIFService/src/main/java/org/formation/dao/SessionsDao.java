/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;

import org.formation.model.Formateur;
import org.formation.model.Sessions;
import org.formation.model.Statut;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public interface SessionsDao extends Serializable {

    void create(Sessions s);

    void edit(Sessions s);

    void destroy(Integer id);
    
    void updateStatut(Integer id,Statut s);
    
    Sessions findSessions(Integer id);
        

    List<Sessions> findSessionsEntities(int maxResults, int firstResult);

    List<Sessions> findSessionsEntities();
    
    List<Sessions> findAllSessionsEntities();

    List<Sessions> findSessionsByTypeFormation(TypeFormation tf);
    
    List<Sessions> findSessionsByFormateur(Formateur f);

    List<Sessions> findSessionsByStatut(Statut s);

    int getSessionsCount();

}
