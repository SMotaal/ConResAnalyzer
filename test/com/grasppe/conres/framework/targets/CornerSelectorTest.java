/*
 * @(#)CornerSelectorTest.java   11/12/07
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.framework.targets;

import com.grasppe.lure.framework.GrasppeKit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

//~--- JDK imports ------------------------------------------------------------

import java.rmi.UnexpectedException;

/**
 * @author daflair
 *
 */
public class CornerSelectorTest {

    /**
     *  @throws Exception
     */
    @After
    public void tearDown() throws Exception {}

    /**
     *  @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    /**
     */
    @Test
    public final void test() {

//      fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link com.grasppe.conres.framework.targets.CornerSelector#sortRectangleROIIndex(int[]}.
     * @throws UnexpectedException
     */
    @Test
    public final void testSortRectangleROIIndex() throws UnexpectedException {
        int[]	sX = { 50, 100, 100, 50 };
        int[]	sY = { 50, 50, 100, 100 };

        int[][]	tI = new int[][] {
            new int[] { 0, 1, 2, 3 }, new int[] { 0, 1, 3, 2 }, new int[] { 0, 2, 1, 3 },
            new int[] { 0, 2, 3, 1 }, new int[] { 0, 3, 1, 2 }, new int[] { 1, 0, 2, 3 },
            new int[] { 1, 2, 0, 3 }, new int[] { 1, 2, 3, 0 }, new int[] { 1, 3, 0, 2 },
            new int[] { 1, 3, 2, 0 }, new int[] { 2, 0, 1, 3 }, new int[] { 2, 0, 3, 1 },
            new int[] { 2, 1, 3, 0 }, new int[] { 2, 3, 0, 1 }, new int[] { 2, 3, 1, 0 },
            new int[] { 3, 0, 1, 2 }, new int[] { 3, 0, 2, 1 }, new int[] { 3, 1, 0, 2 },
            new int[] { 3, 1, 2, 0 }, new int[] { 3, 2, 0, 1 }
        };

        int[]	cX,	cY,	cI,	dI,
				fI = new int[4], eI = new int[]{0, 1, 2, 3};

        for (int i = 0; i < tI.length; i++) {
            cI = tI[i];
            cX = new int[] { sX[cI[0]], sX[cI[1]], sX[cI[2]], sX[cI[3]] };
            cY = new int[] { sY[cI[0]], sY[cI[1]], sY[cI[2]], sY[cI[3]] };
            dI = CornerSelector.sortRectangleROIIndex(cX, cY);
            for (int p = 0; p < 4; p++)
                fI[p] = cI[dI[p]];
            GrasppeKit.debugText("ArrayEquals",
                                 ("cI = " + cI[0] + ", " + cI[1] + ", " + cI[2] + ", " + cI[3]
                                  + ", ") + "\t"
                                          + ("dI = " + dI[0] + ", " + dI[1] + ", " + dI[2] + ", "
                                             + dI[3] + ", ") + "\t"
                                                 + ("fI = " + fI[0] + ", " + fI[1] + ", " + fI[2]
                                                     + ", " + fI[3] + ", "), 1);
            assertArrayEquals(eI, fI);
        }
    }

    /**
     *  @throws Exception
     */
    @Before
    public void setUp() throws Exception {}

    /**
     *  @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}
}
