package taskman.view.commandline;

import java.io.PrintWriter;
import java.util.List;

public class Output {

	private final PrintWriter writer;

	/**
	 * The constructor of the output. It will initialize a print writer to print
	 * text to the command line.
	 */
	public Output() {
		writer = new PrintWriter(System.out);
	}

	/**
	 * This method will print the given string.
	 * 
	 * @param message
	 * 
	 * @throws IllegalArgumentException
	 *             The message isn't a valid string.
	 */
	public void display(String message) throws IllegalArgumentException {
		if (!isValidMessage(message))
			throw new IllegalArgumentException("Invalid message.");
		print(message);
	}

	/**
	 * This method will check whether the given string is valid.
	 * 
	 * @param message
	 * 
	 * @return Returns true if the message is a string different from null, else
	 *         it returns false.
	 */
	private boolean isValidMessage(String message) {
		return message != null;
	}

	/**
	 * This method will print the given string to the print writer.
	 * 
	 * @param message
	 */
	private void print(String message) {
		writer.println(message);
		writer.flush();
	}

	/**
	 * This method will print an empty line.
	 */
	public void displayEmptyLine() {
		display("");
	}

	/**
	 * This method will print the given list. The tabs integer is to enter the
	 * amount of indentation. The printReturn boolean is whether or not to print
	 * "0. Return" in front of the list.
	 * 
	 * @param list
	 * @param tabs
	 * @param printReturn
	 * 
	 * @throws IllegalArgumentException
	 *             The list isn't valid.
	 */
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

	/**
	 * This method will check whether the list is a valid list (different from
	 * null) containing at least 1 item.
	 * 
	 * @param list
	 * 
	 * @return Returns true if the list differs from null and isn't empty, else
	 *         it returns false.
	 */
	private boolean isValidList(List<?> list) {
		if (list == null)
			return false;
		for (Object o : list) {
			if (o == null)
				return false;
		}
		return true;
	}

	/**
	 * This method will add a given amount of tabs in front of a given string
	 * and also on every new line of this string.
	 * 
	 * @param string
	 * @param tabs
	 * 
	 * @return Returns the given string with the given amount of tabs in front
	 *         of the string and in front of every new line.
	 */
	public String indentStringWithNewLines(String string, int tabs) {
		return string.replaceAll("\n", "\n" + getTabsString(tabs));
	}

	/**
	 * This method will make a string with the given amount of tabs.
	 * 
	 * @param tabs
	 * 
	 * @return Returns a string with the given amount of tabs.
	 */
	private String getTabsString(int tabs) {
		String tabString = "";
		for (int i = 0; i < tabs; i++) {
			tabString += "\t";
		}
		return tabString;
	}

}
