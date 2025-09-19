package org.formation.spring.formateur.resetpassword;

import java.util.Properties;
import org.formation.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	

	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	
	    mailSender.setHost(Constant.MAIL_SMTP_HOST);
	    mailSender.setPort(Integer.parseInt(Constant.MAIL_SMTP_PORT));
	    mailSender.setUsername(Constant.MAIL_USER);
	    mailSender.setPassword(Constant.MAIL_PASSWORD);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
}
