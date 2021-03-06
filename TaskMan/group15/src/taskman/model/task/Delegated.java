package taskman.model.task;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;
import taskman.model.time.TimeSpan;

public class Delegated implements Status {

	private final String name = "DELEGATED";

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
	public boolean isDelegated() {
		return true;
	}

	@Override
	public void updateStatus(Task task, DateTime currentTime)
			throws IllegalStateException {
		throw new IllegalStateException("This task is already delegated");
	}

	@Override
	public TimeSpan getTimeSpan(Task task) throws IllegalStateException {
		throw new IllegalStateException("Delegated Task doesn't have timeSpan.");
	}

	@Override
	public void addTimeSpan(Task task, boolean failed, DateTime startTime,
			DateTime endTime) throws IllegalStateException {
		if (task == null)
			throw new NullPointerException("The task is null.");
		if (startTime == null)
			throw new NullPointerException("The startTime is null.");
		if (endTime == null)
			throw new NullPointerException("The endTime is null.");
		if (startTime.compareTo(endTime) > 0)
			throw new IllegalDateException(
					"The startTime must start before endTime.");
		task.performAddTimeSpan(startTime, endTime);
		if (failed) {
			task.performUpdateStatus(new Failed());
		} else {
			task.performUpdateStatus(new Finished());
		}
	}

	@Override
	public boolean isAlternativeFinished(Task task) {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public void addAlternative(NormalTask task, NormalTask alternative)
			throws IllegalStateException {
		throw new IllegalStateException("This task is already executing");
	}

	@Override
	public boolean isSeverelyOverdue(Task task) throws IllegalStateException {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public int calculateTotalExecutedTime(Task task)
			throws IllegalStateException {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public int calculateOverDuePercentage(Task task)
			throws IllegalStateException {
		throw new IllegalStateException("The task hasn't been completed.");
	}

	@Override
	public void executeTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Delegated task can not be executed.");
	}
	
	@Override
	public void delegateTask(Task task) throws IllegalStateException {
		throw new IllegalStateException("Delegated task can not be delegated.");
	}

}
