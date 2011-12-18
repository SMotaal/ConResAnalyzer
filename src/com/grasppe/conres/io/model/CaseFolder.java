/*
 * @(#)ImageFile.java   11/11/15
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io.model;

import com.grasppe.conres.io.ImageFileReader;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

import java.net.URI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author daflair
 */
public class CaseFolder extends CaseFile {

    protected static FileFilter	fileFilter = new FileFilter() {

        public boolean accept(File file) {
            return CaseFolder.validate(file);
        }

    };
    HashMap<Integer, ImageFile>		imageFileMap = new HashMap<Integer, ImageFile>();
    protected ImageFile[]			imageFiles;
    protected TargetDefinitionFile	targetDefinitionFile;
    
    int dbg = 0;

    /**
     * @param pathname
     */
    public CaseFolder(String pathname) {
        super(pathname);
    }

    /**
     * @param uri
     */
    public CaseFolder(URI uri) {
        super(uri);
    }

    /**
     * @param parent
     * @param child
     */
    public CaseFolder(File parent, String child) {
        super(parent, child);
    }

    /**
     * @param parent
     * @param child
     */
    public CaseFolder(String parent, String child) {
        super(parent, child);
    }

    /**
     *  @throws FileNotFoundException
     */
    public void enumerateImageFiles() throws FileNotFoundException {
    	
    	File folder = new File(getAbsolutePath()+File.separator+"Images");
    	
    	if (!folder.exists()) folder = this;
    	
        File[]	fileList  = folder.listFiles(ImageFile.getFilenameFilter());
        int		fileCount = fileList.length;

        setImageFiles(new ImageFile[fileCount]);

        HashSet<Integer>	imageToneValues = new HashSet<Integer>();
        HashSet<Integer>	blockToneValues = new HashSet<Integer>();

        for (int value : getTargetDefinitionFile().getBlockToneValues())
            blockToneValues.add(new Integer(value));

        for (int i = 0; i < fileCount; i++) {
            ImageFile	thisFile = new ImageFile(fileList[i].getAbsolutePath());
        	try {
        		thisFile = (new ImageFileReader((ImageFile)thisFile)).getFile();
			} catch (Exception exception) {
				GrasppeKit.debugError("Loading Image File", exception, 2);
			}        	

            setImageFile(i,thisFile);

            if (blockToneValues.contains(thisFile.getImageID())) {
                imageFileMap.put(thisFile.getImageID(), thisFile);
                imageToneValues.add(thisFile.getImageID());
            } else
                GrasppeKit.debugText("Enumerting Case Images",
                                     "The image " + thisFile.getName() + " was not load since "
                                     + "block value " + thisFile.getImageID()
                                     + " was not defined in the target defnition file.", dbg);
        }

        if (!imageToneValues.containsAll(blockToneValues))
            throw new FileNotFoundException(
                "The images for one or more blocks defined in the target definition file are missing or mislabeled.\n\n"
                + "Blocks in TDF: \t" + sortedString(blockToneValues) + "\n" + "Images in Folder: \t"
                + sortedString(imageToneValues));

        return;
    }
    
    private String sortedString(HashSet<Integer> set){
    	try {
    		Integer[] setArray = sortedArray(set);
    		return Arrays.toString(setArray);
    	} catch (Exception exception) {
    		return "";
    	}    	
    }
    
    private Integer[] sortedArray(HashSet<Integer> set){
    	Integer[] setArray = set.toArray(new Integer[0]);
    	
    	try {
    		Arrays.sort(setArray);
    		return setArray;
    	} catch (Exception exception) {
    		return null;
    	}
    	
//    	int[] sortingArray = new int[setArray.length];
//    	
//    	for (int i = 0; i < integerList.size(); i++) {
//    		intArray[i] = integerList.get(i);
//    	
//    	Arrays.
    }

    /**
     */
    public void enumerateTargetDefinitionFiles() {
        File[]	fileList  = listFiles(TargetDefinitionFile.getFilenameFilter());
        int		fileCount = fileList.length;

        if (fileCount == 1)
            setTargetDefinitionFile(new TargetDefinitionFile(fileList[0].getAbsolutePath()));

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

        boolean	isValid = CaseFolder.validate(this);

        this.validated = true;

        return isValid;
    }

    /**
     * @param file
     * @return
     */
    public static boolean validate(File file) {
        boolean	isDirectory = file.isDirectory();

        if (!isDirectory) return false;

        File[]	imageFileList = file.listFiles(ImageFile.getFilenameFilter());
        int		imageCount    = imageFileList.length;
        boolean	hasImages     = imageCount > 0;
        boolean	validImages   = true;

        for (File imageFile : imageFileList) {
            validImages = validImages && new ImageFile(imageFile.getAbsolutePath()).validate();
        }

        int		tdfCount = file.list(TargetDefinitionFile.getFilenameFilter()).length;
        boolean	hasTDF   = tdfCount == 1;

        boolean	isValid  = isDirectory && hasImages && validImages && hasTDF;

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

    /**
     *  @param toneValue
     *  @return
     */
    public ImageFile getImageFile(int toneValue) {
        return imageFileMap.get(toneValue);
    }

    /**
     * @return the imageFiles
     * @throws FileNotFoundException
     */
    public ImageFile[] getImageFiles() throws FileNotFoundException {
        if ((imageFiles == null) || (imageFiles.length == 0)) enumerateImageFiles();

        return imageFiles;
    }

    /**
     * @return the targetDefinitionFile
     */
    public TargetDefinitionFile getTargetDefinitionFile() {
        if (targetDefinitionFile == null) enumerateTargetDefinitionFiles();

        return targetDefinitionFile;
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
        CaseFolder.fileFilter = (fileFilter);
    }

    public void setImageFile(int i, ImageFile imageFile) {
    	this.imageFiles[i] = imageFile;
    }
    /**
     * @param imageFiles the imageFiles to set
     */
    public void setImageFiles(ImageFile[] imageFiles) {
        this.imageFiles = imageFiles;
    }

    /**
     * @param targetDefinitionFile the targetDefinitionFile to set
     */
    public void setTargetDefinitionFile(TargetDefinitionFile targetDefinitionFile) {
        this.targetDefinitionFile = targetDefinitionFile;
    }
}
