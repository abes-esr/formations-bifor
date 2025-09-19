/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.TypeFormationDao;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public class TypeFormationDaoImpl extends AbstractDao implements TypeFormationDao {
	private static final long serialVersionUID = -487248912428642239L;

	public TypeFormationDaoImpl() {}

    public void create(TypeFormation tp) {

        String query = "INSERT INTO TYPEFORMATION VALUES(?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setShort(1, tp.getIdtypeformation());
            st.setString(2, tp.getNom());
            st.setString(3, tp.getDescription());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(TypeFormation tp) {

       String query = "UPDATE TYPEFORMATION TP SET " +
                    "NOM = ?," +
                    "DESCRIPTION = ?" +
                    "WHERE TP.IDTYPEFORMATION = ?";
       
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, tp.getNom());
            st.setString(2, tp.getDescription());
            st.setShort(3, tp.getIdtypeformation());
            
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(Short id) {

        String query = "DELETE TYPEFORMATION WHERE IDTYPEFORMATION = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setShort(1, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public TypeFormation findTypeFormation(Short id) {

        String query = "SELECT * FROM TYPEFORMATION WHERE IDTYPEFORMATION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        TypeFormation typeFormation = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setShort(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                typeFormation = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return typeFormation;
    }

    public List<TypeFormation> findTypeFormationEntities(int maxResults,
            int firstResult) {
        return findTypeFormationEntities(false, maxResults, firstResult);
    }

    public List<TypeFormation> findTypeFormationEntities() {
        return findTypeFormationEntities(true, -1, -1);
    }

    private List<TypeFormation> findTypeFormationEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM TYPEFORMATION ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<TypeFormation> typeFormations = new ArrayList<TypeFormation>();

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
                TypeFormation typeFormation = populate(rs);
                typeFormations.add(typeFormation);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return typeFormations;
    }

    public int getTypeFormationCount() {

        String query = "SELECT COUNT(*) FROM TYPEFORMATION";
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

    private TypeFormation populate(ResultSet rs) throws SQLException {
        TypeFormation typeFormation = new TypeFormation();
        typeFormation.setIdtypeformation(rs.getShort(1));
        typeFormation.setNom(rs.getString(2));
        typeFormation.setDescription(rs.getString(3));
        return typeFormation;
    }

 
}
