/*
 * @(#)CaseFileFilter.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io.model;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * @author daflair
 */
public class CaseFileFilter extends WildcardFileFilter {

    /**
     * @param wildcards
     */
    protected CaseFileFilter(List<String> wildcards) {
        super(wildcards);
    }

    /**
     * @param wildcard
     */
    protected CaseFileFilter(String wildcard) {
        super(wildcard);
    }

    /**
     * @param wildcards
     */
    protected CaseFileFilter(String[] wildcards) {
        super(wildcards);
    }

    /**
     * @param wildcards
     * @param caseSensitivity
     */
    protected CaseFileFilter(List<String> wildcards, IOCase caseSensitivity) {
        super(wildcards, caseSensitivity);
    }

    /**
     * @param wildcard
     * @param caseSensitivity
     */
    protected CaseFileFilter(String wildcard, IOCase caseSensitivity) {
        super(wildcard, caseSensitivity);
    }

    /**
     * @param wildcards
     * @param caseSensitivity
     */
    protected CaseFileFilter(String[] wildcards, IOCase caseSensitivity) {
        super(wildcards, caseSensitivity);
    }
}
