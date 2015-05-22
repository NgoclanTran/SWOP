package taskman.view;

import java.util.List;

import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.project.Project;

public class ShowAllBranchesForm implements IShowAllBranchesForm {
	
	private View view;
	
	/**
	 * The constructor of the show all branches form. It will setup the view to be
	 * able to get input and output.
	 * 
	 * @param view
	 */
	public ShowAllBranchesForm(View view) {
		this.view = view;
	}

	@Override
	public BranchOffice getBranchOffice(List<BranchOffice> branchOffices,
			List<List<Project>> projects) throws ShouldExitException {
		try {
			displayBranchOfficesWithProjects(branchOffices, projects);
			view.output.displayEmptyLine();
			int branchOfficeId = view.getListChoice(branchOffices, "Select a branch office:");
			return branchOffices.get(branchOfficeId - 1);
		} catch (ShouldExitException e) {
			view.output.displayEmptyLine();
			throw new ShouldExitException();
		}
	}
	
	private void displayBranchOfficesWithProjects(List<BranchOffice> branchOffices, List<List<Project>> projects) {
		if (branchOffices.size() != projects.size()) {
			view.displayError("Error occured while creating the project list.");
			throw new ShouldExitException();
		}

		view.displayInfo("0. Return");
		for (int i = 1; i <= projects.size(); i++) {
			if (projects.get(i - 1).size() == 0)
				continue;
			String branchOffice = i + ". "
					+ branchOffices.get(i - 1).getLocation().toString();
			view.displayInfo(branchOffice);
			view.output.displayList(projects.get(i - 1), 1, false);
		}
	}

}
