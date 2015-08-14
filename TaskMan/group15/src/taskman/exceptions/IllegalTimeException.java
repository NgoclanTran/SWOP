package taskman.exceptions;

/**
 * A class representing an exception occurring when one enters a wrong date.
 * 
 * @version 1.0
 */
public class IllegalTimeException extends RuntimeException {

	private String message;

	private static final long serialVersionUID = 1L;

	public IllegalTimeException() {
		this("Illegal time.");
	}

	public IllegalTimeException(String s) {
		this.message = s;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
