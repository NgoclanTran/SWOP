package taskman.model.company;

import java.util.ArrayList;
import java.util.List;

import taskman.model.resource.ResourceType;

public class ResourceHandler {
	
	public ResourceHandler(List<ResourceType> resourceTypes) {
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
