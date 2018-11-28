package be.occam.utils.spring.web.rest;

import static be.occam.utils.spring.web.Client.putJSON;
import static org.junit.Assert.assertEquals;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import be.occam.utils.spring.web.MyRestTemplate;
import be.occam.utils.spring.web.Client;

public class TestRest {
	
	protected static final Logger logger
		= LoggerFactory.getLogger( TestRest.class ); 
	
	@Test
	public void doesItSmoke() throws Exception {
		
		try {
		
			ResponseEntity<String> file 
			= putJSON( "https://filemanager.one.com/api/webspace/1/drive/debrodders.be/data/x.txt", "xxx" );
		}
		catch( HttpClientErrorException e ) {
			
			logger.info( "http request failed", e );
			
			String body 
				= e.getResponseBodyAsString(); 
			
			logger.info( "response body: [{}]", body );
			
		}
		
	}
	
	@Test
	public void testGet() throws Exception {
		
		ResponseEntity<String> file 
			= putJSON( "https://filemanager.one.com/api/webspace/1/drive/debrodders.be/data/x.txt", "xxx" );
		
	}
	
	@Test
	public void testOneDotCom() throws Exception {
		
		MultiValueMap<String,Object> fields
			= new LinkedMultiValueMap<String,Object>();
		
		try {
			
			// original URL 
			// https://filemanager.one.com/#sven.gladines@telenet.be/debrodders.be/files;
			StringBuilder cookies
				= new StringBuilder("");
			
			Map<String,String> oneHeaders
				= new HashMap<String,String>();
			
			ResponseEntity<String> getResponse
				= null;
		
			oneHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			oneHeaders.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
			oneHeaders.put("Referer", "https://login.one.com/cp/wfm" );
			oneHeaders.put( "Referer","https://filemanager.one.com/");
			
			getResponse = get( "https://login.one.com/cp/wfm", oneHeaders, cookies );
			
			Map<String,String> postHeaders
				= new HashMap<String,String>();
			
			postHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			postHeaders.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
			postHeaders.put("Referer", "https://login.one.com/cp/wfm" );
			postHeaders.put("Origin", "https://login.one.com" );
			
			String url 
		 		= "https://www.one.com/admin/login.do";
			
			/*
			loginTarget:wfm
			loginDomain:true
			displayUsername:sven.gladines@telenet.be
			username:sven.gladines@telenet.be
			targetDomain:
			password1:br0dders.15
			successUrl:https://filemanager.one.com/
			loginUrl:
			errorUrl:https://login.one.com/cp/wfm
			*/
	
			fields.add( "loginTarget", "wfm" );
			fields.add( "loginDomain", "true" );
			fields.add( "username", "sven.gladines@telenet.be" );
			fields.add( "displayUsername", "sven.gladines@telenet.be" );
			fields.add( "password1", "br0dders.15" );
			fields.add( "targetDomain", "debrodders.be" );
			fields.add( "loginUrl", "" );
			//fields.add( "successUrl", "https://filemanager.one.com/" );
			//fields.add( "errorUrl", "https://login.one.com/cp/wfm" );
		
			ResponseEntity<String> postResponse
				= post( url, fields, postHeaders, cookies );
			
			url = postResponse.getHeaders().getLocation().toASCIIString();
			
			Map<String,String> headers
				= new HashMap<String,String>();
		
			headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			
			// should https://www.one.com/admin/ticketredirect.do;jsessionid=3DB34C1D315A5EFA3755D079B17C4B86?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
			// was    https://www.one.com/admin/ticketredirect.do;jsessionid=2255B648DB12F53326C23810C0B9D9B2?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
			getResponse = get( url, headers, cookies );
			url = URLDecoder.decode( getResponse.getHeaders().getFirst("Location"), "UTF-8" );
			// should http://www.one.com/admin/ticketredirect.do?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
			// was    http://www.one.com/admin/ticketredirect.do?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
			getResponse = get( url, headers, cookies );
			url = URLDecoder.decode( getResponse.getHeaders().getFirst("Location"), "UTF-8" );
			// should https://www.one.com/admin/ticketredirect.do?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
			// was    https://www.one.com/admin/ticketredirect.do?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
			getResponse = get( url, headers, cookies );
			
			assertEquals( HttpStatus.MOVED_TEMPORARILY, getResponse.getStatusCode() );
			
		}
		catch( HttpClientErrorException e ) {

			logger.info( "http request failed", e );
			
			String body 
				= e.getResponseBodyAsString(); 
			
			logger.info( "response body: [{}]", body );
			
		}
		
	}
	
	@Test
	public void testOneDotComUrlEncoded() throws Exception {
		
		MultiValueMap<String,Object> fields
			= new LinkedMultiValueMap<String,Object>();
		
		try {
			
			// original URL 
			// https://filemanager.one.com/#sven.gladines@telenet.be/debrodders.be/files;
			
			
			StringBuilder cookies
				= new StringBuilder("");
			
			ResponseEntity<String> getResponse
				= null;

//			Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//			Accept-Encoding:gzip, deflate
//			Accept-Language:nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4
//			Cache-Control:max-age=0
//			Connection:keep-alive
//			Content-Length:334
//			Content-Type:application/x-www-form-urlencoded
//			Host:www.one.com
//			Origin:null
//			User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36
			
			Map<String,String> postHeaders
				= new HashMap<String,String>();
			
			postHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			postHeaders.put("Accept-Encoding", "gzip, deflate");
			postHeaders.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
			postHeaders.put("Cache-Control", "max-age=0");
			postHeaders.put("Connection", "keep-alive");
			postHeaders.put("Content-Type", "application/x-www-form-urlencoded" );
			postHeaders.put("Host", "www.one.com" );
			postHeaders.put("Origin", "null" );
			postHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			
			String url 
		 		= "https://www.one.com/admin/login.do";
			
			/*
			loginTarget:wfm
			loginDomain:true
			displayUsername:sven.gladines@telenet.be
			username:sven.gladines@telenet.be
			targetDomain:
			password1:br0dders.15
			successUrl:https://filemanager.one.com/
			loginUrl:
			errorUrl:https://login.one.com/cp/wfm
			*/
	
			fields.add( "loginTarget", "wfm" );
			fields.add( "loginDomain", "true" );
			fields.add( "username", "sven.gladines@telenet.be" );
			fields.add( "displayUsername", "sven.gladines@telenet.be" );
			fields.add( "password1", "br0dders.15" );
			fields.add( "targetDomain", "debrodders.be" );
			fields.add( "loginUrl", "" );
			//fields.add( "successUrl", "https://filemanager.one.com/" );
			//fields.add( "errorUrl", "https://login.one.com/cp/wfm" );
		
			ResponseEntity<String> postResponse
				= post( url, fields, postHeaders, cookies );
			
			url = postResponse.getHeaders().getFirst("Location");
			
//			Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//			Accept-Encoding:gzip, deflate, sdch
//			Accept-Language:nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4
//			Cache-Control:max-age=0
//			Connection:keep-alive
//			Cookie:JSESSIONID=1DCBFADC3ACE7172A6FE166FE683AC9E; OneLang=nl; abTestCookie=abtest%1EOld+customer+flow%1D
//			Host:www.one.com
//			User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36
			
			Map<String,String> getHeadersOne
				= new HashMap<String,String>();
		
			getHeadersOne.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			getHeadersOne.put("Accept-Encoding", "gzip, deflate, sdch");
			getHeadersOne.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
			getHeadersOne.put("Cache-Control", "max-age=0");
			getHeadersOne.put("Connection", "keep-alive");
			// getHeadersOne.put("Host", "www.one.com" );
			getHeadersOne.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			
			getResponse = get( url, getHeadersOne, cookies );
			
//			url = getResponse.getHeaders().getFirst("Location");
//			Map<String,String> getHeadersTwo
//				= new HashMap<String,String>();
//			
			//	Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/;q=0.8
			//	Accept-Encoding:gzip, deflate, sdch
			//	Accept-Language:nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4
			//	Cache-Control:max-age=0
			//	Connection:keep-alive
			//	Cookie:JSESSIONID=23F3D4AC0ED8C4A8515FB0C674459BA8; OneLang=nl; abTestCookie=abtest%1EOld+customer+flow%1D
			//	Host:www.one.com
			// User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36
//	
//			getHeadersTwo.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//			getHeadersTwo.put("Accept-Encoding", "gzip, deflate, sdch");
//			getHeadersTwo.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
//			getHeadersTwo.put("Cache-Control", "max-age=0");
//			getHeadersTwo.put("Connection", "keep-alive");
//			getHeadersTwo.put("Host", "www.one.com" );
//			getHeadersTwo.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
//			getResponse = get( url, getHeadersTwo, cookies );
//			
//			url = getResponse.getHeaders().getFirst("Location");
//			
//			getResponse = get( url, getHeadersTwo, cookies );
			
			assertEquals( HttpStatus.OK, getResponse.getStatusCode() );
			
			MyRestTemplate template 
				= new MyRestTemplate();
			
			String boneAuth 
				= template.getBoneAuth();
			
			cookies.append( "; BoneAuth=" ).append( boneAuth );
			
			Map<String,String> putHeaders
				= new HashMap<String,String>();
	
			putHeaders.put("Accept", "*/*");
			putHeaders.put("Accept-Encoding", "gzip, deflate, sdch");
			putHeaders.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
			putHeaders.put("Cache-Control", "max-age=0");
			putHeaders.put("Connection", "keep-alive");
			putHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
			
			putFile( putHeaders, cookies );
			
		}
		catch( HttpClientErrorException e ) {

			logger.info( "http request failed", e );
			
			String body 
				= e.getResponseBodyAsString(); 
			
			logger.info( "response body: [{}]", body );
			
		}
		
	}
	
	protected void putFile( Map<String,String> headers, StringBuilder cookies ) {
		
		String base
			= "https://filemanager.one.com/api/webspace/1/drive/debrodders.be/data/";
		
		String url
			= new StringBuilder( base ).append( System.currentTimeMillis() ).append( ".txt" ).toString();
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
		headrs.set( "Content-Type", MediaType.TEXT_PLAIN_VALUE );
		
		if ( headers != null ) {
		    	
	    	 for ( String header : headers.keySet() ) {
	 	    	headrs.set( header , headers.get( header ) );
	 	    }
	    	
	    }
		
		if ( cookies.length() > 0 ) {
			headrs.set( "Cookie", cookies.toString() );
		}
		
		 HttpEntity<String> entity 
	    	= new HttpEntity<String>( "xXx", headrs );
		
		 ResponseEntity<String> response
		 	= new RestTemplate().exchange(
				url,
				HttpMethod.PUT,
				entity,
				String.class );
		 
		 logger.info("PUT response is [{}]", response.getStatusCode() );
		
	}
	
	protected ResponseEntity<String> get( String url, Map<String,String> headers, StringBuilder cookies ) {
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
		headrs.set( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headrs.set( "Accept-Encoding", "gzip, deflate, sdch" );
		if ( cookies.length() > 0 ) {
			headrs.set( "Cookie", cookies.toString() );
		}
		
		 if ( headers != null ) {
		    	
	    	 for ( String header : headers.keySet() ) {
	    		 
	 	    	headrs.set( header , headers.get( header ) );
	 	    	
	 	    }
	    	
	    }
		
		 HttpEntity<String> entity 
	    	= new HttpEntity<String>( headrs );
		 
		 // should
		 // was    JSESSIONID=8966195C55C95EF226E23C92DA432AD8; Path=/; OneLang=en_us; Domain=.one.com; Expires=Fri, 26-Feb-2016 15:45:44 GMT; Path=/; abTestCookie=abtest%1EOld+customer+flow%1D; Expires=Fri, 26-Feb-2016 15:45:44 GMT; Path=/
		 logger.info("GET url is [{}]", url );
		 logger.info("GET headers are {}", headrs );
		 logger.info("GET cookies are {}", headrs.get( "Cookie" ) );
		
		 ResponseEntity<String> response
		 	= new MyRestTemplate().exchange(
				url,
				HttpMethod.GET,
				entity,
				String.class );
		 
		 logger.info("GET response is [{}]", response.getStatusCode() );
		 
		 if ( HttpStatus.FOUND.equals( response.getStatusCode() ) || HttpStatus.MOVED_PERMANENTLY.equals( response.getStatusCode() ) ) {
			 logger.info( "GET redirect location is {}", response.getHeaders().get( "Location") );
			 // logger.info( "GET body is [{}]", response.getBody() );
		 }
		 else {
			 logger.info( "GET body is [{}]", response.getBody() );
		 }
		 
		 eatCookies( response, cookies );
		 
		 return response;
		
	}
	
	protected ResponseEntity<String> post( String url, MultiValueMap<String,Object> fields, Map<String,String> headers, StringBuilder cookies ) {
		
		headers.put( "Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE );
		
		logger.info("POST url is [{}]", url );
		logger.info("POST headers are {}", headers );
		logger.info("POST cookies are {}", headers.get( "Cookie" ) );

		ResponseEntity<String> response
			= Client.postFormUrlEncoded( url, String.class, fields, headers );
		 
		 logger.info("POST response is [{}]", response.getStatusCode() );
		 
		 if ( HttpStatus.FOUND.equals( response.getStatusCode() ) || HttpStatus.MOVED_PERMANENTLY.equals( response.getStatusCode() ) ) {
			 logger.info( "POST redirect location is {}", response.getHeaders().get( "Location") );
			 // logger.info( "GET body is [{}]", response.getBody() );
		 }
		 else {
			 logger.info( "POST body is [{}]", response.getBody() );
		 }
		 
		 eatCookies( response, cookies );
		 
		 return response;
		
	}
	
	protected void eatCookies( ResponseEntity<?> response, StringBuilder currentCookies ) {
		
		List<String> cookiesToSet 
	 		= response.getHeaders().get( "Set-Cookie" );
		
		 if ( cookiesToSet != null ) {
			 
			 for ( String cookieToSet : cookiesToSet ) {
				 
				 if ( currentCookies.length() > 0 ) {
					 currentCookies.append( "; " );
				 }
				 
				 String cookie
				 	= cookieToSet.substring( 0, cookieToSet.indexOf( ";") );
				 
				 currentCookies.append( cookie ); 
				 
			 }

			 logger.info( "Cookies now are [{}]", currentCookies );
			
		 }
		 else {
			 //logger.info( "No cookies to eat" );
		 }
		
	}
	
}

// should   https://www.one.com/admin/ticketredirect.do?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
// was 		https://www.one.com/admin/ticketredirect.do?target=wfm&username=sven.gladines%40telenet.be&successUrl=https%3A%2F%2Ffilemanager.one.com%2F%23sven.gladines%40telenet.be%2Fdebrodders.be%2Ffiles%2F
