package taskman.view;

import java.util.ArrayList;
import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.task.Task;

public class CreateTaskForm extends View implements ICreateTaskForm {

	public String getNewTaskDescription() throws ShouldExitException {
		try {
			displayInfo("Enter the description of the task (or cancel):");
			String description = input.getInput();
			output.displayEmptyLine();
			return description;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public int getNewTaskEstimatedDuration() throws ShouldExitException {
		try {
			displayInfo("Enter the estimated duration of the task (or cancel):");
			int estimatedDuration = input.getNumberInput();
			output.displayEmptyLine();

			while (!isValidInteger(estimatedDuration)) {
				displayError("You need to enter an integer.\nEnter the estimated duration of the task (or cancel):");
				estimatedDuration = input.getNumberInput();
				output.displayEmptyLine();
			}
			return estimatedDuration;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public int getNewTaskAcceptableDeviation() throws ShouldExitException {
		try {
			displayInfo("Enter the acceptable deviation of the task (or cancel):");
			int acceptableDeviation = input.getNumberInput();
			output.displayEmptyLine();

			while (!isValidInteger(acceptableDeviation)) {
				displayError("You need to enter an integer.\nEnter the acceptable deviation of the task (or cancel):");
				acceptableDeviation = input.getNumberInput();
				output.displayEmptyLine();
			}
			return acceptableDeviation;
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public List<Task> getNewTaskDependencies(List<Task> tasks)
			throws ShouldExitException {
		try {
			displayInfo("Does this task have dependencies? (Y/N or cancel):");
			String hasDependencies = input.getInput();
			output.displayEmptyLine();

			while (!(isValidYesAnswer(hasDependencies) || isValidNoAnswer(hasDependencies))) {
				displayError("Does this task have dependencies? (Y/N or cancel):");
				hasDependencies = input.getInput();
				output.displayEmptyLine();
			}

			if (isValidYesAnswer(hasDependencies)) {
				List<Task> list = parseDependecies(tasks);
				while (list == null) {
					displayError("Incorrect input.");
					list = parseDependecies(tasks);
				}
				return list;
			} else {
				return new ArrayList<Task>();
			}
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private List<Task> parseDependecies(List<Task> tasks)
			throws ShouldExitException {
		ArrayList<Task> list = new ArrayList<Task>();
		output.displayEmptyLine();
		displayTaskList(tasks, 0, true);
		displayInfo("Select the dependencies seperated by a comma (e.g. 1,2,3 and 'cancel' or 0 to return):");
		String dependencies = input.getInput();
		output.displayEmptyLine();

		String dependencie[] = dependencies.split(",");
		for (String i : dependencie) {
			try {
				int j = Integer.parseInt(i);
				if (j > 0 && j <= tasks.size())
					list.add(tasks.get(j - 1));
				else if (j == 0)
					throw new ShouldExitException();
				else
					return null;
				return list;
			} catch (NumberFormatException e2) {
				return null;
			}
		}
		return null;
	}

	public Task getNewTaskAlternativeFor(List<Task> tasks)
			throws ShouldExitException {
		try {
			displayInfo("Is this task an alternative for a failed task? (Y/N or cancel):");
			String hasAlternativeFor = input.getInput();
			output.displayEmptyLine();

			while (!(isValidYesAnswer(hasAlternativeFor) || isValidNoAnswer(hasAlternativeFor))) {
				displayError("Is this task an alternative for a failed task? (Y/N or cancel):");
				hasAlternativeFor = input.getInput();
				output.displayEmptyLine();
			}

			if (isValidYesAnswer(hasAlternativeFor)) {
				displayTaskList(tasks, 0, true);
				int taskId = getListChoice(tasks,
						"Select a task for which this task will be the alternative (or cancel):");
				return tasks.get(taskId - 1);
			} else {
				return null;
			}
		} catch (ShouldExitException e) {
			output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}	

}
