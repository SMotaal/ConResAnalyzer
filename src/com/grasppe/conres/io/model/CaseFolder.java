/**
 * 
 */
package com.grasppe.conres.io.model;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;

/**
 * @author daflair
 *
 */
public class CaseFolder extends CaseFile {
	
    protected FileFilter fileFilter = new FileFilter() {
		public boolean accept(File pathname) {
			if (!pathname.isDirectory()) return false;
			return true;
		}
    };	
	/**
	 * @param pathname
	 */
	public CaseFolder(String pathname) {
		super(pathname);
	}

	/**
	 * @param uri
	 */
	public CaseFolder(URI uri) {
		super(uri);
	}

	/**
	 * @param parent
	 * @param child
	 */
	public CaseFolder(File parent, String child) {
		super(parent, child);
	}

	/**
	 * @param parent
	 * @param child
	 */
	public CaseFolder(String parent, String child) {
		super(parent, child);
	}

}
