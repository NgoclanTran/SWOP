package taskman.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taskman.exceptions.ShouldExitException;
import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;

public class CreateTaskForm implements ICreateTaskForm {

	View view;

	public CreateTaskForm(View view) {
		this.view = view;
	}

	public String getNewTaskDescription() throws ShouldExitException {
		try {
			view.displayInfo("Enter the description of the task (or cancel):");
			String description = view.input.getInput();
			view.output.displayEmptyLine();
			return description;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public int getNewTaskEstimatedDuration() throws ShouldExitException {
		try {
			view.displayInfo("Enter the estimated duration of the task (or cancel):");
			int estimatedDuration = view.input.getNumberInput();
			view.output.displayEmptyLine();

			while (!view.isValidInteger(estimatedDuration)) {
				view.displayError("You need to enter an integer.\nEnter the estimated duration of the task (or cancel):");
				estimatedDuration = view.input.getNumberInput();
				view.output.displayEmptyLine();
			}
			return estimatedDuration;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public int getNewTaskAcceptableDeviation() throws ShouldExitException {
		try {
			view.displayInfo("Enter the acceptable deviation of the task (or cancel):");
			int acceptableDeviation = view.input.getNumberInput();
			view.output.displayEmptyLine();

			while (!view.isValidInteger(acceptableDeviation)) {
				view.displayError("You need to enter an integer.\nEnter the acceptable deviation of the task (or cancel):");
				acceptableDeviation = view.input.getNumberInput();
				view.output.displayEmptyLine();
			}
			return acceptableDeviation;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public List<Task> getNewTaskDependencies(List<Task> tasks)
			throws ShouldExitException {
		try {
			view.displayInfo("Does this task have dependencies? (Y/N or cancel):");
			String hasDependencies = view.input.getInput();
			view.output.displayEmptyLine();

			while (!(view.isValidYesAnswer(hasDependencies) || view
					.isValidNoAnswer(hasDependencies))) {
				view.displayError("Does this task have dependencies? (Y/N or cancel):");
				hasDependencies = view.input.getInput();
				view.output.displayEmptyLine();
			}

			if (view.isValidYesAnswer(hasDependencies)) {
				List<Task> list = parseDependecies(tasks);
				while (list == null) {
					view.displayError("Incorrect input.");
					list = parseDependecies(tasks);
				}
				return list;
			} else {
				return new ArrayList<Task>();
			}
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private List<Task> parseDependecies(List<Task> tasks)
			throws ShouldExitException {
		ArrayList<Task> list = new ArrayList<Task>();
		view.output.displayEmptyLine();
		view.displayTaskList(tasks, 0, true);
		view.displayInfo("Select the dependencies seperated by a comma (e.g. 1,2,3 and 'cancel' or 0 to return):");
		String dependencies = view.input.getInput();
		view.output.displayEmptyLine();

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
			view.displayInfo("Is this task an alternative for a failed task? (Y/N or cancel):");
			String hasAlternativeFor = view.input.getInput();
			view.output.displayEmptyLine();

			while (!(view.isValidYesAnswer(hasAlternativeFor) || view
					.isValidNoAnswer(hasAlternativeFor))) {
				view.displayError("Is this task an alternative for a failed task? (Y/N or cancel):");
				hasAlternativeFor = view.input.getInput();
				view.output.displayEmptyLine();
			}

			if (view.isValidYesAnswer(hasAlternativeFor)) {
				view.displayTaskList(tasks, 0, true);
				int taskId = view
						.getListChoice(tasks,
								"Select a task for which this task will be the alternative (or cancel):");
				return tasks.get(taskId - 1);
			} else {
				return null;
			}
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	public Map<ResourceType, Integer> getNewTaskResourceTypes(ResourceHandler rh) throws ShouldExitException {
		try {
			Map<ResourceType, Integer> resourceTypes = new HashMap<ResourceType, Integer>();
			
			String addResouceType = "Y";
			while (!view.isValidNoAnswer(addResouceType)) {
				view.displayInfo("Do you want to add a(nother) resource type? (Y/N or cancel):");
				addResouceType = view.input.getInput();
				view.output.displayEmptyLine();
				
				if (view.isValidYesAnswer(addResouceType)) {
					ResourceType resourceToAdd = getNewTaskResourceType(rh);
					int amountToAdd = getNewTaskResourceTypeAmount();
					resourceTypes.put(resourceToAdd, amountToAdd);
				}
			}
			
			return resourceTypes;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private ResourceType getNewTaskResourceType(ResourceHandler rh) throws ShouldExitException {
		return view.getResourceType(rh.getResourceTypes());
	}

	private int getNewTaskResourceTypeAmount() throws ShouldExitException {
		int number = Integer.MIN_VALUE;
		while (number < 0) {
			view.displayInfo("How many resources of the selected type do you want (or cancel):");
			number = view.input.getNumberInput();
			view.output.displayEmptyLine();
		}
		return number;
	}

}
