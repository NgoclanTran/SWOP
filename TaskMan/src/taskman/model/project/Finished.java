package taskman.model.project;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;

public class Finished implements State {

	private final String name = "FINISHED";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void addTask(Project project, String description,
			int estimatedDuration, int acceptableDeviation,
			List<NormalTask> dependencies, NormalTask alternativeFor,
			Map<ResourceType, Integer> resourceTypes, int developerAmount)
			throws IllegalStateException {
		throw new IllegalStateException("Project already finished.");
	}

	@Override
	public void updateProjectState(Project project)
			throws IllegalStateException {
		throw new IllegalStateException("Project already finished.");
	}

	@Override
	public DateTime getEstimatedFinishTime(Project project)
			throws IllegalStateException {
		throw new IllegalStateException("Project already finished.");
	}

	@Override
	public int getTotalDelay(Project project) throws IllegalStateException {
		return project.performGetTotalDelay();
	}

}
