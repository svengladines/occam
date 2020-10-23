package be.occam.strava.repository;

import java.util.Date;

public class RideEntity {

	protected Long id;
	protected String riderID;
	protected String name;
	protected String teamID;
	protected String stravaID;
	protected Float distance;
	protected Date moment;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRiderID() {
		return riderID;
	}
	public void setRiderID(String riderID) {
		this.riderID = riderID;
	}
	public String getTeamID() {
		return teamID;
	}
	public void setTeamID(String teamID) {
		this.teamID = teamID;
	}
	public String getStravaID() {
		return stravaID;
	}
	public void setStravaID(String stravaID) {
		this.stravaID = stravaID;
	}
	public Float getDistance() {
		return distance;
	}
	public void setDistance(Float distance) {
		this.distance = distance;
	}
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
