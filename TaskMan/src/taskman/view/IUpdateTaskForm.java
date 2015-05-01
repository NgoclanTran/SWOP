package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
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

	public boolean getUpdateTaskFailed();

	public DateTime getUpdateTaskStopTime(DateTime startTime);

	public Project getProjectWithAvailableTasks(List<Project> projects,
			List<List<Task>> availableTasks);

}
