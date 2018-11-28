package be.occam.utils.spring.web;

public class RedirectException extends RuntimeException {
	
	protected final String redirectUrl;
	
	public RedirectException( String redirectUrl ) {
		
		this.redirectUrl = redirectUrl;
		
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}
	
}
