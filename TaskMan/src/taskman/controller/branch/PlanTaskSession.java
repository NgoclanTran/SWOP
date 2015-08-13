package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.DelegatedTaskHandler;
import taskman.model.company.ProjectHandler;
import taskman.model.company.UserHandler;
import taskman.model.project.Project;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.Reservable;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.PlanningService;
import taskman.model.time.TimeService;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;
import taskman.view.IView;

public class PlanTaskSession extends Session {

	// TODO: Check for required number of developers

	private ProjectHandler ph;
	private UserHandler uh;
	private DelegatedTaskHandler dth;
	private Clock clock;
	private PlanningService planning;
	private TimeService timeService = new TimeService();

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
	 * @param clock
	 *            The system clock.
	 * 
	 * @throws IllegalArgumentException
	 *             The given view needs to be valid.
	 * @throws IllegalArgumentException
	 *             The project handler, user handler and clock need to be valid.
	 */
	public PlanTaskSession(IView cli, ProjectHandler ph, UserHandler uh, DelegatedTaskHandler dth, Clock clock)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException("The plan task controller needs a ProjectHandler");
		if (uh == null)
			throw new IllegalArgumentException("The plan task controller needs a UserHandler");
		if (dth == null)
			throw new IllegalArgumentException("The plan task controller needs a DelegatedTaskHandler");
		if (clock == null)
			throw new IllegalArgumentException("The plan task controller needs a clock");
		this.ph = ph;
		this.uh = uh;
		this.dth = dth;
		this.clock = clock;
		this.planning = new PlanningService(clock);
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
	 * @param clock
	 *            The system clock.
	 * @param project
	 *            The project.
	 * @param task
	 *            The task.
	 * 
	 * @throws IllegalArgumentException
	 *             The given view needs to be valid.
	 * @throws IllegalArgumentException
	 *             The project handler, user handler and clock need to be valid.
	 */
	public PlanTaskSession(IView cli, ProjectHandler ph, UserHandler uh, DelegatedTaskHandler dth, Clock clock,
			Task task) throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException("The plan task controller needs a ProjectHandler");
		if (uh == null)
			throw new IllegalArgumentException("The plan task controller needs a UserHandler");
		if (dth == null)
			throw new IllegalArgumentException("The plan task controller needs a DelegatedTaskHandler");
		if (clock == null)
			throw new IllegalArgumentException("The plan task controller needs a clock");
		this.ph = ph;
		this.uh = uh;
		this.dth = dth;
		this.clock = clock;
		this.planning = new PlanningService(clock);
		this.task = task;
	}

	/**
	 * This method will ask the user to enter a project and a task (if they
	 * aren't already given with the constructor) and start the planning of this
	 * task.
	 */
	@Override
	public void run() throws IllegalStateException {
		if (task == null)
			showProjectsAndUnplannedTasks();
		else
			planTask();
	}

	private void showProjectsAndUnplannedTasks() throws IllegalStateException {
		List<Project> projects = ph.getProjects();
		List<List<Task>> unplannedTasksList = getUnplannedTasksAllProjects(projects);

		if (unplannedTasksList.size() == 0) {
			getUI().displayError("No unplanned tasks.");
			return;
		}

		Project project;
		try {
			project = getUI().getPlanTaskForm().getProjectWithUnplannedTasks(projects, unplannedTasksList);
		} catch (ShouldExitException e) {
			return;
		}

		this.project = project;
		showUnplannedTasks();
	}

	private void showUnplannedTasks() {
		List<Task> tasks;
		if (project != null) {
			tasks = getUnplannedTasks(new ArrayList<Task>(project.getTasks()));
			getUI().displayProjectDetails(project);
		} else {
			tasks = new ArrayList<Task>(dth.getDelegatedTasks());
		}

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
			throw new IllegalStateException("Plan task should have a task by now.");
		if (task.isPlanned())
			throw new IllegalStateException("Plan task can't plan a task that is already planned.");
		while (true) {
			try {
				List<Reservable> reservables = null;
				DateTime startTime = getStartTime();
				TimeSpan timeSpan = new TimeSpan(startTime,
						timeService.addMinutes(startTime, task.getEstimatedDuration()));

				if (!isValidStartTime(timeSpan)) {
					reservables = new ArrayList<Reservable>(getSuggestedResources(timeSpan));
					new ResolveConflictSession(getUI(), ph, uh, dth, clock, task, timeSpan, reservables).run();
					// TODO: Print some info to user to make sure he/she knows
					// the original task is getting planned all over again.
					if (task.isPlanned())
						break;
					else
						continue;
				}

				List<Resource> resources = new ArrayList<Resource>();
				if (!task.getRequiredResourceTypes().isEmpty())
					resources = getResources(timeSpan);

				if (!isValidResource(resources, timeSpan)) {
					reservables = new ArrayList<Reservable>(resources);
					new ResolveConflictSession(getUI(), ph, uh, dth, clock, task, timeSpan, reservables).run();
					// TODO: Print some info to user to make sure he/she knows
					// the original task is getting planned all over again.
					if (task.isPlanned())
						break;
					else
						continue;
				}

				List<Developer> developers = new ArrayList<Developer>();
				if (!uh.getDevelopers().isEmpty())
					developers = getDevelopers();

				if (!isvalidDeveloper(developers, timeSpan)) {
					reservables = new ArrayList<Reservable>(developers);
					new ResolveConflictSession(getUI(), ph, uh, dth, clock, task, timeSpan, reservables).run();
					// TODO: Print some info to user to make sure he/she knows
					// the original task is getting planned all over again.
					if (task.isPlanned())
						break;
					else
						continue;
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

	private boolean isvalidDeveloper(List<Developer> developers, TimeSpan timeSpan) {
		for (Developer developer : developers) {
			if (!developer.isAvailableAt(timeSpan))
				return false;
		}
		return true;
	}

	private boolean isValidPlanning(TimeSpan timeSpan, List<Resource> resources, List<Developer> developers) {
		try {
			for (Developer developer : developers) {
				task.addRequiredDeveloper(developer);
				developer.addReservation(task, timeSpan);
			}
			for (Resource resource : resources) {
				resource.addReservation(task, timeSpan);
			}
			getUI().displayInfo("Task planned.");
			return true;
		} catch (Exception Ex) {
			getUI().displayError(Ex.getMessage().toString());
			return false;
		}
	}

	private List<List<Task>> getUnplannedTasksAllProjects(List<Project> projects) {
		List<List<Task>> unplannedTasksList = new ArrayList<>();
		List<Task> unplannedTasks = null;

		boolean noUnplannedTasks = true;

		for (Project p : projects) {
			unplannedTasks = getUnplannedTasks(new ArrayList<Task>(p.getTasks()));
			unplannedTasksList.add(unplannedTasks);

			if (unplannedTasks.size() != 0) {
				noUnplannedTasks = false;
			}
		}

		unplannedTasks = getUnplannedTasks(new ArrayList<Task>(dth.getDelegatedTasks()));
		unplannedTasksList.add(unplannedTasks);
		if (unplannedTasks.size() != 0) {
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
		if (project != null)
			return getUI().getPlanTaskForm()
					.getStartTime(planning.getPossibleStartTimes(task, 3, project.getCreationTime()));
		else
			return getUI().getPlanTaskForm()
					.getStartTime(planning.getPossibleStartTimes(task, 3, clock.getSystemTime()));
	}

	private List<Resource> getSuggestedResources(TimeSpan timeSpan) {
		List<Resource> suggestedResources = new ArrayList<>();
		for (Entry<ResourceType, Integer> entry : task.getRequiredResourceTypes().entrySet()) {
			suggestedResources.addAll(entry.getKey().getSuggestedResources(timeSpan, entry.getValue()));
		}
		return suggestedResources;
	}

	private List<Resource> getResources(TimeSpan timeSpan) throws ShouldExitException, IllegalStateException {
		List<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		List<Integer> amounts = new ArrayList<Integer>();
		List<List<Resource>> suggestedResources = new ArrayList<>();
		for (Entry<ResourceType, Integer> entry : task.getRequiredResourceTypes().entrySet()) {
			resourceTypes.add(entry.getKey());
			amounts.add(entry.getValue());
			suggestedResources.add(entry.getKey().getSuggestedResources(timeSpan, entry.getValue()));
		}

		return getUI().getPlanTaskForm().getResources(timeSpan, resourceTypes, amounts, suggestedResources);
	}

	private List<Developer> getDevelopers() throws ShouldExitException {
		return getUI().getPlanTaskForm().getDevelopers(uh.getDevelopers());
	}

}
