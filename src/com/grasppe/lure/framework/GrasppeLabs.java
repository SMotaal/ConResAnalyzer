/**
 * 
 */
package com.grasppe.lure.framework;

import java.io.FileNotFoundException;

import com.apple.eio.FileManager;

/**
 * @author daflair
 *
 */
public class GrasppeLabs {
	
	static int dbg = 2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 */
	public static void macFileManagerExample() {
	    String[]	osTypeStrings = new String[] { "pref", "docs", "apps", "macs" };
	
	    for (String osTypeString : osTypeStrings) {
	        try {
	            GrasppeKit.debugText(osTypeString,
	                                 FileManager.findFolder(FileManager.kUserDomain,
	                                     FileManager.OSTypeToInt(osTypeString)), dbg);
	        } catch (FileNotFoundException exception) {
	            GrasppeKit.debugText(osTypeString, exception.getMessage(), dbg);
	        }
	    }
	}

}
