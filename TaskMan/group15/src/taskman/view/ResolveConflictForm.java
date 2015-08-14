package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.task.Task;

public class ResolveConflictForm implements IResolveConflictForm {

	View view;

	/**
	 * The constructor of the resolve conflict form. It will setup the view to
	 * be able to get input and output.
	 * 
	 * @param view
	 */
	public ResolveConflictForm(View view) {
		this.view = view;
	}

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
			return null;
		}
	}

	/**
	 * This method will print a list of all conflicting tasks and asks the user
	 * to select one of them. Afterwards it will return the selected task.
	 * 
	 * @param conflictingTasks
	 * 
	 * @return Returns the selected conflicting task.
	 */
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