/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.formation.model.Lieu;

/**
 *
 * @author jean-laurent
 */
public interface LieuDao extends Serializable {

    void create(Lieu Lieu);

    void edit(Lieu Lieu);

    void destroy(BigDecimal id);

    Lieu findLieu(BigDecimal id);

    List<Lieu> findLieuEntities(int maxResults, int firstResult);

    List<Lieu> findLieuEntities();

    int getLieuCount();

}
