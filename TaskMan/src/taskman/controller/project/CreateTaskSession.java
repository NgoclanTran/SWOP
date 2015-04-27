package taskman.controller.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;
import taskman.view.IView;

public class CreateTaskSession extends Session {
	/**
	 * Creates the create task session using the given UI, ProjectHandler and ResourceHandler.
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
	public CreateTaskSession(IView cli, ProjectHandler ph, ResourceHandler rh) {
		super(cli, ph, rh);
	}

	/**
	 * starts the use case by calling the create task method.
	 */
	public void run() {
		createTask();
	}

	/**
	 * This method will ask the ui to get user input to create the task with
	 * this given input. This is the main method for the execution of this
	 * particular use case.
	 */
	private void createTask() {
		while (true) {
			try {
				Project project = getProject();
				List<Task> tasks = project.getTasks();
				List<Task> failedTasks = getFailedTasks(tasks);

				String description = getDescription();
				int estimatedDuration = getEstimatedDuration();
				int acceptableDeviation = getAcceptableDeviation();
				List<Task> dependencies = new ArrayList<Task>();
				if (tasks.size() > 0)
					dependencies = getDependencies(project.getTasks());
				Task alternativeFor = null;
				if (failedTasks.size() > 0)
					alternativeFor = getAlternativeFor(project.getTasks());
				Map<ResourceType, Integer> resourceTypes = null;
				if (getRH().getResourceTypes().size() > 0)
					resourceTypes = getResourceTypes();

				if (isValidTask(project, description, estimatedDuration,
						acceptableDeviation, dependencies, alternativeFor, resourceTypes))
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
			List<Task> dependencies, Task alternativeFor, Map<ResourceType, Integer> resourceTypes) {
		try {
			project.addTask(description, estimatedDuration,
					acceptableDeviation, dependencies, alternativeFor, resourceTypes);
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
	 * This method will ask the ui to display a list of projects and lets the
	 * user choose one of them.
	 * 
	 * @return The project as chosen by the user in the UI.
	 */
	private Project getProject() throws ShouldExitException {
		List<Project> projects = getPH().getProjects();

		if (projects.size() == 0) {
			getUI().displayError("No projects.");
			throw new ShouldExitException();
		}

		return getUI().getProject(projects);
	}

	/**
	 * This method asks the user to enter the description of the task and
	 * returns it.
	 * 
	 * @return Returns the description of the task that is to be entered.
	 * 
	 * @throws ShouldExitException
	 */
	private String getDescription() throws ShouldExitException {
		return getUI().getNewTaskForm().getNewTaskDescription();
	}

	/**
	 * This method asks the user to enter the estimated duration of the task and
	 * returns it.
	 * 
	 * @return Returns the estimated duration of the task that is to be entered.
	 * 
	 * @throws ShouldExitException
	 */
	private int getEstimatedDuration() throws ShouldExitException {
		return getUI().getNewTaskForm().getNewTaskEstimatedDuration();
	}

	/**
	 * This method asks the user to enter the acceptable deviation of the task
	 * and returns it.
	 * 
	 * @return Returns the acceptable deviation of the task that is to be
	 *         entered.
	 * 
	 * @throws ShouldExitException
	 */
	private int getAcceptableDeviation() throws ShouldExitException {
		return getUI().getNewTaskForm().getNewTaskAcceptableDeviation();
	}

	/**
	 * This method asks the user to select the dependencies and returns it.
	 * 
	 * @param tasks
	 *            The list of all tasks
	 * 
	 * @return Returns a list of dependencies.
	 * 
	 * @throws ShouldExitException
	 */
	private List<Task> getDependencies(List<Task> tasks)
			throws ShouldExitException {
		return getUI().getNewTaskForm().getNewTaskDependencies(tasks);
	}

	/**
	 * This method asks the user to select the task for which this task will be
	 * the alternative and returns it.
	 * 
	 * @param tasks
	 *            The list of all tasks.
	 * 
	 * @return Returns the task for which this task will be the alternative.
	 * 
	 * @throws ShouldExitException
	 */
	private Task getAlternativeFor(List<Task> tasks) throws ShouldExitException {
		return getUI().getNewTaskForm().getNewTaskAlternativeFor(tasks);
	}

	/**
	 * This method asks the user to select the resource types and their amounts
	 * needed for the task and returns it.
	 * 
	 * @return Returns a map of resource types and their amounts.
	 * 
	 * @throws ShouldExitException
	 */
	private Map<ResourceType, Integer> getResourceTypes()
			throws ShouldExitException {
		return getUI().getNewTaskForm().getNewTaskResourceTypes(getRH());
	}

	/**
	 * This method returns the list of failed tasks.
	 * 
	 * @param tasks
	 *            The list of all tasks
	 * 
	 * @return Returns a list of failed tasks.
	 */
	private List<Task> getFailedTasks(List<Task> tasks) {
		ArrayList<Task> failedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.isFailed())
				failedTasks.add(task);
		}
		return failedTasks;
	}

}
