package taskman.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import taskman.controller.project.CreateTaskSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class CreateTaskSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private ResourceHandler rh;
	private CreateTaskSession session;
	private String description;
	private int estimatedDuration ;
	private int acceptableDeviation ;
	private ArrayList<Task> dependencies;
	private Project p;

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Rule
	public final StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Before
	public void setup(){
		//cli = Mockito.mock(View.class);
		cli = new View();
		ph = new ProjectHandler();
		rh = new ResourceHandler();
		session = new CreateTaskSession(cli, ph, rh);
		ph.addProject("Project x", "Test project 1", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.addProject("Project y", "Test project 2", new DateTime(), new DateTime(2016, 4, 1, 0, 0));
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null, null);
		ph.getProjects().get(0).addTask("Task description", 10, 1, new ArrayList<Task>(), null, null);
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 11;
		dependencies = new ArrayList<Task>();
		p = ph.getProjects().get(0);

	}

		@Test 
		public void useCaseTest_NoProject(){
			IView cli = new View();
			ProjectHandler ph = new ProjectHandler();
			ResourceHandler rh = new ResourceHandler();
			CreateTaskSession session = new CreateTaskSession(cli, ph, rh);
	
			session.run();
			String output ="No projects.\r\n\r\n";
			assertEquals(output,log.getLog());
		}
	
		@Test
		public void useCaseTest_WithProjects_WrongInput_Exit_CancelCreation(){
	
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("-1\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	
		}
	
		@Test
		public void useCaseTest_WithProjects_InputExit_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	
		}
	
		@Test
		public void useCaseTest_WithProjects_InputOutOfBound_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("3\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	
		}
	
		@Test
		public void useCaseTest_NoDescription_Stop_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("1\ncancel\n");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	
		}
		@Test
		public void useCaseTest_Description_Stop_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("1\ndescription\ncancel\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
	
		}
	
		@Test
		public void useCaseTest_EnterEstimatedDurationUntilCorrectInput_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("1\ndescription\n-1\n0\n10\ncancel\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
		}
	
		@Test
		public void useCaseTest_EnterAcceptableDeviationUntilCorrectInput_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			systemInMock.provideText("1\ndescription\n10\n-1\na\n10\ncancel\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
		}
	
		@Test
		public void useCaseTest_InputDependenciesYes_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			// select project 1
			// enter description "description"
			// enter estimated duration: 10
			// enter acceptable devation: 10
			// enter wrong input for dependencies: a
			// enter task has dependency: Y
			// select first task in given list
			// cancel creation
	
			systemInMock.provideText("1\ndescription\n10\n10\na\nY\n1\ncancel\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
//			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
			Task lt = ph.getProjects().get(0).getTasks().get(ph.getProjects().get(0).getTasks().size()-1);
			assertEquals(lt.getDescription(), "description");
			assertEquals(lt.getEstimatedDuration(),10);
			assertEquals(lt.getAcceptableDeviation(),10);
			assertEquals(lt.getDependencies().size(),1);
			assertNull(lt.getAlternative());
		}
	
		@Test
		public void useCaseTest_InputDependenciesNo_CancelCreation(){
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			// select project 1
			// enter description "description"
			// enter estimated duration: 10
			// enter acceptable devation: 10
			// enter wrong input for dependencies: a
			// enter task has dependency: N
			// cancel creation
	
			systemInMock.provideText("1\ndescription\n10\n10\na\ncancel\n0");
			session.run();
	
			//-------- Check if nothing has been changed -------
			assertEquals(ph.getProjects(), projects);
			assertEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
		}
	
	
		@Test
		public void useCaseTest_InputAlterativeForNo_CancelCreation(){
	
			//---------- Before running ----------
			ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
			ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
			ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();
	
			// select project 1
			// enter description "description"
			// enter estimated duration: 10
			// enter acceptable devation: 10
			// enter task has dependency: N
			// cancel creation
	
			systemInMock.provideText("1\ndescription\n10\n10\nN\nN\ncancel\n0");
			session.run();
	
			//-------- Check if task was created -------
			assertEquals(ph.getProjects(), projects);
			Task lastAddedTask = ph.getProjects().get(0).getTasks().get(tasksOfProject1.size());
			assertEquals(lastAddedTask.getDescription(),"description");
			assertNull(lastAddedTask.getAlternative());
			assertEquals(lastAddedTask.getEstimatedDuration(),10);
			assertEquals(lastAddedTask.getAcceptableDeviation(),10);
			assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);		
		}


	@Test
	public void useCaseTest_SuccesScenario_WithoutDependencies_WithoutAlternative(){
		//---------- Before running ----------
		ArrayList<Project> projects = (ArrayList<Project>) ph.getProjects();
		ArrayList<Task> tasksOfProject1 = (ArrayList<Task>) projects.get(0).getTasks();
		ArrayList<Task> tasksOfProject2 = (ArrayList<Task>) projects.get(1).getTasks();

		// select project 1
		// enter description "description"
		// enter estimated duration: 10
		// enter acceptable devation: 10
		// enter wrong input for dependencies: a
		// enter task has dependency: Y
		// select first task in given list
		// cancel creation

		systemInMock.provideText("1\ndescription\n10\n10\na\nY\n1\nN\ncancel\n0");
		session.run();

		//-------- Check if nothing has been changed -------
		assertEquals(ph.getProjects(), projects);
		// New task was created
		assertNotEquals(ph.getProjects().get(0).getTasks(), tasksOfProject1);
		assertEquals(ph.getProjects().get(1).getTasks(), tasksOfProject2);
		
		Task newTask = ph.getProjects().get(0).getTasks().get(tasksOfProject1.size());
		assertEquals(newTask.getDescription(), "description");
		assertEquals(newTask.getAcceptableDeviation(),10);
		assertEquals(newTask.getEstimatedDuration(),10);
		assertEquals(newTask.getDependencies().get(0),tasksOfProject1.get(0));
	}
}
