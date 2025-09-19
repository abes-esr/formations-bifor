/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.service;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import org.formation.model.Etablissement;
import org.formation.model.Inscription;
import org.formation.model.Question;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public interface AgifService extends Serializable {

    List<TypeFormation> listTypeFormation();

    List<Sessions> listAvalaibleSessions(TypeFormation tf);

    Stagiaire searchStagiaireByName(String nom, String prenom);

    List<Question> listQuestions(TypeFormation tf);

    boolean saveInscription(Inscription i);

    List<TypeFormation> listTypeFormationWithoutFonction();

    List<Etablissement> listEtablissements(TypeFormation tf);
    
    List<Etablissement> listEtablissementsSTAR();
    
    List<Etablissement> listEtablissements();

    List<TypeFormation> listTypeFormationWithoutRCR();

    List<TypeFormation> listTypeFormationWithoutCoordinateur();

     List<TypeFormation> listTypeFormationWithService();

    Sessions getSessionsById(Integer id);

    TypeFormation getTypeFormationById(Short id);

    List<Question> listQuestionsByTypeFormation(TypeFormation tf);

    Etablissement getEtablissementByName(String name);

    List<Etablissement> getRCRByName(String name);
    
    Etablissement getAddressByRCR(String rcr);

    Stagiaire getStagiaireByLogin(String identifiant, String motdepasse);

    boolean checkLoginStagiaire(String identifiant);

    boolean checkMailStagiaire(String mail);

    boolean retreivePassword (String mail );
    
    List<Inscription> listInscription(Stagiaire s);
    
    ByteArrayOutputStream exportInscription(Inscription i);
    
}
