/*
 * @(#)Preferences.java   11/12/18
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

import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.IPreferencesEnum;

//~--- JDK imports ------------------------------------------------------------

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author daflair
 *
 */
public class Preferences {

  /** Field description */
  public static final String	VALUE_FIELD = "value";

  /** Field description */
  public static final String	COLOR_FIELD = "color";

  /** Field description */
  public static final String	KEYCODE_FIELD = "keycode";
  
  /** Field description */
  public static final String	COMMAND_KEYCODE_FIELD = "commandkeycode"; 

  /**
   *
   */
  public Preferences() {}

  /**
   */
  public static enum Tags implements IPreferencesEnum {																						// , IObjectValue, IStringDescription {

    /* Analysis Preferences */
    BLINK_SPEED("Blinking Speed", 300, VALUE_FIELD, "Cursor blinking speed in milliseconds"),			//
    ZOOM_LEVELS("Zoom Levels", new double[] { 2.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0 }, VALUE_FIELD,
                "Zoom keys scaling factor for number keys 0-9"),																	//

    /* Analysis Colors */
    VOID_COLOR("Void Color", new int[] { 63, 63, 63 }, COLOR_FIELD, "Color for out of range cells"),															//
    CLEAR_COLOR("Clear Color", new int[] { 128, 128, 128 }, COLOR_FIELD, "Color for our of range cells"),													//
    ASSUMED_PASS_COLOR("Assumed Pass Color", new int[] { 0, 128, 0 }, COLOR_FIELD, "Color for cells logically assumed good"),			//
    PASS_COLOR("Pass Color", new int[] { 0, 220, 0 }, COLOR_FIELD, "Color for cells indicated as good"),													//
    MARGINAL_COLOR("Marginal Color", new int[] { 255, 255, 0 }, COLOR_FIELD, "Color for cells indicated as marginal"),						//
    FAIL_COLOR("Fail Color", new int[] { 228, 0, 0 }, COLOR_FIELD, "Color for cells indicated as rejected"),											//
    ASSUMED_FAIL_COLOR("Assumed Fail Color", new int[] { 128, 0, 0 }, COLOR_FIELD,
                       "Color for cells logically assumed rejected"),																															//
    CURSOR_COLOR("Cursor Color", new int[] { 32, 32, 32 }, COLOR_FIELD, "Color for the blinking cursor"),												//

    /* Analysis Keys */
    PASS_KEYCODE("Pass Keycode", KeyCode.VK_G, KEYCODE_FIELD, "Key used to indicate a good patch."),														//
    FAIL_KEYCODE("Fail Keycode", KeyCode.VK_R, KEYCODE_FIELD, "Key used to indicate a rejected patch."),												//
    MARGINAL_KEYCODE("Marginal Keycode", KeyCode.VK_W, KEYCODE_FIELD, "Key used to indicate a marginally accepted patch."),			//
    CLEAR_COLUMN_KEYCODE("Clear Column Keycode", KeyCode.VK_C, KEYCODE_FIELD, "Key used to clearing a grid column."),						//

    /* Case Manager Preferences */
    DEFAULT_CASE_PATH("Default Case Path", "", VALUE_FIELD, "Path to the folder parent folder the case files"),									//
    RECENT_CASES("Recent Cases", new String[] { "" }, VALUE_FIELD, "Path to recently opened case files"),												//

    /* Corner Selection Preferences */
    Y_ROI_OFFSET("Vertical ROI Offset", -1, VALUE_FIELD, "Vertical offset in millimeters"),																			//
    X_ROI_OFFSET("Horizontal ROI Offset", -1, VALUE_FIELD, "Horizontal offset in millimeters"),																	//

    /* Global Keys */
    CORNER_SELECTOR_KEYCODE("Corner Selector Keycode", KeyCode.VK_M, COMMAND_KEYCODE_FIELD, "Key used to indicate a good patch."),			//
    BLOCK_SELECTOR_KEYCODE("Block Selector Keycode", KeyCode.VK_B, COMMAND_KEYCODE_FIELD, "Key used to indicate a rejected patch."),		//
    BLOCK_ANALYZER_KEYCODE("Block Analyzer Keycode", KeyCode.VK_A, COMMAND_KEYCODE_FIELD, "Key used to indicate a rejected patch."),		//
    ;

    private static final Map<String, Tags>	lookup = new HashMap<String, Tags>();

    static {
      for (Tags s : EnumSet.allOf(Tags.class))
        lookup.put(s.key(), s);
    }

    private String	key;
    private Object	defaultValue;
    private String	fieldClass;
    private String	description;

    /**
     *  @param key
     *  @param defaultValue
     * 	@param fieldClass
     *  @param description
     */
    private Tags(String key, Object defaultValue, String fieldClass, String description) {
      this.key          = key;
      this.defaultValue = defaultValue;
      this.description  = description;
      this.fieldClass   = fieldClass;
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
    public Tags get(String key) {
      return lookup.get(key);
    }

    /**
     * @return the fieldClass
     */
    public String getFieldClass() {
      return fieldClass;
    }
  }

  /**
   * 	@param preference
   * 	@param value
   */
  public static void put(Tags preference, Object value) {
    PreferencesAdapter.getInstance().put(preference, value);
  }

  /**
   * 	@param preference
   * 	@param value
   */
  public static void putArray(Tags preference, Object[] value) {
    PreferencesAdapter.getInstance().putArray(preference, value);
  }

  /**
   * 	@param preference
   * 	@return
   */
  public static Object get(Tags preference) {
    return PreferencesAdapter.getInstance().get(preference);
  }

  /**
   * 	@param preference
   * 	@return
   */
  public static int getInt(Tags preference) {
    return ((Integer)PreferencesAdapter.getInstance().get(preference)).intValue();
  }
}
