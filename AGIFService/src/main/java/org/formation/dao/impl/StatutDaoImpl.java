/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.StatutDao;
import org.formation.model.Statut;

/**
 *
 * @author jean-laurent
 */
public class StatutDaoImpl extends AbstractDao implements StatutDao {
	private static final long serialVersionUID = -6318610345498050331L;

	public StatutDaoImpl() {}

    public void create(Statut s) {

        String query = "INSERT INTO STATUT VALUES(?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, s.getIdstatut());
            st.setString(2, s.getNom());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(Statut s) {

        String query = "UPDATE STATUT S SET "
                + "NOM = ? "
                + "WHERE S.IDSTATUT = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, s.getNom());
            st.setInt(2, s.getIdstatut());
            
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(Integer id) {

        String query = "DELETE STATUT WHERE IDSTATUT = ?";

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

    public Statut findStatut(Integer id) {

        String query = "SELECT * FROM STATUT WHERE IDSTATUT = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Statut statut = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                statut = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return statut;
    }

    public List<Statut> findStatutEntities(int maxResults,
            int firstResult) {
        return findStatutEntities(false, maxResults, firstResult);
    }

    public List<Statut> findStatutEntities() {
        return findStatutEntities(true, -1, -1);
    }

    private List<Statut> findStatutEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM STATUT ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Statut> statuts = new ArrayList<Statut>();

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
                Statut statut = populate(rs);
                statuts.add(statut);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return statuts;
    }

    public int getStatutCount() {

        String query = "SELECT COUNT(*) FROM STATUT";
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

    private Statut populate(ResultSet rs) throws SQLException {
        Statut statut = new Statut();
        statut.setIdstatut(rs.getInt(1));
        statut.setNom(rs.getString(2));
        return statut;
    }
    
}
