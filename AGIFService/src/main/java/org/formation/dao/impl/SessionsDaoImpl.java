/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.constant.Constant;
import org.formation.dao.FormateurDao;
import org.formation.dao.LieuDao;
import org.formation.dao.SessionsDao;
import org.formation.dao.StatutDao;
import org.formation.dao.TypeFormationDao;
import org.formation.dao.TypeSessionDao;
import org.formation.model.Formateur;
import org.formation.model.Lieu;
import org.formation.model.Sessions;
import org.formation.model.Statut;
import org.formation.model.TypeFormation;
import org.formation.model.TypeSession;

/**
 *
 * @author jean-laurent
 */
public class SessionsDaoImpl extends AbstractDao implements SessionsDao {
	private static final long serialVersionUID = 1755707780416529391L;

	@Override
    public void create(Sessions s) {

        String query = "INSERT INTO SESSIONS "
                + "(IDSESSIONS, TITRE, NBPERSONNEMAX, DATEDEBUT, DATEFIN, "
                + "DATECLOTURE, DUREE,ETAT, IDFORMATEUR, IDTYPESESSION,"
                + " IDSTATUT, IDTYPEFORMATION, IDLIEU,DATECONFIRMATION,NBJOURS) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setInt(1, s.getIdsessions());
            st.setString(2, s.getTitre().trim());
            st.setInt(3, s.getNbpersonnemax());
            st.setTimestamp(4, new java.sql.Timestamp(s.getDatedebut().getTime()));
            st.setTimestamp(5, new java.sql.Timestamp(s.getDatefin().getTime()));
            st.setTimestamp(6, new java.sql.Timestamp(s.getDatecloture().getTime()));
            st.setInt(7, s.getDuree());
            st.setString(8, s.getEtat());
            st.setInt(9, s.getIdformateur().getIdformateur());
            st.setInt(10, s.getIdtypesession().getIdtypesession());
            st.setInt(11, s.getIdstatut().getIdstatut());
            st.setShort(12, s.getIdtypeformation().getIdtypeformation());
            st.setBigDecimal(13, s.getIdlieu().getIdlieu());
            if (s.getDateconfirmation() != null) {
                st.setTimestamp(14, new java.sql.Timestamp(s.getDateconfirmation().getTime()));
            } else {
                st.setTimestamp(14, null);
            }
            st.setDouble(15, s.getNbJours());
            st.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        	close(st);
            close(connection);
        }
    }

	@Override
    public void edit(Sessions s) {

        String query = "UPDATE SESSIONS  SET "
                + "TITRE = ?,"
                + "NBPERSONNEMAX = ?,"
                + "DATEDEBUT = ?,"
                + "DATEFIN = ?,"
                + "DATECLOTURE = ?,"
                + "DUREE = ?,"
                + "ETAT = ?,"
                + "IDFORMATEUR = ?,"
                + "IDLIEU = ?,"
                + "IDSTATUT = ?,"
                + "IDTYPEFORMATION = ?,"
                + "IDTYPESESSION = ?,"
                + "DATECONFIRMATION = ?, "
                + "NBJOURS = ? "
                + " WHERE IDSESSIONS = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setString(1, s.getTitre().trim());
            st.setInt(2, s.getNbpersonnemax());
            st.setTimestamp(3, new java.sql.Timestamp(s.getDatedebut().getTime()));
            st.setTimestamp(4, new java.sql.Timestamp(s.getDatefin().getTime()));
            st.setTimestamp(5, new java.sql.Timestamp(s.getDatecloture().getTime()));
            st.setInt(6, s.getDuree());
            st.setString(7, s.getEtat());
            st.setInt(8, s.getIdformateur().getIdformateur());
            st.setBigDecimal(9, s.getIdlieu().getIdlieu());
            st.setInt(10, s.getIdstatut().getIdstatut());
            st.setShort(11, s.getIdtypeformation().getIdtypeformation());
            st.setInt(12, s.getIdtypesession().getIdtypesession());
            if (s.getDateconfirmation() != null) {
                st.setTimestamp(13, new java.sql.Timestamp(s.getDateconfirmation().getTime()));
            } else {
                st.setTimestamp(13, null);
            }
            st.setDouble(14, s.getNbJours());
            st.setInt(15, s.getIdsessions());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

	@Override
    public void destroy(Integer id) {
        String query = "DELETE SESSIONS WHERE IDSESSIONS = ?";

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

	@Override
    public Sessions findSessions(Integer id) {
        //String query = "SELECT * FROM SESSIONS WHERE IDSESSIONS = ?";
         String query = "SELECT IDSESSIONS,TITRE,NBPERSONNEMAX,DATEDEBUT,DATEFIN"
                + ",DATECLOTURE,DUREE,ETAT,DATECONFIRMATION,NBJOURS,"
                + " F.IDFORMATEUR AS IDFORMATEUR,F.NOM AS NOMFORMATEUR, F.PRENOM AS PRENOMFORMATEUR,"
                + " TS.IDTYPESESSION AS IDTYPESESSION, TS.NOM AS NOMTYPESESSION, "
                + " ST.IDSTATUT AS IDSTATUT, ST.NOM AS NOMSTATUT,"
                + " TF.IDTYPEFORMATION AS IDTYPEFORMATION,TF.NOM AS NOMTYPEFORMATION, DESCRIPTION,"
                + " L.IDLIEU AS IDLIEU, L.NOM AS NOMLIEU, L.ADRESSE AS ADRESSELIEU,"
                + " L.ADRESSE2 AS ADRESSE2LIEU, L.ADRESSE3 AS ADRESSE3LIEU, "
                + " L.CODEPOSTAL AS CODEPOSTALLIEU, L.VILLE AS VILLELIEU, L.PAYS AS PAYSLIEU"
                + " FROM SESSIONS S, FORMATEUR F, TYPESESSION TS, STATUT ST, "
                + " TYPEFORMATION TF, LIEU L  "
                + " WHERE IDSESSIONS = ? AND S.IDFORMATEUR=F.IDFORMATEUR AND "
                + " S.IDTYPESESSION=TS.IDTYPESESSION AND S.IDSTATUT=ST.IDSTATUT "
                + " AND S.IDTYPEFORMATION=TF.IDTYPEFORMATION AND S.IDLIEU=L.IDLIEU "                
                + " ORDER BY DATECLOTURE ASC,DATEFIN ASC, DATEDEBUT ASC";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Sessions sessions = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                sessions = populateByName(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }

	@Override
    public List<Sessions> findSessionsEntities(int maxResults, int firstResult) {
        return findSessionsEntities(false, maxResults, firstResult);
    }

	@Override
    public List<Sessions> findSessionsEntities() {
       //return findSessionsEntities(true, -1, -1);
        String query = "SELECT IDSESSIONS,TITRE,NBPERSONNEMAX,DATEDEBUT,DATEFIN"
                + ",DATECLOTURE,DUREE,ETAT,DATECONFIRMATION,NBJOURS, "
                + " F.IDFORMATEUR AS IDFORMATEUR,F.NOM AS NOMFORMATEUR, F.PRENOM AS PRENOMFORMATEUR,"
                + " TS.IDTYPESESSION AS IDTYPESESSION, TS.NOM AS NOMTYPESESSION, "
                + " ST.IDSTATUT AS IDSTATUT, ST.NOM AS NOMSTATUT,"
                + " TF.IDTYPEFORMATION AS IDTYPEFORMATION,TF.NOM AS NOMTYPEFORMATION, DESCRIPTION,"
                + " L.IDLIEU AS IDLIEU, L.NOM AS NOMLIEU, L.ADRESSE AS ADRESSELIEU,"
                + " L.ADRESSE2 AS ADRESSE2LIEU, L.ADRESSE3 AS ADRESSE3LIEU, "
                + " L.CODEPOSTAL AS CODEPOSTALLIEU, L.VILLE AS VILLELIEU, L.PAYS AS PAYSLIEU"
                + " FROM SESSIONS S, FORMATEUR F, TYPESESSION TS, STATUT ST, "
                + " TYPEFORMATION TF, LIEU L  "
                + " WHERE S.IDFORMATEUR=F.IDFORMATEUR AND "
                + " S.IDTYPESESSION=TS.IDTYPESESSION AND S.IDSTATUT=ST.IDSTATUT "
                + " AND S.IDTYPEFORMATION=TF.IDTYPEFORMATION AND S.IDLIEU=L.IDLIEU "                
                + " AND S.IDSTATUT<>2 " 
                + " ORDER BY DATECLOTURE ASC,DATEFIN ASC, DATEDEBUT ASC";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Sessions> sessions = new ArrayList<Sessions>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Sessions session = populateByName(rs);
                sessions.add(session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }
    
	@Override
	public List<Sessions> findAllSessionsEntities() {
       //return findSessionsEntities(true, -1, -1);
        String query = "SELECT IDSESSIONS,TITRE,NBPERSONNEMAX,DATEDEBUT,DATEFIN"
                + ",DATECLOTURE,DUREE,ETAT,DATECONFIRMATION,NBJOURS, "
                + " F.IDFORMATEUR AS IDFORMATEUR,F.NOM AS NOMFORMATEUR, F.PRENOM AS PRENOMFORMATEUR,"
                + " TS.IDTYPESESSION AS IDTYPESESSION, TS.NOM AS NOMTYPESESSION, "
                + " ST.IDSTATUT AS IDSTATUT, ST.NOM AS NOMSTATUT,"
                + " TF.IDTYPEFORMATION AS IDTYPEFORMATION,TF.NOM AS NOMTYPEFORMATION, DESCRIPTION,"
                + " L.IDLIEU AS IDLIEU, L.NOM AS NOMLIEU, L.ADRESSE AS ADRESSELIEU,"
                + " L.ADRESSE2 AS ADRESSE2LIEU, L.ADRESSE3 AS ADRESSE3LIEU, "
                + " L.CODEPOSTAL AS CODEPOSTALLIEU, L.VILLE AS VILLELIEU, L.PAYS AS PAYSLIEU"
                + " FROM SESSIONS S, FORMATEUR F, TYPESESSION TS, STATUT ST, "
                + " TYPEFORMATION TF, LIEU L  "
                + " WHERE S.IDFORMATEUR=F.IDFORMATEUR AND "
                + " S.IDTYPESESSION=TS.IDTYPESESSION AND S.IDSTATUT=ST.IDSTATUT "
                + " AND S.IDTYPEFORMATION=TF.IDTYPEFORMATION AND S.IDLIEU=L.IDLIEU "                
                + " ORDER BY DATECLOTURE ASC,DATEFIN ASC, DATEDEBUT ASC";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Sessions> sessions = new ArrayList<Sessions>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Sessions session = populateByName(rs);
                sessions.add(session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }

    private List<Sessions> findSessionsEntities(boolean all, int maxResults, int firstResult) {
        String query = "SELECT * FROM SESSIONS "
                + " ORDER BY DATECLOTURE ASC,DATEFIN ASC, DATEDEBUT ASC ";
        
       
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Sessions> sessions = new ArrayList<Sessions>();

        try {
            connection = getConnection();
            if (!all) {
                query = "SELECT * FROM "
                        + " ( SELECT row_.*, rownum rownum_ FROM"
                        + " ( SELECT * FROM SESSIONS ) row_ ) ";
                query = query + " WHERE  rownum_ > ? AND rownum_ <= ?";
                st = connection.prepareStatement(query);
                st.setInt(1, firstResult);
                st.setInt(2, firstResult + maxResults);
            } else {
                st = connection.prepareStatement(query);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Sessions session = populate(rs);
                sessions.add(session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }

    @Override
    public List<Sessions> findSessionsByTypeFormation(TypeFormation tf) {
        String query = "SELECT * FROM SESSIONS WHERE IDTYPEFORMATION=? "
                + "AND IDSTATUT=" + Constant.OUVERTE + " AND DATEDEBUT>SYSDATE "
                + " AND DATECLOTURE >=SYSDATE " 
                + " ORDER BY DATEDEBUT ASC,DATEFIN ASC";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Sessions> sessions = new ArrayList<Sessions>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setShort(1, tf.getIdtypeformation());
            rs = st.executeQuery();
            while (rs.next()) {
                Sessions session = populate(rs);
                sessions.add(session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }

    @Override
    public int getSessionsCount() {
        String query = "SELECT COUNT(*) FROM SESSIONS";
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

    private Sessions populateByName(ResultSet rs) throws SQLException {
        Sessions s = new Sessions();
        // Get the values
        s.setIdsessions(rs.getInt("IDSESSIONS"));
        s.setTitre(rs.getString("TITRE"));
        s.setNbpersonnemax(rs.getInt("NBPERSONNEMAX"));
        s.setDatedebut(new java.util.Date(rs.getTimestamp("DATEDEBUT").getTime()));
        s.setDatefin(new java.util.Date(rs.getTimestamp("DATEFIN").getTime()));
        s.setDatecloture(new java.util.Date(rs.getTimestamp("DATECLOTURE").getTime()));
        s.setDuree(rs.getInt("DUREE"));
        s.setEtat(rs.getString("ETAT"));
        if (rs.getTimestamp("DATECONFIRMATION") != null) {
            s.setDateconfirmation(new java.util.Date(rs.getTimestamp("DATECONFIRMATION").getTime()));
        } else {
            s.setDateconfirmation(null);
        }
        s.setNbJours(rs.getDouble("NBJOURS"));        
        // foreign key      
        // Formateur
        Formateur formateur = new Formateur();
        formateur.setIdformateur(rs.getInt("IDFORMATEUR"));
        formateur.setNom(rs.getString("NOMFORMATEUR"));
        formateur.setPrenom(rs.getString("PRENOMFORMATEUR"));        
        s.setIdformateur(formateur);

        // type de session
        TypeSession typeSession = new TypeSession();
        typeSession.setIdtypesession(rs.getInt("IDTYPESESSION"));
        typeSession.setNom(rs.getString("NOMTYPESESSION"));
        s.setIdtypesession(typeSession);
        
        // Statut
        Statut statut = new Statut();
        statut.setIdstatut(rs.getInt("IDSTATUT"));
        statut.setNom(rs.getString("NOMSTATUT"));
        s.setIdstatut(statut);

        // Type de formation
        TypeFormation typeFormation = new TypeFormation();
        typeFormation.setIdtypeformation(rs.getShort("IDTYPEFORMATION"));
        typeFormation.setNom(rs.getString("NOMTYPEFORMATION"));
        typeFormation.setDescription(rs.getString("DESCRIPTION"));
        s.setIdtypeformation(typeFormation);

        // Lieu
        Lieu lieu = new Lieu();
        lieu.setIdlieu(rs.getBigDecimal("IDLIEU"));
        lieu.setNom(rs.getString("NOMLIEU"));
        lieu.setAdresse(rs.getString("ADRESSELIEU"));
        lieu.setAdresse2(rs.getString("ADRESSE2LIEU"));
        lieu.setAdresse3(rs.getString("ADRESSE3LIEU"));
        lieu.setCodepostal(rs.getString("CODEPOSTALLIEU"));
        lieu.setVille(rs.getString("VILLELIEU"));
        lieu.setPays(rs.getString("PAYSLIEU"));
        s.setIdlieu(lieu);
        


        return s;
    }
     
    private Sessions populate(ResultSet rs) throws SQLException {
        Sessions s = new Sessions();
        // Get the values
        s.setIdsessions(rs.getInt(1));
        s.setTitre(rs.getString(2));
        s.setNbpersonnemax(rs.getInt(3));
        s.setDatedebut(new java.util.Date(rs.getTimestamp(4).getTime()));
        s.setDatefin(new java.util.Date(rs.getTimestamp(5).getTime()));
        s.setDatecloture(new java.util.Date(rs.getTimestamp(6).getTime()));
        s.setDuree(rs.getInt(7));
        s.setEtat(rs.getString(8));

        // foreign key
        int idFormateur = rs.getInt(9);
        FormateurDao formateurDao = new FormateurDaoImpl();
        Formateur formateur = formateurDao.findFormateur(idFormateur);

        s.setIdformateur(formateur);

        int idTypeSession = rs.getInt(10);
        TypeSessionDao typeSessionDao = new TypeSessionDaoImpl();
        TypeSession typeSession = typeSessionDao.findTypeSession(idTypeSession);

        s.setIdtypesession(typeSession);

        int idStatut = rs.getInt(11);
        StatutDao statutDao = new StatutDaoImpl();
        Statut statut = statutDao.findStatut(idStatut);

        s.setIdstatut(statut);

        Short idTypeFormation = rs.getShort(12);
        TypeFormationDao typeFormationDao = new TypeFormationDaoImpl();
        TypeFormation typeFormation = typeFormationDao.findTypeFormation(idTypeFormation);

        s.setIdtypeformation(typeFormation);

        BigDecimal idLieu = rs.getBigDecimal(13);
        LieuDao lieuDao = new LieuDaoImpl();
        Lieu lieu = lieuDao.findLieu(idLieu);

        s.setIdlieu(lieu);
        if (rs.getTimestamp(14) != null) {
            s.setDateconfirmation(new java.util.Date(rs.getTimestamp(14).getTime()));
        } else {
            s.setDateconfirmation(null);
        }
        s.setNbJours(rs.getDouble(15));


        return s;
    }

    @Override
    public List<Sessions> findSessionsByStatut(Statut s) {
        String query = "SELECT * FROM SESSIONS WHERE IDSTATUT=?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Sessions> sessions = new ArrayList<Sessions>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdstatut());
            rs = st.executeQuery();
            while (rs.next()) {
                Sessions session = populate(rs);
                sessions.add(session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }
    
    @Override
    public List<Sessions> findSessionsByFormateur(Formateur f) {
        String query = "SELECT * FROM SESSIONS WHERE IDFORMATEUR=?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Sessions> sessions = new ArrayList<Sessions>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, f.getIdformateur());
            rs = st.executeQuery();
            while (rs.next()) {
                Sessions session = populate(rs);
                sessions.add(session);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return sessions;
    }

    @Override
    public void updateStatut(Integer id, Statut s) {
        String query = "UPDATE SESSIONS  SET "
                + "IDSTATUT = ? "
                + "WHERE IDSESSIONS = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setInt(1, s.getIdstatut());
            st.setInt(2, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }
}
