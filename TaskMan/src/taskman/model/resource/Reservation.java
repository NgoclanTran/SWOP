package taskman.model.resource;

import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class Reservation {

	Task task;
	TimeSpan timeSpan;

	public Reservation(Task task, TimeSpan timeSpan) {
		this.task = task;
		this.timeSpan = timeSpan;
	}

	public TimeSpan getTimeSpan() {
		return timeSpan;
	}

	public Task getTask() {
		return task;
	}

}
