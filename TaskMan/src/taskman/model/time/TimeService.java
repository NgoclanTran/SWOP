package taskman.model.time;

import org.joda.time.DateTime;

public class TimeService {

	/**
	 * The constructor of the time service.
	 */
	public TimeService() {

	}

	/**
	 * Returns the first possible starting time
	 * 
	 * @param time
	 *            The time of which this will be calculated
	 * @return the first possible starting time
	 */
	public DateTime getFirstPossibleStartTime(DateTime time) {
		if (time == null)
			throw new IllegalArgumentException("The time cannot be null.");
		if (time.getHourOfDay() < 8) {
			time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
			time = time.plusHours(8 - time.getHourOfDay());
		} else if (time.getHourOfDay() >= 17) {
			time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
			time = time.plusHours((24 - time.getHourOfDay()) % 24);
			time = time.plusHours(8);
		}
		if (time.getDayOfWeek() == 6) {
			time = time.plusDays(1);
			time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
			time = time.plusHours((24 - time.getHourOfDay()) % 24);
			time = time.plusHours(8);
		} else if (time.getDayOfWeek() == 7) {
			time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
			time = time.plusHours((24 - time.getHourOfDay()) % 24);
			time = time.plusHours(8);
		}
		time = resetSecondsAndMilliSeconds(time);
		return time;
	}

	/**
	 * This method will add breaks to the date time
	 * 
	 * @param time
	 *            The date Time to which the breaks will be added
	 * @return returns the date time with the added breaks
	 */
	public DateTime addBreaks(DateTime time) {
		if (time == null)
			throw new IllegalArgumentException("The time cannot be null.");

		if (time.getHourOfDay() == 11 && time.getMinuteOfHour() > 0) {
			time = time.plusHours(1);
		}
		if (time.getHourOfDay() == 17 && time.getMinuteOfHour() == 0) {
			time = time.plusHours(15);
		}
		if (time.getDayOfWeek() > 5) {
			time = time.plusDays(8 - time.getDayOfWeek());
		}

		return time;
	}

	/**
	 * Adds a given amount of minutes to the date time
	 * 
	 * @param time
	 *            The datetime to which these minutes will be added
	 * @param minutesToAdd
	 *            The amount of minutes to be added to the date time
	 * 
	 * @return the date time will be returned with the minutes added
	 */
	public DateTime addMinutes(DateTime time, int minutesToAdd) {
		if(time == null) throw new IllegalArgumentException("The time cannot be null.");
		while (minutesToAdd > 0) {
			time = time.plusMinutes(1);
			time = addBreaks(time);
			minutesToAdd -= 1;
		}
		return time;
	}

	/**
	 * This method will remove breaks from the date time
	 * 
	 * @param time
	 *            The date time from which the breaks will be removed
	 * @return returns the date time with the removed breaks
	 */
	private DateTime removeBreaks(DateTime time) {
		if (time == null)
			throw new IllegalArgumentException("The time cannot be null.");
		if (time.getHourOfDay() == 8 && time.getMinuteOfHour() == 0) {
			time = time.minusHours(15);
		}
		if (time.getHourOfDay() < 8) {
			time = time.minusMinutes(420 + time.getMinuteOfDay());
		}
		if (time.getDayOfWeek() > 5) {
			time = time.minusDays(time.getDayOfWeek() - 5);
		}
		if (time.getHourOfDay() == 12 && time.getMinuteOfHour() == 0) {
			time = time.minusHours(1);
		}
		return time;
	}

	/**
	 * Removes a given amount of minutes from the date time
	 * 
	 * @param time
	 *            The datetime from which these minutes will be removed
	 * @param minutesToAdd
	 *            The amount of minutes to be removed from the date time
	 * 
	 * @return the date time will be returned with the minutes removed
	 */
	public DateTime subtractMinutes(DateTime time, int minutesToSubtract) {
		if(time == null) throw new IllegalArgumentException("The time cannot be null.");
		while (minutesToSubtract > 0) {
			time = time.minusMinutes(1);
			time = removeBreaks(time);
			minutesToSubtract -= 1;
		}
		return time;
	}

	/**
	 * Will return the exact hour of the given date time
	 * 
	 * @param time
	 *            The date time of which the exact hour will be calculated
	 * 
	 * @return the exact hour of the parameter
	 */
	public DateTime getExactHour(DateTime time) {
		if(time == null) throw new IllegalArgumentException("The time cannot be null.");
		time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
		return resetSecondsAndMilliSeconds(time);
	}

	/**
	 * Will return the parameter without the seconds and miliseconds
	 * 
	 * @param time
	 *            The date time from which the seconds and miliseconds will be
	 *            deleted
	 * @return returns the date time without the seconds and miliseconds
	 */
	public DateTime resetSecondsAndMilliSeconds(DateTime time) {
		time = time.minusSeconds(time.getSecondOfMinute());
		time = time.minusMillis(time.getMillisOfSecond());
		return time;
	}

}
