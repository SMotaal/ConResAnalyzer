/**
 * 
 */
package com.grasppe.conres.io.model;

import java.io.File;
import java.util.List;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * @author daflair
 *
 */
public class CaseFolderFilter extends WildcardFileFilter {


	/* (non-Javadoc)
	 * @see org.apache.commons.io.filefilter.WildcardFileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File file) {
		boolean isAccepted = true;
		if (!file.isDirectory()) file = file.getParentFile();
		
		File[] imageFiles = file.listFiles(ImageFile.fileFilter);
		File[] tdfFiles = file.listFiles(TargetDefinitionFile.fileFilter);
		
		// TODO: Check TargetDefinitionFile
		isAccepted &= tdfFiles.length == 1;
		
		
		// TODO: 
		isAccepted &= imageFiles.length > 3;
		
		return super.accept(file);
	}

	/**
	 * @param wildcards
	 * @param caseSensitivity
	 */
	protected CaseFolderFilter(List<String> wildcards, IOCase caseSensitivity) {
		super(wildcards, caseSensitivity);
	}

	/**
	 * @param wildcards
	 */
	protected CaseFolderFilter(List<String> wildcards) {
		super(wildcards);
	}

	/**
	 * @param wildcard
	 * @param caseSensitivity
	 */
	protected CaseFolderFilter(String wildcard, IOCase caseSensitivity) {
		super(wildcard, caseSensitivity);
	}

	/**
	 * @param wildcard
	 */
	protected CaseFolderFilter(String wildcard) {
		super(wildcard);
	}

	/**
	 * @param wildcards
	 * @param caseSensitivity
	 */
	protected CaseFolderFilter(String[] wildcards, IOCase caseSensitivity) {
		super(wildcards, caseSensitivity);
	}

	/**
	 * @param wildcards
	 */
	protected CaseFolderFilter(String[] wildcards) {
		super(wildcards);
	}

}
