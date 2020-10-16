package be.occam.strava.object;

import java.util.Date;

import be.occam.strava.repository.TokenEntity;

public class Token {

	protected String id;
	protected String riderID;
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
	
	public String getRiderID() {
		return riderID;
	}

	public void setRiderID(String riderID) {
		this.riderID = riderID;
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

	public static Token from( TokenEntity from ) {
		
		Token to
			= new Token();
		
		to.setId( from.getId() );
		to.setRiderID( from.getRiderID() );
		to.setCode( from.getCode() );
		to.setAccessToken( from.getAccessToken() );
		to.setRefreshToken( from.getRefreshToken() );
		
		return to;
		
	}
	
	public static TokenEntity entity( Token from ) {
		
		TokenEntity to
			= new TokenEntity();
		
		to.setId( from.getId() );
		to.setRiderID( from.getRiderID() );
		to.setCode( from.getCode() );
		to.setAccessToken( from.getAccessToken() );
		to.setRefreshToken( from.getRefreshToken() );
		to.setAccessTokenExpiration( from.getAcessTokenExpiration() );
		return to;
		
	}
	
}
