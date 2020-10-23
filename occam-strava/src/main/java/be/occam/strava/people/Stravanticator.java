package be.occam.strava.people;

import static be.occam.utils.javax.Utils.map;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import be.occam.strava.dto.StravaTokenDTO;
import be.occam.strava.exception.NotAuthenticatedException;
import be.occam.strava.object.Token;
import be.occam.strava.repository.TokenEntity;
import be.occam.strava.repository.TokenRepository;
import be.occam.utils.spring.web.Client;

/**
 * Manages Strava Oauth2 tokens
 * 
 * Authorization (getting code via consent page) is done from the front-end.
 * 
 * @author u0060396
 *
 */
public class Stravanticator {

	protected final Logger logger
		= LoggerFactory.getLogger( Stravanticator.class );

	protected final String
		authorizationEndpoint = "";

	protected final String
		accessTokenEndpoint = "https://www.strava.com/oauth/token";
	
	protected final static String GRANT_TYPE_CODE
		= "authorization_code";
	
	protected final static String GRANT_TYPE_REFRESH
		= "refresh_token";

	protected final String
		responseType = "code";
	
	@Resource
	TokenRepository tokenRepository;

	protected final String clientID;
	protected final String clientSecret;
	protected final String redirectURI;

	public Stravanticator( String clientID, String clientSecret, String redirectURI ) {
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.redirectURI = redirectURI;
	}
	
	public void redirectURI( Token token ) {
		token.setRedirectURI( this.redirectURI );
	}
	
	public Token findToken( String riderID ) {
		
		TokenEntity existing
			= this.tokenRepository.findOneByRiderID( riderID );
		
		if ( existing == null ) {
			throw new NotAuthenticatedException();
		}
		
		return Token.from( existing );
	}
	
	/**
	 * return valid token which can be used for calling the various API's.
	 * @param riderID
	 * @return
	 */
	public Token token( String riderID ) {
		
		Token token
			= null;
		
		TokenEntity existing
			= this.tokenRepository.findOneByRiderID( riderID );
		
		if ( existing == null ) {
			logger.info( "[{}]; no token yet, create new one", riderID );
			token = new Token();
			token.setId( riderID );
			token.setRiderID( riderID );
			this.redirectURI( token );
		}
		else {
			token = Token.from( existing );
			this.redirectURI( token );
		}
		
		this.accessToken(token);
		
		TokenEntity updated
			= Token.entity( token );
		
		this.tokenRepository.save( updated );
		
		return token;
		
	}
	
	public Token updateCode( String riderID, String newCode ) {
		
		Token token
			= null;
		
		TokenEntity entity
			= this.tokenRepository.findOneByRiderID( riderID );
		
		if ( entity == null ) {
			logger.info( "[{}]; no token yet, create new one", riderID );
			token = new Token();
			token.setId( riderID );
			token.setRiderID( riderID );
			entity = Token.entity( token );
		}
		
		logger.info( "[{}]; update code to [{}]", riderID, newCode  );
		entity.setCode( newCode );
		entity = this.tokenRepository.save( entity );
		
		return Token.from( entity );
		
	}
	
	protected void accessToken( Token token ) {
	
		try {
			
			String currentAccessToken
				= token.getAccessToken();
			
			if ( currentAccessToken == null ) {
				
				StravaTokenDTO stravaToken
					= requestAccessTokenFromCode( token );
				
				token.setAccessToken( stravaToken.getAccessToken() );
				token.setRefreshToken( stravaToken.getRefreshToken() );
				Date date = new Date();
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime( date );
				calendar.add( Calendar.SECOND, stravaToken.getExpires() );
				token.setAcessTokenExpiration( calendar.getTime() );
			}
			else {
				
				// refresh access token
				StravaTokenDTO stravaToken
					= requestAccessTokenFromRefreshToken( token );
			
				String newAccessToken
					= stravaToken.getAccessToken();
				
				logger.info( "received new access token [{}]", newAccessToken );
				token.setAccessToken( newAccessToken );
				
				Date date = new Date();
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime( date );
				calendar.add( Calendar.SECOND, stravaToken.getExpires() );
				token.setAcessTokenExpiration( calendar.getTime() );
				
				String newRefreshToken
					= stravaToken.getRefreshToken();
				
				if ( newRefreshToken != null ) {
					logger.info( "received new refresh token [{}]", newRefreshToken );
					token.setRefreshToken( newRefreshToken );
				}
				
			}
			
		}
		catch ( HttpClientErrorException e ) {
			
			logger.warn( "oauth http request failed: {}", e.getResponseBodyAsString() );
			
		}
		catch ( Exception e ) {
			
			logger.warn( "oath failed", e );
			
		}
		
	}
	
	protected StravaTokenDTO requestAccessTokenFromCode( Token token ) {
		
		StravaTokenDTO stravaToken
			= null;
		
		try {
			
			String code = token.getCode();
		
			logger.info( "[{}], no access token yet, get one with code [{}]", token.getRiderID(), code );
		
			logger.debug( "using client id [{}]", this.clientID );
		
			Map<String,String> headers
				= map();	
		
			/*
			StringBuilder b
				= new StringBuilder();
				
			b.append( this.clientID );
			b.append( ":" );
			b.append( this.clientSecret );
			
			String authorizationValue
				= Base64.encodeBase64String( b.toString().getBytes( "utf-8") );
			// logger.info( "Basic value is [{}]", authorizationValue );
			// headers.put( "Authorization", String.format( "Basic %s", authorizationValue ) );
			*/
		
			MultiValueMap<String, Object> fields
				= new LinkedMultiValueMap<String, Object>();
		
			fields.add( "grant_type", GRANT_TYPE_CODE );
			fields.add( "code", code );
			fields.add( "redirect_uri", this.redirectURI );
			fields.add( "client_id", this.clientID );
			fields.add( "client_secret", this.clientSecret );
		
			ResponseEntity<StravaTokenDTO> tokenResponse 
				= Client.postFormURLEncoded( this.accessTokenEndpoint, StravaTokenDTO.class, fields, headers );
		
			if ( tokenResponse.getStatusCode().equals( HttpStatus.OK ) ) {
				stravaToken = tokenResponse.getBody();
				String accessToken = stravaToken.getAccessToken();
				String refreshToken = stravaToken.getRefreshToken();
				logger.info( "received access token = [{}], refresh token [{}]", accessToken, refreshToken );
			}
			else {
				logger.info( "failed to get strava access token, response status code is [{}]", tokenResponse.getStatusCode() );
			}
			
			return stravaToken;
		}
		catch( HttpClientErrorException e ) {
			
			logger.info( "failed to get strava access token, response status is [{}], response body is [{}]", e.getStatusCode().value(), e.getResponseBodyAsString() );
			throw new RuntimeException( e );
		}
		catch( HttpServerErrorException e ) {
			throw new RuntimeException( e );
		}
		catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
		
	protected StravaTokenDTO requestAccessTokenFromRefreshToken( Token token ) {
		
		StravaTokenDTO stravaToken
			= null;
		
		try {
			
			String currentRefreshToken = token.getRefreshToken();
			
			if ( currentRefreshToken == null ) {
				throw new RuntimeException( "no refresh token in database");
			}
		
			logger.info( "[{}], access token expired, get one with refresh token [{}]", token.getRiderID(), currentRefreshToken );
		
			logger.debug( "using client id [{}]", this.clientID );
		
			Map<String,String> headers
				= map();	
		
			/*
			StringBuilder b
				= new StringBuilder();
				
			b.append( this.clientID );
			b.append( ":" );
			b.append( this.clientSecret );
			
			String authorizationValue
				= Base64.encodeBase64String( b.toString().getBytes( "utf-8") );
			// logger.info( "Basic value is [{}]", authorizationValue );
			// headers.put( "Authorization", String.format( "Basic %s", authorizationValue ) );
			*/
		
			MultiValueMap<String, Object> fields
				= new LinkedMultiValueMap<String, Object>();
		
			fields.add( "grant_type", GRANT_TYPE_REFRESH );
			fields.add( "refresh_token", currentRefreshToken );
			fields.add( "client_id", this.clientID );
			fields.add( "client_secret", this.clientSecret );
		
			ResponseEntity<StravaTokenDTO> tokenResponse 
				= Client.postFormURLEncoded( this.accessTokenEndpoint, StravaTokenDTO.class, fields, headers );
		
			if ( tokenResponse.getStatusCode().equals( HttpStatus.OK ) ) {
				stravaToken = tokenResponse.getBody();
				String accessToken = stravaToken.getAccessToken();
				String refreshToken = stravaToken.getRefreshToken();
				logger.info( "received access token = [{}], refresh token [{}]", accessToken, refreshToken );
			}
			else {
				logger.info( "failed to get strava access token, response status code is [{}]", tokenResponse.getStatusCode() );
				throw new RuntimeException( "could not get access token via refresh token");
			}
			
			return stravaToken;
		} catch( HttpClientErrorException e ) {
			
			logger.info( "failed to get strava access token, response status is [{}], response body is [{}]", e.getStatusCode().value(), e.getResponseBodyAsString() );
			throw new RuntimeException( e );
		} catch( HttpServerErrorException e ) {
			throw new RuntimeException( e );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
		
	}

	public void setTokenRepository(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
}
