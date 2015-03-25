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
	abstract String getName(Project project);
		
	/**
	 * Adds a new task without dependencies.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 */
	abstract void addTask(Project project, String description, int estimatedDuration, int acceptableDeviation);
	
	/**
	 * Adds a new task with dependencies.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 */
	abstract void addTask(Project project, String description, int estimatedDuration, int acceptableDeviation, List<Task> dependencies);
	
	/**
	 * Updates the state.
	 */
	abstract void updateProjectState(Project project);
	
	/**
	 * Returns the estimated finish time of the project.
	 * 
	 * @return Returns the estimated finish time of the project.
	 */
	abstract DateTime getEstimatedFinishTime(Project project);
	
	/**
	 * Returns the total delay of the project in minutes.
	 * 
	 * @return Returns the total delay of the project in minutes.
	 */
	abstract int getTotalDelay(Project project);

}
