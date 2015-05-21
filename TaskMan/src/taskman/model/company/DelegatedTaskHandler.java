package taskman.model.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.TaskFactory;

public class DelegatedTaskHandler {

	private TaskFactory factory;
	private List<DelegatedTask> delegatedTasks;

	public DelegatedTaskHandler(TaskFactory factory)
			throws IllegalArgumentException {
		if (factory == null)
			throw new IllegalArgumentException(
					"A delegated task handler needs a task factory.");
		this.factory = factory;
		delegatedTasks = new ArrayList<DelegatedTask>();
	}

	/**
	 * Returns the list of tasks delegated to this branch office
	 * 
	 * @return The list of tasks delegated to this branch office
	 */
	public List<DelegatedTask> getDelegatedTasks() {
		return new ArrayList<DelegatedTask>(delegatedTasks);
	}

	private void addDelegatedTask(DelegatedTask task) {
		delegatedTasks.add(task);
	}

	public void addDelegatedTask(UUID id, String description,
			int estimatedDuration, int acceptableDeviation,
			Map<ResourceType, Integer> resourceTypes, boolean dependenciesFinished) {
		DelegatedTask taskToAdd = factory.makeDelegatedTask(description,
				estimatedDuration, acceptableDeviation, resourceTypes,
				dependenciesFinished);
		addDelegatedTask(taskToAdd);
	}
}