package taskman.controller.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.IllegalDateException;
import taskman.exceptions.ShouldExitException;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;

public class UpdateTaskStatusSession extends Session {

	/**
	 * Creates the update task session using the given UI, ProjectHandler and ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param rh
	 *            The resource handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public UpdateTaskStatusSession(IView cli, ProjectHandler ph, ResourceHandler rh)
			throws IllegalArgumentException {
		super(cli, ph, rh);
	}

	/**
	 * Starts the use case by calling the update task method.
	 */
	@Override
	public void run() {
		showProjectsAndAvailableTasks();
	}

	/**
	 * This method asks the UI to render the list of all projects and the
	 * available tasks per project. Also it will ask to make a choice from the
	 * projects to select a task to update.
	 */
	private void showProjectsAndAvailableTasks() {
		List<Project> projects = getPH().getProjects();
		List<List<Task>> availableTasksList = getAvailableTasksAllProjects(projects);

		if (availableTasksList.size() == 0) {
			getUI().displayError("No available tasks.");
			return;
		}

		Project project;
		try {
			project = getUI().getUpdateTaskForm().getProjectWithAvailableTasks(projects,
					availableTasksList);
		} catch (ShouldExitException e) {
			return;
		}

		showAvailableTasks(project);
	}

	/**
	 * This method returns a list of a list of all available tasks of all
	 * projects. Per project there will be a list of available tasks that will
	 * be added to the main list.
	 * 
	 * @param projects
	 * 
	 * @return Returns a list of a list of all available tasks.
	 */
	private List<List<Task>> getAvailableTasksAllProjects(List<Project> projects) {
		List<List<Task>> availableTasksList = new ArrayList<>();
		List<Task> availableTasks = null;

		boolean noAvailableTasks = true;

		for (Project p : projects) {
			availableTasks = getAvailableTasksProject(p);
			availableTasksList.add(availableTasks);

			if (noAvailableTasks && availableTasks.size() > 0)
				noAvailableTasks = false;
		}

		if (noAvailableTasks)
			return new ArrayList<>();
		else
			return availableTasksList;
	}

	/**
	 * This method returns a list of all available tasks within a given project.
	 * 
	 * @param project
	 * 
	 * @return Returns a list of all available tasks within a given project.
	 */
	private List<Task> getAvailableTasksProject(Project project) {
		List<Task> availableTasks = new ArrayList<Task>();
		List<Task> tasks = project.getTasks();
		for (Task t : tasks) {
			if (t.isAvailable())
				availableTasks.add(t);
		}
		return availableTasks;
	}

	/**
	 * This method asks the UI to render a list of all the available tasks of a
	 * given project. Also it will ask to make a choice from the list to update
	 * the details of the selected task.
	 * 
	 * @param project
	 */
	private void showAvailableTasks(Project project) {
		List<Task> tasks = getAvailableTasksProject(project);
		getUI().displayProjectDetails(project);

		if (tasks.size() == 0)
			return;

		Task task;
		try {
			task = getUI().getTask(tasks);
		} catch (ShouldExitException e) {
			return;
		}

		updateTask(task);
	}

	/**
	 * This method loops over the update form of a task until the user neters
	 * all details correctly or decides to cancel the update process.
	 * 
	 * @param task
	 */
	private void updateTask(Task task) {
		while (true) {
			try {
				boolean isFailed = getFailed();
				DateTime startTime = getStartTime();
				DateTime endTime = getEndTime();

				if (isValidUpdateTask(task, isFailed, startTime, endTime))
					break;

			} catch (ShouldExitException e) {
				return;
			}
		}
	}

	/**
	 * This method asks the user to enter whether or not the specified task has
	 * failed and returns it as a boolean.
	 * 
	 * @return Returns true if the user enters 'yes' and false if the user
	 *         enters 'no'.
	 */
	private boolean getFailed() {
		return getUI().getUpdateTaskForm().getUpdateTaskFailed();
	}

	/**
	 * This method asks the user to enter the start time of the specified task
	 * and returns it. It will loop over the question until the date is
	 * correctly given or the user decides to cancel.
	 * 
	 * @return Returns the start time of the specified task that is to be
	 *         entered.
	 */
	private DateTime getStartTime() {
		return getUI().getUpdateTaskForm().getUpdateTaskStartTime();
	}

	/**
	 * This method asks the user to enter the end time of the specified task and
	 * returns it. It will loop over the question until the date is correctly
	 * given or the user decides to cancel.
	 * 
	 * @return Returns the end time of the specified task that is to be entered.
	 */
	private DateTime getEndTime() {
		return getUI().getUpdateTaskForm().getUpdateTaskStopTime();
	}

	/**
	 * This method will try to update the given task with the given parameters.
	 * If the update process fails it will print the error message and return
	 * false.
	 * 
	 * @param task
	 * @param isFailed
	 * @param startTime
	 * @param endTime
	 * 
	 * @return Returns true if the update process of the task is successful and
	 *         false if there was an error.
	 */
	private boolean isValidUpdateTask(Task task, boolean isFailed,
			DateTime startTime, DateTime endTime) {
		try {
			task.addTimeSpan(isFailed, startTime, endTime);
			;
			getUI().displayInfo("Task updated.");
			return true;
		} catch (IllegalArgumentException argEx) {
			getUI().displayError(argEx.getMessage());
			return false;
		} catch (NullPointerException nullEx) {
			getUI().displayError(nullEx.getMessage());
			return false;
		} catch( IllegalDateException DateEx){
			getUI().displayError(DateEx.getMessage());
			return false;
		}
	}
}
