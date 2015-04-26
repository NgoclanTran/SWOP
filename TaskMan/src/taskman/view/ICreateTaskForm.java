package taskman.view;

import java.util.List;
import java.util.Map;

import taskman.model.ResourceHandler;
import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;

public interface ICreateTaskForm {

	public String getNewTaskDescription();

	public int getNewTaskEstimatedDuration();

	public int getNewTaskAcceptableDeviation();

	public List<Task> getNewTaskDependencies(List<Task> tasks);

	public Task getNewTaskAlternativeFor(List<Task> tasks);
	
	public Map<ResourceType, Integer> getNewTaskResourceTypes(ResourceHandler rh);

}
