/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.Formateur;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class FormateurConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer id = new Integer(value);
        AgifManageService service = new AgifManageServiceImpl();
        Formateur formateur = service.getFormateurById(id);
        return formateur;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Formateur formateur = (Formateur) value;
        return formateur.getIdformateur().toString();
    }
}
