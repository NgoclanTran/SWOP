package taskman.controller.project;

import java.io.IOException;
import java.text.ParseException;

import org.joda.time.DateTime;

import taskman.UI.UI;
import taskman.controller.Session;
import taskman.model.facade.ProjectHandler;

public class CreateProjectSession extends Session{
	/**
	 * Creates the create project session using the given UI and ProjectHandler.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * 
	 * @throws IllegalArgumentException
	 */
	public CreateProjectSession(UI cli, ProjectHandler ph){
		super(cli, ph);
	}

	@Override
	/**
	 * starts the use case by calling the create project method
	 */
	public void run() {
		createProject();
	}
	/**
	 * This is the main method for the use case, it will ask for user input for the details of the project
	 * 
	 */
	private void createProject(){
		
		String name = cli.getTextInput("Enter the name of the project: ");
		String description = cli.getTextInput("Enter the description of the project: ");
		DateTime creationTime = new DateTime();
		DateTime dueTime = null;
		try{
			dueTime = cli.getDateTimeInput("Enter the due time of the project with format yyyy-MM-dd HH-mm: ");
		}
		catch(Exception e){
			createProject();
		}
		String text = getUI().getTextInput("Confirm by typing ok, to create project: ");
		if(!text.equalsIgnoreCase("ok")){
			getUI().display("Stop system");
		
			return;
		
		}
		
		try{
			super.getPH().addProject(name, description, creationTime, dueTime);
			getUI().display("Project created.");
		}catch(Exception e){
			getUI().display(e.getMessage());
			createProject();
		}
		
		
		
		
	}

}
