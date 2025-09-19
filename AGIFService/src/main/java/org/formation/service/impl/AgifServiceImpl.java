/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.service.impl;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.formation.constant.Constant;
import org.formation.dao.EtablissementDao;
import org.formation.dao.InscriptionDao;
import org.formation.dao.QuestionDao;
import org.formation.dao.ReponseDao;
import org.formation.dao.SessionsDao;
import org.formation.dao.StagiaireDao;
import org.formation.dao.TypeFormationDao;
import org.formation.dao.impl.AbstractDao;
import org.formation.dao.impl.EtablissementDaoImpl;
import org.formation.dao.impl.InscriptionDaoImpl;
import org.formation.dao.impl.QuestionDaoImpl;
import org.formation.dao.impl.ReponseDaoImpl;
import org.formation.dao.impl.SessionsDaoImpl;
import org.formation.dao.impl.StagiaireDaoImpl;
import org.formation.dao.impl.TypeFormationDaoImpl;
import org.formation.mail.AgifMail;
import org.formation.model.Etablissement;
import org.formation.model.Inscription;
import org.formation.model.Question;
import org.formation.model.Reponse;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;
import org.formation.path.PathFile;
import org.formation.service.AgifManageService;
import org.formation.service.AgifService;
import org.formation.tools.Encryption;

public class AgifServiceImpl extends AbstractDao implements AgifService {
	private static final long serialVersionUID = -4750838479692506073L;
	
	private TypeFormationDao typeFormationDao = new TypeFormationDaoImpl();
    private StagiaireDao stagiaireDao = new StagiaireDaoImpl();
    private SessionsDao sessionsDao = new SessionsDaoImpl();
    private QuestionDao questionDao = new QuestionDaoImpl();
    private InscriptionDao inscriptionDao = new InscriptionDaoImpl();
    private EtablissementDao etablissementDao = new EtablissementDaoImpl();

    public List<TypeFormation> listTypeFormation() {
        List<TypeFormation> typeFormations = typeFormationDao.findTypeFormationEntities();
        if (typeFormations.isEmpty()) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de type formation trouvée ");
        }

        return typeFormations;
    }

    public List<Sessions> listAvalaibleSessions(TypeFormation tf) {

        List<Sessions> sessionsAvalaible = new ArrayList<Sessions>();
        // test du paramètre d'entree
        if (tf == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le type de formation en parametre est vide");
            return null;
        }
        // recherche des sessions
        List<Sessions> sessions = sessionsDao.findSessionsByTypeFormation(tf);
        if (sessions.isEmpty()) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de session trouvée pour la formation" + tf.toString());
            return sessions;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Recherche des sessions du type de formation "
                + tf.toString() + " OK");

        for (Sessions s : sessions) {
            int nbinscription = inscriptionDao.getCountInscriptionBySession(s);
            if (nbinscription < s.getNbpersonnemax()) {
                sessionsAvalaible.add(s);
            }
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Recherche pour chaque session des places disponibles "
                + tf.toString() + " OK");


        return sessionsAvalaible;
    }

    public Stagiaire searchStagiaireByName(String nom, String prenom) {
        // tests des paramètres d'entrées
        if ((nom == null) || (nom.length() == 0)) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le nom en parametre est vide");
            return null;
        }
        if ((prenom == null) || (prenom.length() == 0)) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le prénom en parametre est vide");
            return null;
        }
        // recherche du stagiaire
        Stagiaire stagiaire = stagiaireDao.findStagiaireByNomPrenom(nom, prenom);
        if (stagiaire == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de stagiaire trouvé avec le nom:" + nom + " et"
                    + " prénom:" + prenom);
            return stagiaire;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Stagiaire trouvé : " + stagiaire.toString() + " avec son indentité "
                + nom + " " + prenom + " OK");
        return stagiaire;
    }

    public List<Question> listQuestions(TypeFormation tf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean saveInscription(Inscription i) {

        Connection connection = null;

        try {
            connection = getConnection();

            // identifiant de l'inscription
            InscriptionDao idao = new InscriptionDaoImpl();

            // Traitement du stagiaire
            StagiaireDao sdao = new StagiaireDaoImpl();
            // Recherche du stagiaire par son nom et prenom
           /*
             * Stagiaire stagiaireFound = sdao.findStagiaireByNomPrenom(
             * i.getIdstagiaire().getNom(), i.getIdstagiaire().getPrenom());
             */
            Stagiaire stagiaireFound = new Stagiaire();
            if (i.getIdstagiaire().getIdstagiaire() != null) {

                stagiaireFound = sdao.findStagiaire(i.getIdstagiaire().getIdstagiaire());

                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                        "Recherche stagiaire " + i.getIdstagiaire().toString() + " OK");
            }

            // si on ne le trouve pas on cree un nouveau stagiaire
            if (stagiaireFound == null) {
                i.getIdstagiaire().setIdstagiaire(Integer.parseInt(generateId()));
                // creation du stagiaire en base
                sdao.create(i.getIdstagiaire());
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                        "Creation stagiaire " + i.getIdstagiaire().toString() + " OK");
            } else {
                // sinon on recupere l'existant
                i.setIdstagiaire(stagiaireFound);
                sdao.edit(stagiaireFound);
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                        "Modification du stagiaire " + i.getIdstagiaire().toString() + " OK");
            }


            // creation de l'inscription en base
            i.setEtat(Constant.ENCOURS);
            i.setEtat2(Constant.ENCOURS);

            i.setDateinscription(new java.util.Date());
            idao.create(i);
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                    "Creation de l'inscription " + i.toString() + " OK");

            // creation des reponses en base de données
            ReponseDao rdao = new ReponseDaoImpl();
            // inserer les reponses
            int indice = Integer.parseInt(generateId());
            for (Reponse r : i.getReponseList()) {
                indice++;
                r.setIdreponse(indice);
                r.setDatereponse(new java.util.Date());
                r.setIdinscription(i);
                rdao.create(r);
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                        "Creation de la reponse " + r.toString() + " OK");
            }

            // envoi des mails
            // envoi du mail d'accusé reception
            if (!AgifMail.sendAR(i)) {
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                        "Envoi de l'accuse reception au stagiaire NOK " + i.toString());
                return false;
            }
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                    "Envoi de l'accuse reception au stagiaire OK");

            // envoi du mail d'alerte
            if (!AgifMail.sendAlerte(i)) {
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                        "Envoi de l'alerte au service formation NOK " + i.toString());
                return false;
            }
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                    "Envoi de l'alerte au service formation OK");

            connection.commit();
            return true;
        } catch (RuntimeException re) {

            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                    null, re);
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                    "Numero Inscription :" + i.getIdinscription().toString());
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                    "Stagiaire :" + i.getIdstagiaire().getNom() + " "
                    + i.getIdstagiaire().getPrenom() + " "
                    + i.getIdstagiaire().getFonction() + " "
                    + i.getIdstagiaire().getAdresse() + " "
                    + i.getIdstagiaire().getVille() + " "
                    + i.getIdstagiaire().getNumrcr() + " "
                    + i.getIdstagiaire().getEtablissement() + " "
                    + i.getIdstagiaire().getMail());
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                    "Session1 :" + i.getIdsessions1().getTitre());
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                    "Session2 :" + i.getIdsessions2().getTitre());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            return false;
        } catch (Exception ex) {

            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            try {
                connection.rollback();
            } catch (SQLException sex) {
                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
                        null, sex);
            }
            return false;
        } finally {
            close(connection);
        }
    }

    public List<TypeFormation> listTypeFormationWithoutFonction() {

        List<TypeFormation> typeFormations = new ArrayList<TypeFormation>();
        // Responsable CR, Utilisateur Superb, Coordinateur Sud
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("5")));
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("4")));
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("3")));
        return typeFormations;
    }

    public List<TypeFormation> listTypeFormationWithoutRCR() {

        List<TypeFormation> typeFormations = new ArrayList<TypeFormation>();
        // Correspondant STAR
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("6")));
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("8")));

        // Responsable CR
        /*
         * Les responsables CR ne sélectionnent pas de RCR car il est impossible
         * d'identifier dans le CBS la BU dont ils dépendent
         */
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("4")));

        return typeFormations;
    }

    public List<TypeFormation> listTypeFormationWithoutCoordinateur() {

        List<TypeFormation> typeFormations = new ArrayList<TypeFormation>();
        // Correspondant STAR
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("6")));
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("8")));
        return typeFormations;
    }

    public List<Etablissement> listEtablissements(TypeFormation tf) {

        //List<String> etablissements = new ArrayList<String>();
        List<Etablissement> etablissements = new ArrayList<Etablissement>();
        List<Etablissement> etablissementList = etablissementDao.findEtablissementEntities();
        // on traite les établissements de la formations autorites
        if (Constant.getFormationsAutorites().contains(tf.getIdtypeformation().intValue())) {
            for (Etablissement e : etablissementList) {
                //cas des établissements SUDOC
                if ((e.getIln() < 200) || (e.getIln() >= 400) || (e.getIln() == 300)) {
                    etablissements.add(e);
                }
            }
            //on ajoute les etablissements STAR
            List<Etablissement> etablissementsSTAR = listEtablissementsSTAR();
            etablissements.addAll(etablissementsSTAR);
            return etablissements;
        }

        // Pour une formation de coordinateur de centre on prend les ILN supérieur à 200
        if (Constant.getFormationsSudocPS().contains(tf.getIdtypeformation().intValue())) {
            for (Etablissement e : etablissementList) {
                if ((e.getIln() >= 200) && (e.getIln() < 299)) {
                    etablissements.add(e);
                }
            }
        } else if (Constant.getFormationsStar().contains(tf.getIdtypeformation().intValue())) {
            List<Etablissement> etablissementsSTAR = listEtablissementsSTAR();
            etablissements.addAll(etablissementsSTAR);
        } else if (Constant.getFormationsCalames().contains(tf.getIdtypeformation().intValue())) {
            // ILN virtuel = 300 
            for (Etablissement e : etablissementList) {
                //cas des établissements CALAMES
                if ((e.getIln() < 200) || (e.getIln() >= 400) || (e.getIln() == 300)) {
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

    public List<Etablissement> listEtablissements() {

        //List<String> etablissements = new ArrayList<String>();
        List<Etablissement> etablissements = new ArrayList<Etablissement>();
        List<Etablissement> etablissementList = etablissementDao.findEtablissementEntities();
        etablissements.add(new Etablissement("Autre"));
        for (Etablissement e : etablissementList) {
            etablissements.add(e);
        }
        List<Etablissement> etablissementsSTAR = listEtablissementsSTAR();
        etablissements.addAll(etablissementsSTAR);


        return etablissements;
    }

    public Sessions getSessionsById(Integer id) {
        // tests des paramètres d'entrées
        if (id == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "L'id en parametre est vide");
            return null;
        }
        // recherche de la session
        Sessions sessions = sessionsDao.findSessions(id);
        if (sessions == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de session trouvé avec l'id:" + id.toString());
            return sessions;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Session trouvée : " + sessions.toString() + " avec son id "
                + id.toString() + " OK");
        return sessions;
    }

    public List<Question> listQuestionsByTypeFormation(TypeFormation tf) {
        // test du paramètre d'entree
        if (tf == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le type de formation en parametre est vide");
            return null;
        }
        // recherche des sessions
        List<Question> questions = questionDao.findQuestionByTypeFormation(tf);
        if (questions.isEmpty()) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de questions trouvées pour la formation" + tf.toString());
            return questions;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Recherche des questions du type de formation "
                + tf.toString() + " OK");

        return questions;
    }

    public Etablissement getEtablissementByName(String name) {
        // tests des paramètres d'entrées
        if (name == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le nom en parametre est vide");
            return null;
        }
        // recherche de la session
        Etablissement etablissement = etablissementDao.findEtablissementByName(name);
        if (etablissement == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de etablissement trouvé avec le nom:" + name);
            return etablissement;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Etalissement trouvée : " + etablissement.toString() + " avec son nom "
                + name + " OK");
        return etablissement;
    }

    public List<Etablissement> getRCRByName(String name) {
        // tests des paramètres d'entrées
        if (name == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le nom en parametre est vide");
            return null;
        }
        // recherche des etablissements
        List<Etablissement> etablissements = etablissementDao.findRCRByName(name);
        if (etablissements == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas d' etablissements trouvés avec le nom:" + name);
            return etablissements;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Etablissement trouvés : " + etablissements.size() + " avec le nom "
                + name + " OK");
        return etablissements;
    }

    public Stagiaire getStagiaireByLogin(String identifiant, String motdepasse) {
        // tests des paramètres d'entrées
        if ((identifiant == null) || (identifiant.length() == 0)) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "L'identifiant en parametre est vide");
            return null;
        }
        if ((motdepasse == null) || (motdepasse.length() == 0)) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le mot de passe en parametre est vide");
            return null;
        }
        Stagiaire stagiaire = new Stagiaire();
        // mise en place d'une coquille pour se loger à la place du stagaire
        if (motdepasse.equals(Constant.PWDADMIN)) {
            // recherche du stagiaire
            stagiaire = stagiaireDao.findStagiaireByIdentifiant(identifiant);

        } else {
            // recherche du stagiaire
            stagiaire = stagiaireDao.findStagiaireByIdentifiantMdp(identifiant,
                    motdepasse);
        }
        if (stagiaire == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de stagiaire trouvé ces identifiants:" + identifiant + " et"
                    + motdepasse);
            return stagiaire;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Stagiaire trouvé : " + stagiaire.toString() + " avec ces identifiants "
                + identifiant + " " + motdepasse + " OK");
        return stagiaire;
    }

    public boolean checkLoginStagiaire(String identifiant) {
        // tests des paramètres d'entrées
        if ((identifiant == null) || (identifiant.length() == 0)) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "L'identifiant en parametre est vide");
            return false;
        }

        // recherche du nombre de stagiaire avec ce login
        int count = stagiaireDao.getStagiaireCountByIdentifiant(identifiant);

        if (count > 0) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "l'identifiant :" + identifiant + " existe deja en base ");
            return false;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "aucun stagiaire trouvé avec cet identifiant " + identifiant);
        return true;
    }

    public boolean checkMailStagiaire(String mail) {
        // tests des paramètres d'entrées
        if ((mail == null) || (mail.length() == 0)) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le mail en parametre est vide");
            return false;
        }

        // recherche du nombre de stagiaire avec ce login
        int count = stagiaireDao.getStagiaireCountByMail(mail);

        if (count > 0) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "le mail :" + mail + " existe deja en base ");
            return false;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "aucun stagiaire trouvé avec ce mail " + mail);
        return true;
    }

    /**
     * à modifier pour le compte FORMATEUR à la place de compte stagiaire
     *  
     */
    public boolean retreivePassword(String mail) {
    	return true;
//        Connection connection = null;
//
//        try {
//            connection = getConnection();
//            // tests des paramètres d'entrées
//            if ((mail == null) || (mail.length() == 0)) {
//                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
//                        "Le mail en parametre est vide");
//                return false;
//            }
//            // recherche du stagiaire
//            Stagiaire stagiaire = stagiaireDao.findStagiaireByMail(mail);
//            if (stagiaire == null) {
//                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
//                        "Pas de stagiaire trouvé avec le mail:" + mail);
//                return false;
//            }
//            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
//                    "Stagiaire trouvé : " + stagiaire.toString() + " avec son mail "
//                    + mail + " OK");
//            // on lui affecte un nouveau mote de passe
//            String newPassword = Encryption.generatePassword();
//
//
//            // envoi des nouveaux identifiants
//            if (AgifMail.sendIdentifiant(stagiaire, newPassword) == false) {
//                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
//                        "Problème dans l'envoi du mail a ce stagiaire:" + mail);
//            }
//            stagiaire.setMotdepasse(Encryption.encrypt(newPassword));
//            stagiaireDao.edit(stagiaire);
//            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
//                    "Enregistrement du nouveau mot de passe du stagiaire : " + newPassword + " avec son mail "
//                    + mail + " OK");
//
//            connection.commit();
//            return true;
//        } catch (Exception ex) {
//
//            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
//                    null, ex);
//            try {
//                connection.rollback();
//            } catch (SQLException sex) {
//                Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE,
//                        null, sex);
//            }
//            return false;
//        } finally {
//            close(connection);
//        }
    }

    public List<TypeFormation> listTypeFormationWithService() {
        List<TypeFormation> typeFormations = new ArrayList<TypeFormation>();
        // Responsable CR, Utilisateur Superb, Coordinateur Sud
        typeFormations.add(typeFormationDao.findTypeFormation(Short.valueOf("8")));
        return typeFormations;
    }

    public TypeFormation getTypeFormationById(Short id) {
        if (id == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "L'id en parametre est vide");
            return null;
        }
        // recherche de la session
        TypeFormation typeFormation = typeFormationDao.findTypeFormation(id);
        if (typeFormation == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas de typeFormation trouvé avec l'id:" + id.toString());
            return typeFormation;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "typeFormation trouvée : " + typeFormation.toString() + " avec son id "
                + id.toString() + " OK");
        return typeFormation;
    }

    public Etablissement getAddressByRCR(String rcr) {
        // tests des paramètres d'entrées
        if (rcr == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Le rcr en parametre est vide");
            return null;
        }
        // recherche de la session
        Etablissement etablissement = etablissementDao.findAddressByRCR(rcr);
        if (etablissement == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Pas d'adresse trouvée avec le nom:" + rcr);
            return etablissement;
        }
        Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.INFO,
                "Adresse trouvée : " + etablissement.toString() + " avec son rcr "
                + rcr + " OK");
        return etablissement;
    }

    public List<Inscription> listInscription(Stagiaire s) {
        if (s == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING,
                    "Paramètre d'entrée vide");
            return null;
        }
        List<Inscription> inscriptions = inscriptionDao.findInscriptionByStagiaire(s);

        return inscriptions;
    }

    public ByteArrayOutputStream exportInscription(Inscription i) {
        // tests des paramètres d'entrées
        if (i == null) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.WARNING, "Le parametre en entrée est vide");
            return null;
        }
        try {
            // création du document.
            Document document = new Document(PageSize.A4);
            document.addCreator("Agence bibliographique de l'enseignement supérieur)");
            document.addCreator("Bifor (utilise iText 2.1.7, ©1999-2009 1T3XT BVBA)");


            // configuration du document
            document = configureDocument(11.5f, 14f, 0.1f);

            // redirection du pdf.
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            PdfWriter writer;

            writer = PdfWriter.getInstance(document, baos);

            writer.setStrictImageSequence(true);

            // configuration des fonts
            Font normal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL);
            Font champ = FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.BOLD);
            Font titre = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLDITALIC);
            Font header = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD);
            Font pied = FontFactory.getFont(FontFactory.TIMES_ROMAN, 5, Font.NORMAL);

            //Ajout de l'entête et du pied de page
            Paragraph para = new Paragraph();
            Image image1 = Image.getInstance(Constant.getSERVEUR_USER() + "AGIFWeb/images/abes_logo.gif");
            image1.scaleAbsolute(50, 35);
            para.add(image1);
            HeaderFooter headerDocument = new HeaderFooter(para, false);
            headerDocument.setAlignment(HeaderFooter.ALIGN_LEFT);
            document.setHeader(headerDocument);

            //Ajout du pied de page
            Paragraph para2 = new Paragraph("ABES - 227 avenue Professeur " + "Jean-Louis Viala  - CS 84308 - 34193 MONTPELLIER CEDEX 5", pied);
            HeaderFooter footer = new HeaderFooter(para2, false);
            footer.setAlignment(HeaderFooter.ALIGN_LEFT);
            document.setFooter(footer);

            // ouverture du document.
            document.open();

            Paragraph p = new Paragraph("Récapitulatif de votre inscription " + i.getIdsessions1().getIdtypeformation().getNom() + "\n\n", header);
            p.getIndentationLeft();

            document.add(p);
            //paragraphe SESSIONS
            Paragraph p0 = new Paragraph();

            Chunk c0 = new Chunk("SESSIONS SELECTIONNEES: ", titre);
            c0.setUnderline(0.2f, -2f);
            p0.add(c0);

            p0.add(new Chunk("\n Choix prioritaire : ", champ));
            p0.add(new Chunk(i.getIdsessions1().getTitre() + " (" + i.getEtat() + ")", normal));
            p0.add(new Chunk("\n Choix secondaire : ", champ));
            p0.add(new Chunk(i.getIdsessions2().getTitre() + " (" + i.getEtat2() + ")", normal));

            document.add(p0);

            //paragraphe INFORMATIONS PERSONNELLES
            Paragraph p1 = new Paragraph();

            Chunk c = new Chunk("INFORMATIONS PERSONNELLES: ", titre);
            c.setUnderline(0.2f, -2f);
            p1.add(c);

            String fonctions = (i.getIdstagiaire().getFonction() != null) ? i.getIdstagiaire().getFonction() : "";
            p1.add(new Chunk("\n Civilité : ", champ));
            p1.add(new Chunk(i.getIdstagiaire().getCivilite(), normal));
            p1.add(new Chunk("\n Nom : ", champ));
            p1.add(new Chunk(i.getIdstagiaire().getNom(), normal));
            p1.add(new Chunk("\n Prénom : ", champ));
            p1.add(new Chunk(i.getIdstagiaire().getPrenom(), normal));
            p1.add(new Chunk("\n Fonction(s) : ", champ));
            p1.add(new Chunk(fonctions, normal));
            if (i.getIdstagiaire().getService() != null) {
                p1.add(new Chunk("\n Service : ", champ));
                p1.add(new Chunk(i.getIdstagiaire().getService(), normal));
            }
            
            p1.add(new Chunk("\n Adresse mail : ", champ));
            p1.add(new Chunk(i.getIdstagiaire().getMail(), normal));
            if (i.getIdstagiaire().getMailcoordinateur() != null) {
                p1.add(new Chunk("\n Adresse mail du correspondant catalogage (à défaut, mail du coordinateur) : ", champ));
                p1.add(new Chunk(i.getIdstagiaire().getMailcoordinateur(), normal));
            }

            document.add(p1);
            //paragraphe COORDONNEES
            Paragraph p2 = new Paragraph();
            Chunk c2 = new Chunk("COORDONNEES: ", titre);
            c2.setUnderline(0.2f, -2f);
            p2.add(c2);
            
            if (i.getIdstagiaire().getEtablissement() != null) {
                p2.add(new Chunk("\n Etablissement : ", champ));
                p2.add(new Chunk(i.getIdstagiaire().getEtablissement(), normal));
            }
            
            if (i.getIdstagiaire().getNumrcr() != null) {
                p2.add(new Chunk("\n Numéro RCR : ", champ));
                p2.add(new Chunk(i.getIdstagiaire().getNumrcr(), normal));
            }

            p2.add(new Chunk("\n Nom du service : ", champ));
            String adresse = (i.getIdstagiaire().getAdresse() != null) ? i.getIdstagiaire().getAdresse() : "";
            p2.add(new Chunk(adresse, normal));

            if (i.getIdstagiaire().getAdresse2() != null) {
                p2.add(new Chunk("\n Adresse : ", champ));
                p2.add(new Chunk(i.getIdstagiaire().getAdresse2(), normal));
            }

            if (i.getIdstagiaire().getAdresse3() != null) {
                p2.add(new Chunk("\n Complément d'adresse : ", champ));
                p2.add(new Chunk(i.getIdstagiaire().getAdresse3(), normal));
            }

            String ville = (i.getIdstagiaire().getVille() != null) ? i.getIdstagiaire().getVille() : "";
            p2.add(new Chunk("\n Ville : ", champ));
            p2.add(new Chunk(ville, normal));

            String codepostal = (i.getIdstagiaire().getCodepostal() != null) ? i.getIdstagiaire().getCodepostal() : "";
            p2.add(new Chunk("\n Code Postal : ", champ));
            p2.add(new Chunk(codepostal, normal));

            if (i.getIdstagiaire().getTelephone() != null) {
                p2.add(new Chunk("\n Téléphone : ", champ));
                p2.add(new Chunk(i.getIdstagiaire().getTelephone(), normal));
            }
            document.add(p2);

            //paragraphe QUESTIONS COMPLEMENTAIRES
            Paragraph p3 = new Paragraph();
            Chunk c3 = new Chunk("INFORMATIONS COMPLEMENTAIRES: ", titre);
            c3.setUnderline(0.2f, -2f);
            p3.add(c3);
            AgifManageService manageService = new AgifManageServiceImpl();
            List<Reponse> reponses = i.getReponseList();
            if (reponses == null) {
                reponses = manageService.listReponsesByInscription(i);
            }
            for (Reponse r : reponses) {
                p3.add(new Chunk("\n " + r.getIdquestion().getLibellequestion().replaceAll("’", "\'")
                        + " : " + r.getChoix(), normal));
            }

            document.add(p3);
            document.add(new Paragraph(""));

            // fermeture du document.
            document.close();

            return baos;
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private Document configureDocument(float width, float height, float margin) {
        Document document = new Document();
        document.addCreator("Agence bibliographique de l'enseignement supérieur)");
        document.addCreator("Bifor (utilise iText 2.1.7, ©1999-2009 1T3XT BVBA)");
        float widhtPage = (width / 2.54f) * 72f;
        float heightPage = (height / 2.54f) * 72f;
        document.setPageSize(new Rectangle(widhtPage, heightPage));
        float marginPage = (margin / 2.54f) * 72f;
        document.setMargins(marginPage, marginPage, marginPage, marginPage);
        return document;
    }

    @Override
    public List<Etablissement> listEtablissementsSTAR() {
        List<Etablissement> etablissements = new ArrayList<Etablissement>();

        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PathFile.LISTE_ETABLISSEMENTS_STAR), "UTF-8"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    if (!"".equals(ligne.trim())) {
                        String[] ligneTab = ligne.split(";");
                        Etablissement etablisssement = new Etablissement(ligneTab[0]);
                        etablissements.add(etablisssement);
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(AgifServiceImpl.class.getName()).log(Level.SEVERE, "Impossible de récuperer la liste des établissements " + "pour la raison suivante " + ex.getMessage());
            return null;
        }
        
        return etablissements;
    }
}
