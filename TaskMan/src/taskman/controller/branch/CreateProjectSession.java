package taskman.controller.branch;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.IllegalDateException;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.ProjectHandler;
import taskman.model.time.Clock;
import taskman.view.IView;

public class CreateProjectSession extends Session {

	private ProjectHandler ph;
	private Clock clock;

	/**
	 * Creates the create project session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param clock
	 *            The system clock.
	 * 
	 * @throws IllegalArgumentException
	 *             The clock needs to be valid.
	 */
	public CreateProjectSession(IView cli, ProjectHandler ph, Clock clock)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The create project controller needs a ProjectHandler");
		if (clock == null)
			throw new IllegalArgumentException(
					"The create project controller needs a clock");
		this.ph = ph;
		this.clock = clock;
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
				String description = getUI().getNewProjectForm()
						.getNewProjectDescription();
				DateTime creationTime = clock.getSystemTime();
				DateTime dueTime = getUI().getNewProjectForm()
						.getNewProjectDueTime();

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
			ph.addProject(name, description, creationTime, dueTime);
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
