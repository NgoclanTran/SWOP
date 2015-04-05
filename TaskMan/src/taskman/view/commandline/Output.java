package taskman.view.commandline;

import java.io.PrintWriter;
import java.util.List;

public class Output {

	private final PrintWriter writer;

	public Output() {
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
	
	public void displayEmptyLine() throws IllegalArgumentException {
		display("");
	}
	
	public void displayList(List<?> list, int tabs, boolean printReturn)
			throws IllegalArgumentException {
		if (!isValidList(list))
			throw new IllegalArgumentException("Invalid list");
		
		String tabString = getTabsString(tabs);

		if (list.size() == 0) {
			if (printReturn)
				display(tabString + "0. Return");
			else
				display(tabString + "Nothing to display.");
		} else {
			if (printReturn)
				display(tabString + "0. Return");
			for (int i = 1; i <= list.size(); i++) {
				String item = i + ". " + list.get(i - 1).toString();
				item = indentStringWithNewLines(item, tabs + 1);
				item = tabString + item;
				display(item);
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
	
	public String indentStringWithNewLines(String string, int tabs) {
		return string.replaceAll("\n", "\n" + getTabsString(tabs));
	}
	
	private String getTabsString(int tabs) {
		String tabString = "";
		for (int i = 0; i < tabs; i++) {
			tabString += "\t";
		}
		return tabString;
	}

}
