package br.diastecnologia.shopmaquinas.email;

import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.vraptor.environment.Property;

@Named("SenderConfiguration")
public class SenderConfiguration {

	@Inject
	@Property("email.smtp")
	private String smtp;
	
	@Inject
	@Property("email.port")
	private String port;
	
	@Inject
	@Property("email.name")
	private String name;
	
	@Inject
	@Property("email.username")
	private String username;
	
	@Inject
	@Property("email.password")
	private String password;
	
	@Inject
	@Property("email.authentication")
	private String authentication;
	
	@Inject
	@Property("email.ssl")
	private String ssl;
	
	@Inject
	@Property("email.cc")
	private String cc;

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
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

	public String isSsl() {
		return ssl;
	}

	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public String getSsl() {
		return ssl;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}
	
}
