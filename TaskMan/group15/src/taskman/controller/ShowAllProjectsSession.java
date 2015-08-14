package taskman.controller;

import java.util.ArrayList;
import java.util.List;

import taskman.controller.branch.ShowProjectsSession;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.project.Project;
import taskman.view.IView;

public class ShowAllProjectsSession extends Session {

	private final Company company;

	public ShowAllProjectsSession(IView cli, Company company)
			throws IllegalArgumentException {
		super(cli);
		if (company == null)
			throw new IllegalArgumentException(
					"The login controller needs a company");
		this.company = company;
	}

	@Override
	public void run() {
		showAllBranchesWithTheirProjects();
	}

	private void showAllBranchesWithTheirProjects() {
		List<BranchOffice> branchOffices = company.getBranchOffices();
		List<List<Project>> allProjects = getAllProjects(branchOffices);

		if (allProjects.size() == 0) {
			getUI().displayError("No projects.");
			return;
		}

		BranchOffice branchOffice;
		try {
			branchOffice = getUI().getShowAllBranchesForm().getBranchOffice(
					branchOffices, allProjects);
		} catch (ShouldExitException e) {
			return;
		}
		
		new ShowProjectsSession(getUI(), branchOffice.getPh()).run();
	}

	private List<List<Project>> getAllProjects(List<BranchOffice> branchOffices) {
		boolean noProjects = true;

		List<List<Project>> allProjects = new ArrayList<>();
		List<Project> projects;

		for (BranchOffice branchOffice : branchOffices) {
			projects = branchOffice.getPh().getProjects();
			allProjects.add(branchOffice.getPh().getProjects());
			if (noProjects && projects.size() > 0)
				noProjects = false;
		}

		if (noProjects)
			return new ArrayList<>();
		else
			return allProjects;
	}

}
