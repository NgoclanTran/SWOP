package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.view.IView;

public class ShowProjectsSession extends Session {

	private ProjectHandler ph;

	/**
	 * Constructor of the show projects controller.
	 * 
	 * @param cli
	 * @param ph
	 * 
	 * @throws IllegalArgumentException
	 */
	public ShowProjectsSession(IView cli, ProjectHandler ph)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The show projects controller needs a project handler");
		this.ph = ph;
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
		List<Project> projects = ph.getProjects();

		if (projects.size() == 0) {
			getUI().displayError("No projects.");
			return;
		}

		Project project;
		try {
			project = getUI().getProject(projects);
		} catch (ShouldExitException e) {
			return;
		}

		showProjectDetails(project);
	}

	/**
	 * This method asks the UI to render the details of the given project and a
	 * list of all the tasks within the given project. Also it will ask to make
	 * a choice from the list to view the details of the selected task.
	 * 
	 * @param branchOffice
	 * @param project
	 *            The project to show the details of.
	 */
	private void showProjectDetails(Project project) {
		List<Task> tasks = new ArrayList<Task>(project.getTasks());
		getUI().displayProjectDetails(project);

		if (tasks.size() == 0)
			return;

		Task task;
		try {
			task = getUI().getTask(tasks);
		} catch (ShouldExitException e) {
			return;
		}

		// TODO: Add delegated tasks

		showTaskDetails(task);
	}

	/**
	 * This method asks the UI to render the task details.
	 * 
	 * @param task
	 *            The task to show the details of.
	 */
	private void showTaskDetails(Task task) {
		getUI().displayTaskDetails(task);
	}

}
