package br.diastecnologia.shopmaquinas.email;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.environment.Property;
import br.diastecnologia.shopmaquinas.bean.Ad;
import br.diastecnologia.shopmaquinas.bean.Person;
import br.diastecnologia.shopmaquinas.utils.CurrencyUtils;

@Named("EmailConfiguration")
public class EmailConfiguration {
	
	@Inject
	@Property("email.proposal.text")
	private String proposalText;
	
	@Inject
	@Property("email.proposal.title")
	private String proposalTitle;
	
	
	public String getProposalText( Person from, Person to, Ad ad, double price, String text ){
		StringBuilder builder = new StringBuilder();
		
		builder.append(proposalTitle);
		builder.append("Dados do interessado <br/>");
		builder.append("Nome: " + from.getFirstName() + " " + from.getLastname() + "<br/>");
		builder.append("E-mail: " + from.getEmail() + "<br/>");
		builder.append("Telefone: " + from.getPhone() + "<br/><br/>");
		
		builder.append("Dados do anunciante <br/>" );
		builder.append("Nome: " + from.getFirstName() + " " + from.getLastname() + "<br/>");
		builder.append("E-mail: " + from.getEmail() + "<br/>");
		builder.append("Telefone: " + from.getPhone() + "<br/><br/>");
		
		builder.append("Produto de interesse: <p>" + ad.getDescription() + "</p><br/><br/>");
		builder.append("Valor da proposta: " + CurrencyUtils.toString(price) + "<br/>");
		builder.append("Descrição: " + text);
		
		return builder.toString();
	}
	
	
	
}
