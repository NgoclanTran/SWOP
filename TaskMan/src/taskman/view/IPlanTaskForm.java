package taskman.view;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.TimeSpan;

public interface IPlanTaskForm {

	public Project getProjectWithUnplannedTasks(List<Project> projects,
			List<List<Task>> unplannedTasks);

	public DateTime getStartTime(Set<DateTime> startTimes);

	public List<Resource> getResources(TimeSpan timeSpan,
			List<ResourceType> resourceTypes, List<Integer> amounts,
			List<List<Resource>> suggestedResources);

}
