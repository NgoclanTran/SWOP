package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.Project;
import taskman.model.project.task.Task;

public interface IView {
	
	public void displayInfo(String message);
	
	public void displayError(String message);
	
	public void displayWelcome();
	
	public int getMainMenuID();
	
	public void displayProjectDetails(Project project);
	
	public Project getProject(List<Project> projects);
	
	public void displayTaskDetails(Task task);
	
	public Task getTask(List<Task> tasks);
	
	public String getNewProjectName();
	
	public String getNewProjectDescription();
	
	public DateTime getNewProjectDueTime();
	
	public String getNewTaskDescription();
	
	public int getNewTaskEstimatedDuration();
	
	public int getNewTaskAcceptableDeviation();
	
	public List<Task> getNewTaskDependencies(List<Task> tasks);

	public Task getNewTaskAlternativeFor(List<Task> tasks);
	
	public boolean getUpdateTaskFailed();
	
	public DateTime getUpdateTaskStartTime();
	
	public DateTime getUpdateTaskStopTime();
	
	public Project getProjectIDWithAvailableTasks(List<Project> projects, List<List<Task>> availableTasks);
	
}
