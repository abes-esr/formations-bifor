/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.formation.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.formation.model.Sessions;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.formation.web.ListSessionsBean;
import org.formation.web.components.ManageSessions;

/**
 *
 * @author jean-laurent
 */
public class ListInscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 5965082578651090813L;

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
        List<String> messages = new  ArrayList<String>();
        AgifManageService service = new AgifManageServiceImpl();
        try {
            // recuperation de l'identifiant de session
            String idsession = request.getParameter("idsession");
            if (idsession.compareTo("") == 0) {
                messages.add("Pas d'identifiant de session dans la requete");            
            }
            // recuperation de la session correspondante
            Sessions s = service.getSessionsById(Integer.parseInt(idsession));
            if (s == null) {
                messages.add("Impossible de trouv&eacute; la session"
                        + " avec l'identifiant " + idsession);
            }
            // initialisation du contexte JSF
            FacesContext context = FacesContext.getCurrentInstance();
            if (context == null) {
                messages.add("Impossible de récuperer le contexte JSF"
                        + " avec l'identifiant " + idsession);
            }
            // création du backing bean par JSF
            ListSessionsBean listSessions = (ListSessionsBean)context.
                    getExternalContext().getSessionMap().get("listSessionsBean");

            // recuperation de la session courante
            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
             String intitule = s.getIdlieu().getNom() + " [" + s.getTitre() + ", "
                    + " du "
                    + simpleFormat.format(s.getDatedebut())
                    + " au "
                    + simpleFormat.format(s.getDatefin())
                    + "] ";
            int nbInscrits = 0;
            listSessions.setSession(new ManageSessions(intitule, nbInscrits, s));
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
        return "Short description";
    }// </editor-fold>


    
    

}
