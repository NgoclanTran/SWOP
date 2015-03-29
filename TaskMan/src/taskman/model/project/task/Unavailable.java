package taskman.model.project.task;

import org.joda.time.DateTime;

class Unavailable implements Status {

	private final String name = "UNAVAILABLE";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void updateTaskAvailability(Task task) {
		if(task == null) throw new NullPointerException("The task is null.");
		task.performUpdateTaskAvailability(new Available());
		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException{
		throw new IllegalStateException("This task is unavailable");
		
	}

	@Override
	public void addAlternative(Task task, Task alternative) throws IllegalStateException{
		throw new IllegalStateException("This task is unavailable");
		
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
	public int calculateTotalExecutedTime(Task task) throws IllegalStateException{
		throw new IllegalStateException("The task is unavailable");
	}

	@Override
	public int calculateOverDuePercentage(Task task) throws IllegalStateException{
		throw new IllegalStateException("The task is unavailable");
	}

}
