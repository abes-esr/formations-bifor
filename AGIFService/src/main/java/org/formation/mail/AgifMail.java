/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.mail;

import java.io.File;
import java.text.SimpleDateFormat;
import javax.activation.FileDataSource;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.formation.constant.ConfigLoader;
import org.formation.constant.Constant;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;

import fr.opensagres.xdocreport.utils.StringEscapeUtils;

public class AgifMail {
    public static String EMAIL_SERVICE_FORMATION = null;
    public static String EMAIL_ADMIN_FORMATION = null;
    public static String EMAIL_GAPP = null;
    public static String EMAIL_RH = null;

    static {
        try {
            EMAIL_SERVICE_FORMATION = ConfigLoader.getProperty("mail.service.formation");
            EMAIL_ADMIN_FORMATION = ConfigLoader.getProperty("mail.admin.formation");
            EMAIL_GAPP = ConfigLoader.getProperty("mail.gapp");
            EMAIL_RH = ConfigLoader.getProperty("mail.rh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * You can do nothing with this object
     */
    public AgifMail() {
        // You can do nothing with this instantiate object
    }

    /**
     * Send the AR to the user
     *
     * @param Inscription i
     */
    public static boolean sendAR(Inscription i) {
        try {
            Message message = Constant.initMessage();

            // origine
            message.setFrom(new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // destinataire
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(i.getIdstagiaire().getMail()));

            // coordinateur en copie
            if (Constant.getEnv() == Constant.PROD && i.getIdstagiaire().getMailcoordinateur() != null) {
                for (String mail : i.getIdstagiaire().getArrayOfCoordinateur()) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail));
                }
            }

            // en copie            
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(AgifMail.EMAIL_ADMIN_FORMATION));

            // Sujet du message
            message.setSubject("Accusé réception d'une demande d'inscription de " + i.getIdstagiaire().getPrenom() + " " + i.getIdstagiaire().getNom().toUpperCase());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            // Corps du message
            message.setContent(
                    "<html>"
                            + "<body>"
                            + "Bonjour " + i.getIdstagiaire().getPrenom() + " " + i.getIdstagiaire().getNom().toUpperCase() + ",<br/><br/>"

                            + "Vous venez de d&eacute;poser une demande d'inscription &agrave; : "
                            + "<b>" + i.getIdsessions1().getIdtypeformation().getNom() + " (" + i.getIdsessions1().getTitre() + ", du "
                            + simpleDateFormat.format(i.getIdsessions1().getDatedebut()) + " au "
                            + simpleDateFormat.format(i.getIdsessions1().getDatefin()) + ")" + "</b><br/><br/> "

                            + "Vous recevez ce message automatique d'accusé de réception <b>qui, pour l'instant, vaut acceptation de votre demande.</b>"
                            + " Ce message peut servir à initier un ordre de mission pour l'organisation de votre déplacement (transport, nuitées).<br/>"
                            + "Si un <b>problème</b> devait être identifié dans la prise en compte de votre demande, <b>l'Abes vous en avertira rapidement.</b><br/>"
                            + "Dans tout autre cas, vous recevrez un <b>courriel de confirmation</b> de votre inscription (avec votre <b>convocation</b> en pièce jointe)"
                            + " <b>trois semaines avant le début de la session de formation.</b><br/>"
                            + "Pour toute question sur votre inscription, vous pouvez répondre à ce message en contactant l'adresse suformation[at]abes.fr.<br/>"
                            + "Bien cordialement,<br/>"
                            + "L'équipe formation de l'Abes<br/><br/>"

                            + "<b>ABES</b><br/>"
                            + "227 avenue Professeur-Jean-Louis-Viala<br/>"
                            + "34193 MONTPELLIER CEDEX 5<br/>"
                            + "T&eacute;l.  33 (0)4 67 54 84 10<br/>"
                            + "Fax  33 (0)4 67 54 84 14"
                            + "</body>"
                            + "</html>", "text/html");

            message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
            message.setSentDate(new Date());
            Transport.send(message);

            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Send the Alert to the formation service
     *
     * @param Inscription i
     */
    public static boolean sendAlerte(Inscription i) {
        try {
            Message message = Constant.initMessage();

            // origine
            message.setFrom(new InternetAddress(i.getIdstagiaire().getMail()));

            // destinataire
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(AgifMail.EMAIL_ADMIN_FORMATION));

            // sujet du message
            message.setSubject("Inscription à la " + i.getIdsessions1().getIdtypeformation().getNom() + " " + i.getIdsessions1().getTitre());
            // contenu du message
            message.setContent(
                    "<html>"
                            + "<body>"
                            + "Bonjour,<br/>"
                            + "Une demande d'inscription vient d'&ecirc;tre d&eacute;pos&eacute;e par : <b>"
                            + i.getIdstagiaire().getNom() + " " + i.getIdstagiaire().getPrenom() + "</b><br/> "
                            + "Cliquez <a href='" + Constant.getSERVLET_VALIDATION() + "?idinscription=" + i.getIdinscription() + "&idsessions=" + i.getIdsessions1().getIdsessions() + "'><b>ici</b> " + "</a>" + "pour la valider.<br/>"
                            + "</body>"
                            + "</html>", "text/html");
            message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
            message.setSentDate(new Date());

            Transport.send(message);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Send the Suppression mail to the formation service
     *
     * @param Sessions    s
     * @param Inscription i
     */
    public static boolean sendSuppression(Sessions s, Inscription i) {
        try {
            Message message = Constant.initMessage();

            // origine
            message.setFrom(new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // destinataire
            // enlever les commenraires  pour la production
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(i.getIdstagiaire().getMail()));

            // en copie
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(AgifMail.EMAIL_ADMIN_FORMATION));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // sujet du message
            message.setSubject(s.getIdtypeformation().getNom() + " annulation de la session " + s.getTitre());
            // contenu du message
            message.setContent(
                    "<html>"
                            + "<body>"
                            + "Bonjour,<br/>"
                            + "Nous vous informons de l'annulation de la session suivante : <br/>"
                            + " <b>" + " Nom :" + "</b>" + s.getIdtypeformation().getNom() + " <br/>"
                            + "<b> Session </b>: " + s.getTitre() + "<br/><br/>"

                            + " Merci de prendre contact le plus rapidement possible avec notre &eacute;quipe en &eacute;crivant &agrave; " + AgifMail.EMAIL_SERVICE_FORMATION + "<br/>"
                            + " Cordialement,<br/>"
                            + " L'&eacute;quipe Formation."
                            + "</body>"
                            + "</html>", "text/html");
            message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
            message.setSentDate(new Date());
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Send the Paiement mail to the GAPP service
     *
     * @param Sessions s
     */
    public static boolean sendPaiement(Sessions s) {
        try {
            Message message = Constant.initMessage();
            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

            // origine
            message.setFrom(new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // destinataire            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AgifMail.EMAIL_GAPP));

            // en copie
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(AgifMail.EMAIL_RH));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // sujet du message
            message.setSubject(buildMailSubject(s.getTitre()));

            // contenu du message
            message.setContent(
                    "<html>"
                            + "<body>"
                            + "Bonjour," + "<br/>"
                            + "La formation de " + s.getIdformateur().getPrenom() + " " + s.getIdformateur().getNom() + "(" + s.getIdlieu().getNom() + ") "
                            + "a bien eu lieu du  : " + simpleFormat.format(s.getDatedebut()) + "au " + simpleFormat.format(s.getDatefin()) + ", aux conditions pr&eacute;vues pour sa r&eacute;mun&eacute;ration.<br/> "
                            + "<b>Le paiement peut &ecirc;tre d&eacute;clench&eacute;.</b><br/>"
                            + "Les diff&eacute;rentes pi&egrave;ces du dossier ont &eacute;t&eacute; transmises au p&ocirc;le RH<br/>"
                            + "Merci<br/>"
                            + "Cordialement,<br/>"
                            + "L'&eacute;quipe Formation."
                            + "</body>"
                            + "</html>", "text/html");

            message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
            message.setSentDate(new Date());
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * rend spécifique l'objet du mail dans les cas SUDOC et CALAMES
     *
     * @param titre
     * @return
     */
    public static String buildMailSubject(String titre) {
        String subject = "FORMATEUR RELAIS : rémunération";
        if (titre.toLowerCase().contains("winibw"))
            subject = "FORMATEUR RELAIS SUDOC : rémunération";
        if (titre.toLowerCase().contains("calames"))
            subject = "FORMATEUR RELAIS CALAMES : rémunération";
        return subject;
    }

    /**
     * Send the annulation mail to the stgaiaire
     *
     * @param Sessions s
     */
    public static boolean sendAnnulation(Inscription i, Sessions s) {
        try {
            Message message = Constant.initMessage();

            // origine
            message.setFrom(new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // destinataire
            // enlever les commenraires  pour la production
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(i.getIdstagiaire().getMail()));

            // en copie
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // sujet du message
            message.setSubject(s.getIdtypeformation().getNom()
                    + " : annulation d'inscription");
            // contenu du message
            message.setContent(
                    "<html>"
                            + "<body>"
                            + "Bonjour," + "<br/>"
                            + "Nous vous informons de l'annulation de votre inscription &agrave; la session suivante :<br/>"
                            + "<b>Module : </b>" + s.getIdtypeformation().getNom() + "<br/>"
                            + "<b>Session : </b>" + s.getTitre() + "<br/><br/>"
                            + "Pour plus de renseignements, merci de contacter " + AgifMail.EMAIL_SERVICE_FORMATION + "<br/>"
                            + "Merci<br/>"
                            + "Cordialement,<br />"
                            + "L'&eacute;quipe Formation."
                            + "</body>"
                            + "</html>", "text/html");
            message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
            message.setSentDate(new Date());
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static String getMailConfirmation(Sessions s) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return ""
                + "<html>"
                + "<body>"
                + "Bonjour ###PRENOM_STAGIAIRE### ###NOM_STAGIAIRE###," + "<br/><br/>"

                + "Nous vous confirmons votre inscription &agrave; la session suivante : " + "<br/>"
                + "<b>" + " Module : " + "</b>" + s.getIdtypeformation().getNom() + "<br/> "
                + "<b> Session </b>: " + s.getTitre() + "<br/>"
                + "<b> Date de d&eacute;but </b>: " + simpleFormat.format(s.getDatedebut()) + "<br/>"
                + "<b> Date de fin </b>: " + simpleFormat.format(s.getDatefin()) + "<br/></br/>"

                + "Veuillez trouver en <b>pi&egrave;ce jointe votre convocation.</b><br/>"
                + "Pour organiser votre mission, vous trouverez sur la page suivante : "
                + "<a href=\"http://documentation.abes.fr/divers/formations/PreparerSaFormation/index.html\">"
                + "http://documentation.abes.fr/divers/formations/PreparerSaFormation/index.html</a> "
                + "les documents nécessaires : programme, horaires et dossier pratique pour se rendre sur le lieu du stage.<br/>"
                + "Pour préparer votre formation, veuillez consulter ce tutoriel d'introduction et ce QCM :<br/>"
                + "<a href=\"https://documentation.abes.fr/foad/IntroductionINIT/index.html\">https://documentation.abes.fr/foad/IntroductionINIT/index.html</a><br/>"
                + "<a href=\"https://documentation.abes.fr/foad/prerequis_catalogueurs/index.html\">https://documentation.abes.fr/foad/prerequis_catalogueurs/index.html</a><br/><br/>"

                + "Nous vous souhaitons une excellente " + s.getIdtypeformation().getNom() + "." + "<br/><br/>"

                + "Pour plus de renseignements, merci d'utiliser le guichet d’assistance de l’ABES.<br/>"
                + "Cordialement," + "<br/>"
                + "L'&eacute;quipe Formation."
                + "</body>"
                + "</html>";
    }

    /**
     * Send the confirmation mail to the stgaiaire
     *
     * @param Stagiaire s
     */
    public static boolean sendConfirmation(Sessions sessions, Stagiaire s, String mailConfirmation) {
        try {
            Message message = Constant.initMessage();
            Multipart multipart = new MimeMultipart();

            // origine
            message.setFrom(new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // destinataire
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(s.getMail()));

            if (Constant.getEnv() == Constant.PROD && s.getMailcoordinateur() != null) {
                for (String mail : s.getArrayOfCoordinateur()) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail));
                }
            }

            // en copie
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // sujet du message
            message.setSubject("Convocation a la " + sessions.getIdtypeformation().getNom() + " ("
                    + sessions.getTitre() + ")");

            // contenu du message
            // creation partie principale du message
            BodyPart messageBodyPart = new MimeBodyPart();
            String content = mailConfirmation.replace("###PRENOM_STAGIAIRE###", StringEscapeUtils.escapeHtml(s.getPrenom()));
            content = content.replace("###NOM_STAGIAIRE###", StringEscapeUtils.escapeHtml(s.getNom().toUpperCase()));
            messageBodyPart.setContent(content, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Création et ajout de la convocation
            Convocation document = new Convocation();
            String convocationPath = document.EditConvocation(sessions, s);
            Logger.getLogger(AgifMail.class.getName()).log(Level.INFO, "File path convocation: " + convocationPath);
            File file = new File(convocationPath);
            FileDataSource source = new FileDataSource(file);

            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file.getName());
            multipart.addBodyPart(messageBodyPart);

            // Header et conclusion
            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport.send(message);

            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Send the modification mail to the admin
     *
     * @param Sessions s
     */
    public static boolean sendModification(Inscription i, Sessions s) {

        try {
            Message message = Constant.initMessage();

            // origine
            message.setFrom(new InternetAddress(AgifMail.EMAIL_SERVICE_FORMATION));

            // destinataire
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AgifMail.EMAIL_ADMIN_FORMATION));

            // sujet du message
            message.setSubject("Modification d'affectation sur la session " + "d'inscription " + s.getIdtypeformation().getNom() + " " + s.getTitre());

            // contenu du message
            message.setContent("<html>"
                    + "<body>"
                    + "Bonjour," + "<br/>"
                    + "Une place s'est liber&eacute;e sur la session "
                    + s.getTitre() + ".<br/>"
                    + "Elle revient &agrave; <b>" + i.getIdstagiaire().getPrenom()
                    + " " + i.getIdstagiaire().getNom() + "</b>."
                    + "<br/> "
                    + "<b> Cliquez "
                    + "<a href='" + Constant.getSERVLET_ECHANGE() + "?idinscription="
                    + "" + i.getIdinscription() + "'>"
                    + "<b> ici </b>" + "</a>"
                    + "pour proc&eacute;der au changement d'affectation </b>."
                    + "<br/>"
                    + "Merci" + " <br/>"
                    + " Cordialement," + "<br/>"
                    + " L'&eacute;quipe Formation."
                    + "</body>"
                    + "</html>", "text/html");
            message.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
            message.setSentDate(new Date());
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifMail.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
