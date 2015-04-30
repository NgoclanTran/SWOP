package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public class CreateProjectForm implements ICreateProjectForm {

	private View view;

	/**
	 * The constructor of the create project form. It will setup the view to be
	 * able to get input and output.
	 * 
	 * @param view
	 */
	public CreateProjectForm(View view) {
		this.view = view;
	}

	/**
	 * This method will ask the user to enter a project name.
	 * 
	 * @return Returns a string of the project name.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the project creation.
	 */
	@Override
	public String getNewProjectName() throws ShouldExitException {
		try {
			view.displayInfo("Enter the name of the project (or cancel):");
			String name = view.input.getInput();
			view.output.displayEmptyLine();
			return name;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	/**
	 * This method will ask the user to enter a project description.
	 * 
	 * @return Returns a string of the project description.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the project creation.
	 */
	@Override
	public String getNewProjectDescription() throws ShouldExitException {
		try {
			view.displayInfo("Enter the description of the project (or cancel):");
			String description = view.input.getInput();
			view.output.displayEmptyLine();
			return description;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	/**
	 * This method will ask the user to enter a project due time.
	 * 
	 * @return Returns a date time of the project due time.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the project creation.
	 */
	@Override
	public DateTime getNewProjectDueTime() throws ShouldExitException {
		try {
			view.displayInfo("Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");
			String date = view.input.getInput();
			view.output.displayEmptyLine();

			while (!view.isValidDateTime(date)) {
				view.displayError("Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");
				date = view.input.getInput();
				view.output.displayEmptyLine();
			}

			DateTime dueTime = view.formatter.parseDateTime(date);

			return dueTime;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

}
