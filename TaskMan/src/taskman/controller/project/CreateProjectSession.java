package taskman.controller.project;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import taskman.UI.UI;
import taskman.controller.Session;
import taskman.exceptions.IllegalDateException;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;

public class CreateProjectSession extends Session {

	private final String stop = "cancel";
	private final DateTimeFormatter formatter = DateTimeFormat
			.forPattern("dd-MM-yyyy HH:mm");

	/**
	 * Creates the create project session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public CreateProjectSession(UI cli, ProjectHandler ph) {
		super(cli, ph);
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
				getUI().displayEmptyLine();

				if (isValidProject(name, description, creationTime, dueTime))
					break;

			} catch (ShouldExitException e) {
				getUI().displayEmptyLine();
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
		String name = getUI().getTextInput(
				"Enter the name of the project (or cancel):");

		if (name.toLowerCase().equals(stop))
			throw new ShouldExitException();

		return name;
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
		String description = getUI().getTextInput(
				"Enter the description of the project (or cancel):");

		if (description.toLowerCase().equals(stop))
			throw new ShouldExitException();

		return description;
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
		String date = getUI()
				.getTextInput(
						"Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");

		if (date.toLowerCase().equals(stop))
			throw new ShouldExitException();

		while (!isValidDateTime(date)) {
			getUI().display("Invalid date format!");
			date = getUI()
					.getTextInput(
							"Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");

			if (date.toLowerCase().equals(stop))
				throw new ShouldExitException();
		}

		DateTime dueTime = formatter.parseDateTime(date);

		return dueTime;
	}

	/**
	 * This method will check whether the given date string can be parsed or not
	 * to a DateTime.
	 * 
	 * @param date
	 * 
	 * @return Returns true if it is a correctly spelled date, else it returns
	 *         false.
	 */
	private boolean isValidDateTime(String date) {
		try {
			formatter.parseDateTime(date);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
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
	 * @return Returns true if the creation of the project is succesful and
	 *         false if there was an error.
	 */
	private boolean isValidProject(String name, String description,
			DateTime creationTime, DateTime dueTime) {
		try {
			getPH().addProject(name, description, creationTime, dueTime);
			getUI().display("Project created");
			getUI().displayEmptyLine();
			return true;
		} catch (IllegalDateException dateEx) {
			getUI().display(dateEx.getMessage());
			return false;
		} catch (IllegalArgumentException argEx) {
			getUI().display(argEx.getMessage());
			return false;
		}
	}
}
