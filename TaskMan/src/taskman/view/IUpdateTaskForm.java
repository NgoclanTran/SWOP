package taskman.view;

import java.util.List;

import org.joda.time.DateTime;

import taskman.model.project.Project;
import taskman.model.project.task.Task;

public interface IUpdateTaskForm extends IView {

	public boolean getUpdateTaskFailed();

	public DateTime getUpdateTaskStartTime();

	public DateTime getUpdateTaskStopTime();

	public Project getProjectWithAvailableTasks(List<Project> projects,
			List<List<Task>> availableTasks);

}
