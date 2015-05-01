package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.user.Developer;

public class UpdateTaskForm implements IUpdateTaskForm {

	View view;

	public UpdateTaskForm(View view) {
		this.view = view;
	}

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
	@Override
	public Developer getDeveloper(List<Developer> developers)
			throws ShouldExitException {
		try {
			view.displayInfo("List of developers:");
			view.output.displayList(developers, 0, true);
			view.output.displayEmptyLine();
			int developerId = view.getListChoice(developers, "Select a developer:");
			return developers.get(developerId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public boolean getUpdateTaskFailed() throws ShouldExitException {
		try {
			view.displayInfo("Did the selected task fail? (Y/N or cancel):");
			String didFail = view.input.getInput();
			view.output.displayEmptyLine();

			while (!(view.isValidYesAnswer(didFail) || view
					.isValidNoAnswer(didFail))) {
				view.displayInfo("Did the selected task fail? (Y/N or cancel):");
				didFail = view.input.getInput();
				view.output.displayEmptyLine();
			}

			if (view.isValidYesAnswer(didFail)) {
				return true;
			} else {
				return false;
			}
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public DateTime getUpdateTaskStartTime() throws ShouldExitException {
		try {
			view.displayInfo("Enter the start time of the task with format dd-MM-yyyy HH:mm (or cancel):");
			String date = view.input.getInput();
			view.output.displayEmptyLine();

			while (!view.isValidDateTime(date)) {
				view.displayInfo("Enter the start time of the task with format dd-MM-yyyy HH:mm (or cancel):");
				date = view.input.getInput();
				view.output.displayEmptyLine();
			}

			DateTime startTime = view.formatter.parseDateTime(date);

			return startTime;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public DateTime getUpdateTaskStopTime(DateTime startTime) throws ShouldExitException {
		try {
			view.displayInfo("Start time: " + view.getStringDate(startTime));
			view.output.displayEmptyLine();
			
			view.displayInfo("Enter the stop time of the task with format dd-MM-yyyy HH:mm (or cancel):");
			String date = view.input.getInput();
			view.output.displayEmptyLine();

			while (!view.isValidDateTime(date)) {
				view.displayInfo("Enter the stop time of the task with format dd-MM-yyyy HH:mm (or cancel):");
				date = view.input.getInput();
				view.output.displayEmptyLine();
			}

			DateTime stopTime = view.formatter.parseDateTime(date);

			return stopTime;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private void displayProjectsWithAvailableTasksList(List<Project> projects,
			List<List<Task>> availableTasks) throws ShouldExitException {
		if (projects.size() != availableTasks.size()) {
			view.displayError("Error occured while creating the available tasks list.");
			throw new ShouldExitException();
		}

		view.displayInfo("0. Return");
		for (int i = 1; i <= availableTasks.size(); i++) {
			if (availableTasks.get(i - 1).size() == 0)
				continue;
			String project = i + ". "
					+ projects.get(i - 1).getName().toString();
			view.displayInfo(project);
			view.displayTaskList(availableTasks.get(i - 1), 1, false);
		}
	}

	public Project getProjectWithAvailableTasks(List<Project> projects,
			List<List<Task>> availableTasks) throws ShouldExitException {
		try {
			displayProjectsWithAvailableTasksList(projects, availableTasks);
			int projectId = view.getListChoice(projects, "Select a project:");
			return projects.get(projectId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

}
