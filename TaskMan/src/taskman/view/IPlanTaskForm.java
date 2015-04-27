package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.Project;
import taskman.model.project.task.Task;

public interface IPlanTaskForm {
	
	public Project getProjectWithUnplannedTasks(List<Project> projects,
			List<List<Task>> unplannedTasks);
	
	public DateTime getStartTime(List<DateTime> startTimes);

}
