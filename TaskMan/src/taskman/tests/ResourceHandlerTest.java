package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import taskman.model.company.ResourceHandler;
import taskman.model.resource.ResourceType;

public class ResourceHandlerTest {

	private ResourceHandler r;
	@Before
	public void setUp() throws Exception {
		ResourceType rt = new ResourceType("name", null,null, false);
		ArrayList<ResourceType> rl = new ArrayList<ResourceType>();
		rl.add(rt);
		r = new ResourceHandler(rl);
	}

	@Test
	public void test() {

		assertEquals(r.getResourceTypes().get(0).getName(),"name");
		assertEquals(r.getResourceTypes().get(0).getConflictsWith().size(), 0);
		assertEquals(r.getResourceTypes().get(0).getRequires().size(),0);
	}

}
