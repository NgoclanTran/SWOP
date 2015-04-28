package taskman.model.project.task;

import org.joda.time.DateTime;

class Finished implements Status{

	private final String name = "FINISHED";

	@Override
	public String getName() {
		
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative) throws IllegalStateException{
		throw new IllegalStateException("This task is already finished");
		
	}

	@Override
	public void updateTaskAvailability(Task task) throws IllegalStateException{
		throw new IllegalStateException("This task is already finished");		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException{
		throw new IllegalStateException("This task is already finished");
		
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
	public int calculateTotalExecutedTime(Task task) {
		return task.performGetTotalExecutionTime();
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		return task.performGetOverduePercentage();
	}

	@Override
	public boolean isAlternativeCompleted(Task task) {
		throw new IllegalStateException("The available task doesn't have alternative.");
	}

	@Override
	public boolean isSeverelyOverdue(Task task) {
		return task.performIsSeverelyOverDue();
	}

	@Override
	public boolean isPlanned(Task task) {
		return true;
	}

}
