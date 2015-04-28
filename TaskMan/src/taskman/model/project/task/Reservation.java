package taskman.model.project.task;

import taskman.model.time.TimeSpan;

public class Reservation {

	Task task;
	TimeSpan timeSpan;

	public Reservation(Task task, TimeSpan timeSpan) {
		if(task == null) throw new NullPointerException("The given task is null.");
		if(timeSpan == null) throw new NullPointerException("The given timeSpan is null.");
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
