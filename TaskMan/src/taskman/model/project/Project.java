package taskman.model.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.Observer;
import taskman.model.memento.ProjectMemento;
import taskman.model.project.task.Task;
import taskman.model.project.task.TaskFactory;
import taskman.model.resource.ResourceType;
import taskman.model.time.TimeService;

public class Project implements Observer {

	private final TaskFactory taskFactory;
	private TimeService timeService = new TimeService();

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
			DateTime dueTime, TaskFactory taskFactory)
			throws IllegalArgumentException, IllegalDateException {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		if (description == null)
			throw new IllegalArgumentException("Description can not be null.");
		if (creationTime == null)
			throw new IllegalArgumentException("Creation time can not be null.");
		if (dueTime == null)
			throw new IllegalArgumentException("Due time can not be null.");
		if (taskFactory == null)
			throw new IllegalArgumentException("Task factory can not be null.");
		if (dueTime.isBefore(creationTime))
			throw new IllegalDateException(
					"Due time has to be after creation time.");
		this.name = name;
		this.description = description;
		this.creationTime = creationTime;
		this.dueTime = dueTime;
		this.taskFactory = taskFactory;
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
			int acceptableDeviation, List<Task> dependencies,
			Task alternativeFor, Map<ResourceType, Integer> resourceTypes)
			throws IllegalStateException {
		this.state.addTask(this, description, estimatedDuration,
				acceptableDeviation, dependencies, alternativeFor,
				resourceTypes);
	}

	protected void performAddTask(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies,
			Task alternativeFor, Map<ResourceType, Integer> resourceTypes)
			throws IllegalArgumentException {
		Task task = taskFactory.makeTask(description, estimatedDuration,
				acceptableDeviation, dependencies, alternativeFor,
				resourceTypes);
		this.tasks.add(task);
		task.attach(this);
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

	// TODO Testing
	protected DateTime performGetEstimatedFinishTime() {
		DateTime lastEndTime = timeService
				.getFirstPossibleStartTime(creationTime);
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
		return timeService.addMinutes(lastEndTime, minutesToAdd);
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
		for (Task t : this.tasks) {
			if (t.getTimeSpan().getEndTime().isAfter(lastEndTime)) {
				lastEndTime = t.getTimeSpan().getEndTime();
			}
		}

		if (lastEndTime.isAfter(dueTime))
			return calculateTotalDelayInMinutes(dueTime, lastEndTime);
		else
			return -calculateTotalDelayInMinutes(lastEndTime, dueTime);
	}

	/**
	 * Will calculate the total delay of the project in minutes
	 * 
	 * @param expected
	 *            The date time in which the project will be expected to finish
	 * @param real
	 *            The date time in which the project actually finished
	 * @return Returns the total delay in minutes for this project
	 */
	private int calculateTotalDelayInMinutes(DateTime expected, DateTime real) {
		int minutes = 0;
		expected = timeService.getFirstPossibleStartTime(expected);
		real = timeService.getFirstPossibleStartTime(real);
		if (real.getHourOfDay() == 11)
			real = real.plusHours(1);
		while (expected.isBefore(real)) {
			DateTime breaks = timeService.addBreaks(expected);
			if (breaks.isEqual(expected)) {
				expected = expected.plusMinutes(1);
				minutes += 1;
			} else {
				expected = breaks;
			}
		}
		return minutes;
	}

	@Override
	/**
	 * Will update the project state
	 * @post the project state will be updated
	 */
	public void update() {
		updateProjectState();
	}

	@Override
	/**
	 * Returns the name and if the project is finished or ongoing in a String
	 * @return The name and whether the project is finished or ongoing
	 */
	public String toString() {
		return name + ": " + (isFinished() ? "FINISHED" : "ONGOING");
	}

	/**
	 * Creates a new project memento that saves the state of project.
	 * 
	 * @return Creates a new project memento that saves the state of the
	 *         project.
	 */
	public ProjectMemento createMemento() {
		return new ProjectMemento(new ArrayList<Task>(tasks), state.getName());
	}

	/**
	 * Returns the state of project to that saved in the project memento.
	 * 
	 * @param m
	 */
	public void setMemento(ProjectMemento m) {
		tasks = m.getTasks();
		if (m.getStateName().equals("ONGOING")) {
			state = new Ongoing();
		} else {
			state = new Finished();
		}
	}

}
