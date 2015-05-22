package taskman.model.project;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;

interface State {

	/**
	 * Returns the name of the state.
	 * 
	 * @return Returns the name of the state.
	 */
	abstract String getName();

	/**
	 * Returns whether or not the state is finished.
	 * 
	 * @return Returns whether or not the state is finished.
	 */
	abstract boolean isFinished();

	/**
	 * Adds a new task.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 * 
	 * @throws IllegalStateException
	 */
	abstract void addTask(Project project, String description,
			int estimatedDuration, int acceptableDeviation,
			List<NormalTask> dependencies, NormalTask alternativeFor,
			Map<ResourceType, Integer> resourceTypes, int developerAmount)
			throws IllegalStateException;

	/**
	 * Updates the state.
	 * 
	 * @throws IllegalStateException
	 */
	abstract void updateProjectState(Project project)
			throws IllegalStateException;

	/**
	 * Returns the estimated finish time of the project.
	 * 
	 * @return Returns the estimated finish time of the project.
	 * 
	 * @throws IllegalStateException
	 */
	abstract DateTime getEstimatedFinishTime(Project project)
			throws IllegalStateException;

	/**
	 * Returns the total delay of the project in minutes.
	 * 
	 * @return Returns the total delay of the project in minutes.
	 * 
	 * @throws IllegalStateException
	 */
	abstract int getTotalDelay(Project project) throws IllegalStateException;

}
