/*
 * @(#)AbstractPreferencesAdapter.java   11/12/18
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.IIntegerValue;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.IPreferencesEnum;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/18
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class AbstractPreferencesAdapter {

  protected static AbstractPreferencesFactory	factory            = null;
  protected static Preferences								preferences        = null;
  protected static String											packageName        = null;
  protected static AbstractPreferencesAdapter	preferencesAdapter = null;		// new AbstractPreferencesAdapter();
  int																					dbg                = 2;

  /**
   */
  public AbstractPreferencesAdapter() {
    super();
  }

  /**
   * 	@param arrayList
   * 	@return
   */
  public boolean[] asArray(ArrayList<Boolean> arrayList) {
    boolean[]	array = new boolean[arrayList.size()];

    for (int i = 0; i < arrayList.size(); i++)		// (Integer item : arrayList.toArray(new Integer[0]))
      array[i] = arrayList.get(i).booleanValue();

    return array;
  }

  /**
   * 	@param arrayList
   * 	@return
   */
  public double[] asArray(ArrayList<Double> arrayList) {
    double[]	array = new double[arrayList.size()];

    for (int i = 0; i < arrayList.size(); i++)		// (Integer item : arrayList.toArray(new Integer[0]))
      array[i] = arrayList.get(i).doubleValue();

    return array;
  }

  /**
   * 	@param arrayList
   * 	@return
   */
  public int[] asArray(ArrayList<Integer> arrayList) {
    int[]	array = new int[arrayList.size()];

    for (int i = 0; i < arrayList.size(); i++)		// (Integer item : arrayList.toArray(new Integer[0]))
      array[i] = arrayList.get(i).intValue();

    return array;
  }

  /**
   *    @param array
   *    @return
   */
  public Object[] asObjectArray(Object array) {
    if (!array.getClass().isArray()) return null;

//  if (!array.getClass().isPrimitive())
//      return null;
    ArrayList<Object>	objectArrayList = new ArrayList<Object>();
    Class							componentType   = array.getClass().getComponentType();

    if (componentType == int.class) {
      for (int item : (int[])array)
        objectArrayList.add(new Integer(item));

      return objectArrayList.toArray(new Integer[0]);
    } else if (componentType == double.class) {
      for (double item : (double[])array)
        objectArrayList.add(new Double(item));

      return objectArrayList.toArray(new Double[0]);
    } else if (componentType == boolean.class) {
      for (boolean item : (boolean[])array)
        objectArrayList.add(new Boolean(item));

      return objectArrayList.toArray(new Boolean[0]);
    }			// else {}

    return objectArrayList.toArray();

  }

  /**
   */
  public void dumpPreferences() {
    String	dumpString = "\n\n\t\t" + getPreferences().absolutePath();

    try {
      for (String preferenceKey : getPreferences().keys()) {
        dumpString += "\n\t\t\t" + preferenceKey;
        dumpString += " \t\t" + getPreferences().get(preferenceKey, null);

      }
    } catch (BackingStoreException exception) {
      dumpString += "<Exception>";
      GrasppeKit.debugError("Enumerating Application Preferences", exception, 2);

    }

    GrasppeKit.debugText("Preferences " + getPackageName(), dumpString + "\n\n", 2);

  }

  /**
   *    @param preference
   *    @param value
   */
  public void put(IPreferencesEnum preference, Object value) {
    String	key          = preference.key();
    Object	defaultValue = preference.defaultValue();

    if (defaultValue.getClass().isArray()) {
      putArray(preference, value);

      return;
    }

    try {
      Object	objectValue = value;
      Class		objectType  = objectValue.getClass();
      String	stringValue = null;

      if (value == null) objectValue = defaultValue;									// getPreferences().put(key, defaultValue); //.toString());

      if (objectType == int.class) stringValue = "" + objectValue;		// getPreferences().putInt(key, (int)objectValue);
      else if (objectType == double.class) stringValue = "" + objectValue;
      else if (objectType == boolean.class) stringValue = "" + objectValue;
      else if (objectValue instanceof IIntegerValue) stringValue = "" + ((IIntegerValue)objectValue).value();
      else stringValue = "" + objectValue;

      getPreferences().put(key, stringValue);

    } catch (Exception exception) {
      GrasppeKit.debugError("Writing " + key + "Preferences", exception, 2);
    }
  }

  /**
   *    @param preference
   *    @param value
   */
  public void putArray(IPreferencesEnum preference, Object value) {
    Object[]	objectArray = asObjectArray(value);

    putArray(preference, objectArray);
  }

  /**
   *    @param preference
   *    @param value
   */
  public void putArray(IPreferencesEnum preference, Object[] value) {
    String		key          = preference.key();
    Object[]	defaultValue = asObjectArray(preference.defaultValue());

    Object[]	array        = null;

//  if (value instanceof int[]) array = (int[])value;

    try {
      if ((value == null))		// || ((Array)value.length) == 0))
        getPreferences().put(key, AbstractPreferencesFactory.encodeArrayValues(defaultValue));
      else getPreferences().put(key, AbstractPreferencesFactory.encodeArrayValues(value));
    } catch (Exception exception) {
      GrasppeKit.debugError("Writing " + key + "Preferences", exception, 2);
    }
  }

  /**
   * @param preference
   * @param value
   */
  public void putKeyCode(IPreferencesEnum preference, KeyCode value) {
    getPreferences().put(preference.key(), "" + value.value());
  }

///**
// * @param preference
// * @return
// */
//public Object get(IPreferencesEnum preference) {
//  
//}

  /**
   * @param preference
   * @return
   */
  public Object get(IPreferencesEnum preference) {
    String	key          = preference.key();
    Object	defaultValue = preference.defaultValue();
    String	value        = getPreferences().get(key, null);
    Class		valueClass   = defaultValue.getClass();
    Object	returnValue  = null;

//  if (value == null) return defaultValue;
    if (value != null) {

      value = value.trim();

      try {
        if (defaultValue instanceof String) return value;
        else if (defaultValue instanceof int[])
               returnValue = asArray((ArrayList<Integer>)AbstractPreferencesFactory.decodeArrayString(value));		// .toArray(new int[0]);
        else if (defaultValue instanceof double[])		// returnValue = AbstractPreferencesFactory.decodeArrayString(value);
               returnValue = asArray((ArrayList<Double>)AbstractPreferencesFactory.decodeArrayString(value));			// .toArray(new int[0]);
        else if (defaultValue instanceof boolean[])			// returnValue = AbstractPreferencesFactory.decodeArrayString(value);
               returnValue = asArray((ArrayList<Boolean>)AbstractPreferencesFactory.decodeArrayString(value));		// .toArray(new int[0]);
        else if (defaultValue instanceof Object[]) returnValue = AbstractPreferencesFactory.decodeArrayString(value).toArray();
        else if (defaultValue instanceof IIntegerValue) returnValue = new Integer(value).intValue();
//        else if (valueClass.isPrimitive()) {
        else if (valueClass.isAssignableFrom(int.class) || valueClass==Integer.class)
        	  returnValue = new Integer(value).intValue();							// .intValue();
          else if (valueClass.isAssignableFrom(double.class) || valueClass==Double.class)
        	  returnValue = new Double(value).doubleValue();		// .doubleValue();
          else if (valueClass.isAssignableFrom(boolean.class) || valueClass==Boolean.class)
                 returnValue = Arrays.asList(new String[] { "y", "yes", "true", "t", "1" }).contains(value) ? true
                                                                                                            : false;
//        }
      } catch (Exception exception) {
        GrasppeKit.debugError("Reading " + key + "Preferences", exception, 2);
      }
    } else {}

    if (returnValue == null) {		// || (returnValue.getClass() != defaultValue.getClass())) {
      put(preference, defaultValue);
//      GrasppeKit.debugText("Reading " + key + "Preferences", "Using default value", dbg);
      returnValue = defaultValue;
    } else {
//      GrasppeKit.debugText("Reading " + key + "Preferences", "Using stored value", dbg);
    }

    return returnValue;
  }

  /**
   *    @return
   */
  public abstract AbstractPreferencesFactory getFactory();

  /**
   *    @return
   */
  public static AbstractPreferencesAdapter getInstance() {
    return preferencesAdapter;
  }

///**
// * @return the factory
// */
//protected static AbstractPreferencesFactory getFactory(); // {return factory;}

  /**
   * @param preference
   * @return
   */
  public int getInt(IPreferencesEnum preference) {
    return ((Integer)get(preference)).intValue();
  }

  /**
   * @param preference
   * @return
   */
  public KeyCode getKeyCode(IPreferencesEnum preference) {
    try {
      return KeyCode.get(getInt(preference));
    } catch (Exception exception) {
      if (preference.defaultValue() instanceof KeyCode) {
        putKeyCode(preference, (KeyCode)preference.defaultValue());

        return (KeyCode)preference.defaultValue();
      } else
        return null;
    }
  }

  /**
   * @return the packageName
   */
  public String getPackageName() {
    if (packageName == null) packageName = getFactory().getPackageName();

    return packageName;
  }

  /**
   * @return the preferences
   */
  public Preferences getPreferences() {
    if (preferences == null) preferences = getFactory().getPreferences();

    return preferences;
  }
}
