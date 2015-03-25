package taskman.model.project;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;

class Ongoing implements State {
	
	private final String name = "Ongoing";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation) {
		project.addTask(description, estimatedDuration, acceptableDeviation);
	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		project.addTask(description, estimatedDuration, acceptableDeviation, dependencies);
	}

	@Override
	public void updateProjectState(Project project) {
		project.updateProjectState();
	}

	@Override
	public DateTime getEstimatedFinishTime(Project project) {
		return project.getEstimatedFinishTime();
	}

	@Override
	public int getTotalDelay(Project project) {
		throw new IllegalStateException("Project not finished.");
	}

}
