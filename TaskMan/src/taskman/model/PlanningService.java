package taskman.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.IClock;
import taskman.model.time.TimeSpan;

public class PlanningService {

	ResourceHandler rh;
	IClock clock = Clock.getInstance();

	public PlanningService(ResourceHandler rh) {
		this.rh = rh;
	}

	public Set<DateTime> getPossibleStartTimes(Task task, int amount,
			DateTime earliestPossibleStartTime) {
		// TODO First check if there are enough resources before getting their reservations
		return null;
	}

	// TODO Documentation
	public boolean isValidTimeSpan(Task task, TimeSpan timeSpan,
			DateTime earliestPossibleStartTime) {
		if (earliestPossibleStartTime == null) {
			earliestPossibleStartTime = clock.getSystemTime();
		}
		if (timeSpan.getStartTime().isBefore(earliestPossibleStartTime)) {
			return false;
		} else {
			Map<ResourceType, Integer> requiredResourceTypes = task
					.getRequiredResourceTypes();
			List<ResourceType> resourceTypes = rh.getResourceTypes();
			for (ResourceType resourceType : resourceTypes) {
				if (requiredResourceTypes.containsKey(resourceType)) {
					if (resourceType.getAvailableResources(timeSpan).size() < requiredResourceTypes
							.get(resourceType)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
