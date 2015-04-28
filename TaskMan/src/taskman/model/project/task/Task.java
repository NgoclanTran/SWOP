package taskman.model.project.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.resource.Reservation;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.TimeSpan;

public class Task extends Subject {

	/**
	 * The first constructor of task. This will create a task with the given
	 * parameters
	 * 
	 * @param description
	 *            The description of the task
	 * @param estimatedDuration
	 *            The estimated duration of the task
	 * @param acceptableDeviation
	 *            The acceptable deviation of the task
	 * @param dependencies
	 *            The list of dependencies of the task
	 * @post The new description is equal to the given description
	 * @post The new estimatedDuration is equal to the given estimatedDuration
	 * @post The new acceptableDeviation is equal to the given
	 *       acceptableDevaition
	 * @post The status of this task is Unavailable
	 * @post The new dependencies is equal to the given dependencies
	 * @throws NullPointerException
	 *             The description cannot be null
	 * @throws IllegalArgumentException
	 *             The estimatedDuration is negative
	 * @throws IllegalArgumentException
	 *             The acceptableDeviation is negative
	 */
	public Task(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies,
			Task alternativeFor, Map<ResourceType, Integer> resourceTypes)
			throws IllegalStateException {
		if (description == null)
			throw new NullPointerException("Description is null");
		if (estimatedDuration <= 0)
			throw new IllegalArgumentException(
					"The estimated duration cannot be negative.");
		if (acceptableDeviation < 0)
			throw new IllegalArgumentException(
					"The deviation cannot be negative.");
		if (dependencies == null)
			throw new NullPointerException("The dependencies cannot be null.");

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
			updateTaskAvailability();
		} catch (IllegalStateException e) {

		}

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
	private HashMap<ResourceType, Integer> requiredResourceTypes = new HashMap<ResourceType, Integer>();

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
	 * @throws NullPointerException
	 *             The dependant task is null
	 * @post The list of depandants contains the given dependant task
	 */
	public void attachDependant(Task dependant) {
		if (dependant == null)
			throw new NullPointerException("the dependant observer is null.");
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
				dependant.updateTaskAvailability();
			} catch (IllegalStateException e) {
			}
		}
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
	 * Returns the timespan of the task
	 * 
	 * @return The timespan of the task
	 */
	public TimeSpan getTimeSpan() {
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
	 * @throws NullPointerException
	 *             The startTime cannot be equal to null
	 * @throws NullPointerException
	 *             The endTime is equal to null
	 * @throws IllegalArgumentException
	 *             Exception will be thrown if the end time is earlier than the
	 *             start time
	 */
	public void addTimeSpan(boolean failed, DateTime startTime, DateTime endTime)
			throws IllegalArgumentException, IllegalDateException,
			NullPointerException {
		if (startTime == null)
			throw new NullPointerException("The startTime is null.");
		if (endTime == null)
			throw new NullPointerException("The endTime is null.");

		this.status.addTimeSpan(this, failed, startTime, endTime);

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
	 * @throws NullPointerException
	 *             The alternative task is equal to null
	 * 
	 */
	public void addAlternative(Task task) throws IllegalStateException {
		if (task == null)
			throw new NullPointerException("The alternative is null.");

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
		return new HashMap<ResourceType, Integer>(requiredResourceTypes);
	}

	/**
	 * Adds the resource type if the required resource types are already added
	 * and no conflicting resource type has been added.
	 * 
	 * @param resourceType
	 * @param amount
	 */
	public void addRequiredResourceType(ResourceType resourceType, int amount)
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

	private boolean hasEnougResources(ResourceType resourceType, int amount) {
		if (resourceType.getResources().size() < amount)
			return false;
		else
			return true;
	}

	private boolean checkResourceTypeConflicts(ResourceType resourceType) {
		List<ResourceType> conflicts = resourceType.getConflictsWith();
		if (conflicts != null) {
			for (ResourceType conflictingResourceType : conflicts) {
				if (requiredResourceTypes.containsKey(conflictingResourceType)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkResourceTypeSelfConflicting(ResourceType resourceType,
			int amount) {
		if (resourceType.isSelfConflicting() && amount > 1) {
			return true;
		}
		return false;
	}

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
	 * Check if this task is available
	 * 
	 * @return True if the task has status available
	 */
	public boolean isAvailable() {
		return this.status.isAvailable();
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

	/**
	 * Set to status available
	 */
	public void updateTaskAvailability() throws IllegalStateException {
		this.status.updateTaskAvailability(this);
	}

	protected void performUpdateTaskAvailability(Status status) {
		for (Task task : this.dependencies) {
			try {
				if (!task.status.isAlternativeCompleted(task))
					return;

			} catch (IllegalStateException e) {
				if (!task.isFinished())
					return;
			}

		}
		this.status = status;

		this.notifyAllDependants(); // notify dependant task
		this.notifyAllObservers(); // observer pattern for project
	}

	protected boolean isAlternativeCompleted() {
		if (this.alternative == null)
			return false;
		if (this.alternative.isFinished())
			return true;
		if (this.alternative.isFailed())
			return this.alternative.status.isAlternativeCompleted(alternative);
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

		// TODO: is dit nodig?
		if (this.alternative != null)
			try {
				time = time + this.alternative.getTotalExecutionTime();
			} catch (IllegalStateException e) {
			}
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

	public boolean isSeverelyOverdue() {
		return this.status.isSeverelyOverdue(this);
	}

	protected boolean performIsSeverelyOverDue() {
		return getOverduePercentage() > getAcceptableDeviation();
	}

	public boolean isPlanned() {
		return status.isPlanned(this);
	}

	protected boolean performIsPlanned() {
		boolean planned = false;

		if (getRequiredResourceTypes().size() > 0) {
			for (Entry<ResourceType, Integer> entry : getRequiredResourceTypes()
					.entrySet()) {
				for (Resource resource : entry.getKey().getResources()) {
					for (Reservation reservation : resource.getReservations()) {
						if (reservation.getTask().equals(this)) {
							planned = true;
							break;
						}
					}
				}
			}
		}

		return planned;
	}

}
