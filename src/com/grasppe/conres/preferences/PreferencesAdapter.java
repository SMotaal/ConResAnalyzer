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

import com.grasppe.lure.components.AbstractPreferencesAdapter;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------


/**
 * @author daflair
 *
 */
public class PreferencesAdapter extends AbstractPreferencesAdapter {

    protected static PreferencesFactory	factory = new PreferencesFactory();

    /** Field description */
    public static String	defaultCasePath = null;

    /** Field description */
    public static final String
		DEFAULT_CASE_PATH_KEY = "Default Case Path",
		RECENT_CASES_KEY      = "Recent Cases";

    /** Field description */
    public static final String	FIELD_SEPARATOR = GrasppeKit.FIELD_SEPARATOR;
//    
    protected static PreferencesAdapter preferencesAdapter = new PreferencesAdapter();
    
    public static PreferencesAdapter getInstance() {
    	return preferencesAdapter;
    }
    /**
     *
     */
    public PreferencesAdapter() {
        super();
    }
//
//    /**
//     *  @param newPath
//     */
//    public void putDefaultCasePath(String newPath) {
//        getPreferences().put(DEFAULT_CASE_PATH_KEY, newPath);
//    }
//
//    /**
//     *  @return
//     */
//    public String getDefaultCasePath() {
//        return getPreferences().get(DEFAULT_CASE_PATH_KEY, "").trim();
//    }
    
//    public 

    /**
     * @return the factory
     */
    public PreferencesFactory getFactory() {
        return factory;
    }
}
