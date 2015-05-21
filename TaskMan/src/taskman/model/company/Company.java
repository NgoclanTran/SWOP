package taskman.model.company;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;

public class Company {

	ArrayList<BranchOffice> branchOffices;

	public Company() {
		branchOffices = new ArrayList<BranchOffice>();
	}

	/**
	 * Adds a branch office to the list of branch offices in the company
	 * 
	 * @param branchOffice
	 */
	public void addBranchOffice(BranchOffice branchOffice) {
		branchOffices.add(branchOffice);
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
				if (delegatedTask.getParentID() == id) {
					delegatedTask.setDependenciesAreFinished(true);
				}
			}
		}
	}

	public void delegateTask(NormalTask task, BranchOffice toBranchOffice) throws IllegalStateException {
		UUID id = task.getID();
		String description = task.getDescription();
		int estimatedDuration = task.getEstimatedDuration();
		int acceptableDeviation = task.getAcceptableDeviation();
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
				dependenciesFinished);
		task.delegateTask();
	}

}
