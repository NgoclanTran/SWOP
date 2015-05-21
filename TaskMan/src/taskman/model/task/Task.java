package taskman.model.task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.Observer;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.TimeService;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public abstract class Task extends TaskSubject implements Observer {

	private final Clock clock;
	private TimeService timeService = new TimeService();

	public Task(Clock clock, String description, int estimatedDuration,
			int acceptableDeviation, Map<ResourceType, Integer> resourceTypes)
			throws IllegalArgumentException {
		if (clock == null)
			throw new IllegalArgumentException("A task needs a clock.");
		if (description == null)
			throw new IllegalArgumentException("A task needs a description");
		if (estimatedDuration <= 0)
			throw new IllegalArgumentException(
					"The estimated duration of a task cannot be negative.");
		if (acceptableDeviation < 0)
			throw new IllegalArgumentException(
					"The acceptable deviation of a task cannot be negative.");
		this.id = UUID.randomUUID();
		this.clock = clock;
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;

		setStatus(new Unavailable());

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
	 * Returns the unique identifier of the task
	 * 
	 * @return The unique identifier of the task
	 */
	public UUID getID() {
		return id;
	}

	private final UUID id;

	/**
	 * Returns the unique identifier of the parent of the task
	 * 
	 * @return The unique identifier of the parent of the task
	 */
	public UUID getParentID() {
		return parentId;
	}

	/**
	 * This method will set the new parent id.
	 * 
	 * @param newId
	 */
	public void setParentID(UUID newId) {
		parentId = newId;
	}

	private UUID parentId = null;

	/**
	 * Returns the description of the task
	 * 
	 * @return The description of the task
	 */
	public String getDescription() {
		return description;
	}

	private final String description;

	/**
	 * Returns the estimated duration of the task
	 * 
	 * @return The estimated duration of the task
	 */
	public int getEstimatedDuration() {
		return estimatedDuration;
	}

	private final int estimatedDuration;

	/**
	 * Returns the acceptable deviation of the task
	 * 
	 * @return The acceptable deviation of the task
	 */
	public int getAcceptableDeviation() {
		return acceptableDeviation;
	}

	private final int acceptableDeviation;

	/**
	 * Returns the status of the task
	 * 
	 * @return The status of the task
	 */
	public String getStatusName() {
		return status.getName();
	}

	/**
	 * Returns the status of the task
	 * 
	 * @return The status of the task
	 */
	protected Status getStatus() {
		return status;
	}

	/**
	 * This method will set the status to the given status.
	 * 
	 * @param status
	 */
	protected void setStatus(Status newStatus) {
		status = newStatus;
	}

	private Status status;

	/**
	 * Check if this task is available
	 * 
	 * @return True if the task has status available
	 */
	public boolean isAvailable() {
		return status.isAvailable();
	}

	/**
	 * Check if this task is delegated
	 * 
	 * @return True if the task has status delegated
	 */
	public boolean isDelegated() {
		return status.isDelegated();
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
	 * Check if this task is executing
	 * 
	 * @return True if the task has status executing
	 */
	public boolean isExecuting() {
		return status.isExecuting();
	}

	/**
	 * Check if this task is failed
	 * 
	 * @return True if the task has status failed
	 */
	public boolean isFailed() {
		return status.isFailed();
	}

	/**
	 * Check if this task is finished
	 * 
	 * @return True if the task has status finished
	 */
	public boolean isFinished() {
		return status.isFinished();
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

	public void executeTask() {
		status.executeTask(this);
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
		setStatus(status);
	}

	public void delegateTask() {
		status.delegateTask(this);
	}

	protected void performDelegateTask(Status status) {
		setStatus(status);
	}

	public abstract void completeTask(boolean failed, DateTime startTime,
			DateTime endTime);

	/**
	 * Returns the total execution time for the task
	 * 
	 * @return The total execution time for the task
	 */
	public int getTotalExecutionTime() throws IllegalStateException {
		return status.calculateTotalExecutedTime(this);
	}

	protected int performGetTotalExecutionTime() throws IllegalStateException {
		return timeSpan.calculatePerformedTime();
	}

	/**
	 * Calculate the overdue percentage
	 * 
	 * @return Calculate the overdue percentage
	 */
	public int getOverduePercentage() throws IllegalStateException {
		return status.calculateOverDuePercentage(this);
	}

	protected int performGetOverduePercentage() throws IllegalStateException {
		int totalExecutedTime = getTotalExecutionTime();
		return (totalExecutedTime - estimatedDuration) * 100
				/ estimatedDuration;
	}

	/**
	 * Returns a boolean depending on whether the task is severely overdue or
	 * not
	 * 
	 * @return Returns true or false depending on whether the task is severely
	 *         overdue or not
	 */
	public boolean isSeverelyOverdue() {
		return status.isSeverelyOverdue(this);
	}

	protected boolean performIsSeverelyOverDue() {
		return getOverduePercentage() > getAcceptableDeviation();
	}

	/**
	 * Returns the timespan of the task
	 * 
	 * @return The timespan of the task
	 */
	public TimeSpan getTimeSpan() {
		return status.getTimeSpan(this);
	}

	protected TimeSpan performGetTimeSpan() {
		return new TimeSpan(timeSpan.getStartTime(), timeSpan.getEndTime());
	}

	protected void setTimeSpan(TimeSpan newTimeSpan) {
		timeSpan = newTimeSpan;
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

		status.addTimeSpan(this, failed, startTime, endTime);
	}

	protected void performAddTimeSpan(DateTime startTime, DateTime endTime)
			throws IllegalDateException {
		timeSpan = new TimeSpan(startTime, endTime);
	}

	private TimeSpan timeSpan;

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

	private LinkedHashMap<ResourceType, Integer> requiredResourceTypes = new LinkedHashMap<ResourceType, Integer>();

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
	 * fulfilled or not
	 * 
	 * @param resourceType
	 *            The resource type to be checked
	 * @return Returns true or false whether the resource type requirements are
	 *         fulfilled or not
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

	private List<Developer> requiredDevelopers = new ArrayList<Developer>();

	/**
	 * Returns a boolean whether the developers and resource types are available
	 * for this task or not
	 * 
	 * @param time
	 *            The date time for which this will be checked
	 * @return Returns true or false depending on whether the developers and
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

	protected void endReservations(DateTime endTime) {
		if (endTime == null)
			throw new IllegalArgumentException("The endTime is null.");
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

	/**
	 * Returns a boolean indicating whether the dependencies for this task are
	 * fulfilled or not
	 * 
	 * @return Returns true or false depending on whether the dependencies are
	 *         fulfilled or not
	 */
	public abstract boolean dependenciesAreFinished();

	/**
	 * Returns a boolean whether the alternative task is finished or not
	 * 
	 * @return Returns true or false depending on whether the alternative task
	 *         is finished or not
	 */
	public abstract boolean isAlternativeFinished();

	@Override
	public void update() {
		try {
			status.updateStatus(this, clock.getSystemTime());
		} catch (IllegalStateException ex) {
		}
	}

	protected abstract void performUpdateStatus(Status status);
}
