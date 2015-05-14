package taskman.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.project.Project;
import taskman.model.project.task.TaskFactory;

public class ProjectHandler {

	private final TaskFactory taskFactory;

	/**
	 * The constructor of the project handler.
	 * 
	 * @param taskFactory
	 * 
	 * @post The taskFactory will be equal to the given taskFactory.
	 * 
	 * @throws IllegalArgumentException
	 *             The task factory is null.
	 */
	public ProjectHandler(TaskFactory taskFactory)
			throws IllegalArgumentException {
		if (!isValidTaskFactory(taskFactory))
			throw new IllegalArgumentException(
					"The project handler needs a task factory.");
		this.taskFactory = taskFactory;
	}

	/**
	 * Checks if the given taskFactory is valid.
	 * 
	 * @param taskFactory
	 * 
	 * @return Returns true if the taskFactory is different from null.
	 */
	private boolean isValidTaskFactory(TaskFactory taskFactory) {
		if (taskFactory != null)
			return true;
		else
			return false;
	}

	/**
	 * Return a copy of the list of projects.
	 * 
	 * @return Returns a copy of projects.
	 */
	public List<Project> getProjects() {
		return new ArrayList<Project>(projects);
	}

	/**
	 * Adds the given project to the list of projects.
	 * 
	 * @param project
	 *            The project to be added to the list
	 * @post The new list of projects contains the given project.
	 */
	private void addProject(Project project) {
		projects.add(project);
	}

	/**
	 * Make a new project and store it to the list of projects.
	 * 
	 * @param name
	 *            The name of the project to be created
	 * @param description
	 *            The description of the project to be created
	 * @param creationTime
	 *            The creation time of the project to be created
	 * @param dueTime
	 *            The due time of the project to be created
	 * @effect Adds the project to the list of projects.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalDateException
	 */
	public void addProject(String name, String description,
			DateTime creationTime, DateTime dueTime)
			throws IllegalArgumentException, IllegalDateException {
		Project projectToAdd = new Project(name, description, creationTime,
				dueTime, taskFactory);
		addProject(projectToAdd);
	}

	private ArrayList<Project> projects = new ArrayList<Project>();
}
