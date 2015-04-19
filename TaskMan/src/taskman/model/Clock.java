package taskman.model;

import org.joda.time.DateTime;

public class Clock {

	DateTime systemTime;

	public Clock(DateTime systemTime) {
		this.systemTime = new DateTime(systemTime);
	}

	public void setSystemTime(DateTime systemTime) {
		this.systemTime = systemTime;
	}

	public DateTime getSystemTime() {
		return systemTime;
	}

}
