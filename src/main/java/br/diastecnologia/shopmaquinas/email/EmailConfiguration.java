package br.diastecnologia.shopmaquinas.email;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.environment.Property;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.bean.User;

@Named("EmailConfiguration")
public class EmailConfiguration {
	
	@Inject
	@Property("email.proposal.text")
	private String proposalText;
	
	@Inject
	@Property("email.proposal.title")
	private String proposalTitle;
	
	@Inject
	@Property("email.proposal.subject")
	private String proposalSubject;
	
	@Inject
	@Property("email.path")
	private String emailPath;
	
	public String getRegisterHtml( User user , String token ){
		try{
			String body = getRegisterHtml();
			
			body = body.replace("[NAME]", user.getPerson().getFirstname() + " " + user.getPerson().getLastname());
			body = body.replace("[DOCUMENT]", user.getUsername());
			body = body.replace("[PASSWORD]", user.getPassword());
			
			body = body.replace("[LINK_PATH]", "http://www.shopmaquinas.com.br/acesso-direto?token=" + token);
			
			return body;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	public String getProposalText( Person from, Person to, Ad ad, String text ){
		try{
			String body = getProposalHtml();
			
			String fromName = from.getFirstname();
			if( from.getLastname() != null ){
				fromName += " " + from.getLastname(); 
			}
			body = body.replace("[FROM_NAME]", fromName);
			body = body.replace("[FROM_EMAIL]", from.getEmail());
			body = body.replace("[FROM_PHONE]", from.getPhone());
			
			body = body.replace("[AD_DESCRIPTION]", ad.getDescription());
			body = body.replace("[PROPOSAL_TEXT]", text);
			
			body = body.replace("[TO_NAME]", ad.getPerson().getFirstname());
			
			body = body.replace("[LINK_PATH]", "http://www.shopmaquinas.com.br/anuncios/detalhes-do-anuncio/" + ad.getAdID());
			
			return body;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	private String getRegisterHtml() throws IOException{
		String path = emailPath + "cadastro.html?data=" + Calendar.getInstance().getTimeInMillis();
		return retrieve(path);
	}
	
	private String getProposalHtml() throws IOException{
		String path = emailPath + "proposta.html?data=" + Calendar.getInstance().getTimeInMillis();
		return retrieve(path);
	}
	
	private String retrieve( String path ) throws IOException{
		URL url = new URL( path );
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		
		byte[] buffer = new byte[1024 * 1024];
		String content = "";
		int read = 0;
		while( (read = stream.read(buffer, 0, buffer.length) ) != -1){
			content += new String( buffer, 0 , read);
		}
		return content;
	}
	
	public String getProposalSubject() {
		return proposalSubject;
	}

	public void setProposalSubject(String proposalSubject) {
		this.proposalSubject = proposalSubject;
	}
	
	
	
}
