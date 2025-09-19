/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.InscriptionDao;
import org.formation.dao.QuestionDao;
import org.formation.dao.ReponseDao;
import org.formation.model.Inscription;
import org.formation.model.Question;
import org.formation.model.Reponse;

/**
 *
 * @author jean-laurent
 */
public class ReponseDaoImpl extends AbstractDao implements ReponseDao {
	private static final long serialVersionUID = 5156841802812038272L;

	public ReponseDaoImpl() {
    }

    public void create(Reponse r) {

        String query = "INSERT INTO REPONSE "
                + "(IDREPONSE, IDINSCRIPTION, IDQUESTION, CHOIX, "
                + "SPECIFICATION, DATEREPONSE) "
                + "VALUES (?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, r.getIdreponse());

            st.setInt(2, r.getIdinscription().getIdinscription());
            st.setBigDecimal(3, r.getIdquestion().getIdquestion());
            st.setString(4, r.getChoix());
            st.setString(5, r.getSpecification());
            st.setTimestamp(6, new java.sql.Timestamp(r.getDatereponse().getTime()));

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(Reponse r) {

        String query = "UPDATE REPONSE  SET "
                + "IDINSCRIPTION = ?,"
                + "IDQUESTION = ?,"
                + "CHOIX = ?,"
                + "SPECIFICATION = ?,"
                + "DATEREPONSE = ?"
                + " WHERE IDREPONSE = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, r.getIdinscription().getIdinscription());
            st.setBigDecimal(2, r.getIdquestion().getIdquestion());
            st.setString(3, r.getChoix());
            st.setString(4, r.getSpecification());
            st.setTimestamp(5, new java.sql.Timestamp(r.getDatereponse().getTime()));
            st.setInt(6, r.getIdreponse());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(Integer id) {

        String query = "DELETE REPONSE WHERE IDREPONSE = ?";

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

    public Reponse findReponse(Integer id) {

        String query = "SELECT * FROM REPONSE WHERE IDREPONSE = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Reponse reponse = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                reponse = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return reponse;
    }

    public List<Reponse> findReponseEntities(int maxResults,
            int firstResult) {
        return findReponseEntities(false, maxResults, firstResult);
    }

    public List<Reponse> findReponseEntities() {
        return findReponseEntities(true, -1, -1);
    }

    private List<Reponse> findReponseEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM REPONSE ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Reponse> reponses = new ArrayList<Reponse>();

        try {
            connection = getConnection();
            if (!all) {
                query = query + " WHERE  rownum_ > ? AND rownum_ <= ?";
                st = connection.prepareStatement(query);
                st.setInt(1, firstResult);
                st.setInt(2, firstResult + maxResults);
            } else {
                st = connection.prepareStatement(query);
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Reponse reponse = populate(rs);
                reponses.add(reponse);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return reponses;
    }

    public int getReponseCount() {

        String query = "SELECT COUNT(*) FROM REPONSE";
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

    private Reponse populate(ResultSet rs) throws SQLException {
        Reponse reponse = new Reponse();
        reponse.setIdreponse(rs.getInt(1));
        // forgein key        
        int idInscription = rs.getInt(2);
        InscriptionDao inscriptiondao = new InscriptionDaoImpl();
        Inscription inscription = inscriptiondao.findInscription(idInscription);

        reponse.setIdinscription(inscription);

        BigDecimal idQuestion = rs.getBigDecimal(3);
        QuestionDao questionDao = new QuestionDaoImpl();
        Question question = questionDao.findQuestion(idQuestion);

        reponse.setIdquestion(question);

        // Get the values
        reponse.setChoix(rs.getString(4));
        reponse.setSpecification(rs.getString(5));
        reponse.setDatereponse(rs.getDate(6));


        return reponse;
    }

    public List<Reponse> findReponseByInscription(Inscription i) {
        String query = "SELECT * FROM REPONSE WHERE IDINSCRIPTION = ?"
                + " ORDER BY IDQUESTION";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Reponse> Reponses = new ArrayList<Reponse>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setInt(1, i.getIdinscription());
            rs = st.executeQuery();
            while (rs.next()) {
                Reponse reponse = populate(rs);
                Reponses.add(reponse);
            }
            } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return Reponses;
    }
}
