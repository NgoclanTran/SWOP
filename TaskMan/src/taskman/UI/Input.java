package taskman.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Input {

	private final BufferedReader reader;

	protected Input() {
		InputStreamReader readin = new InputStreamReader(System.in);
		reader = new BufferedReader(readin);
	}

	protected String getInput() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			return "";
		}
	}

	protected int getNumberInput() {
		try {
			return Integer.parseInt(getInput());
		} catch (NumberFormatException e2) {
			return -1;
		}
	}

}
