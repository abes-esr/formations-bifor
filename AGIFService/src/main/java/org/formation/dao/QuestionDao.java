/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.formation.model.Question;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public interface QuestionDao extends Serializable {

    void create(Question question);

    void edit(Question question);

    void destroy(BigDecimal id);

    Question findQuestion(BigDecimal id);

    List<Question> findQuestionEntities(int maxResults, int firstResult);

    List<Question> findQuestionEntities();

    List<Question> findQuestionByTypeFormation(TypeFormation tf);

    int getQuestionCount();

}