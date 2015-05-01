package taskman.model.time;

import org.joda.time.DateTime;

import taskman.model.memento.ClockMemento;

public class Clock implements IClock {

	private DateTime systemTime;

	private static Clock systemClock = new Clock();
	/**
	 * The constructor of the class
	 */
	public Clock() {
		systemTime = new DateTime(0);
	}
	/**
	 * Will return the instance of the clock
	 * @return the instance of the clock
	 */
	public static Clock getInstance() {
		return systemClock;
	}
	/**
	 * Will set the system time of the clock to the given system time
	 * @param systemTime
	 * 			The system time to which the clock will be set
	 * @post the system time of the clock will be set to the parameter
	 */
	public void setSystemTime(DateTime systemTime) {
		if(systemTime == null) throw new IllegalArgumentException("The systemTime cannot be null.");
		this.systemTime = systemTime;
	}
	/**
	 * Will return the current system time of the clock
	 * @return the system time of the clock
	 */
	public DateTime getSystemTime() {
		return systemTime;
	}
	/**
	 * Returns the first possible starting time
	 * @param time
	 * 			The time of which this will be calculated
	 * @return the first possible starting time
	 */
	public DateTime getFirstPossibleStartTime(DateTime time) {
		if(time == null) throw new IllegalArgumentException("The time cannot be null.");
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
	 * @param time
	 * 			The date Time to which the breaks will be added
	 * @return returns the date time with the added breaks
	 */
	public DateTime addBreaks(DateTime time) {
		if(time == null) throw new IllegalArgumentException("The time cannot be null.");

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
	 * @param time
	 * 			The datetime to which these minutes will be added
	 * @param minutesToAdd
	 * 			The amount of minutes to be added to the date time
	 * @return the date time will be returned with the minutes added
	 */
	public DateTime addMinutes(DateTime time, int minutesToAdd) {
		while (minutesToAdd > 0) {
			time = time.plusMinutes(1);
			time = addBreaks(time);
			minutesToAdd -= 1;
		}
		return time;
	}
	/**
	 * Will return the exact hour of the given date time
	 * @param time
	 * 			The date time of which the exact hour will be calculated
	 * @return the exact hour of the parameter
	 */
	public DateTime getExactHour(DateTime time) {
		time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
		return resetSecondsAndMilliSeconds(time);
	}
	
	/**
	 * Will return the parameter without the seconds and miliseconds
	 * @param time 
	 * 			The date time from which the seconds and miliseconds will be deleted
	 * @return returns the date time without the seconds and miliseconds
	 */
	public DateTime resetSecondsAndMilliSeconds(DateTime time) {
		time = time.minusSeconds(time.getSecondOfMinute());
		time = time.minusMillis(time.getMillisOfSecond());
		return time;
	}
	/**
	 * Will return a new clockmemento with the systemtime of the current clock
	 * @return a new clockmemento object with the systemtime of the current clock
	 */
	public ClockMemento createMemento() {
		return new ClockMemento(systemTime);
	}
	/**
	 * Sets the system time of the clock to the state of the clockmemento parameter
	 * @param m
	 * 			The clockmemento object of which the state will be used
	 * @post the systemtime of the clock will be set to the state of the clockmemento object
	 */
	public void setMemento(ClockMemento m) {
		systemTime = m.getState();
	}
}
