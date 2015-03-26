package taskman.model.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import be.kuleuven.cs.som.taglet.ThrowsTaglet;
import taskman.exceptions.IllegalDateException;
import taskman.model.project.task.Task;

public class Project {

	/**
	 * Creates a new project.
	 * 
	 * @param name
	 * @param description
	 * @param creationTime
	 * @param dueTime
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalDateException
	 */
	public Project(String name, String description, DateTime creationTime,
			DateTime dueTime) throws IllegalArgumentException,
			IllegalDateException {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		if (description == null)
			throw new IllegalArgumentException("Description can not be null.");
		if (creationTime == null)
			throw new IllegalArgumentException("Creation time can not be null.");
		if (dueTime == null)
			throw new IllegalArgumentException("Due time can not be null.");
		if (dueTime.isBefore(creationTime))
			throw new IllegalDateException(
					"Due time has to be after creation time.");
		this.name = name;
		this.description = description;
		this.creationTime = creationTime;
		this.dueTime = dueTime;
		this.tasks = new ArrayList<Task>();
	}

	/**
	 * Returns the name of the project.
	 * 
	 * @return Returns the name of the project.
	 */
	public String getName() {
		return this.name;
	}

	private final String name;

	/**
	 * Returns the description of the project.
	 * 
	 * @return Returns the description of the project.
	 */
	public String getDescription() {
		return this.description;
	}

	private final String description;

	/**
	 * Returns the creation time of the project.
	 * 
	 * @return Returns the creation time of the project.
	 */
	public DateTime getCreationTime() {
		return this.creationTime;
	}

	private final DateTime creationTime;

	/**
	 * Returns the due time of the project.
	 * 
	 * @return Returns the due time of the project.
	 */
	public DateTime getDueTime() {
		return this.dueTime;
	}

	private final DateTime dueTime;

	/**
	 * Returns the list of tasks of the project.
	 * 
	 * @return Returns the list of tasks of the project.
	 */
	public List<Task> getTasks() {
		return new ArrayList<Task>(this.tasks);
	}

	/**
	 * Creates a task without dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * 
	 * @throws IllegalStateException
	 */
	public void addTask(String description, int estimatedDuration,
			int acceptableDeviation) throws IllegalStateException {
		this.state.addTask(this, description, estimatedDuration,
				acceptableDeviation);
	}

	protected void performAddTask(String description, int estimatedDuration,
			int acceptableDeviation) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation);
		this.tasks.add(task);
	}

	/**
	 * Creates a task with dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 * 
	 * @throws IllegalStateException
	 */
	public void addTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies)
			throws IllegalStateException {
		this.state.addTask(this, description, estimatedDuration,
				acceptableDeviation, dependencies);
	}

	protected void performAddTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation, dependencies);
		this.tasks.add(task);
	}

	private ArrayList<Task> tasks;

	/**
	 * Call the addTimeSpan function of the specified task.
	 * 
	 * @param task
	 * @param failed
	 * @param startTime
	 * @param endTime
	 * 
	 * @throws IllegalArgumentException
	 */
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalArgumentException {
		if(task == null) throw new IllegalArgumentException("Cannot be called without a task.");
		task.addTimeSpan(failed, startTime, endTime);
		updateProjectState();
	}

	/**
	 * Returns the name of the state of the project.
	 * 
	 * @return Returns the name of the state of the project.
	 */
	public String getStateName() {
		return this.state.getName();
	}

	/**
	 * Updates the state of the project in accordance with the tasks.
	 * 
	 * @throws IllegalStateException
	 */
	private void updateProjectState() throws IllegalStateException {
		this.state.updateProjectState(this);
	}

	protected void performUpdateProjectState() {
		boolean finished = true;
		for (Task t : this.tasks) {
			if (!t.isFinished()) {
				finished = false;
			}
		}
		if (finished) {
			this.state = new Finished();
		}
	}

	/**
	 * Returns whether the project is finished or not.
	 * 
	 * @return Returns whether the project is finished or not.
	 */
	public boolean isFinished() {
		return this.state.isFinished();
	}

	private State state = new Ongoing();

	/**
	 * Returns the time the project is estimated to finish.
	 * 
	 * @return Returns the time the project is estimated to finish.
	 * 
	 * @throws IllegalStateException
	 */
	public DateTime getEstimatedFinishTime() throws IllegalStateException {
		return this.state.getEstimatedFinishTime(this);
	}

	protected DateTime performGetEstimatedFinishTime() {
		DateTime lastEndTime = this.creationTime;
		int timeToGo = 0;
		for (Task t : this.tasks) {
			// TODO if statement??
			if (t.isFinished() || t.isFailed()) {
				if (t.getTimeSpan().getEndTime().isAfter(lastEndTime)) {
					lastEndTime = t.getTimeSpan().getEndTime();
				}
			} else {
				timeToGo += t.getEstimatedDuration();
			}
		}
		while (timeToGo > 0) {
			lastEndTime = lastEndTime.plusMinutes(1);
			if (lastEndTime.getDayOfWeek() > 5) {
				lastEndTime = lastEndTime.plusDays(2);
			}
			if (lastEndTime.getHourOfDay() == 17) {
				lastEndTime = lastEndTime.plusHours(15);
			}
			timeToGo -= 1;
		}
		return lastEndTime;
	}

	/**
	 * Returns the total delay of the tasks belonging to the project in minutes.
	 * 
	 * @return Returns the total delay of the tasks belonging to the project in
	 *         minutes.
	 * 
	 * @throws IllegalStateException
	 */
	public int getTotalDelay() throws IllegalStateException {
		return this.state.getTotalDelay(this);
	}

	protected int performGetTotalDelay() {
		DateTime lastEndTime = this.creationTime;
		int totalDelay = 0;
		for (Task t : this.tasks) {
			if (t.getTimeSpan().getEndTime().isAfter(lastEndTime)) {
				lastEndTime = t.getTimeSpan().getEndTime();
			}
		}
		long delay = lastEndTime.getMillis() - dueTime.getMillis();
		if (delay > 0) {
			totalDelay = (int) delay;
		}
		return totalDelay;
	}
}
