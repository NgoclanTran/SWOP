package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class UpdateTaskForm extends View implements IUpdateTaskForm {

	public boolean getUpdateTaskFailed() throws ShouldExitException {
		try {
			displayInfo("Did the selected task fail? (Y/N or cancel):");
			String didFail = input.getInput();
			output.displayEmptyLine();

			while (!(isValidYesAnswer(didFail) || isValidNoAnswer(didFail))) {
				displayInfo("Did the selected task fail? (Y/N or cancel):");
				didFail = input.getInput();
				output.displayEmptyLine();
			}

			if (isValidYesAnswer(didFail)) {
				return true;
			} else {
				return false;
			}
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public DateTime getUpdateTaskStartTime() throws ShouldExitException {
		try {
			displayInfo("Enter the start time of the task with format dd-MM-yyyy HH:mm (or cancel):");
			String date = input.getInput();
			output.displayEmptyLine();

			while (!isValidDateTime(date)) {
				displayInfo("Enter the start time of the task with format dd-MM-yyyy HH:mm (or cancel):");
				date = input.getInput();
				output.displayEmptyLine();
			}

			DateTime startTime = formatter.parseDateTime(date);

			return startTime;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public DateTime getUpdateTaskStopTime() throws ShouldExitException {
		try {
			displayInfo("Enter the stop time of the task with format dd-MM-yyyy HH:mm (or cancel):");
			String date = input.getInput();
			output.displayEmptyLine();

			while (!isValidDateTime(date)) {
				displayInfo("Enter the stop time of the task with format dd-MM-yyyy HH:mm (or cancel):");
				date = input.getInput();
				output.displayEmptyLine();
			}

			DateTime stopTime = formatter.parseDateTime(date);

			return stopTime;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private void displayProjectsWithAvailableTasksList(List<Project> projects,
			List<List<Task>> availableTasks) throws ShouldExitException {
		if (projects.size() != availableTasks.size()) {
			displayError("Error occured while creating the available tasks list.");
			throw new ShouldExitException();
		}

		displayInfo("0. Return");
		for (int i = 1; i <= availableTasks.size(); i++) {
			if (availableTasks.get(i - 1).size() == 0)
				continue;
			String project = i + ". "
					+ projects.get(i - 1).getName().toString();
			displayInfo(project);
			displayTaskList(availableTasks.get(i - 1), 1, false);
		}
	}

	public Project getProjectWithAvailableTasks(List<Project> projects,
			List<List<Task>> availableTasks) throws ShouldExitException {
		try {
			displayProjectsWithAvailableTasksList(projects, availableTasks);
			int projectId = getListChoice(projects, "Select a project:");
			return projects.get(projectId - 1);
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

}
