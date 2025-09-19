/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.formation.constant.Constant;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.web.components.ManageSessions;
import org.formation.writer.AgifWriter;
import org.formation.writer.impl.AgifWriterImpl;

/**
 *
 * @author jean-laurent
 */
public class ListInscriptionsBean {
    // Service metier

    private AgifManageService service = new AgifManageServiceImpl();
    private AgifWriter document = new AgifWriterImpl();
    // Injections
    private ListSessionsBean listSessions;
    // Sessions en edition
    private ManageSessions manageSessions;
    // champ d'affichage
    private List<Inscription> inscriptionItems = new ArrayList<Inscription>();
    private Integer nbPlacesDisponibles = 0;
    private String valueEtat;
    private String lienConvocation;
    private String lienEmargement;
    private String lienAttestation;
    private String lienListeStagiaires;
    private String lienChevalets;
    // Binding sur la data table + item selectionne
    private Inscription inscription;
    private UIData dataTable;
    private HtmlOutputText outputEtat;

    @PostConstruct
    public void init() {
        getServletRequest();
        initialisation();
    }

    private void getServletRequest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
        if (request != null) {
            String idsession = request.getParameter("idsessions");
            if (idsession != null) {
                Sessions s = service.getSessionsById(Integer.parseInt(idsession));
                FacesContext context = FacesContext.getCurrentInstance();
                listSessions = (ListSessionsBean) context.getExternalContext().
                        getSessionMap().get("listSessionsBean");
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
                String intitule = s.getIdlieu().getNom() + " [" + s.getTitre() + ", "
                        + " du "
                        + simpleFormat.format(s.getDatedebut())
                        + " au "
                        + simpleFormat.format(s.getDatefin())
                        + "] ";
                int nbInscrits = 0;
                listSessions.setSession(new ManageSessions(intitule, nbInscrits, s));
            }
        }
    }

    private void initialisation() {
        manageSessions = listSessions.getSession();
        inscriptionItems = service.listInscription((Sessions) manageSessions);
        
        // calcul du nombre d'inscrits
        int nbInscrits = 0;
        for (Inscription i : inscriptionItems) {
            if (Constant.VALIDE.equals(i.getEtat())
                    || Constant.VALIDE.equals(i.getEtat2())) {
                nbInscrits++;
            }
        }
        
        this.nbPlacesDisponibles = manageSessions.getNbpersonnemax() - nbInscrits;
    }

    public ListSessionsBean getListSessions() {
        return listSessions;
    }

    public void setListSessions(ListSessionsBean listSessions) {
        this.listSessions = listSessions;
    }

    public ManageSessions getManageSessions() {
        return manageSessions;
    }

    public void setManageSessions(ManageSessions manageSessions) {
        this.manageSessions = manageSessions;
    }

    public List<Inscription> getInscriptionItems() {
        return inscriptionItems;
    }

    public void setInscriptionItems(List<Inscription> inscriptionItems) {
        this.inscriptionItems = inscriptionItems;
    }

    public UIData getDataTable() {
        return dataTable;
    }

    public void setDataTable(UIData dataTable) {
        this.dataTable = dataTable;
    }

    public AgifManageService getService() {
        return service;
    }

    public void setService(AgifManageService service) {
        this.service = service;
    }

    public AgifWriter getDocument() {
        return document;
    }

    public void setDocument(AgifWriter document) {
        this.document = document;
    }

    public Integer getNbPlacesDisponibles() {
        return nbPlacesDisponibles;
    }

    public void setNbPlacesDisponibles(Integer nbPlacesDisponibles) {
        this.nbPlacesDisponibles = nbPlacesDisponibles;
    }

    public HtmlOutputText getOutputEtat() {
        return outputEtat;
    }

    public void setOutputEtat(HtmlOutputText outputEtat) {
        this.outputEtat = outputEtat;
    }

    public String getValueEtat() {
        return valueEtat;
    }

    public void setValueEtat(String valueEtat) {
        this.valueEtat = valueEtat;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public String getLienAttestation() {
        return lienAttestation;
    }

    public void setLienAttestation(String lienAttestation) {
        this.lienAttestation = lienAttestation;
    }

    public String getLienConvocation() {
        return lienConvocation;
    }

    public void setLienConvocation(String lienConvocation) {
        this.lienConvocation = lienConvocation;
    }

    public String getLienEmargement() {
        return lienEmargement;
    }

    public void setLienEmargement(String lienEmargement) {
        this.lienEmargement = lienEmargement;
    }

    public String getLienListeStagiaires() {
        return lienListeStagiaires;
    }

    public void setLienListeStagiaires(String lienListeStagiaires) {
        this.lienListeStagiaires = lienListeStagiaires;
    }

    public String getLienChevalets() {
        return lienChevalets;
    }

    public void setLienChevalets(String lienChevalets) {
        this.lienChevalets = lienChevalets;
    }

    /**
     * Modification de l'inscription
     */
    public String modify() {
        inscription = ((Inscription) dataTable.getRowData());
        return "edit";
    }

    /**
     * Validation de l'inscription
     */
    public String valid() {
        // recuperation de la session a updateSessionsr
        inscription = (Inscription) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "L'inscription " + inscription.toString() + " a été validée";
        
        if (service.validateInscription(inscription, manageSessions)) {
            // validation de l'inscription
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des inscriptions
            initialisation();
        }
        else {
            msg = "Erreur dans la validation de l'inscription " + inscription.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }

    /**
     * Refus de l'inscription
     */
    public String refuse() {
        // recuperation de la session a updateSessionsr
        inscription = ((Inscription) dataTable.getRowData());
        // declaration d'un message
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "L'inscription " + inscription.toString() + " a été refusée";

        if (service.refuseInscription(inscription, manageSessions)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des inscriptions
            initialisation();
        } else {
            msg = "Erreur dans le refus de l'inscription " + inscription.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        context.addMessage(null, message);

        return "success";
    }

    /**
     * Suppression de l'inscription
     */
    public String delete() {
        // recuperation de la session a updateSessionsr
        inscription = (Inscription) dataTable.getRowData();
        // declaration d'un message
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "L'inscription " + inscription.toString() + " a été supprimée";

        if (service.deleteInscription(inscription)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des inscriptions
            initialisation();
        }
        else {
            msg = "Erreur dans la suppression de l'inscription " + inscription.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        }
        
        context.addMessage(null, message);

        return "success";
    }

    /**
     * Edition de l'inscription
     */
    public String edit() {
        // recuperation de la session a updateSessionsr
        return "confirm";
    }

    /**
     * Parcours de l'inscription
     */
    public void browse() {
    }

    /**
     * Generation des documents pour la session
     */
    public String generate() {
        Sessions s = service.getSessionsById(manageSessions.getIdsessions());
        // declaration d'un message
        FacesMessage message;
        FacesContext context = FacesContext.getCurrentInstance();
        String returnCode = "success";
        String msg = "Les documents de la session " + s.toString() + " ont bien été genérés";

        // generation de la feuille d'émargement
        if (!document.EditEmargement(s)) {
            msg = "Erreur la generation de la feuille d'émargement pour la session " + s.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            returnCode = "error";
        }
        // generation de la liste des stagiaires convocations
        if (!document.EditListeStagiaires(s)) {
            msg = "Erreur la generation de la liste des stagiaires pour la session " + s.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            returnCode = "error";
        }


        // generation des attestations
        if (!document.EditAttestation(s)) {
            msg = "Erreur la generation des attestations pour la session " + s.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            returnCode = "error";
        }

        // generation des chevalets
        if (!document.EditChevalet(s)) {
            msg = "Erreur dans la generation des chevalets pour la session "
                    + s.toString();
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            context.addMessage(null, message);
            returnCode = "error";
        }

        if ("success".equals(returnCode)) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            // reinitialisation de la liste des inscriptions        
            context.addMessage(null, message);
        }
        
        initialisation();
        return returnCode;
    }

	/**
	 * Close de l'inscription
	 */
	public String close() {
		// declaration d'un message
		Sessions s = service.getSessionsById(manageSessions.getIdsessions());

		// On confirm la session en premier lieu
		if (s.getDateconfirmation() == null) {
			return "confirm";
		}

		FacesMessage message;
		FacesContext context = FacesContext.getCurrentInstance();
		String msg = "La session " + s.toString() + " a bien été cloturée";

		// cloture de la session
		if (service.closeSession(s)) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);

			// reinitialisation de la liste des inscriptions
			initialisation();
		} else {
			msg = "Erreur la cloture de la session " + s.toString();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		}
		context.addMessage(null, message);

		return "success";
	}

    /**
     * liste des sessions
     */
    public String back() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "back";
    }

	/**
	 * Export de l'inscription
	 */
	public void export() {

		// Recuperation de la session courante
		Sessions s = service.getSessionsById(manageSessions.getIdsessions());
		// Declaration d'un message
		FacesMessage message = new FacesMessage();

		FacesContext context = FacesContext.getCurrentInstance();
		String msg = "L'export de la session " + s.toString() + " a bien été généré";

		StringBuffer sb = service.exportInscription(s, inscriptionItems);
		byte[] csvData = ("\ufeff" + sb.toString()).getBytes();

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		response.setHeader("Content-disposition", "attachment; " + "filename=" + s.getIdsessions().toString() + ".csv");
		response.setContentLength(csvData.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		System.out.println("Encoding :" + response.getCharacterEncoding());

		try {
			response.getOutputStream().write(csvData);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
		} catch (IOException e) {
			msg = "Erreur la gérération de l'export de la session " + s.toString() + " pour la raison suivante :" + e.getMessage();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
			context.addMessage(null, message);
		}
	}
}
