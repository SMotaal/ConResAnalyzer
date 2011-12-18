/*
 * @(#)CaseFile.java   11/11/14
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io.model;

import com.grasppe.conres.io.IGrasppeFileReader;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import java.net.URI;

/**
 * Verification is a quality control process that is used to evaluate whether a product,
 * service, or system complies with regulations, specifications, or conditions imposed at
 * the start of a development phase. Verification can be in development, scale-up, or
 * production. This is often an internal process.
 * Validation is a quality assurance process of establishing evidence that provides a high
 * degree of assurance that a product, service, or system accomplishes its intended
 * requirements. This often involves acceptance of fitness for purpose with end users and
 * other product stakeholders. This is often an external process.
 * {@link http://en.wikipedia.org/wiki/Verification_and_validation}
 * @author daflair
 */
public abstract class CaseFile extends File {

    protected static FileFilter	fileFilter = new FileFilter() {

        public boolean accept(File pathname) {
            return false;
        }
    };
    protected static FilenameFilter	filenameFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return false;
        }
    };
    protected boolean	verified, validated;

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
    }

    /**
     * @param parent
     * @param child
     */
    public CaseFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * "Are you building the right thing?"
     * @return
     */
    public boolean validate() {
        boolean	isValid = true;

        this.validated = true;

        return isValid;
    }

    /**
     * "Are you building it right?"
     * @return
     */
    public boolean verify() {
        boolean	isVerified = true;

        this.verified = true;

        return isVerified;
    }

    /**
     * @return the fileFilter
     */
    public static FileFilter getFileFilter() {
        return fileFilter;
    }

    /**
     * @return the filenameFilter
     */
    public static FilenameFilter getFilenameFilter() {
        return filenameFilter;
    }

    /**
     *  @return
     */
    public IGrasppeFileReader getReader() {
        return null;
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
     *  @param newFilter
     */
    public static void setFileFilter(FileFilter newFilter) {
        fileFilter = newFilter;
    }

    /**
     * @param filenameFilter the filenameFilter to set
     */
    public static void setFilenameFilter(FilenameFilter filenameFilter) {
        CaseFile.filenameFilter = filenameFilter;
    }
}
