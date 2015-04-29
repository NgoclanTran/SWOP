package taskman.model.time;

import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;

public class DailyAvailability {

	LocalTime startTime, endTime;

	/**
	 * Creates a new daily availability with the given start and end time.
	 * 
	 * @param startTime
	 * @param endTime
	 * 
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
	 
	public LocalTime getStartTime(){
		return new LocalTime(startTime.getHourOfDay(), startTime.getMinuteOfHour());
	}
	
	public LocalTime getEndTime(){
		return new LocalTime(endTime.getHourOfDay(), endTime.getMinuteOfHour());
	}
	
	public boolean isValidTimeSpan(TimeSpan timeSpan) {
		if (timeSpan.getStartTime().getHourOfDay() < startTime.getHourOfDay() || timeSpan.getStartTime().getHourOfDay() > endTime.getHourOfDay())
			return false;
		return true;
	}

}
