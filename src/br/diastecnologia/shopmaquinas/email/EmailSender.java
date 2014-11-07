package br.diastecnologia.shopmaquinas.email;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import br.diastecnologia.shopmaquinas.bean.Person;

@Named("EmailSender")
public class EmailSender {

	@Inject
	@Named("SenderConfiguration")
	private SenderConfiguration senderConfiguration;
	
	public void SendEmail( String subject, String htmlBody, String textBody, Person person , Person fromPerson) throws EmailException{
		try{
			
			HtmlEmail email = new HtmlEmail();
			email.setHostName(senderConfiguration.getSmtp());
			email.setSmtpPort(new Integer(senderConfiguration.getPort()));
			if( new Boolean(senderConfiguration.getAuthentication())){
				email.setAuthenticator(new DefaultAuthenticator(senderConfiguration.getUsername(), senderConfiguration.getPassword()));
			}
			email.setSSLOnConnect(new Boolean( senderConfiguration.getSsl() ) );
			email.setFrom(senderConfiguration.getUsername(), senderConfiguration.getName());
			email.addCc(fromPerson.getEmail());
			
			if( senderConfiguration.getCc() != null ){
				String[] emails = senderConfiguration.getCc().split(";");
				for( String emailAddress : emails ){
					email.addBcc(emailAddress);
				}
			}
			
			email.setSubject(subject);
			email.setHtmlMsg(htmlBody);
			email.setTextMsg(textBody);
			email.addTo(person.getEmail(), person.getFirstName() + " " + person.getLastname() );
			email.send();
			
		}catch(Exception ex){
			throw new EmailException("Problema ao enviar e-mail: " + ex.getMessage() , ex);
		}
	}
}
