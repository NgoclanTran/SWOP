package taskman.model.project.task;

import java.util.List;

import org.joda.time.DateTime;

public class Task {




	private final String description;
	private final int estimatedDuration;
	public Task(String description, int estimatedDuration,
			int acceptableDeviation) {
		super();
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
	}
	public Task(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		super();
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		this.dependencies = dependencies;
	}

	private int acceptableDeviation;
	private List<Task> dependencies;
	private Status status;
	private TimeSpan timeSpan;
	public String getDescription() {
		return description;
	}

	public int getEstimatedDuration() {
		return estimatedDuration;
	}

	public int getAcceptableDeviation() {
		return acceptableDeviation;
	}

	public List<Task> getDependencies() {
		return dependencies;
	}

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public TimeSpan getTimeSpan() {
		return timeSpan;
	}
	public void updateStatusAndTimeSpan(Status status, DateTime startTime, DateTime endTime){
		this.setStatus(status);
		this.timeSpan = new TimeSpan(startTime, endTime);
	}
}
