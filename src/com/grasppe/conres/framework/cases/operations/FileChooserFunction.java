/*
 * @(#)FileChooserFunction.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.cases.operations;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.conres.framework.cases.model.CaseManagerModel;
import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.conres.preferences.Preferences;
import com.grasppe.conres.preferences.Preferences.Tags;
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
 * @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public abstract class FileChooserFunction extends CaseManagerFunction {

  String							defaultChooserPath = (String)Preferences.get(Tags.DEFAULT_CASE_PATH);
  File								selectedFile;
  protected boolean		executable = true;
  JFileChooser				fileChooser;
  FileSelectionMode		fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
  TreeSet<FileFilter>	filters           = new TreeSet<FileFilter>();
  int									dbg               = 0;

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
    return IJ.showMessageWithCancel(name, "This is not a valid selection. Please make a valid selection.");
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
      if (fileChooser.showOpenDialog(GrasppeKit.commonFrame) == JFileChooser.CANCEL_OPTION) return false;

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

//      
//  System.setProperty("apple.awt.fileDialogForDirectories", "true");
//  FileDialog d = new FileDialog(frame) {
//      
//  };

//  fileChooser = new JFileChooser();
//  if (!GrasppeKit.isRunningJar()) {
    fileChooser = new JFileChooser() {		// new File(".")) {

      /*
       *  (non-Javadoc)
       * @see javax.swing.JFileChooser#setCurrentDirectory(java.io.File)
       */
      @Override
      public void setCurrentDirectory(File dir) {

        // TODO Auto-generated method stub
        try {
          if (dir != null) {		// ))) {
            CaseFolder	caseFolder = new CaseFolder(dir.getAbsolutePath());

            CaseManager.verifyCaseFolder(caseFolder);
            setSelectedFile(dir);
            super.approveSelection();

            return;
          }
        } catch (Exception exception) {}

        super.setCurrentDirectory(dir);
      }

      public void approveSelection() {
        try {
          CaseFolder	caseFolder = new CaseFolder(getSelectedFile().getAbsolutePath());
          CaseManager.verifyCaseFolder(caseFolder);
          super.approveSelection();
        } catch (Exception exception) {
          setCurrentDirectory(getSelectedFile());
        }

//      if (getSelectedFile().isDirectory()) {
//          try {
////              String                  folderPath           = getSelectedFile().getAbsolutePath();
////              CaseFolder              caseFolder           = new CaseFolder(folderPath);
////              TargetDefinitionFile    targetDefinitionFile =
////                  caseFolder.getTargetDefinitionFile();
////              TargetDefinitionReader  reader =
////                  new TargetDefinitionReader(targetDefinitionFile);
////              ImageFile[] imageFiles = caseFolder.getImageFiles();
//            
//            if (!CaseManager.verifyCaseFolder(new CaseFolder(getSelectedFile().getAbsolutePath())))
//                setCurrentDirectory(getSelectedFile());
//            else 
//                super.approveSelection();
//          } catch (Exception exception) {
//              setCurrentDirectory(getSelectedFile());
//          }
//
//          return;
//      } else
        
      }
    };
    fileChooser.setFileSelectionMode(fileSelectionMode.value());

    // Add filters
    while (filters.iterator().hasNext())
      fileChooser.addChoosableFileFilter(filters.iterator().next());

    // Setting initial chooser selection
    try {
      String	defaultPathString = "";

      if (!getDefaultChooserPath().isEmpty()) defaultPathString = getDefaultChooserPath();
      else if (!GrasppeKit.isRunningJar()) defaultPathString = CaseManagerModel.defaultChooserPath;

      if (!defaultPathString.isEmpty()) {
        File	defaultPath = new File(defaultPathString);

        fileChooser.setCurrentDirectory(defaultPath.getAbsoluteFile());

//      fileChooser.setSelectedFile(defaultPath.get);
      }
    } catch (NullPointerException exception) {}
  }

  /**
   * @return
   */
  public boolean quickSelect() {
    boolean	canProceed = false;
    String	finalName  = GrasppeKit.humanCase(getName());

    try {
      canProceed = execute(true);
    } catch (Exception exception) {
      GrasppeKit.debugError("Selecing Case Folder", exception, 3);
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
