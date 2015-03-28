package taskman.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import taskman.model.project.Project;
import taskman.model.project.task.Task;

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

	private boolean isValidMessage(String message) {
		return message != null;
	}

	private void displayList(List<?> list, int tab, boolean printReturn)
			throws IllegalArgumentException {
		if (!isValidList(list))
			throw new IllegalArgumentException("Invalid list");

		String tabString = "";
		for (int i = 0; i < tab; i++) {
			tabString += "\t";
		}

		if (list.size() == 0) {
			if(printReturn)
				display(tabString + "0. Return");
			else
				display(tabString + "Nothing to display.");
		} else {
			if(printReturn)
				display(tabString + "0. Return");
			for (int i = 1; i <= list.size(); i++) {
				Object item = i + ". " + list.get(i - 1);
				String string = item.toString();
				string = indentStringWithNewLines(string, tabString + "\t");
				string = tabString + string;
				display(string);
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

	private String getString(DateTime time) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
		return formatter.print(time);
	}

	private List<String> getString(List<Task> tasks) {
		ArrayList<String> tasksInfo = new ArrayList<String>();
		for(int i = 1; i <= tasks.size(); i++) {
			StringBuilder taskInfo = new StringBuilder();
			taskInfo.append("Task ");
			taskInfo.append(i);
			taskInfo.append(": ");
			taskInfo.append(tasks.get(i-1).getStatusName());
			tasksInfo.add(taskInfo.toString());
		}
		return tasksInfo;
	}

	public int getNumberInput(String message) throws IllegalArgumentException {
		display(message);
		while (true) {
			try {
				String input = reader.readLine();
				displayEmptyLine();
				return Integer.parseInt(input);
			} catch (IOException e1) {
				display(e1.getMessage());
			} catch (NumberFormatException e2) {
				display("Please input a number.");
			}
		}
	}

	public String getTextInput(String message) throws IllegalArgumentException {
		display(message);
		try {
			return reader.readLine();
		} catch (IOException e) {
			return "";
		}
	}

	public void display(String message) throws IllegalArgumentException {
		if (!isValidMessage(message))
			throw new IllegalArgumentException("Invalid message.");
		print(message);
	}

	public void displayEmptyLine() throws IllegalArgumentException {
		display("");
	}

	public void displayVersion() throws IllegalArgumentException {
		display("TaskMan V2.0");
		displayEmptyLine();
	}

	public void displayList(List<?> list) {
		displayList(list, 0, false);
		displayEmptyLine();
	}

	public void displayProjectList(List<?> projects) {
		displayList(projects, 0, true);
		displayEmptyLine();
	}

	public void displayProjectDetails(Project project, List<Task> tasks) {
		StringBuilder projectDetails = new StringBuilder();
		projectDetails.append(project.getName());
		projectDetails.append(":\n");
		projectDetails.append(project.getDescription());
		projectDetails.append("\n");
		projectDetails.append(getString(project.getCreationTime()));
		projectDetails.append(" - ");
		projectDetails.append(getString(project.getDueTime()));
		projectDetails.append("\n");
		if(project.isFinished())
			projectDetails.append("Status: Finished");
		else
			projectDetails.append("Status: Ongoing");
		display(indentStringWithNewLines(projectDetails.toString(), "\t"));
		displayEmptyLine();
		displayList(getString(tasks), 1, true);
		displayEmptyLine();
	}
}
