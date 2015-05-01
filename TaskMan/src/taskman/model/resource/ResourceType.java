package taskman.model.resource;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

import taskman.model.time.TimeSpan;

public class ResourceType {

	String name;
	boolean selfConflicting;
	List<ResourceType> requires = new ArrayList<ResourceType>();
	List<ResourceType> conflictsWith = new ArrayList<ResourceType>();
	List<Resource> resources = new ArrayList<Resource>();

	/**
	 * Creates a new resource type with the given name, and optionally other
	 * required or conflicting resource types, and a daily availability.
	 * 
	 * @param name
	 * @param requires
	 * @param conflictsWith
	 * @param selfConflicting
	 * 
	 * @throws IllegalArgumentException
	 */
	public ResourceType(String name, List<ResourceType> requires,
			List<ResourceType> conflictsWith, boolean selfConflicting)
					throws IllegalArgumentException {

		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		this.name = name;
		if (requires != null)
			this.requires.addAll(requires);
		if (conflictsWith != null)
			this.conflictsWith.addAll(conflictsWith);
		this.selfConflicting = selfConflicting;
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
	 * Returns whether or not the resource type conflicts with itself.
	 * 
	 * @return Returns whether or not the resource type conflicts with itself.
	 */
	public boolean isSelfConflicting() {
		return this.selfConflicting;
	}

	/**
	 * Returns the list of resources.
	 * 
	 * @return Returns the list of resources.
	 */
	public List<Resource> getResources() {
		return new ArrayList<Resource>(resources);
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
	 * Creates a new resource with the given name and adds it to the list.
	 * 
	 * @param name
	 * 
	 * @throws IllegalArgumentException
	 */
	public void addResource(String name, LocalTime startTime, LocalTime endTime)
			throws IllegalArgumentException {
		if(name == null) throw new IllegalArgumentException("The name cannot be null.");
		if(startTime == null) throw new IllegalArgumentException("The startTime cannot be null.");
		if(endTime == null) throw new IllegalArgumentException("The endTime cannot be null.");
		Resource resource = new Resource(name, startTime, endTime);
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
	public List<Resource> getSuggestedResources(TimeSpan timeSpan, int amount)
			throws IllegalArgumentException {
		if (timeSpan == null)
			throw new IllegalArgumentException("The timeSpan cannot be null.");
		if (amount < 0)
			throw new IllegalArgumentException("The amount cannot be negative.");

		List<Resource> resources = getResources();
		List<Resource> availableResources = getAvailableResources(timeSpan);
		List<Resource> suggestedResources = new ArrayList<Resource>();
		for (int i = 0; i < availableResources.size(); i++) {
			suggestedResources.add(availableResources.get(i));
		}
		if(amount > suggestedResources.size()){
			resources.removeAll(suggestedResources);
			for (int i = 0; i < amount - suggestedResources.size() && i < resources.size(); i++) {
				suggestedResources.add(resources.get(i));
			}
			return suggestedResources;
		}else{
			return suggestedResources.subList(0, amount);
		}
		
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
		if (timeSpan == null)
			throw new IllegalArgumentException("The timeSpan cannot be null.");
		ArrayList<Resource> availableResources = new ArrayList<Resource>();
		for (Resource resource : resources) {
			boolean available = resource.isAvailableAt(timeSpan);
			boolean requirements = true;
			if (requires != null && !requires.isEmpty()) {
				requirements = checkRequiredResourceTypes(timeSpan);
			}
			if (available && requirements) {
				availableResources.add(resource);
			}
		}
		return availableResources;
	}
	/**
	 * Will return true or false depending on whether there are available resources
	 * from the required resource types
	 * @param timeSpan
	 * 			The timespan for which this will be checked in
	 * @return returns true or false depending on whether there are available resources or not
	 */
	private boolean checkRequiredResourceTypes(TimeSpan timeSpan) {
		for (ResourceType resourceType : requires) {
			if (resourceType.getAvailableResources(timeSpan).size() > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	/**
	 * Will return the name of the resource type
	 * @return returns the name of the resource type
	 */
	public String toString() {
		return name;
	}
}
