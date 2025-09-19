package org.formation.batch.webstats.export.correspondence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.formation.batch.export.Export;

import com.opencsv.CSVWriter;

public class InscriptionStatus extends Export {
	protected void lineToCSV(CSVWriter writer, ResultSet rs) throws SQLException {
		writer.writeNext(new String[]{rs.getString(1)});
	}
	
	protected String getQuery() {
		return "(SELECT DISTINCT(ETAT) FROM INSCRIPTION) " +
				"UNION" +
				"(SELECT DISTINCT(ETAT2) FROM INSCRIPTION)";
	}
	
	protected void setParameters(PreparedStatement st, Date date) throws SQLException {
	}
}
