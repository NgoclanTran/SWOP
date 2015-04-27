package taskman.view;

import java.util.List;

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
	public DateTime getStartTime(List<DateTime> startTimes) {
		view.output.displayList(startTimes, 0, true);
		view.output.displayEmptyLine();
		int startTimeId = view.getListChoice(startTimes, "Select a start time:");
		view.displayInfo("Chosen start time: " + startTimeId);
		return null;
	}

}
