package taskman.view;

import java.util.List;

import taskman.model.project.task.Task;

public interface IResolveConflictForm {
	
	public Task getTaskToRechedule(Task task, List<Task> conflictingTasks);

}
