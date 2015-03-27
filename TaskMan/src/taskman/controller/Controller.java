package taskman.controller;

import task.UI.UI;
import taskman.model.facade.ProjectHandler;

public abstract class Controller {

	public Controller(UI cli, ProjectHandler ph) {
		if(cli == null) throw new NullPointerException("The UI is is null.");
		if( ph == null) throw new NullPointerException("The projecthandler is null.");
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
	
	private boolean isValidFacade(IFacade facade) {
		if(facade != null)
			return true;
		else
			return false;
	}
	
	public IFacade getFacade() {
		return facade;
	}
	
	protected IFacade facade;
	
	public abstract void run();

}
