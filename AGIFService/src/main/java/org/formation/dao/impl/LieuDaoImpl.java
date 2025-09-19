/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.LieuDao;
import org.formation.model.Lieu;

/**
 *
 * @author jean-laurent
 */
public class LieuDaoImpl extends AbstractDao implements LieuDao {
	private static final long serialVersionUID = -5233660093134475329L;

	public LieuDaoImpl() {}

    public void create(Lieu l) {

        String query = "INSERT INTO LIEU VALUES(?,?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setBigDecimal(1, l.getIdlieu());
            st.setString(2, l.getNom());
            st.setString(3, l.getAdresse());
            st.setString(4, l.getVille());
            st.setString(5, l.getCodepostal());
            st.setString(6, l.getPays());
            st.setString(7, l.getAdresse2());
            st.setString(8, l.getAdresse3());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(Lieu l) {

         String query = "UPDATE LIEU l SET " +
                    "NOM = ?,"+
                    "ADRESSE = ?," +
                    "VILLE = ?," +
                    "CODEPOSTAL = ?," +
                    "PAYS = ?," +
                    "ADRESSE2 = ?," +
                    "ADRESSE3 = ? " +
                    "WHERE l.IDLIEU = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, l.getNom());
            st.setString(2, l.getAdresse());
            st.setString(3, l.getVille());
            st.setString(4, l.getCodepostal());
            st.setString(5, l.getPays());
            st.setString(6, l.getAdresse2());
            st.setString(7, l.getAdresse3());
            st.setBigDecimal(8, l.getIdlieu());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(BigDecimal id) {

        String query = "DELETE LIEU WHERE IDLIEU = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setBigDecimal(1, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public Lieu findLieu(BigDecimal id) {

        String query = "SELECT * FROM LIEU WHERE IDLIEU = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Lieu Lieu = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setBigDecimal(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Lieu = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return Lieu;
    }

    public List<Lieu> findLieuEntities(int maxResults,
            int firstResult) {
        return findLieuEntities(false, maxResults, firstResult);
    }

    public List<Lieu> findLieuEntities() {
        return findLieuEntities(true, -1, -1);
    }

    private List<Lieu> findLieuEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM LIEU ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Lieu> Lieux = new ArrayList<Lieu>();

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
                Lieu Lieu = populate(rs);
                Lieux.add(Lieu);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return Lieux;
    }

    public int getLieuCount() {

        String query = "SELECT COUNT(*) FROM LIEU";
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

    private Lieu populate(ResultSet rs) throws SQLException {
        Lieu lieu = new Lieu();
        lieu.setIdlieu(rs.getBigDecimal(1));
        lieu.setNom(rs.getString(2));
        lieu.setAdresse(rs.getString(3));
        lieu.setVille(rs.getString(4));
        lieu.setCodepostal(rs.getString(5));
        lieu.setPays(rs.getString(6));
        lieu.setAdresse2(rs.getString(7));
        lieu.setAdresse3(rs.getString(8));
        return lieu;
    }
    
}
