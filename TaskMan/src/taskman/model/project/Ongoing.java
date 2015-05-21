package taskman.model.project;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;

class Ongoing implements State {

	private final String name = "ONGOING";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void addTask(Project project, String description,
			int estimatedDuration, int acceptableDeviation,
			List<NormalTask> dependencies, NormalTask alternativeFor,
			Map<ResourceType, Integer> resourceTypes)
			throws IllegalStateException, IllegalArgumentException {
		project.performAddTask(description, estimatedDuration,
				acceptableDeviation, dependencies, alternativeFor,
				resourceTypes);
	}

	@Override
	public void updateProjectState(Project project)
			throws IllegalStateException {
		project.performUpdateProjectState();
	}

	@Override
	public DateTime getEstimatedFinishTime(Project project)
			throws IllegalStateException {
		return project.performGetEstimatedFinishTime();
	}

	@Override
	public int getTotalDelay(Project project) throws IllegalStateException {
		throw new IllegalStateException("Project not finished.");
	}

}
