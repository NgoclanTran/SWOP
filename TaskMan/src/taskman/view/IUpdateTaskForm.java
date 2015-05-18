package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.model.user.Developer;

public interface IUpdateTaskForm {
	
	/**
	 * This method will ask the user to select the developer to update a task
	 * and it will return the developer.
	 * 
	 * @param developers
	 * 
	 * @return Returns the selected developer.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the updating of the task.
	 */
	public Developer getDeveloper(List<Developer> developers)
			throws ShouldExitException;

	/**
	 * This method will ask the user to enter whether or not the selected task
	 * has failed.
	 */
	public boolean getUpdateTaskFailed();

	/**
	 * This method will view the start time of the selected task and ask the
	 * user to enter the end time of the selected task. Afterwards it will
	 * return the end time as a DateTime.
	 * 
	 * @param startTime
	 * 
	 * @return Returns the end time as a DateTime.
	 * 
	 * @throws ShouldExitException
	 */
	public DateTime getUpdateTaskStopTime(DateTime startTime);

	/**
	 * This method will ask the user to select a project and returns it.
	 * 
	 * @param projects
	 * @param availableTasks
	 * 
	 * @throws ShouldExitException
	 */
	public Project getProjectWithAvailableTasks(List<Project> projects,
			List<List<Task>> availableTasks);

}
