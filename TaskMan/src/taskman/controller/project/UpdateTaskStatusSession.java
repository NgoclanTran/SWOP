package taskman.controller.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;

public class UpdateTaskStatusSession extends Session {

	/**
	 * Creates the update task session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public UpdateTaskStatusSession(IView cli, ProjectHandler ph)
			throws IllegalArgumentException {
		super(cli, ph);
	}

	/**
	 * Starts the use case by calling the update task method.
	 */
	@Override
	public void run() {
		showProjectsAndAvailableTasks();
	}

	private void showProjectsAndAvailableTasks() {
		List<Project> projects = getPH().getProjects();
		List<List<Task>> availableTasksList = getAvailableTasksAllProjects(projects);

		if (availableTasksList.size() == 0) {
			getUI().displayError("No available tasks.");
			return;
		}
		
		Project project;
		try {
			project = getUI().getProjectIDWithAvailableTasks(projects, availableTasksList);
		} catch (ShouldExitException e) {
			return;
		}
		
		showAvailableTasks(project);
	}

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

	private List<Task> getAvailableTasksProject(Project project) {
		List<Task> availableTasks = new ArrayList<Task>();
		List<Task> tasks = project.getTasks();
		for (Task t : tasks) {
			if (t.isAvailable())
				availableTasks.add(t);
		}
		return availableTasks;
	}
	
	private void showAvailableTasks(Project project) {
		List<Task> tasks = project.getTasks();
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
	
	private boolean getFailed() {
		return getUI().getUpdateTaskFailed();
	}
	
	private DateTime getStartTime() {
		return getUI().getUpdateTaskStartTime();
	}
	
	private DateTime getEndTime() {
		return getUI().getUpdateTaskStopTime();
	}
	
	private boolean isValidUpdateTask(Task task, boolean isFailed, DateTime startTime, DateTime endTime) {
		try {
			task.addTimeSpan(isFailed, startTime, endTime);;
			getUI().displayInfo("Task updated");
			return true;
		} catch (IllegalArgumentException argEx) {
			getUI().displayError(argEx.getMessage());
			return false;
		} catch (NullPointerException nullEx) {
			getUI().displayError(nullEx.getMessage());
			return false;
		}
	}
}
