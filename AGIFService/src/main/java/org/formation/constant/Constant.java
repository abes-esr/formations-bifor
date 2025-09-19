/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.formation.constant;

import org.formation.dao.impl.AbstractDao;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jean-laurent
 */
public class Constant {
	private static final Logger log = Logger.getLogger(Constant.class.getName());
	
	public static final String MAIL_SMTP_HOST = ConfigLoader.getProperty("smtp.host.mail");
	public static final String MAIL_SMTP_STARTTLS_ENABLE = "true";
	public static final String MAIL_SMTP_AUTH = "true";
	public static final String MAIL_SMTP_PORT = ConfigLoader.getProperty("smtp.mail.port");
	public static final String MAIL_SMTP_SOCKETFACTORY_FALLBACK = "false";
	public static final String MAIL_USER = ConfigLoader.getProperty("mail.user");
	public static final String MAIL_PASSWORD = ConfigLoader.getProperty("mail.password");
	public static final String MAIL_SMTP_DEBUG = "false";
	public static final int MAIL_SMTP_SOCKETFACTORY_PORT = Integer.parseInt(ConfigLoader.getProperty("smtp.mail.port"));
	
	public static final String MAIL_NOREPLY = ConfigLoader.getProperty("mail.noreply");
	

	public static final char SEPARATEUR = java.io.File.separatorChar;
	public static final String SEPARATEUR_EXPORT = ";";
	public static final String MODELES = Constant.getWorkingDirectory() + "documents/modeles/";

	protected static final List<Integer> FORMATIONS_STAR = Arrays.asList(6, 8);
	protected static final List<Integer> FORMATIONS_SUDOCPS = Arrays.asList(4);
	protected static final List<Integer> FORMATIONS_SUPEB = Arrays.asList(5);
	protected static final List<Integer> FORMATIONS_CALAMES = Arrays.asList(7);
	protected static final List<Integer> FORMATIONS_AUTORITES = Arrays.asList(750);

	public static final int OUVERTE = 1;
	public static final int ARCHIVE = 2;
	public static final int CLOTURE = 3;
	public static final int SUPPRIME = 4;
	public static final int PAYEE = 5;

	public static final String ENCOURS = "ENCOURS";
	public static final String VALIDE = "VALIDEE";
	public static final String REFUSE = "REFUSEE";
	public static final String MODIFIEE = "MODIFIEE";
	public static final String PWDADMIN = ConfigLoader.getProperty("admin.password");

	public static final String PROD = "PROD";
	public static final String TEST = "TEST";
	public static final String DEV = "DEV";
	public static final String LOCAL = "LOCAL";

	protected static String SERVLET_VALIDATION;
	protected static String SERVLET_ECHANGE;
	
	public static final int NBTENTATIVESCPTFORMATEUR= 10;

	public static String getStatistiques() {
		String serveur;

		switch (Constant.getEnv()) {
		case Constant.LOCAL:
			serveur = ConfigLoader.getProperty("stats.url.localhost");
			break;
		case Constant.DEV:
			serveur = ConfigLoader.getProperty("stats.url.dev");
			break;
		case Constant.TEST:
			serveur = ConfigLoader.getProperty("stats.url.test");
			break;
		default:
			serveur = ConfigLoader.getProperty("stats.url.prod");
			break;
		}

		return serveur;
	}

	public static String getDocument() {
		switch (Constant.getEnv()) {
			case Constant.LOCAL:
				// Sauvegarde les documents à la racine du dossier où votre serveur local tourne
				return System.getProperty("user.dir") + SEPARATEUR + "applis" + SEPARATEUR + "bifor" + SEPARATEUR + "formations" + SEPARATEUR;
			default:
				return ConfigLoader.getProperty("documents.url");
		}
	}

	public static String getWorkingDirectory() {
		return ((new Constant()).getClass().getClassLoader().getResource("").getPath()) + "../../";
	}

	public static String getHOSTNAME() {
		String hostName = null;

		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			hostName = addr.getHostName().toLowerCase();
		} catch (UnknownHostException e) {
			Constant.log.log(Level.SEVERE, "Erreur dans la recuperation de la machine", e);
		}
		
		return hostName;
	}

	public static String getEnv() {
		// Si on est sur les machines acier ou ardoise
		if (getHOSTNAME().toLowerCase().contains("pc") || getHOSTNAME().toLowerCase().contains("lap")) {
			return Constant.LOCAL;
		} else if (getHOSTNAME().contains("-dev") || getHOSTNAME().contains("immortelle") || getHOSTNAME().contains("jacinthe")) {
			return Constant.DEV;
		} else if (getHOSTNAME().contains("-test")) {
			return Constant.TEST;
		} else {
			return Constant.PROD;
		}
	}

	public static String getSERVEUR_USER() {
		String serveur;

		switch (Constant.getEnv()) {
		case Constant.LOCAL:
			serveur = ConfigLoader.getProperty("web.url.localhost");
			break;
		case Constant.DEV:
			serveur = ConfigLoader.getProperty("web.url.dev");
			break;
		case Constant.TEST:
			serveur = ConfigLoader.getProperty("web.url.test");
			break;
		default:
			serveur = ConfigLoader.getProperty("web.url.prod");
			break;
		}

		return serveur;
	}

	public static String getSERVEUR_ADMIN() {
		String serveur;

		switch (Constant.getEnv()) {
		case Constant.LOCAL:
			serveur = ConfigLoader.getProperty("admin.url.localhost");
			break;
		case Constant.DEV:
			serveur = ConfigLoader.getProperty("admin.url.dev");
			break;
		case Constant.TEST:
			serveur = ConfigLoader.getProperty("admin.url.test");
			break;
		default:
			serveur = ConfigLoader.getProperty("admin.url.prod");
			break;
		}

		return serveur;
	}

	public static String getSERVLET_ECHANGE() {
		SERVLET_ECHANGE = getSERVEUR_ADMIN() + "AGIFAdmin/changeAffectation";
		return SERVLET_ECHANGE;
	}

	public static void setSERVLET_ECHANGE(String SERVLET_ECHANGE) {
		Constant.SERVLET_ECHANGE = SERVLET_ECHANGE;
	}

	public static String getSERVLET_VALIDATION() {
		SERVLET_VALIDATION = getSERVEUR_ADMIN() + "AGIFAdmin/faces" + "/valide-inscriptions.xhtml";
		return SERVLET_VALIDATION;
	}

	public static void setSERVLET_VALIDATION(String SERVLET_VALIDATION) {
		Constant.SERVLET_VALIDATION = SERVLET_VALIDATION;
	}
	
	public static String getSQLUrl() {
		String url;

		switch (Constant.getEnv()) {
		case Constant.LOCAL:
			url = ConfigLoader.getProperty("sql.url.test") + "/Abes";
			break;
		case Constant.DEV:
			url = ConfigLoader.getProperty("sql.url.dev") + "/Abes";
			break;
		case Constant.TEST:
			url = ConfigLoader.getProperty("sql.url.test") + "/Abes";
			break;
		default:
			url = ConfigLoader.getProperty("sql.url.prod") + "/Abes";
			break;
		}

		return url;
	}
	
	public static String getCBSUrl() {
		String url;

		switch (Constant.getEnv()) {
		case Constant.LOCAL:
			url = ConfigLoader.getProperty("sql.url.test") + "/APISUDOC";
			break;
		case Constant.DEV:
			url = ConfigLoader.getProperty("sql.url.dev") + "/APISUDOC";
			break;
		case Constant.TEST:
			url = ConfigLoader.getProperty("sql.url.test") + "/APISUDOC";
			break;
		default:
			url = ConfigLoader.getProperty("sql.url.prod") + "/APISUDOC";
			break;
		}

		Logger.getLogger(AbstractDao.class.getName()).log(Level.INFO, "Machine : " + Constant.getHOSTNAME());
		Logger.getLogger(AbstractDao.class.getName()).log(Level.INFO, "CBS : " + url);

		return url;
	}

	public static Connection getSQLConnection() throws SQLException, ClassNotFoundException {
		Class.forName("net.sf.log4jdbc.DriverSpy");
		Connection connection = DriverManager.getConnection(Constant.getSQLUrl(), ConfigLoader.getProperty("sql.user"), ConfigLoader.getProperty("sql.password"));

		// Désactiver le mode autocommit
		if (Constant.getEnv() == Constant.LOCAL) {
			connection.setAutoCommit(false);
		}
		return connection;
	}
	
	public static Connection getCBSConnection() throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.OracleDriver");
		switch (Constant.getEnv()) {
			case Constant.LOCAL:
			case Constant.DEV:
			case Constant.TEST:
				return DriverManager.getConnection(Constant.getCBSUrl(), ConfigLoader.getProperty("cbs.user.test"), ConfigLoader.getProperty("cbs.password.test"));
			default:
				return DriverManager.getConnection(Constant.getCBSUrl(), ConfigLoader.getProperty("cbs.user.prod"), ConfigLoader.getProperty("cbs.password.prod") );
		}
	}

	/**
	 * Pour travailler en local, il peut-être nécessaire de désactiver le
	 * parefeu
	 */
	public static MimeMessage initMessage() {
		final String username = ConfigLoader.getProperty("mail.user");
		final String password = ConfigLoader.getProperty("mail.password");

		Properties propsystem = System.getProperties();

		propsystem.put("mail.smtp.host", Constant.MAIL_SMTP_HOST);
		propsystem.put("mail.smtp.starttls.enable", Constant.MAIL_SMTP_STARTTLS_ENABLE);
		propsystem.put("mail.smtp.auth", Constant.MAIL_SMTP_AUTH);
		propsystem.put("mail.smtp.port", Constant.MAIL_SMTP_PORT);
		propsystem.put("mail.smtp.socketFactory.port", Constant.MAIL_SMTP_SOCKETFACTORY_PORT);
		propsystem.put("mail.smtp.socketFactory.fallback", Constant.MAIL_SMTP_SOCKETFACTORY_FALLBACK);
		propsystem.put("mail.user", Constant.MAIL_USER);
		propsystem.put("mail.password", Constant.MAIL_PASSWORD);
		propsystem.put("mail.smtp.debug", Constant.MAIL_SMTP_DEBUG);

		Session session = Session.getInstance(propsystem, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		return new MimeMessage(session);
	}
	
	public static List<Integer> getFormationsStar() {
		return FORMATIONS_STAR;
	}

	public static List<Integer> getFormationsSudocPS() {
		return FORMATIONS_SUDOCPS;
	}

	public static List<Integer> getFormationsSupeb() {
		return FORMATIONS_SUPEB;
	}

	public static List<Integer> getFormationsCalames() {
		return FORMATIONS_CALAMES;
	}

	public static List<Integer> getFormationsAutorites() {
		return FORMATIONS_AUTORITES;
	}
	
	private Constant() {
		
	}
}
