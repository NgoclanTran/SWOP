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
	/**
	 * Will make a delegated task with the given parameters
	 * @param description
	 * 			The description for the delegated task to be made
	 * @param estimatedDuration
	 * 			The estimated duration for the delegated task to be made
	 * @param acceptableDeviation
	 * 			The acceptable deviation for the delegated task to be made
	 * @param resourceTypes
	 * 			The resource types for the delegated task to be made
	 * @param dependenciesFinished
	 * 			A boolean indicating whether the dependencies are finished for the delegated task to be made
	 * @param developerAmount
	 * 			An integer indicating the amount of developers needed for the delegated task to be made
	 * @return
	 * 			returns the constructed delegated task
	 * @throws IllegalArgumentException
	 * 			Will throw an exception incase one of the given parameters is illegal
	 */
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
