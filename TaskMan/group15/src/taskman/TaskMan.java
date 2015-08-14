package taskman;

import taskman.controller.LoginSession;
import taskman.model.company.Company;
import taskman.view.View;
import taskman.view.commandline.Input;

public class TaskMan {

	public static void main(String[] args) {
		try {
			Company company = new Company();
			View view = new View();
			Input input = view.getInput();
			Parser parser = new Parser(company, input);
			parser.parse();
			LoginSession controller = new LoginSession(view, company);
			controller.run();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

}
