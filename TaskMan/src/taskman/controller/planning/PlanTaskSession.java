package taskman.controller.planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.PlanningService;
import taskman.model.ProjectHandler;
import taskman.model.UserHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Reservable;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.IClock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;
import taskman.view.IView;

public class PlanTaskSession extends Session {

	private UserHandler uh;
	private IClock clock = Clock.getInstance();
	private PlanningService planning = new PlanningService();

	private Project project = null;
	private Task task = null;

	/**
	 * Creates the planning session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param uh
	 *            The user handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public PlanTaskSession(IView cli, ProjectHandler ph, UserHandler uh)
			throws IllegalArgumentException {
		super(cli, ph);
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The plan task controller needs a UserHandler");
		this.uh = uh;
	}

	/**
	 * Creates the planning session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param uh
	 *            The user handler.
	 * @param project
	 *            The project.
	 * @param task
	 *            The task.
	 * 
	 * @throws IllegalArgumentException
	 */
	public PlanTaskSession(IView cli, ProjectHandler ph, UserHandler uh,
			Project project, Task task) throws IllegalArgumentException {
		super(cli, ph);
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The plan task controller needs a UserHandler");
		if (!isValidProject(project))
			throw new IllegalArgumentException(
					"The plan task controller needs a project");
		if (!isValidTask(task))
			throw new IllegalArgumentException(
					"The plan task controller needs a task");
		this.uh = uh;
		this.project = project;
		this.task = task;
	}

	/**
	 * Checks if the given user handler is valid.
	 * 
	 * @param rh
	 * 
	 * @return Returns true if the user handler is different from null.
	 */
	private boolean isValidUserHandler(UserHandler uh) {
		if (uh != null)
			return true;
		else
			return false;
	}

	/**
	 * Checks if the given project is valid.
	 * 
	 * @param project
	 * 
	 * @return Returns true if the project is different from null.
	 */
	private boolean isValidProject(Project project) {
		if (project != null)
			return true;
		else
			return false;
	}

	/**
	 * Checks if the given task is valid.
	 * 
	 * @param task
	 * 
	 * @return Returns true if the task is different from null.
	 */
	private boolean isValidTask(Task task) {
		if (task != null)
			return true;
		else
			return false;
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
			project = getUI().getPlanTaskForm().getProjectWithUnplannedTasks(
					projects, unplannedTasksList);
		} catch (ShouldExitException e) {
			return;
		}

		this.project = project;
		showUnplannedTasks();
	}

	private void showUnplannedTasks() {
		if (project == null)
			throw new IllegalStateException(
					"Plan task should have a project by now.");
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

		this.task = task;
		planTask();
	}

	private void planTask() throws IllegalStateException {
		if (task == null)
			throw new IllegalStateException(
					"Plan task should have a task by now.");
		while (true) {
			try {
				List<Reservable> reservables = null;
				DateTime startTime = getStartTime();
				TimeSpan timeSpan = new TimeSpan(startTime, clock.addMinutes(
						startTime, task.getEstimatedDuration()));

				if (!isValidStartTime(timeSpan)) {
					reservables = new ArrayList<Reservable>(
							getSuggestedResources(timeSpan));
					new ResolveConflictSession(getUI(), getPH(), uh, task,
							timeSpan, reservables).run();
					break;
				}

				List<Resource> resources = new ArrayList<Resource>();
				if (!task.getRequiredResourceTypes().isEmpty())
					resources = getResources(timeSpan);

				if (!isValidResource(resources, timeSpan)) {
					reservables = new ArrayList<Reservable>(resources);
					new ResolveConflictSession(getUI(), getPH(), uh, task,
							timeSpan, reservables).run();
					break;
				}

				List<Developer> developers = new ArrayList<Developer>();
				if (!uh.getDevelopers().isEmpty())
					developers = getDevelopers();

				if (!isvalidDeveloper(developers, timeSpan)) {
					reservables = new ArrayList<Reservable>(developers);
					new ResolveConflictSession(getUI(), getPH(), uh, task,
							timeSpan, reservables).run();
					break;
				}

				if (isValidPlanning(timeSpan, resources, developers))
					break;
			} catch (ShouldExitException e) {
				return;
			}
		}
	}

	private boolean isValidStartTime(TimeSpan timeSpan) {
		return planning.isValidTimeSpan(task, timeSpan, null);
	}

	private boolean isValidResource(List<Resource> resources, TimeSpan timeSpan) {
		for (Resource resource : resources) {
			if (!resource.isAvailableAt(timeSpan))
				return false;
		}
		return true;
	}

	private boolean isvalidDeveloper(List<Developer> developers,
			TimeSpan timeSpan) {
		for (Developer developer : developers) {
			if (!developer.isAvailableAt(timeSpan))
				return false;
		}
		return true;
	}

	private boolean isValidPlanning(TimeSpan timeSpan,
			List<Resource> resources, List<Developer> developers) {
		try {
			for (Developer developer : developers) {
				task.addRequiredDeveloper(developer);
				developer.addReservation(task, timeSpan);
			}
			for (Resource resource : resources) {
				resource.addReservation(task, timeSpan);
			}
			task.updateTaskAvailability();
			getUI().displayInfo("Task planned.");
			return true;
		} catch (Exception Ex) {
			getUI().displayError(Ex.getMessage());
			return false;
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

	private DateTime getStartTime() {
		return getUI().getPlanTaskForm().getStartTime(
				planning.getPossibleStartTimes(task, 3,
						project.getCreationTime()));
	}

	private List<Resource> getSuggestedResources(TimeSpan timeSpan) {
		List<Resource> suggestedResources = new ArrayList<>();
		for (Entry<ResourceType, Integer> entry : task
				.getRequiredResourceTypes().entrySet()) {
			suggestedResources.addAll(entry.getKey().getSuggestedResources(
					timeSpan, entry.getValue()));
		}
		return suggestedResources;
	}

	private List<Resource> getResources(TimeSpan timeSpan)
			throws ShouldExitException, IllegalStateException {
		List<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		List<Integer> amounts = new ArrayList<Integer>();
		List<List<Resource>> suggestedResources = new ArrayList<>();
		for (Entry<ResourceType, Integer> entry : task
				.getRequiredResourceTypes().entrySet()) {
			resourceTypes.add(entry.getKey());
			amounts.add(entry.getValue());
			suggestedResources.add(entry.getKey().getSuggestedResources(
					timeSpan, entry.getValue()));
		}

		return getUI().getPlanTaskForm().getResources(timeSpan, resourceTypes,
				amounts, suggestedResources);
	}

	private List<Developer> getDevelopers() throws ShouldExitException {
		return getUI().getPlanTaskForm().getDevelopers(uh.getDevelopers());
	}

}
