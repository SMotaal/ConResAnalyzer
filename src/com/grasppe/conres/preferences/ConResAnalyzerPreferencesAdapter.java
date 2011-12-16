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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author daflair
 *
 */
public class ConResAnalyzerPreferencesAdapter {

    protected static ConResPreferencesFactory	factory     = new ConResPreferencesFactory();
    protected static Preferences				preferences = factory.getPreferences();
    protected static String packageName = factory.getOwnerClass().getPackage().getName();
    
    public static String defaultCasePath = null;
    
    public static final String DEFAULT_CASE_PATH_KEY = "Default Case Path";
    

    /**
     */
    public static void dumpPreferences() {
    	String dumpString = "\n\n\t\t"  + preferences.absolutePath();
        try {
            for (String preferenceKey : preferences.keys()) {
            	dumpString += "\n\t\t\t" + preferenceKey;
    			dumpString += " \t\t" +  preferences.get(preferenceKey, null);
//                GrasppeKit.debugText(packageName + "\t" + preferenceKey, preferences.get(preferenceKey, null), 2);
            }
        } catch (BackingStoreException exception) {
        	dumpString += "<Exception>";
            GrasppeKit.debugError("Enumerating Application Preferences", exception, 2);
            
        }
        
        GrasppeKit.debugText("Preferences " + packageName, dumpString + "\n\n",2);

    }

    /**
     * 	@param args
     */
    public static void main(String[] args) {
    	ConResAnalyzerPreferencesAdapter.dumpPreferences();
    }

    /**
     */
    public static void putDefaultCasePath(String newPath) {
    	preferences.put(DEFAULT_CASE_PATH_KEY, newPath);
    }
    
    public static String getDefaultCasePath() {
		return preferences.get(DEFAULT_CASE_PATH_KEY, "").trim();
    }
}
