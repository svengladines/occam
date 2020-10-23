package be.occam.strava.dto;

import java.util.Date;

public class TokenDTO {

	protected String id;
	protected String staffNumber;
	protected String code;
	protected String accessToken;
	protected String refreshToken;
	protected String redirectURI;
	
	protected Date acessTokenExpiration;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStaffNumber() {
		return this.staffNumber;
	}
	
	public void setStaffNumber(String staffNumber) {
		this.staffNumber = staffNumber;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getAcessTokenExpiration() {
		return acessTokenExpiration;
	}

	public void setAcessTokenExpiration(Date acessTokenExpiration) {
		this.acessTokenExpiration = acessTokenExpiration;
	}
	
	public String getRedirectURI() {
		return redirectURI;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}
	
}
