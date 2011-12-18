/*
 * @(#)PreferencesAdapter.java   11/12/16
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
import com.grasppe.lure.framework.GrasppeKit.IStringDescription;
import com.grasppe.lure.framework.GrasppeKit.IStringKey;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author daflair
 *
 */
public class PreferencesAdapter {

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
     */
    public enum AnalysisPreferences implements IPreferencesEnum {		// , IObjectValue, IStringDescription {

        /* Colors */
        VOID_COLOR("Void Color", new int[] { 63, 63, 63 }, "Color for out of range cells"),			//
        CLEAR_COLOR("Clear Color", new int[] { 128, 128, 128 }, "Color for our of range cells"),	//
        ASSUMED_PASS_COLOR("Assumed Pass Color", new int[] { 0, 128, 0 },
                           "Color for cells logically assumed good"),								//
        PASS_COLOR("Pass Color", new int[] { 0, 220, 0 }, "Color for cells indicated as good"),		//
        MARGINAL_COLOR("Marginal Color", new int[] { 255, 255, 0 },
                       "Color for cells indicated as marginal"),									//
        FAIL_COLOR("Fail Color", new int[] { 228, 0, 0 }, "Color for cells indicated as rejected"),		//
        ASSUMED_FAIL_COLOR("Assumed Fail Color", new int[] { 128, 0, 0 },
                           "Color for cells logically assumed rejected"),		//
        CURSOR_COLOR("Cursor Color", new int[] { 32, 32, 32 }, "Color for the blinking cursor"),	//

        /* Others */
        BLINK_SPEED("Blinking Speed", 300, "Cursor blinking speed in milliseconds"),				//
        ZOOM_LEVELS("Zoom Levels", new double[] {
            2.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0
        }, "Zoom keys scaling factor for number keys 0-9"),		//

        /* Keycodes */
        PASS_KEYCODE("Pass Keycode", KeyCode.VK_G, "Key used to indicate a good patch."),			//
        FAIL_KEYCODE("Fail Keycode", KeyCode.VK_R, "Key used to indicate a rejected patch."),		//
        MARGINAL_KEYCODE("Marginal Keycode", KeyCode.VK_W,
                         "Key used to indicate a marginally accepted patch."),						//
        CLEAR_COLUMN_KEYCODE("Pass Keycode", KeyCode.VK_C, "Key used to clearing a grid column."),		//
        ;

        private static final Map<String, AnalysisPreferences>	lookup = new HashMap<String,
                                                                           AnalysisPreferences>();

        static {
            for (AnalysisPreferences s : EnumSet.allOf(AnalysisPreferences.class))
                lookup.put(s.key(), s);
        }

        private String	key;
        private Object	defaultValue;
        private String	description;

        /**
         *  @param key
         *  @param defaultValue
         *  @param description
         */
        private AnalysisPreferences(String key, Object defaultValue, String description) {
            this.key          = key;
            this.defaultValue = defaultValue;
            this.description  = description;
        }

        /**
         *  @return
         */
        public Object defaultValue() {
            return defaultValue;
        }

        /**
         *  @return
         */
        public String description() {
            return description;
        }

        /**
         *  @return
         */
        public String key() {
            return key;
        }

        /**
         *  @param key
         *  @return
         */
        public AnalysisPreferences get(String key) {
            return lookup.get(key);
        }
    }

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
            boolean	emptyArray = valueStrings[1].trim().equals("{}") && (valueStrings.length == 2);

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

//  /** Field Description */
//  public static int[]
//        defaultVoidColor        = new int[]{63, 63, 63},
//        defaultClearColor       = new int[]{128, 128, 128},
//        defaultPassColor        = new int[]{0, 220, 0},
//        defaultFailColor        = new int[]{228, 0, 0},
//        defaultMarginalColor    = new int[]{255, 255, 0},
//        defaultAssumePassColor = new int[]{0,0,0},
//        defaultAssumeFailColor = new int[]{128, 0, 0},
//        defaultCursorColor  = new int[]{32,32, 32},
//        roiFillColor    =   new int[]{0,0,0},
//        roiStrokeColor  =   new int[]{0,0,0};

    /**
     *  @param array
     *  @return
     */
    protected static String encodeArrayValues(Object[] array) {
        String	arrayString = "";
        String	typeString  = "";

        if ((array == null) || (array.length == 0)) arrayString = "{}";
        else for (Object object : array)
            arrayString += " " + object.toString();

        if (array == null) return ARRAY_OF_STRINGS + " " + arrayString;
        else {
	        Class	componentClass = array.getClass().getComponentType();
	
	        if (componentClass == String.class) arrayString = ARRAY_OF_STRINGS + arrayString;
	        else if ((componentClass == int.class) || (componentClass == Integer.class))
	                 arrayString = ARRAY_OF_INTEGERS + arrayString;
	        else if ((componentClass == double.class) || (componentClass == Double.class))
	                 arrayString = ARRAY_OF_DOUBLES + arrayString;
	        else if ((componentClass == boolean.class) || (componentClass == Boolean.class))
	                 arrayString = ARRAY_OF_BOOLEANS + arrayString;
	        else arrayString = ARRAY_OF_OBJECTS + arrayString;
        }

        return arrayString;

    }

    /**
     * @param args
     */
    public static void oldmain(String[] args) {

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

        PreferencesAdapter.dumpPreferences();
    }

    /**
     *  @param newPath
     */
    public static void putDefaultCasePath(String newPath) {
        preferences.put(DEFAULT_CASE_PATH_KEY, newPath);
    }

    /**
     *  @param preference
     *  @param value
     */
    public static void putKeyCode(IPreferencesEnum preference, KeyCode value) {
        preferences.put(preference.key(), "" + value.value());
    }

    /**
     *  @param preference
     *  @return
     */
    public static Object get(IPreferencesEnum preference) {
        String	key          = preference.key();
        Object	defaultValue = preference.defaultValue();
        String	value        = preferences.get(key, null);

        if (value == null) return defaultValue;

        value = value.trim();

        try {
            if (defaultValue instanceof String) return value;
            else if (defaultValue instanceof Object[]) return decodeArrayString(value);
            else if (defaultValue.getClass().isPrimitive()) {
                if (defaultValue.getClass().isAssignableFrom(int.class)) return new Integer(value);		// .intValue();
                else if (defaultValue.getClass().isAssignableFrom(double.class))
                         return new Double(value);		// .doubleValue();
                else if (defaultValue.getClass().isAssignableFrom(boolean.class))
                         return new Boolean(Arrays.asList(new String[] { "y", "yes", "true", "t",
                        "1" }).contains(value) ? true
                                               : false);
            }
        } catch (Exception exception) {}

        return defaultValue;
    }

    /**
     *  @return
     */
    public static String getDefaultCasePath() {
        return preferences.get(DEFAULT_CASE_PATH_KEY, "").trim();
    }

    /**
     *  @param preference
     *  @return
     */
    public static int getInt(IPreferencesEnum preference) {
        return ((Integer)get(preference)).intValue();
    }

    /**
     *  @param preference
     *  @return
     */
    public static KeyCode getKeyCode(IPreferencesEnum preference) {
        try {
            return KeyCode.get(getInt(preference));		// ((Integer)get(preference)).intValue();
        } catch (Exception exception) {
            if (preference.defaultValue() instanceof KeyCode) {
                putKeyCode(preference, (KeyCode)preference.defaultValue());

                return (KeyCode)preference.defaultValue();
            } else
                return null;
        }
    }

    /**
     *
     *  @version        $Revision: 1.0, 11/12/17
     *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public interface IPreferencesEnum extends IStringKey, IStringDescription {

        /**
         *  @return
         */
        Object defaultValue();
    }

//    
//  public static int[] getZoomKeyLevels() {
//    
//  }
}
