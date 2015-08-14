package taskman.model.company;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;
import taskman.model.user.ProjectManager;
import taskman.model.user.User;

public class UserHandler {

	/**
	 * Gives a copy of a list of the project managers
	 * 
	 * @return the list of project managers
	 */
	public List<ProjectManager> getProjectManagers() {
		return new ArrayList<ProjectManager>(projectManagers);
	}

	/**
	 * Adds a project manager to the list of project managers
	 * 
	 * @param name
	 *            The name of the project manager to be added
	 * 
	 * @post the project manager will be added to the list
	 */
	public void addProjectManager(String name) {
		if (name == null)
			throw new IllegalArgumentException("The name cannot be null.");
		ProjectManager projectManager = new ProjectManager(name);
		addProjectManager(projectManager);
	}

	/**
	 * Will add a project manager to the list of project managers
	 * 
	 * @param projectManager
	 *            The project manager to be added to the list
	 * @post the project manager will be added to the list
	 */
	private void addProjectManager(ProjectManager projectManager) {
		projectManagers.add(projectManager);
	}

	private ArrayList<ProjectManager> projectManagers = new ArrayList<ProjectManager>();;

	/**
	 * Gives a copy of a list of the developers
	 * 
	 * @return the list of developers
	 */
	public List<Developer> getDevelopers() {
		return new ArrayList<Developer>(developers);
	}

	/**
	 * Will return the list of available developers for a given time span
	 * 
	 * @param timeSpan
	 *            The timespan for which will be checked if the developers are
	 *            available or not
	 * @return the list of the available developers for the given timespan
	 */
	public List<Developer> getAvailableDevelopers(TimeSpan timeSpan) {
		if (timeSpan == null)
			throw new IllegalArgumentException("The timeSpan cannot be null.");
		List<Developer> availableDevelopers = new ArrayList<Developer>();
		for (Developer developer : developers) {
			if (developer.isAvailableAt(timeSpan)) {
				availableDevelopers.add(developer);
			}
		}
		return availableDevelopers;
	}

	/**
	 * Adds a developer to the list of developers
	 * 
	 * @param name
	 *            The name of the developer to be added
	 * @post the developer will be added to the list
	 */
	public void addDeveloper(String name) {
		if (name == null)
			throw new IllegalArgumentException("The name cannot be null.");
		Developer developer = new Developer(name, new LocalTime(8, 0),
				new LocalTime(17, 0));
		addDeveloper(developer);
	}

	/**
	 * Will add a developer to the list of developers
	 * 
	 * @param developer
	 *            The developer to be added to the list
	 * @post the developer will be added to the list of developers
	 */
	private void addDeveloper(Developer developer) {
		developers.add(developer);
	}

	private List<Developer> developers = new ArrayList<Developer>();

	/**
	 * Returns a list of all users together.
	 * 
	 * @return Returns a list of all users together.
	 */
	public List<User> getUsers() {
		ArrayList<User> users = new ArrayList<User>(projectManagers);
		users.addAll(developers);
		return users;
	}
}
