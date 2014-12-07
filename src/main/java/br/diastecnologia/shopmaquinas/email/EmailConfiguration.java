package br.diastecnologia.shopmaquinas.email;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.environment.Property;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.Person;

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
	
	public String getProposalText( Person from, Person to, Ad ad, String text ){
		StringBuilder builder = new StringBuilder();
		
		builder.append(proposalTitle);
		builder.append("Dados do interessado <br/>");
		builder.append("Nome: " + from.getFirstname() + "<br/>");
		builder.append("E-mail: " + from.getEmail() + "<br/>");
		builder.append("Telefone: " + from.getPhone() + "<br/><br/>");
		
		builder.append("Dados do anunciante <br/>" );
		builder.append("Nome: " + to.getFirstname() + " " + to.getLastname() + "<br/>");
		builder.append("E-mail: " + to.getEmail() + "<br/>");
		builder.append("Telefone: " + to.getPhone() + "<br/><br/>");
		
		builder.append("Produto de interesse: <b>" + ad.getDescription() + "</b><br/><br/>");
		builder.append("Descrição: " + text);
		
		return builder.toString();
	}

	public String getProposalSubject() {
		return proposalSubject;
	}

	public void setProposalSubject(String proposalSubject) {
		this.proposalSubject = proposalSubject;
	}
	
	
	
}
