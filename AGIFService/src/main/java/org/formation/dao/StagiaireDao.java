/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.Stagiaire;

/**
 *
 * @author jean-laurent
 */
public interface StagiaireDao extends Serializable {

    void create(Stagiaire s);

    void edit(Stagiaire s);

    void destroy(Integer id);

    Stagiaire findStagiaire(Integer id);

    Stagiaire findStagiaireByNomPrenom(String nom, String prenom);

    Stagiaire findStagiaireByIdentifiantMdp(String identifiant, String motdepasse);
    
    List<Stagiaire> findStagiaireEntities(int maxResults, int firstResult);

    List<Stagiaire> findStagiaireEntities();

    int getStagiaireCount();

    int getStagiaireCountByIdentifiant(String identifiant);

    int getStagiaireCountByMail(String mail);

    Stagiaire findStagiaireByMail(String mail);
    
    Stagiaire findStagiaireByIdentifiant(String identifiant);
    

}
