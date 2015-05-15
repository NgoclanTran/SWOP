package taskman.model.project.task;

import org.joda.time.DateTime;

import taskman.model.time.TimeSpan;

class Failed implements Status {

	private final String name = "FAILED";

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
		return true;
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
	public void updateStatus(Task task, DateTime currentTime)
			throws IllegalStateException {
		throw new IllegalStateException("This task is failed");

	}

	@Override
	public TimeSpan getTimeSpan(Task task) {
		return task.performGetTimeSpan();
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException {
		throw new IllegalStateException("This task is failed");
	
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		return task.isAlternativeFinished();
	}

	@Override
	public void addAlternative(Task task, Task alternative) {
		task.performAddAlternative(alternative);
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
		throw new IllegalStateException("Failed task can not be executed");
	}

}
