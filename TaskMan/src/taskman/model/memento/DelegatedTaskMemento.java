package taskman.model.memento;

import taskman.model.task.DelegatedTask;
import taskman.model.time.TimeSpan;

public class DelegatedTaskMemento {

	DelegatedTask task;
	boolean dependenciesFinished;
	String stateName;
	TimeSpan timeSpan;

	/**
	 * The constructor of the delegated task memento class
	 * 
	 * @param delegatedTask
	 * @param dependenciesFinished
	 * @param name
	 * @param timeSpan
	 */
	public DelegatedTaskMemento(DelegatedTask delegatedTask,
			boolean dependenciesFinished, String name, TimeSpan timeSpan) {
		this.task = delegatedTask;
		this.dependenciesFinished = dependenciesFinished;
		this.stateName = name;
		this.timeSpan = timeSpan;
	}

	/**
	 * Returns the task the memento is made for.
	 * 
	 * @return Returns the task the memento is made for.
	 */
	public DelegatedTask getObject() {
		return this.task;
	}

	/**
	 * Returns the status of the depencies of the delegated task
	 * 
	 * @return Returns the status of the depencies of the delegated task
	 */
	public boolean getDependenciesFinished() {
		return this.dependenciesFinished;
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

}
