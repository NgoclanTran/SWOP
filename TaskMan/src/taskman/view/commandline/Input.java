package taskman.view.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import taskman.exceptions.ShouldExitException;

public class Input {

	private final BufferedReader reader;
	private final String stop = "cancel";

	public Input() {
		InputStreamReader readin = new InputStreamReader(System.in);
		reader = new BufferedReader(readin);
	}

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

	public int getNumberInput() throws ShouldExitException {
		try {
			return Integer.parseInt(getInput());
		} catch (NumberFormatException e2) {
			return Integer.MIN_VALUE;
		}
	}

}
