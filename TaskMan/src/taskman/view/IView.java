package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.project.Project;
import taskman.model.task.Task;

public interface IView {

	/**
	 * This method will print an info string on the command line.
	 * 
	 * @param message
	 */
	public void displayInfo(String message);

	/**
	 * This method will print an error string on the command line.
	 * 
	 * @param message
	 */
	public void displayError(String message);

	/**
	 * This method will print a welcome message.
	 */
	public void displayWelcome();

	/**
	 * This method will print the given main menu and return the selection as an
	 * integer.
	 * 
	 * @param menu
	 * 
	 * @return Returns the index of the menu the users choose.
	 * 
	 * @throws IllegalArgumentException
	 *             The menu needs to be a list with at least 1 item.
	 */
	public int getMainMenuID(List<String> menu) throws IllegalArgumentException;

	/**
	 * This method will print the given list of projects to the command line and
	 * will ask the user to choose one. It will return the chosen project.
	 * 
	 * @param projects
	 * 
	 * @return The chosen project.
	 * 
	 * @throws ShouldExitException
	 *             The choice of the user is to stop.
	 */
	public Project getProject(List<Project> projects)
			throws ShouldExitException;

	/**
	 * This method will print the project details of a given project to the
	 * command line.
	 * 
	 * @param branchOffice
	 * @param project
	 */
	public void displayProjectDetails(BranchOffice branchOffice, Project project);

	/**
	 * This method will print the given list of tasks to the command line and
	 * will ask the user to choose one. It will return the chosen task.
	 * 
	 * @param tasks
	 * 
	 * @return The chosen task.
	 * 
	 * @throws ShouldExitException
	 *             The choice of the user is to stop.
	 */
	public Task getTask(List<Task> tasks) throws ShouldExitException;

	/**
	 * This method will print the task details of a given task to the command
	 * line.
	 * 
	 * @param task
	 */
	public void displayTaskDetails(Task task);
	
	/**
	 * This method will return a new login form.
	 */
	public IShowAllBranchesForm getShowAllBranchesForm();

	/**
	 * This method will return a new create project form.
	 */
	public ICreateProjectForm getNewProjectForm();

	/**
	 * This method will return a new create task form.
	 */
	public ICreateTaskForm getNewTaskForm();

	/**
	 * This method will return a new update task form.
	 */
	public IUpdateTaskForm getUpdateTaskForm();

	/**
	 * This method will return a new advance time form.
	 */
	public IAdvanceTimeForm getAdvanceTimeForm();

	/**
	 * This method will return a new plan task form.
	 */
	public IPlanTaskForm getPlanTaskForm();

	/**
	 * This method will return a new resolve conflict form.
	 */
	public IResolveConflictForm getResolveConflictForm();
	
	/**
	 * This method will return a new login form.
	 */
	public ILoginForm getLoginForm();

}
