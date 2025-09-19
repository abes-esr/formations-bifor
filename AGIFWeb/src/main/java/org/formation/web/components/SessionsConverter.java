/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.Sessions;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class SessionsConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        AgifService service = new AgifServiceImpl();
        Integer id = new Integer(value);
        Sessions sessions = service.getSessionsById(id);
        return sessions;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Sessions sessions = (Sessions) value;
        return sessions.getIdsessions().toString();
    }
}
