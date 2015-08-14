package taskman.model.time;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;

public class DailyAvailability {

	LocalTime startTime, endTime;

	/**
	 * Creates a new daily availability with the given start and end time.
	 * 
	 * @param startTime
	 * 			The start time of the daily availability
	 * @param endTime
	 * 			The end time of the daily availability
	 * @throws IllegalTimeException
	 */
	public DailyAvailability(LocalTime startTime, LocalTime endTime)
			throws IllegalTimeException {
		if(startTime == null) throw new NullPointerException("The given startTime is null.");
		if(endTime == null) throw new NullPointerException("The given endTime is null.");
		if (startTime.isAfter(endTime))
			throw new IllegalTimeException(
					"Start time has to be before end time.");
		this.startTime = startTime;
		this.endTime = endTime;
	}
	/**
	 * Will return the start time of the daily availability
	 * @return returns the start time of the daily availability
	 */
	public LocalTime getStartTime(){
		return new LocalTime(startTime.getHourOfDay(), startTime.getMinuteOfHour());
	}
	/**
	 * Will return the end time of the daily availability
	 * @return returns the end time of the daily availability
	 */
	public LocalTime getEndTime(){
		return new LocalTime(endTime.getHourOfDay(), endTime.getMinuteOfHour());
	}
	/**
	 * Will return a boolean whether the timespan is valid within this daily availability.
	 * 
	 * @param timeSpan
	 * 			The timespan of which this will be checked
	 * @return returns true or false whether the timespan falls within the daily availability or not
	 */
	public boolean isValidTimeSpan(TimeSpan timeSpan) {
		if (timeSpan.getStartTime().getHourOfDay() < startTime.getHourOfDay() || timeSpan.getStartTime().getHourOfDay() > endTime.getHourOfDay())
			return false;
		return true;
	}

}
