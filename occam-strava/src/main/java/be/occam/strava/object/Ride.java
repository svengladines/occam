package be.occam.strava.object;

import java.util.Date;

import be.occam.strava.dto.RideDTO;
import be.occam.strava.repository.RideEntity;

public class Ride {

	protected Long id;
	protected String name;
	protected String riderID;
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
	
	public static RideDTO dto( Ride from ) {
		
		RideDTO to
			= new RideDTO();
		
		to.setId( from.getId() );
		to.setName( from.getName() );
		to.setRiderID( from.getRiderID() );
		to.setStravaID( from.getStravaID() );
		
		
		
		to.setMoment( from.getMoment() );
		to.setTeamID( from.getTeamID() );
		
		return to;
		
	}
	
	public static Ride from( RideDTO from ) {
		
		Ride to
			= new Ride();
		
		to.setId( from.getId() );
		to.setName( from.getName() );
		to.setRiderID( from.getRiderID() );
		
		to.setDistance( from.getDistance() );
		to.setMoment( from.getMoment() );
		to.setTeamID( from.getTeamID() );
		
		return to;
		
	}
	
	public static RideEntity entity( Ride from ) {
		
		RideEntity to
			= new RideEntity();
		
		to.setId( from.getId() );
		to.setName( from.getName() );
		to.setRiderID( from.getRiderID() );
		to.setStravaID( from.getStravaID() );
		
		to.setMoment( from.getMoment() );
		to.setTeamID( from.getTeamID() );
		
		return to;
		
	}
	
	public static Ride from( RideEntity from ) {
		
		Ride to
			= new Ride();
		
		to.setId( from.getId() );
		to.setName( from.getName() );
		to.setRiderID( from.getRiderID() );
		
		to.setDistance( from.getDistance() );
		to.setMoment( from.getMoment() );
		to.setTeamID( from.getTeamID() );
		
		return to;
		
	}
	
}
