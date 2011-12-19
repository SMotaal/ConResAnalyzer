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
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.hamcrest.core.IsAnything;

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

  int dbg = 2;
  /**
   */
  public AbstractPreferencesAdapter() {
    super();
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
      if (value == null)
    	  getPreferences().put(key, defaultValue.toString());
      else 
    	  getPreferences().put(key, value.toString().trim());

    } catch (Exception exception) {
      GrasppeKit.debugError("Writing " + key + "Preferences", exception, 2);
    }
  }

  /**
   *    @param preference
   *    @param value
   */
  public void putArray(IPreferencesEnum preference, Object value) {
	  Object[] objectArray = asObjectArray(value);
	  putArray(preference, objectArray);
  }
  
  public Object[] asObjectArray(Object array) {
	  if (!array.getClass().isArray()) return null;
//	  if (!array.getClass().isPrimitive())
//		  return null;
	  ArrayList<Object>  objectArrayList = new ArrayList<Object>();
	  
	  if(array.getClass().getComponentType()==int.class)
		  for (int item : (int[])array)
			  objectArrayList.add(new Integer(item));
		  else if(array.getClass().getComponentType()==double.class)
			  for (double item : (double[])array)
				  objectArrayList.add(new Double(item));
		  else if(array.getClass().getComponentType()==boolean.class)
			  for (boolean item : (boolean[])array)
				  objectArrayList.add(new Boolean(item));
	  
	  return objectArrayList.toArray();
		  
  }
  /**
   *    @param preference
   *    @param value
   */
  public void putArray(IPreferencesEnum preference, Object[] value) {
    String		key          = preference.key();
    Object[]	defaultValue = asObjectArray(preference.defaultValue());
    
    Object[] array = null;
    
//    if (value instanceof int[]) array = (int[])value;
    
    try {
      if ((value == null) ) //|| ((Array)value.length) == 0))
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

//  /**
//   * @param preference
//   * @return
//   */
//  public Object get(IPreferencesEnum preference) {
//	  
//  }
  /**
   * @param preference
   * @return
   */
  public Object get(IPreferencesEnum preference) {
    String	key          = preference.key();
    Object	defaultValue = preference.defaultValue();
    String	value        = getPreferences().get(key, null);
    Class valueClass = defaultValue.getClass();

//  if (value == null) return defaultValue;
    if (value != null) {

      value = value.trim();

      try {
        if (defaultValue instanceof String) return value;
        else if (defaultValue instanceof int[])
        	return AbstractPreferencesFactory.decodeArrayString(value); //.toArray(new int[0]);
        else if (defaultValue instanceof double[])
        	return AbstractPreferencesFactory.decodeArrayString(value);
        else if (defaultValue instanceof boolean[])
        	return AbstractPreferencesFactory.decodeArrayString(value);
        else if (defaultValue instanceof Object[])
        	return AbstractPreferencesFactory.decodeArrayString(value);
        else if (defaultValue instanceof IIntegerValue)
        	return new Integer(value).intValue();
        else if (valueClass.isPrimitive()) {
          if (valueClass.isAssignableFrom(int.class)) 
        	  return new Integer(value);						// .intValue();
          else if (valueClass.isAssignableFrom(double.class))
        	  return new Double(value);		// .doubleValue();
          else if (valueClass.isAssignableFrom(boolean.class))
                 return new Boolean(Arrays.asList(new String[] { "y", "yes", "true", "t", "1" }).contains(value) ? true
                                                                                                                 : false);
        }
      } catch (Exception exception) {
        GrasppeKit.debugError("Reading " + key + "Preferences", exception, 2);
      }
    } else {
    	
    }
    put(preference, defaultValue);
    GrasppeKit.debugText("Reading " + key + "Preferences", "Using default value", dbg);

    return defaultValue;
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
