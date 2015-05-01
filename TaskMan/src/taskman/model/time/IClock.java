package taskman.model.time;

import org.joda.time.DateTime;

public interface IClock {
	/**
	 * Will return the system time of the clock
	 * @return returns the systemtime of the clock
	 */
	public DateTime getSystemTime();
	/**
	 * Returns the first possible starting time
	 * @param time
	 * 			The time of which this will be calculated
	 * @return the first possible starting time
	 */
	public DateTime getFirstPossibleStartTime(DateTime time);
	/**
	 * This method will add breaks to the date time
	 * @param time
	 * 			The date Time to which the breaks will be added
	 * @return returns the date time with the added breaks
	 */
	public DateTime addBreaks(DateTime time);
	/**
	 * Adds a given amount of minutes to the date time
	 * @param time
	 * 			The datetime to which these minutes will be added
	 * @param minutesToAdd
	 * 			The amount of minutes to be added to the date time
	 * @return the date time will be returned with the minutes added
	 */
	public DateTime addMinutes(DateTime time, int minutesToAdd);
	/**
	 * Will return the exact hour of the given date time
	 * @param time
	 * 			The date time of which the exact hour will be calculated
	 * @return the exact hour of the parameter
	 */
	public DateTime getExactHour(DateTime time);
	/**
	 * Will return the parameter without the seconds and miliseconds
	 * @param time 
	 * 			The date time from which the seconds and miliseconds will be deleted
	 * @return returns the date time without the seconds and miliseconds
	 */
	public DateTime resetSecondsAndMilliSeconds(DateTime time);

}
