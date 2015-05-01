package taskman.model;

import java.util.ArrayList;
import java.util.List;

import taskman.model.resource.ResourceType;

public class ResourceHandler {

	/**
	 * Returns the list of resource types.
	 * 
	 * @return Returns the list of resource types.
	 */
	public List<ResourceType> getResourceTypes() {
		return new ArrayList<ResourceType>(resourceTypes);
	}

	/**
	 * Adds the resource type to the list of resource types.
	 * 
	 * @param resourceType
	 */
	private void addResourceType(ResourceType resourceType) {
		resourceTypes.add(resourceType);
	}

	/**
	 * Creates a new resource type and adds it to the list of resource types.
	 * 
	 * @param name
	 * 			The name of the resource type to be created
	 * @param requires
	 * 			The list of required resource types for the resource to be created
	 * @param conflictsWith
	 * 			The list of resources that this resource conflicts with
	 * @param selfConflicting
	 * 			An indicator whether this resource conflicts with itself or not
	 * 
	 * @throws IllegalArgumentException
	 */
	public void addResourceType(String name, List<ResourceType> requires,
			List<ResourceType> conflictsWith, boolean selfConflicting)
			throws IllegalArgumentException {
		ResourceType resourceType = new ResourceType(name, requires,
				conflictsWith, selfConflicting);
		addResourceType(resourceType);
	}

	private List<ResourceType> resourceTypes = new ArrayList<ResourceType>();

}
