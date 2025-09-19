/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.Etablissement;
import org.formation.service.AgifService;
import org.formation.service.impl.AgifServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class EtablissementConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        AgifService service = new AgifServiceImpl();
        String name = new String(value);
        Etablissement etablissement = service.getEtablissementByName(name);
        return etablissement;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Etablissement etablissement = (Etablissement) value;
        return String.valueOf(etablissement.getIln());
    }
}
