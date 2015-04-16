package taskman.view;

import java.util.List;

import taskman.model.project.task.Task;

public interface ICreateTaskForm {

	public String getNewTaskDescription();

	public int getNewTaskEstimatedDuration();

	public int getNewTaskAcceptableDeviation();

	public List<Task> getNewTaskDependencies(List<Task> tasks);

	public Task getNewTaskAlternativeFor(List<Task> tasks);

}
