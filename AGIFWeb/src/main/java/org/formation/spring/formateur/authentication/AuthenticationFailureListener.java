package org.formation.spring.formateur.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
   
	private AgifManageService service = new AgifManageServiceImpl();
	
    @Override
    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent e) {
        
  	  	Logger.getLogger(AuthenticationFailureListener.class.getName()).log(Level.SEVERE,
    		  "mauvaise connexion pour " + e.getAuthentication().getName());
  	  	service.incrementeFormateurUserNbTentatives(e.getAuthentication().getName());
    }

}
