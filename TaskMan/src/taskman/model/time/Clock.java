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

}
