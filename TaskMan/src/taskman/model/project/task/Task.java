package taskman.model.project.task;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

public class Task extends Subject {

	/**
	 * The first constructor of task. This will create a task with the given
	 * parameters
	 * 
	 * @param description
	 *            The description of the task
	 * @param estimatedDuration
	 *            The estimated duration of the task
	 * @param acceptableDeviation
	 *            The acceptable deviation of the task
	 * @param dependencies
	 *            The list of dependencies of the task
	 * @post The new description is equal to the given description
	 * @post The new estimatedDuration is equal to the given estimatedDuration
	 * @post The new acceptableDeviation is equal to the given
	 *       acceptableDevaition
	 * @post The status of this task is Unavailable
	 * @post The new dependencies is equal to the given dependencies
	 * @throws NullPointerException
	 *             The description cannot be null
	 * @throws IllegalArgumentException
	 *             The estimatedDuration is negative
	 * @throws IllegalArgumentException
	 *             The acceptableDeviation is negative
	 * @throws NullPointerException
	 *             The dependencies is equal to null

	 */
	public Task(String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies,
			Task alternativeFor) throws IllegalStateException {
		if (description == null)
			throw new NullPointerException("Description is null");
		if (estimatedDuration <= 0)
			throw new IllegalArgumentException(
					"The estimated duration cannot be negative.");
		if (acceptableDeviation < 0)
			throw new IllegalArgumentException(
					"The deviation cannot be negative.");
		if (dependencies == null)
			throw new IllegalArgumentException("The dependencies are null.");

		this.description = description;
		this.estimatedDuration = estimatedDuration;
		this.acceptableDeviation = acceptableDeviation;
		this.status = new Unavailable();
		this.dependencies.addAll(dependencies);
		
		for (Task subject : this.dependencies) {
			try{
				subject.attachDependant(this);
			}
			catch(NullPointerException e){
				// hier doe niets
			}
			
		}
		
		if(alternativeFor != null)
			try{
				alternativeFor.addAlternative(this);
			}
			catch (IllegalStateException e){
				// dit moet verder doorgegeven worden -> nodig in UI
				throw e;
			}
			
		
		updateTaskAvailability();
	}

	private final String description;
	private final int estimatedDuration;
	private final int acceptableDeviation;
	private List<Task> dependencies = new ArrayList<Task>();
	// Tasks that are looking to me
	private List<Task> dependants = new ArrayList<Task>();
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
		return new ArrayList<Task>(dependencies);
	}

	/**
	 * Add dependant task
	 * 
	 * @param dependant
	 * @throws NullPointerException
	 *             The dependant task is null
	 * @post The list of depandants contains the given dependant task
	 */
	public void attachDependant(Task dependant){
		if (dependant == null)
			throw new NullPointerException("the dependant observer is null.");
		this.dependants.add(dependant);
	}

	/**
	 * When this task changed his status to Finished or failed, notify his
	 * dependants
	 * 
	 */
	private void notifyAllDependants() {
		for (Task dependant : this.dependants) {
			try {
				dependant.updateTaskAvailability();
			} catch (IllegalStateException e) {
			}
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
		return new TimeSpan(this.timeSpan.getStartTime(),
				this.timeSpan.getEndTime());
	}

	/**
	 * This will create a new timespan for the task
	 * 
	 * @param startTime
	 *            The start time of the timespan
	 * @param endTime
	 *            The end time of the timespan
	 * @throws NullPointerException
	 *             The startTime cannot be equal to null
	 * @throws NullPointerException
	 *             The endTime is equal to null
	 * @throws IllegalArgumentException
	 *             Exception will be thrown if the end time is earlier than the
	 *             start time
	 */
	public void addTimeSpan(boolean failed, DateTime startTime, DateTime endTime)
			throws IllegalArgumentException {
		if (startTime == null)
			throw new NullPointerException("The startTime is null.");
		if (endTime == null)
			throw new NullPointerException("The endTime is null.");
		if (startTime.compareTo(endTime) > 0)
			throw new IllegalDateException(
					"The startTime must start before endTime.");
		try{
			this.status.addTimeSpan(this, failed, startTime, endTime);
		}
		catch(IllegalStateException e){
			throw e;
		}
		
	}

	protected void performAddTimeSpan(DateTime startTime, DateTime endTime) {
		try{
		this.timeSpan = new TimeSpan(startTime, endTime);
		}
		catch(Exception e){
			throw e;
		}
		this.notifyAllDependants(); //notify dependant task
		this.notifyAllObservers(); //observer pattern for project
	}

	/**
	 * Returns the alternative task for the task
	 * 
	 * @return The alternative task for the task
	 */
	public Task getAlternative() {
		return alternative;
	}

	/**
	 * Add the alternative task for the task
	 *
	 * @param task
	 *            The new alternative task for this task
	 * @throws NullPointerException
	 *             The alternative task is equal to null
	 * 
	 */
	public void addAlternative(Task task) {
		if (task == null)
			throw new NullPointerException("The alternative is null.");
		try{
			this.status.addAlternative(this, task);
		}
		catch(IllegalStateException e){
			throw e;
		}
		catch(Exception e){
			
		}
	}

	protected void performAddAlternative(Task task) {
		this.alternative = task;
	}

	/**
	 * Check if this task is available
	 * 
	 * @return True if the task has status available
	 */
	public boolean isAvailable() {
		return this.status.isAvailable();
	}

	/**
	 * Check if this task is failed
	 * 
	 * @return True if the task has status failed
	 */
	public boolean isFailed() {
		return this.status.isFailed();
	}

	/**
	 * Check if this task is finished
	 * 
	 * @return True if the task has status finished
	 */
	public boolean isFinished() {
		return this.status.isFinished();
	}

	/**
	 * Check if this task has completed (either finished or failed).
	 * 
	 * @return True if the task is finished or failed.
	 */
	public boolean isCompleted() {
		if (isFinished() || isFailed())
			return true;
		else
			return false;
	}

	/**
	 * Set to status available
	 */
	public void updateTaskAvailability() throws IllegalStateException {
		try{
			this.status.updateTaskAvailability(this);
		}
		catch(IllegalStateException e){
			throw e;
		}
	}	

	protected void performUpdateTaskAvailability(Status status) {
		for( Task task : this.dependencies){
			try{
				if(!task.status.isAlternativeCompleted(task)) return;

			} catch(IllegalStateException e){
				if(!task.isFinished()) return;
			}

		}
		this.status = status;

	}

	protected boolean isAlternativeCompleted(){
		if(this.alternative == null) return false;
		if(this.alternative.isFinished()) return true;
		if(this.alternative.isFailed()) return this.alternative.status.isAlternativeCompleted(alternative);
		return false;

	}
	/**
	 * Returns the total execution time for the task
	 * 
	 * @return The total execution time for the task
	 */

	public int getTotalExecutionTime() throws IllegalStateException {
		int time;
		try{
			time = this.status.calculateTotalExecutedTime(this);
			
		}
		catch(IllegalStateException e){
			throw e;
		}
		return time;
		 
	}

	protected int performGetTotalExecutionTime() {
		int time;
		try{
		 time = this.timeSpan.calculatePerformedTime();
		} 
		catch( IllegalStateException e){
			throw e;
		}
		if (this.alternative != null)
			try {
				time = time + this.alternative.getTotalExecutionTime();
			} catch (IllegalStateException e) {
			}
		return time;
	}

	/**
	 * Calculate the overdue percentage
	 * 
	 * @return Calculate the overdue percentage
	 */
	public int getOverduePercentage() {
		return this.status.calculateOverDuePercentage(this);

	}

	protected int performGetOverduePercentage() {
		int totalExecutedTime;
		try{
			totalExecutedTime = this.performGetTotalExecutionTime();
		}
		catch (IllegalStateException e){
			throw e;
		}

		return (totalExecutedTime - this.estimatedDuration)
				/ this.estimatedDuration;
	}

}
