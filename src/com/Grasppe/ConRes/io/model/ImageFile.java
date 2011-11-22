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

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileFilter;

import java.net.URI;

import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.grasppe.conres.image.management.model.ImageSpecifications;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 *
 */
public class ImageFile extends CaseFile {

    protected FileFilter	fileFilter = new CaseFileFilter(Arrays.asList(new String[] { "*i.tif" }));

    /**
     * @param pathname
     */
    protected ImageFile(String pathname) {
        super(pathname);
    }

    /**
     * @param uri
     */
    protected ImageFile(URI uri) {
        super(uri);
    }

    /**
     * @param parent
     * @param child
     */
    protected ImageFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * @param parent
     * @param child
     */
    protected ImageFile(String parent, String child) {
        super(parent, child);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#validate()
     */

    /**
     *
     * @return
     */
    @Override
    public boolean validate() {
        validateScanImageFile(this);
        validateScanImageSpecifications(this, getImageSpecifications());

        return super.validate();
    }
    
    private ImageSpecifications getImageSpecifications() {
    	return new ImageSpecifications();
    }

    /**
     * Check specified image specifications
     * @param file
     *
     * @return
     */
    private boolean validateScanImageSpecifications(File file, ImageSpecifications specifications) {

        // TODO: Check color mode: 8-bit gray
        // TODO: Check resolution: 600 dpi minimum
        // TODO: Check against TDF Model target size ±10%
        // TODO: Check against TDF Model dpi > spi * 1.5
        return true;
    }

    /**
     * Method description
     *
     * @param file
     *
     * @return
     */
    private boolean validateScanImageFile(File file) {
        File	dir       = this.getParentFile();
        String	name      = this.getName();
        String	baseName  = FilenameUtils.getBaseName(name);
        String	extension = FilenameUtils.getExtension(name);
        String suffix = GrasppeKit.lastSplit(name,".").toLowerCase();
        int suffixCode = new Integer(suffix.split("_")[1]);
        //String suffixText = suffix.

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#verify()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    public boolean verify() {

        // TODO Auto-generated method stub
        return super.verify();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#getFileFilter()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    public FileFilter getFileFilter() {

        // TODO Auto-generated method stub
        return super.getFileFilter();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isValidated()
     */

    /**
     * Method description
     *
     * @return
     */
    @Override
    public boolean isValidated() {

        // TODO Auto-generated method stub
        return super.isValidated();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.CaseFiles.model.CaseFile#isVerified()
     */

    /**
     * Method description
     *
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
     * Method description
     *
     * @param fileFilter
     */
    @Override
    public void setFileFilter(FileFilter fileFilter) {

        // TODO Auto-generated method stub
        super.setFileFilter(fileFilter);
    }
}
