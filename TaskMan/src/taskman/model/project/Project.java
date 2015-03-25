package taskman.model.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;

public class Project {

	/**
	 * Creates a new project.
	 * 
	 * @param name
	 * @param description
	 * @param creationTime
	 * @param dueTime
	 */
	public Project(String name, String description, DateTime creationTime,
			DateTime dueTime) {
	}

	/**
	 * Returns the name of the project.
	 * 
	 * @return Returns the name of the project.
	 */
	public String getName() {
	}

	private final String name;

	/**
	 * Returns the description of the project.
	 * 
	 * @return Returns the description of the project.
	 */
	public String getDescription() {
	}

	private final String description;

	/**
	 * Returns the creation time of the project.
	 * 
	 * @return Returns the creation time of the project.
	 */
	public DateTime getCreationTime() {
	}

	private final DateTime creationTime;

	/**
	 * Returns the due time of the project.
	 * 
	 * @return Returns the due time of the project.
	 */
	public DateTime getDueTime() {
	}

	private final DateTime dueTime;

	/**
	 * Returns the list of tasks of the project.
	 * 
	 * @return Returns the list of tasks of the project.
	 */
	public List<Task> getTasks() {
	}

	/**
	 * Creates a task without dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 */
	public void addTask(String description, int estimatedDuration,
			int acceptableDeviation) {
	}

	protected void performAddTask(String description, int estimatedDuration,
			int acceptableDeviation) {

	}

	/**
	 * Creates a task with dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 */
	public void addTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
	}

	protected void performAddTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {

	}

	private ArrayList<Task> tasks;

	/**
	 * Returns the name of the state of the project.
	 * 
	 * @return Returns the name of the state of the project.
	 */
	public String getStateName() {

	}

	/**
	 * Updates the state of the project in accordance with the tasks.
	 */
	public void updateProjectState() {
	}

	protected void performUpdateProjectState() {

	}

	/**
	 * Returns whether the project is finished or not.
	 * 
	 * @return Returns whether the project is finished or not.
	 */
	public boolean isFinished() {
	}

	private State state = new Ongoing();

	/**
	 * Returns the time the project is estimated to finish.
	 * 
	 * @return Returns the time the project is estimated to finish.
	 */
	public DateTime getEstimatedFinishTime() {
	}

	protected DateTime performGetEstimatedFinishTime() {

	}

	/**
	 * Returns the total delay of the tasks belonging to the project in minutes.
	 * 
	 * @return Returns the total delay of the tasks belonging to the project in
	 *         minutes.
	 */
	public int getTotalDelay() {

	}

	protected int performGetTotalDelay() {

	}

}
