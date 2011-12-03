/*
 * @(#)CaseModel.java   11/12/02
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.model;

import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.conres.io.model.TargetDefinitionFile;
import com.grasppe.lure.framework.GrasppeKit;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseModel {

    /** Field description */
    public String	path;

    /** Field description */
    public String	title;

    /** Field description */
    public CaseFolder	caseFolder;

    /** Field description */
    public TargetDefinitionFile	targetDefinitionFile;

    /** Field description */
    public ImageFile[]	imageFiles;

    /** Field description */
    public boolean	filesLoaded = false;

    /**
     *  @return
     */
    public String toString() {
        String	loadedString = null,
				tdfString    = null,
				imageString  = null,
				pathString   = null,
				returnString = null;

        try {
            loadedString = (filesLoaded) ? "Loaded"
                                         : "";
            tdfString    = (targetDefinitionFile != null) ? " is a " + targetDefinitionFile.getName()
                    : "";
            imageString  = ((imageFiles != null) && (imageFiles.length > 0))
                          ? "with " + imageFiles.length + " image"
                          : "";

            if (imageFiles.length > 1) imageString += "s";

            int	beginIndex = path.lastIndexOf(File.separator,
                                              path.lastIndexOf(File.separator,
                                                  path.lastIndexOf(File.separator) - 2) - 2);
            int	endIndex = path.lastIndexOf(File.separator);	// path.length();

            pathString = "from ..." + path.subSequence(beginIndex, endIndex);
        } catch (Exception exception) {
            if (loadedString == null) loadedString = "";
            if (tdfString == null) tdfString = "";
            if (imageString == null) imageString = "";
            if (pathString == null) pathString = "";
        }

        returnString = GrasppeKit.cat(new String[] { title, tdfString, imageString, loadedString,
                pathString });

        return (returnString);		// title + " - " + loadedString + " (" + path + ")");

    }
}
