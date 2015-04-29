package taskman.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.memento.ProjectHandlerMemento;
import taskman.model.project.Project;

public class ProjectHandler {

	private ArrayList<Project> projects = new ArrayList<Project>();

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
	 * 
	 * @post The new list of projects contains the given project.
	 */
	private void addProject(Project project) {
		projects.add(project);
	}

	/**
	 * Make a new project and store it to the list of projects.
	 * 
	 * @param name
	 * @param description
	 * @param creationTime
	 * @param dueTime
	 * 
	 * @effect Adds the project to the list of projects.
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalDateException
	 */
	public void addProject(String name, String description,
			DateTime creationTime, DateTime dueTime)
			throws IllegalArgumentException, IllegalDateException {
		Project projectToAdd = new Project(name, description, creationTime,
				dueTime);
		addProject(projectToAdd);
	}

	public void setMemento(ProjectHandlerMemento m) {
		this.projects = m.getProjects();
	}

	public ProjectHandlerMemento createMemento() {
		return new ProjectHandlerMemento(new ArrayList<Project>(projects));
	}
}
