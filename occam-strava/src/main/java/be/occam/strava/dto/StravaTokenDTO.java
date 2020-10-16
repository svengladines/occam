package be.occam.strava.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StravaTokenDTO {

	@JsonProperty(value="access_token")
	protected String accessToken;
	
	@JsonProperty(value="refresh_token")
	protected String refreshToken;
	
	@JsonProperty(value="expires")
	protected int expires;
	
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

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}
	
	

}
