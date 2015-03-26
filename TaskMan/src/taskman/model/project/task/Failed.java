package taskman.model.project.task;

import org.joda.time.DateTime;

public class Failed implements Status{

	private final String name = "FAILED";
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative) {
		task.setAlternative(alternative);
		
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
	public void updateTaskAvailability(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int calculateTotalExecutedTime(Task task) {
		return task.calculateTotalExecutionTime();
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		return task.calculateOverduePercentage();
	}

}
