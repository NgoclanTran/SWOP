package taskman.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class View implements UI {

	private final Input input;
	private final Output output;
	private final DateTimeFormatter formatter = DateTimeFormat
			.forPattern("dd-MM-yyyy HH:mm");

	private final List<String> menu = Arrays.asList("Show projects",
			"Create project", "Create task", "Update task", "Quit");

	public View() {
		input = new Input();
		output = new Output();
	}

	private String getStringDate(DateTime time) {
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
	
	private String getStringTaskDetails(Task task) {
		StringBuilder taskInfo = new StringBuilder();
		taskInfo.append("Task:");
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

	public void displayError(String message) {
		output.display(message);
		output.displayEmptyLine();
	}

	public void displayWelcome() {
		output.display("TaskMan V2.0");
		output.displayEmptyLine();
	}

	private void displayMainMenu() {
		output.displayList(menu, 0, false);
		output.displayEmptyLine();
	}

	public int getMainMenuID() {
		displayMainMenu();
		output.display("Select an option:");
		int menuId = input.getNumberInput();
		output.displayEmptyLine();
		while (menuId <= 0 || menuId > menu.size()) {
			output.display("Invalid selection!\nSelect an option:");
			menuId = input.getNumberInput();
		}
		return menuId;
	}

	/**
	 * Returns an integer representing the chosen object from the given list.
	 * 
	 * @param list
	 *            The given list to chose from.
	 * @param question
	 *            The question to accompany the input request.
	 * 
	 * @return Returns the number of the chosen object from the given list.
	 * 
	 * @throws ShouldExitException
	 */
	private int getListChoice(List<?> list, String question)
			throws ShouldExitException {
		output.display(question);
		int choice = input.getNumberInput();
		while (!shouldExit(choice) && !(choice > 0 && choice <= list.size())) {
			output.display("Invalid selection!\n" + question);
			choice = input.getNumberInput();
		}
		if (shouldExit(choice)) {
			throw new ShouldExitException();
		}
		return choice;
	}

	/**
	 * Checks if the user requested to exit.
	 * 
	 * @param index
	 * 
	 * @return Returns true if the index equals the exit value.
	 */
	private boolean shouldExit(int index) {
		return index == 0;
	}

	public void displayProjectDetails(Project project) {
		output.display(output.indentStringWithNewLines(
				getStringProjectDetails(project), 1));
		output.displayEmptyLine();
	}

	private void displayProjectList(List<Project> projects) {
		output.displayList(projects, 0, true);
		output.displayEmptyLine();
	}

	public int getProjectID(List<Project> projects) throws ShouldExitException {
		displayProjectList(projects);
		return getListChoice(projects,
				"Select a project to view the details of:");
	}
	
	public void displayTaskDetails(Task task) {
		output.display(output.indentStringWithNewLines(
				getStringTaskDetails(task), 1));
		output.displayEmptyLine();
	}

	private void displayTaskList(List<Task> tasks) {
		ArrayList<String> tasksInfo = new ArrayList<String>();
		for (int i = 1; i <= tasks.size(); i++) {
			tasksInfo.add(getStringTask(tasks.get(i - 1), i));
		}
		output.displayList(tasksInfo, 1, true);
		output.displayEmptyLine();
	}

	public int getTaskID(List<Task> tasks) throws ShouldExitException {
		displayTaskList(tasks);
		return getListChoice(tasks, "Select a task to view the details of:");
	}

}
