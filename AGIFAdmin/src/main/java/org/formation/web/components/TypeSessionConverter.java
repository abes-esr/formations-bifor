/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.TypeSession;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class TypeSessionConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         Integer id = new Integer(value);
        AgifManageService service = new AgifManageServiceImpl();
       TypeSession typeSession = service.getTypeSessionById(id);
        return typeSession;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        TypeSession typeSession = (TypeSession) value;
        return typeSession.getIdtypesession().toString();
    }
}
