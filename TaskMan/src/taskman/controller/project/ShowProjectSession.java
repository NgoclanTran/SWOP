package taskman.controller.project;

import java.util.List;

import taskman.UI.UI;
import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class ShowProjectSession extends Session {

	/**
	 * Creates the show project session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public ShowProjectSession(UI cli, ProjectHandler ph)
			throws IllegalArgumentException {
		super(cli, ph);
	}

	/**
	 * Starts the use case by calling the show projects method.
	 */
	@Override
	public void run() {
		showProjects();
	}

	/**
	 * This method asks the UI to render the list of all projects. Also it will
	 * ask to make a choice from the list to view the details of the selected
	 * project.
	 */
	private void showProjects() {
		List<Project> projects = getPH().getProjects();

		if (projects.size() == 0) {
			getUI().displayError("No projects.");
			return;
		}

		int projectId;
		try {
			projectId = getUI().getProjectID(projects);
		} catch (ShouldExitException e) {
			return;
		}

		showProjectDetails(projects.get(projectId - 1));
	}

	/**
	 * This method asks the UI to render the details of the given project and a
	 * list of all the tasks within the given project. Also it will ask to make
	 * a choice from the list to view the details of the selected task.
	 * 
	 * @param project
	 *            The project to show the details of.
	 */
	private void showProjectDetails(Project project) {
		List<Task> tasks = project.getTasks();
		getUI().displayProjectDetails(project);

		if (tasks.size() == 0)
			return;


		int taskId;
		try {
			taskId = getUI().getTaskID(tasks);
		} catch (ShouldExitException e) {
			return;
		}

		showTaskDetails(tasks.get(taskId - 1));
	}

	/**
	 * This method asks the UI to render the task details.
	 * 
	 * @param task
	 *            The task to show the details of.
	 * @param index
	 *            The index of the task.
	 */
	private void showTaskDetails(Task task) {
		getUI().displayTaskDetails(task);
	}

}
