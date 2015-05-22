package taskman.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.time.Clock;

public class BranchOfficeTest {

	private Company company;
	BranchOffice b;
	@Before
	public void setup(){
		b = new BranchOffice(company, "", null);
	}
	
	@Test
	public void toStringTest(){
		assertEquals(b.toString(), "");
	}
	@Test
	public void getCompanyTest(){
		assertEquals(b.getCompany(), company);
	}
	@Test
	public void getLocationTest(){
		assertEquals(b.getLocation(), "");
	}
}
