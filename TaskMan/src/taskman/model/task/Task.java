package taskman.model.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.Observer;
import taskman.model.memento.TaskMemento;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.TimeService;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class Task extends TaskSubject implements Observer {

	private Clock clock;
	private TimeService timeService = new TimeService();

	/**
	 * The first constructor of task. This will create a task with the given
	 * parameters
	 * 
	 * @param clock
	 *            The system clock.
	 * @param description
	 *            The description of the task
	 * @param estimatedDuration
	 *            The estimated duration of the task
	 * @param acceptableDeviation
	 *            The acceptable deviation of the task
	 * @param dependencies
	 *            The list of dependencies of the task
	 * @param alternativeFor
	 *            The task for which this task is the alternative.
	 * @param resourcetypes
	 *            The resource types and their amount accordingly.
	 * 
	 * @post The new description is equal to the given description
	 * @post The new estimatedDuration is equal to the given estimatedDuration
	 * @post The new acceptableDeviation is equal to the given
	 *       acceptableDevaition
	 * @post The status of this task is Unavailable
	 * @post The new dependencies is equal to the given dependencies
	 * 
	 * @throws IllegalArgumentException
	 *             The description cannot be null
	 * @throws IllegalArgumentException
	 *             The estimatedDuration is negative
	 * @throws IllegalArgumentException
	 *             The acceptableDeviation is negative
	 */
	public Task(Clock clock, String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies,
			Task alternativeFor, Map<ResourceType, Integer> resourceTypes)
			throws IllegalStateException, IllegalArgumentException {
		if (!isValidClock(clock))
			throw new IllegalArgumentException(
					"The create project controller needs a clock");
		if (description == null)
			throw new IllegalArgumentException("Description is null");
		if (estimatedDuration <= 0)
			throw new IllegalArgumentException(
					"The estimated duration cannot be negative.");
		if (acceptableDeviation < 0)
			throw new IllegalArgumentException(
					"The deviation cannot be negative.");
		this.clock = clock;
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		this.status = new Unavailable();
		if (dependencies != null)
			this.dependencies.addAll(dependencies);

		for (Task subject : this.dependencies) {

			subject.attachDependant(this);

		}

		if (alternativeFor != null)
			alternativeFor.addAlternative(this);

		if (resourceTypes != null) {
			for (Entry<ResourceType, Integer> entry : resourceTypes.entrySet()) {
				addRequiredResourceType(entry.getKey(), entry.getValue());
			}
		}

		try {
			update();
		} catch (IllegalStateException e) {

		}

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

	private final String description;
	private final int estimatedDuration;
	private final int acceptableDeviation;
	private List<Task> dependencies = new ArrayList<Task>();
	// Tasks that are looking to me
	private List<Task> dependants = new ArrayList<Task>();
	private Status status;
	private TimeSpan timeSpan;
	private Task alternative = null;
	private LinkedHashMap<ResourceType, Integer> requiredResourceTypes = new LinkedHashMap<ResourceType, Integer>();
	private List<Developer> requiredDevelopers = new ArrayList<Developer>();

	/**
	 * Returns the description of the task
	 * 
	 * @return The description of the task
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the estimated duration of the task
	 * 
	 * @return The estimated duration of the task
	 */
	public int getEstimatedDuration() {
		return estimatedDuration;
	}

	/**
	 * Returns the acceptable deviation of the task
	 * 
	 * @return The acceptable deviation of the task
	 */
	public int getAcceptableDeviation() {
		return acceptableDeviation;
	}

	/**
	 * Returns the list of dependencies of the task
	 * 
	 * @return The list of dependencies of the task
	 */
	public List<Task> getDependencies() {
		return new ArrayList<Task>(dependencies);
	}

	/**
	 * Add dependant task
	 * 
	 * @param dependant
	 * @throws IllegalArgumentException
	 *             The dependant task is null
	 * @post The list of depandants contains the given dependant task
	 */
	public void attachDependant(Task dependant) {
		if (dependant == null)
			throw new IllegalArgumentException(
					"the dependant observer is null.");
		this.dependants.add(dependant);
	}

	/**
	 * When this task changed his status to Finished or failed, notify his
	 * dependants
	 * 
	 */
	private void notifyAllDependants() {
		for (Task dependant : this.dependants) {
			try {
				dependant.update();
			} catch (IllegalStateException e) {
			}
		}
	}

	/**
	 * Returns the timespan of the task
	 * 
	 * @return The timespan of the task
	 */
	public TimeSpan getTimeSpan() {
		return this.status.getTimeSpan(this);
	}

	protected TimeSpan performGetTimeSpan() {
		return new TimeSpan(this.timeSpan.getStartTime(),
				this.timeSpan.getEndTime());
	}

	/**
	 * This will create a new timespan for the task
	 * 
	 * @param startTime
	 *            The start time of the timespan
	 * @param endTime
	 *            The end time of the timespan
	 * @throws IllegalArgumentException
	 *             The startTime cannot be equal to null
	 * @throws IllegalArgumentException
	 *             The endTime is equal to null
	 * @throws IllegalArgumentException
	 *             Exception will be thrown if the end time is earlier than the
	 *             start time
	 */
	public void addTimeSpan(boolean failed, DateTime startTime, DateTime endTime)
			throws IllegalArgumentException, IllegalDateException,
			IllegalArgumentException {
		if (startTime == null)
			throw new IllegalArgumentException("The startTime is null.");
		if (endTime == null)
			throw new IllegalArgumentException("The endTime is null.");

		this.status.addTimeSpan(this, failed, startTime, endTime);
		for (Developer d : getRequiredDevelopers()) {
			for (Reservation r : d.getReservations()) {
				if (r.getTask().equals(this)) {
					if (r.getTimeSpan().getEndTime().isAfter(endTime)) {
						if (r.getTimeSpan().getStartTime().isAfter(endTime)) {
							d.removeReservation(r);
						} else {
							DateTime start = r.getTimeSpan().getStartTime();
							DateTime end = endTime;
							d.removeReservation(r);
							d.addReservation(this, new TimeSpan(start, end));
						}
					}
				}
			}
		}
		for (Entry<ResourceType, Integer> entry : getRequiredResourceTypes()
				.entrySet()) {
			for (Resource r : entry.getKey().getResources()) {
				for (Reservation res : r.getReservations()) {
					if (res.getTask().equals(this)) {
						if (res.getTimeSpan().getEndTime().isAfter(endTime)) {
							if (res.getTimeSpan().getStartTime()
									.isAfter(endTime)) {
								r.removeReservation(res);
							} else {
								DateTime start = res.getTimeSpan()
										.getStartTime();
								DateTime end = endTime;
								r.removeReservation(res);
								r.addReservation(this, new TimeSpan(start, end));
							}
						}
					}
				}
			}
		}
	}

	protected void performAddTimeSpan(DateTime startTime, DateTime endTime)
			throws IllegalDateException {

		this.timeSpan = new TimeSpan(startTime, endTime);
	}

	/**
	 * Returns the alternative task for the task
	 * 
	 * @return The alternative task for the task
	 */
	public Task getAlternative() {
		return alternative;
	}

	/**
	 * Add the alternative task for the task
	 *
	 * @param task
	 *            The new alternative task for this task
	 * @throws IllegalArgumentException
	 *             The alternative task is equal to null
	 * 
	 */
	public void addAlternative(Task task) throws IllegalStateException {
		if (task == null)
			throw new IllegalArgumentException("The alternative is null.");

		this.status.addAlternative(this, task);

	}

	protected void performAddAlternative(Task task) {
		this.alternative = task;
	}

	/**
	 * Returns the map of required resource types for the task.
	 * 
	 * @return Returns the map of required resource types for the task.
	 */
	public Map<ResourceType, Integer> getRequiredResourceTypes() {
		return new LinkedHashMap<ResourceType, Integer>(requiredResourceTypes);
	}

	/**
	 * Adds the resource type if the required resource types are already added
	 * and no conflicting resource type has been added.
	 * 
	 * @param resourceType
	 * @param amount
	 */
	private void addRequiredResourceType(ResourceType resourceType, int amount)
			throws IllegalArgumentException {
		if (checkResourceTypeConflicts(resourceType))
			throw new IllegalArgumentException(
					"Conflicting resource already added.");
		if (checkResourceTypeSelfConflicting(resourceType, amount))
			throw new IllegalArgumentException(
					"Only one of this resource type allowed.");
		if (!checkResourceTypeRequirements(resourceType))
			throw new IllegalArgumentException(
					"Required resource has to be added first.");
		if (!hasEnougResources(resourceType, amount))
			throw new IllegalArgumentException("Not enough resources.");
		requiredResourceTypes.put(resourceType, amount);
	}

	/**
	 * Returns true or false depending on whether the task has a certain amount
	 * resources of a given resource type
	 * 
	 * @param resourceType
	 *            The given resource type to be checked
	 * @param amount
	 *            The amount of resources that are needed
	 * @return Returns true or false depending on whether there is a certain
	 *         amount of resources available for the given resource type
	 */
	private boolean hasEnougResources(ResourceType resourceType, int amount) {
		if (resourceType.getResources().size() < amount)
			return false;
		else
			return true;
	}

	/**
	 * Returns true or false depending on whether the given resource type has
	 * conflicts or not
	 * 
	 * @param resourceType
	 *            The resource type to be checked
	 * @return true or false depending on whether the given resource type has
	 *         conflicts or not
	 */
	private boolean checkResourceTypeConflicts(ResourceType resourceType) {
		List<ResourceType> conflicts = resourceType.getConflictsWith();
		if (conflicts != null) {
			for (ResourceType conflictingResourceType : conflicts) {
				if (requiredResourceTypes.containsKey(conflictingResourceType)) {
					return true;
				}
			}
		}
		for (Entry<ResourceType, Integer> entry : getRequiredResourceTypes()
				.entrySet()) {
			if (entry.getKey().getConflictsWith().contains(resourceType))
				return true;
		}
		return false;
	}

	/**
	 * Returns a boolean indicating whether the resource type is self
	 * conflicting or not
	 * 
	 * @param resourceType
	 *            The resource type to be checked
	 * @param amount
	 *            The amount of resources needed of this type
	 * @return Returns true if the amount is larger than 1 and if the resource
	 *         type is self conflicting
	 */
	private boolean checkResourceTypeSelfConflicting(ResourceType resourceType,
			int amount) {
		if (resourceType.isSelfConflicting() && amount > 1) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true or false whether the resource type requirements are
	 * fullfilled or not
	 * 
	 * @param resourceType
	 *            The resource type to be checked
	 * @return Returns true or false whether the resource type requirements are
	 *         fullfilled or not
	 */
	private boolean checkResourceTypeRequirements(ResourceType resourceType) {
		List<ResourceType> requirements = resourceType.getRequires();
		if (requirements != null) {
			for (ResourceType requiredResourceType : requirements) {
				if (!requiredResourceTypes.containsKey(requiredResourceType)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns the list of required developers for the task.
	 * 
	 * @return Returns the list of required developers for the task.
	 */
	public List<Developer> getRequiredDevelopers() {
		return new ArrayList<Developer>(requiredDevelopers);
	}

	/**
	 * Adds the given developer to the list of required developers.
	 * 
	 * @param developer
	 *            The developer to add
	 */
	public void addRequiredDeveloper(Developer developer) {
		if (developer == null)
			throw new IllegalArgumentException("The developer cannot be null.");
		if (requiredDevelopers.contains(developer))
			throw new IllegalArgumentException(
					"The developer is already in the list of developpers.");
		requiredDevelopers.add(developer);
	}

	/**
	 * Returns the status of the task
	 * 
	 * @return The status of the task
	 */
	public String getStatusName() {
		return this.status.getName();
	}

	/**
	 * Check if this task is available
	 * 
	 * @return True if the task has status available
	 */
	public boolean isAvailable() {
		return this.status.isAvailable();
	}

	/**
	 * Check if this task is executing
	 * 
	 * @return True if the task has status executing
	 */
	public boolean isExecuting() {
		return this.status.isExecuting();
	}

	/**
	 * Check if this task is failed
	 * 
	 * @return True if the task has status failed
	 */
	public boolean isFailed() {
		return this.status.isFailed();
	}

	/**
	 * Check if this task is finished
	 * 
	 * @return True if the task has status finished
	 */
	public boolean isFinished() {
		return this.status.isFinished();
	}

	/**
	 * Check if this task has completed (either finished or failed).
	 * 
	 * @return True if the task is finished or failed.
	 */
	public boolean isCompleted() {
		if (isFinished() || isFailed())
			return true;
		else
			return false;
	}

	@Override
	public void update() {
		try {
			this.status.updateStatus(this, clock.getSystemTime());
		} catch (IllegalStateException ex) {
		}
	}

	protected void performUpdateStatus(Status status) {
		this.status = status;

		this.notifyAllDependants(); // notify dependant task
		this.notifyAllObservers(); // observer pattern for project
	}

	/**
	 * Returns a boolean whether the developpers and resource types are
	 * available for this task or not
	 * 
	 * @param time
	 *            The date time for which this will be checked
	 * @return Returns true or false depending on whether the developpers and
	 *         resource types are available for this task or not
	 */
	protected boolean developersAndResourceTypesAvailable(DateTime time) {
		for (Developer developer : getRequiredDevelopers()) {
			if (!developer.isAvailableAt(new TimeSpan(time, timeService
					.addMinutes(time, getEstimatedDuration()))))
				return false;
		}
		for (Entry<ResourceType, Integer> entry : getRequiredResourceTypes()
				.entrySet()) {
			for (Resource resource : entry.getKey().getResources()) {
				if (!resource.isAvailableAt(new TimeSpan(time, timeService
						.addMinutes(time, getEstimatedDuration()))))
					return false;
			}
		}
		return true;
	}

	/**
	 * Returns a boolean indicating whether the dependencies for this task are
	 * fulfilled or not
	 * 
	 * @return Returns true or false depending on whether the dependencies are
	 *         fulfilled or not
	 */
	protected boolean dependenciesAreFinished() {
		for (Task task : this.dependencies) {
			try {
				if (!task.status.isAlternativeFinished(task))
					return false;

			} catch (IllegalStateException e) {
				if (!task.isFinished())
					return false;
			}
		}
		return true;
	}

	/**
	 * Returns the list of reservations for this task
	 * 
	 * @return returns the list of reservations for this task
	 */
	protected List<Reservation> getReservations() {
		List<Reservation> reservations = new ArrayList<Reservation>();
		for (Developer developer : getRequiredDevelopers()) {
			for (Reservation reservation : developer.getReservations()) {
				if (reservation.getTask().equals(this))
					reservations.add(reservation);
			}
		}
		for (Entry<ResourceType, Integer> entry : getRequiredResourceTypes()
				.entrySet()) {
			for (Resource resource : entry.getKey().getResources()) {
				for (Reservation reservation : resource.getReservations()) {
					if (reservation.getTask().equals(this))
						reservations.add(reservation);
				}
			}
		}
		return reservations;
	}

	/**
	 * Returns a boolean whether the alternative task is finished or not
	 * 
	 * @return Returns true or false depending on whether the alternative task
	 *         is finished or not
	 */
	protected boolean isAlternativeFinished() {
		if (this.alternative == null)
			return false;
		if (this.alternative.isFinished())
			return true;
		if (this.alternative.isFailed())
			return this.alternative.status.isAlternativeFinished(alternative);
		return false;

	}

	/**
	 * Returns the total execution time for the task
	 * 
	 * @return The total execution time for the task
	 */

	public int getTotalExecutionTime() throws IllegalStateException {

		int time = this.status.calculateTotalExecutedTime(this);

		return time;

	}

	protected int performGetTotalExecutionTime() throws IllegalStateException {

		int time = this.timeSpan.calculatePerformedTime();

		// if (this.alternative != null)
		// try {
		// time = time + this.alternative.getTotalExecutionTime();
		// } catch (IllegalStateException e) {
		// }
		return time;
	}

	/**
	 * Calculate the overdue percentage
	 * 
	 * @return Calculate the overdue percentage
	 */
	public int getOverduePercentage() throws IllegalStateException {
		return this.status.calculateOverDuePercentage(this);
	}

	protected int performGetOverduePercentage() throws IllegalStateException {

		int totalExecutedTime = this.getTotalExecutionTime();

		return (totalExecutedTime - this.estimatedDuration) * 100
				/ this.estimatedDuration;
	}

	/**
	 * Returns a boolean depending on whether the task is severly overdue or not
	 * 
	 * @return Returns true or false depending on whether the task is serverly
	 *         overdue or not
	 */
	public boolean isSeverelyOverdue() {
		return this.status.isSeverelyOverdue(this);
	}

	protected boolean performIsSeverelyOverDue() {
		return getOverduePercentage() > getAcceptableDeviation();
	}

	/**
	 * Returns a boolean indicating whether this task is planned or not
	 * 
	 * @return Returns true or false depending on whether the task is planned or
	 *         not
	 */
	public boolean isPlanned() {
		return status.isPlanned();
	}

	/**
	 * Creates a new task memento that saves the state of task.
	 * 
	 * @return Creates a new task memento that saves the state of the task.
	 */
	public TaskMemento createMemento() {
		return new TaskMemento(this, dependants, status.getName(), timeSpan,
				alternative);
	}

	/**
	 * Returns the state of task to that saved in the task memento.
	 * 
	 * @param m
	 */
	public void setMemento(TaskMemento m) {
		dependants = m.getDependants();
		timeSpan = m.getTimeSpan();
		alternative = m.getAlternative();
		if (m.getStateName().equals("UNAVAILABLE")) {
			status = new Unavailable();
		} else if (m.getStateName().equals("AVAILABLE")) {
			status = new Available();
		} else if (m.getStateName().equals("EXECUTING")) {
			status = new Executing();
		} else if (m.getStateName().equals("FINISHED")) {
			status = new Finished();
		} else if (m.getStateName().equals("PLANNED")){
			status = new Planned();
		} else {
			status = new Failed();
		}
	}

	public void executeTask() {
		this.status.executeTask(this);
	}

	protected void performExecuteTask(Status status) {
		DateTime reservationStart = null;
		for (Developer d : requiredDevelopers) {
			for (Reservation r : d.getReservations()) {
				if (r.getTask().equals(this)) {
					reservationStart = r.getTimeSpan().getStartTime();
				}
				if (reservationStart != null) {
					break;
				}
			}
			if (reservationStart != null) {
				break;
			}
		}
		if (clock.getSystemTime().isBefore(reservationStart)) {
			TimeSpan ts = new TimeSpan(
					timeService
							.getFirstPossibleStartTime(clock.getSystemTime()),
					timeService.addMinutes(timeService
							.getFirstPossibleStartTime(clock.getSystemTime()),
							estimatedDuration));
			for (Developer d : requiredDevelopers) {
				d.addReservation(this, ts);
			}

			for (Entry<ResourceType, Integer> entry : requiredResourceTypes
					.entrySet()) {
				List<Resource> availableResources = entry.getKey()
						.getAvailableResources(ts);
				for (int i = 0; i < entry.getValue(); i++) {
					availableResources.get(i).addReservation(this, ts);
				}
			}
		}
		this.status = status;
	}
}