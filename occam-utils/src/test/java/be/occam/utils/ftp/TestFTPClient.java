package be.occam.utils.ftp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestFTPClient {
	
	@Test
	public void testPirlewiet() {
		
		FTPClient client
			= new FTPClient( "94.198.164.46", "pirlewietbe", "d;giTaal.15");
		
		boolean ok 
			= client.putTextFile( "svekke", "sven.txt", "cowabunga" );
		
		assertTrue( ok );
		
	}
	
	@Test
	public void testDeBrodders() {
		
		FTPClient client
			= new FTPClient( "ftp.debrodders.be", "debrodders.be", "debrodders2230");
		
		boolean ok 
			= client.putTextFile( "svekke", "sven.txt", "cowabunga" );
		
		assertTrue( ok );
		
	}


}
