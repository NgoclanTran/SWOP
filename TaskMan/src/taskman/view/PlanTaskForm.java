package taskman.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

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
	public DateTime getStartTime(Set<DateTime> startTimes) throws ShouldExitException {
		List<DateTime> startTimesList = new ArrayList<DateTime>();
		List<String> startTimesStringList = new ArrayList<String>();
		for (DateTime startTime : startTimes) {
			startTimesList.add(startTime);
			startTimesStringList.add(view.getStringDate(startTime));
		}
		startTimesStringList.add("Enter custom start time");
		view.output.displayList(startTimesStringList, 0, true);
		view.output.displayEmptyLine();
		int startTimeId = view.getListChoice(startTimesStringList, "Select a start time:");
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

}
