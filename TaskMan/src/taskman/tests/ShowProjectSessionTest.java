package taskman.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import taskman.controller.project.ShowProjectSession;
import taskman.model.facade.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.view.IView;
import taskman.view.View;

public class ShowProjectSessionTest {
	private IView cli;
	private ProjectHandler ph;
	private ShowProjectSession session;
	private String description;
	private int estimatedDuration ;
	private int acceptableDeviation ;
	private ArrayList<Task> dependencies;
	private Project p;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	// This use case cannot be tested. It's working with output, but the outputs buffers are flushed everytime.
	
	@Before
	public void setUp() throws Exception {
		cli = Mockito.mock(View.class);
		ph = new ProjectHandler();
		session = new ShowProjectSession(cli, ph);
		description = "description";
		estimatedDuration = 10;
		acceptableDeviation = 11;
		dependencies = new ArrayList<Task>();
		System.setOut(new PrintStream(outContent));
	}

	@Test
	public void showProjectTest_NoProjects(){
		session.run();
		assertEquals(outContent.toString(),"Select an option:");
	}
	
	@Test
	public void showProjectTest_OngoingProjects(){
//		System.out.print("test");
//		assertEquals(outContent.toString(),"test");
	}
	
	@Test 
	public void showProjectTest_DifferentTasks(){
		
	}

	
	
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}

}
