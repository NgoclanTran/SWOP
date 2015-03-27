package taskman.controller;

import task.UI.UI;
import taskman.model.facade.ProjectHandler;

public class Controller {

	public Controller(UI cli, ProjectHandler ph) {
		if(!isValidUI(cli)) 
			throw new IllegalArgumentException("The controller needs a UI.");
		if(!isValidProjectHandler(ph))
			throw new IllegalArgumentException("The controller needs a ProjectHandler");
		this.cli = cli;
		this.ph = ph;
	}
	
	private boolean isValidUI(UI cli) {
		if(cli != null)
			return true;
		else
			return false;
	}
	
	public UI getUI() {
		return cli;
	}
	
	protected UI cli;
	
	private boolean isValidProjectHandler(ProjectHandler ph) {
		if(ph != null)
			return true;
		else
			return false;
	}
	
	public ProjectHandler getFacade() {
		return ph;
	}
	
	protected ProjectHandler ph;
	
	public void run() {
		// TODO
	}

}
