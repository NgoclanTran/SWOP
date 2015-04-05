package taskman.UI;

import java.util.List;

import taskman.model.project.Project;
import taskman.model.project.task.Task;

public interface UI {
	
	public void displayError(String message);
	
	public void displayWelcome();
	
	public int getMainMenuID();
	
	public void displayProjectDetails(Project project);
	
	public int getProjectID(List<Project> projects);
	
	public void displayTaskDetails(Task task);
	
	public int getTaskID(List<Task> tasks);
	
}
