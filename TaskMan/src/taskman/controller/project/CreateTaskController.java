package taskman.controller.project;

import taskman.controller.MainSession;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;

public class CreateTaskController{

	public CreateTaskController(ProjectHandler ph) {
		this.ph = ph;
	}
	private ProjectHandler ph;
	
	public void run() {
		
	}
	
	private void createTask(String description, int estimatedDuration, int acceptableDeviation, Project project){	
		project.addTask(description, estimatedDuration, acceptableDeviation);
	}

}
