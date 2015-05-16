package taskman.model.memento;

import java.util.ArrayList;
import java.util.List;

import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class TaskMemento {

	Task task;
	List<Task> dependants;
	String stateName;
	TimeSpan timeSpan;
	Task alternative;

	/**
	 * The constructor of the task memento class
	 * 
	 * @param task
	 *            The object the memento is made for.
	 * @param dependants
	 *            The list of tasks that are the dependants of the task memento
	 * @param stateName
	 *            The state name of the task memento
	 * @param timeSpan
	 *            The timespan to for this task memento
	 * @param alternative
	 *            The alternative task for the task memento
	 */
	public TaskMemento(Task task, List<Task> dependants, String stateName,
			TimeSpan timeSpan, Task alternative) {
		this.task = task;
		this.dependants = dependants;
		this.stateName = stateName;
		this.timeSpan = timeSpan;
		this.alternative = alternative;
	}

	/**
	 * Returns the task the memento is made for.
	 * 
	 * @return Returns the task the memento is made for.
	 */
	public Task getObject() {
		return this.task;
	}

	/**
	 * Will return a copy of the list of dependants for this task memento
	 * 
	 * @return returns a copy of the list of dependants for the task memento
	 */
	public ArrayList<Task> getDependants() {
		return new ArrayList<Task>(dependants);
	}

	/**
	 * Will return the state name of the task memento.
	 * 
	 * @return returns the statename of the task memento.
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * Will return the timespan of the task memento.
	 * 
	 * @return returns the timespan of the task memento.
	 */
	public TimeSpan getTimeSpan() {
		return timeSpan;
	}

	/**
	 * Will return the alternative task of this task memento.
	 * 
	 * @return returns the alternative task of this task memento.
	 */
	public Task getAlternative() {
		return alternative;
	}

}
