package org.formation.batch;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.formation.batch.webstats.export.correspondence.FormationQuestions;
import org.formation.batch.webstats.export.correspondence.FormationStatus;
import org.formation.batch.webstats.export.correspondence.FormationType;
import org.formation.batch.webstats.export.correspondence.InscriptionStatus;
import org.formation.batch.webstats.export.statsgeneral.NbHeures;
import org.formation.batch.webstats.export.statsgeneral.NbJours;
import org.formation.batch.webstats.export.statsgeneral.NbStagiaires;
import org.formation.batch.webstats.export.statsspecifics.Etablissement;
import org.formation.batch.webstats.export.statsspecifics.Responses;
import org.formation.constant.Constant;

public class WebStatsExport {
	private static String YEAR = "";
	private static String MONTH = "";

	public static void main(String[] args) throws ParseException, IOException {
		WebStatsExport.initDate(args);
		System.out.println("Génération des statistiques pour " + WebStatsExport.getDate());
		
		// Exports des référentiels
		FormationStatus formationStatus = new FormationStatus();
		formationStatus.generate(WebStatsExport.getFileName("correspondence_formationStatus.csv"), WebStatsExport.getDate());
		
		FormationType formationType = new FormationType();
		formationType.generate(WebStatsExport.getFileName("correspondence_formationType.csv"), WebStatsExport.getDate());
		
		FormationQuestions formationQuestions = new FormationQuestions();
		formationQuestions.generate(WebStatsExport.getFileName("correspondence_formationQuestions.csv"), WebStatsExport.getDate());
		
		InscriptionStatus inscriptionStatus = new InscriptionStatus();
		inscriptionStatus.generate(WebStatsExport.getFileName("correspondence_inscriptionStatus.csv"), WebStatsExport.getDate());
		
		// Exports des statistiques généralistes
		NbStagiaires stagiaires = new NbStagiaires();
		stagiaires.generate(WebStatsExport.getFileName("general_nbStagiaires.csv"), WebStatsExport.getDate());
		
		NbJours jours = new NbJours();
		jours.generate(WebStatsExport.getFileName("general_nbJours.csv"), WebStatsExport.getDate());
		
		NbHeures heures = new NbHeures();
		heures.generate(WebStatsExport.getFileName("general_nbHeures.csv"), WebStatsExport.getDate());
		
		// Exports des statistiques spécifiques
		Etablissement etablissements = new Etablissement();
		etablissements.generate(WebStatsExport.getFileName("spec_etablissement.csv"), WebStatsExport.getDate());
		
		Responses responses = new Responses();
		responses.generate(WebStatsExport.getFileName("spec_responses.csv"), WebStatsExport.getDate());
	}
	
	private static Date getDate() throws ParseException {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formater.parse(WebStatsExport.YEAR + "-" + WebStatsExport.MONTH + "-01");
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.MONTH, 1); // Je rajoute un mois pour arriver au mois suivant (en effet, comme je fais 01, je suis au début du mois hors je dois passer à mes stats la date max de recherche
		return c.getTime();
	}
	
	private static String getFileName(String fileName) {
		return Constant.getStatistiques() + WebStatsExport.YEAR + WebStatsExport.MONTH + "_" + fileName;
	}

	private static void initDate(String[] args) {
		if (args.length == 0) {
			SimpleDateFormat formaterYear = new SimpleDateFormat("yyyy");
			SimpleDateFormat formaterMonth = new SimpleDateFormat("MM");
			
			Date today = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(today); 
			c.add(Calendar.MONTH, -1); // Je le prends tel quel car dans les stats je fais "plus petit que" ou j'enlève un mois
			
			WebStatsExport.YEAR = formaterYear.format(c.getTime());
			WebStatsExport.MONTH = formaterMonth.format(c.getTime());
		}
		else if (args.length == 2) {
			if (args[0].length() == 4 && args[1].length() == 2) {
				try { 
					// Je prends les arguments comme ils sont car dans les stats j'aoute un mois
					
					Integer.parseInt(args[0]);
					Integer.parseInt(args[1]);
					
					WebStatsExport.YEAR = args[0];
					WebStatsExport.MONTH = args[1];
			    } catch(NumberFormatException e) { 
			    	throw new IllegalArgumentException("Format des paramètres incorrects: (" + args[0] + " & " + args[1] + "). Taper help.");
			    } catch(NullPointerException e) {
			    	throw new IllegalArgumentException("Format des paramètres incorrects: (" + args[0] + " & " + args[1] + "). Taper help.");
			    }
			}
			else {
				throw new IllegalArgumentException("Longueur des paramètres incorrects: (" + args[0] + " & " + args[1] + "). Taper help.");
			}
		}
		else {
			System.out.println("Usage: java -jar package.war year month");
			System.out.println("You must execute this script 01 of a month");
			System.out.println("If no parameters are given, the actual date is used.");
			System.out.println("Year (YYYY) i the year when you want stats.");
			System.out.println("Month (MM) i the month when you want stats.");
			System.out.println("If I want stat for February 2016, I must give 2016 02.");
			System.out.println("If we are 01/03/2016, i will have stats for February.");
		}
	}
}
