package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public class CreateProjectForm extends View implements ICreateProjectForm {

	public String getNewProjectName() throws ShouldExitException {
		try {
			displayInfo("Enter the name of the project (or cancel):");
			String name = input.getInput();
			output.displayEmptyLine();
			return name;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public String getNewProjectDescription() throws ShouldExitException {
		try {
			displayInfo("Enter the description of the project (or cancel):");
			String description = input.getInput();
			output.displayEmptyLine();
			return description;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public DateTime getNewProjectDueTime() throws ShouldExitException {
		try {
			displayInfo("Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");
			String date = input.getInput();
			output.displayEmptyLine();

			while (!super.isValidDateTime(date)) {
				displayError("Enter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):");
				date = input.getInput();
				output.displayEmptyLine();
			}

			DateTime dueTime = formatter.parseDateTime(date);

			return dueTime;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

}
