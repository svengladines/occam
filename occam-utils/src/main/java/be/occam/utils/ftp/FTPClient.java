package be.occam.utils.ftp;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPClient {
	
	protected static final Logger logger
		= LoggerFactory.getLogger( FTPClient.class );
	
	protected final String host;
	protected final String userName;
	protected final String passWord;
	
	public FTPClient( String host, String userName, String passWord ) {
		
		this.host = host;
		this.userName = userName;
		this.passWord = passWord;
		
	}
	
	public boolean putTextFile( String path, String fileName, String content ) {
		
		  org.apache.commons.net.ftp.FTPClient ftp 
		  		= new org.apache.commons.net.ftp.FTPClient();
		   
		    FTPClientConfig config 
		    	= new FTPClientConfig();
		   
		    ftp.configure(config );
		    
		    boolean ok 
		    	= true;
		    
		    try {
		      int reply;
		      ftp.connect( this.host );
		      System.out.println("Connected to " + this.host + ".");
		      System.out.print(ftp.getReplyString());
		      
		      // After connection attempt, you should check the reply code to verify
		      // success.
		      reply = ftp.getReplyCode();

		      if(!FTPReply.isPositiveCompletion(reply)) {
		    	  
		        ftp.disconnect();
		        System.err.println("FTP server refused connection.");
		        
		        ok = false;
		      }
		      
		      ok = ftp.login( userName , passWord );
		      
		      if ( !ok ) {
		    	  System.err.println("login failed");
		    	  return false;
		      }
		      
		      System.out.println("Logged in as " + this.userName );
		      System.out.println("Current working directory: " + ftp.printWorkingDirectory() );
		      
		      ok = ftp.changeWorkingDirectory( path );
		      
		      if ( !ok ) {
		    	  System.err.println("change working directory failed.");
		    	  return false;
		      }
		      
		      System.out.println("New working directory: " + ftp.printWorkingDirectory() );
		      
		      ByteArrayInputStream bis
		      	= new ByteArrayInputStream( content.getBytes("utf-8") );
		      
		      System.out.println( "storing file..." );
		      
		      ftp.enterLocalPassiveMode();
		      ftp.setFileTransferMode( FTP.BINARY_FILE_TYPE );
		      ok = ftp.storeFile( fileName, bis );
		      
	    	  if ( !ok ) {
		    	  System.err.println("failed to store file.");
		    	  return false;
		      }
	    	  
	    	  bis.close();
	    	  
	    	  System.out.println( "stored file..." );
		      
		      ftp.logout();
		      
		      return true;
		      
		    } catch(IOException e) {
		    	System.out.println( String.format("FTP error, message is [%s]", e.getMessage() ) );
		    	e.printStackTrace( System.out );
		      return false;
		    } catch(Throwable e) {
		    	System.out.println( String.format("error, message is [%s]", e.getMessage() ) );
		      e.printStackTrace();
		      return false;
		    }finally {
		      if(ftp.isConnected()) {
		        try {
		          ftp.disconnect();
		        } catch(IOException ioe) {
		          // do nothing
		        }
		      }
		    }
		
	}
	
	
}
