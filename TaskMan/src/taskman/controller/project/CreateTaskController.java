package taskman.controller.project;

import java.util.ArrayList;
import java.util.List;

import taskman.UI.UI;
import taskman.controller.MainSession;
import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class CreateTaskController extends Session{

	public CreateTaskController(UI cli, ProjectHandler ph) {
		super(cli, ph);
	}
	
	public void run() {
		createTask();
	}
	
	private void createTask(){	
		Project project = this.getProject();
		String description = super.getUI().getTextInput("Enter the description of the task: ");
		int estimatedDuration = super.getUI().getNumberInput("Enter the estimated duration of the task: ");
		int acceptableDeviation = super.getUI().getNumberInput("Enter an acceptable deviation of the task: ");
		ArrayList<Task> dependencies = getDependencies(project);
		Task alternativeFor;
		
		
		try{
			int alternativeIndex = this.getListChoice(project.getTasks(), "Select alternative: ");
			alternativeFor = project.getTasks().get(alternativeIndex - 1);
		}catch (ShouldExitException e){
			alternativeFor = null;
		}
		String text = getUI().getTextInput("Confirm by typing ok, to create task: ");
		if(!text.equalsIgnoreCase("ok")){
			getUI().display("Stop system");
		
			return;
		}
		try{
			project.addTask(description, estimatedDuration, acceptableDeviation, dependencies, alternativeFor);
		}
		catch(Exception e){
			createTask();
		}
		
		
		
	}
	
	private Project getProject(){
		List<Project> projects = getPH().getProjects();
		getUI().displayProjectList(projects);

		int projectId;
		try {
			projectId = getListChoice(projects,
					"Select a project: ");
			return projects.get(projectId-1);
		} catch (ShouldExitException e) {
			return null;
		}
	}
	
	private ArrayList<Task> getDependencies(Project project){
		List<Task> tasks = project.getTasks();
		ArrayList<Task> dependencies = new ArrayList<Task>();
		getUI().displayTaskList(tasks);
		int taskId = 0;
			try {
				taskId = getListChoice(tasks,"Select a task: ");
				dependencies.add(tasks.get(taskId-1));
			} catch (ShouldExitException e) {
				return dependencies;
			}
			return dependencies;
		
		
	}

}
