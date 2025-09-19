package org.formation.batch.webstats.export.statsspecifics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.formation.batch.export.Export;

import com.opencsv.CSVWriter;

public class Responses extends Export {
	protected void lineToCSV(CSVWriter writer, ResultSet rs) throws SQLException {
		writer.writeNext(new String[]{
				Integer.toString(rs.getInt(1)),
				Integer.toString(rs.getInt(2)),
				rs.getString(3),
				Integer.toString(rs.getInt(4))
		});
	}
	
	protected String getQuery() {
		return "(SELECT f.IDTYPEFORMATION, r.IDQUESTION, CASE WHEN r.CHOIX IS NULL THEN r.SPECIFICATION ELSE r.CHOIX END AS CHOIX, COUNT(0) " +
				"FROM REPONSE r, INSCRIPTION i, SESSIONS s, TYPEFORMATION f " +
				"WHERE " +
					"s.DATEDEBUT BETWEEN ADD_MONTHS( ? , -1) AND ? " +
					"AND i.ETAT IN ('VALIDEE') " +
					"AND s.IDSTATUT IN ('1', '2', '3', '5') " +
					"AND r.IDINSCRIPTION = i.IDINSCRIPTION " +
					"AND i.IDSESSIONS1 = s.IDSESSIONS " +
					"AND s.IDTYPEFORMATION = f.IDTYPEFORMATION " +
				"GROUP BY f.IDTYPEFORMATION, r.IDQUESTION, r.CHOIX, r.SPECIFICATION) " +
				"UNION " +
				"(SELECT f.IDTYPEFORMATION, r.IDQUESTION, CASE WHEN r.CHOIX IS NULL THEN r.SPECIFICATION ELSE r.CHOIX END AS CHOIX, COUNT(0) " +
				"FROM REPONSE r, INSCRIPTION i, SESSIONS s, TYPEFORMATION f " +
				"WHERE " +
					"s.DATEDEBUT BETWEEN ADD_MONTHS( ? , -1) AND ? " +
					"AND i.ETAT2 IN ('VALIDEE') " +
					"AND s.IDSTATUT IN ('1', '2', '3', '5') " +
					"AND r.IDINSCRIPTION = i.IDINSCRIPTION " +
					"AND i.IDSESSIONS2 = s.IDSESSIONS " +
					"AND s.IDTYPEFORMATION = f.IDTYPEFORMATION " +
				"GROUP BY f.IDTYPEFORMATION, r.IDQUESTION, r.CHOIX, r.SPECIFICATION)";
	}
	
	protected void setParameters(PreparedStatement st, Date date) throws SQLException {
		st.setDate(1, new java.sql.Date(date.getTime()));
        st.setDate(2, new java.sql.Date(date.getTime()));
        st.setDate(3, new java.sql.Date(date.getTime()));
        st.setDate(4, new java.sql.Date(date.getTime()));
	}
}
