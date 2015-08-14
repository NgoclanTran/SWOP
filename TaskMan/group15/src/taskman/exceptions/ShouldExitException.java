package taskman.exceptions;

/**
 * A class representing an exception occurring when one wants to exit a choice menu.
 * 
 * @version		1.0
 */
public class ShouldExitException extends RuntimeException {
	
	private String message;
	
	private static final long serialVersionUID = 1L;
	
	public ShouldExitException(){
		this("");
	}
	
	public ShouldExitException(String s){
		this.message = s;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
	
}
