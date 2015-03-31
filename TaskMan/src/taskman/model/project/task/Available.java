package taskman.model.project.task;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

class Available  implements Status {


	private final String name = "AVAILABLE";
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addAlternative(Task task, Task alternative) throws IllegalStateException{
		throw new IllegalStateException("Task is not failed");
	}

	@Override
	public void updateTaskAvailability(Task task) throws IllegalStateException{
		throw new IllegalStateException("This task is already available");		
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) {
		if(task == null) throw new NullPointerException("The task is null.");
		if(startTime == null) throw new NullPointerException("The startTime is null.");
		if(endTime == null) throw new NullPointerException("The endTime is null.");
		if(startTime.compareTo(endTime) > 0) throw new IllegalDateException("The startTime must start before endTime.");
		if(failed){
			task.performUpdateTaskAvailability(new Failed());
		}
		else{
			task.performUpdateTaskAvailability(new Finished());
		}
		task.performAddTimeSpan(startTime, endTime);
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
		//return task.performGetTotalExecutionTime();
		throw new IllegalStateException("The available task doesn't have a timespan.");
	}

	@Override
	public int calculateOverDuePercentage(Task task) {
		//return task.performGetOverduePercentage();
		throw new IllegalStateException("The available task doesn't have a timespan.");
	}

	@Override
	public boolean isAlternativeCompleted(Task task) {
		//throw new IllegalStateException("The available task doesn't have alternative.");
		throw new IllegalStateException("The available task doesn't have a timespan.");
	}


}
