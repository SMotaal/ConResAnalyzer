/*
 * @(#)SelectCaseFolder.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import javax.swing.JFileChooser;

import com.grasppe.lure.framework.GrasppeKit.FileSelectionMode;

import ij.IJ;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCaseFolder extends FileChooserFunction {

    protected static final String		name              = "SelectCaseFolder";
    protected boolean					executable        = true;
    protected final FileSelectionMode	fileSelectionMode = FileSelectionMode.DIRECTORIES_ONLY;

    /**
     */
    public SelectCaseFolder() {
        super(name);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
}
