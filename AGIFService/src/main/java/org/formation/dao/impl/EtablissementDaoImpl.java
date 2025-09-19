/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.EtablissementDao;
import org.formation.model.Etablissement;

/**
 *
 * @author jean-laurent
 */
public class EtablissementDaoImpl extends AbstractDao implements EtablissementDao {
	private static final long serialVersionUID = -2298278192321753024L;

	public EtablissementDaoImpl() {
	}

    
    public List<Etablissement> findEtablissementEntities() {
        
        String query = "SELECT DISTINCT iln,rcr,short_name "
                + " FROM lib_profile "
                + " WHERE main_library='Y' AND ILN>=1  " // on laisse l'iln ABES
                + " AND SUBSTR(short_name,1,4)<>'SUP/' " // on enleve les iln supprimés
                + " ORDER BY to_number(iln),short_name";
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Etablissement> etablissements = new ArrayList<Etablissement>();
        
        try {
            connection = getCBSConnection();
            
            st = connection.prepareStatement(query);
            
            rs = st.executeQuery();
            while (rs.next()) {
                Etablissement etablissement = populate(rs);
                etablissements.add(etablissement);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return etablissements;
    }
    
    private Etablissement populate(ResultSet rs) throws SQLException {
        
        Etablissement etablissement = new Etablissement();
        etablissement.setIln(rs.getInt("iln"));
        etablissement.setLibrary(rs.getString("rcr"));
        etablissement.setShort_name(rs.getString("short_name"));
        
        return etablissement;
    }
    
    public Etablissement findEtablissementByName(String name) {
        String query = "SELECT  DISTINCT iln,rcr,short_name "
                + " FROM lib_profile "
                + " WHERE main_library='Y' and short_name=? "
                + " ORDER BY short_name";
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Etablissement etablissement = new Etablissement();
        
        try {
            connection = getCBSConnection();
            st = connection.prepareStatement(query);
            st.setString(1, name);
            rs = st.executeQuery();
            if (rs.next()) {
                etablissement = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return etablissement;
    }
    
    public List<Etablissement> findRCRByName(String name) {
        String query = "SELECT  iln,rcr,short_name "
                + " FROM lib_profile "
                + " WHERE iln IN (SELECT iln FROM lib_profile WHERE main_library='Y'"
                + " AND short_name=?) "
                + " AND SUBSTR(rcr,6,2)<>'00' "
                + " AND main_library='N' " //suppression des RCRS virtuels
                + " ORDER BY rcr";
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Etablissement> etablissements = new ArrayList<Etablissement>();
        
        try {
            connection = getCBSConnection();
            st = connection.prepareStatement(query);
            st.setString(1, name);
            rs = st.executeQuery();
            while (rs.next()) {
                Etablissement etablissement = populate(rs);
                etablissements.add(etablissement);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return etablissements;
    }
    
    public Etablissement findRCRByLibrary(String library) {
        String query = "SELECT  DISTINCT iln,rcr,short_name "
                + " FROM lib_profile "
                + " WHERE rcr=? "
                + " ORDER BY short_name";
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Etablissement etablissement = new Etablissement();
        
        try {
            connection = getCBSConnection();
            st = connection.prepareStatement(query);
            st.setString(1, library);
            rs = st.executeQuery();
            if (rs.next()) {
                etablissement = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return etablissement;
    }
    
    public Etablissement findAddressByRCR(String library) {
        
        /*String query = "SELECT DISTINCT library,R120$A as full_name,R130$A as sub_name,"
                + "R200$A as adresse_principale,R200$B as address2,R200$E as postcode,"
                + "R200$F as city,R210$A as telephone,R220$D as email_address,"
                + "R211$A as fax_address "
                + "FROM SELECT_REGISTRY "
                + "WHERE library=? and R200$A is not null "
                + "ORDER BY library";*/
        
        String query = "SELECT DISTINCT RCR as library,NOM as full_name,ORGA as sub_name,"
                + "NOMPOSTAL as adresse_principale,ADPHYSIQUE as address2,ADPOSTALE as address3,"
                + "CDPOSTAL as postcode,"
                + "VILLE as city,TEL as telephone,EMAIL as email_address,"
                + "FAX as fax_address,NOM_COURT as shortname,ILN as iln "
                + "FROM VIEW_LST_BIBS_FORMA "
                + "WHERE RCR=? and NOMPOSTAL is not null "
                + "ORDER BY RCR";
        
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Etablissement etablissement = new Etablissement();
        
        try {
            connection = getCBSConnection();
            st = connection.prepareStatement(query);
            st.setString(1, library);
            rs = st.executeQuery();
            if (rs.next()) {
                etablissement.setLibrary(rs.getString("library"));
                etablissement.setShort_name(rs.getString("shortname"));
                etablissement.setIln(rs.getInt("iln"));
                etablissement.setFull_name(rs.getString("full_name"));
                etablissement.setSub_name(rs.getString("sub_name"));
                etablissement.setAddress(rs.getString("adresse_principale"));
                etablissement.setAddress2(rs.getString("address2"));
                etablissement.setAddress3(rs.getString("address3"));
                etablissement.setPostcode(rs.getString("postcode"));
                etablissement.setCity(rs.getString("city"));
                etablissement.setTelephone(rs.getString("telephone"));
                etablissement.setEmail_address(rs.getString("email_address"));
                etablissement.setFax_address(rs.getString("fax_address"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return etablissement;
    }

    @Override
    public List<Etablissement> findAllEtablissementEntities() {
        
        String query = "SELECT DISTINCT iln,rcr,short_name "
                + " FROM lib_profile "
                + " WHERE main_library = 'Y' AND ILN >= 1  " // on laisse l'ILN ABES
                + " AND SUBSTR(short_name,1,4) <> 'SUP/' " // on enleve les iln supprimés
                + " ORDER BY short_name";
        
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Etablissement> etablissements = new ArrayList<Etablissement>();
        
        try {
            connection = getCBSConnection();
            
            st = connection.prepareStatement(query);
            
            rs = st.executeQuery();
            while (rs.next()) {
                Etablissement etablissement = populate(rs);
                etablissements.add(etablissement);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return etablissements;
    }
}
