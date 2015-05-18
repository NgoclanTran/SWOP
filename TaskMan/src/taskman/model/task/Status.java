package taskman.model.task;

import org.joda.time.DateTime;

import taskman.model.time.TimeService;
import taskman.model.time.TimeSpan;

interface Status {

	TimeService timeService = new TimeService();

	/**
	 * Returns the name of the status
	 * 
	 * @return the name of the status
	 */
	public String getName();

	/**
	 * Returns whether the status is available or not
	 * 
	 * @return boolean depending on whether the status is available or not
	 */
	public boolean isAvailable();

	/**
	 * Returns whether the status is finished or not
	 * 
	 * @return boolean depending on whether the status is finished or not
	 */
	public boolean isFinished();

	/**
	 * Returns whether the status is failed or not
	 * 
	 * @return boolean depending on whether the status is failed or not
	 */
	public boolean isFailed();

	/**
	 * Returns whether the status is executing or not
	 * 
	 * @return boolean depending on whether the status is executing or not
	 */
	public boolean isExecuting();

	/**
	 * Returns whether the status is planned or not
	 * 
	 * @return boolean depending on whether the status is planned or not
	 */
	public boolean isPlanned();

	/**
	 * Will update the task status if possible
	 * 
	 * @param task
	 *            the task to be updated if possible
	 * @throws IllegalStateException
	 */
	public void updateStatus(Task task, DateTime currentTime)
			throws IllegalStateException;

	/**
	 * Returns the timespan of the task
	 * 
	 * @return The timespan of the task
	 */
	public TimeSpan getTimeSpan(Task task);

	/**
	 * Will add a time span to the given task if possible
	 * 
	 * @param task
	 *            The task for which this time span will be created
	 * @param failed
	 *            a boolean indication whether the task is failed or not
	 * @param startTime
	 *            the start time of the time span
	 * @param endTime
	 *            the end time of the time span
	 * @throws IllegalStateException
	 */
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException;

	/**
	 * Will check if the task given in the parameter has his alternatives
	 * complete
	 * 
	 * @param task
	 *            the task to be checked
	 * @return a boolean indicating whether a task's alternatives have been
	 *         completed or not
	 */
	public boolean isAlternativeFinished(Task task);

	/**
	 * Will add an alternative task to the given task
	 * 
	 * @param task
	 *            the task to which the alternative will be added
	 * @param alternative
	 *            the alternative to be added to the given task
	 * @throws IllegalStateException
	 */
	public void addAlternative(Task task, Task alternative)
			throws IllegalStateException;

	/**
	 * Returns a boolean indicating whether the given task is severly overdue or
	 * not. This will return true if the overdue percentage is higher than the
	 * acceptable deviation
	 * 
	 * @param task
	 *            The task which this will be checked for
	 * @return returns a boolean indicating whether the task is serverly overdue
	 *         or not
	 */
	public boolean isSeverelyOverdue(Task task);

	/**
	 * Will calculate the total executed time if possible
	 * 
	 * @param task
	 *            The task of which it will be calculated
	 * @return the total executed time
	 * @throws IllegalStateException
	 */
	public int calculateTotalExecutedTime(Task task)
			throws IllegalStateException;

	/**
	 * Will calculate the over due percentage of the given task
	 * 
	 * @param task
	 *            The task of which this will be calculated
	 * @return the over due percentage of the given task
	 * @throws IllegalStateException
	 */
	public int calculateOverDuePercentage(Task task)
			throws IllegalStateException;

	public void executeTask(Task task) throws IllegalStateException;
}
