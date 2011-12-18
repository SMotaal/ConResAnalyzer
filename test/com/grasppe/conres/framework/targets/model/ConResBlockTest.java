/**
 */
package com.grasppe.conres.framework.targets.model;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;

/**
 * @author daflair
 */
public class ConResBlockTest {
	
	protected double[] xDoubleValues = new double[]{100, 59.9, 35.9, 21.5, 12.9, 7.7, 4.6, 2.8, 1.7, 1};
	protected double[] yDoubleValues = new double[]{0.63, 0.81, 1.04, 1.35, 1.74, 2.24, 2.91, 3.76, 4.85, 6.25};
	protected double toneDoubleValue = 10;

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

	/**
	 * Test method for {@link com.grasppe.conres.framework.targets.model.grid.ConResBlock#ConResBlock(com.grasppe.conres.framework.units.ContrastValue[], com.grasppe.conres.framework.units.ResolutionValue[])}.
	 */
	@Test
	public final void testConResBlock() {
		System.out.println(Thread.currentThread().getStackTrace()[1].toString());
		ConResBlock testBlock  = new ConResBlock(toneDoubleValue, xDoubleValues, yDoubleValues);
		
        int					xSteps     = xDoubleValues.length;
        int					ySteps     = xDoubleValues.length;

        for (int xi = 0; xi < xSteps; xi++) {
        	Assert.assertEquals(xDoubleValues[xi], testBlock.getXValue(xi).getValue());
        	System.out.println("x["+xi+"] = " + xDoubleValues[xi] + "\t== getXValue["+xi+"].getValue = " + testBlock.getXValue(xi).toString());
        }
        for (int yi = 0; yi < ySteps; yi++) {
        	Assert.assertEquals(yDoubleValues[yi], testBlock.getYValue(yi).getValue());
        	System.out.println("y["+yi+"] = " + yDoubleValues[yi] + "\t== getYValue["+yi+"].getValue = " + testBlock.getYValue(yi).toString());
        }
        
        System.out.println(testBlock);
	}

	/**
	 * Test method for {@link com.grasppe.conres.framework.targets.model.grid.ConResBlock#buildBlock(double[], double[])}.
	 */
	@Test
	public final void testBuildGridBlock() {
		System.out.println(Thread.currentThread().getStackTrace()[1].toString());
		
		ConResBlock testBlock = ConResBlock.buildBlock(toneDoubleValue, xDoubleValues, yDoubleValues);

        int					xSteps     = xDoubleValues.length;
        int					ySteps     = xDoubleValues.length;

        for (int xi = 0; xi < xSteps; xi++) {
        	Assert.assertEquals(xDoubleValues[xi], testBlock.getXValue(xi).getValue());
        	System.out.println("x["+xi+"] = " + xDoubleValues[xi] + "\t== getXValue["+xi+"].getValue = " + testBlock.getXValue(xi).toString());
        }
        for (int yi = 0; yi < ySteps; yi++) {
        	Assert.assertEquals(yDoubleValues[yi], testBlock.getYValue(yi).getValue());
        	System.out.println("y["+yi+"] = " + yDoubleValues[yi] + "\t== getYValue["+yi+"].getValue = " + testBlock.getYValue(yi).toString());
        }
        
        System.out.println(testBlock);
	}

	/**
	 * Test method for {@link com.grasppe.conres.framework.targets.model.grid.GridBlock#getPatch(int, int)}.
	 */
	@Test
	public final void testGetPatch() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.grasppe.conres.framework.targets.model.grid.GridBlock#getXValue(int)}.
	 */
	@Test
	public final void testGetXValue() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.grasppe.conres.framework.targets.model.grid.GridBlock#getYValue(int)}.
	 */
	@Test
	public final void testGetYValue() {
		fail("Not yet implemented");
	}

}
