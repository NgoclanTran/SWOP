package taskman.model.project;

import java.util.ArrayList;

import org.joda.time.DateTime;

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
		
	}
	
	private String name, description;
	private DateTime creationTime, dueTime;
	private ArrayList<Task> tasks;
	
	/**
	 * Returns the name of the project.
	 * 
	 * @return Returns the name of the project.
	 */
	public String getName(){
		
	}
	
	/**
	 * Returns the description of the project.
	 * 
	 * @return Returns the description of the project.
	 */
	public String getDescription(){
		
	}
	
	/**
	 * Returns the creation time of the project.
	 * 
	 * @return Returns the creation time of the project.
	 */
	public DateTime getCreationTime(){
		
	}
	
	/**
	 * Returns the due time of the project.
	 * 
	 * @return Returns the due time of the project.
	 */
	public DateTime getDueTime(){
		
	}
	
	/**
	 * Returns the list of tasks of the project.
	 * 
	 * @return Returns the list of tasks of the project.
	 */
	public ArrayList<Task> getTasks(){
		
	}
	
	/**
	 * Creates a task without dependencies and adds it to the project.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 */
	public void makeTask(String description, int estimatedDuration, int acceptableDeviation){
		
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
		
	}
	
}