package be.occam.utils.spring.web;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultController {
	
	public static <T> ResponseEntity<Result<T>> response( T t, HttpStatus status, Map<String, String> headers ) {
		
		HttpHeaders headrs 
			= new HttpHeaders();
	
		headrs.add("Access-Control-Allow-Origin", "*" ) ;
		headrs.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS" );
		headrs.add("Access-Control-Allow-Headers", "Content-Type");
		headrs.add("Access-Control-Allow-Credentials","true");
		
		if ( headers != null ) {
			
			for ( String key : headers.keySet() ) {
				headrs.add( key, headers.get( key ) );
			}
			
		}
		
		ResponseEntity<Result<T>> response;
		
		Result<T> r
			= new Result<T>();
		r.setObject( t );
		
		if ( t != null ) {
			 response 
				= new ResponseEntity<Result<T>>( r, headrs, status );
		}
		else {
			response 
				= new ResponseEntity<Result<T>>( headrs, status );
		}
		
		return response;
		
	}
	
	public static <T> ResponseEntity<Result<T>> response( T t, HttpStatus status ) {
	
		return response( t, status, null );
		
	}
	
	public static <T> ResponseEntity<Result<T>> response( HttpStatus status ) {
		
		return response( null, status );
	
	}

}
