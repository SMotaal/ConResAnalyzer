/*
 * @(#)CaseFolderFilter.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io.model;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;

/**
 * @author daflair
 */
public class CaseFolderFilter extends WildcardFileFilter {

    /**
     * @param wildcards
     */
    protected CaseFolderFilter(List<String> wildcards) {
        super(wildcards);
    }

    /**
     * @param wildcard
     */
    protected CaseFolderFilter(String wildcard) {
        super(wildcard);
    }

    /**
     * @param wildcards
     */
    protected CaseFolderFilter(String[] wildcards) {
        super(wildcards);
    }

    /**
     * @param wildcards
     * @param caseSensitivity
     */
    protected CaseFolderFilter(List<String> wildcards, IOCase caseSensitivity) {
        super(wildcards, caseSensitivity);
    }

    /**
     * @param wildcard
     * @param caseSensitivity
     */
    protected CaseFolderFilter(String wildcard, IOCase caseSensitivity) {
        super(wildcard, caseSensitivity);
    }

    /**
     * @param wildcards
     * @param caseSensitivity
     */
    protected CaseFolderFilter(String[] wildcards, IOCase caseSensitivity) {
        super(wildcards, caseSensitivity);
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.commons.io.filefilter.WildcardFileFilter#accept(java.io.File)
     */

    /**
     *  @param file
     *  @return
     */
    @Override
    public boolean accept(File file) {
        boolean	isAccepted = true;

        if (!file.isDirectory()) file = file.getParentFile();

        File[]	imageFiles = CaseFolder.listFilesRecursively(file,ImageFile.fileFilter,2);
        File[]	tdfFiles   = CaseFolder.listFilesRecursively(file,TargetDefinitionFile.fileFilter,2);

        // TODO: Check TargetDefinitionFile
        isAccepted &= tdfFiles.length == 1;

        isAccepted &= imageFiles.length > 3;

        return super.accept(file);
    }
}
