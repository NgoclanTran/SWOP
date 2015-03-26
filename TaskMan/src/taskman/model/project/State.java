package taskman.model.project;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;

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
	 * Adds a new task without dependencies.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * 
	 * @throws IllegalStateException
	 */
	abstract void addTask(Project project, String description, int estimatedDuration, int acceptableDeviation) throws IllegalStateException;
	
	/**
	 * Adds a new task with dependencies.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 * 
	 * @throws IllegalStateException
	 */
	abstract void addTask(Project project, String description, int estimatedDuration, int acceptableDeviation, List<Task> dependencies) throws IllegalStateException;
	
	/**
	 * Updates the state.
	 * 
	 * @throws IllegalStateException
	 */
	abstract void updateProjectState(Project project) throws IllegalStateException;
	
	/**
	 * Returns the estimated finish time of the project.
	 * 
	 * @return Returns the estimated finish time of the project.
	 * 
	 * @throws IllegalStateException
	 */
	abstract DateTime getEstimatedFinishTime(Project project) throws IllegalStateException;
	
	/**
	 * Returns the total delay of the project in minutes.
	 * 
	 * @return Returns the total delay of the project in minutes.
	 * 
	 * @throws IllegalStateException
	 */
	abstract int getTotalDelay(Project project) throws IllegalStateException;

}
