/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.formation.model.Etablissement;
import org.formation.model.Formateur;
import org.formation.model.FormateurUser;
import org.formation.model.Inscription;
import org.formation.model.Lieu;
import org.formation.model.Question;
import org.formation.model.Reponse;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.Statut;
import org.formation.model.TypeFormation;
import org.formation.model.TypeSession;

/**
 *
 * @author jean-laurent
 */
public interface AgifManageService {

    boolean saveSession(Sessions s);

    boolean saveFormateur(Formateur f);
    
    boolean saveStagiaire(Stagiaire s);

    boolean saveLieu(Lieu l);

    boolean saveTypeFormatation(TypeFormation tf);

    void saveTypeSession(TypeSession ts);

    void saveStatut(Statut st);

    List<Sessions> listSessionsByTypeFormation(TypeFormation tf);
    
    List<Sessions> listSessionsByFormateur(Formateur f);

    boolean paidSession(Sessions s);

    boolean deleteSession(Sessions s);

    boolean destroySession(Sessions s);

    boolean cloneSession(Sessions s);

    boolean archiveSession(Sessions s);

    boolean confirmSession(Sessions s, String mailConfirmation, List<String> mailStagiaires);

    boolean closeSession(Sessions s);

    void browseSession(Sessions s);

    List<Inscription> listInscription(Sessions s);

    List<Inscription> listValidInscriptions(Sessions s);

    boolean validateInscription(Inscription i, Sessions s);

    boolean refuseInscription(Inscription i, Sessions s);

    boolean updateInscription(Inscription i);

    boolean changeAffectation(Inscription i);

    List<Etablissement> listEtablissements(TypeFormation tf);

    List<Sessions> listSessions();
    
    List<Sessions> listAllSessions();

    List<TypeFormation> listTypeFormations();

    List<Lieu> listLieux();

    List<TypeSession> listTypeSessions();

    List<Formateur> listFormateurs();
    
    List<Stagiaire> listStagiaires();


    List<Statut> listStatuts();

    Sessions getSessionsById(Integer id);

    Formateur getFormateurById(Integer id);
    
    FormateurUser getFormateurUserByEmail(String email);
    
    FormateurUser getFormateurUserParToken (String token);
    
    void majMotDePasseFormateurUser (FormateurUser formateurUser);
    
    void incrementeFormateurUserNbTentatives(String email);
    
    void initZeroFormateurUserNbTentatives(String email);
    
    boolean estCompteFormateurUserBloque(String email);
    
    void enregistreFormateurUserToken (String email, String token, Date dateExp);

    Lieu getLieuById(BigDecimal id);

    TypeFormation getTypeFormationById(Short id);

    TypeSession getTypeSessionById(Integer id);

    Inscription getInscriptionById(Integer id);

    Statut getStatutById(Integer id);

    List<Reponse> listReponsesByInscription(Inscription i);

    Inscription searchBestInscription(Sessions s);

    int getCountInscriptionsBySession(Sessions s);

    boolean deleteFormateur(Formateur f);

    boolean deleteLieu(Lieu l);

    boolean deleteTypeFormation(TypeFormation tf);

    public boolean deleteQuestion(Question question);

    public List<Question> listQuestions();

    public boolean saveQuestion(Question question);
    
    boolean deleteStagiaire (Stagiaire s);
    
    boolean deleteInscription (Inscription i);
    
    StringBuffer exportInscription (Sessions s, List<Inscription> inscriptions);
}
