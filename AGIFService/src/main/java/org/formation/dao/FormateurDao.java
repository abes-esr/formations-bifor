/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.Formateur;

/**
 *
 * @author jean-laurent
 */
public interface FormateurDao extends Serializable {

    void create(Formateur formateur);

    void edit(Formateur formateur);

    void destroy(Integer id);

    Formateur findFormateur(Integer id);

    List<Formateur> findFormateurEntities(int maxResults, int firstResult);

    List<Formateur> findFormateurEntities();

    int getFormateurCount();

}
