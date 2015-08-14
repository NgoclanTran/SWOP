package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_NullResourceTypes(){
		new ResourceHandler(null);
	}

	@Test
	public void test() {

		assertEquals(r.getResourceTypes().get(0).getName(),"name");
		assertEquals(r.getResourceTypes().get(0).getConflictsWith().size(), 0);
		assertEquals(r.getResourceTypes().get(0).getRequires().size(),0);
	}
	
	@Test
	public void getResourceTest(){
		List<ResourceType> list = r.getResourceTypes();
		ResourceType rt = new ResourceType("name", null,null, false);
		list.add(rt);
		
		assertNotEquals(r.getResourceTypes(), list);
	}

}
