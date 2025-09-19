package org.formation.spring.formateur.resetpassword;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.formation.model.FormateurUser;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	private AgifManageService service = new AgifManageServiceImpl();
	private static final int EXPIRATION_TOKEN = 60 * 24;

	/**
	 * enregistre le token et sa date de peremption dans la BD
	 */
	@Override
	public void createPasswordResetTokenForUser(final FormateurUser user, final String token) {
		service.enregistreFormateurUserToken(
				user.getUsername(), 
				token, this.calculateExpiryDate());
	}
	private Date calculateExpiryDate() {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, EXPIRATION_TOKEN);
		return new Date(cal.getTime().getTime());
	}

	/**
	 * lors du clic sur le lien dans le mail, vérif de la validité du token si
	 * le token est valide, il est placé dans l'objet Authnetification du
	 * context spring security
	 * 
	 * @param adresseMailFormateur
	 * @param token
	 * @return
	 */
	@Override
	public String validatePasswordResetToken(String adresseMailFormateur, String token) {
		FormateurUser user = service.getFormateurUserParToken(token);
		if ((user == null) || (!user.getUsername().equals(adresseMailFormateur.trim()))) {
			System.out.println("return invaidtoken");
			return "invalidToken";
		}
		System.out.println("avant calendar");
		Calendar cal = Calendar.getInstance();
		if ((user.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			System.out.println("---expired-------");
			// supprimer le token et sa date de la BD
			return "expired";
		}

		System.out.println("avant authentification....");
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		System.out.println("apres authentification....");
		return null;
	}

	/**
	 * enregistre le nouveau mot de passe du formateur dans la BD
	 * le mot de passe est encode avec bcrypt avant d'être stocké
	 */
	public void changeUserPassword(FormateurUser formateurUser, String password) {
		
		formateurUser.setPassword(passwordEncoder.encode(password));
		System.out.println("REC du password dans la BD -----");
		service.majMotDePasseFormateurUser(formateurUser);
	}
	/**
	 * verifie que le mot de passe respecte les règles de complexité :
	 * au moins 8 caractères, 
	 * au moins une majuscule, 
	 * au moins un chiffre,
	 * au moins un caractère spécial (!@#$%&*())
	 * @param motdepasse
	 * @return
	 */
	@Override
	public boolean estMotDePasseOk(String motDePasse)
	{
		return motDePasse.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()]).{8,}");
	}

	@Override
	public FormateurUser findUserbyUsername(String username) {
		return service.getFormateurUserByEmail(username);
	}
}
