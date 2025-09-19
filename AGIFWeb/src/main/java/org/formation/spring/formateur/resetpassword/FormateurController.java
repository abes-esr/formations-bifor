package org.formation.spring.formateur.resetpassword;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.formation.constant.Constant;
import org.formation.model.FormateurUser;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class FormateurController {

	@Autowired
	IUserService userService;
	@Autowired
	private JavaMailSender mailSender;

	/*
	 * APPELLEE DEPUIS LA PAGE OUBLIE DE MOT DE PASSE
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public String resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		FormateurUser user = userService.findUserbyUsername(userEmail);
		if (user == null) {
			throw new UserNotFoundException();
		}
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		return "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body>Veuillez consulter votre boîte aux lettres. Un lien permettant de choisir un nouveau mot de passe a été envoyé.</body></html>";
	}
	
	/**
	 * APPELLEE LORS D'UN CLIC SUR LE LIEN DANS LE MAIL
	 * vérifie d'abord la validité du token fourni
	 * si le toekn ezst valide, redirige sur la page de maj du mot de passe
	 * @param id adresse mail du formateur
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	@ResponseBody
	public String changePassword(HttpServletRequest request, @RequestParam("email") String email, @RequestParam("token") String token) {
	    String result = userService.validatePasswordResetToken(email, token);
	    if (result != null) {
	    	if ("expired".equals(result)) {
		    	return "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body>Le lien est expiré, veuillez faire une nouvelle demande</body></html>";
		    }
	    	if ("invalidToken".equals(result)) {
		    	return "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"></head><body>Erreur, veuillez faire une nouvelle demande</body></html>";
		    }
	    }
    	return "<!DOCTYPE html><html><head><script type=\"text/javascript\">window.location.href='"+ getAppUrl(request) +"/formateur-maj-motdepasse.html';</script></head></html>";
	}
	
	/**
	 * APPELLE DEPUIS LE FORMULAIRE DE MAJ DE MOT DE PASSE
	 * @param passwordDto
	 * @return
	 */
	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	@ResponseBody
	public String savePassword(HttpServletRequest request, @RequestParam("password")  String password) {
		System.out.println("entree dans savepassword");
		System.out.println("session? = " + SecurityContextHolder.getContext()
	                                  .getAuthentication().getName());
		
		System.out.println("verif du passwd cote serveur...");
		if (!userService.estMotDePasseOk(password))
		{
			
		    return "<!DOCTYPE html><html><head><title>Problème lors de la mise à jour du mot de passe</title></head><body>Problème lors de la mise à jour du mot de passe. Le mot de passe ne respecte pas les critères de complexité. Veuillez choisir un nouveau mot de passe <a href='" 
		    		+ getAppUrl(request) + "/formateur-maj-motdepasse.html'></a></body></html>";
		}

	    FormateurUser user = 
	      (FormateurUser) SecurityContextHolder.getContext()
	                                  .getAuthentication().getPrincipal();
	     
	    System.out.println("savepassword ; pares recup user");
	    
	    userService.changeUserPassword(user, password);
	    System.out.println("savepassword ;fin");
	    return "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"><title>mot de passe mis à jour avec succès</title></head><body>Votre mot de passe a ete mis a jour, <a href='" + getAppUrl(request) + "/faces/formateur-liste-sessions.xhtml'>accès à la liste des sessions</a></body></html>";
	}
	

	// ************************************************************************************************************
	// méthodes utilitaires DAO et mail
	//
	// ************************************************************************************************************
	

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale,
			final String token, final FormateurUser user) {
		final String url = contextPath + "/formateur/changePassword?email=" + user.getUsername() + "&token=" + token;
		final String message = "veuillez cliquer sru le lien pour choisir un nouveau passe etc.";
		return constructEmail("Formateur ABES : modifier votre mot de passe", message + " \r\n" + url, user);
	}
	private SimpleMailMessage constructEmail(String subject, String body, FormateurUser user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getUsername()); // email du user
        email.setFrom(Constant.MAIL_NOREPLY);
        return email;
}

}