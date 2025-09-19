/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web.components;

import java.math.BigDecimal;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.formation.model.Lieu;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;

/**
 *
 * @author jean-laurent
 */
public class LieuConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        BigDecimal id = new BigDecimal(value);
        AgifManageService service = new AgifManageServiceImpl();
        Lieu lieu = service.getLieuById(id);
        return lieu;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Lieu lieu = (Lieu) value;
        return lieu.getIdlieu().toString();
    }
}
