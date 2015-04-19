package taskman.model.resource;

import org.joda.time.DateTime;

import taskman.model.project.task.TimeSpan;

public class Reservation {

	TimeSpan timeSpan;

	public Reservation(DateTime startTime, DateTime endTime) {
		timeSpan = new TimeSpan(startTime, endTime);
	}

	public TimeSpan getTimeSpan() {
		return timeSpan;
	}

}
