package be.occam.utils.one;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.client.RestTemplate;

import be.occam.utils.spring.web.Client;
import be.occam.utils.spring.web.MyRestTemplate;

public class OneDotComClient {
	
	protected static final Logger logger
		= LoggerFactory.getLogger( OneDotComClient.class );
	
	protected final String domain;
	protected final String userName;
	protected final String passWord;
	
	public OneDotComClient( String domain, String userName, String passWord ) {
		
		this.domain = domain;
		this.userName = userName;
		this.passWord = passWord;
		
	};
	
	public void store( String path, String json ) {
		
		StringBuilder cookies
			= new StringBuilder();
		
		this.login( this.domain, this.userName, this.passWord, cookies );
		
		this.putFile( this.domain, path, json, this.commonHeaders(), cookies );
		
	}
	
	public String retrieve( String path ) {
		
		StringBuilder cookies
			= new StringBuilder();
		
		this.login( this.domain, this.userName, this.passWord, cookies );
		
		ResponseEntity<String> response 
			= this.get( path, this.commonHeaders(), cookies );
		
		return response.getBody();
		
	}
	
	protected void login( String domain, String userName, String passWord, StringBuilder cookies ) {
		
		try {
			
			// POST to the login form
			
			Map<String,String> postHeaders
				= this.commonHeaders();
			
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
			
			String url 
		 		= "https://www.one.com/admin/login.do";
			
			MultiValueMap<String,Object> fields
				= new LinkedMultiValueMap<String,Object>();
			
			fields.add( "loginTarget", "filemanager.one.com" );
			fields.add( "loginDomain", "true" );
			fields.add( "username", userName );
			fields.add( "displayUsername", userName );
			fields.add( "password1", passWord );
			fields.add( "targetDomain", domain );
			fields.add( "loginUrl", "" );
			fields.add( "successUrl", "https://filemanager.one.com/#sven.gladines@telenet.be/debrodders.be/files/" );
			//fields.add( "errorUrl", "https://login.one.com/cp/wfm" );
			
			ResponseEntity<String> postResponse
				= post( url, fields, postHeaders, cookies );
			
			url = postResponse.getHeaders().getLocation().toASCIIString();
			
			logger.info( "ticket url is [{}]", url );
			
			// follow the redirect
			
			ResponseEntity<String> ticketOne
				= this.get( url, this.ticketHeaders(), cookies);
			
			logger.info( "ticket one response code is [{}]", ticketOne.getStatusCode() );
			logger.info( "ticket one response content type is [{}]", ticketOne.getHeaders().getContentType() );
			// logger.info( "ticket one response body is [{}]", ticketOne.getBody() );
			
			url = ticketOne.getHeaders().getLocation().toASCIIString();
			
			ResponseEntity<String> ticketTwo
				= this.get( url, this.ticketHeaders(), cookies);
		
			logger.info( "ticket two response code is [{}]", ticketTwo.getStatusCode() );
			// logger.info( "ticket two response body is [{}]", ticketTwo.getBody() );
			
			url = ticketTwo.getHeaders().getLocation().toASCIIString();
			
			ResponseEntity<String> ticketThree
				= this.get( url, this.ticketHeaders(), cookies);
	
			logger.info( "ticket three response code is [{}]", ticketThree.getStatusCode() );
		// logger.info( "ticket two response body is [{}]", ticketTwo.getBody() );
			
			url = ticketThree.getHeaders().getLocation().toASCIIString();
			logger.info( "file manager url is [{}]", url );
			
		}
		catch( Exception e ) {
			
			logger.warn( "login failed" );
			
		}
		
	}
	
	
	protected void putFile( String domain, String file, String content, Map<String,String> headers, StringBuilder cookies ) {
	
		String base
			= String.format( "https://filemanager.one.com/api/webspace/2/drive/%s/data/httpd.www", domain );
		
		String url
			= new StringBuilder( base ).append( file ).toString();
		
		HttpHeaders headrs 
			= new HttpHeaders();
		
		headrs.set( "Content-Type", MediaType.APPLICATION_JSON_VALUE );
		
		if ( headers != null ) {
		    	
	    	 for ( String header : headers.keySet() ) {
	 	    	headrs.set( header , headers.get( header ) );
	 	    }
	    	
	    }
		
		if ( cookies.length() > 0 ) {
			headrs.set( "Cookie", cookies.toString() );
		}
	
		 HttpEntity<String> entity 
	    	= new HttpEntity<String>( content, headrs );
		
		 ResponseEntity<String> response
		 	= new RestTemplate().exchange(
				url,
				HttpMethod.PUT,
				entity,
				String.class );
	 
		 logger.info("PUT response is [{}]", response.getStatusCode() );
	
	}

	protected ResponseEntity<String> get( String path, Map<String,String> headers, StringBuilder cookies ) {
	
		String base
			= String.format( "https://filemanager.one.com/api/webspace/2/drive/%s/data/httpd.www", domain );
	
		String url
			= new StringBuilder( base ).append( path ).toString();
	
		HttpHeaders headrs 
			= new HttpHeaders();
		
		// headrs.set( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		//headrs.set( "Accept-Encoding", "gzip, deflate, sdch" );
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
		// logger.info( "GET body is [{}]", response.getBody() );
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
	
	protected Map<String,String> commonHeaders() {
		
		Map<String,String> headers
			= new HashMap<String,String>();

		headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		headers.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
		
		return headers;
		
	}
	
	protected Map<String,String> ticketHeaders() {
		
//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//		Accept-Encoding:gzip, deflate, sdch, br
//				Accept-Language:en-US,en;q=0.8,nl;q=0.6,fr;q=0.4
//				Cache-Control:max-age=0
//				Connection:keep-alive
//				Cookie:JSESSIONID=t7F_n9GKiV-fE3cZV-DF7MvwXHWaFd8n9bZfRzsu.crm-appsrv2; __utmt=1; __utma=113821017.1261716520.1504516030.1504516030.1504516030.1; __utmb=113821017.2.10.1504516030; __utmc=113821017; __utmz=113821017.1504516030.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _hp2_id.2821398594=%7B%22userId%22%3A%227334004307359900%22%2C%22pageviewId%22%3A%223970706516049143%22%2C%22sessionId%22%3A%225851059398873440%22%2C%22identity%22%3Anull%2C%22trackerVersion%22%3A%223.0%22%7D; _hp2_ses_props.2821398594=%7B%22r%22%3A%22https%3A%2F%2Ffilemanager.one.com%2F%22%2C%22ts%22%3A1504516031393%2C%22d%22%3A%22login.one.com%22%2C%22h%22%3A%22%2Fcp%2Fwfm%22%7D; CSRF_G_TOKEN=c354dd52e4ca80407084ace3c9f51d6f; OneLang=en_gb; abTestCookie=abtest%1E%1D
//				Host:www.one.com
//				Referer:https://login.one.com/cp/wfm
//				Upgrade-Insecure-Requests:1
//				User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
		
		Map<String,String> headers
		= this.commonHeaders();

		//headers.put( "Accept-Encoding","gzip, deflate, sdch, br" );
		headers.put( "Referer","https://login.one.com/cp/wfm" );
		headers.put( "Upgrade-Insecure-Requests","1" );
		
		return headers;
	
	}

}

/*/*

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

//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/*;q=0.8
//		Accept-Encoding:gzip, deflate
//		Accept-Language:nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4
//		Cache-Control:max-age=0
//		Connection:keep-alive
//		Content-Length:334
//		Content-Type:application/x-www-form-urlencoded
//		Host:www.one.com
//		Origin:null
//		User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36
		
		Map<String,String> postHeaders
			= new HashMap<String,String>();
		
		postHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,/;q=0.8");
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
		
//		loginTarget:wfm
//		loginDomain:true
//		displayUsername:sven.gladines@telenet.be
//		username:sven.gladines@telenet.be
//		targetDomain:
//		password1:br0dders.15
//		successUrl:https://filemanager.one.com/
//		loginUrl:
//		errorUrl:https://login.one.com/cp/wfm

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
		
//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*;q=0.8
//		Accept-Encoding:gzip, deflate, sdch
//		Accept-Language:nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4
//		Cache-Control:max-age=0
//		Connection:keep-alive
//		Cookie:JSESSIONID=1DCBFADC3ACE7172A6FE166FE683AC9E; OneLang=nl; abTestCookie=abtest%1EOld+customer+flow%1D
//		Host:www.one.com
//		User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36
		
		Map<String,String> getHeadersOne
			= new HashMap<String,String>();
	
		getHeadersOne.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,;q=0.8");
		getHeadersOne.put("Accept-Encoding", "gzip, deflate, sdch");
		getHeadersOne.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
		getHeadersOne.put("Cache-Control", "max-age=0");
		getHeadersOne.put("Connection", "keep-alive");
		// getHeadersOne.put("Host", "www.one.com" );
		getHeadersOne.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		
		getResponse = get( url, getHeadersOne, cookies );
		
//		url = getResponse.getHeaders().getFirst("Location");
//		Map<String,String> getHeadersTwo
//			= new HashMap<String,String>();
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
//		getHeadersTwo.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,;q=0.8");
//		getHeadersTwo.put("Accept-Encoding", "gzip, deflate, sdch");
//		getHeadersTwo.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
//		getHeadersTwo.put("Cache-Control", "max-age=0");
//		getHeadersTwo.put("Connection", "keep-alive");
//		getHeadersTwo.put("Host", "www.one.com" );
//		getHeadersTwo.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
//		getResponse = get( url, getHeadersTwo, cookies );
//		
//		url = getResponse.getHeaders().getFirst("Location");
//		
//		getResponse = get( url, getHeadersTwo, cookies );
		
		MyRestTemplate template 
			= new MyRestTemplate();
		
		String boneAuth 
			= template.getBoneAuth();
		
		cookies.append( "; BoneAuth=" ).append( boneAuth );
		
		Map<String,String> putHeaders
			= new HashMap<String,String>();

		putHeaders.put("Accept", "");
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

	/*
	public void storeJSON( String file, String json ) throws Exception {
	
	MultiValueMap<String,Object> fields
		= new LinkedMultiValueMap<String,Object>();
	
	try {
		
		// original URL 
		// https://filemanager.one.com/#sven.gladines@telenet.be/debrodders.be/files;
		StringBuilder cookies
			= new StringBuilder("");
		
		Map<String,String> commonHeaders
			= new HashMap<String,String>();
		
		// POST to the login form
		
		Map<String,String> postHeaders
			= new HashMap<String,String>();
		
		postHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		postHeaders.put("Accept-Language", "nl-NL,nl;q=0.8,en-US;q=0.6,en;q=0.4");
		postHeaders.put("Referer", "https://login.one.com/cp/wfm" );
		postHeaders.put("Origin", "https://login.one.com" );
		
		String url 
	 		= "https://www.one.com/admin/login.do";
		
//		loginTarget:wfm
//		loginDomain:true
//		displayUsername:sven.gladines@telenet.be
//		username:sven.gladines@telenet.be
//		targetDomain:
//		password1:br0dders.15
//		successUrl:https://filemanager.one.com/
//		loginUrl:
//		errorUrl:https://login.one.com/cp/wfm

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
		
	}
	catch( HttpClientErrorException e ) {

		logger.info( "http request failed", e );
		
		String body 
			= e.getResponseBodyAsString(); 
		
		logger.info( "response body: [{}]", body );
		
	}
	
}

*/
