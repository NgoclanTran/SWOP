package taskman.view;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;

public class CreateProjectForm implements ICreateProjectForm {
	
	View view;
	
	public CreateProjectForm(View view) {
		this.view = view;
	}

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
