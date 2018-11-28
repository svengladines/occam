package be.occam.colloseum.publish.appengine.itests;

import org.datanucleus.api.jpa.JPAAdapter;
import org.datanucleus.enhancer.DataNucleusEnhancer;
import org.junit.Test;

public class TestEnhancer {

	@Test
	public void doesItSmoke() throws Exception {
		
		String[] args
			= new String[] { "-v","-pu","colloseumUnit","-api","JPA" };
		
		DataNucleusEnhancer.main( args );
		
	}
}
