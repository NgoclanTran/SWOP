package taskman.view.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import taskman.exceptions.ShouldExitException;

public class Input {

	private final BufferedReader reader;
	private final String stop = "cancel";

	/**
	 * The constructor of the input. It will initialize a buffered reader to
	 * read input from the command line.
	 */
	public Input() {
		InputStreamReader readin = new InputStreamReader(System.in);
		reader = new BufferedReader(readin);
	}

	/**
	 * This method will return a string the user entered in the command line.
	 * 
	 * @return Returns the entered string in the command line.
	 * 
	 * @throws ShouldExitException
	 *             The user entered "cancel".
	 */
	public String getInput() throws ShouldExitException {
		try {
			String input = reader.readLine();

			if (input.toLowerCase().equals(stop))
				throw new ShouldExitException();

			return input;
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * This method will return an integer the user entered in the command line.
	 * 
	 * @return Returns the entered integer in the command line.
	 * 
	 * @throws ShouldExitException
	 *             The user entered "cancel".
	 */
	public int getNumberInput() throws ShouldExitException {
		try {
			return Integer.parseInt(getInput());
		} catch (NumberFormatException e2) {
			return Integer.MIN_VALUE;
		}
	}

}
