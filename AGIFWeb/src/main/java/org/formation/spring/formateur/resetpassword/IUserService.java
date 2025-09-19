package org.formation.spring.formateur.resetpassword;

import org.formation.model.FormateurUser;

public interface IUserService {

	void createPasswordResetTokenForUser(FormateurUser user, String token);

	String validatePasswordResetToken(String email, String token);

	void changeUserPassword(FormateurUser user, String password);
	
	boolean estMotDePasseOk(String motDePasse);
	
	FormateurUser findUserbyUsername(String username);

}
