package taskman.controller.project;

import java.util.ArrayList;
import java.util.List;

import taskman.UI.UI;
import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;

public class CreateTaskSession extends Session{
	/**
	 * Creates the create task session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public CreateTaskSession(UI cli, ProjectHandler ph) {
		super(cli, ph);
	}
	/**
	 * starts the use case by calling the create task method
	 */
	public void run() {
		createTask();
	}
	/**
	 * This method will ask the ui to get user input to create the task with this given input
	 * This is the main method for the execution of this particular use case
	 */
	private void createTask(){	
		Project project = this.getProject();
		String description = super.getUI().getTextInput("Enter the description of the task: ");
		int estimatedDuration = super.getUI().getNumberInput("Enter the estimated duration of the task: ");
		int acceptableDeviation = super.getUI().getNumberInput("Enter an acceptable deviation of the task: ");
		String hasDependencies = getUI().getTextInput("Does task have dependencies? Enter yes or no");
		ArrayList<Task> dependencies;
		if(hasDependencies.equalsIgnoreCase("yes")){
			dependencies = getDependencies(project);
		}else{
			dependencies = new ArrayList<Task>();
		}
		
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
			getUI().display("Task created.");
		}
		catch(Exception e){
			getUI().display(e.getMessage());
			createTask();
		}
		
		
		
	}
	/**
	 * This method will ask the ui to display a list of projects and lets the user choose one of them
	 * @return
	 * 			The project as chosen by the user in the UI
	 */
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
	/**
	 * This method will ask the UI to display a list of the task of the project followed by a user input to chose one of these
	 * to act as a depency for the task that is being created
	 * @param project
	 * 			The project of which the tasks will be displayed, previously chosen by the user in the main method for the use case
	 * @return
	 * 			The list of dependencies as chosen by the user
	 */
	private ArrayList<Task> getDependencies(Project project){
		List<Task> tasks = project.getTasks();
		ArrayList<Task> dependencies = new ArrayList<Task>();
		getUI().displayTaskList(tasks, 1);
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
