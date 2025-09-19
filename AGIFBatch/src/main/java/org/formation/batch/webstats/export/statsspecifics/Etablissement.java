package org.formation.batch.webstats.export.statsspecifics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.formation.batch.export.Export;

import com.opencsv.CSVWriter;

public class Etablissement extends Export {
	protected void lineToCSV(CSVWriter writer, ResultSet rs) throws SQLException {
		writer.writeNext(new String[]{rs.getString(1), Integer.toString(rs.getInt(2)),  Integer.toString(rs.getInt(3))});
	}
	
	protected String getQuery() {
		return "(SELECT s.ETABLISSEMENT, sx.IDTYPEFORMATION, COUNT(0) " +
				"FROM STAGIAIRE s, INSCRIPTION i, SESSIONS sx " +
				"WHERE " +
					"sx.DATEDEBUT BETWEEN ADD_MONTHS( ? , -1) AND ? " +
					"AND i.ETAT IN ('VALIDEE') " +
					"AND sx.IDSTATUT IN ('1', '2', '3', '5') " +
					"AND s.IDSTAGIAIRE = i.IDSTAGIAIRE " +
					"AND i.IDSESSIONS1 = sx.IDSESSIONS " +
				"GROUP BY s.ETABLISSEMENT, sx.IDTYPEFORMATION) " +
				"UNION " +
				"(SELECT s.ETABLISSEMENT, sx.IDTYPEFORMATION, COUNT(0) " +
				"FROM STAGIAIRE s, INSCRIPTION i, SESSIONS sx " +
				"WHERE " +
					"sx.DATEDEBUT BETWEEN ADD_MONTHS( ? , -1) AND ? " +
					"AND i.ETAT2 IN ('VALIDEE') " +
					"AND sx.IDSTATUT IN ('1', '2', '3', '5') " +
					"AND s.IDSTAGIAIRE = i.IDSTAGIAIRE " +
					"AND i.IDSESSIONS2 = sx.IDSESSIONS " +
				"GROUP BY s.ETABLISSEMENT, sx.IDTYPEFORMATION)";
	}
	
	protected void setParameters(PreparedStatement st, Date date) throws SQLException {
		st.setDate(1, new java.sql.Date(date.getTime()));
        st.setDate(2, new java.sql.Date(date.getTime()));
        st.setDate(3, new java.sql.Date(date.getTime()));
        st.setDate(4, new java.sql.Date(date.getTime()));
	}
}
