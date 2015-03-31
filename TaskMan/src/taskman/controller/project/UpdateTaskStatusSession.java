package taskman.controller.project;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.UI.UI;
import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

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
	public UpdateTaskStatusSession(UI cli, ProjectHandler ph)
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
		List<List<Task>> availableTasksList = new ArrayList<>();
		List<Task> availableTasks = null;
		for (Project p : projects) {
			availableTasks = new ArrayList<Task>();
			List<Task> tasks = p.getTasks();
			for (Task t : tasks) {
				if (t.isAvailable())
					availableTasks.add(t);
			}
			availableTasksList.add(availableTasks);
		}

		getUI().displayProjectsWithAvailableTasks(projects, availableTasksList);

		int projectId;
		try {
			projectId = getListChoice(projects, "Select a project: ");
		} catch (ShouldExitException e) {
			return;
		}
		showAvailableTasks(projects.get(projectId - 1));
	}

	private void showAvailableTasks(Project project) {
		List<Task> availableTasks = new ArrayList<Task>();
		List<Task> tasks = project.getTasks();
		for (Task t : tasks) {
			if (t.isAvailable())
				availableTasks.add(t);
		}

		if (availableTasks.isEmpty()) {
			getUI().display(
					"This project has no available tasks. Please select another project.\n");
			showProjectsAndAvailableTasks();
		}

		getUI().displayAvailableTaskList(availableTasks);

		int taskId;
		try {
			taskId = getListChoice(availableTasks,
					"Select a task to update it's status: ");
		} catch (ShouldExitException e) {
			return;
		}
		updateTask(availableTasks.get(taskId - 1));
	}

	private void updateTask(Task task) {
		DateTime startTime = null, endTime = null;
		String failedString = null;
		boolean failed = false;
		while (failedString == null) {
			try {
				failedString = getUI().getTextInput(
						"Did the task fail? (yes/no)");
				if (!failedString.toLowerCase().equals("yes")
						&& !failedString.toLowerCase().equals("no")) {
					failedString = null;
					getUI().display("Please enter 'yes' or 'no'.\n");
					continue;
				}
			} catch (Exception e) {
			}
			if (failedString.toLowerCase().equals("yes")) {
				failed = true;
			} else {
				failed = false;
			}
		}
		DateTime start = null;
		while (startTime == null) {
			try {
				start = getUI()
						.getDateTimeInput(
								"Please enter the start time using the following format: yyyy-MM-dd HH:mm.\n");
			} catch (IllegalArgumentException | IOException | ParseException e) {
				getUI().display(
						"The given start time did not follow the specified format.");
			}
			startTime = start;
		}
		DateTime end = null;
		while (endTime == null) {
			try {
				end = getUI()
						.getDateTimeInput(
								"Please enter the end time using the following format: yyyy-MM-dd HH:mm.\n");
			} catch (IllegalArgumentException | IOException | ParseException e) {
				getUI().display(
						"The given end time did not follow the specified format.");
			}
			endTime = end;
		}
		task.addTimeSpan(failed, startTime, endTime);
	}
}
