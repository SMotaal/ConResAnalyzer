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
import java.io.FilenameFilter;
import java.net.URI;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;

import com.grasppe.conres.image.management.model.ImageSpecifications;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * @author daflair
 *
 */
public class ImageFile extends CaseFile {

    protected static FileFilter	fileFilter = new CaseFileFilter(Arrays.asList(new String[] { "*i.tif" }));
    protected static FilenameFilter filenameFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith("i.tif");
		}
    };
    protected float referenceToneValue;

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
     * @return
     */
    @Override
    public boolean validate() {
    	boolean validFile = validateScanImageFile(this);
    	boolean validImage = validateScanImageSpecifications(this, getImageSpecifications());

    	boolean isValid = validFile && validImage;
        
        this.validated = true;
        
        return isValid;
    }
    
    private ImageSpecifications getImageSpecifications() {
    	return new ImageSpecifications();
    }

    /**
     * Check specified image specifications
     * @param file
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
     * @param file
     * @return
     */
    private boolean validateScanImageFile(File file) {
        File	dir       = this.getParentFile();
        String	name      = this.getName();
        String	baseName  = FilenameUtils.getBaseName(name);
        String	extension = FilenameUtils.getExtension(name);
        String suffix = GrasppeKit.lastSplit(name,".").toLowerCase();
        int suffixCode = new Integer(suffix.split("_")[1]);

        return true;
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

        // TODO Auto-generated method stub
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

    /**
     * @param fileFilter
     */
    public static void setFileFilter(FileFilter newFilter) {
       fileFilter = newFilter;
    }
    
    /**
	 * @return the filenameFilter
	 */
	public static FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}

	/**
	 * @param filenameFilter the filenameFilter to set
	 */
	public static void setFilenameFilter(FilenameFilter filenameFilter) {
		ImageFile.filenameFilter = filenameFilter;
	}    
}
