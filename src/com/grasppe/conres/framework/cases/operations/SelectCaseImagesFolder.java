/*
 * @(#)SelectCaseImagesFolder.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.io.model.ImageFolder;
import com.grasppe.lure.framework.GrasppeKit.FileSelectionMode;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCaseImagesFolder extends FileChooserFunction {

    protected static final String	name = "SelectCaseImagesFolder";

//  protected static final String defaultChooserPath =
//      "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS";

    protected boolean					executable        = true;
    protected final FileSelectionMode	fileSelectionMode = FileSelectionMode.DIRECTORIES_ONLY;

    /**
     */
    public SelectCaseImagesFolder() {
        super(name);
    }

    /**
     *  @return
     */
    @Override
    protected boolean confirmSelectionInvalid() {
        return IJ.showMessageWithCancel(
            name,
            "This is not a case images folder.\n\n"
            + "Please select a folder and ensure that the scanned images \n"
            + "filenames end with *i.tif.");
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.conres.framework.cases.operations.FileChooserFunction#verifySelection(java.io.File)
     */

    /**
     *  @param selectedFile
     *  @return
     */
    @Override
    protected boolean verifySelection(File selectedFile) {
        if (!selectedFile.isDirectory()) selectedFile = selectedFile.getParentFile();

        boolean	isVerified = ImageFolder.validate(selectedFile);

        return isVerified;		// super.verifySelection(selectedFile);
    }

//  /**
//   * @return the defaultChooserPath
//   */
//  public String getDefaultChooserPath() {
//    return defaultChooserPath;
//  }

}
