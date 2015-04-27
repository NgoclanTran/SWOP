package taskman.model.time;

import org.joda.time.DateTime;

public class Clock implements IClock {

	private DateTime systemTime;

	private static Clock systemClock = new Clock();

	public Clock() {
		systemTime = new DateTime(0);
	}

	public static Clock getInstance() {
		return systemClock;
	}

	public void setSystemTime(DateTime systemTime) {
		this.systemTime = systemTime;
	}

	public DateTime getSystemTime() {
		return systemTime;
	}
	
	public DateTime getFirstPossibleStartTime(DateTime time) {
		if (time.getHourOfDay() < 8) {
			time = time.plusMinutes((60 - time
					.getMinuteOfHour()) % 60);
			time = time.plusHours(8 - time
					.getHourOfDay());
		} else if (time.getHourOfDay() >= 17) {
			time = time.plusMinutes((60 - time
					.getMinuteOfHour()) % 60);
			time = time.plusHours((24 - time
					.getHourOfDay()) % 24);
			time = time.plusHours(8);
		}
		if (time.getDayOfWeek() == 6) {
			time = time.plusDays(1);
			time = time.plusMinutes((60 - time
					.getMinuteOfHour()) % 60);
			time = time.plusHours((24 - time
					.getHourOfDay()) % 24);
			time = time.plusHours(8);
		} else if (time.getDayOfWeek() == 7) {
			time = time.plusMinutes((60 - time
					.getMinuteOfHour()) % 60);
			time = time.plusHours((24 - time
					.getHourOfDay()) % 24);
			time = time.plusHours(8);
		}
		time = resetSecondsAndMilliSeconds(time);
		return time;
	}
	
	public DateTime addBreaks(DateTime time) {
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
	
	public DateTime addMinutes(DateTime time, int minutesToAdd) {
		while (minutesToAdd > 0) {
			time = time.plusMinutes(1);
			time = addBreaks(time);
			minutesToAdd -= 1;
		}
		return time;
	}
	
	public DateTime getExactHour(DateTime time) {
		time = time.plusMinutes((60 - time.getMinuteOfHour()) % 60);
		return resetSecondsAndMilliSeconds(time);
	}

	public DateTime resetSecondsAndMilliSeconds(DateTime time) {
		time = time.minusSeconds(time.getSecondOfMinute());
		time = time.minusMillis(time.getMillisOfSecond());
		return time;
	}

}
