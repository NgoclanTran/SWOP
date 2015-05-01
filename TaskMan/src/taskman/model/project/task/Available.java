package taskman.model.project.task;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

class Available implements Status {

	private final String name = "AVAILABLE";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative)
			throws IllegalStateException {
		throw new IllegalStateException(
				"The alternative for task has not failed.");
	}

	@Override
	public void updateTaskAvailability(Task task) throws IllegalStateException {
		if (task == null)
			throw new IllegalStateException("Task cannot be null.");

		if (task.dependenciesAreFinished() && task.isPlanned()) {
			for (Reservation reservation : task.getReservations()) {
				if (!reservation.getTimeSpan().isDuringTimeSpan(
						clock.getSystemTime())
						&& !task.developersAndResourceTypesAvailable(clock
								.getSystemTime()))
					task.performUpdateTaskAvailability(new Unavailable());
			}
		}
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) {
		throw new IllegalStateException("Available task can not get a timespan.");
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isFailed() {
		return false;
	}

	@Override
	public int calculateTotalExecutedTime(Task task) {
		// return task.performGetTotalExecutionTime();
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		// return task.performGetOverduePercentage();
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		// throw new
		// IllegalStateException("The available task doesn't have alternative.");
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public boolean isSeverelyOverdue(Task task) {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public boolean isPlanned(Task task) {
		return task.performIsPlanned();
	}

	@Override
	public boolean isExecuting() {
		return false;
	}
	
	@Override
	public void executeTask(Task task) throws IllegalStateException {
		task.performExecuteTask(new Executing());
	}

}