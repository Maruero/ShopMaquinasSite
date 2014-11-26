package br.diastecnologia.shopmaquinas.bean;

import br.diastecnologia.shopmaquinas.enums.JsonResponseCode;

public class JsonResponse {

	private String code;
	private String message;
	private Object data;
	
	public JsonResponse(String message ){
		this.message = message;
		this.code = JsonResponseCode.OK.toString();
	}
	
	public JsonResponse(String message, Object data ){
		this.code = JsonResponseCode.OK.toString();
		this.data = data;
	}
	
	public JsonResponse(JsonResponseCode code, String message ){
		this(message);
		this.code = code.toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
