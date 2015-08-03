package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.TaskMan;
import taskman.controller.branch.PlanTaskSession;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.company.UserHandler;
import taskman.model.resource.ResourceType;
import taskman.model.task.NormalTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class PlanTaskSessionTest {

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();



	@Test
	public void useCaseTest_Succes() {

		//Deze test plan de tweede taak van project Y
		// - plan task
		// - show task
		systemInMock.provideText("1\n1\n1\n4\n2\n2\n3\n1\ny\n1\nN\n1\n1\n2\n2\n8\n2\n");
		TaskMan.main(null);
		


		// Check of de taak gepland is
		assertTrue( log.getLog().contains("Task planned."));

		// Check of de status van die taak PLANNED is
		assertTrue(log.getLog().contains("Description: yet another task description\n\tStatus: PLANNED"));
		//TODO nog de tijd checken
	}

	@Test
	public void useCaseTest_Succes_Input_StartTime(){
		//Deze test plan de tweede taak van project Y
		// - plan task
		// - show task
		systemInMock.provideText("1\n1\n1\n4\n2\n1\n4\n02-04-2014 09:00\n5\nN\n1\n1\n2\n1\n8\n2\n");
		TaskMan.main(null);


		// Check of de taak gepland is
		assertTrue( log.getLog().contains("Task planned."));

		// Check of de status van die taak PLANNED is
		assertTrue(log.getLog().contains("Description: another task description\n\tStatus: PLANNED"));
		//TODO nog de tijd checken
	}

// TODO test conflic resource
// TODO test conflict developper

}
