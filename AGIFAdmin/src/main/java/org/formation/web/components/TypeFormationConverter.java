/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.TypeFormation;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class TypeFormationConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Short id = new Short(value);
        AgifManageService service = new AgifManageServiceImpl();
        TypeFormation typeFormation = service.getTypeFormationById(id);
        return typeFormation;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        TypeFormation typeFormation = (TypeFormation) value;
        return typeFormation.getIdtypeformation().toString();
    }
}
