/*
 * @(#)FileChooserFunction.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.FileSelectionMode;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/10
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class FileChooserFunction extends CaseManagerFunction {

    String				defaultChooserPath = CaseManagerModel.defaultChooserPath;
    File				selectedFile;
    protected boolean	executable = true;
    JFileChooser		fileChooser;
    FileSelectionMode	fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
    TreeSet<FileFilter>	filters           = new TreeSet<FileFilter>();

    /**
     * @param name
     */
    public FileChooserFunction(String name) {
        super(name);
        prepareFileChooser();
    }

    /**
     *  @return
     */
    protected boolean confirmSelectionInvalid() {
        return IJ.showMessageWithCancel(
            name, "This is not a valid selection. Please make a valid selection.");
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.GrasppeKit.AbstractOperation#perfomOperation()
     */

    /**
     * @return
     */
    @Override
    protected boolean perfomOperation() {
        prepareFileChooser();

        boolean	canProceed = false;

        while (!canProceed) {
            if (fileChooser.showOpenDialog(GrasppeKit.commonFrame) == JFileChooser.CANCEL_OPTION)
                return false;

            if (verifySelection(fileChooser.getSelectedFile())) canProceed = true;
            else if (!confirmSelectionInvalid()) return false;
        }

        // TODO: Inspect & Verify Scan Images / TDF are in selectedFile
        selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.isDirectory()) selectedFile = selectedFile.getParentFile();

        return true;
    }

    /**
     */
    public void prepareFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(fileSelectionMode.value());

        // Add filters
        while (filters.iterator().hasNext())
            fileChooser.addChoosableFileFilter(filters.iterator().next());

        // Setting initial chooser selection
        try {
            File	defaultPath = new File(getDefaultChooserPath());

            fileChooser.setSelectedFile(defaultPath);
        } catch (NullPointerException e) {
            e.printStackTrace();

            // Not setting initial chooser selection
        }
    }

    /**
     * @return
     */
    public boolean quickSelect() {
        boolean	canProceed = false;
        String	finalName  = GrasppeKit.humanCase(getName());

        try {
            canProceed = execute(true);
        } catch (Exception e) {
            GrasppeKit.debugText(finalName + " Failed",
                                 finalName + " threw a " + e.getClass().getSimpleName() + "\n\n"
                                 + e.toString(), 2);
            e.printStackTrace();
        }

        return canProceed;
    }

    /**
     *  @param selectedFile
     *  @return
     */
    protected boolean verifySelection(File selectedFile) {
        return true;
    }

    /**
     * @return the defaultChooserPath
     */
    public String getDefaultChooserPath() {
        return defaultChooserPath;
    }

    /**
     * @return the selectedFile
     */
    public File getSelectedFile() {
        return selectedFile;
    }

    /**
     * @param selectedFile the selectedFile to set
     */
    protected void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }
}
