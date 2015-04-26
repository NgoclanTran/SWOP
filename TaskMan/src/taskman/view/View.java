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
import taskman.model.resource.ResourceType;
import taskman.view.commandline.Input;
import taskman.view.commandline.Output;

public class View implements IView {

	protected final Input input;
	protected final Output output;
	protected final DateTimeFormatter formatter = DateTimeFormat
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
	protected boolean isValidDateTime(String date) {
		try {
			formatter.parseDateTime(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	protected boolean isValidInteger(int integer) {
		if (integer == Integer.MIN_VALUE)
			return false;
		else
			return true;
	}

	protected boolean isValidYesAnswer(String answer) {
		if (answer.toLowerCase().equals("y")
				|| answer.toLowerCase().equals("yes"))
			return true;
		else
			return false;
	}

	protected boolean isValidNoAnswer(String answer) {
		if (answer.toLowerCase().equals("n")
				|| answer.toLowerCase().equals("no"))
			return true;
		else
			return false;
	}

	private String getStringDate(DateTime time) {
		return formatter.print(time);
	}

	private String getStringMinutes(int minutes) {
		if (minutes < 0) {
			minutes = -minutes;
		}
		int hours = minutes / 60;
		minutes = minutes % 60;
		int days = hours / 8;
		hours = hours % 8;
		return days + " day(s), " + hours + " hour(s), " + minutes
				+ " minute(s)";
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
			projectDetails.append("\n");
			projectDetails.append("Total delay: ");
			int minutes = project.getTotalDelay();
			projectDetails.append(getStringMinutes(minutes));
			if (minutes < 0)
				projectDetails.append(" early.");
			else
				projectDetails.append(" too late.");
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
		if (task.isCompleted()) {
			taskInfo.append("\n");
			taskInfo.append("Percentage of overdueness: ");
			taskInfo.append(task.getOverduePercentage());
			taskInfo.append(" %");
			if (task.isSeverelyOverdue())
				taskInfo.append(" *");
		}
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
		taskInfo.append("\n");
		taskInfo.append("Acceptable deviation: ");
		taskInfo.append(task.getAcceptableDeviation());
		taskInfo.append(" %");
		if (task.isCompleted()) {
			taskInfo.append("\n");
			taskInfo.append("Start time: ");
			taskInfo.append(getStringDate(task.getTimeSpan().getStartTime()));
			taskInfo.append("\n");
			taskInfo.append("End time: ");
			taskInfo.append(getStringDate(task.getTimeSpan().getEndTime()));
			taskInfo.append("\n");
			taskInfo.append("Total execution time: ");
			taskInfo.append(getStringMinutes(task.getTotalExecutionTime()));
			taskInfo.append("\n");
			taskInfo.append("Percentage of overdueness: ");
			taskInfo.append(task.getOverduePercentage());
			taskInfo.append(" %");
			if (task.isSeverelyOverdue())
				taskInfo.append(" *");
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
	protected int getListChoice(List<?> list, String question)
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

	private void displayProjectList(List<Project> projects, int tabs,
			boolean printReturn) {
		output.displayList(projects, tabs, printReturn);
		output.displayEmptyLine();
	}

	public Project getProject(List<Project> projects)
			throws ShouldExitException {
		displayProjectList(projects, 0, true);
		int projectId = getListChoice(projects, "Select a project:");
		return projects.get(projectId - 1);
	}

	public void displayTaskDetails(Task task) {
		displayInfo(output.indentStringWithNewLines(getStringTaskDetails(task),
				1));
		output.displayEmptyLine();
	}

	protected void displayTaskList(List<Task> tasks, int tabs,
			boolean printReturn) {
		ArrayList<String> tasksInfo = new ArrayList<String>();
		for (int i = 1; i <= tasks.size(); i++) {
			tasksInfo.add(getStringTask(tasks.get(i - 1), i));
		}
		output.displayList(tasksInfo, tabs, printReturn);
		output.displayEmptyLine();
	}

	public Task getTask(List<Task> tasks) throws ShouldExitException {
		displayTaskList(tasks, 1, true);
		int taskId = getListChoice(tasks, "Select a task:");
		return tasks.get(taskId - 1);
	}
	
	protected void displayResourceTypeList(List<ResourceType> resouceTypes, int tabs, boolean printReturn) {
		ArrayList<String> resourceTypeInfo = new ArrayList<String>();
		for (int i = 1; i <= resouceTypes.size(); i++) {
			resourceTypeInfo.add(resouceTypes.get(i - 1).toString());
		}
		output.displayList(resourceTypeInfo, tabs, printReturn);
		output.displayEmptyLine();
	}
	
	public ResourceType getResourceType(List<ResourceType> resourceTypes) throws ShouldExitException {
		displayResourceTypeList(resourceTypes, 1, true);
		int resourceTypeId = getListChoice(resourceTypes, "Select a resource type:");
		return resourceTypes.get(resourceTypeId - 1);
	}

	public ICreateProjectForm getNewProjectForm() {
		return new CreateProjectForm(this);
	}

	public ICreateTaskForm getNewTaskForm() {
		return new CreateTaskForm(this);
	}

	public IUpdateTaskForm getUpdateTaskForm() {
		return new UpdateTaskForm(this);
	}

}
