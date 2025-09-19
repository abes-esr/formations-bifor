package org.formation.dao;

import java.io.Serializable;
import java.util.Date;

import org.formation.model.FormateurUser;

public interface FormateurUserDao extends Serializable {

	FormateurUser findFormateurUser (String email);
	FormateurUser findFormateurUserParToken(String token);
	void incrementeFormateurUserNbTentatives (String email);
	void initZeroFormateurUserNbTentatives (String email);
	boolean estCompteFormateurUserBloque(String email);
	void enregistreToken(String email, String token, Date dateExp);
	void majMotDePasse(FormateurUser formateurUser);
}
