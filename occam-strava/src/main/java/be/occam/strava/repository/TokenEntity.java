package be.occam.strava.repository;

import java.util.Date;

public class TokenEntity {

	protected String id;
	protected String riderID;
	protected String code;
	protected String accessToken;
	protected String refreshToken;
	protected Date accessTokenExpiration;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getRiderID() {
		return this.riderID;
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

	public Date getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public void setAccessTokenExpiration(Date accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}
	
}
