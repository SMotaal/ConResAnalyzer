/*
 * @(#)PreferencesAdapterTest.java   11/12/18
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.conres.preferences;

import com.grasppe.lure.components.AbstractPreferencesFactory;
import com.grasppe.lure.framework.GrasppeKit;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;

/**
 * @author daflair
 *
 */
public class PreferencesAdapterTest {

    /**
     */
    @Test
    public final void testBooleans() {
        Boolean[]	array         = { true, true, false, false, true };
        String		arrayString   = "t yes no false true";
        String		arrayType     = AbstractPreferencesFactory.ARRAY_OF_BOOLEANS;
        String		componentName = (array == null) ? "null"
                : array.getClass().getComponentType()
                    .getSimpleName() + "[]";

        String	valueString = AbstractPreferencesFactory.encodeArrayValues(array);		// arrayType + " " + arrayString;
        ArrayList<?>	decodedList = AbstractPreferencesFactory.decodeArrayString(valueString);

        GrasppeKit.debugText(componentName, ((decodedList.isEmpty()) ? "null"
                : decodedList.toString()) + "\t\t" + valueString + "\t", 2);
        assertArrayEquals(array, decodedList.toArray(new Boolean[0]));
    }

    /**
     */
    @Test
    public final void testDoubles() {
        Double[]	array       = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        String		arrayString = array[0] + " " + array[1] + " " + array[2] + " " + array[3] + " "
                             + array[4];	// + " " + array[5];
        String	arrayType     = AbstractPreferencesFactory.ARRAY_OF_DOUBLES;
        String	componentName = (array == null) ? "null"
                : array.getClass().getComponentType().getSimpleName() + "[]";

        String	valueString = AbstractPreferencesFactory.encodeArrayValues(array);		// arrayType + " " + arrayString;
        ArrayList<?>	decodedList = AbstractPreferencesFactory.decodeArrayString(valueString);

        GrasppeKit.debugText(componentName, ((decodedList.isEmpty()) ? "null"
                : decodedList.toString()) + "\t\t" + valueString + "\t", 2);
        assertArrayEquals(array, decodedList.toArray(new Double[0]));
    }

    /**
     */
    @Test
    public final void testDump() {
        PreferencesAdapter.getInstance().dumpPreferences();
    }

    /**
     */
    @Test
    public final void testIntegers() {
        Integer[]	array       = { 1, 2, 3, 4, 5 };
        String		arrayString = array[0] + " " + array[1] + " " + array[2] + " " + array[3] + " "
                             + array[4];	// + " " + array[5];
        String	arrayType     = AbstractPreferencesFactory.ARRAY_OF_INTEGERS;
        String	componentName = (array == null) ? "null"
                : array.getClass().getComponentType().getSimpleName() + "[]";

        String	valueString = AbstractPreferencesFactory.encodeArrayValues(array);		// arrayType + " " + arrayString;
        ArrayList<?>	decodedList = AbstractPreferencesFactory.decodeArrayString(valueString);

        GrasppeKit.debugText(componentName, ((decodedList.isEmpty()) ? "null"
                : decodedList.toString()) + "\t\t" + valueString + "\t", 2);
        assertArrayEquals(array, decodedList.toArray(new Integer[0]));
    }

    /**
     */
    @Test
    public final void testNullStrings() {

        String		arrayString   = "{}";
        String[]	array         = null;
        String		arrayType     = AbstractPreferencesFactory.ARRAY_OF_STRINGS;
        String		componentName = (array == null) ? "null"
                : array.getClass().getComponentType().getSimpleName() + "[]";

        String	valueString = AbstractPreferencesFactory.encodeArrayValues(array);		// arrayType + " " + arrayString;
        ArrayList<?>	decodedList = AbstractPreferencesFactory.decodeArrayString(valueString);

        GrasppeKit.debugText(componentName, ((decodedList.isEmpty()) ? "null"
                : decodedList.toString()) + "\t\t" + valueString + "\t", 2);
        assertEquals(true, decodedList.isEmpty());
    }

    /**
     */
    @Test
    public final void testStrings() {
        String		arrayString   = "false 1 two III four";
        String[]	array         = arrayString.split(" ");
        String		arrayType     = AbstractPreferencesFactory.ARRAY_OF_STRINGS;
        String		componentName = (array == null) ? "null"
                : array.getClass().getComponentType().getSimpleName() + "[]";

        String	valueString = AbstractPreferencesFactory.encodeArrayValues(array);		// arrayType + " " + arrayString;
        ArrayList<?>	decodedList = AbstractPreferencesFactory.decodeArrayString(valueString);

        GrasppeKit.debugText(componentName, ((decodedList.isEmpty()) ? "null"
                : decodedList.toString()) + "\t\t" + valueString + "\t", 2);
        assertArrayEquals(array, decodedList.toArray(new String[0]));
    }
}
