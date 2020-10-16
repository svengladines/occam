package be.occam.strava.repository;

public interface TokenRepository {
	
	public TokenEntity findOneByRiderID( String riderID );
	public TokenEntity save(TokenEntity entity);

}
