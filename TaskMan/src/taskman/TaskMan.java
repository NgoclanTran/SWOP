package taskman;

import java.util.ArrayList;

import org.joda.time.DateTime;

import taskman.UI.UI;
import taskman.controller.MainSession;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.task.Task;

public class TaskMan {

	public static void main(String[] args) {
		UI cli = new UI();
		ProjectHandler ph = new ProjectHandler();
		MainSession controller = new MainSession(cli, ph);
		
//		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
//		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
//		
//		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
//		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
		
		controller.run();
	}

}
