/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;

/**
 *
 * @author jean-laurent
 */
public interface InscriptionDao extends Serializable {

    void create(Inscription Inscription);

    void edit(Inscription Inscription);

    void destroy(Integer id);

    void updateEtatPrioritaire(Integer id, String e);

    void updateEtatSecondaire(Integer id, String e);

    Inscription findInscription(Integer id);

    List<Inscription> findInscriptionEntities(int maxResults, int firstResult);

    List<Inscription> findInscriptionEntities();

    List<Inscription> findInscriptionPrioritaireBySession(Sessions s);

    List<Inscription> findInscriptionBySession(Sessions s);
    
    List<Inscription> findInscriptionByStagiaire(Stagiaire s);
    

    List<Inscription> findInscriptionSecondaireBySession(Sessions s);

    int getInscriptionCount();

    int getCountInscriptionBySession(Sessions s);
}
