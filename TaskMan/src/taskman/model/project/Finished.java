package taskman.model.project;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;

public class Finished implements State {
	
	private final String name = "Finished";

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation) {
		throw new IllegalStateException("Project already finished.");

	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		throw new IllegalStateException("Project already finished.");
	}

	@Override
	public void updateProjectState(Project project) {
		throw new IllegalStateException("Project already finished.");
	}

	@Override
	public DateTime getEstimatedFinishTime(Project project) {
		throw new IllegalStateException("Project already finished.");
	}

	@Override
	public int getTotalDelay(Project project) {
		return project.getTotalDelay();
	}

}
