package taskman.controller;

import taskman.model.company.Company;
import taskman.view.IView;

public abstract class AbstractCompanySession extends Session {

	/**
	 * Constructor of the abstract company session.
	 * 
	 * @param cli
	 * @param company
	 * 
	 * @throws IllegalArgumentException
	 *             The given company needs to be valid.
	 */
	public AbstractCompanySession(IView cli, Company company)
			throws IllegalArgumentException {
		super(cli);
		if (!isValidCompany(company))
			throw new IllegalArgumentException(
					"The main controller needs a company");
		this.company = company;
	}

	/**
	 * Checks if the given company is valid.
	 * 
	 * @param company
	 * 
	 * @return Returns true if the company is different from null.
	 */
	private boolean isValidCompany(Company company) {
		if (company != null)
			return true;
		else
			return false;
	}

	/**
	 * Get the company.
	 * 
	 * @return Returns the company.
	 */
	public Company getCompany() {
		return company;
	}

	private Company company;

}
