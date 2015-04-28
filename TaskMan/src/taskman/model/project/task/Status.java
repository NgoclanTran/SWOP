package taskman.model.project.task;

import org.joda.time.DateTime;

interface Status{
/**
 * Returns the name of the status
 * 
 * @return the name of the status
 */
public String getName();

/**
 * Will add an alternative task to the given task
 * 
 * @param task
 * 			the task to which the alternative will be added
 * @param alternative
 * 			the alternative to be added to the given task
 * @throws IllegalStateException
 */
public void addAlternative(Task task, Task alternative) throws IllegalStateException;

/**
 * Returns whether the status is available or not
 * 
 * @return boolean depending on whether the status is available or not
 */
public boolean isAvailable();

/**
 * Returns whether the status is finished or not
 * 
 * @return boolean depending on whether the status is finished or not
 */
public boolean isFinished();

/**
 * Returns whether the status is failed or not
 * 
 * @return boolean depending on whether the status is failed or not
 */
public boolean isFailed();

/**
 * Will update the task availability if possible
 * 
 * @param task
 * 			the task to be updated if possible
 * @throws IllegalStateException
 */
public void updateTaskAvailability(Task task) throws IllegalStateException;

/**
 * Will calculate the total executed time if possible
 * 
 * @param task
 * 			The task of which it will be calculated
 * @return the total executed time
 * @throws IllegalStateException
 */
public int calculateTotalExecutedTime(Task task) throws IllegalStateException;

/**
 * Will calculate the over due percentage of the given task
 * 
 * @param task
 * 			The task of which this will be calculated
 * @return the over due percentage of the given task
 * @throws IllegalStateException
 */
public int calculateOverDuePercentage(Task task) throws IllegalStateException;

/**
 * Will add a time span to the given task if possible
 * 
 * @param task
 * 			The task for which this time span will be created
 * @param failed
 * 			a boolean indication whether the task is failed or not
 * @param startTime
 * 			the start time of the time span
 * @param endTime
 * 			the end time of the time span
 * @throws IllegalStateException
 */
public void addTimeSpan(Task task, boolean failed, DateTime startTime, DateTime endTime) throws IllegalStateException;
/**
 * Will check if the task given in the parameter has his alternatives complete
 * 
 * @param task
 * 			the task to be checked
 * @return
 * 			a boolean indicating whether a task's alternatives have been completed or not
 */
public boolean isAlternativeCompleted(Task task);

public boolean isSeverelyOverdue(Task task);

public boolean isPlanned(Task task);

}


