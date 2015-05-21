package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.view.IView;

public class CreateTaskSession extends Session {

	private ProjectHandler ph;
	private ResourceHandler rh;

	/**
	 * Creates the create task session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param rh
	 *            The resource handler.
	 * 
	 * @throws IllegalArgumentException
	 *             The given project handler and resource handler need to be valid.
	 */
	public CreateTaskSession(IView cli, ProjectHandler ph, ResourceHandler rh)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The create task controller needs a ProjectHandler");
		if (rh == null)
			throw new IllegalArgumentException(
					"The create task controller needs a ResourceHandler");
		this.ph = ph;
		this.rh = rh;
	}

	/**
	 * starts the use case by calling the create task method.
	 */
	@Override
	public void run() {
		createTask();
	}

	/**
	 * This method will ask the UI to get user input to create the task with
	 * this given input. This is the main method for the execution of this
	 * particular use case.
	 */
	private void createTask() {
		while (true) {
			try {
				Project project = getProject();
				List<NormalTask> tasks = project.getTasks();
				List<NormalTask> failedTasks = getFailedTasks(tasks);

				String description = getUI().getNewTaskForm()
						.getNewTaskDescription();
				int estimatedDuration = getUI().getNewTaskForm()
						.getNewTaskEstimatedDuration();
				int acceptableDeviation = getUI().getNewTaskForm()
						.getNewTaskAcceptableDeviation();
				List<NormalTask> dependencies = new ArrayList<NormalTask>();
				if (tasks.size() > 0)
					dependencies = getUI().getNewTaskForm()
							.getNewTaskDependencies(tasks);
				NormalTask alternativeFor = null;
				if (failedTasks.size() > 0)
					alternativeFor = getUI().getNewTaskForm()
							.getNewTaskAlternativeFor(tasks);
				Map<ResourceType, Integer> resourceTypes = null;
				if (rh.getResourceTypes().size() > 0)
					resourceTypes = getUI().getNewTaskForm()
							.getNewTaskResourceTypes(rh);

				if (isValidTask(project, description, estimatedDuration,
						acceptableDeviation, dependencies, alternativeFor,
						resourceTypes))
					break;

			} catch (ShouldExitException e) {
				return;
			}
		}
	}

	/**
	 * This method will try to make a new task with the given parameters. If the
	 * creation fails it will print the error message and return false.
	 * 
	 * @param project
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 * @param alternativeFor
	 * 
	 * @return Returns true if the creation of the task is successful and false
	 *         if there was an error.
	 */
	private boolean isValidTask(Project project, String description,
			int estimatedDuration, int acceptableDeviation,
			List<NormalTask> dependencies, NormalTask alternativeFor,
			Map<ResourceType, Integer> resourceTypes) {
		try {
			project.addTask(description, estimatedDuration,
					acceptableDeviation, dependencies, alternativeFor,
					resourceTypes);
			getUI().displayInfo("Task created");
			return true;
		} catch (NullPointerException nullEx) {
			getUI().displayError(nullEx.getMessage());
			return false;
		} catch (IllegalArgumentException argEx) {
			getUI().displayError(argEx.getMessage());
			return false;
		} catch (IllegalStateException stateEx) {
			getUI().displayError(stateEx.getMessage());
			return false;
		}
	}

	/**
	 * This method will ask the UI to display a list of projects and lets the
	 * user choose one of them.
	 * 
	 * @return The project as chosen by the user in the UI.
	 */
	private Project getProject() throws ShouldExitException {
		List<Project> projects = ph.getProjects();

		if (projects.size() == 0) {
			getUI().displayError("No projects.");
			throw new ShouldExitException();
		}

		return getUI().getProject(projects);
	}

	/**
	 * This method returns the list of failed tasks.
	 * 
	 * @param tasks
	 *            The list of all tasks
	 * 
	 * @return Returns a list of failed tasks.
	 */
	private List<NormalTask> getFailedTasks(List<NormalTask> tasks) {
		ArrayList<NormalTask> failedTasks = new ArrayList<NormalTask>();
		for (NormalTask task : tasks) {
			if (task.isFailed())
				failedTasks.add(task);
		}
		return failedTasks;
	}

}
