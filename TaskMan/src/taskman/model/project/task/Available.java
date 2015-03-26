package taskman.model.project.task;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

public class Available  implements Status {


	private final String name = "AVAILABLE";
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative) {
		
	}

	@Override
	public void updateTaskAvailability(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) {
		if(task == null) throw new NullPointerException("The task is null.");
		if(startTime == null) throw new NullPointerException("The startTime is null.");
		if(endTime == null) throw new NullPointerException("The endTime is null.");
		if(startTime.compareTo(endTime) > 0) throw new IllegalDateException("The startTime must start before endTime.");
		if(failed){
			task.updateStatus(new Failed());
		}
		else{
			task.updateStatus(new Finished());
		}
		task.addTimeSpan(startTime, endTime);
	}

	@Override
	public boolean isAvailable() {
		return true;
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
