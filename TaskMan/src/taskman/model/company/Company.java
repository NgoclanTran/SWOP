package taskman.model.company;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.joda.time.DateTime;

import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.time.TimeService;

public class Company {

	ArrayList<BranchOffice> branchOffices;

	public Company() {
		branchOffices = new ArrayList<BranchOffice>();
	}

	public String getName() {
		return name;
	}

	private final String name = "SWOP15";

	/**
	 * Adds a branch office to the list of branch offices in the company
	 * 
	 * @param branchOffice
	 */
	public void addBranchOffice(BranchOffice branchOffice) {
		if (branchOffice == null)
			throw new IllegalArgumentException(
					"The branche office cannot be null.");
		branchOffices.add(branchOffice);
	}

	public List<BranchOffice> getBranchOffices() {
		return new ArrayList<BranchOffice>(branchOffices);
	}

	/**
	 * Sets the dependencies status of the delegated task with the given id to
	 * true.
	 * 
	 * @param id
	 */
	public void setDependenciesFinished(UUID id) {
		for (BranchOffice branchOffice : branchOffices) {
			for (DelegatedTask delegatedTask : branchOffice.getDth()
					.getDelegatedTasks()) {
				if (delegatedTask.getParentID().equals(id)) {
					delegatedTask.setDependenciesAreFinished(true);
				}
			}
		}
	}

	public void announceCompletion(DelegatedTask task) {
		if (task == null)
			throw new IllegalArgumentException("The task cannot be null.");
		UUID parent = task.getParentID();
		for (BranchOffice branchOffice : branchOffices) {
			for (Project project : branchOffice.getPh().getProjects()) {
				for (NormalTask normalTask : project.getTasks()) {
					if (!normalTask.isCompleted()
							&& normalTask.getID().equals(parent)) {
						DateTime endTime = branchOffice.getClock()
								.getSystemTime();
						DateTime startTime = new TimeService().subtractMinutes(
								endTime, task.getTotalExecutionTime());
						normalTask.completeTask(task.isFailed(), startTime,
								endTime);
					}
				}
			}
		}
	}

	public void delegateTask(Task task, BranchOffice toBranchOffice)
			throws IllegalStateException {
		if (task == null)
			throw new IllegalArgumentException("The task cannot be null.");
		if (toBranchOffice == null)
			throw new IllegalArgumentException(
					"The branch office cannot be null.");

		UUID id = task.getID();
		String description = task.getDescription();
		int estimatedDuration = task.getEstimatedDuration();
		int acceptableDeviation = task.getAcceptableDeviation();
		int developerAmount = task.getRequiredAmountOfDevelopers();
		LinkedHashMap<ResourceType, Integer> requiredResourceTypes = new LinkedHashMap<ResourceType, Integer>();
		boolean dependenciesFinished = task.dependenciesAreFinished();

		for (Entry<ResourceType, Integer> entry : task
				.getRequiredResourceTypes().entrySet()) {
			ResourceType typeToAdd = null;
			for (ResourceType type : toBranchOffice.getRh().getResourceTypes()) {
				if (entry.getKey().getName().equals(type.getName()))
					typeToAdd = entry.getKey();
			}

			if (typeToAdd == null)
				throw new IllegalStateException("Resource type is missing.");
			else
				requiredResourceTypes.put(typeToAdd, entry.getValue());
		}

		toBranchOffice.getDth().addDelegatedTask(id, description,
				estimatedDuration, acceptableDeviation, requiredResourceTypes,
				dependenciesFinished, developerAmount);
		task.delegateTask();
		announceDelegate(task, toBranchOffice.toString());
	}

	private void announceDelegate(Task task, String toBranchOffice) {
		task.setResponsibleBranchOffice(toBranchOffice);
		if (task.getParentID() != null) {
			Task toUpdate = findTask(task.getParentID());
			announceDelegate(toUpdate, toBranchOffice);
		}
	}

	private Task findTask(UUID id) {
		for (BranchOffice branchOffice : branchOffices) {
			for (Project project : branchOffice.getPh().getProjects()) {
				for (NormalTask normalTask : project.getTasks()) {
					if (normalTask.getID().equals(id)) {
						return normalTask;
					}
				}
			}
			for (DelegatedTask delegatedTask : branchOffice.getDth()
					.getDelegatedTasks()) {
				if (delegatedTask.getID().equals(id)) {
					return delegatedTask;
				}
			}
		}
		return null;
	}
}
