package org.formation.spring.formateur.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.formation.service.AgifManageService;
import org.formation.service.impl.AgifManageServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private AgifManageService service = new AgifManageServiceImpl();
	
    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent e) {
        
    	Logger.getLogger(AuthenticationFailureListener.class.getName()).log(Level.SEVERE,
      		  "connexion OK pour " + e.getAuthentication().getName());
    	  	service.initZeroFormateurUserNbTentatives(e.getAuthentication().getName());
    }

}
