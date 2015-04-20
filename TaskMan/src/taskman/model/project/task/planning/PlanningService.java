package taskman.model.project.task.planning;

import taskman.model.DeveloperHandler;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;

public class PlanningService {

	DeveloperHandler dh;
	ProjectHandler ph;
	ResourceHandler rh;

	public PlanningService(DeveloperHandler dh, ProjectHandler ph,
			ResourceHandler rh) {
		this.dh = dh;
		this.ph = ph;
		this.rh = rh;
	}

}
