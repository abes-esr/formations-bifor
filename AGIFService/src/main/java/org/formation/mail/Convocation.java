package org.formation.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.path.PathFile;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

public class Convocation {
	/**
     * Pour la génération d'une convocation, nous utilisons ici "Apache POI"
     * Pour ceci, il est nécessaire que dans le word, il n'y est pas de tableau, ...
     * 
     * La fonction retourne null si une erreur est survenu lors de la génération de la convocation
     * Sinon le path vers la convocation
     */
    public String EditConvocation(Sessions session, Stagiaire stagiaire) {
        try {
        	Logger.getLogger(Convocation.class.getName()).log(Level.INFO, "Generate convocation for stagiaire " + stagiaire.getIdstagiaire() + " for session" + session.getIdsessions());
        	
        	// Gestion des dates
            SimpleDateFormat simpleFullFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        	
            // Génération des paths
        	String docxSource = PathFile.MODELE_CONVOCATION;
            String docxTarget = PathFile.CONVOCATION +
            		session.getTitre() +
            		"_" +
            		stagiaire.getNom() +
            		"_" +
            		stagiaire.getPrenom() +
            		".pdf";
        	
            // Vérification que le stagiaire est bien dans la session
            AgifManageService service = new AgifManageServiceImpl();
            List<Inscription> inscriptions = service.listValidInscriptions(session);
            Boolean find = false;
            
            if (!inscriptions.isEmpty()) {
            	Iterator<Inscription> i = inscriptions.iterator();
            	
            	while (!find && i.hasNext()) {
            		Inscription ins = i.next();
            		find = (ins.getIdstagiaire().getIdstagiaire().intValue() == stagiaire.getIdstagiaire().intValue());
            	}
            }
            
            // Si le stagiaire ne fait pas partie de la session
            if(!find) {
            	Logger.getLogger(Convocation.class.getName()).log(Level.WARNING, "Stagiaire non trouvé dans la session");
            	return null;
            }

            // convocation 1/4 heures avant lde début de la formation
            GregorianCalendar calendar = new java.util.GregorianCalendar();
            calendar.setTime(session.getDatedebut());
            calendar.add(GregorianCalendar.SECOND, -900);
            Date heure_convocation = calendar.getTime();

            // Génération du PDF
            DocxToPDF doc = new DocxToPDF(docxSource);

			// Gestion du cas "Non spécifié" à ne pas reporter sur le document
			String civilite = stagiaire.getCivilite();
			String valeurARemplacer = "";
			if ("monsieur".equalsIgnoreCase(civilite) || "madame".equalsIgnoreCase(civilite)) {
				valeurARemplacer = civilite + " ";
			}

			doc.replaceText("stagiairecivilite", valeurARemplacer);        	doc.replaceText("stagiaireprenom", stagiaire.getPrenom());
        	doc.replaceText("stagiairenom", stagiaire.getNom());
        	
        	doc.replaceText("stagiaireadresse", stagiaire.getAdresse());
        	doc.replaceText("stagiaireetablissement", stagiaire.getEtablissement());
        	
        	doc.replaceText("datejour", simpleDateFormat.format(new java.util.Date()));
        	
        	doc.replaceText("formationnom", session.getIdtypeformation().getNom());
        	doc.replaceText("sessiontitre", session.getTitre());
        	doc.replaceText("sessiondebutdate", simpleDateFormat.format(session.getDatedebut()));
        	doc.replaceText("sessiondebutheure", simpleTimeFormat.format(session.getDatedebut()));
        	doc.replaceText("sessionfindate", simpleFullFormat.format(session.getDatefin()));
        	
        	doc.replaceText("sessionconvocation", simpleTimeFormat.format(heure_convocation));
        	
        	doc.replaceText("sessionlieunom", session.getIdlieu().getAdresse());
        	doc.replaceText("sessionlieucodepostal", session.getIdlieu().getCodepostal());
        	doc.replaceText("sessionlieuville", session.getIdlieu().getVille());
        	
        	if (session.getIdlieu().getAdresse2() != null && session.getIdlieu().getAdresse3() != null)
        		doc.replaceText("sessionlieuadresse", session.getIdlieu().getAdresse2() + ", " + session.getIdlieu().getAdresse3());
        	else if (session.getIdlieu().getAdresse2() != null)
        		doc.replaceText("sessionlieuadresse", session.getIdlieu().getAdresse2());
        	else
        		doc.replaceText("sessionlieuadresse", "");
        	
        	
            doc.savePDF(docxTarget);
            return docxTarget;
        } catch (Exception ex) {
            Logger.getLogger(Convocation.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
