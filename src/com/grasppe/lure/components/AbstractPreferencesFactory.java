/*
 * @(#)AbstractPreferencesFactory.java   11/12/16
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.lure.components;

import com.apple.eio.FileManager;

import com.grasppe.lure.framework.GrasppeKit;

import net.infotrek.util.prefs.FilePreferencesFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author daflair
 *
 */
public abstract class AbstractPreferencesFactory extends FilePreferencesFactory {

    /** Field description */
    public static final String	FIELD_SEPARATOR = GrasppeKit.FIELD_SEPARATOR;

    /** Field description */
    public static final String	ARRAY_OF_BOOLEANS = "boolean[]";

    /** Field description */
    public static final String	ARRAY_OF_DOUBLES = "double[]";

    /** Field description */
    public static final String	ARRAY_OF_INTEGERS = "integer[]";

    /** Field description */
    public static final String	ARRAY_OF_OBJECTS = "array[]";

    /** Field description */
    public static final String	ARRAY_OF_STRINGS = "string[]";
    static int					dbg              = 2;

    /**
     *  @param arrayString
     *  @return
     */
    public static ArrayList<?> decodeArrayString(String arrayString) {
        String[]	valueStrings = new String[] {};

        try {
            valueStrings = arrayString.trim().split(FIELD_SEPARATOR);
            if (valueStrings.length < 1)
                throw new java.util.MissingFormatArgumentException(
                    "Invalid array string value format.");

            // TODO: Parse the values
            String	typeString = valueStrings[0].toLowerCase();
            boolean	emptyArray = valueStrings[1].trim().equals("{}") && (valueStrings.length == 2);

            if (typeString.equals(AbstractPreferencesFactory.ARRAY_OF_INTEGERS)) {
                ArrayList<Integer>	integers = new ArrayList<Integer>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    Integer	value = new Integer(valueStrings[i]);

                    integers.add(value);
                }

                return integers;
            } else if (typeString.equals(AbstractPreferencesFactory.ARRAY_OF_DOUBLES)) {

                ArrayList<Double>	doubles = new ArrayList<Double>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    Double	value = new Double(valueStrings[i]);

                    doubles.add(value);
                }

                return doubles;

            } else if (typeString.equals(AbstractPreferencesFactory.ARRAY_OF_STRINGS)) {
                ArrayList<String>	strings = new ArrayList<String>();

                if (!emptyArray) for (int i = 1; i < valueStrings.length; i++) {
                    String	value = new String(valueStrings[i]);

                    strings.add(value);
                }

                return strings;

            } else if (typeString.equals(AbstractPreferencesFactory.ARRAY_OF_BOOLEANS)) {
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

    // /** Field Description */
    // public static int[]
    // defaultVoidColor        = new int[]{63, 63, 63},
    // defaultClearColor       = new int[]{128, 128, 128},
    // defaultPassColor        = new int[]{0, 220, 0},
    // defaultFailColor        = new int[]{228, 0, 0},
    // defaultMarginalColor    = new int[]{255, 255, 0},
    // defaultAssumePassColor = new int[]{0,0,0},
    // defaultAssumeFailColor = new int[]{128, 0, 0},
    // defaultCursorColor  = new int[]{32,32, 32},
    // roiFillColor    =   new int[]{0,0,0},
    // roiStrokeColor  =   new int[]{0,0,0};

    /**
     *  @param array
     *  @return
     */
    public static String encodeArrayValues(Object[] array) {
        String	arrayString = "";
        String	typeString  = "";

        if ((array == null) || (array.length == 0)) arrayString = "{}";
        else for (Object object : array)
            arrayString += " " + object.toString();

        if (array == null) return AbstractPreferencesFactory.ARRAY_OF_STRINGS + " " + arrayString;
        else {
            Class	componentClass = array.getClass().getComponentType();

            if (componentClass == String.class)
                arrayString = AbstractPreferencesFactory.ARRAY_OF_STRINGS + arrayString;
            else if ((componentClass == int.class) || (componentClass == Integer.class))
                     arrayString = AbstractPreferencesFactory.ARRAY_OF_INTEGERS + arrayString;
            else if ((componentClass == double.class) || (componentClass == Double.class))
                     arrayString = AbstractPreferencesFactory.ARRAY_OF_DOUBLES + arrayString;
            else if ((componentClass == boolean.class) || (componentClass == Boolean.class))
                     arrayString = AbstractPreferencesFactory.ARRAY_OF_BOOLEANS + arrayString;
            else arrayString = AbstractPreferencesFactory.ARRAY_OF_OBJECTS + arrayString;
        }

        return arrayString;

    }

    /**
     * 	@return
     */
    public abstract Class<?> getFactoryClass();

    /**
     *  @param fileName
     *  @return
     */
    protected static String getMacUserPreferencesPath(String fileName) {
        String	filePath = null;

        if (!GrasppeKit.OperatingSystem.isMac()) return filePath;

        try {
            filePath = FileManager.findFolder(FileManager.kUserDomain,
                                              FileManager.OSTypeToInt("pref"));
        } catch (FileNotFoundException exception) {
        	try {
            filePath = GrasppeKit.cat(new String[] { System.getProperty("user.home"), "Library",
                    "Preferences" }, File.separator);
        	} catch (Exception exception2) {
        		GrasppeKit.debugError("Getting Preferences File", exception2, 1);
        		filePath = ".";
        	}
        }

        if (fileName != null) filePath += File.separator + fileName;

        return filePath;
    }

    /**
     * 	@return
     */
    public abstract Class<?> getOwnerClass();

    /**
     * 	@return
     */
    public abstract Preferences getPreferences();
    
    public String getPackageName() {
    	return getOwnerClass().getPackage().getName();
    }

    /**
     *  @param ownerClass
     *  @return
     */
    protected static String getPreferencesFileByClass(Class<?> ownerClass) {
        String	fileName = ownerClass.getPackage().getName() + ".ini";

        switch (GrasppeKit.OperatingSystem.currentSystem()) {

        case MAC :
            return getMacUserPreferencesPath(fileName);

        default :
            return "." + File.separator + fileName;
        }
    }

    /**
     * 	@return
     */
    public String getUserPreferencesFile() {
        return getPreferencesFileByClass(getOwnerClass());
    }
}
