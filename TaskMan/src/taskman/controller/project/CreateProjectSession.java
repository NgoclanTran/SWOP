package taskman.controller.project;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.IllegalDateException;
import taskman.exceptions.ShouldExitException;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.view.IView;

public class CreateProjectSession extends Session {

	/**
	 * Creates the create project session using the given UI, ProjectHandler and ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param rh
	 *            The resource handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public CreateProjectSession(IView cli, ProjectHandler ph, ResourceHandler rh) {
		super(cli, ph, rh);
	}

	@Override
	/**
	 * starts the use case by calling the create project method
	 */
	public void run() {
		createProject();
	}

	/**
	 * This method loops over the creation form of a project until the users
	 * enters all details correctly or decides to cancel the creation.
	 */
	private void createProject() {
		while (true) {
			try {
				String name = getName();
				String description = getDescription();
				DateTime creationTime = new DateTime();
				DateTime dueTime = getDueTime();

				if (isValidProject(name, description, creationTime, dueTime))
					break;

			} catch (ShouldExitException e) {
				return;
			}
		}
	}

	/**
	 * This method asks the user to enter the name of the project and returns
	 * it.
	 * 
	 * @return Returns the name of the project that is to be entered.
	 * 
	 * @throws ShouldExitException
	 */
	private String getName() throws ShouldExitException {
		return getUI().getNewProjectForm().getNewProjectName();
	}

	/**
	 * This method asks the user to enter the description of the project and
	 * returns it.
	 * 
	 * @return Returns the description of the project that is to be entered.
	 * 
	 * @throws ShouldExitException
	 */
	private String getDescription() throws ShouldExitException {
		return getUI().getNewProjectForm().getNewProjectDescription();
	}

	/**
	 * This method asks the user to enter the due time of the project and
	 * returns it. It will loop over the question until the date is correctly
	 * given or the user decides to cancel.
	 * 
	 * @return Returns the due time of the project that is to be entered.
	 * 
	 * @throws ShouldExitException
	 */
	private DateTime getDueTime() throws ShouldExitException {
		return getUI().getNewProjectForm().getNewProjectDueTime();
	}

	/**
	 * This method will try to make a new project with the given parameters. If
	 * the creation fails it will print the error message and return false.
	 * 
	 * @param name
	 * @param description
	 * @param creationTime
	 * @param dueTime
	 * 
	 * @return Returns true if the creation of the project is successful and
	 *         false if there was an error.
	 */
	private boolean isValidProject(String name, String description,
			DateTime creationTime, DateTime dueTime) {
		try {
			getPH().addProject(name, description, creationTime, dueTime);
			getUI().displayInfo("Project created");
			return true;
		} catch (IllegalDateException dateEx) {
			getUI().displayError(dateEx.getMessage());
			return false;
		} catch (IllegalArgumentException argEx) {
			getUI().displayError(argEx.getMessage());
			return false;
		}
	}
}
