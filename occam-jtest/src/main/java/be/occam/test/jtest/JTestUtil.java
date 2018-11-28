package be.occam.test.jtest;

import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class JTestUtil {
	
	protected static final Logger logger
		= LoggerFactory.getLogger( JTestUtil.class );
	
	public static RestTemplate restTemplate() {
		
		RestTemplate template = new RestTemplate();
		
		for ( HttpMessageConverter< ? > converter : template.getMessageConverters() ) {
			
			if ( converter instanceof MappingJacksonHttpMessageConverter ) {
				
				MappingJacksonHttpMessageConverter jacksonConverter
					= (MappingJacksonHttpMessageConverter) converter;
					
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
				jacksonConverter.setObjectMapper( mapper );
				
			}
			
		}
		
		return template;
		 
	}
	
	public static RestTemplate restTemplateForJSONViaJAXB() {
		
		RestTemplate template = new RestTemplate();
		
		for ( HttpMessageConverter< ? > converter : template.getMessageConverters() ) {
			
			if ( converter instanceof MappingJacksonHttpMessageConverter ) {
				
				MappingJacksonHttpMessageConverter jacksonConverter
					= (MappingJacksonHttpMessageConverter) converter;
					
				ObjectMapper mapper = new ObjectMapper();
				mapper.setAnnotationIntrospector( new JaxbAnnotationIntrospector() );
				jacksonConverter.setObjectMapper( mapper );
				
			}
			
		}
		
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
			= restTemplateForJSONViaJAXB().exchange(
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
			= restTemplateForJSONViaJAXB().exchange(
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
			= restTemplateForJSONViaJAXB().exchange(
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
			= restTemplateForJSONViaJAXB().exchange(
				url,
				HttpMethod.POST,
				entity,
				(Class<T>) t.getClass(),
				(Object[]) variables );
		
		return getResponse;
		
	} 
	
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> putJSON( String url, T t, String... variables ) {
		
		logger.debug( "POST request with JSON data at url [{}]", url );
		
		HttpHeaders headers 
			= new HttpHeaders();
		
	    headers.set( "Accept", MediaType.APPLICATION_JSON_VALUE );
	    
	    HttpEntity<T> entity 
	    	= new HttpEntity<T>( t, headers );
		
		ResponseEntity<T> getResponse 
			= restTemplateForJSONViaJAXB().exchange(
				url,
				HttpMethod.PUT,
				entity,
				(Class<T>) t.getClass(),
				(Object[]) variables );
		
		return getResponse;
		
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
			= restTemplateForJSONViaJAXB().exchange(
				url,
				HttpMethod.POST,
				entity,
				(Class<T>) type,
				(Object[]) variables );
		
		return getResponse;
		
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
	
}
