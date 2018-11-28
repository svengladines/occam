package be.occam.test.jtest;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import be.occam.utils.spring.configuration.ConfigurationProfiles;

public class JTest {
	
	protected final Logger logger 
		= LoggerFactory.getLogger( this.getClass() );

	protected int forcePort = 0;
	protected int actualPort;
	protected String context;
	
	protected String forceConfigPath = null;
	protected String forcePath = null;
	
	protected Server server;
	
	protected Map<String,String> contexts
		= new HashMap<String, String>();
	
	protected Set<WebAppContext> webapps
		= new HashSet<WebAppContext>();
	
	protected RestTemplate template = null;
	
	public JTest( String context, String path, int port, String profile ) {
		
		ConfigurationProfiles.activateProfile( profile );
		this.contexts.put( context, path );
		this.forcePort = port;
		
	}
	
	public JTest( String context, int port, String profile ) {
		
		this( context, "src/main/webapp", port, profile );
		
	}
	
	public JTest( String context, String path ) {
		
		this( context, path, 0, ConfigurationProfiles.TEST );
		
	}
	
	public JTest( String context ) {
		
		this( context, "src/main/webapp" );
		
	}
	
	public JTest( String context, int port ) {
		
		this( context );
		this.forcePort = port;
		
	}
	
	protected JTest context( String context, String path ) {
		
		this.contexts.put( context, path );
		return this;
		
	}

	@Before
	public void setUp() {
		
		try {
		
			if ( server == null ) {
				server = new Server( forcePort );
			}
			
			HandlerCollection handlers 
				= new HandlerCollection();
			
			for ( String context : this.contexts.keySet() ) {
				
				WebAppContext webContext
					= new WebAppContext( this.contexts.get( context ), context );
			
				handlers.addHandler( webContext );
				
				this.webapps.add( webContext );
				
			}
			
			this.server.setHandler( handlers );
			
			ServerConnector connector
				= new ServerConnector(server);
			connector.setIdleTimeout(30000);
			
			server.addConnector( connector );
	        server.start();
	        
	        this.actualPort = connector.getLocalPort();
	        
	        this.template = new RestTemplate();
	        
	        for ( WebAppContext webapp : this.webapps ) {
	        	
	        	if ( this.contextFailed( webapp ) ) {
		        	fail( "web application [" + webapp.getContextPath() + "] could not be started" );
		        }
	        	
	        }
	        
	        
		}
		catch( AssertionError e ) {
			throw e;
		}
		catch( Throwable e ) {
			logger.warn( "web server startup failed", e );
			fail( "web server could not be started: " + e.getMessage() );
		}
	
	}

	@After
	public void tearDown() {
		
		try {
			this.server.stop();
		}
		catch( Exception e ) {
			logger.warn( "stopping server failed", e );
		}
		
	}

	protected StringBuilder baseUrl() {
		
		return new StringBuilder("http://localhost:").append(this.actualPort).append( this.contexts.keySet().iterator().next() );
		
	}
	
	protected StringBuilder baseResourceUrl() {
		
		return new StringBuilder().append( baseUrl() ).append( "/rs" );
		
	}
	
	protected StringBuilder baseAPIURL() {
		
		return new StringBuilder().append( baseUrl() ).append( "/api" );
		
	}

	protected boolean contextFailed( WebAppContext context ) {
		
		final Boolean[] failed = new Boolean[] { false };
		
		/*
		try {
		
			HttpServletResponse response = new Response( null ) {
	
				@Override
				public void sendError(int sc) throws IOException {
					
					failed[0] = true;
					
				}
				
			};
			
			context.handle( "" , null, response, 0 );
			
			
		}
		catch (Exception e){
			// Exception is to be expected
			failed[0] = false;
		}
		*/
		return failed[0];
		
		
	}
	
}
