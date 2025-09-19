/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.formation.dao.QuestionDao;
import org.formation.dao.TypeFormationDao;
import org.formation.model.Question;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public class QuestionDaoImpl extends AbstractDao implements QuestionDao {
	private static final long serialVersionUID = -3122486255614166656L;

	public void create(Question q) {

        String query = "INSERT INTO QUESTION "
                + "(IDQUESTION, LIBELLEQUESTION, LIBELLECHOIX1, LIBELLECHOIX2,"
                + " LIBELLECHOIX3, LIBELLECHOIX4, UNIQUEMULTIPLE,SPECIFICATION,"
                + " IDTYPEFORMATION, TYPEQUESTION) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setBigDecimal(1, q.getIdquestion());
            st.setString(2, q.getLibellequestion());
            st.setString(3, q.getLibellechoix1());
            st.setString(4, q.getLibellechoix2());
            st.setString(5, q.getLibellechoix3());
            st.setString(6, q.getLibellechoix4());
            st.setString(7, q.getUniquemultiple());
            st.setString(8, q.getSpecification());
            st.setShort(9, q.getIdtypeformation().getIdtypeformation());
            st.setString(10, q.getTypequestion());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void edit(Question q) {

        String query = "UPDATE QUESTION  SET "
                + "LIBELLEQUESTION = ?,"
                + "LIBELLECHOIX1 = ?,"
                + "LIBELLECHOIX2 = ?,"
                + "LIBELLECHOIX3 = ?,"
                + "LIBELLECHOIX4 = ?,"
                + "UNIQUEMULTIPLE = ?,"
                + "SPECIFICATION = ?,"
                + "IDTYPEFORMATION = ?,"
                + "TYPEQUESTION = ? "
                + "WHERE IDQUESTION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setString(1, q.getLibellequestion());
            st.setString(2, q.getLibellechoix1());
            st.setString(3, q.getLibellechoix2());
            st.setString(4, q.getLibellechoix3());
            st.setString(5, q.getLibellechoix4());
            st.setString(6, q.getUniquemultiple());
            st.setString(7, q.getSpecification());
            st.setShort(8, q.getIdtypeformation().getIdtypeformation());
            st.setString(9, q.getTypequestion());
            st.setBigDecimal(10, q.getIdquestion());

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public void destroy(BigDecimal id) {

        String query = "DELETE QUESTION WHERE IDQUESTION = ?";

        Connection connection = null;
        PreparedStatement st = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setBigDecimal(1, id);

            st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(st);
            close(connection);
        }
    }

    public Question findQuestion(BigDecimal id) {

        String query = "SELECT * FROM QUESTION WHERE IDQUESTION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Question question = null;
        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setBigDecimal(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                question = populate(rs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return question;
    }

    public List<Question> findQuestionByTypeFormation(TypeFormation tf) {

        String query = "SELECT * FROM QUESTION WHERE IDTYPEFORMATION = ?";
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Question> Questions = new ArrayList<Question>();

        try {
            connection = getConnection();
            st = connection.prepareStatement(query);
            st.setShort(1, tf.getIdtypeformation());
            rs = st.executeQuery();
            while (rs.next()) {
                Question question = populate(rs);
                Questions.add(question);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return Questions;
    }
    
    public List<Question> findQuestionEntities(int maxResults, int firstResult){
        return findQuestionEntities(false, maxResults, firstResult);
    }

    public List<Question> findQuestionEntities() {
        return findQuestionEntities(true, -1, -1);
    }

    private List<Question> findQuestionEntities(boolean all, int maxResults,
            int firstResult) {

        String query = "SELECT * FROM "
                + " ( SELECT row_.*, rownum rownum_ FROM"
                + " ( SELECT * FROM QUESTION ) row_ ) ";

        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Question> Questions = new ArrayList<Question>();

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
                Question question = populate(rs);
                Questions.add(question);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(st);
            close(connection);
        }
        return Questions;
    }

    public int getQuestionCount() {
        String query = "SELECT COUNT(*) FROM QUESTION";
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

    private Question populate(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setIdquestion(rs.getBigDecimal(1));
        question.setLibellequestion(rs.getString(2));
        question.setLibellechoix1(rs.getString(3));
        question.setLibellechoix2(rs.getString(4));
        question.setLibellechoix3(rs.getString(5));
        question.setLibellechoix4(rs.getString(6));
        question.setUniquemultiple(rs.getString(7));
        question.setSpecification(rs.getString(8));
        // foreign key
        Short idTypeFormation = new Short(rs.getShort(9));
        TypeFormationDao typeFormationDao = new TypeFormationDaoImpl();
        TypeFormation typeFormation = typeFormationDao.findTypeFormation(idTypeFormation);

        question.setIdtypeformation(typeFormation);
        question.setTypequestion(rs.getString(10));

        return question;
    }

    
}
