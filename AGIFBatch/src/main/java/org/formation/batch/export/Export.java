package org.formation.batch.export;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.formation.dao.impl.AbstractDao;

import com.opencsv.CSVWriter;

public abstract class Export extends AbstractDao {
	protected abstract void lineToCSV(CSVWriter writer, ResultSet rs) throws SQLException;
	
	protected abstract String getQuery();
	
	protected abstract void setParameters(PreparedStatement st, Date date) throws SQLException;
	
	public Export() {
		super();
	}
	
	public void generate(String destination, Date date) {
		System.out.println(destination + " [" + date + "]: Génération");
		
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
        	CSVWriter writer = new CSVWriter(new FileWriter(destination), ';', CSVWriter.NO_QUOTE_CHARACTER);
        	
            connection = this.getConnection();
            st = connection.prepareStatement(this.getQuery());
            
            this.setParameters(st, date);

            rs = st.executeQuery();
            while (rs.next()) {
            	this.lineToCSV(writer, rs);
            }
            
            writer.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            close(rs);
            close(st);
            close(connection);
        }
        
        System.out.println(destination + " [" + date + "]: Done");
	}
}
