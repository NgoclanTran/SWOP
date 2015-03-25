package taskman.model.project;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.task.Task;

class Ongoing implements State {
	
	private final String name = "Ongoing";

	@Override
	public String getName(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTask(Project project, String description, int estimatedDuration,
			int acceptableDeviation, List<Task> dependencies) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProjectState(Project project) {
		// TODO Auto-generated method stub

	}

	@Override
	public DateTime getEstimatedFinishTime(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTotalDelay(Project project) {
		// TODO Auto-generated method stub
		return 0;
	}

}
