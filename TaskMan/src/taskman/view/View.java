package taskman.view;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.view.commandline.Input;
import taskman.view.commandline.Output;

public class View implements IView {

	protected final Input input;
	protected final Output output;
	protected final DateTimeFormatter formatter = DateTimeFormat
			.forPattern("dd-MM-yyyy HH:mm");

	/**
	 * The constructor of the view object. This object will communicate with the
	 * user by command line. It initializes both the input and output.
	 */
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

	/**
	 * This method will check whether the given string can be parsed or not to
	 * an Integer.
	 * 
	 * @param integer
	 * 
	 * @return Returns true if it is an Integer (except for the MIN_VALUE of
	 *         Integer), else it returns false.
	 */
	protected boolean isValidInteger(int integer) {
		if (integer == Integer.MIN_VALUE)
			return false;
		else
			return true;
	}

	/**
	 * This method will check whether the given string is "y" or "yes" case
	 * insensitive.
	 * 
	 * @param answer
	 * 
	 * @return Returns true if the string is equal to "y" or "yes" (case
	 *         insensitive), else it returns false.
	 */
	protected boolean isValidYesAnswer(String answer) {
		if (answer.toLowerCase().equals("y")
				|| answer.toLowerCase().equals("yes"))
			return true;
		else
			return false;
	}

	/**
	 * This method will check whether the given string is "n" or "no" case
	 * insensitive.
	 * 
	 * @param answer
	 * 
	 * @return Returns true if the string is equal to "n" or "no" (case
	 *         insensitive), else it returns false.
	 */
	protected boolean isValidNoAnswer(String answer) {
		if (answer.toLowerCase().equals("n")
				|| answer.toLowerCase().equals("no"))
			return true;
		else
			return false;
	}

	/**
	 * This method will parse the given date to a string with a specified
	 * format.
	 * 
	 * @param time
	 * 
	 * @return Returns the date as a string.
	 */
	protected String getStringDate(DateTime time) {
		return formatter.print(time);
	}

	/**
	 * This method will parse the given minutes as integer to a string with
	 * days, hours and minutes. A day counts 8 hours and an hour counts 60
	 * minutes.
	 * 
	 * @param minutes
	 * 
	 * @return Returns the minutes as a string of days, hours and minutes.
	 */
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

	/**
	 * This method will generate a string with detailed information about the
	 * given project.
	 * 
	 * @param branchOffice
	 * @param project
	 * 
	 * @return Returns a string with detailed information about the given
	 *         project.
	 */
	private String getStringProjectDetails(Project project) {
		StringBuilder projectDetails = new StringBuilder();
		projectDetails.append(project.getName());
		projectDetails.append(":\n");
		projectDetails.append("\n");
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

	/**
	 * This method will generate a string with basic information about the given
	 * task. The index is fitted in front of the string.
	 * 
	 * @param task
	 * @param index
	 * 
	 * @return Returns a string with basic information about the given task.
	 */
	private String getStringTask(Task task, int index) {
		StringBuilder taskInfo = new StringBuilder();
		taskInfo.append("Task ");
		taskInfo.append(index);
		taskInfo.append(": ");
		taskInfo.append(task.getStatusName());
		taskInfo.append("\n");
		taskInfo.append("Responsible branch office: ");
		taskInfo.append(task.getResponsibleBranchOffice());
		taskInfo.append("\n");
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

	/**
	 * This method will generate a string with detailed information about the
	 * given task.
	 * 
	 * @param task
	 * 
	 * @return Returns a string with detailed information about the given task.
	 */
	private String getStringTaskDetails(Task task) {
		StringBuilder taskInfo = new StringBuilder();
		taskInfo.append("Task:");
		taskInfo.append("\n");
		taskInfo.append("Description: ");
		taskInfo.append(task.getDescription());
		taskInfo.append("\n");
		taskInfo.append("Status: ");
		taskInfo.append(task.getStatusName());
		taskInfo.append("\n");
		taskInfo.append("Responsible branch office: ");
		taskInfo.append(task.getResponsibleBranchOffice());
		taskInfo.append("\n");
		taskInfo.append("Estimated duration: ");
		taskInfo.append(getStringMinutes(task.getEstimatedDuration()));
		taskInfo.append("\n");
		taskInfo.append("Acceptable deviation: ");
		taskInfo.append(task.getAcceptableDeviation());
		taskInfo.append(" %");
		if (!task.getRequiredDevelopers().isEmpty()) {
			taskInfo.append("\n");
			taskInfo.append("Assigned developers: ");
			taskInfo.append(task.getRequiredDevelopers().toString());
		}
		if (!task.getRequiredResourceTypes().isEmpty()) {
			taskInfo.append("\n");
			taskInfo.append("Required resource types: ");
			taskInfo.append(task.getRequiredResourceTypes().toString());
		}
		if (task.isPlanned()) {
			taskInfo.append("\n");
			taskInfo.append("Planned start time: ");
			taskInfo.append(getStringDate(task.getRequiredDevelopers().get(0)
					.getReservations().get(0).getTimeSpan().getStartTime()));
			taskInfo.append("\n");
			taskInfo.append("Planned end time: ");
			taskInfo.append(getStringDate(task.getRequiredDevelopers().get(0)
					.getReservations().get(0).getTimeSpan().getEndTime()));
		}
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

	/**
	 * This method will pass the given string to the output.
	 * 
	 * @param message
	 * 
	 * @throws IllegalArgumentException
	 *             The message needs to be a valid string.
	 */
	private void display(String message) throws IllegalArgumentException {
		output.display(message);
		output.displayEmptyLine();
	}

	/**
	 * This method will print an info string on the command line.
	 * 
	 * @param message
	 */
	@Override
	public void displayInfo(String message) {
		try {
			display(message);
		} catch (IllegalArgumentException ex) {
			displayError(ex.getStackTrace().toString());
		}
	}

	/**
	 * This method will print an error string on the command line.
	 * 
	 * @param message
	 */
	@Override
	public void displayError(String message) {
		try {
			display(message);
		} catch (IllegalArgumentException ex) {
			displayError(ex.getStackTrace().toString());
		}
	}

	/**
	 * This method will print a welcome message.
	 */
	@Override
	public void displayWelcome() {
		displayInfo("TaskMan V2.0");
		output.displayEmptyLine();
	}

	/**
	 * This method will print the given main menu and return the selection as an
	 * integer.
	 * 
	 * @param menu
	 * 
	 * @return Returns the index of the menu the users choose.
	 * 
	 * @throws IllegalArgumentException
	 *             The menu needs to be a list with at least 1 item.
	 */
	@Override
	public int getMainMenuID(List<String> menu) throws IllegalArgumentException {
		try {
			displayMainMenu(menu);
			displayInfo("Select an option:");
			int menuId = input.getNumberInput();
			output.displayEmptyLine();

			while (menuId <= 0 || menuId > menu.size()) {
				displayError("Invalid selection!\nSelect an option:");
				menuId = input.getNumberInput();
				output.displayEmptyLine();
			}

			return menuId;
		} catch (ShouldExitException ex) {
			output.displayEmptyLine();
			return getMainMenuID(menu);
		}
	}

	/**
	 * This method will display the given menu as main menu.
	 * 
	 * @param menu
	 * 
	 * @throws IllegalArgumentException
	 *             The menu needs to be a list with at least 1 item.
	 */
	private void displayMainMenu(List<String> menu)
			throws IllegalArgumentException {
		output.displayList(menu, 0, false);
		output.displayEmptyLine();
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
	 *             The choice of the user is to stop.
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

	/**
	 * This method will print the given list of projects to the command line and
	 * will ask the user to choose one. It will return the chosen project.
	 * 
	 * @param projects
	 * 
	 * @return The chosen project.
	 * 
	 * @throws ShouldExitException
	 *             The choice of the user is to stop.
	 */
	@Override
	public Project getProject(List<Project> projects)
			throws ShouldExitException {
		displayProjectList(projects, 0, true);
		int projectId = getListChoice(projects, "Select a project:");
		return projects.get(projectId - 1);
	}

	/**
	 * This method will print the project details of a given project to the
	 * command line.
	 * 
	 * @param branchOffice
	 * @param project
	 */
	@Override
	public void displayProjectDetails(Project project) {
		displayInfo(output.indentStringWithNewLines(
				getStringProjectDetails(project), 1));
		output.displayEmptyLine();
	}

	/**
	 * This method will print the given list of projects. The tabs integer is to
	 * enter the amount of indentation. The printReturn boolean is whether or
	 * not to print "0. Return" in front of the list.
	 * 
	 * @param projects
	 * @param tabs
	 * @param printReturn
	 */
	protected void displayProjectList(List<Project> projects, int tabs,
			boolean printReturn) {
		output.displayList(projects, tabs, printReturn);
		output.displayEmptyLine();
	}

	/**
	 * This method will print the given list of tasks to the command line and
	 * will ask the user to choose one. It will return the chosen task.
	 * 
	 * @param tasks
	 * 
	 * @return The chosen task.
	 * 
	 * @throws ShouldExitException
	 *             The choice of the user is to stop.
	 */
	@Override
	public Task getTask(List<Task> tasks) throws ShouldExitException {
		displayTaskList(tasks, 1, true);
		int taskId = getListChoice(tasks, "Select a task:");
		return tasks.get(taskId - 1);
	}

	/**
	 * This method will print the task details of a given task to the command
	 * line.
	 * 
	 * @param task
	 */
	@Override
	public void displayTaskDetails(Task task) {
		displayInfo(output.indentStringWithNewLines(getStringTaskDetails(task),
				1));
		output.displayEmptyLine();
	}

	/**
	 * This method will print the given list of tasks. The tabs integer is to
	 * enter the amount of indentation. The printReturn boolean is whether or
	 * not to print "0. Return" in front of the list.
	 * 
	 * @param tasks
	 * @param tabs
	 * @param printReturn
	 */
	protected void displayTaskList(List<Task> tasks, int tabs,
			boolean printReturn) {
		ArrayList<String> tasksInfo = new ArrayList<String>();
		for (int i = 1; i <= tasks.size(); i++) {
			tasksInfo.add(getStringTask(tasks.get(i - 1), i));
		}
		output.displayList(tasksInfo, tabs, printReturn);
		output.displayEmptyLine();
	}

	/**
	 * This method will return a new login form.
	 */
	public IShowAllBranchesForm getShowAllBranchesForm() {
		return new ShowAllBranchesForm(this);
	}

	/**
	 * This method will return a new create project form.
	 */
	@Override
	public ICreateProjectForm getNewProjectForm() {
		return new CreateProjectForm(this);
	}

	/**
	 * This method will return a new create task form.
	 */
	@Override
	public ICreateTaskForm getNewTaskForm() {
		return new CreateTaskForm(this);
	}

	/**
	 * This method will return a new update task form.
	 */
	@Override
	public IUpdateTaskForm getUpdateTaskForm() {
		return new UpdateTaskForm(this);
	}

	/**
	 * This method will return a new advance time form.
	 */
	@Override
	public IAdvanceTimeForm getAdvanceTimeForm() {
		return new AdvanceTimeForm(this);
	}

	/**
	 * This method will return a new plan task form.
	 */
	@Override
	public IPlanTaskForm getPlanTaskForm() {
		return new PlanTaskForm(this);
	}

	/**
	 * This method will return a new resolve conflict form.
	 */
	@Override
	public IResolveConflictForm getResolveConflictForm() {
		return new ResolveConflictForm(this);
	}

	/**
	 * This method will return a new login form.
	 */
	@Override
	public ILoginForm getLoginForm() {
		return new LoginForm(this);
	}

}
