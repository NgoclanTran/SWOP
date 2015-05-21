package taskman.view;

import java.util.List;

import taskman.model.task.Task;

public interface IResolveConflictForm {

	/**
	 * This method will show all the conflicting tasks and ask the user to
	 * either reschedule the task currently being planned or one of the
	 * conflicting tasks. It will return the task the user selected and restart
	 * the planning use case.
	 * 
	 * @param task
	 * @param conflictingTasks
	 * 
	 * @return Returns the task the user wants to reschedule.
	 */
	public Task getTaskToRechedule(Task task, List<Task> conflictingTasks);

}
