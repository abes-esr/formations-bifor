/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.TypeSessionDao;
import org.formation.model.TypeSession;

/**
 *
 * @author jean-laurent
 */
public class TypeSessionDaoImpl extends AbstractDao implements TypeSessionDao {
	private static final long serialVersionUID = 8276032026720239632L;

	public TypeSessionDaoImpl() {}


    public void create(TypeSession ts) {

        String query = "INSERT INTO TYPESESSION VALUES(?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, ts.getIdtypesession());
            st.setString(2, ts.getNom());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(TypeSession ts) {

        String query = "UPDATE TYPESESSION TS SET "
                + "NOM = ? "
                + "WHERE TS.IDTYPESESSION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, ts.getNom());
            st.setInt(2, ts.getIdtypesession());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(Integer id) {

        String query = "DELETE TYPESESSION WHERE IDTYPESESSION = ?";

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

    public TypeSession findTypeSession(Integer id) {

        String query = "SELECT * FROM TYPESESSION WHERE IDTYPESESSION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        TypeSession typesession = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                typesession = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return typesession;
    }

    public List<TypeSession> findTypeSessionEntities(int maxResults,
            int firstResult) {
        return findTypeSessionEntities(false, maxResults, firstResult);
    }

    public List<TypeSession> findTypeSessionEntities() {
        return findTypeSessionEntities(true, -1, -1);
    }

    private List<TypeSession> findTypeSessionEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM TYPESESSION ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<TypeSession> typesessions = new ArrayList<TypeSession>();

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
                TypeSession typesession = populate(rs);
                typesessions.add(typesession);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return typesessions;
    }

    public int getTypeSessionCount() {

        String query = "SELECT COUNT(*) FROM TYPESESSION";
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

    private TypeSession populate(ResultSet rs) throws SQLException {
        TypeSession typesession = new TypeSession();
        typesession.setIdtypesession(rs.getInt(1));
        typesession.setNom(rs.getString(2));        
        return typesession;
    }
    
}
