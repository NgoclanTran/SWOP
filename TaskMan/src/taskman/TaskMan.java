package taskman;

import java.util.ArrayList;

import org.joda.time.DateTime;

import taskman.controller.MainSession;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class TaskMan {

	public static void main(String[] args) {
		try {
			IView cli = new View();
			ProjectHandler ph = new ProjectHandler();
			MainSession controller = new MainSession(cli, ph);
			
			ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
			ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
			ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
			ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null);
			
			controller.run();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
		
	}

}
