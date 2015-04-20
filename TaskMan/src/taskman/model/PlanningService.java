package taskman.model;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;
import taskman.model.time.TimeSpan;

public class PlanningService {

	UserHandler uh;
	ProjectHandler ph;
	ResourceHandler rh;

	public PlanningService(UserHandler uh, ProjectHandler ph, ResourceHandler rh) {
		this.uh = uh;
		this.ph = ph;
		this.rh = rh;
	}

	public List<DateTime> getPossibleStartTimes(Task task, int amount,
			DateTime earliestPossibleStartTime) {
		// TODO
		// DateTime start = earliestPossibleStartTime;
		// ArrayList<DateTime> possibleStartTimes = new ArrayList<DateTime>();
		// while (possibleStartTimes.size() < amount) {
		// isValidTimeSpan(task, start)
		// }
		return null;
	}

	public boolean isValidTimeSpan(Task task, TimeSpan timeSpan,
			DateTime earliestPossibleStartTime) {
		if (timeSpan.getStartTime().isBefore(earliestPossibleStartTime)) {
			return false;
		} else {
			Map<ResourceType, Integer> requiredResourceTypes = task
					.getRequiredResourceTypes();
			List<ResourceType> resourceTypes = rh.getResourceTypes();
			for (ResourceType resourceType : resourceTypes) {
				if (requiredResourceTypes.containsKey(resourceType)){
					resourceType.getAvailableResources(timeSpan);
				}
			}
		}
		return true;
	}

}
