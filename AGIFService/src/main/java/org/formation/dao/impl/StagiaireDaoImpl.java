/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.formation.dao.InscriptionDao;
import org.formation.dao.StagiaireDao;
import org.formation.model.Stagiaire;
import org.formation.tools.Encryption;

/**
 *
 * @author jean-laurent
 */
public class StagiaireDaoImpl extends AbstractDao implements StagiaireDao {
	private static final long serialVersionUID = -7164809530160729743L;

	@Override
	public void create(Stagiaire s) {
        String query = "INSERT INTO STAGIAIRE "
                + "(IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, "
                + "ETABLISSEMENT, NUMRCR,ADRESSE, CODEPOSTAL, VILLE,"
                + " PAYS, TELEPHONE, MAIL,MAILCOORDINATEUR, "
                + " ADRESSE2,ADRESSE3,SERVICE ) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setInt(1, s.getIdstagiaire());
            st.setString(2, s.getCivilite());
            st.setString(3, s.getNom());
            st.setString(4, s.getPrenom());
            st.setString(5, s.getFonction());
            st.setString(6, s.getEtablissement());
            st.setString(7, s.getNumrcr());
            st.setString(8, s.getAdresse());
            st.setString(9, s.getCodepostal());
            st.setString(10, s.getVille());
            st.setString(11, s.getPays());
            st.setString(12, s.getTelephone());
            st.setString(13, s.getMail());
            st.setString(14, s.getMailcoordinateur());
            st.setString(15, s.getAdresse2());
            st.setString(16, s.getAdresse3());
            st.setString(17, s.getService());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

	@Override
    public void edit(Stagiaire s) {

        String query = "UPDATE STAGIAIRE  SET "
                + "CIVILITE = ?,"
                + "NOM = ?,"
                + "PRENOM = ?,"
                + "FONCTION = ?,"
                + "ETABLISSEMENT = ?,"
                + "NUMRCR = ?,"
                + "ADRESSE = ?,"
                + "CODEPOSTAL = ?,"
                + "VILLE = ?,"
                + "PAYS = ?,"
                + "TELEPHONE = ?,"
                + "MAIL = ?, "
                + "MAILCOORDINATEUR = ?, "
                + "ADRESSE2 = ?,"
                + "ADRESSE3 = ?, "
                + "SERVICE = ? "
                + "WHERE IDSTAGIAIRE = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            // Set the values
            st.setString(1, s.getCivilite());
            st.setString(2, s.getNom());
            st.setString(3, s.getPrenom());
            st.setString(4, s.getFonction());
            st.setString(5, s.getEtablissement());
            st.setString(6, s.getNumrcr());
            st.setString(7, s.getAdresse());
            st.setString(8, s.getCodepostal());
            st.setString(9, s.getVille());
            st.setString(10, s.getPays());
            st.setString(11, s.getTelephone());
            st.setString(12, s.getMail());
            st.setString(13, s.getMailcoordinateur());
            st.setString(14, s.getAdresse2());
            st.setString(15, s.getAdresse3());
            st.setString(16, s.getService());

            // Key
            st.setInt(17, s.getIdstagiaire());
            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    @Override
    public void destroy(Integer id) {

        String query = "DELETE STAGIAIRE WHERE IDSTAGIAIRE = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    @Override
    public Stagiaire findStagiaire(Integer id) {
        String query = "SELECT IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, ETABLISSEMENT, NUMRCR, ADRESSE, CODEPOSTAL, VILLE, PAYS, TELEPHONE, MAIL, MAILCOORDINATEUR, ADRESSE2, ADRESSE3, SERVICE FROM STAGIAIRE WHERE IDSTAGIAIRE = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Stagiaire stagiaire = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                stagiaire = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return stagiaire;
    }

    @Override
    public List<Stagiaire> findStagiaireEntities(int maxResults,
            int firstResult) {
        return findStagiaireEntities(false, maxResults, firstResult);
    }

    @Override
    public List<Stagiaire> findStagiaireEntities() {
        return findStagiaireEntities(true, -1, -1);
    }

    private List<Stagiaire> findStagiaireEntities(boolean all, int maxResults,
            int firstResult) {
        
        String query = "SELECT IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, ETABLISSEMENT, NUMRCR, ADRESSE, CODEPOSTAL, VILLE, PAYS, TELEPHONE, MAIL, MAILCOORDINATEUR, ADRESSE2, ADRESSE3, SERVICE FROM STAGIAIRE";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Stagiaire> Stagiaires = new ArrayList<Stagiaire>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Stagiaire stagiaire = populate(rs);

                InscriptionDao inscriptionDao = new InscriptionDaoImpl();
                stagiaire.setInscriptionList(inscriptionDao.findInscriptionByStagiaire(stagiaire));
                Stagiaires.add(stagiaire);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return Stagiaires;
    }

    @Override
    public int getStagiaireCount() {

        String query = "SELECT COUNT(*) FROM STAGIAIRE";
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

    private Stagiaire populate(ResultSet rs) throws SQLException {
        Stagiaire stagiaire = new Stagiaire();
        stagiaire.setIdstagiaire(rs.getInt(1));
        stagiaire.setCivilite(rs.getString(2));
        stagiaire.setNom(rs.getString(3));
        stagiaire.setPrenom(rs.getString(4));
        stagiaire.setFonction(rs.getString(5));
        stagiaire.setEtablissement(rs.getString(6));
        stagiaire.setNumrcr(rs.getString(7));
        stagiaire.setAdresse(rs.getString(8));
        stagiaire.setCodepostal(rs.getString(9));
        stagiaire.setVille(rs.getString(10));
        stagiaire.setPays(rs.getString(11));
        stagiaire.setTelephone(rs.getString(12));
        stagiaire.setMail(rs.getString(13));
        stagiaire.setMailcoordinateur(rs.getString(14));
        stagiaire.setAdresse2(rs.getString(15));
        stagiaire.setAdresse3(rs.getString(16));
        stagiaire.setService(rs.getString(17));

        return stagiaire;
    }

    @Override
    public Stagiaire findStagiaireByNomPrenom(String nom, String prenom) {

        String query = "SELECT IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, ETABLISSEMENT, NUMRCR, ADRESSE, CODEPOSTAL, VILLE, PAYS, TELEPHONE, MAIL, MAILCOORDINATEUR, ADRESSE2, ADRESSE3, SERVICE FROM STAGIAIRE WHERE UPPER(NOM) LIKE UPPER(?) AND "
                + "UPPER(PRENOM) LIKE UPPER(?)";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Stagiaire stagiaire = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, nom);
            st.setString(2, prenom);

            rs = st.executeQuery();
            if (rs.next()) {
                stagiaire = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return stagiaire;
    }

    @Override
    public Stagiaire findStagiaireByIdentifiantMdp(String identifiant,
                                                   String motdepasse) {
        String query = "SELECT IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, ETABLISSEMENT, NUMRCR, ADRESSE, CODEPOSTAL, VILLE, PAYS, TELEPHONE, MAIL, MAILCOORDINATEUR, ADRESSE2, ADRESSE3, SERVICE FROM STAGIAIRE "
                    + "WHERE LOWER(IDENTIFIANT)=? AND MOTDEPASSE=?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Stagiaire stagiaire = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, identifiant.toLowerCase());
            st.setString(2, Encryption.encrypt(motdepasse));

            rs = st.executeQuery();
            if (rs.next()) {
                stagiaire = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return stagiaire;
    }

    @Override
    public int getStagiaireCountByIdentifiant(String identifiant) {
        
        String query = "SELECT COUNT(*) FROM STAGIAIRE WHERE IDENTIFIANT=?";
        int count = 0;
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, identifiant);

            rs = st.executeQuery();
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

    @Override
    public int getStagiaireCountByMail(String mail) {

        String query = "SELECT COUNT(*) FROM STAGIAIRE WHERE MAIL=?";
        int count = 0;
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, mail);

            rs = st.executeQuery();
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

    @Override
    public Stagiaire findStagiaireByMail(String mail) {
        String query = "SELECT IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, ETABLISSEMENT, NUMRCR, ADRESSE, CODEPOSTAL, VILLE, PAYS, TELEPHONE, MAIL, MAILCOORDINATEUR, ADRESSE2, ADRESSE3, SERVICE FROM STAGIAIRE WHERE MAIL=?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Stagiaire stagiaire = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, mail);            
            rs = st.executeQuery();
            if (rs.next()) {
                stagiaire = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return stagiaire;
    }

    @Override
    public Stagiaire findStagiaireByIdentifiant(String identifiant) {
         String query = "SELECT IDSTAGIAIRE, CIVILITE, NOM, PRENOM, FONCTION, ETABLISSEMENT, NUMRCR, ADRESSE, CODEPOSTAL, VILLE, PAYS, TELEPHONE, MAIL, MAILCOORDINATEUR, ADRESSE2, ADRESSE3, SERVICE FROM STAGIAIRE "
                    + "WHERE IDENTIFIANT=?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Stagiaire stagiaire = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, identifiant);
         
            rs = st.executeQuery();
            if (rs.next()) {
                stagiaire = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return stagiaire;
    }
}
