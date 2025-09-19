package org.formation.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * permet de stocker les informations d'authentification
 * d'un formateur pour lui permettre d'accéder aux pages sécurisées
 */
public class FormateurUser implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String[] roles;
    private String token;
    private Date expiryDate;

	public FormateurUser(String username, String password, String... roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	public FormateurUser(String username, String password, String token, Date tokenExp, String... roles) {
		this.username = username;
		this.password = password;
		this.token = token;
		this.expiryDate = tokenExp;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
