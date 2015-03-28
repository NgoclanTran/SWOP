package taskman.controller.project;

import java.util.List;

import taskman.UI.UI;
import taskman.controller.Controller;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class ShowProjectController extends Controller {

	public ShowProjectController(UI cli, ProjectHandler ph) {
		super(cli, ph);
	}
	
	@Override
	public void run() {
		showProjects();
	}
	
	private void showProjects() {
		List<Project> projects = getPH().getProjects();
		getUI().displayProjectList(projects);
		
		int projectId;	
		try {
			projectId = getChoiceForList(projects, "Select a project to view the details: ");
		}
		catch (ShouldExitException e) {
			return;
		}
		
		showProjectDetails(projects.get(projectId - 1));	
	}
	
	private void showProjectDetails(Project project) {
		List<Task> tasks = project.getTasks();
		getUI().displayProjectDetails(project, tasks);
		
		int taskId;	
		try {
			taskId = getChoiceForList(tasks, "Select a task to view the details: ");
		}
		catch (ShouldExitException e) {
			return;
		}
		
		getUI().display("Selected task " + taskId);
	}

}
