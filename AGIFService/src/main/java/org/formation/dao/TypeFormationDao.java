/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.TypeFormation;

/**
 *
 * @author jean-laurent
 */
public interface TypeFormationDao extends Serializable {

    void create(TypeFormation TypeFormation);

    void edit(TypeFormation TypeFormation);

    void destroy(Short id);

    TypeFormation findTypeFormation(Short id);

    List<TypeFormation> findTypeFormationEntities(int maxResults, int firstResult);

    List<TypeFormation> findTypeFormationEntities();

    int getTypeFormationCount();

}
