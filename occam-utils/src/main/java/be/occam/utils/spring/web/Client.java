package be.occam.utils.spring.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
	
	protected static final Logger logger
		= LoggerFactory.getLogger( Client.class );
	
	public static RestTemplate restTemplate() {
		
		RestTemplate template = new RestTemplate();
		
		return template;
		 
	}
	
	public static <T> ResponseEntity<T> getJSON( String url, Class<T> t, String... variables ) {
		
		logger.debug( "GET request for JSON data at url [{}]", url );
		
		Object c = new Object();
		
		// Class<T> x = (Class<T>) c.getClass();
		
		HttpHeaders headers 
			= new HttpHeaders();
		
	    headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
	    
	    HttpEntity<T> entity 
	    	= new HttpEntity<T>( headers );
		
		ResponseEntity<T> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.GET,
				entity,
				t,
				(Object[]) variables );
		
		return getResponse;
		
	}
	
	public static <T> ResponseEntity<T> getJSON( String url, Class<T> t, Map<String,String> headers, String... variables ) {
		
		logger.debug( "GET request for JSON data at url [{}]", url );
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
	    headrs.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
	    
	    for ( String header : headers.keySet() ) {
	    	headrs.set( header , headers.get( header ) );
	    }
	    
	    HttpEntity<T> entity 
	    	= new HttpEntity<T>( headrs );
		
		ResponseEntity<T> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.GET,
				entity,
				t,
				(Object[]) variables );
		
		return getResponse;
		
	}
	
	public static ResponseEntity<byte[]> getBytes( String url, Map<String,String> headers, String... variables ) {
		
		logger.debug( "GET request for byte data at url [{}]", url );
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
	    headrs.set( "Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE );

	    if ( headers != null ) {
		    for ( String header : headers.keySet() ) {
		    	headrs.set( header , headers.get( header ) );
		    }
	    }
	    
	    HttpEntity<byte[]> entity 
	    	= new HttpEntity<byte[]>( headrs );
		
		ResponseEntity<byte[]> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.GET,
				entity,
				byte[].class,
				(Object[]) variables );
		
		return getResponse;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> postJSON( String url, T t, String... variables ) {
		
		return postJSON( url, t, null, variables );
		
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> postJSON( String url, T t,  Map<String,String> headers, String... variables ) {
		
		logger.debug( "POST request with JSON data at url [{}]", url );
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
	    headrs.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
	    
	    if ( headers != null ) {
	    	
	    	 for ( String header : headers.keySet() ) {
	 	    	headrs.set( header , headers.get( header ) );
	 	    }
	    	
	    }
	    
	    HttpEntity<T> entity 
	    	= new HttpEntity<T>( t, headrs );
		
		ResponseEntity<T> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.POST,
				entity,
				(Class<T>) t.getClass(),
				(Object[]) variables );
		
		return getResponse;
		
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> putJSON( String url, T t,  Map<String,String> headers, String... variables ) {
		
		logger.debug( "PUT request with JSON data at url [{}]", url );
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
	    headrs.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
	    
	    if ( headers != null ) {
	    	
	    	 for ( String header : headers.keySet() ) {
	 	    	headrs.set( header , headers.get( header ) );
	 	    }
	    	
	    }
	    
		headrs.set( "Content-Type", MediaType.APPLICATION_JSON_VALUE );
	    
	    HttpEntity<T> entity 
	    	= new HttpEntity<T>( t, headrs );
		
		ResponseEntity<T> response 
			= restTemplate().exchange(
				url,
				HttpMethod.PUT,
				entity,
				(Class<T>) t.getClass(),
				(Object[]) variables );
		
		return response;
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> putJSON( String url, T t, String... variables ) {
		
		logger.debug( "PUT request with JSON data at url [{}]", url );
		
		HttpHeaders headers 
			= new HttpHeaders();
		
	    headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
	    
	    HttpEntity<T> entity 
	    	= new HttpEntity<T>( t, headers );
		
		ResponseEntity<T> response 
			= restTemplate().exchange(
				url,
				HttpMethod.PUT,
				entity,
				(Class<T>) t.getClass(),
				(Object[]) variables );
		
		return response;
		
	} 
	
	@SuppressWarnings("unchecked")
	public static ResponseEntity<String> putJSON( String url, Object t, MediaType mediaType, String... variables ) {
		
		logger.debug( "POST request with JSON data at url [{}]", url );
		
		HttpHeaders headers 
			= new HttpHeaders();
		
	    headers.set( "Accept", mediaType.toString() );
	    
	    HttpEntity<Object> entity 
	    	= new HttpEntity<Object>( t, headers );
		
		ResponseEntity<String> response 
			= restTemplate().exchange(
				url,
				HttpMethod.PUT,
				entity,
				String.class,
				(Object[]) variables );
		
		return response;
		
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> postMultiPart( String url, Class<T> type, MultiValueMap<String, Object> parts, String... variables ) {
		
		logger.debug( "POST request with multipart data at url [{}]", url );
		
		HttpHeaders headers 
			= new HttpHeaders();
		
		headers.set( "Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE );
	    headers.set( "Accept", MediaType.ALL_VALUE );
	    
	    @SuppressWarnings("rawtypes")
		HttpEntity<MultiValueMap> entity 
	    	= new HttpEntity<MultiValueMap>( parts );
		
		ResponseEntity<T> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.POST,
				entity,
				(Class<T>) type,
				(Object[]) variables );
		
		return getResponse;
		
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> postMultiPart( String url, Class<T> type, MultiValueMap<String, Object> parts, Map<String, String> headers ) {
		
		logger.debug( "POST request with multipart data at url [{}]", url );
		
		HttpHeaders hders 
			= new HttpHeaders();
		
		hders.set( "Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE );
		hders.set( "Accept", MediaType.ALL_VALUE );
		
		  
	    if ( headers != null ) {
	    	
	    	 for ( String header : headers.keySet() ) {
	    		 hders.set( header , headers.get( header ) );
	 	    }
	    	
	    }
	    
	    @SuppressWarnings("rawtypes")
		HttpEntity<MultiValueMap> entity 
	    	= new HttpEntity<MultiValueMap>( parts, hders );
		
		ResponseEntity<T> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.POST,
				entity,
				(Class<T>) type );
		
		return getResponse;
		
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> postFormUrlEncoded( String url, Class<T> type, MultiValueMap<String, Object> parts, Map<String, String> headers ) {
		
		logger.debug( "POST request with multipart data at url [{}]", url );
		
		HttpHeaders hders 
			= new HttpHeaders();
		
		hders.set( "Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE );
		  
	    if ( headers != null ) {
	    	
	    	 for ( String header : headers.keySet() ) {
	    		 hders.set( header , headers.get( header ) );
	 	    }
	    	
	    }
	    
	    MultiValueMap<String, Object> encoded
	    	= new LinkedMultiValueMap<String,Object>();
	    
	    try {
	    
		    for ( String key : parts.keySet() ) {
		    	
		    	String value 
		    	 	= (String) parts.getFirst( key );
		    	
		    	Object encodedValue
		    		= URLEncoder.encode( value , "UTF-8" );
		    		
		    	encoded.put( key ,Arrays.asList( encodedValue ) );
		    	
		    }
	    }
	    catch ( UnsupportedEncodingException e ) {
	    	throw new RuntimeException( e );
	    }
	    
	    @SuppressWarnings("rawtypes")
		HttpEntity<MultiValueMap> entity 
	    	= new HttpEntity<MultiValueMap>( parts, hders );
	    
		ResponseEntity<T> response 
			= restTemplate().exchange(
				url,
				HttpMethod.POST,
				entity,
				(Class<T>) type );
		
		return response;
		
	} 
	
	public static ResponseEntity<String> getHTML( String url, Map<String,String> headers, String... variables ) {
		
		logger.debug( "GET request for HTML data at url [{}]", url );
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
		headrs.set( "Accept", MediaType.TEXT_HTML_VALUE );
	    
	    if ( headers != null ) {
		    for ( String header : headers.keySet() ) {
		    	headrs.set( header , headers.get( header ) );
		    }
	    }
	    
	    HttpEntity<String> entity 
	    	= new HttpEntity<String>( headrs );
	    
		ResponseEntity<String> getResponse 
			= restTemplate().exchange(
				url,
				HttpMethod.GET,
				entity,
				String.class,
				(Object[]) variables );
		
		return getResponse;
		
	} 
	
	public static ResponseEntity<String> getHTML( String url, String... variables ) {
		
		return getHTML( url, null, variables );
		
	} 
	
	public static Map<String,String> headers() {
		
		return new HashMap<String,String>();
		
	}
	
}
