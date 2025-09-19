/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.FormateurDao;
import org.formation.model.Formateur;

/**
 *
 * @author jean-laurent
 */
public class FormateurDaoImpl extends AbstractDao implements FormateurDao {
	private static final long serialVersionUID = -2527770922590848728L;

	public FormateurDaoImpl() {}


    public void create(Formateur f) {

        String query = "INSERT INTO FORMATEUR (IDFORMATEUR, NOM, PRENOM) VALUES(?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, f.getIdformateur());
            st.setString(2, f.getNom());
            st.setString(3, f.getPrenom());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(Formateur f) {

        String query = "UPDATE FORMATEUR F SET "
                + "NOM = ?,"
                + "PRENOM = ?"
                + "WHERE F.IDFORMATEUR = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, f.getNom());
            st.setString(2, f.getPrenom());
            st.setInt(3, f.getIdformateur());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(Integer id) {

        String query = "DELETE FORMATEUR WHERE IDFORMATEUR = ?";

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

    public Formateur findFormateur(Integer id) {

        String query = "SELECT IDFORMATEUR, NOM, PRENOM FROM FORMATEUR WHERE IDFORMATEUR = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Formateur formateur = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                formateur = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return formateur;
    }

    public List<Formateur> findFormateurEntities(int maxResults,
            int firstResult) {
        return findFormateurEntities(false, maxResults, firstResult);
    }

    public List<Formateur> findFormateurEntities() {
        return findFormateurEntities(true, -1, -1);
    }

    private List<Formateur> findFormateurEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM FORMATEUR ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Formateur> formateurs = new ArrayList<Formateur>();

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
                Formateur formateur = populate(rs);
                formateurs.add(formateur);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return formateurs;
    }

    public int getFormateurCount() {

        String query = "SELECT COUNT(*) FROM FORMATEUR";
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

    private Formateur populate(ResultSet rs) throws SQLException {
        Formateur formateur = new Formateur();
        formateur.setIdformateur(rs.getInt(1));
        formateur.setNom(rs.getString(2));
        formateur.setPrenom(rs.getString(3));
        return formateur;
    }
    
}
