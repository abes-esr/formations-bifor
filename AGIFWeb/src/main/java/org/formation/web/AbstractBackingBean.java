/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author jean-laurent
 */
public class AbstractBackingBean implements Serializable {
	private static final long serialVersionUID = -2768486868243508306L;

	public Locale getLocale() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale locale = null;
        if (ctx.getViewRoot() != null) {
            locale = ctx.getViewRoot().getLocale();
        } else {
            locale = ctx.getApplication().getDefaultLocale();
        }
        return locale;

    }

    public String getDisplayString(String bundleName, String key, Object[] params,
            Locale locale) {
        String text = null;
        String basename = "org.formation.ressources.";
        ResourceBundle bundle = ResourceBundle.getBundle(basename + bundleName, locale);
        try {
            text = bundle.getString(key);
        } catch (MissingResourceException mre) {
            text = "???" + key + "???";
        }
        if (params != null) {
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params);
        }
        return text;
    }

  
}
