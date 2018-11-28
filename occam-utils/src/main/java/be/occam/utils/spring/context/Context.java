package be.occam.utils.spring.context;

import org.springframework.context.ApplicationContext;

public class Context {
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(ApplicationContext context, String name ) {
		
		return (T) context.getBean(name);
		
	}

}
