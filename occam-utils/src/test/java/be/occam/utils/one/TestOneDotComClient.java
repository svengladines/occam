package be.occam.utils.one;

import org.junit.Test;

public class TestOneDotComClient {
	
	@Test
	public void doTheBroddersSmoke() {
		
		OneDotComClient oneDotComClient
			= new OneDotComClient( "debrodders.be", "sven.gladines@telenet.be", "br0dders.15" );
		
		StringBuilder cookies
			= new StringBuilder();
		
		oneDotComClient.login( "debrodders.be", "sven.gladines@telenet.be", "br0dders.15", cookies );
		
	}
	
}
