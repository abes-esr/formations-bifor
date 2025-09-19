/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class ValidInscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = -610740388962707189L;

	/** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<String> resultat = getServletResult(request);

        try {
            // reponse HTML
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Validation d'une inscription d'un stagiaire</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Resultat de la validation de l'inscription : " + "</h1>");
            if (!resultat.isEmpty()) {
                out.println("<ul>");
                for (String msg : resultat) {
                    out.println("<li>");
                    out.println(msg);
                    out.println("</li>");
                }
                out.println("</ul>");
            } else {
                out.println("<b>");
                out.println("Erreur grave dans le traitement de la requete");
                out.println("</b>");
            }
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet de validation d'une inscription";
    }// </editor-fold>

    /**
     * Returns the resulte of the request process
     * @return a String containing servlet result
     */
    private List<String> getServletResult(HttpServletRequest request) {
        AgifManageService service = new AgifManageServiceImpl();
        List<String> messages = new ArrayList<String>();

        // recuperation d'identifiant d'inscription
        String idinscription = request.getParameter("idinscription");
        if (idinscription.compareTo("") == 0) {
            messages.add("Pas d'identifiant d'inscription dans la requete");
            return messages;
        }
        messages.add("recuperation d'identifiant d'inscription OK");

        // recuperation de l'inscription correspondante
        Inscription i = service.getInscriptionById(Integer.parseInt(idinscription));
        if (i == null) {
            messages.add("Impossible de trouv&eacute; l'inscription"
                    + " avec l'identifiant " + idinscription);
            return messages;
        }
        messages.add("recuperation de l'inscription OK");

        // recuperation de l'identifiant de session
        String idsessions = request.getParameter("idsessions");
        if (idsessions.compareTo("") == 0) {
            messages.add("Pas d'identifiant de session dans la requete");
            return messages;
        }
        messages.add("recuperation de l'identifiant de session OK");

        // recuperation de la session correspondante
        Sessions s = service.getSessionsById(Integer.parseInt(idsessions));
        if (s == null) {
            messages.add("Impossible de trouv&eacute; la session"
                    + " avec l'identifiant " + idsessions);
            return messages;
        }
        messages.add("recuperation de la session correspondante OK");

        // validation de l'inscription
        if (!service.validateInscription(i, s)) {
            messages.add("Erreur dans la validation "
                    + " de l'inscription    " + i.toString());
            return messages;
        }
        messages.add("validation de l'inscription " + i.toString()
                + " du stagiaire " + i.getIdstagiaire().getPrenom() + " "
                + i.getIdstagiaire().getNom() + "  OK");
        return messages;
    }
}
