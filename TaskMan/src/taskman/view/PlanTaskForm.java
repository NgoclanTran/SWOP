package taskman.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class PlanTaskForm implements IPlanTaskForm {

	View view;

	/**
	 * The constructor of the plan task form. It will setup the view to be able
	 * to get input and output.
	 * 
	 * @param view
	 */
	public PlanTaskForm(View view) {
		this.view = view;
	}

	/**
	 * This method will show all the projects with their unplanned tasks and ask
	 * the user to select a project. When the user selected one, it will return
	 * this project.
	 * 
	 * @param projects
	 * @param unplannedTasks
	 * 
	 * @return Returns the selected project.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	@Override
	public Project getProjectWithUnplannedTasks(List<Project> projects,
			List<List<Task>> unplannedTasks, List<Task> delegatedTasks) throws ShouldExitException {
		try {
			displayProjectsWithUnplannedTasksList(projects, unplannedTasks,
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

	/**
	 * This method will print all the projects and their unplanned tasks.
	 * 
	 * @param projects
	 * @param unplannedTasks
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	private void displayProjectsWithUnplannedTasksList(List<Project> projects,
			List<List<Task>> unplannedTasks, List<Task> delegatedTasks) throws ShouldExitException {
		if (projects.size() != unplannedTasks.size()) {
			view.displayError("Error occured while creating the available tasks list.");
			throw new ShouldExitException();
		}

		view.displayInfo("0. Return");
		for (int i = 1; i <= unplannedTasks.size(); i++) {
			if (unplannedTasks.get(i - 1).size() == 0)
				continue;
			String project = i + ". "
					+ projects.get(i - 1).getName().toString();
			view.displayInfo(project);
			view.displayTaskList(unplannedTasks.get(i - 1), 1, false);
		}
		displayDelegatedTasks(unplannedTasks.size() + 1, delegatedTasks);
	}
	
	private void displayDelegatedTasks(int startIndex, List<Task> tasks) {
		view.displayInfo(startIndex + ". Delegated tasks");
		view.displayTaskList(tasks, 1, false);
	}

	/**
	 * This method will ask the user to select to appropriate start time or
	 * enter a custom one. Afterwards it will return this start time as a
	 * DateTime.
	 * 
	 * @param startTimes
	 * 
	 * @return Returns the select start time as a DateTime.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	@Override
	public DateTime getStartTime(Set<DateTime> startTimes)
			throws ShouldExitException {
		List<DateTime> startTimesList = new ArrayList<DateTime>();
		List<String> startTimesStringList = new ArrayList<String>();
		for (DateTime startTime : startTimes) {
			startTimesList.add(startTime);
			startTimesStringList.add(view.getStringDate(startTime));
		}
		startTimesStringList.add("Enter custom start time");
		view.output.displayList(startTimesStringList, 0, true);
		view.output.displayEmptyLine();
		int startTimeId = view.getListChoice(startTimesStringList,
				"Select a start time:");
		if (startTimeId == startTimesStringList.size())
			return getCustomStartTime();
		else
			return (DateTime) startTimesList.get(startTimeId - 1);
	}

	/**
	 * This method will handle the request of entering a custom start time. It
	 * will parse the start time to a DateTime and return it.
	 * 
	 * @return Returns the entered custom start time.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	private DateTime getCustomStartTime() throws ShouldExitException {
		try {
			view.displayInfo("Enter the start time of the task with format dd-MM-yyyy HH:mm (or cancel):");
			String date = view.input.getInput();
			view.output.displayEmptyLine();

			while (!view.isValidDateTime(date)) {
				view.displayError("Enter the start time of the task with format dd-MM-yyyy HH:mm (or cancel):");
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

	/**
	 * This method will show the user the suggested list of resource according
	 * to the required resource types of the task. The user can choose to edit
	 * this suggested list or leave it the way it is. Afterwards this method
	 * will return the list of resources the user has chosen.
	 * 
	 * @param timeSpan
	 * @param resourceTypes
	 * @param amounts
	 * @param suggestedResources
	 * 
	 * @return Returns a list of resources the user has chosen.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	@Override
	public List<Resource> getResources(TimeSpan timeSpan,
			List<ResourceType> resourceTypes, List<Integer> amounts,
			List<List<Resource>> suggestedResources) throws ShouldExitException {
		try {
			String changeResource = "Y";
			while (!view.isValidNoAnswer(changeResource)) {
				view.displayInfo("Suggested resources for each resource type:");
				displayResourceTypesWithSuggestedResources(false,
						resourceTypes, amounts, suggestedResources);
				view.displayInfo("Do you want to change a(nother) resource? (Y/N or cancel):");
				changeResource = view.input.getInput();
				view.output.displayEmptyLine();

				if (view.isValidYesAnswer(changeResource)) {
					suggestedResources = changeResource(timeSpan,
							resourceTypes, amounts, suggestedResources);
				}
			}

			List<Resource> resources = new ArrayList<Resource>();
			for (List<Resource> list : suggestedResources) {
				resources.addAll(list);
			}

			return resources;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	/**
	 * This method will print the list of required resource types and their
	 * amount with the suggested resources for each resource type.
	 * 
	 * @param printReturn
	 * @param resourceTypes
	 * @param amounts
	 * @param suggestedResources
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	private void displayResourceTypesWithSuggestedResources(
			boolean printReturn, List<ResourceType> resourceTypes,
			List<Integer> amounts, List<List<Resource>> suggestedResources)
			throws ShouldExitException {
		if (resourceTypes.size() != suggestedResources.size()) {
			view.displayError("Error occured while creating the suggested resources list.");
			throw new ShouldExitException();
		}

		if (printReturn)
			view.displayInfo("0. Return");
		for (int i = 1; i <= suggestedResources.size(); i++) {
			if (suggestedResources.get(i - 1).size() == 0)
				continue;
			String resourceType = i + ". "
					+ resourceTypes.get(i - 1).getName().toString() + ": "
					+ amounts.get(i - 1).toString() + " resource(s)";
			view.displayInfo(resourceType);
			view.output.displayList(suggestedResources.get(i - 1), 1, false);
			view.output.displayEmptyLine();
		}

		view.output.displayEmptyLine();
	}

	/**
	 * This method will handle the request to change one of the suggested
	 * resources and returns the new list of resources.
	 * 
	 * @param timeSpan
	 * @param resourceTypes
	 * @param amounts
	 * @param suggestedResources
	 * 
	 * @return Returns the new list of resources after the user has changed one
	 *         of them.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	private List<List<Resource>> changeResource(TimeSpan timeSpan,
			List<ResourceType> resourceTypes, List<Integer> amounts,
			List<List<Resource>> suggestedResources) throws ShouldExitException {
		displayResourceTypesWithSuggestedResources(true, resourceTypes,
				amounts, suggestedResources);
		int resourceTypeId = view.getListChoice(resourceTypes,
				"Select a resource type:");

		view.output.displayList(suggestedResources.get(resourceTypeId - 1), 0,
				true);
		view.output.displayEmptyLine();
		int resourceId = view.getListChoice(
				suggestedResources.get(resourceTypeId - 1),
				"Select the resource you want to change:");

		view.displayInfo("You want to change resource ("
				+ suggestedResources.get(resourceTypeId - 1)
						.get(resourceId - 1) + ") with:");

		List<Resource> availableResources = resourceTypes.get(
				resourceTypeId - 1).getAvailableResources(timeSpan);
		view.output.displayList(availableResources, 0, true);
		view.output.displayEmptyLine();
		int newResourceId = view.getListChoice(availableResources,
				"Select the new resource:");

		suggestedResources.get(resourceTypeId - 1).set(resourceId - 1,
				availableResources.get(newResourceId - 1));

		return suggestedResources;
	}

	/**
	 * This method will ask the user to select the required developers to
	 * perform the task. It will return the list of selected developer.
	 * 
	 * @param developers
	 * 
	 * @return Returns the list of selected developers.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	@Override
	public List<Developer> getDevelopers(List<Developer> developers)
			throws ShouldExitException {
		try {
			List<Developer> assignedDevelopers = new ArrayList<Developer>();
			String addDeveloper = "Y";
			while (!view.isValidNoAnswer(addDeveloper) && !developers.isEmpty()) {
				if (!assignedDevelopers.isEmpty()) {
					view.displayInfo("Assigned developers:");
					view.output.displayList(assignedDevelopers, 0, false);
					view.output.displayEmptyLine();
					view.displayInfo("Do you want to add a(nother) developer? (Y/N or cancel):");
					addDeveloper = view.input.getInput();
					view.output.displayEmptyLine();
				}

				if (view.isValidYesAnswer(addDeveloper)) {
					Developer developer = addDeveloper(developers);
					assignedDevelopers.add(developer);
					developers.remove(developer);
				}
			}
			return assignedDevelopers;
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	/**
	 * This method will ask the user to select one developer and will add it to
	 * the list of required developers. Afterwards it returns this list.
	 * 
	 * @param developers
	 * 
	 * @return Returns the list of required developers.
	 * 
	 * @throws ShouldExitException
	 *             The user cancelled the planning of the task.
	 */
	private Developer addDeveloper(List<Developer> developers)
			throws ShouldExitException {
		view.displayInfo("List of developers:");
		view.output.displayList(developers, 0, true);
		view.output.displayEmptyLine();
		int developerId = view.getListChoice(developers, "Select a developer:");
		return developers.get(developerId - 1);
	}

}
