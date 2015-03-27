package task.UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class UI {

	private final BufferedReader reader;
	private final PrintWriter writer;

	public UI() {
		InputStreamReader readin = new InputStreamReader(System.in);
		reader = new BufferedReader(readin);
		writer = new PrintWriter(System.out);
	}

	private void print(String message) {
		writer.println(message);
		writer.flush();
	}

	public void display(String message) throws IllegalArgumentException {
		if (!isValidMessage(message))
			throw new IllegalArgumentException("Invalid message.");
		print(message);
	}

	private boolean isValidMessage(String message) {
		return message != null;
	}

	private void displayEmptyLine() throws IllegalArgumentException {
		display("");
	}

	public void displayWelcome() throws IllegalArgumentException {
		display("TaskMan V2.0");
		displayEmptyLine();
	}

	public void displayList(List<?> list) {
		displayList(list, "", "");
	}

	private void displayList(List<?> list, String tab, String prefix)
			throws IllegalArgumentException {
		if (!isValidList(list))
			throw new IllegalArgumentException("Invalid list");
		
		if (list.size() == 0) {
			display(tab + "Nothing to display.");
		} else {
			for (int i = 1; i <= list.size(); i++) {
				Object item = list.get(i - 1);
				if (item instanceof List) {
					display(tab + prefix + i + ")");
					displayList((List<?>) item, tab + "\t", prefix + i + ".");
				}
				else {
					String string = item.toString();
					string = indentStringWithNewLines(string, tab + "\t");
					string = tab + prefix + string;
					display(string);
				}
			}
		}
	}

	private boolean isValidList(List<?> list) {
		if (list == null)
			return false;
		for (Object o : list) {
			if (o == null)
				return false;
		}
		return true;
	}
	
	private String indentStringWithNewLines(String string, String prefix) {
		return string.replaceAll("\n", "\n" + prefix);
	}

}
