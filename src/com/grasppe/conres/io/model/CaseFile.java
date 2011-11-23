/*
 * @(#)CaseFile.java   11/11/14
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



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
public abstract class CaseFile extends File {

    protected boolean		verified, validated;
    protected FileFilter	fileFilter = new FileFilter() {

        public boolean accept(File pathname) {
            return false;
        }
    };

    /**
     * @param pathname
     */
    public CaseFile(String pathname) {
        super(pathname);
    }

    /**
     * @param uri
     */
    public CaseFile(URI uri) {
        super(uri);
    }

    /**
     * @param parent
     * @param child
     */
    public CaseFile(File parent, String child) {
        super(parent, child);

        // TODO Auto-generated constructor stub
    }

    /**
     * @param parent
     * @param child
     */
    public CaseFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean validate() {
        this.validated = false;

        return isValidated();
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean verify() {
        this.verified = false;

        return isVerified();
    }

    /**
     * @return the fileFilter
     */
    public FileFilter getFileFilter() {
        return fileFilter;
    }

    /**
     * @return the validated
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * @return the verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * @param fileFilter the fileFilter to set
     */
    public void setFileFilter(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }
}
