package org.formation.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formation.constant.Constant;
import org.formation.dao.FormateurUserDao;
import org.formation.model.FormateurUser;

public class FormateurUserDaoImpl extends AbstractDao implements FormateurUserDao {

	private static final long serialVersionUID = 1L;

	@Override
	public FormateurUser findFormateurUser(String email) {
		
		Logger.getLogger(FormateurUserDaoImpl.class.getName()).log(Level.WARNING,
                "entree dans FormateurUserDaoImpl...");
		
		// faut-il ajouter dans la clause where 'uniquement les formateurs sudoc'?
		// si oui, ajouter une colonne dans la table formateur_user avec un boolean 
		// formateur sudoc : oui ou non
		String query = "SELECT motdepasse FROM FORMATEUR_USER WHERE email = ?";
	    Connection connection = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    FormateurUser formateurUser = null;
	    try {
	        connection = getConnection();
	        st = connection.prepareStatement(query);
	        st.setString(1, email);
	        rs = st.executeQuery();
	        if (rs.next()) {
	        	String[] roles = new String[1];
	        	roles[0] = "USER"; // pas de gestion fine des roles, tous les formateurs sont "ROLE_USER"
	        	formateurUser = new FormateurUser(email, rs.getString(1), roles);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } finally {
	        close(rs);
	        close(st);
	        close(connection);
	    }
	    return formateurUser;
	}
	
	@Override
	public void incrementeFormateurUserNbTentatives(String email)
	{
		String query = "update FORMATEUR_USER set nbtentatives = nbtentatives + 1 where email = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, email);
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
	}

	@Override
	public void initZeroFormateurUserNbTentatives(String email) {
		String query = "update FORMATEUR_USER set nbtentatives = 0 where email = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, email);
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
	}

	@Override
	public boolean estCompteFormateurUserBloque(String email) {
		
		 String query = "select nbtentatives from FORMATEUR_USER where email = ?";
	        int nb = 0;
	        Connection connection = null;
	        PreparedStatement st = null;
	        ResultSet rs = null;
	        try {
	        	connection = getConnection();
	            st = connection.prepareStatement(query);
	            st.setString(1, email);
	            rs = st.executeQuery();
	            if (rs.next()) {
	                nb = rs.getInt(1);
	            }
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        } finally {
	            close(rs);
	            close(st);
	            close(connection);
	        }
	        return (nb>Constant.NBTENTATIVESCPTFORMATEUR)?true:false;
	}

	@Override
	public void enregistreToken(String email, String token, Date dateExp) {
		String query = "update FORMATEUR_USER set token = ?, tokenexp = ? where email = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, token);
            st.setTimestamp(2, new java.sql.Timestamp(dateExp.getTime()));
            st.setString(3, email);
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
	}

	@Override
	public FormateurUser findFormateurUserParToken(String token) {
		
		Logger.getLogger(FormateurUserDaoImpl.class.getName()).log(Level.WARNING,
                "entree dans findFormateurUserParToken...");
		String query = "SELECT email, motdepasse, tokenexp FROM FORMATEUR_USER WHERE token = ?";
	    Connection connection = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    FormateurUser formateurUser = null;
	    try {
	        connection = getConnection();
	        st = connection.prepareStatement(query);
	        st.setString(1, token);
	        rs = st.executeQuery();
	        if (rs.next()) {
	        	String[] roles = new String[1];
	        	roles[0] = "USER"; // pas de gestion fine des roles, tous les formateurs sont "ROLE_USER"
	        	formateurUser = new FormateurUser(
	        			rs.getString("email"),
	        			rs.getString("motdepasse"), 
	        			token,
	        			rs.getDate("tokenexp"),
	        			roles);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } finally {
	        close(rs);
	        close(st);
	        close(connection);
	    }
	    return formateurUser;
	}
	
	@Override
	public void majMotDePasse(FormateurUser formateurUser) {
		String query = "update FORMATEUR_USER set motdepasse = ?, token = null, tokenexp = null where email = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, formateurUser.getPassword());
            st.setString(2, formateurUser.getUsername());
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
	}
}
