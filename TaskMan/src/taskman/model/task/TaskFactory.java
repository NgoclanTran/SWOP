package taskman.model.task;

import java.util.List;
import java.util.Map;

import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class TaskFactory {

	private Clock clock;

	/**
	 * Creates the task factory.
	 * 
	 * @param clock
	 *            The system clock.
	 * 
	 * @throws IllegalArgumentException
	 *             The clock needs to be valid.
	 */
	public TaskFactory(Clock clock) {
		if (!isValidClock(clock))
			throw new IllegalArgumentException(
					"The create project controller needs a clock");
		this.clock = clock;
	}

	/**
	 * Checks if the given clock is valid.
	 * 
	 * @param clock
	 * 
	 * @return Returns true if the clock is different from null.
	 */
	private boolean isValidClock(Clock clock) {
		if (clock != null)
			return true;
		else
			return false;
	}

	/**
	 * This method will make a new task, attach the task to the clock and return the task.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 * @param alternativeFor
	 * @param resourceTypes
	 * 
	 * @return Returns the new task made with the given parameters.
	 * 
	 * @throws IllegalArgumentException
	 */
	public Task makeTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies,
			Task alternativeFor, Map<ResourceType, Integer> resourceTypes)
			throws IllegalArgumentException {
		Task task =  new Task(clock, description, estimatedDuration, acceptableDeviation,
				dependencies, alternativeFor, resourceTypes);
		//TODO: Moet deze attach hier gebeuren of in taak zelf?
		clock.attach(task);
		return task;
	}

}
