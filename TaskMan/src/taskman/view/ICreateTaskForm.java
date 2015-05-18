package taskman.view;

import java.util.List;
import java.util.Map;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.ResourceHandler;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task;

public interface ICreateTaskForm {

	/**
	 * This method will ask the user to enter a task description.
	 * 
	 * @return Returns a string with the task description.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	public String getNewTaskDescription() throws ShouldExitException;

	/**
	 * This method will ask the user to enter a task estimated duration in
	 * minutes and returns it.
	 * 
	 * @return Returns a integer with the task estimated duration in minutes.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	public int getNewTaskEstimatedDuration() throws ShouldExitException;

	/**
	 * This method will ask the user to enter a task acceptable deviation in
	 * percents and returns it.
	 * 
	 * @return Returns an integer with the task acceptable deviation in percents.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	public int getNewTaskAcceptableDeviation() throws ShouldExitException;

	/**
	 * This method will ask he user to enter the task dependencies and returns
	 * them as a list of tasks.
	 * 
	 * @return Returns a list of tasks that are dependencies.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	public List<Task> getNewTaskDependencies(List<Task> tasks) throws ShouldExitException;

	/**
	 * This method will ask the user to enter the task for which this task is an
	 * alternative and return it.
	 * 
	 * @param tasks
	 * 
	 * @return Returns a task for which this task is an alternative.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	public Task getNewTaskAlternativeFor(List<Task> tasks) throws ShouldExitException;
	
	/**
	 * This method will ask the user to enter the required resource types and
	 * their amount and return them as a map of resource types and integers.
	 * 
	 * @param rh
	 * 
	 * @return Returns a map of ResourceType and Integer with the required
	 *         resource types and their amount.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	public Map<ResourceType, Integer> getNewTaskResourceTypes(ResourceHandler rh) throws ShouldExitException;

}
