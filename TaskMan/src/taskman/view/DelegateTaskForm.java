package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;

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

	@Override
	public BranchOffice getBranchOffice(List<BranchOffice> branchOffices)
			throws ShouldExitException {
		try {
			displayBranchOffices(branchOffices);
			view.output.displayEmptyLine();
			int branchOfficeId = view.getListChoice(branchOffices, "Select a branch office:");
			return branchOffices.get(branchOfficeId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}
	
	private void displayBranchOffices(List<BranchOffice> branchOffices) {
		if (branchOffices.size() == 0) {
			view.displayError("Error occured while creating the branch offices list.");
			throw new ShouldExitException();
		}
		
		view.output.displayList(branchOffices, 0, false);
	}

}
