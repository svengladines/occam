package be.occam.strava.object;

import be.occam.strava.dto.RiderDTO;
import be.occam.strava.repository.RiderEntity;

public class Rider {
	
	protected String id;
	protected String teamID;
	protected String givenName;
	protected String familyName;
	protected String email;
	
	protected String riderID;
	protected String stravaID;
	
	protected Token token;
	
	public String getTeamID() {
		return teamID;
	}
	
	public void setTeamID(String teamID) {
		this.teamID = teamID;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public String getRiderID() {
		return riderID;
	}
	
	public void setRiderID(String riderID) {
		this.riderID = riderID;
	}
	
	public String getStravaID() {
		return this.stravaID;
	}
	
	public void setStravaID(String stravaID) {
		this.stravaID = stravaID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public static Rider from( RiderDTO from ) {
		
		Rider to
			= new Rider();
		
		to.setId( from.getId() );
		to.setGivenName( from.getGivenName() );
		to.setFamilyName( from.getFamilyName() );
		to.setRiderID( from.getRiderID() );
		to.setStravaID( from.getStravaID() );
		to.setTeamID( from.getTeamID() );
		to.setEmail( from.getEmail() );
		// refreshtoken not relevant for dto
		
		return to;
		
	}
	
	public static Rider from( RiderEntity from ) {
		
		Rider to
			= new Rider();
		
		to.setId( from.getId() );
		to.setGivenName( from.getGivenName() );
		to.setFamilyName( from.getFamilyName() );
		to.setRiderID( from.getRiderID() );
		to.setStravaID( from.getStravaID() );
		to.setTeamID( from.getTeamID() );
		to.setEmail( from.getEmail() );
		
		return to;
		
	}

	public static RiderDTO dto( Rider from ) {
		
		RiderDTO to
			= new RiderDTO();
		
		to.setId( from.getId() );
		to.setGivenName( from.getGivenName() );
		to.setFamilyName( from.getFamilyName() );
		to.setRiderID( from.getRiderID() );
		to.setStravaID( from.getStravaID() );
		to.setTeamID( from.getTeamID() );
		to.setEmail( from.getEmail() );
		
		return to;
		
	}
	
	public static RiderEntity entity( Rider from ) {
		
		RiderEntity to
			= new RiderEntity();
		
		to.setId( from.getId() );
		to.setGivenName( from.getGivenName() );
		to.setFamilyName( from.getFamilyName() );
		to.setRiderID( from.getRiderID() );
		to.setStravaID( from.getStravaID() );
		to.setTeamID( from.getTeamID() );
		to.setEmail( from.getEmail() );
		
		return to;
		
	}

}
