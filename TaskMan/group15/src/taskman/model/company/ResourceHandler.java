package taskman.model.company;

import java.util.ArrayList;
import java.util.List;

import taskman.model.resource.ResourceType;

public class ResourceHandler {
	/**
	 * Creates the resource types list
	 * @param resourceTypes
	 * 			The list of resource types to be made
	 */
	public ResourceHandler(List<ResourceType> resourceTypes) {
		if(resourceTypes == null) throw new IllegalArgumentException("The resourcesTypes cannot be null.");
		this.resourceTypes = resourceTypes;
	}

	/**
	 * Returns the list of resource types.
	 * 
	 * @return Returns the list of resource types.
	 */
	public List<ResourceType> getResourceTypes() {
		return new ArrayList<ResourceType>(resourceTypes);
	}

	private final List<ResourceType> resourceTypes;

}
