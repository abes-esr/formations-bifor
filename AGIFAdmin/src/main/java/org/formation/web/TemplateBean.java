/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.web;

import javax.annotation.PostConstruct;
import org.formation.constant.Constant;
import org.formation.web.components.BufferedRW;

/**
 *
 * @author jean-laurent
 */
public class TemplateBean {
    // champ d'affichage
    private String css="lh_main_test";
    private String nom_machine;
    
    @PostConstruct
    public void init() {

        // affectation de la machine
        nom_machine = BufferedRW.hashMD5(Constant.getHOSTNAME());
        if (Constant.getEnv() == Constant.PROD) {
            css = "lh_main";
        }
    }
    
    public String getCss() {
        if (Constant.getEnv() == Constant.PROD) {
            css="lh_main";
        }
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }
      public String getNom_machine() {
        return nom_machine;
    }

    public void setNom_machine(String nom_machine) {
        this.nom_machine = nom_machine;
    }
    
}
