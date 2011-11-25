/**
 * 
 */
package com.grasppe.conres.io.model;

import java.util.List;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * @author daflair
 *
 */
public class CaseFileFilter extends WildcardFileFilter {


	/**
	 * @param wildcards
	 * @param caseSensitivity
	 */
	protected CaseFileFilter(List<String> wildcards, IOCase caseSensitivity) {
		super(wildcards, caseSensitivity);
	}

	/**
	 * @param wildcards
	 */
	protected CaseFileFilter(List<String> wildcards) {
		super(wildcards);
	}

	/**
	 * @param wildcard
	 * @param caseSensitivity
	 */
	protected CaseFileFilter(String wildcard, IOCase caseSensitivity) {
		super(wildcard, caseSensitivity);
	}

	/**
	 * @param wildcard
	 */
	protected CaseFileFilter(String wildcard) {
		super(wildcard);
	}

	/**
	 * @param wildcards
	 * @param caseSensitivity
	 */
	protected CaseFileFilter(String[] wildcards, IOCase caseSensitivity) {
		super(wildcards, caseSensitivity);
	}

	/**
	 * @param wildcards
	 */
	protected CaseFileFilter(String[] wildcards) {
		super(wildcards);
	}

}
