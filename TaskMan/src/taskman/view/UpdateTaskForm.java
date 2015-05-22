package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.task.Task;

public class UpdateTaskForm implements IUpdateTaskForm {

	View view;

	public UpdateTaskForm(View view) {
		this.view = view;
	}

	/**
	 * This method will ask the user to enter whether or not the selected task
	 * has failed.
	 */
	@Override
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

	/**
	 * This method will view the start time of the selected task and ask the
	 * user to enter the end time of the selected task. Afterwards it will
	 * return the end time as a DateTime.
	 * 
	 * @param startTime
	 * 
	 * @return Returns the end time as a DateTime.
	 * 
	 * @throws ShouldExitException
	 */
	@Override
	public DateTime getUpdateTaskStopTime(DateTime startTime)
			throws ShouldExitException {
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

	/**
	 * This method will view all the projects and their available tasks.
	 * 
	 * @param projects
	 * @param availableTasks
	 * 
	 * @throws ShouldExitException
	 */
	private void displayProjectsWithAvailableTasksList(List<Project> projects,
			List<List<Task>> availableTasks, List<Task> delegatedTasks)
			throws ShouldExitException {
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
		displayDelegatedTasks(availableTasks.size(), delegatedTasks);
	}

	private void displayDelegatedTasks(int startIndex, List<Task> tasks) {
		view.displayInfo(startIndex + ". Delegated tasks");
		view.displayTaskList(tasks, 1, false);
	}

	/**
	 * This method will ask the user to select a project and returns it.
	 * 
	 * @param projects
	 * @param availableTasks
	 * 
	 * @throws ShouldExitException
	 */
	@Override
	public Project getProjectWithAvailableTasks(List<Project> projects,
			List<List<Task>> availableTasks, List<Task> delegatedTasks)
			throws ShouldExitException {
		try {
			displayProjectsWithAvailableTasksList(projects, availableTasks,
					delegatedTasks);
			int projectId = view.getListChoice(projects, "Select a project:");
			return projects.get(projectId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		} catch (IndexOutOfBoundsException e2) {
			return null;
		}
	}

}
