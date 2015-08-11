package taskman.model.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import taskman.model.memento.DelegatedTaskHandlerMemento;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.TaskFactory;

public class DelegatedTaskHandler {

	private TaskFactory factory;
	private List<DelegatedTask> delegatedTasks;

	/**
	 * The constructor of the delegated task handler class
	 * 
	 * @param factory
	 * 
	 * @throws IllegalArgumentException
	 */
	public DelegatedTaskHandler(TaskFactory factory)
			throws IllegalArgumentException {
		if (factory == null)
			throw new IllegalArgumentException(
					"A delegated task handler needs a task factory.");
		this.factory = factory;
		delegatedTasks = new ArrayList<DelegatedTask>();
	}

	/**
	 * Returns the list of tasks delegated to this branch office
	 * 
	 * @return The list of tasks delegated to this branch office
	 */
	public List<DelegatedTask> getDelegatedTasks() {
		return new ArrayList<DelegatedTask>(delegatedTasks);
	}
	/**
	 * Adds a delegated task to the list in this handler
	 * @param task
	 * 			The task to be added to the list of delegated tasks
	 */
	private void addDelegatedTask(DelegatedTask task) {
		delegatedTasks.add(task);
	}
	/**
	 * Makes a new delegated task
	 * @param id
	 * 			The UUID to be used for this task
	 * @param description
	 * 			The description of the task
	 * @param estimatedDuration
	 * 			The estimated duration for this task
	 * @param acceptableDeviation
	 * 			The acceptable deviation for this task
	 * @param resourceTypes
	 * 			The needed resource types for this task
	 * @param dependenciesFinished
	 * 			The boolean indicating whether the dependencies are finished or not
	 * @param developerAmount
	 * 			An integer indicating the amount of developers needed
	 * @throws IllegalArgumentException
	 * 			Throws an exception if one of the params are an illegal argument
	 */
	public void addDelegatedTask(UUID id, String description,
			int estimatedDuration, int acceptableDeviation,
			Map<ResourceType, Integer> resourceTypes,
			boolean dependenciesFinished, int developerAmount) throws IllegalArgumentException{
		DelegatedTask taskToAdd = factory.makeDelegatedTask(description,
				estimatedDuration, acceptableDeviation, resourceTypes,
				dependenciesFinished, developerAmount);
		addDelegatedTask(taskToAdd);
	}
	/**
	 * Creates the delegated task handler memento
	 * @return
	 * 			A new arraylist containing all the delegated tasks
	 */
	public DelegatedTaskHandlerMemento createMemento() {
		return new DelegatedTaskHandlerMemento(this,
				new ArrayList<DelegatedTask>(delegatedTasks));
	}

	/**
	 * Returns the state of project to that saved in the project memento.
	 * 
	 * @param m
	 */
	public void setMemento(DelegatedTaskHandlerMemento m) {
		delegatedTasks = m.getDelegatedTasks();
	}
}