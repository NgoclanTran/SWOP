package taskman.model.project.task;

import org.joda.time.DateTime;

class Failed implements Status{

	private final String name = "FAILED";
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative) {
		task.performAddAlternative(alternative);
		
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
	public void updateTaskAvailability(Task task) throws IllegalStateException{
		throw new IllegalStateException("This task is failed");
		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException{
		throw new IllegalStateException("This task is failed");
		
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
	public boolean isAlternativeCompleted(Task task) {
		return task.isAlternativeCompleted();
	}

}
