package org.formation.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formation.constant.Constant;

public abstract class AbstractDao {
	private static final Logger log = Logger.getLogger(Constant.class.getName());
	
	public AbstractDao() {
	}

	public Connection getCBSConnection() throws ClassNotFoundException, SQLException {
		return Constant.getCBSConnection();
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		return Constant.getSQLConnection();
	}

	// genere une cle primaire
	protected String generateId() {
		Date dateCourante = new Date();
		long key = dateCourante.getTime();

		String id = String.valueOf(key);
		return id.substring(6);
	}
	// Fermeture des ressources

	protected void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	protected void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	protected void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
	}
}
