package taskman.model.time;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

public class TimeSpan {

	TimeService timeService = new TimeService();

	/**
	 * The constructor of time span
	 * 
	 * @param startTime
	 *            The given start time of the time span
	 * @param endTime
	 *            The given end time of the time span
	 */
	public TimeSpan(DateTime startTime, DateTime endTime)
			throws IllegalArgumentException, IllegalDateException {
		if (startTime.compareTo(endTime) > 0)
			throw new IllegalArgumentException(
					"The end time cannot be later than de start time.");
		if (startTime.getHourOfDay() < 8)
			throw new IllegalDateException("The day starts at 8:00");
		if (endTime.getHourOfDay() > 17)
			throw new IllegalDateException("The day ends at 17:00");
		if (startTime.getDayOfWeek() > 5 || endTime.getDayOfWeek() > 5)
			throw new IllegalDateException("Only working days are permitted.");
		this.startTime = startTime;
		this.endTime = endTime;
	}

	private final DateTime startTime;

	/**
	 * Will return the start time of the time span
	 * 
	 * @return The start time of the time span
	 */
	public DateTime getStartTime() {
		return this.startTime;
	}

	private final DateTime endTime;

	/**
	 * Will return the end time of the time span
	 * 
	 * @return The end time of the time span
	 */
	public DateTime getEndTime() {
		return this.endTime;
	}
	/**
	 * Will return true or false depending on whether the parameter date time falls within the
	 * timespan or not
	 * @param time
	 * 			the date time parameter that will be used.
	 * @return returns true or false depending on whether the given parameter falls within the 
	 * 		   timespan or not
	 */
	public boolean isDuringTimeSpan(DateTime time) {
		if (time.isBefore(getStartTime()) || time.isAfter(getEndTime()))
			return false;
		else
			return true;
	}

	/**
	 * Will return the performed time of the time span
	 * 
	 * @return The performed time of the time span
	 */
	public int calculatePerformedTime() {
		int minutesSpent = 0;
		DateTime start = this.startTime;
		DateTime end = this.endTime;
		while (start.isBefore(end)) {
			minutesSpent++;
			start = start.plusMinutes(1);
			start = timeService.addBreaks(start);
		}

		return minutesSpent;
	}
}
