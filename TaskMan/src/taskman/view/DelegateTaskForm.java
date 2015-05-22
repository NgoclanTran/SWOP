package taskman.view;

public class DelegateTaskForm implements IDelegateTaskForm {

	View view;

	/**
	 * The constructor of the create task form. It will setup the view to be
	 * able to get input and output.
	 * 
	 * @param view
	 */
	public DelegateTaskForm(View view) {
		this.view = view;
	}

}
