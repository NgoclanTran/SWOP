package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import taskman.model.company.ProjectHandler;
import taskman.model.company.UserHandler;
import taskman.model.project.Project;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.Reservable;
import taskman.model.task.Reservation;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;
import taskman.view.IView;

public class ResolveConflictSession extends AbstractProjectHandlerSession {

	private UserHandler uh;
	private Clock clock;
	private Task task = null;
	private TimeSpan timeSpan = null;
	private List<Reservable> reservables = null;

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
	 * @param task
	 *            The task currently being planned.
	 * @param reservables
	 *            The reservables that are conflicting.
	 * 
	 * @throws IllegalArgumentException
	 *             Both the given view and the project handler need to be valid.
	 * @throws IllegalArgumentException
	 *             The user handler and clock need to be valid.
	 */
	public ResolveConflictSession(IView cli, ProjectHandler ph, UserHandler uh,
			Clock clock, Task task, TimeSpan timeSpan,
			List<Reservable> reservables) throws IllegalArgumentException {
		super(cli, ph);
		if (uh == null)
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a UserHandler");
		if (clock == null)
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a clock");
		this.uh = uh;
		this.clock = clock;
		this.task = task;
		this.timeSpan = timeSpan;
		this.reservables = reservables;
	}

	/**
	 * This method ask the user which task to reschedule and start the planning
	 * of that task all over again.
	 * 
	 * @throws IllegalStateException
	 */
	@Override
	public void run() throws IllegalStateException {
		if (reservables.isEmpty())
			throw new IllegalStateException(
					"A conflict cannot have an empty reservable list.");

		Task taskToReschedule = getTaskToReschedule(getConflictingTasks());
		Project project = getProject(taskToReschedule);
		removeAllReservations(taskToReschedule);
		new PlanTaskSession(getUI(), getPH(), uh, clock, project, taskToReschedule)
				.run();
	}

	private Project getProject(Task task) {
		for (Project project : getPH().getProjects()) {
			if (project.getTasks().contains(task))
				return project;
		}
		throw new IllegalStateException(
				"A task cannot exists without a project.");
	}

	private List<Task> getConflictingTasks() {
		List<Task> conflictingTasks = new ArrayList<Task>();
		for (Reservable reservable : reservables) {
			if (!reservable.isAvailableAt(timeSpan)) {
				for (Reservation reservation : reservable.getReservations()) {
					if (timeSpan.isDuringTimeSpan(reservation.getTimeSpan()
							.getStartTime())
							|| timeSpan.isDuringTimeSpan(reservation
									.getTimeSpan().getEndTime()))
						conflictingTasks.add(reservation.getTask());
				}
			}
		}
		if (conflictingTasks.isEmpty())
			throw new IllegalStateException(
					"Resolve conflict use case cannot be called when there is no conflict.");
		return conflictingTasks;
	}

	private Task getTaskToReschedule(List<Task> conflictingTasks) {
		return getUI().getResolveConflictForm().getTaskToRechedule(task,
				conflictingTasks);
	}

	private void removeAllReservations(Task task) {
		for (Developer developer : task.getRequiredDevelopers()) {
			for (Reservation reservation : developer.getReservations()) {
				if (reservation.getTask().equals(task))
					developer.removeReservation(reservation);
			}
		}

		for (Entry<ResourceType, Integer> entry : task
				.getRequiredResourceTypes().entrySet()) {
			for (Resource resource : entry.getKey().getResources()) {
				for (Reservation reservation : resource.getReservations()) {
					if (reservation.getTask().equals(task))
						resource.removeReservation(reservation);
				}
			}
		}
	}

}
