package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public interface ICreateProjectForm {

	/**
	 * This method will ask the user to enter a project name.
	 * 
	 * @return Returns a string of the project name.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the project creation.
	 */
	public String getNewProjectName() throws ShouldExitException;

	/**
	 * This method will ask the user to enter a project description.
	 * 
	 * @return Returns a string of the project description.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the project creation.
	 */
	public String getNewProjectDescription() throws ShouldExitException;

	/**
	 * This method will ask the user to enter a project due time.
	 * 
	 * @return Returns a date time of the project due time.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the project creation.
	 */
	public DateTime getNewProjectDueTime() throws ShouldExitException;

}
