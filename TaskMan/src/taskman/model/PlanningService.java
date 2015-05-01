package taskman.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.IClock;
import taskman.model.time.TimeSpan;

public class PlanningService {

	IClock clock = Clock.getInstance();
	/**
	 * De constructor van de service.
	 */
	public PlanningService() {

	}

	/**
	 * Returns a set of possible starting times for the task. These are the
	 * (amount) first times that the required resources for the task are
	 * available starting from the earliest possible starting time.
	 * 
	 * @param task
	 * 			The task of which the starting times will be gathered
	 * @param amount
	 * 			The amount of starting times that will be gathered
	 * @param earliestPossibleStartTime
	 * 			The earliest time that the task should be started
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
		earliestPossibleStartTime = clock
				.getExactHour(earliestPossibleStartTime);
		Set<DateTime> possibleStartTimes = new TreeSet<DateTime>();
		DateTime startTime = clock
				.getFirstPossibleStartTime(earliestPossibleStartTime);
		//TODO: Deze loop kan oneindig worden!
		while (possibleStartTimes.size() < amount) {
			TimeSpan timeSpan = new TimeSpan(startTime, clock.addMinutes(
					startTime, task.getEstimatedDuration()));
			if (isValidTimeSpan(task, timeSpan, earliestPossibleStartTime))
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
	 * 			The task for which this will be checked for
	 * @param timeSpan
	 * 			The timespan for which will be checked if it is valid for the task
	 * @param earliestPossibleStartTime
	 * 			The earliest possible starting time for the task
	 * 
	 * @return Returns whether or not the timespan is valid for the task.
	 */
	// TODO: Check for developers?
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
			for (Entry<ResourceType, Integer> entry : requiredResourceTypes
					.entrySet()) {
				if (entry.getKey().getAvailableResources(timeSpan).size() < entry
						.getValue())
					return false;
			}
		}
		return true;
	}
}
