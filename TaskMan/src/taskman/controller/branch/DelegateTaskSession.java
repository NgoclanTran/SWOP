package taskman.controller.branch;

import taskman.controller.Session;
import taskman.view.IView;

public class DelegateTaskSession extends Session {

	public DelegateTaskSession(IView cli) throws IllegalArgumentException {
		super(cli);
	}

	@Override
	public void run() {
		delegateTask();
	}

	private void delegateTask() {

	}

}
