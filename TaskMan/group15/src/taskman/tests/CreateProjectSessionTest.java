package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.branch.CreateProjectSession;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.view.IView;
import taskman.view.View;

public class CreateProjectSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private ResourceHandler rh;
	private CreateProjectSession session;
	private String description;
	private ArrayList<Task> dependencies;
	private Project p;
	private TaskFactory tf;
	private Clock clock;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();
	private Company company = new Company();
	List<ResourceType> list = new ArrayList<ResourceType>();
	private BranchOffice bo = new BranchOffice(company, "", list);

	@Before
	public void setup() {
		clock = new Clock();
		tf = new TaskFactory(bo, clock);
		cli = new View();
		ph = new ProjectHandler(tf);
		rh = new ResourceHandler(list);
		session = new CreateProjectSession(cli, ph, clock);

	}
	@Test(expected = IllegalArgumentException.class)
	public void phNullTest(){
		CreateProjectSession session1 = new CreateProjectSession(cli, null, clock);
	}
	@Test(expected = IllegalArgumentException.class)
	public void clockNullTest(){
		CreateProjectSession session1 = new CreateProjectSession(cli, ph, null);
	}
	@Test
	public void useCaseTest_Name_Cancelcreation() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock.provideText("cancel");
		session.run();
		assertEquals(ph.getProjects(), projects);
		String lastMessage = log.getLog().substring(0, 42);
		assertEquals("Enter the name of the project (or cancel):", lastMessage);

	}

	@Test
	public void useCaseTest_Description_Cancelcreation() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock.provideText("name\ncancel");
		session.run();

		// --------- Check if nothing has changed or created --------------
		assertEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\n",
		// log.getLog());

	}

	@Test
	public void useCaseTest_DueTime_CancelCreation() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock
				.provideText("name\n21-04-201511:11\n21-04-100 10:10\ncancel");
		session.run();

		// --------- Check if nothing has changed or created --------------
		assertEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nDue time has to be after creation time.\r\n\r\nEnter the name of the project (or cancel):\r\n\r\n\r\n",
		// log.getLog());
	}

	@Test
	public void useCaseTest_SuccesScenario() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock.provideText("name\ndescription\n21-04-2016 12:10\ncancel");
		session.run();

		// --------- Check if project is created --------------

		assertNotEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nProject created\r\n\r\n",
		// log.getLog());

		Project p = (ph.getProjects().get(ph.getProjects().size() - 1)); // Last
																			// created
																			// project
		assertEquals(p.getName(), "name");
		assertEquals(p.getDescription(), "description");
		assertEquals(p.getDueTime().getDayOfMonth(), 21);
		assertEquals(p.getDueTime().getMonthOfYear(), 04);
		assertEquals(p.getDueTime().getYear(), 2016);
		assertEquals(p.getDueTime().getMinuteOfDay(), 730);

	}

	@Test
	public void useCaseTest_DueTime_WrongInputLoopTest() {
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock
				.provideText("name\ndescription\nfoutedatum\n21-04-2016 12:10\ncancel");
		session.run();

		// --------- Check if project is created --------------

		assertNotEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nProject created\r\n\r\n",
		// log.getLog());

		Project p = (ph.getProjects().get(ph.getProjects().size() - 1)); // Last
																			// created
																			// project
		assertEquals(p.getName(), "name");
		assertEquals(p.getDescription(), "description");
		assertEquals(p.getDueTime().getDayOfMonth(), 21);
		assertEquals(p.getDueTime().getMonthOfYear(), 04);
		assertEquals(p.getDueTime().getYear(), 2016);
		assertEquals(p.getDueTime().getMinuteOfDay(), 730);
	}

	@Test
	public void useCaseTest_SuccesScenario_BlankName() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock.provideText("\ndescription\n21-04-2016 12:10\ncancel");
		session.run();

		// --------- Check if project is created --------------

		assertNotEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nProject created\r\n\r\n",
		// log.getLog());

		Project p = (ph.getProjects().get(ph.getProjects().size() - 1)); // Last
																			// created
																			// project
		assertEquals(p.getName(), "");
		assertEquals(p.getDescription(), "description");
		assertEquals(p.getDueTime().getDayOfMonth(), 21);
		assertEquals(p.getDueTime().getMonthOfYear(), 04);
		assertEquals(p.getDueTime().getYear(), 2016);
		assertEquals(p.getDueTime().getMinuteOfDay(), 730);

	}

	@Test
	public void useCaseTest_SuccesScenario_BlankDescription() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock.provideText("name\n\n21-04-2016 12:10\ncancel");
		session.run();

		// --------- Check if project is created --------------

		assertNotEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nProject created\r\n\r\n",
		// log.getLog());

		Project p = (ph.getProjects().get(ph.getProjects().size() - 1)); // Last
																			// created
																			// project
		assertEquals(p.getName(), "name");
		assertEquals(p.getDescription(), "");
		assertEquals(p.getDueTime().getDayOfMonth(), 21);
		assertEquals(p.getDueTime().getMonthOfYear(), 04);
		assertEquals(p.getDueTime().getYear(), 2016);
		assertEquals(p.getDueTime().getMinuteOfDay(), 730);

	}

	@Test
	public void useCaseTest_SuccesScenario_BlankDueTimeRepeatUntilProperInput() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock
				.provideText("name\ndescription\n\n\n21-04-2016 12:10\ncancel");
		session.run();

		// --------- Check if project is created --------------

		assertNotEquals(ph.getProjects(), projects);
		// assertEquals("Enter the name of the project (or cancel):\r\n\r\n\r\nEnter the description of the project (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nEnter the due time of the project with format dd-MM-yyyy HH:mm (or cancel):\r\n\r\n\r\nProject created\r\n\r\n",
		// log.getLog());

		Project p = (ph.getProjects().get(ph.getProjects().size() - 1)); // Last
																			// created
																			// project
		assertEquals(p.getName(), "name");
		assertEquals(p.getDescription(), "description");
		assertEquals(p.getDueTime().getDayOfMonth(), 21);
		assertEquals(p.getDueTime().getMonthOfYear(), 04);
		assertEquals(p.getDueTime().getYear(), 2016);
		assertEquals(p.getDueTime().getMinuteOfDay(), 730);

	}

	@Test
	public void useCaseTest_DueTimeBeforeStartTime_NoCreation() {
		// -------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();

		systemInMock.provideText("name\ndescription\n20-10-100 12:10\ncancel");
		session.run();

		// --------- Check if nothing has changed or created --------------
		assertEquals(ph.getProjects(), projects);

	}

}
