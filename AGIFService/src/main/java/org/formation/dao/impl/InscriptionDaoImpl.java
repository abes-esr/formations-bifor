/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.InscriptionDao;
import org.formation.dao.SessionsDao;
import org.formation.dao.StagiaireDao;
import org.formation.model.Inscription;
import org.formation.model.Sessions;
import org.formation.model.Stagiaire;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public class InscriptionDaoImpl extends AbstractDao implements InscriptionDao {
	private static final long serialVersionUID = 1570573654059273647L;

	public void create(Inscription i) {

        String query = "INSERT INTO INSCRIPTION "
                + "(IDINSCRIPTION, IDSESSIONS1, IDSESSIONS2, IDSTAGIAIRE, "
                + "DATEINSCRIPTION, ETAT, " + " ETAT2) "
                + "VALUES (?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            
            st.setInt(1, i.getIdinscription());
            st.setInt(2, i.getIdsessions1().getIdsessions());
            st.setInt(3, i.getIdsessions2().getIdsessions());
            st.setInt(4, i.getIdstagiaire().getIdstagiaire());
            st.setTimestamp(5, new java.sql.Timestamp(i.getDateinscription().getTime()));
            st.setString(6, i.getEtat());
            st.setString(7, i.getEtat2());

            st.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(Inscription i) {

        String query = "UPDATE INSCRIPTION  SET "
                + "IDSESSIONS1 = ?,"
                + "IDSESSIONS2 = ?,"
                + "IDSTAGIAIRE = ?,"
                + "DATEINSCRIPTION = ?,"
                + "ETAT = ?,"
                + "ETAT2 = ?"
                + " WHERE IDINSCRIPTION = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setInt(1, i.getIdsessions1().getIdsessions());
            st.setInt(2, i.getIdsessions2().getIdsessions());
            st.setInt(3, i.getIdstagiaire().getIdstagiaire());
            st.setTimestamp(4, new java.sql.Timestamp(i.getDateinscription().getTime()));
            st.setString(5, i.getEtat());
            st.setString(6, i.getEtat2());
            st.setInt(7, i.getIdinscription());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void updateEtatPrioritaire(Integer id, String etat) {
        String query = "UPDATE INSCRIPTION  SET "
                + "ETAT = ? "
                + "WHERE IDINSCRIPTION = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setString(1, etat);
            st.setInt(2, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void updateEtatSecondaire(Integer id, String etat) {
        String query = "UPDATE INSCRIPTION  SET "
                + "ETAT2 = ? "
                + "WHERE IDINSCRIPTION = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setString(1, etat);
            st.setInt(2, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(Integer id) {
        String query = "DELETE INSCRIPTION WHERE IDINSCRIPTION= ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public Inscription findInscription(Integer id) {
        String query = "SELECT * FROM INSCRIPTION WHERE IDINSCRIPTION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Inscription inscription = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                inscription = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return inscription;
    }

    public List<Inscription> findInscriptionEntities(int maxResults,
            int firstResult) {
        return findInscriptionEntities(false, maxResults, firstResult);
    }

    public List<Inscription> findInscriptionEntities() {
        return findInscriptionEntities(true, -1, -1);
    }

    private List<Inscription> findInscriptionEntities(boolean all,
            int maxResults, int firstResult) {
        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM INSCRIPTION ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Inscription> inscriptions = new ArrayList<Inscription>();

        try {
            connection = getConnection();
            if (!all) {
                query = query + " WHERE  rownum_ > ? AND rownum_ <= ?";
                st = connection.prepareStatement(query);
                st.setInt(1, firstResult);
                st.setInt(2, firstResult + maxResults);
            } else {
                st = connection.prepareStatement(query);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Inscription inscription = populate(rs);
                inscriptions.add(inscription);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return inscriptions;
    }

    public List<Inscription> findInscriptionPrioritaireBySession(Sessions s) {
        String query = "SELECT * FROM INSCRIPTION WHERE IDSESSIONS1 = ? "
                + "ORDER BY DATEINSCRIPTION";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Inscription> inscriptions = new ArrayList<Inscription>();
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdsessions());
            rs = st.executeQuery();
            while (rs.next()) {
                Inscription inscription = populate(rs);
                inscriptions.add(inscription);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return inscriptions;
    }

    public List<Inscription> findInscriptionSecondaireBySession(Sessions s) {
        String query = "SELECT * FROM INSCRIPTION WHERE IDSESSIONS2 = ? "
                + " ORDER BY DATEINSCRIPTION ASC ";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Inscription> inscriptions = new ArrayList<Inscription>();
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdsessions());
            rs = st.executeQuery();
            while (rs.next()) {
                Inscription inscription = populate(rs);
                inscriptions.add(inscription);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return inscriptions;
    }

    public List<Inscription> findInscriptionBySession(Sessions s) {
        /* String query = "SELECT * FROM INSCRIPTION WHERE IDSESSIONS2 = ? OR "
                + " IDSESSIONS1 = ? "
                + " ORDER BY DATEINSCRIPTION ASC "; */

        /*Gestion de l'affichage des prioritaires en premier*/
        String query = "SELECT * FROM INSCRIPTION I WHERE IDSESSIONS1 = ? "
                + " UNION "
                + " SELECT * FROM INSCRIPTION WHERE IDSESSIONS2 = ? "
                + " ORDER BY 5 ASC ";
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Inscription> inscriptions = new ArrayList<Inscription>();
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdsessions());
            st.setInt(2, s.getIdsessions());
            rs = st.executeQuery();
            while (rs.next()) {
                Inscription inscription = populateWithoutSessions(rs);
                // Get the values
                /*inscription.setIdinscription(rs.getInt(1));
                // foreign key
                int idSessionsPrioritaire = rs.getInt(2);
                Sessions sessionPrioritaire = new Sessions(idSessionsPrioritaire);
                inscription.setIdsessions1(sessionPrioritaire);

                int idSessionsSecondaire = rs.getInt(3);
                Sessions sessionSecondaire = new Sessions(idSessionsSecondaire);
                inscription.setIdsessions2(sessionSecondaire);

                int idStagiaire = rs.getInt(4);
                StagiaireDao stagiaireDao = new StagiaireDaoImpl();
                Stagiaire stagiaire = stagiaireDao.findStagiaire(idStagiaire);

                inscription.setIdstagiaire(stagiaire);

                inscription.setDateinscription(rs.getDate(5));
                inscription.setEtat(rs.getString(6));
                inscription.setEtat2(rs.getString(7));*/
                
                inscriptions.add(inscription);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return inscriptions;
    }

    public List<Inscription> findInscriptionByStagiaire(Stagiaire s) {
        /*Gestion de l'affichage des prioritaires en premier*/
        String query = "SELECT DATEINSCRIPTION,IDINSCRIPTION,I.ETAT AS ETAT,I.ETAT2,"
                + "I.IDSESSIONS1,S1.TITRE AS TITRE1,I.IDSESSIONS2,S2.TITRE AS TITRE2, "
                + "TF.IDTYPEFORMATION AS IDTYPEFORMATION, TF.NOM AS NOM "
                + " FROM INSCRIPTION I "
                + " JOIN SESSIONS S1 ON I.IDSESSIONS1 = S1.IDSESSIONS"
                + " JOIN SESSIONS S2 ON I.IDSESSIONS2 = S2.IDSESSIONS"
                + " JOIN TYPEFORMATION TF ON S1.IDTYPEFORMATION = TF.IDTYPEFORMATION"
                + " WHERE IDSTAGIAIRE = ? ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Inscription> inscriptions = new ArrayList<Inscription>();
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdstagiaire());
            rs = st.executeQuery();

            while (rs.next()) {
                Inscription inscription = new Inscription();
                inscription.setIdinscription(rs.getInt("IDINSCRIPTION"));
                inscription.setDateinscription(rs.getDate("DATEINSCRIPTION"));
                inscription.setEtat(rs.getString("ETAT"));
                inscription.setEtat2(rs.getString("ETAT2"));

                Integer idS1 = rs.getInt("IDSESSIONS1");
                if (idS1 != null) {
                    Sessions s1 = new Sessions(rs.getInt("IDSESSIONS1"));
                    s1.setTitre(rs.getString("TITRE1"));
                    TypeFormation tf = new TypeFormation(rs.getShort("IDTYPEFORMATION"));
                    tf.setNom(rs.getString("NOM"));
                    s1.setIdtypeformation(tf);

                    inscription.setIdsessions1(s1);
                }

                Integer idS2 = rs.getInt("IDSESSIONS2");
                if (idS2 != null) {
                    Sessions s2 = new Sessions(rs.getInt("IDSESSIONS2"));
                    s2.setTitre(rs.getString("TITRE2"));
                    inscription.setIdsessions2(s2);
                }

                inscriptions.add(inscription);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return inscriptions;
    }
    
    
    public int getInscriptionCount() {
        String query = "SELECT COUNT(*) FROM INSCRIPTION";
        int count = 0;
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return count;
    }

     private Inscription populateWithoutSessions(ResultSet rs) throws SQLException {
        Inscription i = new Inscription();
        // Get the values
        i.setIdinscription(rs.getInt(1));
        // foreign key
        int idSessionsPrioritaire = rs.getInt(2);
        Sessions sessionPrioritaire = new Sessions(idSessionsPrioritaire);        
        i.setIdsessions1(sessionPrioritaire);

        int idSessionsSecondaire = rs.getInt(3);
        Sessions sessionSecondaire = new Sessions(idSessionsSecondaire);
        i.setIdsessions2(sessionSecondaire);

        int idStagiaire = rs.getInt(4);
        StagiaireDao stagiaireDao = new StagiaireDaoImpl();
        Stagiaire stagiaire = stagiaireDao.findStagiaire(idStagiaire);

        i.setIdstagiaire(stagiaire);

        i.setDateinscription(rs.getDate(5));
        i.setEtat(rs.getString(6));
        i.setEtat2(rs.getString(7));

        return i;
    }
     
    private Inscription populate(ResultSet rs) throws SQLException {
        Inscription i = new Inscription();
        // Get the values
        i.setIdinscription(rs.getInt(1));
        // foreign key
        int idSessionsPrioritaire = rs.getInt(2);
        SessionsDao sessionsDao = new SessionsDaoImpl();
        Sessions sessionPrioritaire = sessionsDao.findSessions(idSessionsPrioritaire);
        i.setIdsessions1(sessionPrioritaire);

        int idSessionsSecondaire = rs.getInt(3);
        Sessions sessionSecondaire = sessionsDao.findSessions(idSessionsSecondaire);
        i.setIdsessions2(sessionSecondaire);


        int idStagiaire = rs.getInt(4);
        StagiaireDao stagiaireDao = new StagiaireDaoImpl();
        Stagiaire stagiaire = stagiaireDao.findStagiaire(idStagiaire);

        i.setIdstagiaire(stagiaire);

        i.setDateinscription(rs.getDate(5));
        i.setEtat(rs.getString(6));
        i.setEtat2(rs.getString(7));

        return i;
    }

    public int getCountInscriptionBySession(Sessions s) {
        String query = "SELECT COUNT(IDINSCRIPTION)"
                + " FROM INSCRIPTION I, SESSIONS S"
                + " WHERE ((I.IDSESSIONS1=S.IDSESSIONS"
                + " AND I.ETAT='VALIDEE') OR (I.IDSESSIONS2=S.IDSESSIONS"
                + " AND I.ETAT2='VALIDEE')) AND S.IDSESSIONS=? ";
        int count = 0;
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdsessions());
            rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return count;
    }
}
