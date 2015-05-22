package taskman.model.task;

import java.util.List;
import java.util.Map;

import taskman.model.company.BranchOffice;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class TaskFactory {

	private final Clock clock;
	private final BranchOffice branchOffice;

	/**
	 * Creates the task factory.
	 * 
	 * @param clock
	 *            The system clock.
	 * 
	 * @throws IllegalArgumentException
	 *             The clock needs to be valid.
	 */
	public TaskFactory(BranchOffice branchOffice, Clock clock) {
		if (branchOffice == null)
			throw new IllegalArgumentException(
					"The task factory needs a branch office.");
		if (clock == null)
			throw new IllegalArgumentException(
					"The task factory needs a clock.");
		this.branchOffice = branchOffice;
		this.clock = clock;
	}

	/**
	 * This method will make a new task, attach the task to the clock and return
	 * the task.
	 * 
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 * @param dependencies
	 * @param alternativeFor
	 * @param resourceTypes
	 * 
	 * @return Returns the new task made with the given parameters.
	 * 
	 * @throws IllegalArgumentException
	 */
	public NormalTask makeNormalTask(String description, int estimatedDuration,
			int acceptableDeviation, List<NormalTask> dependencies,
			NormalTask alternativeFor,
			Map<ResourceType, Integer> resourceTypes, int developerAmount)
			throws IllegalArgumentException {
		NormalTask task = new NormalTask(clock, description, estimatedDuration,
				acceptableDeviation, dependencies, alternativeFor,
				resourceTypes, developerAmount);
		clock.attach(task);
		task.attach(branchOffice);
		task.setResponsibleBranchOffice(branchOffice.getLocation());
		return task;
	}

	public DelegatedTask makeDelegatedTask(String description,
			int estimatedDuration, int acceptableDeviation,
			Map<ResourceType, Integer> resourceTypes,
			boolean dependenciesFinished, int developerAmount)
			throws IllegalArgumentException {
		DelegatedTask task = new DelegatedTask(clock, description,
				estimatedDuration, acceptableDeviation, resourceTypes,
				dependenciesFinished, developerAmount);
		clock.attach(task);
		task.attach(branchOffice);
		task.setResponsibleBranchOffice(branchOffice.getLocation());
		return task;
	}

}
