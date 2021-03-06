package taskman.model.task;

import org.joda.time.DateTime;

import taskman.model.time.TimeSpan;

class Finished implements Status {

	private final String name = "FINISHED";

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
		return true;
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
		throw new IllegalStateException("This task is already finished");
	}

	@Override
	public TimeSpan getTimeSpan(Task task) {
		return task.performGetTimeSpan();
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException {
		throw new IllegalStateException("This task is already finished");
	
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		throw new IllegalStateException(
				"The available task doesn't have alternative.");
	}

	@Override
	public void addAlternative(NormalTask task, NormalTask alternative)
			throws IllegalStateException {
		throw new IllegalStateException("This task is already finished");
	
	}

	@Override
	public boolean isSeverelyOverdue(Task task) {
		return task.performIsSeverelyOverDue();
	}

	@Override
	public int calculateTotalExecutedTime(Task task) {
		return task.performGetTotalExecutionTime();
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		return task.performGetOverduePercentage();
	}

	@Override
	public void executeTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Finished task can not be executed");
	}
	
	@Override
	public void delegateTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Finished task can not be delegated.");
	}

}
