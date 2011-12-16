/*
 * @(#)GrasppePreferencesFactory.java   11/12/16
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.lure.framework;

import com.apple.eio.FileManager;

import net.infotrek.util.prefs.FilePreferencesFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author daflair
 *
 */
public abstract class GrasppePreferencesFactory extends FilePreferencesFactory {

    static int	dbg = 2;

    /**
     * 	@param fileName
     * 	@return
     */
    protected static String getMacUserPreferencesPath(String fileName) {
        String	filePath = null;

        if (!GrasppeKit.OperatingSystem.isMac()) return filePath;

        try {
            filePath = FileManager.findFolder(FileManager.kUserDomain,
                                              FileManager.OSTypeToInt("pref"));
        } catch (FileNotFoundException exception) {
            filePath = GrasppeKit.cat(new String[] { System.getProperty("user.home"), "Library",
                    "Preferences" }, File.separator);
        }

        if (fileName != null) filePath += File.separator + fileName;

        return filePath;
    }

    /**
     * 	@param ownerClass
     * 	@return
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
    
    public String getUserPreferencesFile() {
    	return getPreferencesFileByClass(getOwnerClass());
    }
    
    public abstract Class<?> getFactoryClass();
    public abstract Class<?> getOwnerClass();
}
