/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author jean-laurent
 */
public class FieldValidator implements Validator {

    /**
     * fonction générique de validation d'un champ
     *
     * @param FacesContext context
     * @param UIComponent component
     * @param Object value
     */
    public void validate(FacesContext context,
            UIComponent component,
            Object value) throws ValidatorException {
        //Get the type of validator
        String type = (String) component.getAttributes().get("typeValidator");
        // Get the message
        String msg = (String) component.getAttributes().get("messageValidator");
        
        //Get the component's contents and cast it to a String
        String enteredText = (String) value;
        String expression = "";
        //Set the  pattern string
        if ("codepostal".equals(type)) {
            expression= "^([0-9]{5})$";
        }
        if ("rcr".equals(type)) {
            expression= "^([0-9]{9})$";
        }
        if ("mail".equals(type)) {
            expression= ".+@.+\\.[a-z]+";
            /*expression= "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/
            
            
            
        }
        
        
        
        if ("mailCoordinateur".equals(type)) {
            //expression= ".+@.+\\.[a-z]+";
            expression= "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*([,;]"
                    + "\\s*\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*";

        }        
                
        if ("name".equals(type)) {
            expression="^[a-zA-Z]+(([',\\s.-][a-zA-Z])?[a-zA-Z]*)*$";
        }
        if ("login".equals(type)) {
            expression="^[a-zA-Z0-9]+$";
        }
        if ("password".equals(type)) {
            expression="^[a-zA-Z0-9]+$";
        }

        if ("fonction".equals(type)) {
            expression="^[a-zA-Zéèàêôùçëïöü]+(([',\\s.-][a-zA-Zéèàêôùçëïöü])"
                    + "¨?[a-zA-Zéèàêôùçëïöü]*)*$";
        }
        if ("service".equals(type)) {
            expression="^[a-zA-Zéèàêôùçëïöü]+(([',\\s.-][a-zA-Zéèàêôùçëïöü])"
                    + "¨?[a-zA-Zéèàêôùçëïöü]*)*$";
        }
        if ("firstname".equals(type)) {
            expression="^[a-zA-Zéèàêôùçïëöü]+(([',\\s.-][a-zA-Zéèàêôëùçïöü])"
                    + "?[a-zA-Zéèàêôùëçïöü]*)*$";
        }
        if ("phone".equals(type)) {
            expression="^0[1-68][0-9]{8}$";
        }
        if ("ville".equals(type)) {
            expression="^[a-zA-Zéèàêëôùçïöü]+(([',\\s.-][a-zA-Zéëèàêôùçïöü0-9])?"
                    + "[a-zA-Zéèàêôëùçïöü0-9]*)*$";
        }
        Pattern p = Pattern.compile(expression);

        //Match the given string with the pattern
        Matcher m = p.matcher(enteredText);

        //Check whether match is found
        boolean matchFound = m.matches();

        if (!matchFound) {
           FacesMessage message = new FacesMessage();
            message.setDetail(msg);
            message.setSummary(msg);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);            
        }
    }
}
