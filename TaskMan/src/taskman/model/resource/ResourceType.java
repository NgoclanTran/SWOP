package taskman.model.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalTime;

import taskman.model.time.TimeSpan;

public class ResourceType {

	String name;
	List<ResourceType> requires;
	List<ResourceType> conflictsWith;
	DailyAvailability dailyAvailability;
	List<Resource> resources;

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
		this.requires = requires;
		this.conflictsWith = conflictsWith;
		this.dailyAvailability = new DailyAvailability(startTime, endTime);
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

	public List<Resource> getSuggestedResources(TimeSpan timeSpan, int amount) {
		ArrayList<Resource> suggestedResources = new ArrayList<Resource>();
		return suggestedResources;
	}

	public List<Resource> getAvailableResources(TimeSpan timeSpan) {
		ArrayList<Resource> availableResources = new ArrayList<Resource>();
		for (Resource resource : resources) {
			if (resource.isAvailableAt(timeSpan)) {
				availableResources.add(resource);
			}
		}
		return availableResources;
	}

}
