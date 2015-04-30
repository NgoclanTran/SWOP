package taskman.view;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public interface IPlanTaskForm {

	/**
	 * This method will show all the projects with their unplanned tasks and ask
	 * the user to select a project. When the user selected one, it will return
	 * this project.
	 * 
	 * @param projects
	 * @param unplannedTasks
	 * 
	 * @return Returns the selected project.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	public Project getProjectWithUnplannedTasks(List<Project> projects,
			List<List<Task>> unplannedTasks) throws ShouldExitException;

	/**
	 * This method will ask the user to select to appropriate start time or
	 * enter a custom one. Afterwards it will return this start time as a
	 * DateTime.
	 * 
	 * @param startTimes
	 * 
	 * @return Returns the select start time as a DateTime.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	public DateTime getStartTime(Set<DateTime> startTimes)
			throws ShouldExitException;

	/**
	 * This method will show the user the suggested list of resource according
	 * to the required resource types of the task. The user can choose to edit
	 * this suggested list or leave it the way it is. Afterwards this method
	 * will return the list of resources the user has chosen.
	 * 
	 * @param timeSpan
	 * @param resourceTypes
	 * @param amounts
	 * @param suggestedResources
	 * 
	 * @return Returns a list of resources the user has chosen.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	public List<Resource> getResources(TimeSpan timeSpan,
			List<ResourceType> resourceTypes, List<Integer> amounts,
			List<List<Resource>> suggestedResources) throws ShouldExitException;

	/**
	 * This method will ask the user to select the required developers to
	 * perform the task. It will return the list of selected developer.
	 * 
	 * @param developers
	 * 
	 * @return Returns the list of selected developers.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	public List<Developer> getDevelopers(List<Developer> developers)
			throws ShouldExitException;

}
