/*
 * @(#)ConResAnalyzerPreferencesAdapter.java   11/12/16
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

import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author daflair
 *
 */
public class ConResAnalyzerPreferencesAdapter {

    protected static ConResPreferencesFactory	factory     = new ConResPreferencesFactory();
    protected static Preferences				preferences = factory.getPreferences();
    protected static String						packageName = factory.getOwnerClass().getPackage().getName();

    /** Field description */
    public static String	defaultCasePath = null;

    /** Field description */
    public static final String	DEFAULT_CASE_PATH_KEY = "Default Case Path";

    /** Field description */
    public static final String	FIELD_SEPARATOR = GrasppeKit.FIELD_SEPARATOR;

    /** Field description */
    public static final String	ARRAY_OF_OBJECTS = "array[]";

    /** Field description */
    public static final String	ARRAY_OF_INTEGERS = "integer[]";

    /** Field description */
    public static final String	ARRAY_OF_DOUBLES = "double[]";

    /** Field description */
    public static final String	ARRAY_OF_STRINGS = "string[]";

    /** Field description */
    public static final String	ARRAY_OF_BOOLEANS = "boolean[]";

    /**
     *  @param arrayString
     *  @return
     */
    protected static ArrayList<?> decodeArrayString(String arrayString) {
        String[]	valueStrings = new String[] {};

        try {
            valueStrings = arrayString.trim().split(FIELD_SEPARATOR);
            if (valueStrings.length < 1)
                throw new java.util.MissingFormatArgumentException(
                    "Invalid array string value format.");
        } catch (Exception exception) {
            GrasppeKit.debugError("Decoding Array Values", exception, 2);
        }

//      ArrayList<?>  objects; // = new ArrayList<?>();       // = new Class<T>[];

        // TODO: Parse the values
        switch (valueStrings[0].toLowerCase()) {

        case ARRAY_OF_INTEGERS :
            ArrayList<Integer>	integers = new ArrayList<Integer>();

            for (int i = 1; i < valueStrings.length; i++) {
                Integer	value = new Integer(valueStrings[i]);

                integers.add(value);
            }

            return integers;

        case ARRAY_OF_DOUBLES :
            ArrayList<Double>	doubles = new ArrayList<Double>();

            for (int i = 1; i < valueStrings.length; i++) {
                Double	value = new Double(valueStrings[i]);

                doubles.add(value);
            }

            return doubles;

        case ARRAY_OF_STRINGS :
            ArrayList<String>	strings = new ArrayList<String>();

            for (int i = 1; i < valueStrings.length; i++) {
                String	value = new String(valueStrings[i]);

                strings.add(value);
            }

            return strings;

        case ARRAY_OF_BOOLEANS :
            ArrayList<Boolean>	booleans = new ArrayList<Boolean>();

            for (int i = 1; i < valueStrings.length; i++) {
                Boolean	value = new Boolean((valueStrings[i].toLowerCase() == "y") ? "true"
                        : "false");

                booleans.add(value);
            }

            return booleans;

        case ARRAY_OF_OBJECTS :
        default :
            ArrayList<Object>	objects = new ArrayList<Object>();

            for (int i = 1; i < valueStrings.length; i++) {
                Object	value = valueStrings[i];

                objects.add(value);
            }

            return objects;
        }
    }

    /**
     */
    public static void dumpPreferences() {
        String	dumpString = "\n\n\t\t" + preferences.absolutePath();

        try {
            for (String preferenceKey : preferences.keys()) {
                dumpString += "\n\t\t\t" + preferenceKey;
                dumpString += " \t\t" + preferences.get(preferenceKey, null);

            }
        } catch (BackingStoreException exception) {
            dumpString += "<Exception>";
            GrasppeKit.debugError("Enumerating Application Preferences", exception, 2);

        }

        GrasppeKit.debugText("Preferences " + packageName, dumpString + "\n\n", 2);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        ArrayList<?>	values = decodeArrayString(ARRAY_OF_STRINGS + " false 1 two III four");

        values.isEmpty();

//      ConResAnalyzerPreferencesAdapter.dumpPreferences();
    }

    /**
     *  @param newPath
     */
    public static void putDefaultCasePath(String newPath) {
        preferences.put(DEFAULT_CASE_PATH_KEY, newPath);
    }

    /**
     *  @return
     */
    public static String getDefaultCasePath() {
        return preferences.get(DEFAULT_CASE_PATH_KEY, "").trim();
    }

//    
//  public static int[] getZoomKeyLevels() {
//    
//  }
}
