package br.diastecnologia.shopmaquinas.email;

public class EmailException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmailException(){}
	
	public EmailException(String message){
		super(message);
	}
	
	public EmailException(String message, Throwable inner ){
		super(message, inner);
	}
	
}
