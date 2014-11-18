package br.diastecnologia.shopmaquinas.session;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.diastecnologia.shopmaquinas.bean.User;

@SessionScoped
@Named("session")
public class SessionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private User user;
	private List<String> uploadedImages;
	private Object toUseAfterRedirect;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getUploadedImages() {
		return uploadedImages;
	}

	public void setUploadedImages(List<String> uploadedImages) {
		this.uploadedImages = uploadedImages;
	}

	public boolean redirectObjectIs( Class<?> clazz ){
		if( toUseAfterRedirect != null && toUseAfterRedirect.getClass().isAssignableFrom(clazz)){
			return true;
		}else{
			return false;
		}
	}
	
	public Object getRedirectObject() {
		Object obj = toUseAfterRedirect;
		toUseAfterRedirect = null;
		return obj;
	}

	public void setRedirectObject(Object toUseAfterRedirect) {
		this.toUseAfterRedirect = toUseAfterRedirect;
	}

}
