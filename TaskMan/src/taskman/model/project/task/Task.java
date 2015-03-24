 package taskman.model.project.task;

import java.util.List;

import org.joda.time.DateTime;

public class Task {




	public Task(String description, int estimatedDuration,
			int acceptableDeviation) {
		super();
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		this.status = Status.UNAVAILABLE;
	}
	public Task(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		super();
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		for(Task task: dependencies){
			if(!task.getTimeSpan().isBefore(this.timeSpan)) throw new IllegalArgumentException("The given denpendent task cannot start later");	
		}
		this.dependencies = dependencies;
		this.status = Status.UNAVAILABLE;
	}

	private final String description;
	private final int estimatedDuration;
	private int acceptableDeviation;
	private List<Task> dependencies;
	private Status status;
	private TimeSpan timeSpan;
	private Task alternative = null;
	
	
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

//	public Status getStatus() {
//		return status;
//	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public TimeSpan getTimeSpan() {
		return timeSpan;
	}
	public void setTimeSpan(DateTime startTime, DateTime endTime) throws IllegalArgumentException {
		this.timeSpan = new TimeSpan(startTime, endTime);
		
	}
	public void updateStatus(){
		if(this.status == Status.UNAVAILABLE){
			for(Task task : this.dependencies){
				if(!task.isFullfilled()) return;
			}
			this.setStatus(Status.AVAILABLE);
		}
		
	}
	private boolean isFullfilled(){
		if(this.alternative  == null && this.status == Status.FINISHED)
			return true;
		if(this.alternative != null) return this.alternative.isFullfilled();
		return false;
	}
	public void updateStatusAndTimeSpan(Status status, DateTime startTime, DateTime endTime){
		this.setStatus(status);
		this.timeSpan = new TimeSpan(startTime, endTime);
	}
	
	public Task getAlternative(){
		return alternative;
	}
	
	//naam veranderd, add lijkt alsof het meerdere kan hebben
	public void setAlternative(Task task){
		if(this.status != Status.FAILED) throw new IllegalArgumentException("Task is not failed.");
		this.alternative = task;
		
	}
	public int calculateTotalExecutionTime(){
		int time = this.timeSpan.calculatePerformedTime();
		if(this.alternative != null)
			time = time + this.alternative.calculateTotalExecutionTime();
		return time;
		
	}
}
