/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.formation.constant.Constant;
import org.formation.dao.EtablissementDao;
import org.formation.dao.FormateurDao;
import org.formation.dao.FormateurUserDao;
import org.formation.dao.InscriptionDao;
import org.formation.dao.LieuDao;
import org.formation.dao.QuestionDao;
import org.formation.dao.ReponseDao;
import org.formation.dao.SessionsDao;
import org.formation.dao.StagiaireDao;
import org.formation.dao.StatutDao;
import org.formation.dao.TypeFormationDao;
import org.formation.dao.TypeSessionDao;
import org.formation.dao.impl.AbstractDao;
import org.formation.dao.impl.EtablissementDaoImpl;
import org.formation.dao.impl.FormateurDaoImpl;
import org.formation.dao.impl.FormateurUserDaoImpl;
import org.formation.dao.impl.InscriptionDaoImpl;
import org.formation.dao.impl.LieuDaoImpl;
import org.formation.dao.impl.QuestionDaoImpl;
import org.formation.dao.impl.ReponseDaoImpl;
import org.formation.dao.impl.SessionsDaoImpl;
import org.formation.dao.impl.StagiaireDaoImpl;
import org.formation.dao.impl.StatutDaoImpl;
import org.formation.dao.impl.TypeFormationDaoImpl;
import org.formation.dao.impl.TypeSessionDaoImpl;
import org.formation.mail.AgifMail;
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
import org.formation.service.AgifManageService;
import org.formation.service.AgifService;

/**
 *
 * @author jean-laurent
 */
public class AgifManageServiceImpl extends AbstractDao implements AgifManageService {

    private SessionsDao sessionsDao = new SessionsDaoImpl();
    private StagiaireDao stagiaireDao = new StagiaireDaoImpl();
    private InscriptionDao inscriptionDao = new InscriptionDaoImpl();
    private LieuDao lieuDao = new LieuDaoImpl();
    private StatutDao statutDao = new StatutDaoImpl();
    private FormateurDao formateurDao = new FormateurDaoImpl();
    private FormateurUserDao formateurUserDao = new FormateurUserDaoImpl();
    private TypeFormationDao typeFormationDao = new TypeFormationDaoImpl();
    private TypeSessionDao typeSessionDao = new TypeSessionDaoImpl();
    private ReponseDao reponseDao = new ReponseDaoImpl();
    private QuestionDao questionDao = new QuestionDaoImpl();
    private EtablissementDao etablissementDao = new EtablissementDaoImpl();

    public boolean saveSession(Sessions s) {
        Connection connection = null;

        // test du parametre d'entrée
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (s.getIdsessions() == null) {
                // identifiant de la session
                s.setIdsessions(Integer.parseInt(generateId()));
                // statut de la session
                Statut statut = new Statut();
                statut.setIdstatut(Constant.OUVERTE);
                s.setIdstatut(statut);
                // date de confirmation
                // s.setDateconfirmation(new java.util.Date());
                // etat de la session
                s.setEtat(Constant.ENCOURS);

                sessionsDao.create(s);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Creation de la session" + s.toString() + " OK");
            } else {
                sessionsDao.edit(s);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour de la session" + s.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean saveLieu(Lieu l) {
        Connection connection = null;

        // test du parametre d'entrée
        if (l == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (l.getIdlieu() == null) {
                // identifiant du lieu
                l.setIdlieu(BigDecimal.valueOf(Long.parseLong(generateId())));

                lieuDao.create(l);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Creation du lieu" + l.toString() + " OK");
            } else {
                lieuDao.edit(l);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour du lieu" + l.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean saveTypeFormatation(TypeFormation tf) {
        Connection connection = null;

        // test du parametre d'entrée
        if (tf == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (tf.getIdtypeformation() == null) {
                // identifiant du type de formation
                tf.setIdtypeformation(Short.valueOf(generateId().substring(1, 4)));

                typeFormationDao.create(tf);

                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Creation du type de formation" + tf.toString() + " OK");
            } else {
                typeFormationDao.edit(tf);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour du type de formation" + tf.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public void saveTypeSession(TypeSession ts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveStatut(Statut st) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Sessions> listSessions() {

        List<Sessions> listSessions = new ArrayList<Sessions>();

        for (Sessions s : sessionsDao.findSessionsEntities()) {

            /*List<Inscription> inscriptions =
             inscriptionDao.findInscriptionPrioritaireBySession(s);
             Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
             "Recherche d'inscriptions prioritaires "
             + "pour la sessions " + s.toString() + " OK");
             for (Inscription i : inscriptions) {
             // sessions prioritaires
             if (Constant.VALIDE.equals(i.getEtat())) {
             s.addInscription(i);
             Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
             "Ajout de l'inscription prioritaire "
             + i.toString() + " OK");
             }
            
             }*/
            // liste des sesions secondaires
           /* List<Inscription> inscriptionsSecondaire =
             inscriptionDao.findInscriptionSecondaireBySession(s);
             Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
             "Recherche d'inscriptions secondaires "
             + "pour la sessions " + s.toString() + " OK");
            
             for (Inscription i : inscriptionsSecondaire) {
             // sessions secondaires
             if (Constant.VALIDE.equals(i.getEtat2())) {
             s.addInscription2(i);
             Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
             "Ajout de l'inscription secondaire "
             + i.toString() + " OK");
             }
             }*/
            listSessions.add(s);
        }
        return listSessions;
    }

    /*public List<Inscription> listInscription(Sessions s) {
     if (s == null) {
     Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
     "Paramètre d'entrée vide");
     return null;
     }
     List<Inscription> inscriptions = new ArrayList<Inscription>();
    
     // traitement des inscriptions prioritaires
     List<Inscription> inscriptionsPrioritaires =
     inscriptionDao.findInscriptionPrioritaireBySession(s);
     for (Inscription i : inscriptionsPrioritaires) {
    
     // on ajoute pas une isncription si elle est déjà validée dans une autre session
     if (!Constant.VALIDE.equals(i.getEtat2())) {
     // on recupere les reponses pour cette inscription
     List<Reponse> reponses = reponseDao.findReponseByInscription(i);
     if (reponses != null) {
     i.setReponseList(reponses);
     Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
     "Ajout de la liste des réponses à l'inscription "
     + i.toString() + " OK");
     }
     inscriptions.add(i);
     Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
     "Ajout de l'inscription prioritaire "
     + i.toString() + " OK");
     }
     }
     // traitement des inscriptions secondaires
     List<Inscription> inscriptionsSecondaires =
     inscriptionDao.findInscriptionSecondaireBySession(s);
    
     for (Inscription i : inscriptionsSecondaires) {
     // on ajoute pas une inscription si elle est déjà validée dans une autre session
     if (!Constant.VALIDE.equals(i.getEtat())) {
     // on recupere les reponses pour cette inscription
     List<Reponse> reponses = reponseDao.findReponseByInscription(i);
     if (reponses != null) {
     i.setReponseList(reponses);
     Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
     "Ajout de la liste des réponses à l'inscription "
     + i.toString() + " OK");
     }
    
     inscriptions.add(i);
     Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
     "Ajout de l'inscription secondaire "
     + i.toString() + " OK");
     }
     }
    
    
     return inscriptions;
     }*/
    public List<Inscription> listInscription(Sessions s) {
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        List<Inscription> inscriptions = new ArrayList<Inscription>();

        // traitement des inscriptions prioritaires
        List<Inscription> inscriptionsSessions = inscriptionDao.findInscriptionBySession(s);
        for (Inscription i : inscriptionsSessions) {

            // on ajoute pas une inscription si elle est déjà validée dans une autre session
            /*if ((!Constant.VALIDE.equals(i.getEtat2()))
             || (!Constant.VALIDE.equals(i.getEtat()))) {
             inscriptions.add(i);
             Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
             "Ajout de l'inscription "
             + i.toString() + " OK");
             }*/
            // si on affiche la session 1 et que la session2 n'est pas validée ou
            // qu'on affiche la session 2 et que la session 1 n'est pas validée alors
            // on ajoute l'inscription
            if ((((i.getIdsessions1()).equals(s)) && (!Constant.VALIDE.equals(i.getEtat2())))
                    || (((i.getIdsessions2()).equals(s)) && (!Constant.VALIDE.equals(i.getEtat())))) {
                i.setIdsessions1(sessionsDao.findSessions(i.getIdsessions1().getIdsessions()));
                i.setIdsessions2(sessionsDao.findSessions(i.getIdsessions2().getIdsessions()));

                inscriptions.add(i);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Ajout de l'inscription "
                        + i.toString() + " OK");
            }
        }


        return inscriptions;
    }

    public boolean paidSession(Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();

            // envoi d'un mail au GAPP
            AgifMail.sendPaiement(s);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Envoi du mail de paiement pour la sessions "
                    + s.toString() + " OK");

            // mise à jour du statut
            Statut statut = new Statut();
            statut.setIdstatut(Constant.PAYEE);
            sessionsDao.updateStatut(s.getIdsessions(), statut);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise a jour du statut " + statut.toString() + " OK");


            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean deleteSession(Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            List<Inscription> inscriptions = new ArrayList<Inscription>();

            // recuperation des inscriptions de la sessions prioritaire
            List<Inscription> inscriptionsPrioritaires =
                    inscriptionDao.findInscriptionPrioritaireBySession(s);
            if (inscriptionsPrioritaires != null) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Recherche d'inscriptions prioritaires "
                        + "pour la sessions " + s.toString() + " OK");

                for (Inscription i : inscriptionsPrioritaires) {
                    // sessions prioritaires
                    if (Constant.VALIDE.equals(i.getEtat())) {
                        inscriptions.add(i);
                        Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                                "Ajout de l'inscription prioritaire "
                                + i.toString() + " OK");
                    }
                }
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Recuperation des inscriptions de la sessions prioritaire "
                        + s.toString() + " OK");
            }
            // recuperation des inscriptions de la sessions secondaire
            List<Inscription> inscriptionsSecondaires =
                    inscriptionDao.findInscriptionSecondaireBySession(s);
            if (inscriptionsSecondaires != null) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Recherche d'inscriptions secondaires "
                        + "pour la sessions " + s.toString() + " OK");

                for (Inscription i : inscriptionsSecondaires) {
                    // sessions secondaires
                    if (Constant.VALIDE.equals(i.getEtat2())) {
                        inscriptions.add(i);
                        Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                                "Ajout de l'inscription secondaire "
                                + i.toString() + " OK");
                    }
                }
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Recuperation des inscriptions de la sessions secondaire "
                        + s.toString() + " OK");
            }
            // envoi d'un mail à chaque stagiaire
            for (Inscription i : inscriptions) {
                AgifMail.sendSuppression(s, i);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Envoi du mail de suppression au stagiaire "
                        + i.getIdstagiaire().toString() + " OK");
            }
            // mise à jour du statut
            Statut statut = new Statut();
            statut.setIdstatut(Constant.SUPPRIME);
            sessionsDao.updateStatut(s.getIdsessions(), statut);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise a jour du statut " + statut.toString() + " OK");


            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean archiveSession(Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // mise à jour du statut
            Statut statut = new Statut();
            statut.setIdstatut(Constant.ARCHIVE);
            sessionsDao.updateStatut(s.getIdsessions(), statut);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise a jour du statut " + statut.toString() + " OK");

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean validateInscription(Inscription i, Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée session vide");
            return false;
        }
        if (i == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée inscription vide");
            return false;
        }
        // on verifie que la session est ouverte
        if (Constant.OUVERTE != s.getIdstatut().getIdstatut()) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "La session de cette inscription n'est pas ouverte");
            return false;
        }
        // on teste le nombre d'inscrits
        int nbInscrits = getCountInscriptionsBySession(s);

        if (nbInscrits >= s.getNbpersonnemax()) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "La nombre d'inscrits " + nbInscrits + "pour cette session est "
                    + "supérieur au nombre de place disponibles " + s.getNbpersonnemax());
            return false;
        }

        try {
            connection = getConnection();
            // mise à jour du statut de l'inscription prioritaire
            if (i.getIdsessions1().equals(s)) {
                // on verifie que l'inscription n'est pas validée pour une autre session
                if (Constant.VALIDE.equals(i.getEtat2())) {
                    Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                            "Impossible de valider cette inscription car elle l'est déjà "
                            + " en choix secondaire "
                            + i.toString() + " OK");
                    return false;
                }

                inscriptionDao.updateEtatPrioritaire(i.getIdinscription(), Constant.VALIDE);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise a jour du statut de l'inscription  prioritaire "
                        + i.toString() + " OK");

            } else if (i.getIdsessions2().equals(s)) {
                // mise à jour du statut de l'inscription secondaire
                if (Constant.VALIDE.equals(i.getEtat())) {
                    Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                            "Impossible de valider cette inscription car elle l'est déjà "
                            + " en choix prioritaire "
                            + i.toString() + " OK");
                    return false;
                }
                inscriptionDao.updateEtatSecondaire(i.getIdinscription(), Constant.VALIDE);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise a jour du statut de l'inscription secondaire "
                        + i.toString() + " OK");
            }



            connection.commit();
            // on cloture la session su le nombre d'inscrits est atteint
            if (s.getNbpersonnemax() == nbInscrits + 1) {
                closeSession(s);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Cloture de la session :" + s.toString() + " OK");
            }

            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean refuseInscription(Inscription i, Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée session vide");
            return false;
        }
        if (i == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée inscription vide");
            return false;
        }
        try {
            connection = getConnection();
            // envoi du mail au stagiaire pour le prevnir du refus
            AgifMail.sendAnnulation(i, s);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "envoi du mail au stagiaire pour le prevenir du refus "
                    + i.getIdstagiaire().toString() + " OK");

            // mise à jour du statut de l'inscription
            if (i.getIdsessions1().equals(s)) {
                inscriptionDao.updateEtatPrioritaire(i.getIdinscription(), Constant.REFUSE);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise a jour du statut de l'inscription  prioritaire "
                        + i.toString() + " OK");
            }
            if (i.getIdsessions2().equals(s)) {
                inscriptionDao.updateEtatSecondaire(i.getIdinscription(), Constant.REFUSE);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise a jour du statut de l'inscription secondaire "
                        + i.toString() + " OK");
            }

            // recherche de la meilleur inscription secondaire possible
            Inscription inscription = this.searchBestInscription(s);

            // envoi du mail de proposition de changement d'affectation
            if (inscription != null) {
                AgifMail.sendModification(inscription, s);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "envoi du mail de proposition de changement d'affectation "
                        + s.toString() + " OK");
            }


            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public void editSession(Sessions s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean closeSession(Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée session vide");
            return false;
        }

        try {
            connection = getConnection();
            // mise à jour du statut
            Statut statut = new Statut();
            statut.setIdstatut(Constant.CLOTURE);
            sessionsDao.updateStatut(s.getIdsessions(), statut);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise a jour du statut " + statut.toString() + " OK");

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public void browseSession(Sessions s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Etablissement> listEtablissements(TypeFormation tf) {
        if (tf == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée typeformation vide");
            return null;
        }

        //List<String> etablissements = new ArrayList<String>();
        List<Etablissement> etablissements = new ArrayList<Etablissement>();
        List<Etablissement> etablissementList = etablissementDao.findAllEtablissementEntities();
        AgifServiceImpl service = new AgifServiceImpl();

        if (Constant.getFormationsAutorites().contains(tf.getIdtypeformation().intValue())) {
            for (Etablissement e : etablissementList) {
                //cas des établissements SUDOC
                if ((e.getIln() < 200) || (e.getIln() >= 400) || (e.getIln() == 300)) {
                    etablissements.add(e);
                }
            }
            //on ajoute les etablissements STAR
            List<Etablissement> etablissementsSTAR = service.listEtablissementsSTAR();
            etablissements.addAll(etablissementsSTAR);
            return etablissements;
        }
        //return service.listEtablissements(tf);
        // Pour une formation de coordinateur de centre on prend les ILN supérieur à 200
        if (Constant.getFormationsSudocPS().contains(tf.getIdtypeformation().intValue())) {
            for (Etablissement e : etablissementList) {
                if ((e.getIln() >= 200) && (e.getIln() < 299)) {
                    etablissements.add(e);
                }
            }
        } else if (Constant.getFormationsStar().contains(tf.getIdtypeformation().intValue())) {
            List<Etablissement> etablissementsSTAR = service.listEtablissementsSTAR();
            etablissements.addAll(etablissementsSTAR);
        } else if (Constant.getFormationsCalames().contains(tf.getIdtypeformation().intValue())) {
            // ILN virtuel = 300 
            for (Etablissement e : etablissementList) {
                if (e.getIln() == 300) {
                    etablissements.add(e);
                }
            }
        } else {
            // ILN inférieur a 200 et supérieur a 400
            for (Etablissement e : etablissementList) {
                if ((e.getIln() < 200) || (e.getIln() >= 400)) {
                    etablissements.add(e);
                }
            }
        }
        return etablissements;
    }

    public List<Sessions> listSessionsByTypeFormation(TypeFormation tf) {
        if (tf == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée typeformation vide");
            return null;
        }
        List<Sessions> listSessions = new ArrayList<Sessions>();

        for (Sessions s : sessionsDao.findSessionsEntities()) {
            if (s.getIdtypeformation().equals(tf)) {
                listSessions.add(s);
            }
        }
        return listSessions;
    }
    
    /**
     * retourne la liste des sessions de type "sudoc, winibw" pour un formateur donné
     */
    @Override
    public List<Sessions> listSessionsByFormateur(Formateur f) {
        if (f == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée formateur vide");
            return null;
        }
        List<Sessions> listSessions = new ArrayList<Sessions>();

        for (Sessions s : sessionsDao.findSessionsByFormateur(f)) {
                listSessions.add(s);
        }
        return listSessions;
    }

    public boolean updateInscription(Inscription i) {
        Connection connection = null;
        if (i == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée inscription vide");
            return false;
        }
        try {
            connection = getConnection();

            // Mise à jour de l'inscription
            InscriptionDao idao = new InscriptionDaoImpl();
            idao.edit(i);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise à jour de l'inscription " + i.toString() + " OK");

            // Mise à jour du stagiaire
            StagiaireDao sdao = new StagiaireDaoImpl();
            sdao.edit(i.getIdstagiaire());

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise à jour du stagiaire " + i.getIdstagiaire().toString() + " OK");

            // mise à jour des reponses en base de données
            ReponseDao rdao = new ReponseDaoImpl();
            // inserer les reponses
            for (Reponse r : i.getReponseList()) {
                rdao.edit(r);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour de la reponse " + r.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (RuntimeException re) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, re);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "Numero Inscription :" + i.getIdinscription().toString());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "Stagiaire :" + i.getIdstagiaire().getNom() + " "
                    + i.getIdstagiaire().getPrenom() + " "
                    + i.getIdstagiaire().getFonction() + " "
                    + i.getIdstagiaire().getAdresse() + " "
                    + i.getIdstagiaire().getVille() + " "
                    + i.getIdstagiaire().getNumrcr() + " "
                    + i.getIdstagiaire().getEtablissement() + " "
                    + i.getIdstagiaire().getMail());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "Session1 :" + i.getIdsessions1().getTitre());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "Session2 :" + i.getIdsessions2().getTitre());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            return false;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean confirmSession(Sessions s, String mailConfirmation, List<String> idStagiaires) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée session vide");
            return false;
        }

        try {
            connection = getConnection();

            // parcours de la liste des stagiaires
            int i = 0;

            for (i = 0; i < idStagiaires.size(); i++) {
                int idstagiaire = java.lang.Integer.parseInt(idStagiaires.get(i).trim());
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO, "Identifiant de stagiaire " + idstagiaire + " OK");

                Stagiaire stagiaire = stagiaireDao.findStagiaire(idstagiaire);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO, "Recuperation du stagiaire " + stagiaire.toString() + " OK");

                if (AgifMail.sendConfirmation(s, stagiaire, mailConfirmation)) {
                    Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO, "Envoi du mail de confirmation a " + stagiaire.getMail() + " OK");
                }
            }

            // mise à jour de la date de confirmation de la session
            Sessions sessionUpdate = sessionsDao.findSessions(s.getIdsessions());
            sessionUpdate.setDateconfirmation(new java.util.Date());
            sessionsDao.edit(sessionUpdate);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO, "Mise a jour de la date de confirmation de la session " + s.toString() + " OK");

            connection.commit();

            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE, null, sex);
            }
            return false;
        } finally {
            close(connection);
        }

    }

    public List<TypeFormation> listTypeFormations() {
        return typeFormationDao.findTypeFormationEntities();
    }

    public List<Lieu> listLieux() {
        return lieuDao.findLieuEntities();
    }

    public List<TypeSession> listTypeSessions() {
        return typeSessionDao.findTypeSessionEntities();
    }

    public List<Formateur> listFormateurs() {
        return formateurDao.findFormateurEntities();
    }

    public Sessions getSessionsById(Integer id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return sessionsDao.findSessions(id);
    }

    public Formateur getFormateurById(Integer id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return formateurDao.findFormateur(id);
    }
    
    // ************ méthodes pour le compte formateur ********************
    @Override
    public FormateurUser getFormateurUserByEmail(String email) {
        if (email == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide pour getFormateurUserByEmail");
            return null;
        }
        return formateurUserDao.findFormateurUser(email);
    }
    @Override
    public FormateurUser getFormateurUserParToken(String token) {
        if (token == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide pour getFormateurUserParToken");
            return null;
        }
        return formateurUserDao.findFormateurUserParToken(token);
    }
    
    @Override
    public void incrementeFormateurUserNbTentatives(String email) {
        if (email == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide pour incrementeFormateurUserNbTentatives");
        }
        formateurUserDao.incrementeFormateurUserNbTentatives(email);
    }
    
    @Override
    public void initZeroFormateurUserNbTentatives(String email) {
        if (email == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide pour initZeroFormateurUserNbTentatives");
        }
        formateurUserDao.initZeroFormateurUserNbTentatives(email);
    }
    
    @Override
    public boolean estCompteFormateurUserBloque(String email) {
        if (email == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide pour estCompteFormateurUserBloque");
        }
        return formateurUserDao.estCompteFormateurUserBloque(email);
    }
    
    @Override
    public void enregistreFormateurUserToken (String email, String token, Date dateExp)
    {
    	if (email == null || token == null || dateExp == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "au moins un des paramètres d'entrée est vide pour enregistreFormateurUserToken");
        }
        formateurUserDao.enregistreToken(email, token, dateExp);
    }
    @Override
    public void majMotDePasseFormateurUser (FormateurUser formateurUser)
    {
    	if (formateurUser.getUsername() == null || formateurUser.getPassword() == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "au moins un des paramètres d'entrée est vide pour majMotDePasseFormateurUser");
        }
        formateurUserDao.majMotDePasse(formateurUser);
    }
    
    
 // ************ fin méthodes pour le compte formateur ********************

    public Lieu getLieuById(BigDecimal id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return lieuDao.findLieu(id);
    }

    public TypeFormation getTypeFormationById(Short id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return typeFormationDao.findTypeFormation(id);
    }

    public TypeSession getTypeSessionById(Integer id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return typeSessionDao.findTypeSession(id);
    }

    public Inscription getInscriptionById(Integer id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return inscriptionDao.findInscription(id);
    }

    public Statut getStatutById(Integer id) {
        if (id == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        return statutDao.findStatut(id);
    }

    public List<Reponse> listReponsesByInscription(Inscription i) {
        if (i == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée inscription vide");
            return null;
        }
        return reponseDao.findReponseByInscription(i);
    }

    public Inscription searchBestInscription(Sessions s) {
        List<Inscription> inscriptions =
                inscriptionDao.findInscriptionPrioritaireBySession(s);
        for (Inscription i : inscriptions) {
            // on retourne la premère inscription prioritaire en cours
            if ((Constant.ENCOURS.equals(i.getEtat()))
                    && (Constant.VALIDE.equals(i.getEtat2()))) {
                return i;
            }
        }
        return null;

    }

    public int getCountInscriptionsBySession(Sessions s) {
        return inscriptionDao.getCountInscriptionBySession(s);
    }

    public List<Inscription> listValidInscriptions(Sessions s) {
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        List<Inscription> inscriptions = new ArrayList<Inscription>();

        // traitement des inscriptions prioritaires
        List<Inscription> inscriptionsPrioritaires =
                inscriptionDao.findInscriptionPrioritaireBySession(s);
        for (Inscription i : inscriptionsPrioritaires) {
            // on ajoute pas une isncription si elle est déjà validée dans une autre session
            if (Constant.VALIDE.equals(i.getEtat())) {
                inscriptions.add(i);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Ajout de l'inscription prioritaire validé"
                        + i.toString() + " OK");
            }
        }
        // traitement des inscriptions secondaires
        List<Inscription> inscriptionsSecondaires =
                inscriptionDao.findInscriptionSecondaireBySession(s);

        for (Inscription i : inscriptionsSecondaires) {
            // on ajoute pas une inscription si elle est déjà validée dans une autre session
            if (Constant.VALIDE.equals(i.getEtat2())) {
                inscriptions.add(i);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Ajout de l'inscription secondaire validée"
                        + i.toString() + " OK");
            }
        }
        return inscriptions;
    }

    public boolean changeAffectation(Inscription i) {
        Connection connection = null;

        // test du parametre d'entrée
        if (i == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (i.getIdinscription() == null) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        "Identifiant de l'inscription vide");
                return false;
            }
            // on valide la session prioritaire
            i.setEtat(Constant.VALIDE);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "l'inscription pour la session " + i.getIdsessions1().toString()
                    + " est validée");
            // on signale la modification de la session secondaire
            i.setEtat2(Constant.MODIFIEE);
            // modification de l'inscription
            inscriptionDao.edit(i);
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Mise à jour de l'inscription" + i.toString() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);


        }
    }

    public boolean deleteFormateur(Formateur f) {
        Connection connection = null;
        if (f == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression du formateur
            formateurDao.destroy(f.getIdformateur());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression du formateur " + f.getPrenom() + " " + f.getNom()
                    + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean saveFormateur(Formateur f) {
        Connection connection = null;

        // test du parametre d'entrée
        if (f == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (f.getIdformateur() == null) {
                // identifiant de la session
                f.setIdformateur(Integer.parseInt(generateId()));

                formateurDao.create(f);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Creation du formateur" + f.toString() + " OK");
            } else {
                formateurDao.edit(f);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour du formateur" + f.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean deleteLieu(Lieu l) {
        Connection connection = null;
        if (l == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression du formateur
            lieuDao.destroy(l.getIdlieu());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression du lieu " + l.getNom() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean destroySession(Sessions s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression de la session en base
            sessionsDao.destroy(s.getIdsessions());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression de la session" + s.toString() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean cloneSession(Sessions s) {
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        // on reinitialise l'id de la session
        s.setIdsessions(null);
        // on modifie le titre
        s.setTitre(s.getTitre() + " (Copie)");
        // on cree une nouvelle session
        if (this.saveSession(s) == false) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    "Erreur dans la duplication de la session " + s.toString());
            return false;
        }
        Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                "Duplication de la session " + s.toString() + " OK");
        return true;
    }

    public List<Statut> listStatuts() {
        return statutDao.findStatutEntities();
    }

    public boolean deleteTypeFormation(TypeFormation tf) {
        Connection connection = null;
        if (tf == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression du formateur
            typeFormationDao.destroy(tf.getIdtypeformation());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression du type de formation " + tf.getNom() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean deleteQuestion(Question q) {
        Connection connection = null;
        if (q == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression de la question
            questionDao.destroy(q.getIdquestion());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression de la question " + q.getLibellequestion() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public List<Question> listQuestions() {
        return questionDao.findQuestionEntities();
    }

    public boolean saveQuestion(Question q) {
        Connection connection = null;

        // test du parametre d'entrée
        if (q == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (q.getIdquestion() == null) {
                // identifiant du type de formation
                q.setIdquestion(BigDecimal.valueOf(Long.parseLong(generateId())));

                questionDao.create(q);

                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Creation de la question" + q.toString() + " OK");
            } else {
                questionDao.edit(q);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour de la question" + q.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean deleteStagiaire(Stagiaire s) {
        Connection connection = null;
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression de la question
            stagiaireDao.destroy(s.getIdstagiaire());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression du stagiaire " + s.getPrenom() + " " + s.getNom() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public boolean deleteInscription(Inscription i) {
        Connection connection = null;
        if (i == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }
        try {
            connection = getConnection();
            // suppression de la question
            inscriptionDao.destroy(i.getIdinscription());
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Suppression de l'isncription " + i.getIdinscription() + " OK");
            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public StringBuffer exportInscription(Sessions s, List<Inscription> inscriptions) {
        AgifService service = new AgifServiceImpl();
        StringBuffer sb = new StringBuffer();
        if (inscriptions == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return sb;
        }
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return sb;
        }
        // recuperation du type de formation
        TypeFormation tf = s.getIdtypeformation();

        // creation du fichier
        // en tête de colonne
        sb.append("CIVILITE");
        sb.append(Constant.SEPARATEUR_EXPORT);
        sb.append("NOM");
        sb.append(Constant.SEPARATEUR_EXPORT);
        sb.append("PRENOM");
        sb.append(Constant.SEPARATEUR_EXPORT);
        sb.append("ETABLISSEMENT");
        sb.append(Constant.SEPARATEUR_EXPORT);
        // ajout de l'information RCR et ILN pour les formations non STAR
        List<TypeFormation> typeFormationsStar = service.listTypeFormationWithoutRCR();
        if (!typeFormationsStar.contains(tf)) {
            sb.append("ILN");
            sb.append(Constant.SEPARATEUR_EXPORT);
            sb.append("RCR");
            sb.append(Constant.SEPARATEUR_EXPORT);
        }
        sb.append("MAIL");
        sb.append(Constant.SEPARATEUR_EXPORT);
        sb.append("FONCTION");
        sb.append(Constant.SEPARATEUR_EXPORT);

        // ajout des questions complementaires
        List<Question> questions = questionDao.findQuestionByTypeFormation(tf);
        if (!questions.isEmpty()) {
            for (Question q : questions) {
                sb.append(q.getLibellequestion());
                sb.append(Constant.SEPARATEUR_EXPORT);
            }
        }
        sb.append("\n");
        Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                "Creation de l'entete du fichier d'export : " + sb.toString() + " OK");
        
        inscriptions.sort(
                (Inscription arg0, Inscription arg1) -> (arg0.getIdstagiaire().getNom() + arg0.getIdstagiaire().getPrenom()).compareToIgnoreCase(arg1.getIdstagiaire().getNom() + arg1.getIdstagiaire().getPrenom())
        );

        // ligne du fichier
        for (Inscription i : inscriptions) {
            StringBuffer sbLine = new StringBuffer();
            Stagiaire stagiaire = i.getIdstagiaire();
            sbLine.append(stagiaire.getCivilite());
            sbLine.append(Constant.SEPARATEUR_EXPORT);
            sbLine.append(stagiaire.getNom());
            sbLine.append(Constant.SEPARATEUR_EXPORT);
            sbLine.append(stagiaire.getPrenom());
            sbLine.append(Constant.SEPARATEUR_EXPORT);
            sbLine.append(stagiaire.getEtablissement());
            sbLine.append(Constant.SEPARATEUR_EXPORT);
            // ajout de l'information RCR et ILN pour les formations non STAR
            if (!typeFormationsStar.contains(tf)) {
                Etablissement etablissement = etablissementDao.findEtablissementByName(stagiaire.getEtablissement());
                sbLine.append(etablissement.getIln());
                sbLine.append(Constant.SEPARATEUR_EXPORT);
                sbLine.append(stagiaire.getNumrcr());
                sbLine.append(Constant.SEPARATEUR_EXPORT);
            }
            sbLine.append(stagiaire.getMail());
            sbLine.append(Constant.SEPARATEUR_EXPORT);
            sbLine.append(stagiaire.getFonction());
            sbLine.append(Constant.SEPARATEUR_EXPORT);


            // reponses conplementaires
            List<Reponse> reponses = reponseDao.findReponseByInscription(i);
            if (!reponses.isEmpty()) {
                for (Reponse r : reponses) {
                    String specification = "";
                    if (r.getSpecification() != null) {
                        specification = " " + r.getSpecification();
                    }
                    sbLine.append(r.getChoix().concat(specification));
                    sbLine.append(Constant.SEPARATEUR_EXPORT);
                }
            }
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                    "Creation de la ligne du fichier d'export : " + sbLine.toString() + " OK");
            sbLine.append("\n");
            sb.append(sbLine);
        }
        return sb;

    }

    public List<Stagiaire> listStagiaires() {
        return stagiaireDao.findStagiaireEntities();
    }

    public boolean saveStagiaire(Stagiaire s) {
        Connection connection = null;

        // test du parametre d'entrée
        if (s == null) {
            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return false;
        }

        try {
            connection = getConnection();
            if (s.getIdstagiaire() == null) {
                // identifiant de la session
                s.setIdstagiaire(Integer.parseInt(generateId()));

                stagiaireDao.create(s);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Creation du stagiaire" + s.toString() + " OK");
            } else {
                stagiaireDao.edit(s);
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.INFO,
                        "Mise à jour du stagiaire" + s.toString() + " OK");
            }

            connection.commit();
            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifManageServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public List<Sessions> listAllSessions() {
        List<Sessions> listSessions = sessionsDao.findAllSessionsEntities();
        return listSessions;
    }
}
