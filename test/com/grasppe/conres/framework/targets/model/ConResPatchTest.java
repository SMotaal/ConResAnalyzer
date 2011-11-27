/*
 * @(#)ConResPatchTest.java   11/11/23
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets.model;

import junit.framework.Assert;

import com.grasppe.conres.framework.targets.model.grid.ConResPatch;
import com.grasppe.conres.framework.units.ContrastValue;
import com.grasppe.conres.framework.units.ResolutionValue;
import com.grasppe.morie.units.AbstractValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author daflair
 *
 */
public class ConResPatchTest {

    protected ConResPatch	testPatch;
    protected int			row    = 10,
							column = 11;
    ContrastValue			xValue = new ContrastValue(1);
    ResolutionValue			yValue = new ResolutionValue(0);

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    	testPatch = new ConResPatch(row, column, xValue, yValue);
    }
    
    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	
    }

    /**
     * Test method for {@link com.grasppe.conres.framework.targets.model.grid.GridPatch#getPatchColumn()}.
     */
    @Test
    public final void testGetPatchColumn() {
    	Assert.assertEquals(testPatch.getPatchColumn(), column);
    }

    /**
     * Test method for {@link com.grasppe.conres.framework.targets.model.grid.GridPatch#getPatchRow()}.
     */
    @Test
    public final void testGetPatchRow() {
    	Assert.assertEquals(testPatch.getPatchRow(), row);
    }

    /**
     * Test method for {@link com.grasppe.conres.framework.targets.model.grid.ConResPatch#getXValue()}.
     */
    @Test
    public final void testGetXValue() {
    	AbstractValue abstractValue = testPatch.getXValue();
    	double thisValue = abstractValue.getValue();
    	Assert.assertEquals(thisValue, xValue.getValue());
    }

    /**
     * Test method for {@link com.grasppe.conres.framework.targets.model.grid.ConResPatch#getYValue()}.
     */
    @Test
    public final void testGetYValue() {
    	AbstractValue abstractValue = testPatch.getYValue();
    	double thisValue = abstractValue.getValue();
    	Assert.assertEquals(thisValue, yValue.getValue());
    }

//    /**
//     * Test method for {@link com.grasppe.conres.framework.targets.model.GridPatch#setPatchColumn(int)}.
//     */
//    @Test
//    public final void testSetPatchColumn() {
//        fail("Not yet implemented");	// TODO
//    }
//
//    /**
//     * Test method for {@link com.grasppe.conres.framework.targets.model.GridPatch#setPatchRow(int)}.
//     */
//    @Test
//    public final void testSetPatchRow() {
//        fail("Not yet implemented");	// TODO
//    }
//
//    /**
//     * Test method for {@link com.grasppe.conres.framework.targets.model.GridPatch#setXValue(com.grasppe.morie.units.AbstractValue)}.
//     */
//    @Test
//    public final void testSetXValue() {
//        fail("Not yet implemented");	// TODO
//    }
//
//    /**
//     * Test method for {@link com.grasppe.conres.framework.targets.model.GridPatch#setYValue(com.grasppe.morie.units.AbstractValue)}.
//     */
//    @Test
//    public final void testSetYValue() {
//        fail("Not yet implemented");	// TODO
//    }

    /**
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {}
    
    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {}
}
