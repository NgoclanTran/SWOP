package taskman.view;

import java.util.List;

import taskman.model.project.Project;
import taskman.model.project.task.Task;

public interface IView {
	
	public void displayInfo(String message);
	
	public void displayError(String message);
	
	public void displayWelcome();
	
	public int getMainMenuID(List<String> menu);
	
	public void displayProjectDetails(Project project);
	
	public Project getProject(List<Project> projects);
	
	public void displayTaskDetails(Task task);
	
	public Task getTask(List<Task> tasks);
	
	public ICreateProjectForm getNewProjectForm();
	
	public ICreateTaskForm getNewTaskForm();
	
	public IUpdateTaskForm getUpdateTaskForm();
	
	public IPlanTaskForm getPlanTaskForm();
	
}
