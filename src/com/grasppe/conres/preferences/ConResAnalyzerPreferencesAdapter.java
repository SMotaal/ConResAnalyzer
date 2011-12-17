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
import java.util.Arrays;
import java.util.List;
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
    public static final String
		DEFAULT_CASE_PATH_KEY = "Default Case Path",
		RECENT_CASES_KEY      = "Recent Cases";

    /** Field description */
    public static final String
		VERTICAL_ROI_OFFSET_KEY   = "Vertical ROI Offset",
		HORIZONTAL_ROI_OFFSET_KEY = "Horizontal ROI Offset";

    /** Field description */
    public static final String	ZOOM_LEVELS_KEY = "Zoom Levels";

    /** Field description */
    public static final String	BLINK_RATE_KEY = "Blinking Rate";

    /** Field description */
    public static final String
		VOID_COLOR_KEY        = "Void Color",
		CLEAR_COLOR_KEY       = "Clear Color",
		PASS_COLOR_KEY        = "Pass Color",
		FAIL_COLOR_KEY        = "Fail Color",
		MARGINAL_COLOR_KEY    = "Marginal Fail Color",
		ASSUME_PASS_COLOR_KEY = "Assume Pass Color",
		ASSUME_FAIL_COLOR_KEY = "Assume Fail Color",
		BLINK_COLOR_KEY       = "Blinking Color";

    /** Field description */
    public static final String
		ROI_COLOR_KEY  = "ROI Color",
		ROI_STROKE_KEY = "ROI Stroke Color";

    /** Field description */
    public static final String	FIELD_SEPARATOR = GrasppeKit.FIELD_SEPARATOR;

    /** Field description */
    public static final String
		ARRAY_OF_OBJECTS  = "array[]",
		ARRAY_OF_INTEGERS = "integer[]",
		ARRAY_OF_DOUBLES  = "double[]",
		ARRAY_OF_STRINGS  = "string[]",
		ARRAY_OF_BOOLEANS = "boolean[]";

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

            // TODO: Parse the values
            String	typeString = valueStrings[0].toLowerCase();
            boolean	emptyArray = valueStrings[1].trim().equals("{}");

            if (emptyArray && (valueStrings.length == 2)) return null;

            if (typeString.equals(ARRAY_OF_INTEGERS)) {
                ArrayList<Integer>	integers = new ArrayList<Integer>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    Integer	value = new Integer(valueStrings[i]);

                    integers.add(value);
                }

                return integers;
            } else if (typeString.equals(ARRAY_OF_DOUBLES)) {

                ArrayList<Double>	doubles = new ArrayList<Double>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    Double	value = new Double(valueStrings[i]);

                    doubles.add(value);
                }

                return doubles;

            } else if (typeString.equals(ARRAY_OF_STRINGS)) {
                ArrayList<String>	strings = new ArrayList<String>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    String	value = new String(valueStrings[i]);

                    strings.add(value);
                }

                return strings;

            } else if (typeString.equals(ARRAY_OF_BOOLEANS)) {
                ArrayList<Boolean>	booleans = new ArrayList<Boolean>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    String			valueString = valueStrings[i].toLowerCase();
                    List<String>	trueString  = Arrays.asList(new String[] { "y", "yes", "true", "t",
                            "1" });
                    Boolean	value = new Boolean((trueString.contains(valueString)) ? "true"
                            : "false");

                    booleans.add(value);
                }

                return booleans;

            } else {	// (typeString.equals(ARRAY_OF_INTEGERS) {
                ArrayList<Object>	objects = new ArrayList<Object>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    Object	value = valueStrings[i];

                    objects.add(value);
                }

                return objects;
            }

        } catch (Exception exception) {
            GrasppeKit.debugError("Decoding Array Values", exception, 2);
        }

        return null;
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

        String	valueString   = "";		// ArrayList<?> valueList = decodeArrayString(valueString);
        String	decodedString = "";		// ArrayList<?> valueList = decodeArrayString(valueString);

        valueString = ARRAY_OF_STRINGS + " {}";
        if (decodeArrayString(valueString) == null) decodedString = "null";
        else decodedString = decodeArrayString(valueString).toString();
        GrasppeKit.debugText("Array of Strings", decodedString + "\t\t" + valueString + "\t", 2);

        valueString = ARRAY_OF_STRINGS + " false 1 two III four";
        GrasppeKit.debugText("Array of Strings",
                             decodeArrayString(valueString).toString() + "\t\t" + valueString
                             + "\t", 2);

        valueString = ARRAY_OF_INTEGERS + " 1 2 3 4 5";
        GrasppeKit.debugText("Array of Integers",
                             decodeArrayString(valueString).toString() + "\t\t" + valueString
                             + "\t", 2);

        valueString = ARRAY_OF_DOUBLES + " 1 2 3 4 5";
        GrasppeKit.debugText("Array of Doubles",
                             decodeArrayString(valueString).toString() + "\t\t" + valueString
                             + "\t", 2);

        valueString = ARRAY_OF_BOOLEANS + " t yes no false true";
        GrasppeKit.debugText("Array of Strings",
                             decodeArrayString(valueString).toString() + "\t\t" + valueString
                             + "\t", 2);

        ConResAnalyzerPreferencesAdapter.dumpPreferences();
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
