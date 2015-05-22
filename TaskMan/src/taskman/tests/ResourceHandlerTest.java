package taskman.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.model.company.ResourceHandler;
import taskman.model.resource.ResourceType;

public class ResourceHandlerTest {

	private ResourceHandler r;
	@Before
	public void setUp() throws Exception {
		r = new ResourceHandler();
	}

	@Test
	public void test() {
		ResourceType rt = new ResourceType("name", null,null, false);
		r.addResourceType("name", null, null, false);
		assertEquals(r.getResourceTypes().get(0).getName(),"name");
		assertEquals(r.getResourceTypes().get(0).getConflictsWith().size(), 0);
		assertEquals(r.getResourceTypes().get(0).getRequires().size(),0);
	}

}
