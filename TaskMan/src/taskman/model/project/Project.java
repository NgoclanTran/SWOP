package taskman.model.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import taskman.exceptions.IllegalDateException;
import taskman.model.project.task.Task;

public class Project implements Observer {

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
			int acceptableDeviation, List<Task> dependencies, Task alternativeFor)
			throws IllegalStateException {
		this.state.addTask(this, description, estimatedDuration,
				acceptableDeviation, dependencies, alternativeFor);
	}

	protected void performAddTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies, Task alternativeFor) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation, dependencies, alternativeFor);
		this.tasks.add(task);
		task.attachObserver(this);
	}

	private ArrayList<Task> tasks;

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

	// TODO: Documentation + testing
	protected DateTime performGetEstimatedFinishTime() {
		DateTime lastEndTime = getFirstStartTime();
		int minutesToAdd = 0;
		for (Task t : this.tasks) {
			if (t.isCompleted()) {
				if (t.getTimeSpan().getEndTime().isAfter(lastEndTime)) {
					lastEndTime = t.getTimeSpan().getEndTime();
				}
			} else {
				minutesToAdd += t.getEstimatedDuration();
			}
		}
		return calculateEstimatedFinishTime(lastEndTime, minutesToAdd);
	}
	
	private DateTime getFirstStartTime() {
		DateTime firstStartTime = this.creationTime;
		if (firstStartTime.getHourOfDay() < 8) {
			firstStartTime = firstStartTime.plusMinutes((60 - firstStartTime
					.getMinuteOfHour()) % 60);
			firstStartTime = firstStartTime.plusHours(8 - firstStartTime.getHourOfDay());
		} else if (firstStartTime.getHourOfDay() >= 17) {
			firstStartTime = firstStartTime.plusMinutes((60 - firstStartTime
					.getMinuteOfHour()) % 60);
			firstStartTime = firstStartTime.plusHours((24 - firstStartTime
					.getHourOfDay()) % 24);
			firstStartTime = firstStartTime.plusHours(8);
		}
		return firstStartTime;
	}
	
	private DateTime calculateEstimatedFinishTime(DateTime lastEndTime, int minutesToAdd) {
		while (minutesToAdd > 0) {
			lastEndTime = lastEndTime.plusMinutes(1);
			if (lastEndTime.getDayOfWeek() > 5) {
				lastEndTime = lastEndTime.plusDays(2);
			}
			if (lastEndTime.getHourOfDay() == 17
					&& lastEndTime.getMinuteOfHour() > 0) {
				lastEndTime = lastEndTime.plusHours(15);
			}
			minutesToAdd -= 1;
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
		Duration dur = new Duration(dueTime, lastEndTime);
		int delay = dur.toStandardMinutes().getMinutes();
		if (delay > 0) {
			totalDelay = delay;
		}
		return totalDelay;
	}

	@Override
	public void update() {
		updateProjectState();
	}
	
	@Override
	public String toString() {
		return name + ": " + (isFinished() ? "Finished" : "Ongoing");
	}
}
