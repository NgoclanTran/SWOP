package taskman.model.resource;

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
		if (startTime.isAfter(endTime))
			throw new IllegalTimeException(
					"Start time has to be before end time.");
		this.startTime = startTime;
		this.endTime = endTime;
	}

}
