package taskman.controller.project;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.IllegalDateException;
import taskman.exceptions.ShouldExitException;
import taskman.model.ProjectHandler;
import taskman.model.time.Clock;
import taskman.model.time.IClock;
import taskman.view.IView;

public class CreateProjectSession extends Session {
	
	IClock clock = Clock.getInstance();

	/**
	 * Creates the create project session using the given UI, ProjectHandler and ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 *             Both the given view and the project handler need to be valid.
	 */
	public CreateProjectSession(IView cli, ProjectHandler ph) {
		super(cli, ph);
	}

	/**
	 * starts the use case by calling the create project method
	 */
	@Override
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
				String name = getUI().getNewProjectForm().getNewProjectName();
				String description = getUI().getNewProjectForm().getNewProjectDescription();
				DateTime creationTime = clock.getSystemTime();
				DateTime dueTime = getUI().getNewProjectForm().getNewProjectDueTime();

				if (isValidProject(name, description, creationTime, dueTime))
					break;

			} catch (ShouldExitException e) {
				return;
			}
		}
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
