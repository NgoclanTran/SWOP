package taskman;

import taskman.controller.LoginSession;
import taskman.model.company.Company;
import taskman.view.View;

public class TaskMan {

	public static void main(String[] args) {
		try {
			Company company = new Company();
			Parser parser = new Parser(company);
			parser.parse();
			LoginSession controller = new LoginSession(new View(), company);
			controller.run();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

}
