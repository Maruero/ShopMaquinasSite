package br.diastecnologia.shopmaquinas.bean;

import java.util.Date;

public class MessageDTO {

	private String text;
	private Date date;
	private String senderName;
	private String senderEmail;
	private String senderPhone;
	
	private String adDescription;
	
	public MessageDTO(){
		
	}
	
	public MessageDTO( Message message ){
		
		String name = "";
		if( message.getFromPerson().getFirstname() != null ){
			name += message.getFromPerson().getFirstname();
		}
		if( message.getFromPerson().getLastname() != null ){
			name += " " + message.getFromPerson().getLastname();
		}
		
		date = message.getDate();
		text = message.getText();
		senderName = name;
		senderEmail = message.getFromPerson().getEmail();
		senderPhone = message.getFromPerson().getPhone();
		adDescription = message.getAd().getDescription();
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getAdDescription() {
		return adDescription;
	}

	public void setAdDescription(String adDescription) {
		this.adDescription = adDescription;
	}
	
	
	
}
