package taskman.model.facade;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

import taskman.model.resource.*;

public class ResourceTypeHandler {

	private List<ResourceType> resourceTypes;
	public ResourceTypeHandler(List<ResourceType> resourceTypes) {
		if(resourceTypes.isEmpty()) resourceTypes = new ArrayList<ResourceType>();
		this.resourceTypes = resourceTypes;
	}
	
	public void addResourceType(String name, List<ResourceType> requires, List<ResourceType> conflictsWith, LocalTime startTime, LocalTime endTime) throws IllegalArgumentException{
			ResourceType nRT = new ResourceType(name, requires, conflictsWith, startTime, endTime);
			addResourceType(nRT);
	}
	private void addResourceType(ResourceType rt){
		this.resourceTypes.add(rt);
	}
	public List<Reservation> getReservations(ResourceType rt){
		return null;
	}

}
