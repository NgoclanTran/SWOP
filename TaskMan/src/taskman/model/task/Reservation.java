package taskman.model.task;

import taskman.model.time.TimeSpan;

public class Reservation {

	Task task;
	TimeSpan timeSpan;

	/**
	 * The constructor of the reservation class
	 * 
	 * @param task
	 *            The task for which this reservation will be made
	 * @param timeSpan
	 *            The timespan of the reservation
	 */
	public Reservation(Task task, TimeSpan timeSpan) {
		if (task == null)
			throw new NullPointerException("The given task is null.");
		if (timeSpan == null)
			throw new NullPointerException("The given timeSpan is null.");
		this.task = task;
		this.timeSpan = timeSpan;
	}

	/**
	 * Will return the timespan of the reservation
	 * 
	 * @return Returns the time span of the reservation
	 */
	public TimeSpan getTimeSpan() {
		return timeSpan;
	}

	/**
	 * Will return the task for which this reservation is made
	 * 
	 * @return Returns the task for which this reservation is made
	 */
	public Task getTask() {
		return task;
	}

}
