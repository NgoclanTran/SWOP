package taskman.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
			if (printReturn)
				display(tabString + "0. Return");
			else
				display(tabString + "Nothing to display.");
		} else {
			if (printReturn)
				display(tabString + "0. Return");
			for (int i = 1; i <= list.size(); i++) {
				Object item = i + ". " + list.get(i - 1);
				String string = item.toString();
				string = indentStringWithNewLines(string, tab + 1);
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

	private String indentStringWithNewLines(String string, int tab) {
		String tabString = "";
		for (int i = 0; i < tab; i++) {
			tabString += "\t";
		}
		return string.replaceAll("\n", "\n" + tabString);
	}

	private String getStringDate(DateTime time) {
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("dd-MM-yyyy HH:mm");
		return formatter.print(time);
	}

	private String getStringProjectDetails(Project project) {
		StringBuilder projectDetails = new StringBuilder();
		projectDetails.append(project.getName());
		projectDetails.append(":\n");
		projectDetails.append(project.getDescription());
		projectDetails.append("\n");
		projectDetails.append(getStringDate(project.getCreationTime()));
		projectDetails.append(" - ");
		projectDetails.append(getStringDate(project.getDueTime()));
		projectDetails.append("\n");
		if (project.isFinished()) {
			projectDetails.append("Status: Finished");
		} else {
			projectDetails.append("Status: Ongoing");
			projectDetails.append("\n");
			projectDetails.append("Estimated end time: ");
			projectDetails.append(getStringDate(project
					.getEstimatedFinishTime()));
		}
		return projectDetails.toString();
	}

	private String getStringTask(Task task, int index) {
		StringBuilder taskInfo = new StringBuilder();
		taskInfo.append("Task ");
		taskInfo.append(index);
		taskInfo.append(": ");
		taskInfo.append(task.getStatusName());
		return taskInfo.toString();
	}

	private String getStringTaskDetails(Task task, int index) {
		StringBuilder taskInfo = new StringBuilder();
		taskInfo.append("Task ");
		taskInfo.append(index);
		taskInfo.append(":");
		taskInfo.append("\n");
		taskInfo.append(task.getDescription());
		taskInfo.append("\n");
		taskInfo.append("Status: ");
		taskInfo.append(task.getStatusName());
		if (task.isCompleted()) {
			taskInfo.append("\n");
			taskInfo.append("Start time: ");
			taskInfo.append(getStringDate(task.getTimeSpan().getStartTime()));
			taskInfo.append("\n");
			taskInfo.append("End time: ");
			taskInfo.append(getStringDate(task.getTimeSpan().getEndTime()));
		}
		return taskInfo.toString();
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

	public DateTime getDateTimeInput(String message)
			throws IllegalArgumentException, ParseException, IOException {
		display(message);
		return new DateTime(format.parse(reader.readLine()));
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

	public void displayProjectList(List<Project> projects) {
		displayList(projects, 0, true);
		displayEmptyLine();
	}

	public void displayProjectDetails(Project project) {
		display(indentStringWithNewLines(getStringProjectDetails(project), 1));
		displayEmptyLine();
	}

	public void displayProjectsWithAvailableTasks(List<Project> projects,
			List<List<Task>> tasks) {
		for (int i = 1; i <= projects.size(); i++) {
			if(tasks.get(i-1).size() == 0)
				continue;
			Object item = i + ". " + projects.get(i - 1).getName();
			String project = item.toString();
			display(project);
			ArrayList<String> tasksInfo = new ArrayList<String>();
			for (int j = 1; j <= tasks.get(i - 1).size(); j++) {
				tasksInfo.add(getStringTask(tasks.get(i - 1).get(j - 1), j));
			}
			displayList(tasksInfo, 1, false);
			displayEmptyLine();
		}
	}

	public void displayTaskList(List<Task> tasks, int tab) {
		ArrayList<String> tasksInfo = new ArrayList<String>();
		for (int i = 1; i <= tasks.size(); i++) {
			tasksInfo.add(getStringTask(tasks.get(i - 1), i));
		}
		displayList(tasksInfo, tab, true);
		displayEmptyLine();
	}

	public void displayTaskDetails(Task task, int index) {
		display(indentStringWithNewLines(getStringTaskDetails(task, index), 1));
		displayEmptyLine();
	}
}