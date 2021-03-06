package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import taskman.controller.Session;
import taskman.model.company.DelegatedTaskHandler;
import taskman.model.company.ProjectHandler;
import taskman.model.company.UserHandler;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.Reservable;
import taskman.model.task.Reservation;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;
import taskman.view.IView;

public class ResolveConflictSession extends Session {

	private ProjectHandler ph;
	private UserHandler uh;
	private DelegatedTaskHandler dth;
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
	 *             The given view needs to be valid.
	 * @throws IllegalArgumentException
	 *             The project handler, user handler and clock need to be valid.
	 */
	public ResolveConflictSession(IView cli, ProjectHandler ph, UserHandler uh,
			DelegatedTaskHandler dth, Clock clock, Task task,
			TimeSpan timeSpan, List<Reservable> reservables)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a ProjectHandler");
		if (uh == null)
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a UserHandler");
		if (dth == null)
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a DelegatedTaskHandler");
		if (clock == null)
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a clock");
		this.ph = ph;
		this.uh = uh;
		this.dth = dth;
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
		if (taskToReschedule != null) {
			removeAllReservations(taskToReschedule);
			new PlanTaskSession(getUI(), ph, uh, dth, clock, taskToReschedule)
					.run();
		}
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
				if (reservation.getTask().equals(task)){
					developer.removeReservation(reservation);
					task.removeDeveloper(developer);
				}
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
