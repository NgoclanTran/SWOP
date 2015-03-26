package taskman.model.project.task;

import org.joda.time.DateTime;

public class Finished implements Status{

	private final String name = "FINISHED";

	@Override
	public String getName() {
		
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative) {
		// TODO Auto-generated method stub
		
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
		return task.calculateTotalExecutionTime();
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		
		return task.calculateOverduePercentage();
	}

}
