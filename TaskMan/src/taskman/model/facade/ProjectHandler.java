package taskman.model.facade;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.Project;

public class ProjectHandler {
	
	/**
	 * Return a copy of the list of projects.
	 * 
	 * @return	Returns a copy of projects.
	 */
	public List<Project> getProjects() {
		// TODO: implement
		return null;
	}
	
	/**
	 * Make a new project and store it to the list of projects.
	 * 
	 * @param name
	 * @param description
	 * @param creationTime
	 * @param dueTime
	 */
	public void makeProject(String name, String description, DateTime creationTime, DateTime dueTime) {
		// TODO: implement
	}

	private ArrayList<Project> projects = new ArrayList<Project>();
}
