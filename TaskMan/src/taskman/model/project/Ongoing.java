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
			int acceptableDeviation) throws IllegalStateException {
		project.performAddTask(description, estimatedDuration, acceptableDeviation);
	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) throws IllegalStateException {
		project.performAddTask(description, estimatedDuration, acceptableDeviation, dependencies);
	}

	@Override
	public void updateProjectState(Project project) throws IllegalStateException {
		project.performUpdateProjectState();
	}

	@Override
	public DateTime getEstimatedFinishTime(Project project) throws IllegalStateException {
		return project.performGetEstimatedFinishTime();
	}

	@Override
	public int getTotalDelay(Project project) throws IllegalStateException {
		throw new IllegalStateException("Project not finished.");
	}

}
