package taskman.model;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class PlanningService {

	UserHandler uh;
	ProjectHandler ph;
	ResourceHandler rh;

	public PlanningService(UserHandler uh, ProjectHandler ph, ResourceHandler rh) {
		this.uh = uh;
		this.ph = ph;
		this.rh = rh;
	}

	public List<DateTime> getPossibleStartTimes(Task task, int amount,
			DateTime earliestPossibleStartTime) {
		// TODO
		return null;
	}

	public boolean isValidTimeSpan(Task task, TimeSpan timeSpan,
			DateTime earliestPossibleStartTime) {
		// TODO
		return false;
	}

}
