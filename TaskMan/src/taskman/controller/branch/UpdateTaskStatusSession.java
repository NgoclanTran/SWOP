package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.IllegalDateException;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.task.NormalTask;
import taskman.model.task.Reservation;
import taskman.model.task.Task;
import taskman.model.user.Developer;
import taskman.view.IView;

public class UpdateTaskStatusSession extends Session {

	private ProjectHandler ph;
	private Developer developer;

	/**
	 * Creates the update task session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public UpdateTaskStatusSession(IView cli, ProjectHandler ph,
			Developer developer) throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The update task status controller needs a ProjectHandler");
		if (developer == null)
			throw new IllegalArgumentException(
					"The update task status controller needs a developer.");
		this.ph = ph;
		this.developer = developer;
	}

	/**
	 * Starts the use case by asking the user to select a developer, a project
	 * and task that the selected developer can change the status of and finally
	 * change the status of the selected task.
	 */
	@Override
	public void run() {
		showProjectsAndAvailableTasks(developer);
	}

	/**
	 * This method asks the UI to render the list of all projects and the
	 * available tasks per project. Also it will ask to make a choice from the
	 * projects to select a task to update.
	 * 
	 * @param developer
	 */
	private void showProjectsAndAvailableTasks(Developer developer) {
		List<Project> projects = ph.getProjects();
		List<List<Task>> availableTasksList = getAvailableTasksAllProjects(
				developer, projects);

		if (availableTasksList.size() == 0) {
			getUI().displayError("No available tasks.");
			return;
		}

		Project project;
		try {
			project = getUI().getUpdateTaskForm().getProjectWithAvailableTasks(
					projects, availableTasksList);
		} catch (ShouldExitException e) {
			return;
		}

		showAvailableTasks(developer, project);
	}

	/**
	 * This method returns a list of a list of all available tasks of all
	 * projects. Per project there will be a list of available tasks that will
	 * be added to the main list.
	 * 
	 * @param developer
	 * @param projects
	 * 
	 * @return Returns a list of a list of all available tasks.
	 */
	private List<List<Task>> getAvailableTasksAllProjects(Developer developer,
			List<Project> projects) {
		List<List<Task>> availableTasksList = new ArrayList<>();
		List<Task> availableTasks = null;

		boolean noAvailableTasks = true;

		for (Project project : projects) {
			availableTasks = getAvailableTasksProject(developer, project);
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
	 * This method returns a list of all available tasks for a specific
	 * developer within a given project.
	 * 
	 * @param developer
	 * @param project
	 * 
	 * @return Returns a list of all available tasks for a specific developer
	 *         within a given project.
	 */
	private List<Task> getAvailableTasksProject(Developer developer,
			Project project) {
		List<Task> availableTasks = new ArrayList<Task>();
		List<NormalTask> tasks = project.getTasks();
		for (Task t : tasks) {
			if ((t.isAvailable() || t.isExecuting())
					&& t.getRequiredDevelopers().contains(developer))
				availableTasks.add(t);
		}
		//TODO: Add delegated tasks?
		return availableTasks;
	}

	/**
	 * This method asks the UI to render a list of all the available tasks of a
	 * given project. Also it will ask to make a choice from the list to update
	 * the details of the selected task.
	 * 
	 * @param developer
	 * @param project
	 */
	private void showAvailableTasks(Developer developer, Project project) {
		List<Task> tasks = getAvailableTasksProject(developer, project);
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
	 * This method loops over the update form of a task until the user enters
	 * all details correctly or decides to cancel the update process.
	 * 
	 * @param task
	 */
	private void updateTask(Task task) {
		while (true) {
			try {
				if (task.isAvailable()) {
					task.executeTask();
					getUI().displayInfo("The selected task is now executing.");
					break;
				}

				boolean isFailed = getUI().getUpdateTaskForm()
						.getUpdateTaskFailed();
				DateTime startTime = getStartTime(task);
				DateTime endTime = getUI().getUpdateTaskForm()
						.getUpdateTaskStopTime(startTime);

				if (isValidUpdateTask(task, isFailed, startTime, endTime))
					break;

			} catch (ShouldExitException e) {
				return;
			}
		}
	}

	/**
	 * This method will get the first reservation time for the given task and
	 * return it.
	 * 
	 * @param task
	 * 
	 * @return Returns the start time of the specified task that is to be
	 *         entered.
	 */
	private DateTime getStartTime(Task task) {
		DateTime startTime = null;
		for (Developer developer : task.getRequiredDevelopers()) {
			for (Reservation reservation : developer.getReservations()) {
				if (reservation.getTask().equals(task)) {
					if (startTime == null
							|| reservation.getTimeSpan().getStartTime()
									.isBefore(startTime)) {
						startTime = reservation.getTimeSpan().getStartTime();
					}
				}
			}
		}
		return startTime;
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
		} catch (IllegalDateException DateEx) {
			getUI().displayError(DateEx.getMessage());
			return false;
		}
	}
}
