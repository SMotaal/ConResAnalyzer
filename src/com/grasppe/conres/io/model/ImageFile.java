/*
 * @(#)ImageFile.java   11/11/15
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io.model;

import com.grasppe.conres.image.management.model.ImageSpecifications;
import com.grasppe.conres.io.IGrasppeFileReader;
import com.grasppe.conres.io.ImageFileReader;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.resolution.PixelsPerInch;

import org.apache.commons.io.FilenameUtils;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import java.net.URI;

import java.util.Arrays;

/**
 * @author daflair
 */
public class ImageFile extends CaseFile {
	
	int dbg = 3;

    protected static FileFilter	fileFilter = new CaseFileFilter(Arrays.asList(new String[] {
                                                 "*i.tif", "*i.tiff", "*i.png", "*i.jpg" }));
    protected static FilenameFilter	filenameFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
        	String filename = name.toLowerCase().replace("ff", "f");
            return filename.endsWith("i.tif") || filename.endsWith("i.png") || filename.endsWith("i.jpg");
        }
    };
    protected int				channels = 0,
								bitDepth = 0,
								width    = 0,
								height   = 0;
    protected SpatialFrequency	resolution;
    protected double			pixelWidth  = 0,
								pixelHeight = 0;
    protected int				imageID;

//  protected String baseNmae;
//  protected String extension;

    /**
     * @param pathname
     */
    public ImageFile(String pathname) {
        super(pathname);
    }

    /**
     * @param uri
     */
    public ImageFile(URI uri) {
        super(uri);
    }

    /**
     * @param parent
     * @param child
     */
    public ImageFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * @param parent
     * @param child
     */
    public ImageFile(String parent, String child) {
        super(parent, child);
    }

    /**
     *  @return
     */
    public String toString() {
        return super.toString() + "\t" + getChannels() + " x " + getBitDepth() + "-bit x "
               + getWidth() + " x " + getHeight() + " / " + getResolution().value + " "
               + getResolution().getSymbol();
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
        boolean	validFile = validateScanImageFile(this);
        boolean	validImage;

        try {
            validImage = validateScanImageSpecifications(this, getImageSpecifications());
        } catch (Exception e) {
            validImage = false;
            e.printStackTrace();
        }

        boolean	isValid = validFile && validImage;

        this.validated = true;

        return isValid;
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
        String	suffix    = GrasppeKit.lastSplit(name, ".").toLowerCase();

        // String suffixCode = suffix.substring(suffix.indexOf("_")+1,suffix.indexOf(".")-1);
        int	suffixCode = new Integer(suffix.substring(suffix.indexOf("_") + 1,
                             suffix.indexOf(".") - 1)).intValue();		// new Integer(suffix.split("_")[1]);

        setImageID(suffixCode);

        return filenameFilter.accept(dir, name);//name.toLowerCase().replace("ff", "f").endsWith("i.tif") || || name.toLowerCase().endsWith("i.png");		// true;
    }

    /**
     * Check specified image specifications
     * @param file
     * @param specifications
     * @return
     * @throws Exception
     */
    private boolean validateScanImageSpecifications(File file, ImageSpecifications specifications)
            throws Exception {

        if (getResolution() == null) new ImageFileReader(this);

        // TODO: Check color mode: 8-bit gray
        boolean	validChannels = getChannels() == 2;
        boolean	validBitDepth = getBitDepth() >= 8;

        // TODO: Check resolution: 600 dpi minimum
        boolean	validResolution = new PixelsPerInch(getResolution()).getValue() >= 600;

        // TODO: Check against TDF Model target size ±10%
        // TODO: Check against TDF Model dpi > spi * 1.5
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
        return super.verify();
    }

    /**
     *  @return
     */
    public int getBitDepth() {
        return bitDepth;
    }

    /**
     *  @return
     */
    public int getChannels() {
        return channels;
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

    /**
     * @return the filenameFilter
     */
    public static FilenameFilter getFilenameFilter() {
        return filenameFilter;
    }

    /**
     *  @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the imageID
     */
    public int getImageID() {
    	
    	int dbg = 2;
        File	dir       = this.getParentFile();
        String	name      = this.getName();
        String	baseName  = FilenameUtils.getBaseName(name);
        String	extension = FilenameUtils.getExtension(name);
        String	suffix    = GrasppeKit.lastSplit(name, ".").toLowerCase();

        // String suffixCode = suffix.substring(suffix.indexOf("_")+1,suffix.indexOf(".")-1);
        try {
        	int suffixCode = new Integer(baseName.toLowerCase().substring(baseName.length()-3, baseName.length()-1)).intValue();
            setImageID(suffixCode);
            return imageID;
        } catch (Exception exception) {
//        	exception.printStackTrace();
        	GrasppeKit.debugText("GetImageID",exception.getMessage(),dbg);
        }
    	return -1;

    }

    /**
     * @return
     */
    private ImageSpecifications getImageSpecifications() {
        return new ImageSpecifications();
    }

    /**
     *  @return
     */
    public double getPixelHeight() {
        return pixelHeight;
    }

    /**
     *  @return
     */
    public double getPixelWidth() {
        return pixelWidth;
    }

    /**
     *  @return
     */
    @Override
    public IGrasppeFileReader getReader() {
        try {
            return new ImageFileReader(this);
        } catch (Exception exception) {
            exception.printStackTrace();

            return null;
        }
    }

    /**
     *  @return
     */
    public SpatialFrequency getResolution() {
        return resolution;
    }

    /**
     *  @return
     */
    public int getWidth() {
        return width;
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
     *  @param bitDepth
     */
    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    /**
     *  @param channels
     */
    public void setChannels(int channels) {
        this.channels = channels;
    }

    /**
     * @param newFilter
     */
    public static void setFileFilter(FileFilter newFilter) {
        fileFilter = newFilter;
    }

    /**
     * @param filenameFilter the filenameFilter to set
     */
    public static void setFilenameFilter(FilenameFilter filenameFilter) {
        ImageFile.filenameFilter = filenameFilter;
    }

    /**
     *  @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param imageID the imageID to set
     */
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    /**
     *  @param pixelHeight
     */
    public void setPixelHeight(double pixelHeight) {
        this.pixelHeight = pixelHeight;
    }

    /**
     *  @param pixelWidth
     */
    public void setPixelWidth(double pixelWidth) {
        this.pixelWidth = pixelWidth;
    }

    /**
     *  @param resolution
     */
    public void setResolution(SpatialFrequency resolution) {
        this.resolution = resolution;
    }

    /**
     *  @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
