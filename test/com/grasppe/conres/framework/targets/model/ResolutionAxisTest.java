/**
 * 
 */
package com.grasppe.conres.framework.targets.model;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.grasppe.conres.framework.targets.model.axis.ResolutionAxis;
import com.grasppe.conres.framework.units.ResolutionValue;

/**
 * @author daflair
 *
 */
public class ResolutionAxisTest {
	
	double[] steps = new double[]{0, 10, 20, 30, 40, 50, 60};

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		int nSteps = steps.length;
		
		ResolutionAxis axis = new ResolutionAxis(steps);
		ResolutionValue[] values = (ResolutionValue[]) axis.getValues();
		
		System.out.println(axis.getValues().toString());
		
        for (int i = 0; i < nSteps; i++) {
        	Assert.assertEquals(steps[i], values[i].getValue());
        	System.out.println("step["+i+"] = " + steps[i] + "\t== axis.values["+i+"].value = " + values[i].getValue());
        }
		
		//fail("Not yet implemented"); // TODO
	}

}
