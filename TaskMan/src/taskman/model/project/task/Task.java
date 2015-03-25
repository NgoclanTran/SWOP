 package taskman.model.project.task;

import java.util.List;

import org.joda.time.DateTime;

public class Task {



	/**
	 * The first constructor of task. This will create a task with the given parameters without dependencies
	 * 
	 * @param description
	 * 			The description of the task
	 * @param estimatedDuration
	 * 			The estimated duration of the task
	 * @param acceptableDeviation
	 * 			The acceptable deviation of the task
	 */
	public Task(String description, int estimatedDuration,
			int acceptableDeviation) {
		super();
		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		this.status = Status.UNAVAILABLE;
	}
	/**
	 * The second constructor of task. This will create a task with the given parameters with a list of dependencies
	 * 
	 * @param description
	 * 			The description of the task
	 * @param estimatedDuration
	 * 			The estimated duration of the task
	 * @param acceptableDeviation
	 * 			The acceptable deviation of the task
	 * @param dependencies
	 * 			The list of dependencies of the task
	 */
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
	
	/**
	 * Returns the description of the task
	 * 
	 * @return The description of the task
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Returns the estimated duration of the task
	 * 
	 * @return The estimated duration of the task
	 */
	public int getEstimatedDuration() {
		return estimatedDuration;
	}
	/**
	 * Returns the acceptable deviation of the task
	 * 
	 * @return The acceptable deviation of the task
	 */
	public int getAcceptableDeviation() {
		return acceptableDeviation;
	}
	/**
	 * Returns the list of dependencies of the task
	 * 
	 * @return The list of dependencies of the task
	 */
	public List<Task> getDependencies() {
		return dependencies;
	}
	/**
	 * Returns the status of the task
	 * 
	 * @return The status of the task
	 */
	public Status getStatus() {
		return status;
	}
//	public void setStatus(Status status) {
//		this.status = status;
//	}
	/**
	 * Returns the timespan of the task
	 * 
	 * @return The timespan of the task
	 */
	public TimeSpan getTimeSpan() {
		return timeSpan;
	}
	/**
	 * This will create a new timespan for the task
	 * 
	 * @param startTime
	 * 			The start time of the timespan
	 * @param endTime
	 * 			The end time of the timespan
	 * @throws IllegalArgumentException
	 * 			Exception will be thrown if the end time is earlier than the start time
	 */
	public void setTimeSpan(DateTime startTime, DateTime endTime) throws IllegalArgumentException {
		this.timeSpan = new TimeSpan(startTime, endTime);
		
	}
	/**
	 * This will update the status of the task to the correct value
	 */
	public void updateStatus(){
		if(this.status == Status.UNAVAILABLE){
			for(Task task : this.dependencies){
				if(!task.isFullfilled()) return;
			}
			this.setStatus(Status.AVAILABLE);
		}
		
	}
	/**
	 * Returns a true or false depending on whether the task is finished or it's alternative is finished, or not
	 * 
	 * @return True or false depending on whether the task is finished or it's alternative, or not
	 */
	private boolean isFullfilled(){
		if(this.alternative  == null && this.status == Status.FINISHED)
			return true;
		if(this.alternative != null) return this.alternative.isFullfilled();
		return false;
	}
	/**
	 * This will update the status and timespan to the given parameters
	 * 
	 * @param status
	 * 			The new status for the task
	 * @param startTime
	 * 			The start time for the new timespan of the task
	 * @param endTime
	 * 			The end time for the new timespan of the task
	 */
	public void updateStatusAndTimeSpan(Status status, DateTime startTime, DateTime endTime){
		this.setStatus(status);
		this.timeSpan = new TimeSpan(startTime, endTime);
	}
	/**
	 * Returns the alternative task for the task
	 * 
	 * @return The alternative task for the task
	 */
	public Task getAlternative(){
		return alternative;
	}
	
	//naam veranderd, add lijkt alsof het meerdere kan hebben
	/**
	 * Sets an alternative task for this task
	 * 
	 * @param task
	 * 			The task that will be set as an alternative for the task
	 */
	public void setAlternative(Task task){
		if(this.status != Status.FAILED) throw new IllegalArgumentException("Task is not failed.");
		this.alternative = task;
		
	}
	/**
	 * Returns the total execution time for the task
	 * 
	 * @return The total execution time for the task
	 */
	public int calculateTotalExecutionTime(){
		int time = this.timeSpan.calculatePerformedTime();
		if(this.alternative != null)
			time = time + this.alternative.calculateTotalExecutionTime();
		return time;
		
	}
}
