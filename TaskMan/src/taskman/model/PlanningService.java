package taskman.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
		if (rh == null)
			throw new IllegalArgumentException(
					"Resource handler can not be null.");
		this.rh = rh;
	}

	/**
	 * Returns a set of possible starting times for the task. These are the
	 * (amount) first times that the required resources for the task are
	 * available starting from the earliest possible starting time.
	 * 
	 * @param task
	 * @param amount
	 * @param earliestPossibleStartTime
	 * 
	 * @return Returns a set of possible starting times for the task.
	 */
	public Set<DateTime> getPossibleStartTimes(Task task, int amount,
			DateTime earliestPossibleStartTime) {
		if (task == null)
			throw new IllegalArgumentException("Task can not be null.");
		if (!(amount > 0))
			throw new IllegalArgumentException(
					"Amount has to be greater than 0");
		if (earliestPossibleStartTime == null
				|| earliestPossibleStartTime.isBefore(clock.getSystemTime()))
			earliestPossibleStartTime = clock.getSystemTime();
		earliestPossibleStartTime = clock.resetSecondsAndMilliSeconds(earliestPossibleStartTime);
		Set<DateTime> possibleStartTimes = new TreeSet<DateTime>();
		DateTime startTime = clock
				.getFirstPossibleStartTime(earliestPossibleStartTime);
		while (possibleStartTimes.size() < amount) {
			if (isValidTimeSpan(
					task,
					new TimeSpan(startTime, startTime.plusMinutes(task
							.getEstimatedDuration())), earliestPossibleStartTime))
				possibleStartTimes.add(startTime);
			startTime = startTime.plusHours(1);
			startTime = clock.addBreaks(startTime);
		}
		return possibleStartTimes;
	}

	/**
	 * Checks if the timespan is valid for the task. It is valid if it is after
	 * the earliest possible starting time and all the required resources are
	 * available for the entire duration.
	 * 
	 * @param task
	 * @param timeSpan
	 * @param earliestPossibleStartTime
	 * 
	 * @return Returns whether or not the timespan is valid for the task.
	 */
	//TODO: Check for developers?
	public boolean isValidTimeSpan(Task task, TimeSpan timeSpan,
			DateTime earliestPossibleStartTime) {
		if (task == null)
			throw new IllegalArgumentException("Task can not be null.");
		if (earliestPossibleStartTime == null) {
			earliestPossibleStartTime = clock.getSystemTime();
		}
		if (timeSpan == null) {
			throw new IllegalArgumentException("TimeSpan can not be null");
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
