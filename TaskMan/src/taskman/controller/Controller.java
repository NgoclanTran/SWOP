package taskman.controller;

import task.UI.UI;
import taskman.model.facade.ProjectHandler;

public class Controller {

	public Controller(UI cli, ProjectHandler ph) {
		if(cli == null) throw new NullPointerException("The UI is is null.");
		if( ph == null) throw new NullPointerException("The projecthandler is null.");
		this.cli = cli;
		this.ph = ph;
	}
	
	protected UI cli;
	protected ProjectHandler ph;
	
	public void run() {
	}

}
