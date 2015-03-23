package taskman.model.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

public class Project {
	
	/**
	 * Creates a new project object.
	 * 
	 * @param name
	 * @param description
	 * @param creationTime
	 * @param dueTime
	 */
	public Project(String name, String description, DateTime creationTime, DateTime dueTime) {
		if (name == null) throw new IllegalArgumentException("Null not allowed as name.");
		if (description == null) throw new IllegalArgumentException("Null not allowed as description.");
		if (creationTime == null) throw new IllegalArgumentException("Null not allowed as creation time.");
		if (dueTime == null) throw new IllegalArgumentException("Null not allowed as due time.");
		if (dueTime.isBefore(creationTime)) throw new IllegalDateException("Due time cannot be before creation time.");
		this.name = name;
		this.description = description;
		this.creationTime = creationTime;
		this.dueTime = dueTime;
		this.tasks = new ArrayList<Task>();
	}
	
	private ArrayList<Task> tasks;
	
	/**
	 * Returns the name of the project.
	 * 
	 * @return Returns the name of the project.
	 */
	public String getName(){
		return this.name;
	}
	
	private final String name;

	/**
	 * Returns the description of the project.
	 * 
	 * @return Returns the description of the project.
	 */
	public String getDescription(){
		return this.description;
	}
	
	private final String description;

	/**
	 * Returns the creation time of the project.
	 * 
	 * @return Returns the creation time of the project.
	 */
	public DateTime getCreationTime(){
		return this.creationTime;
	}
	
	private final DateTime creationTime;

	/**
	 * Returns the due time of the project.
	 * 
	 * @return Returns the due time of the project.
	 */
	public DateTime getDueTime(){
		return this.dueTime;
	}
	
	private final DateTime dueTime;

	/**
	 * Returns the list of tasks of the project.
	 * 
	 * @return Returns the list of tasks of the project.
	 */
	public List<Task> getTasks(){
		return new ArrayList<Task>(tasks);
	}
	
	/**
	 * Creates a task without dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 */
	public void makeTask(String description, int estimatedDuration, int acceptableDeviation){
		Task task = new Task(description, estimatedDuration, acceptableDeviation);
		tasks.add(task);
	}
	
	/**
	 * Creates a task with dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 */
	public void makeTask(String description, int estimatedDuration, int acceptableDeviation, ArrayList<Task> dependencies){
		Task task = new Task(description, estimatedDuration, acceptableDeviation, dependencies);
		tasks.add(task);
	}
	
}