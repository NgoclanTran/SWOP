
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
		getUI().displayProjectList(projects);

		int projectId;
		try {
			projectId = getListChoice(projects,
					"Select a project to view the details: ");
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
		getUI().displayTaskList(tasks);

		int taskId;
		try {
			taskId = getListChoice(tasks, "Select a task to view the details: ");
		} catch (ShouldExitException e) {
			return;
		}

		showTaskDetails(tasks.get(taskId - 1), taskId);
	}

	/**
	 * This method asks the UI to render the task details.
	 * 
	 * @param task
	 *            The task to show the details of.
	 * @param index
	 *            The index of the task.
	 */
	private void showTaskDetails(Task task, int index) {
		getUI().displayTaskDetails(task, index);
	}

}
