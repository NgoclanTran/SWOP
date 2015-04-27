package taskman.controller.planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;

public class PlanTaskSession extends Session {
	
	PlanningService planning = new PlanningService(getRH());

	/**
	 * Creates the planning session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param rh
	 *            The resource handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public PlanTaskSession(IView cli, ProjectHandler ph, ResourceHandler rh)
			throws IllegalArgumentException {
		super(cli, ph, rh);
	}

	@Override
	public void run() {
		showProjectsAndUnplannedTasks();
	}
	
	private void showProjectsAndUnplannedTasks() {
		List<Project> projects = getPH().getProjects();
		List<List<Task>> unplannedTasksList = getUnplannedTasksAllProjects(projects);
		
		if (unplannedTasksList.size() == 0) {
			getUI().displayError("No unplanned tasks.");
			return;
		}

		Project project;
		try {
			project = getUI().getPlanTaskForm().getProjectWithUnplannedTasks(projects,
					unplannedTasksList);
		} catch (ShouldExitException e) {
			return;
		}

		showUnplannedTasks(project);
	}
	
	private void showUnplannedTasks(Project project) {
		List<Task> tasks = getUnplannedTasks(project.getTasks());
		getUI().displayProjectDetails(project);

		if (tasks.size() == 0)
			return;

		Task task;
		try {
			task = getUI().getTask(tasks);
		} catch (ShouldExitException e) {
			return;
		}

		planTask(project, task);
	}
	
	private void planTask(Project project, Task task) {
		while (true) {
			try {
				DateTime startTime = getStartTime(project, task);
				
//				if (isValidUpdateTask(task, isFailed, startTime, endTime))
//					break;
			} catch (ShouldExitException e) {
				return;
			}
		}
	}
	
	private List<List<Task>> getUnplannedTasksAllProjects(List<Project> projects) {
		List<List<Task>> unplannedTasksList = new ArrayList<>();
		List<Task> unplannedTasks = null;
		
		boolean noUnplannedTasks = true;

		for (Project p : projects) {
			unplannedTasks = getUnplannedTasks(p.getTasks());
			unplannedTasksList.add(unplannedTasks);

			if (noUnplannedTasks && unplannedTasks.size() > 0)
				noUnplannedTasks = false;
		}

		if (noUnplannedTasks)
			return new ArrayList<>();
		else
			return unplannedTasksList;
	}
	
	/**
	 * This method returns the list of planned tasks.
	 * 
	 * @param tasks
	 *            The list of all tasks
	 * 
	 * @return Returns a list of planned tasks.
	 */
	private List<Task> getUnplannedTasks(List<Task> tasks) {
		ArrayList<Task> unplannedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			if (!task.isPlanned())
				unplannedTasks.add(task);
		}
		return unplannedTasks;
	}
	
	private DateTime getStartTime(Project project, Task task) {
		List<DateTime> startTimes = new ArrayList<DateTime>(planning.getPossibleStartTimes(task, 3, project.getCreationTime()));
		return getUI().getPlanTaskForm().getStartTime(startTimes);
	}

}
