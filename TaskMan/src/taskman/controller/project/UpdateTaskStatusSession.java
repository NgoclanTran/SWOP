package taskman.controller.project;

import java.util.ArrayList;
import java.util.List;

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
		updateTask();
	}

	private void updateTask() {
		List<Project> projects = getPH().getProjects();
		List<List<Task>> availableList = new ArrayList<>();
		List<Task> availableTasks;
		for (Project p : projects) {
			availableTasks = new ArrayList<Task>();
			List<Task> tasks = p.getTasks();
			for (Task t : tasks) {
				if (t.isAvailable())
					availableTasks.add(t);
			}
		}
		
		int projectId;
		try {
			projectId = getListChoice(availableTasks,
					"Select a project to update it's task: ");
		} catch (ShouldExitException e) {
			return;
		}

	}

}
