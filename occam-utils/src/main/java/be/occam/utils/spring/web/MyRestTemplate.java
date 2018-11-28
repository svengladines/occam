package be.occam.utils.spring.web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class MyRestTemplate extends RestTemplate {
	
	protected static final Logger logger
		= LoggerFactory.getLogger( MyRestTemplate.class );
	
	protected static HttpComponentsClientHttpRequestFactory clientHttpRequestFactory;
	

	public MyRestTemplate() {
		
		/* super( clientHttpRequestFactory() ); */
		super( 
				new SimpleClientHttpRequestFactory() {
						protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException { super.prepareConnection(connection, httpMethod); connection.setInstanceFollowRedirects(false);
					};
				} );
	}

	private static ClientHttpRequestFactory clientHttpRequestFactory() {
		
		if ( MyRestTemplate.clientHttpRequestFactory == null ) {
			MyRestTemplate.clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
			MyRestTemplate.clientHttpRequestFactory.setConnectTimeout( 0 );
			
			if ( MyRestTemplate.clientHttpRequestFactory.getHttpClient() instanceof DefaultHttpClient ) {
				DefaultHttpClient defaultClient 
					= (DefaultHttpClient) MyRestTemplate.clientHttpRequestFactory.getHttpClient();
				
				defaultClient.setCookieStore( new BasicCookieStore() );
				
			} 
		}
		
        return MyRestTemplate.clientHttpRequestFactory;
	}

	@Override
	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Object... urlVariables) throws RestClientException {
		
		
		URI notExpanded = URI.create( url );
		
		logger.info( "HTTP url is [{}]", notExpanded.toString() );
		
		return doExecute( notExpanded, method, requestCallback, responseExtractor);
		// return super.execute( url, method, requestCallback, responseExtractor, urlVariables ); 
		
		
	}
	
	public String getBoneAuth() {
		
		String boneAuth 
			= null;
		
		if ( MyRestTemplate.clientHttpRequestFactory.getHttpClient() instanceof DefaultHttpClient ) {
			DefaultHttpClient defaultClient 
				= (DefaultHttpClient) MyRestTemplate.clientHttpRequestFactory.getHttpClient();
			
			List<Cookie> cookies
				= defaultClient.getCookieStore().getCookies();
			
			for ( Cookie cookie : cookies ) {
				
				String name 
					= cookie.getName();
				
				logger.info( "cookie name is [{}]", name );
				logger.info( "cookie value is [{}]", cookie.getValue() );
				
				if ("BoneAuth".equals( name ) ) {
					boneAuth = cookie.getValue();
					break;
				}
				
			}
			
		} 
		
		return boneAuth;
		
	}
	
	

}
