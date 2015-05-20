package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.project.Project;
import taskman.model.task.Task;

public class ProjectMemento {

	private Project project;
	private ArrayList<Task> tasks;
	private String stateName;

	/**
	 * The constructor of the project memento class.
	 * 
	 * @param project
	 *            The project the memento is made for.
	 * @param tasks
	 *            The list of tasks for this project memento.
	 * @param stateName
	 *            The state of the project memento.
	 * 
	 */
	public ProjectMemento(Project project, ArrayList<Task> tasks,
			String stateName) {
		this.project = project;
		this.tasks = tasks;
		this.stateName = stateName;
	}

	/**
	 * Returns the project the memento is made for.
	 * 
	 * @return Returns the project the memento is made for.
	 */
	public Project getObject() {
		return this.project;
	}

	/**
	 * Will return the statename of the project memento
	 * 
	 * @return returns the state name of the project memento
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * Will return a copy ofthe list of tasks of the project memento
	 * 
	 * @return returns a copy ofthe list of tasks of the project memento
	 */
	public ArrayList<Task> getTasks() {
		return new ArrayList<Task>(tasks);
	}

}