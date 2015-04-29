package taskman.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class PlanTaskForm implements IPlanTaskForm {

	View view;

	public PlanTaskForm(View view) {
		this.view = view;
	}

	@Override
	public Project getProjectWithUnplannedTasks(List<Project> projects,
			List<List<Task>> unplannedTasks) {
		try {
			displayProjectsWithUnplannedTasksList(projects, unplannedTasks);
			int projectId = view.getListChoice(projects, "Select a project:");
			return projects.get(projectId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}

	private void displayProjectsWithUnplannedTasksList(List<Project> projects,
			List<List<Task>> unplannedTasks) throws ShouldExitException {
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
	}

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

	public List<Resource> getResources(TimeSpan timeSpan,
			List<ResourceType> resourceTypes, List<Integer> amounts,
			List<List<Resource>> suggestedResources) throws ShouldExitException {
		try {
			String changeResource = "Y";
			while (!view.isValidNoAnswer(changeResource)) {
				view.displayInfo("Suggested resources for each resource type:");
				displayResourceTypesWithSuggestedResources(false, resourceTypes,
						amounts, suggestedResources);
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
		
		suggestedResources.get(resourceTypeId - 1).set(resourceId - 1, availableResources.get(newResourceId - 1));
		
		return suggestedResources;
	}
	
	public List<Developer> getDevelopers(List<Developer> developers) throws ShouldExitException {
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
	
	private Developer addDeveloper(List<Developer> developers) throws ShouldExitException {
		view.displayInfo("List of developers:");
		view.output.displayList(developers, 0, true);
		view.output.displayEmptyLine();
		int developerId = view.getListChoice(developers, "Select a developer:");
		return developers.get(developerId - 1);
	}

}
