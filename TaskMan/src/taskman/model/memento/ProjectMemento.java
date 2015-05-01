package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.project.task.Task;

public class ProjectMemento {

	private ArrayList<Task> tasks;
	private String stateName;
	/**
	 * the constructor of the projectmemento class
	 * @param tasks
	 * 			The list of tasks for this project memento
	 * @param stateName
	 * 			The state of the project memento
	 * 
	 */
	public ProjectMemento(ArrayList<Task> tasks, String stateName) {
		this.tasks = tasks;
		this.stateName = stateName;
	}
	/**
	 * Will return the statename of the project memento
	 * @return returns the state name of the project memento
	 */
	public String getStateName() {
		return stateName;
	}
	/**
	 * Will return a copy ofthe list of tasks of the project memento
	 * @return returns a copy ofthe list of tasks of the project memento
	 */
	public ArrayList<Task> getTasks() {
		return new ArrayList<Task>(tasks);
	}

}