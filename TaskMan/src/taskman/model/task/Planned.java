package taskman.model.task;

import org.joda.time.DateTime;

import taskman.model.time.TimeSpan;

public class Planned implements Status {

	private final String name = "PLANNED";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isAvailable() {
		return false;
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
	public void updateStatus(Task task, DateTime currentTime) throws IllegalStateException {
		if (task.dependenciesAreFinished()) {
			for (Reservation reservation : task.getReservations()) {
				if (reservation.getTimeSpan().isDuringTimeSpan(timeService.getFirstPossibleStartTime(currentTime))) {
					task.performUpdateStatus(new Available());
					task.update();
				} else
					if (task.developersAndResourceTypesAvailable(timeService.getFirstPossibleStartTime(currentTime))) {
					task.performUpdateStatus(new Available());
					task.update();
				}
			}
		}
	}

	@Override
	public TimeSpan getTimeSpan(Task task) {
		throw new IllegalStateException("Planned task doesn't have timeSpan.");
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime, DateTime endTime)
			throws IllegalStateException {
		throw new IllegalStateException("Planned task can not have a timespan.");
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public void addAlternative(NormalTask task, NormalTask alternative) throws IllegalStateException {
		throw new IllegalStateException("The task has not failed.");
	}

	@Override
	public boolean isSeverelyOverdue(Task task) {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public int calculateTotalExecutedTime(Task task) throws IllegalStateException {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public int calculateOverDuePercentage(Task task) throws IllegalStateException {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public void executeTask(Task task) throws IllegalStateException {
		task.performExecuteTask(new Executing());
	}

	@Override
	public void delegateTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Planned task can not be delegated.");
	}

}
