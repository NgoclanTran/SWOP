package taskman.model.task;

import org.joda.time.DateTime;

import taskman.model.time.TimeSpan;

class Available implements Status {

	private final String name = "AVAILABLE";

	@Override
	public String getName() {
		return name;
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
	public boolean isExecuting() {
		return false;
	}
	
	@Override
	public boolean isPlanned() {
		return true;
	}
	
	@Override
	public boolean isDelegated() {
		return false;
	}

	@Override
	public void updateStatus(Task task, DateTime currentTime)
			throws IllegalStateException {
		if (task == null)
			throw new IllegalStateException("Task cannot be null.");
	
		if (task.dependenciesAreFinished()) {
			for (Reservation reservation : task.getReservations()) {
				if (!reservation.getTimeSpan().isDuringTimeSpan(currentTime)
						&& !task.developersAndResourceTypesAvailable(currentTime))
					task.performUpdateStatus(new Planned());
			}
		}
	}

	@Override
	public TimeSpan getTimeSpan(Task task) throws IllegalStateException {
		throw new IllegalStateException("Available Task doesn't have timeSpan.");
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) {
		throw new IllegalStateException(
				"Available task can not get a timespan.");
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		// throw new
		// IllegalStateException("The available task doesn't have alternative.");
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public void addAlternative(NormalTask task, NormalTask alternative)
			throws IllegalStateException {
		throw new IllegalStateException(
				"The task has not failed.");
	}

	@Override
	public boolean isSeverelyOverdue(Task task) {
		throw new IllegalStateException("The task hasn't been completed.");
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
	public void executeTask(Task task) throws IllegalStateException {
		task.performExecuteTask(new Executing());
	}

	@Override
	public void delegateTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Available task can not be delegated.");
	}

}