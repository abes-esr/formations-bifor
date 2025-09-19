/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.formation.dao;

import java.io.Serializable;
import java.util.List;
import org.formation.model.Etablissement;

/**
 *
 * @author jean-laurent
 */
public interface EtablissementDao extends Serializable {
    
    List<Etablissement> findEtablissementEntities();
    
    List<Etablissement> findAllEtablissementEntities();
    
    List<Etablissement> findRCRByName(String name);
    
    Etablissement findEtablissementByName(String name);
    
    Etablissement findRCRByLibrary(String library);
    
    Etablissement findAddressByRCR(String library);
    
}
