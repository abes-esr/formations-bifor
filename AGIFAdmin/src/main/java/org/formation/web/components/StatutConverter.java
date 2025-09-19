/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.Statut;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class StatutConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer id = new Integer(value);
        AgifManageService service = new AgifManageServiceImpl();
        Statut statut = service.getStatutById(id);
        return statut;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Statut statut = (Statut) value;
        return statut.getIdstatut().toString();
    }
}
