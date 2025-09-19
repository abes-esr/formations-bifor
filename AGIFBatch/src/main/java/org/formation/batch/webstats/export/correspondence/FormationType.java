package org.formation.batch.webstats.export.correspondence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.formation.batch.export.Export;

import com.opencsv.CSVWriter;

public class FormationType extends Export {
	protected void lineToCSV(CSVWriter writer, ResultSet rs) throws SQLException {
		writer.writeNext(new String[]{Integer.toString(rs.getInt(1)), rs.getString(2)});
	}
	
	protected String getQuery() {
		return "SELECT IDTYPEFORMATION, NOM FROM TYPEFORMATION";
	}
	
	protected void setParameters(PreparedStatement st, Date date) throws SQLException {
		
	}
}
