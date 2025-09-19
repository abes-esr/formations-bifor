/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.TypeSession;

/**
 *
 * @author jean-laurent
 */
public interface TypeSessionDao extends Serializable {

    void create(TypeSession TypeSession);

    void edit(TypeSession TypeSession);

    void destroy(Integer id);

    TypeSession findTypeSession(Integer id);

    List<TypeSession> findTypeSessionEntities(int maxResults, int firstResult);

    List<TypeSession> findTypeSessionEntities();

    int getTypeSessionCount();

}
