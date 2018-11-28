package be.occam.utils.json;

import java.util.HashMap;
import java.util.Map;

public class JSON {
	
	public static String field( String field, String json ) {
		
		Map<String,String> map
			= map( json );
		
		return map.get( field );
		
	}
	
	public static Map<String,String> map( String json ) {
		
		Map<String,String> map
			= new HashMap<String,String>();
		
		String[] parts = json.split( "," );
		
		for ( int i = 0; i < parts.length; i+=2 ) {
			
			String line 
				=  parts[ i ].trim();
			
			String[] keyValue
				= line.split(":");
			
			String key 
				= keyValue[0].replaceAll("\"", "").trim();
			
			String value
				= keyValue[1].replaceAll("\"", "").trim();
			
			map.put(key, value);
			
		}
		
		return map;
		
	} 

}
