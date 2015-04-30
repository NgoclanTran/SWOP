package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.project.task.Task;

public class ResolveConflictForm implements IResolveConflictForm {

	View view;

	public ResolveConflictForm(View view) {
		this.view = view;
	}

	@Override
	public Task getTaskToRechedule(Task task, List<Task> conflictingTasks) {
		try {
			view.displayInfo("Conflicting tasks:");
			view.displayTaskList(conflictingTasks, 0, false);

			String rescheduleCurrentTask = "";
			while (!(view.isValidYesAnswer(rescheduleCurrentTask) || view
					.isValidNoAnswer(rescheduleCurrentTask))) {
				view.displayInfo("Do you want to reschedule the task currently being planned? (Y/N or cancel):");
				rescheduleCurrentTask = view.input.getInput();
				view.output.displayEmptyLine();
			}

			if (view.isValidYesAnswer(rescheduleCurrentTask))
				return task;
			else
				return getConflictingTaskToReschedule(conflictingTasks);
		} catch (ShouldExitException e) {
			return getTaskToRechedule(task, conflictingTasks);
		}
	}

	private Task getConflictingTaskToReschedule(List<Task> conflictingTasks) {
		try {
			view.displayTaskList(conflictingTasks, 0, false);
			int taskId = view.getListChoice(conflictingTasks,
					"Select a conflicting task:");
			return conflictingTasks.get(taskId - 1);
		} catch (ShouldExitException e) {
			return getConflictingTaskToReschedule(conflictingTasks);
		}
	}

}