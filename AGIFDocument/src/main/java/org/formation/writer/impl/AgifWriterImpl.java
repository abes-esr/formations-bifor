package org.formation.writer.impl;

import com.lowagie.text.rtf.document.RtfDocument;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.rtf.RTFTemplate;
import net.sourceforge.rtf.helper.RTFTemplateBuilder;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.formation.dao.EtablissementDao;
import org.formation.dao.impl.EtablissementDaoImpl;
import org.formation.model.Etablissement;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.path.PathFile;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.writer.AgifWriter;

/**
 *
 * @author jean-laurent
 */
public class AgifWriterImpl implements AgifWriter {

	public void initVelocity() {
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute");
    	Velocity.setProperty("runtime.log.logsystem.log4j.logger","com.mindtree.igg.website.email.TemplateMergeUtilVelocityImpl");
	}
	
    /**
     *
     * @author jean-laurent
     * Traiement du probleme d'encodage de RTFTemplate
     */
    public String escape(String sentence) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new RtfDocument().filterSpecialChar(baos, sentence, true, true);
        } catch (IOException e) {
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.SEVERE, "will never happen for ByteArrayOutputStream");
        }
        return new String(baos.toByteArray());
    }

    public Stagiaire escape(Stagiaire stagiaire) {
        Stagiaire s = stagiaire;
        
        s.setNom(escape(stagiaire.getNom()));
        s.setPrenom(escape(stagiaire.getPrenom()));
        s.setEtablissement(escape(stagiaire.getEtablissement()));
        s.setFonction(escape(stagiaire.getFonction()));        
        s.setAdresse(escape(stagiaire.getAdresse()));
        
        // Traitement de l'adresse
        if ( stagiaire.getAdresse2()!=null ) {
             s.setAdresse2(escape(stagiaire.getAdresse2()));
        }
        if ( stagiaire.getAdresse3()!=null ) {
            s.setAdresse3(escape(stagiaire.getAdresse3()));
        }
        

        // recuperation du nom du RCR au lieu du numéro et de l'adresse
        if (stagiaire.getNumrcr() != null) {
        	EtablissementDao edao = new EtablissementDaoImpl();
            Etablissement e = new Etablissement();
        	e=edao.findAddressByRCR(stagiaire.getNumrcr().trim());
            if (e.getShort_name()!=null) {
                s.setNumrcr(escape(e.getShort_name()));
                s.setEtablissement(escape(e.getAddress()));
            }else {
                e=edao.findRCRByLibrary(stagiaire.getNumrcr().trim());
                s.setNumrcr(e.getShort_name());
            }
        }
        
        s.setService(escape(stagiaire.getService()));
        s.setVille(escape(stagiaire.getVille()));
        s.setCodepostal(escape(stagiaire.getCodepostal()));
        return s;
    }

    public boolean EditListeStagiaires(Sessions s) {
        try {
        	this.initVelocity();
        	
            String rtfSource = PathFile.MODELE_LISTE_STAGIAIRES;
            String rtfTarget = PathFile.LISTE_STAGIAIRES + s.getTitre() + ".rtf";

            // 1. Get default RTFtemplateBuilder
            RTFTemplateBuilder builder = RTFTemplateBuilder.newRTFTemplateBuilder();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get default RTFtemplateBuilder OK");

            // 2. Get RTFtemplate with default Implementation of template engine (Velocity)
            RTFTemplate rtfTemplate = builder.newRTFTemplate();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get RTFtemplate with default Implementation  OK");

            // 3. Set the RTF model source
            rtfTemplate.setTemplate(new File(rtfSource));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Set the RTF model source OK");

            // 4. Put the context
            AgifManageService service = new AgifManageServiceImpl();
            List<Inscription> inscriptions = service.listValidInscriptions(s);
            if (inscriptions.isEmpty()) {
                Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.WARNING, "Aucune inscription de stagiaire dans la session");
                return false;
            }
            ArrayList<Stagiaire> stagiaires = new ArrayList<Stagiaire>();
            
            // recuperation de la liste des stagiaires
            for (Inscription i : inscriptions) {
                stagiaires.add(escape(i.getIdstagiaire()));
            }
            this.orderByName(stagiaires);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Recuperation de la liste des stagiaires OK");

            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

            rtfTemplate.put("stagiaires", stagiaires);
            rtfTemplate.put("date_jour", simpleFormat.format(new java.util.Date()));
            rtfTemplate.put("ref_session", s.getIdsessions().toString());
            rtfTemplate.put("formateur", escape(s.getIdformateur().getPrenom() + " " + s.getIdformateur().getNom().toUpperCase()));
            rtfTemplate.put("nom_formation", escape(s.getIdtypeformation().getNom()));
            rtfTemplate.put("titre_session", s.getTitre());
            rtfTemplate.put("date_debut_session", simpleFormat.format(s.getDatedebut()));
            rtfTemplate.put("date_fin_session", simpleFormat.format(s.getDatefin()));
            rtfTemplate.put("nom_lieu", escape(s.getIdlieu().getAdresse()));
            rtfTemplate.put("ville_lieu", escape(s.getIdlieu().getVille()));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Put the context OK");

            // 5. Merge the RTF source model and the context
            rtfTemplate.merge(rtfTarget);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Merge the RTF source model and the context OK");

            return true;
        } catch (Exception ex) {
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean EditEmargement(Sessions s) {
        try {
        	this.initVelocity();
        	
            String rtfSource = PathFile.MODELE_EMARGEMENT;
            String rtfTarget = PathFile.EMARGEMENT + s.getTitre() + ".rtf";

            // 1. Get default RTFtemplateBuilder
            RTFTemplateBuilder builder = RTFTemplateBuilder.newRTFTemplateBuilder();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get default RTFtemplateBuilder OK");

            // 2. Get RTFtemplate with default Implementation of template engine (Velocity)
            RTFTemplate rtfTemplate = builder.newRTFTemplate();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get RTFtemplate with default Implementation  OK");

            // 3. Set the RTF model source
            rtfTemplate.setTemplate(new File(rtfSource));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Set the RTF model source OK");

            // 4. Put the context
            AgifManageService service = new AgifManageServiceImpl();
            List<Inscription> inscriptions = service.listValidInscriptions(s);
            if (inscriptions.isEmpty()) {
                Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.WARNING, "Aucune inscription de stagiaire dans la session");
                return false;
            }
            List<Stagiaire> stagiaires = new ArrayList<Stagiaire>();
            // recuperation de la liste des stagiaires
            for (Inscription i : inscriptions) {
                stagiaires.add(escape(i.getIdstagiaire()));
            }
            this.orderByName(stagiaires);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Recuperation de la liste des stagiaires OK");

            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

            rtfTemplate.put("stagiaires", stagiaires);
            rtfTemplate.put("date_jour", simpleFormat.format(new java.util.Date()));
            rtfTemplate.put("ref_session", s.getIdsessions().toString());
            rtfTemplate.put("formateur", escape(s.getIdformateur().getPrenom() + " " + s.getIdformateur().getNom().toUpperCase()));
            rtfTemplate.put("nom_formation", escape(s.getIdtypeformation().getNom()));
            rtfTemplate.put("titre_session", s.getTitre());
            rtfTemplate.put("date_debut_session", simpleFormat.format(s.getDatedebut()));
            rtfTemplate.put("date_fin_session", simpleFormat.format(s.getDatefin()));
            rtfTemplate.put("nom_lieu", escape(s.getIdlieu().getNom()));
            rtfTemplate.put("ville_lieu", escape(s.getIdlieu().getVille()));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Put the context OK");

            // 5. Merge the RTF source model and the context
            rtfTemplate.merge(rtfTarget);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Merge the RTF source model and the context OK");

            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean EditAttestation(Sessions s) {
        try {
        	this.initVelocity();
        	
            String rtfSource = PathFile.MODELE_ATTESTATION;
            String rtfTarget = PathFile.ATTESTATION + s.getTitre() + ".rtf";

            // 1. Get default RTFtemplateBuilder
            RTFTemplateBuilder builder = RTFTemplateBuilder.newRTFTemplateBuilder();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get default RTFtemplateBuilder OK");

            // 2. Get RTFtemplate with default Implementation of template engine (Velocity)
            RTFTemplate rtfTemplate = builder.newRTFTemplate();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get RTFtemplate with default Implementation  OK");

            // 3. Set the RTF model source
            rtfTemplate.setTemplate(new File(rtfSource));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Set the RTF model source OK");

            // 4. Put the context
            AgifManageService service = new AgifManageServiceImpl();
            List<Inscription> inscriptions = service.listValidInscriptions(s);
            if (inscriptions.isEmpty()) {
                Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.WARNING, "Aucune inscription de stagiaire dans la session");
                return false;
            }
            List<Stagiaire> stagiaires = new ArrayList<Stagiaire>();
            // recuperation de la liste des stagiaires
            for (Inscription i : inscriptions) {
                stagiaires.add(escape(i.getIdstagiaire()));
            }
            this.orderByName(stagiaires);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Recuperation de la liste des stagiaires OK");

            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

            rtfTemplate.put("stagiaires", stagiaires);
            rtfTemplate.put("date_jour", simpleFormat.format(new java.util.Date()));
            rtfTemplate.put("ref_session", s.getIdsessions().toString());
            rtfTemplate.put("nom_formation", escape(s.getIdtypeformation().getNom()));
            rtfTemplate.put("titre_session", s.getTitre());
            rtfTemplate.put("date_debut_session", simpleFormat.format(s.getDatedebut()));
            rtfTemplate.put("date_fin_session", simpleFormat.format(s.getDatefin()));
            rtfTemplate.put("nom_lieu", escape(s.getIdlieu().getAdresse()));
            rtfTemplate.put("ville_lieu", escape(s.getIdlieu().getVille()));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO,
                    "Put the context OK");

            // 5. Merge the RTF source model and the context
            rtfTemplate.merge(rtfTarget);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO,
                    "Merge the RTF source model and the context OK");


            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            return false;
        }
    }
    
    public boolean EditChevalet(Sessions s) {
    	try {
        	this.initVelocity();
        	 
            String rtfSource = PathFile.MODELE_CHEVALET;
            String rtfTarget = PathFile.CHEVALETS + s.getTitre() + ".rtf";

            // 1. Get default RTFtemplateBuilder
            RTFTemplateBuilder builder = RTFTemplateBuilder.newRTFTemplateBuilder();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get default RTFtemplateBuilder OK");

            // 2. Get RTFtemplate with default Implementation of template engine (Velocity)
            RTFTemplate rtfTemplate = builder.newRTFTemplate();
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Get RTFtemplate with default Implementation  OK");

            // 3. Set the RTF model source
            rtfTemplate.setTemplate(new File(rtfSource));
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Set the RTF model source OK");

            // 4. Put the context
            AgifManageService service = new AgifManageServiceImpl();
            List<Inscription> inscriptions = service.listValidInscriptions(s);
            if (inscriptions.isEmpty()) {
                Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.WARNING, "Aucune inscription de stagiaire dans la session");
                return false;
            }
            List<Stagiaire> stagiaires = new ArrayList<Stagiaire>();
            // recuperation de la liste des stagiaires
            for (Inscription i : inscriptions) {
                stagiaires.add(escape(i.getIdstagiaire()));
            }
            this.orderByName(stagiaires);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Recuperation de la liste des stagiaires OK");

            rtfTemplate.put("stagiaires", stagiaires);
            rtfTemplate.put("titre_session", s.getTitre());
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Put the context OK");

            // 5. Merge the RTF source model and the context
            rtfTemplate.merge(rtfTarget);
            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.INFO, "Merge the RTF source model and the context OK");


            return true;
        } catch (Exception ex) {

            Logger.getLogger(AgifWriterImpl.class.getName()).log(Level.SEVERE,
                    null, ex);
            return false;
        }
    }
    
    public void orderByName(List<Stagiaire> stagiaires) {
        stagiaires.sort(
                (Stagiaire arg0, Stagiaire arg1) -> (arg0.getNom() + arg0.getPrenom()).compareToIgnoreCase(arg1.getNom() + arg1.getPrenom())
        );
    }
}
