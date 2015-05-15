package taskman.model.project.task;

import org.joda.time.DateTime;

import taskman.model.time.TimeSpan;

class Unavailable implements Status {

	private final String name = "UNAVAILABLE";

	@Override
	public String getName() {
		return this.name;
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
		return false;
	}

	@Override
	public void updateStatus(Task task, DateTime currentTime) {
		if (task == null)
			throw new NullPointerException("The task is null.");
		
		if (!task.getReservations().isEmpty())
			task.performUpdateStatus(new Planned());
	}

	@Override
	public TimeSpan getTimeSpan(Task task) {
		throw new IllegalStateException(
				"Unavailable Task doesn't have timeSpan.");
	
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException {
		throw new IllegalStateException("This task is unavailable");
	
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		throw new IllegalStateException("The task is unavailable");
	}

	@Override
	public void addAlternative(Task task, Task alternative)
			throws IllegalStateException {
		throw new IllegalStateException("This task is unavailable");
	
	}

	@Override
	public boolean isSeverelyOverdue(Task task) {
		throw new IllegalStateException("The task is unavailable");
	}

	@Override
	public int calculateTotalExecutedTime(Task task)
			throws IllegalStateException {
		throw new IllegalStateException("The task is unavailable");
	}

	@Override
	public int calculateOverDuePercentage(Task task)
			throws IllegalStateException {
		throw new IllegalStateException("The task is unavailable");
	}

	@Override
	public void executeTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Unavailable task can not be executed");

	}

}
