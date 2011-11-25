/*
 * @(#)ImageFile.java   11/11/15
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
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import java.net.URI;

/**
 * @author daflair
 *
 */
public class ImageFolder extends CaseFile {

    protected static FileFilter	fileFilter = new FileFilter() {

        public boolean accept(File file) {
            return ImageFolder.validate(file);
        }

    };
    protected float	referenceToneValue;

    /**
     * @param pathname
     */
    protected ImageFolder(String pathname) {
        super(pathname);
    }

    /**
     * @param uri
     */
    protected ImageFolder(URI uri) {
        super(uri);
    }

    /**
     * @param parent
     * @param child
     */
    protected ImageFolder(File parent, String child) {
        super(parent, child);
    }

    /**
     * @param parent
     * @param child
     */
    protected ImageFolder(String parent, String child) {
        super(parent, child);
    }

    /**
     * @param args
     * @throws Exception
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, Exception {
        String		testFile = "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS";

        ImageFolder	file     = new ImageFolder(testFile);

        boolean		isValid  = file.validate();

        System.out.println(file.toString() + " is valid: " + isValid);

    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#validate()
     */

    /**
     * @return
     */
    @Override
    public boolean validate() {

        boolean	isValid = ImageFolder.validate(this);

        this.validated = true;

        return isValid;
    }

    /**
     * Method description
     *
     * @param file
     *
     * @return
     */
    public static boolean validate(File file) {
        boolean	isDirectory = file.isDirectory();

        if (!isDirectory) return false;

        int		imageCount = file.list(ImageFile.getFilenameFilter()).length;
        boolean	hasImages  = imageCount > 0;

        int		tdfCount = file.list(TargetDefinitionFile.getFilenameFilter()).length;
        boolean	hasTDF   = tdfCount == 1;

        boolean	isValid  = isDirectory && hasImages && hasTDF;

        return isValid;

    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#verify()
     */

    /**
     * Inspect image characteristics to determine if the image is what is expected
     * @return
     */
    @Override
    public boolean verify() {
        return super.verify();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#getFileFilter()
     */

    /**
     * @return
     */
    public static FileFilter getFileFilter() {
        return fileFilter;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isValidated()
     */

    /**
     * @return
     */
    @Override
    public boolean isValidated() {
        return super.isValidated();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isVerified()
     */

    /**
     * @return
     */
    @Override
    public boolean isVerified() {

        // TODO Auto-generated method stub
        return super.isVerified();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#setFileFilter(java.io.FileFilter)
     */

    /**
     * @param fileFilter
     */
    public static void setFileFilter(FileFilter fileFilter) {
        ImageFolder.fileFilter = (fileFilter);
    }
}
