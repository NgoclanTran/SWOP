package taskman.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.ResourceHandler;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task2;

public class CreateTaskForm implements ICreateTaskForm {

	View view;

	/**
	 * The constructor of the create task form. It will setup the view to be
	 * able to get input and output.
	 * 
	 * @param view
	 */
	public CreateTaskForm(View view) {
		this.view = view;
	}

	/**
	 * This method will ask the user to enter a task description.
	 * 
	 * @return Returns a string with the task description.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	@Override
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

	/**
	 * This method will ask the user to enter a task estimated duration in
	 * minutes and returns it.
	 * 
	 * @return Returns a integer with the task estimated duration in minutes.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	@Override
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

	/**
	 * This method will ask the user to enter a task acceptable deviation in
	 * percents and returns it.
	 * 
	 * @return Returns an integer with the task acceptable deviation in percents.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	@Override
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

	/**
	 * This method will ask he user to enter the task dependencies and returns
	 * them as a list of tasks.
	 * 
	 * @return Returns a list of tasks that are dependencies.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	@Override
	public List<Task2> getNewTaskDependencies(List<Task2> tasks)
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
				List<Task2> list = parseDependecies(tasks);
				while (list == null) {
					view.displayError("Incorrect input.");
					list = parseDependecies(tasks);
				}
				return list;
			} else {
				return new ArrayList<Task2>();
			}
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	/**
	 * This method will parse the input that the user entered to the dependent
	 * tasks and return them as a list.
	 * 
	 * @param tasks
	 * 
	 * @return Returns a list of dependent tasks.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	private List<Task2> parseDependecies(List<Task2> tasks)
			throws ShouldExitException {
		ArrayList<Task2> list = new ArrayList<Task2>();
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

	/**
	 * This method will ask the user to enter the task for which this task is an
	 * alternative and return it.
	 * 
	 * @param tasks
	 * 
	 * @return Returns a task for which this task is an alternative.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	@Override
	public Task2 getNewTaskAlternativeFor(List<Task2> tasks)
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

	/**
	 * This method will ask the user to enter the required resource types and
	 * their amount and return them as a map of resource types and integers.
	 * 
	 * @param rh
	 * 
	 * @return Returns a map of ResourceType and Integer with the required
	 *         resource types and their amount.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	@Override
	public Map<ResourceType, Integer> getNewTaskResourceTypes(ResourceHandler rh)
			throws ShouldExitException {
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

	/**
	 * This method will ask the user for one specific resource type and return
	 * it.
	 * 
	 * @param rh
	 * 
	 * @return Returns the resource type the user selected.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	private ResourceType getNewTaskResourceType(ResourceHandler rh)
			throws ShouldExitException {
		List<ResourceType> resourceTypes = rh.getResourceTypes();
		displayResourceTypeList(resourceTypes, 1, true);
		int resourceTypeId = view.getListChoice(resourceTypes,
				"Select a resource type:");
		return resourceTypes.get(resourceTypeId - 1);
	}

	/**
	 * This method will ask the user for the amount needed for the selected
	 * resource type and return it.
	 * 
	 * @return Returns the amount of resource needed for the specified resource
	 *         type.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the task creation.
	 */
	private int getNewTaskResourceTypeAmount() throws ShouldExitException {
		int number = Integer.MIN_VALUE;
		while (number < 0) {
			view.displayInfo("How many resources of the selected type do you want (or cancel):");
			number = view.input.getNumberInput();
			view.output.displayEmptyLine();
		}
		return number;
	}

	/**
	 * This method will print the resource types as a list.
	 * 
	 * @param resouceTypes
	 * @param tabs
	 * @param printReturn
	 */
	private void displayResourceTypeList(List<ResourceType> resouceTypes,
			int tabs, boolean printReturn) {
		ArrayList<String> resourceTypeInfo = new ArrayList<String>();
		for (int i = 1; i <= resouceTypes.size(); i++) {
			resourceTypeInfo.add(resouceTypes.get(i - 1).toString());
		}
		view.output.displayList(resourceTypeInfo, tabs, printReturn);
		view.output.displayEmptyLine();
	}

}
