/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.path;

import org.formation.constant.Constant;

/**
 *
 * @author jean-laurent
 */
public class PathFile {
    public static String CONTEXT = "/DocumentsFormation/formations/";
    public static char SEPARATEUR = java.io.File.separatorChar;
    
    public static String CONVOCATION 				= Constant.getDocument() + "Convocations" + SEPARATEUR;
    public static String EMARGEMENT 				= Constant.getDocument() + "Emargement" + SEPARATEUR;
    public static String ATTESTATION 				= Constant.getDocument() + "Attestations" + SEPARATEUR;
    public static String LISTE_STAGIAIRES 			= Constant.getDocument() + "Sessions" + SEPARATEUR;
    public static String CHEVALETS 					= Constant.getDocument() + "Chevalets" + SEPARATEUR;
    
    public static String PATH_CONVOCATION 			= CONTEXT + "Convocations" + SEPARATEUR;
    public static String PATH_EMARGEMENT 			= CONTEXT + "Emargement" + SEPARATEUR;
    public static String PATH_ATTESTATION 			= CONTEXT + "Attestations" + SEPARATEUR;
    public static String PATH_LISTE_STAGIAIRES 		= CONTEXT + "Sessions" + SEPARATEUR;
    public static String PATH_CHEVALETS 			= CONTEXT + "Chevalets" + SEPARATEUR;
    
    public static String MODELE_CONVOCATION 		= Constant.MODELES + "convocation.docx";
    public static String MODELE_LISTE_STAGIAIRES 	= Constant.MODELES + "liste_stagiaires.rtf";
    public static String MODELE_EMARGEMENT 			= Constant.MODELES + "emargement.rtf";
    public static String MODELE_ATTESTATION 		= Constant.MODELES + "attestation.rtf";
    public static String MODELE_CHEVALET 			= Constant.MODELES + "chevalets.rtf";
    
    public static String LISTE_ETABLISSEMENTS_STAR	= Constant.getDocument() + "etablissements_star.csv";
}
