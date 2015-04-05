package taskman.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.commandline.Input;
import taskman.view.commandline.Output;

public class View implements IView {

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

	/**
	 * This method will check whether the given date string can be parsed or not
	 * to a DateTime.
	 * 
	 * @param date
	 * 
	 * @return Returns true if it is a correctly spelled date, else it returns
	 *         false.
	 */
	private boolean isValidDateTime(String date) {
		try {
			formatter.parseDateTime(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private boolean isValidInteger(int integer) {
		if (integer == Integer.MIN_VALUE)
			return false;
		else
			return true;
	}

	private boolean isValidYesAnswer(String answer) {
		if (answer.toLowerCase().equals("y")
				|| answer.toLowerCase().equals("yes"))
			return true;
		else
			return false;
	}

	private boolean isValidNoAnswer(String answer) {
		if (answer.toLowerCase().equals("n")
				|| answer.toLowerCase().equals("no"))
			return true;
		else
			return false;
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

	private void display(String message) {
		output.display(message);
		output.displayEmptyLine();
	}

	public void displayInfo(String message) {
		display(message);
	}

	public void displayError(String message) {
		display(message);
	}

	public void displayWelcome() {
		displayInfo("TaskMan V2.0");
		output.displayEmptyLine();
	}

	private void displayMainMenu() {
		output.displayList(menu, 0, false);
		output.displayEmptyLine();
	}

	public int getMainMenuID() {
		displayMainMenu();
		displayInfo("Select an option:");
		int menuId = input.getNumberInput();
		output.displayEmptyLine();

		while (menuId <= 0 || menuId > menu.size()) {
			displayError("Invalid selection!\nSelect an option:");
			menuId = input.getNumberInput();
			output.displayEmptyLine();
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
		displayInfo(question);
		int choice = input.getNumberInput();
		output.displayEmptyLine();

		while (!shouldExit(choice) && !(choice > 0 && choice <= list.size())) {
			displayError("Invalid selection!\n" + question);
			choice = input.getNumberInput();
			output.displayEmptyLine();
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
		displayInfo(output.indentStringWithNewLines(
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
				"Select a project:");
	}

	public void displayTaskDetails(Task task) {
		displayInfo(output.indentStringWithNewLines(
				getStringTaskDetails(task), 1));
		output.displayEmptyLine();
	}

	private void displayTaskList(List<Task> tasks, int tabs) {
		ArrayList<String> tasksInfo = new ArrayList<String>();
		for (int i = 1; i <= tasks.size(); i++) {
			tasksInfo.add(getStringTask(tasks.get(i - 1), i));
		}
		output.displayList(tasksInfo, tabs, true);
		output.displayEmptyLine();
	}

	public int getTaskID(List<Task> tasks) throws ShouldExitException {
		displayTaskList(tasks, 1);
		return getListChoice(tasks, "Select a task:");
	}

	public String getNewProjectName() throws ShouldExitException {
		try {
			displayInfo("Enter the name of the project (or cancel):");
			String name = input.getInput();
			output.displayEmptyLine();
			return name;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public String getNewProjectDescription() throws ShouldExitException {
		try {
			displayInfo("Enter the description of the project (or cancel):");
			String description = input.getInput();
			output.displayEmptyLine();
			return description;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public DateTime getNewProjectDueTime() throws ShouldExitException {
		try {
			displayInfo("Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");
			String date = input.getInput();
			output.displayEmptyLine();

			while (!isValidDateTime(date)) {
				displayError("Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");
				date = input.getInput();
				output.displayEmptyLine();
			}

			DateTime dueTime = formatter.parseDateTime(date);

			return dueTime;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public String getNewTaskDescription() throws ShouldExitException {
		try {
			displayInfo("Enter the description of the task (or cancel):");
			String description = input.getInput();
			output.displayEmptyLine();
			return description;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public int getNewTaskEstimatedDuration() throws ShouldExitException {
		try {
			displayInfo("Enter the estimated duration of the task (or cancel):");
			int estimatedDuration = input.getNumberInput();
			output.displayEmptyLine();

			while (!isValidInteger(estimatedDuration)) {
				displayError("You need to enter an integer.\nEnter the estimated duration of the task (or cancel):");
				estimatedDuration = input.getNumberInput();
				output.displayEmptyLine();
			}
			return estimatedDuration;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public int getNewTaskAcceptableDeviation() throws ShouldExitException {
		try {
			displayInfo("Enter the acceptable deviation of the task (or cancel):");
			int acceptableDeviation = input.getNumberInput();
			output.displayEmptyLine();

			while (!isValidInteger(acceptableDeviation)) {
				displayError("You need to enter an integer.\nEnter the acceptable deviation of the task (or cancel):");
				acceptableDeviation = input.getNumberInput();
				output.displayEmptyLine();
			}
			return acceptableDeviation;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public List<Task> getNewTaskDependencies(List<Task> tasks) throws ShouldExitException {
		try {
			displayInfo("Does this task have dependencies? (Y/N or cancel):");
			String hasDependencies = input.getInput();
			output.displayEmptyLine();

			while (!(isValidYesAnswer(hasDependencies) || isValidNoAnswer(hasDependencies))) {
				displayError("Does this task have dependencies? (Y/N or cancel):");
				hasDependencies = input.getInput();
				output.displayEmptyLine();
			}

			if (isValidYesAnswer(hasDependencies)) {
				List<Task> list = parseDependecies(tasks);
				while(list == null) {
					displayError("Incorrect input.");
					list = parseDependecies(tasks);
				}
				return list;
			} else {
				return new ArrayList<Task>();
			}
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private List<Task> parseDependecies(List<Task> tasks) throws ShouldExitException {
		ArrayList<Task> list = new ArrayList<Task>();
		output.displayEmptyLine();
		displayTaskList(tasks, 0);
		displayInfo("Select the dependencies seperated by a comma (e.g. 1,2,3 and 'cancel' or 0 to return):");
		String dependencies = input.getInput();
		output.displayEmptyLine();

		String dependencie[] = dependencies.split(",");
		for(String i : dependencie) {
			try {
				int j = Integer.parseInt(i);
				if(j > 0 && j <= tasks.size())
					list.add(tasks.get(j - 1));
				else if(j == 0)
					throw new ShouldExitException();
				else
					return null;
				return list;
			} catch (NumberFormatException e2) {
				return null;
			}
		}
		return null;
	}

	public Task getNewTaskAlternativeFor(List<Task> tasks)
			throws ShouldExitException {
		try{
			displayInfo("Is this task an alternative for a failed task? (Y/N or cancel):");
			String hasAlternativeFor = input.getInput();
			output.displayEmptyLine();

			while (!(isValidYesAnswer(hasAlternativeFor) || isValidNoAnswer(hasAlternativeFor))) {
				displayError("Is this task an alternative for a failed task? (Y/N or cancel):");
				hasAlternativeFor = input.getInput();
				output.displayEmptyLine();
			}

			if (isValidYesAnswer(hasAlternativeFor)) {
				output.displayEmptyLine();
				displayTaskList(tasks, 0);
				int taskId = getListChoice(tasks,
						"Select a task for which this task will be the alternative (or cancel):");
				return tasks.get(taskId - 1);
			} else {
				return null;
			}
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

}
