package be.occam.strava.repository;

public class RiderEntity {

	protected String id;
	protected String riderID;
	//@Column(name="EMAIL")
	//protected String email;
	protected String teamID;
	protected String givenName;
	protected String familyName;
	protected String stravaID;
	
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
		return stravaID;
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
		// return email;
		return "";
	}

	public void setEmail(String email) {
		//this.email = email;
	}
	
}
