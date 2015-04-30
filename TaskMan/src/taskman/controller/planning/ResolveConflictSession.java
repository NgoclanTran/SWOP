package taskman.controller.planning;

import java.util.ArrayList;
import java.util.List;

import taskman.controller.Session;
import taskman.model.ProjectHandler;
import taskman.model.UserHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Reservable;
import taskman.model.project.task.Reservation;
import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;
import taskman.view.IView;

public class ResolveConflictSession extends Session {

	UserHandler uh;
	Task task = null;
	TimeSpan timeSpan = null;
	List<Reservable> reservables = null;

	/**
	 * Creates the planning session using the given UI, ProjectHandler and
	 * ResourceHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public ResolveConflictSession(IView cli, ProjectHandler ph, UserHandler uh,
			Task task, TimeSpan timeSpan, List<Reservable> reservables) {
		super(cli, ph);
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The resolve conflict controller needs a UserHandler");
		this.uh = uh;
		this.task = task;
		this.timeSpan = timeSpan;
		this.reservables = reservables;
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

	@Override
	public void run() throws IllegalStateException {
		if (reservables.isEmpty())
			throw new IllegalStateException(
					"A conflict cannot have an empty reservable list.");

		Task taskToReschedule = getTaskToReschedule(getConflictingTasks());
		Project project = getProject(taskToReschedule);
		//TODO: Remove old reservations!
		new PlanTaskSession(getUI(), getPH(), uh, project, taskToReschedule).run();
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

}
