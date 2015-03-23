package taskman.model.project;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

public class Project {
	
	/**
	 * Creates a new project.
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
		this.finished = false;
		this.tasks = new ArrayList<Task>();
	}
	
	private final String name, description;
	private final DateTime creationTime, dueTime;
	private boolean finished;
	private ArrayList<Task> tasks;
	
	/**
	 * Returns the name of the project.
	 * 
	 * @return Returns the name of the project.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Returns the description of the project.
	 * 
	 * @return Returns the description of the project.
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Returns the creation time of the project.
	 * 
	 * @return Returns the creation time of the project.
	 */
	public DateTime getCreationTime(){
		return this.creationTime;
	}
	
	/**
	 * Returns the due time of the project.
	 * 
	 * @return Returns the due time of the project.
	 */
	public DateTime getDueTime(){
		return this.dueTime;
	}
	
	
	/**
	 * Returns the status of the project.
	 * 
	 * @return
	 */
	public boolean isFinished(){
		return this.finished;
	}
	
	/**
	 * Sets the status of the project to the given status.
	 * 
	 * @param finished
	 */
	private void setFinished(boolean finished){
		this.finished = finished;
	}
	
	/**
	 * Returns the list of tasks of the project.
	 * 
	 * @return Returns the list of tasks of the project.
	 */
	public List<Task> getTasks(){
		return new ArrayList<Task>(this.tasks);
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
		this.tasks.add(task);
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
		this.tasks.add(task);
	}

	/**
	 * Updates the status of all tasks and the status of the project.
	 * The project is finished if it contains at least one task and all of its tasks, or their alternatives, are finished.
	 */
	public void updateProject() {
		boolean finished = true;
		updateAllTasks();
		if (this.tasks.size() < 1);
			finished = false;
		for (Task t : this.tasks){
			if (t.isFinished() == false);
			finished = false;
		}
		setFinished(finished);
	}

	/**
	 * Updates the status of all tasks.
	 * Unavailable tasks are set to available if all their dependencies, or their alternatives, are finished.
	 */
	private void updateAllTasks() {
		for (Task t : this.tasks){
			t.update();
		}
	}
}