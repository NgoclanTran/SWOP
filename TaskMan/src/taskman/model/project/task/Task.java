package taskman.model.project.task;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

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
	 * @throws	NullPointerException
	 * 			The description cannot be null		
	 * @throws	IllegalArgumentException
	 * 			The estimatedDuration cannot be negatif
	 * @throws	IllegalArgumentException
	 * 			The acceptableDeviation cannot be negatif
	 * @post	The new description is equal to the given descrription
	 * @post	The new estimatedDuration is equal to the given estimatedDuration
	 * @post 	The new acceptableDeviation is equal to the given acceptableDevaition
	 * @post	The status of this task is Unavailable
	 */
	public Task(String description, int estimatedDuration,
			int acceptableDeviation) {
		if(description == null) throw new NullPointerException("Description is null");
		if(estimatedDuration <= 0) throw new IllegalArgumentException("The estimated duration cannot be negatif.");
		if(acceptableDeviation < 0) throw new IllegalArgumentException("The deviation cannot be negatif.");

		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		this.status = new Unavailable();
		this.dependants = new ArrayList<Task>();
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
	 * @throws	NullPointerException
	 * 			The description cannot be null		
	 * @throws	IllegalArgumentException
	 * 			The estimatedDuration is negatif
	 * @throws	IllegalArgumentException
	 * 			The acceptableDeviation is negatif
	 * @throws	NullPointerException
	 * 			The dependencies is equal to null
	 * @post	The new description is equal to the given descrription
	 * @post	The new estimatedDuration is equal to the given estimatedDuration
	 * @post 	The new acceptableDeviation is equal to the given acceptableDevaition
	 * @post	The status of this task is Unavailable
	 * @post 	The new dependencies is equal to the given dependencies
	 */
	public Task(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		this(description, estimatedDuration, acceptableDeviation);
		if(dependencies == null) throw new IllegalArgumentException("The dependencies are null.");

		this.dependencies = new ArrayList<Task>();
		this.dependencies.addAll(dependencies);
		for(Task subject: this.dependencies){
			subject.attachDependant(this);
		}
		
		
	}

	private final String description;
	private final int estimatedDuration;
	private int acceptableDeviation;
	private List<Task> dependencies;
	//Tasks that are looking to me (obsevers)
	private List<Task> dependants;
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
	 * Add dependant task
	 * @param dependant
	 * @throws	NullPointerException
	 * 			The dependant task is null
	 * @post 	The list of depandants contains the given dependant task
	 */
	public void attachDependant(Task dependant){
		if(dependant == null) throw new NullPointerException("the dependant observer is null.");
		this.dependants.add(dependant);
	}
	
	/**
	 * When this task changed his status to Finished or failed, notify his dependants
	 * 
	 */
	
	//moet public?
	public void notifyAllDependants(){
		for(Task observer: this.dependants){
			observer.updateTaskAvaibality();
		}
	}
	/**
	 * Returns the status of the task
	 * 
	 * @return The status of the task
	 */
	public String getStatusName() {
		return this.status.getName();
	}
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
	 * @throws	NullPointerException
	 * 			The startTime cannot be equal to null
	 * @throws	NullPointerException
	 * 			The endTime is equal to null
	 * @throws 	IllegalArgumentException
	 * 			Exception will be thrown if the end time is earlier than the start time
	 */
	public void addTimeSpan(boolean failed, DateTime startTime, DateTime endTime) throws IllegalArgumentException {
		if(startTime == null) throw new NullPointerException("The startTime is null.");
		if(endTime == null) throw new NullPointerException("The endTime is null.");
		if(startTime.compareTo(endTime) > 0) throw new IllegalDateException("The startTime must start before endTime.");
		this.status.addTimeSpan(this, failed, startTime, endTime);
		this.notifyAllDependants();

	}
	protected void performAddTimeSpan(DateTime startTime, DateTime endTime){
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

	/**
	 * Add the alternatif task for the task
	 *
	 * @param 	task
	 * 			The new alternative task for this task
	 * @throws	NullPointerException
	 * 			The alternative task is equal to null
	 * 
	 */
	public void addAlternative(Task task){
		if(task == null) throw new NullPointerException("The alternative is null.");
		this.status.addAlternative(this, task);
	}
	protected void performAddAlternative(Task task){
		this.alternative = task;
	}

	/**
	 * Check if this task is available
	 * 
	 * @return
	 * 			True if the task has status available
	 */
	public boolean isAvailable(){
		return this.status.isAvailable();
	}

	/**
	 * Check if this task is failed
	 * 
	 * @return
	 * 			True if the task has status failed
	 */
	public boolean isFailed(){
		return this.status.isFailed();
	}

	/**
	 * Check if this task is finished
	 * 
	 * @return
	 * 			True if the task has status finished
	 */
	public boolean isFinished(){
		return this.status.isFinished();
	}	
	/**
	 * Set to status available
	 */
	public void updateTaskAvaibality() throws IllegalStateException{
		this.status.updateTaskAvailability(this);
	}
	protected void performUpdateTaskAvailability(Status status){
		this.status = status;
	}
	/**
	 * Will set the status of this task to the given parameter
	 * 
	 * @param status
	 * 			The status to be used
	 */
	public void updateStatus(Status status){
		this.status = status;
	}
	

	/**
	 * Returns the total execution time for the task
	 * 
	 * @return The total execution time for the task
	 */
	
	public int getTotalExecutionTime() throws IllegalStateException{
		return this.status.calculateTotalExecutedTime(this);
	}
	protected int performGetTotalExecutionTime(){
		int time = this.timeSpan.calculatePerformedTime();
		if(this.alternative != null)
			time = time + this.alternative.getTotalExecutionTime();
		return time;
	}
	/**
	 * Calculate de overdue percentage
	 * 
	 * @return	Calculate de overdue percentage
	 */
	public int getOverduePercentage(){
		return this.status.calculateOverDuePercentage(this);

	}
	protected int performGetOverduePercentage(){
		int totalExecutedTime = this.performGetTotalExecutionTime();

		return (totalExecutedTime - this.estimatedDuration)/this.estimatedDuration;
	}
}

