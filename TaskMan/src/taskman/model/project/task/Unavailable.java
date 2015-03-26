package taskman.model.project.task;

import org.joda.time.DateTime;

public class Unavailable implements Status {

	private final String name = "UNAVAILABLE";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void updateTaskAvailability(Task task) {
		if(task == null) throw new NullPointerException("The task is null.");
		task.updateStatus(new Available());
		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAlternative(Task task, Task alternative) {
		// TODO Auto-generated method stub
		
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
	public int calculateTotalExecutedTime(Task task) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		return 0;
	}

}
