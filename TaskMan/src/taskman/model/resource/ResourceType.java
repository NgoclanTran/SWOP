package taskman.model.resource;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

import taskman.model.time.TimeSpan;

public class ResourceType {

	String name;
	List<ResourceType> requires = new ArrayList<ResourceType>();
	List<ResourceType> conflictsWith = new ArrayList<ResourceType>();
	DailyAvailability dailyAvailability;
	List<Resource> resources = new ArrayList<Resource>();

	/**
	 * Creates a new resource type with the given name, and optionally other
	 * required or conflicting resource types, and a daily availability.
	 * 
	 * @param name
	 * @param requires
	 * @param conflictsWith
	 * @param startTime
	 * @param endTime
	 * 
	 * @throws IllegalArgumentException
	 */
	public ResourceType(String name, List<ResourceType> requires,
			List<ResourceType> conflictsWith, LocalTime startTime,
			LocalTime endTime) throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
		if (requires != null)
			this.requires.addAll(requires);
		if (conflictsWith != null)
			this.conflictsWith.addAll(conflictsWith);
		if (startTime == null && endTime == null)
			this.dailyAvailability = new DailyAvailability(new LocalTime(0, 0), new LocalTime(0, 0));
		else if (startTime != null && endTime != null)
			this.dailyAvailability = new DailyAvailability(startTime, endTime);
		else
			throw new IllegalArgumentException("Both the start time and end time need to be a localtime or null.");
	}

	/**
	 * Returns the name of the resource type.
	 * 
	 * @return Returns the name of the resource type.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the required resource types.
	 * 
	 * @return Returns the required resource types.
	 */
	public List<ResourceType> getRequires() {
		return new ArrayList<ResourceType>(this.requires);
	}

	/**
	 * Returns the conflicting resource types.
	 * 
	 * @return Returns the conflicting resource types.
	 */
	public List<ResourceType> getConflictsWith() {
		return new ArrayList<ResourceType>(this.conflictsWith);
	}

	/**
	 * Returns the daily availability of the resource type.
	 * 
	 * @return Returns the daily availability of the resource type.
	 */
	public DailyAvailability getDailyAvailability() {
		return this.dailyAvailability;
	}

	/**
	 * Creates a new resource with the given name and adds it to the list.
	 * 
	 * @param name
	 * 
	 * @throws IllegalArgumentException
	 */
	public void addResource(String name) throws IllegalArgumentException {
		Resource resource = new Resource(name);
		resources.add(resource);
	}

	/**
	 * Returns a list of resources that are suggested by the system to perform
	 * the task in the given timespan.
	 * 
	 * @param timeSpan
	 * @param amount
	 * 
	 * @return Returns a list of resources that are suggested by the system to
	 *         perform the task in the given timespan.
	 */
	public List<Resource> getSuggestedResources(TimeSpan timeSpan, int amount) {
		ArrayList<Resource> suggestedResources = new ArrayList<Resource>();
		for (int i = 0; i < amount; i++) {
			suggestedResources.add(getAvailableResources(timeSpan).get(i));
		}
		return suggestedResources;
	}

	/**
	 * Returns a list of all resources of this resource type that are available
	 * in the given timespan.
	 * 
	 * @param timeSpan
	 * 
	 * @return Returns a list of all resources of this resource type that are
	 *         available in the given timespan.
	 */
	public List<Resource> getAvailableResources(TimeSpan timeSpan) {
		ArrayList<Resource> availableResources = new ArrayList<Resource>();
		for (Resource resource : resources) {
			boolean available = resource.isAvailableAt(timeSpan);
			boolean requirements = false;
			if (requires != null) {
				requirements = checkRequiredResourceTypes(timeSpan);
			}
			if (available && requirements) {
				availableResources.add(resource);
			}
		}
		return availableResources;
	}

	private boolean checkRequiredResourceTypes(TimeSpan timeSpan) {
		for (ResourceType resourceType : requires) {
			if (resourceType.getAvailableResources(timeSpan).size() > 0) {
				return false;
			}
		}
		return true;
	}
}
