package org.formation.spring.formateur.authentication;

import org.formation.model.FormateurUser;
import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {
	 
	private AgifManageService service = new AgifManageServiceImpl();

	/**
	 * appelée lors d'une tentative de log
	 * permet de renvoyer l'utilisateur, son mot de passe et ses rôles si il existe dans la base
	 * il est alors chargé par spring security comme Authentication dans le SecurityContextHolder
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    FormateurUser user = this.findUserbyUsername(username);

	    UserBuilder builder = null;
	    if (user != null) {
	      builder = org.springframework.security.core.userdetails.User.withUsername(username);
	      builder.password(user.getPassword());
	      builder.roles(user.getRoles());
	      if (service.estCompteFormateurUserBloque(username))
	    	  builder.accountLocked(true);
	    } else {
	      throw new UsernameNotFoundException("User not found.");
	    }

	    return builder.build();
	  }

	/**
	 * vérifie si l'utilisateur existe dans la BD, si oui il est chargé dans le User
	 * avec ses mot de passe et rôles
	 * @param username
	 * @return
	 */
	  private FormateurUser findUserbyUsername(String username) {
		  // appel ici à la couche DAO
		  // ne récupérer que les formateurs de "type winibw"
	    //if(username.equalsIgnoreCase("")) {
//	      return new FormateurUser("@abes.fr", "", "");
	    //}
	   // return null;
	    
		  // reste à bloquer si pas winibw sudoc
	    return service.getFormateurUserByEmail(username);
	  }
}
