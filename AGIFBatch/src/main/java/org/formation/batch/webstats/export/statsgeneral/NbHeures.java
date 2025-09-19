package org.formation.batch.webstats.export.statsgeneral;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.formation.batch.export.Export;

import com.opencsv.CSVWriter;

public class NbHeures extends Export {
	protected void lineToCSV(CSVWriter writer, ResultSet rs) throws SQLException {
		Integer hours = (int) Math.round(rs.getFloat(2));
		writer.writeNext(new String[]{rs.getString(1), hours.toString()});
	}
	
	protected String getQuery() {
		return "(SELECT s.IDTYPESESSION, SUM(DUREE)/60 " +
				"FROM INSCRIPTION i, SESSIONS s " + 
				"WHERE " + 
					"s.DATEDEBUT BETWEEN ADD_MONTHS( ? , -1) AND ? " + 
					"AND i.ETAT IN ('VALIDEE') " + 
					"AND s.IDSTATUT IN ('1', '2', '3', '5') " + 
					"AND i.IDSESSIONS1 = s.IDSESSIONS " +
				"GROUP BY s.IDTYPESESSION) " +
				
				"UNION " +
				
				"(SELECT s.IDTYPESESSION, SUM(DUREE)/60 " +
				"FROM INSCRIPTION i, SESSIONS s " +
				"WHERE " +
					"s.DATEDEBUT BETWEEN ADD_MONTHS( ? , -1) AND ? " + 
					"AND i.ETAT2 IN ('VALIDEE') " +
					"AND s.IDSTATUT IN ('1', '2', '3', '5') " +
					"AND i.IDSESSIONS2 = s.IDSESSIONS " +
				"GROUP BY s.IDTYPESESSION)";
	}
	
	protected void setParameters(PreparedStatement st, Date date) throws SQLException {
		st.setDate(1, new java.sql.Date(date.getTime()));
        st.setDate(2, new java.sql.Date(date.getTime()));
        st.setDate(3, new java.sql.Date(date.getTime()));
        st.setDate(4, new java.sql.Date(date.getTime()));
	}
}
