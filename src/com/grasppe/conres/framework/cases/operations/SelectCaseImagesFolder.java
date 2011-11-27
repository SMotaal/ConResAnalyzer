package com.grasppe.conres.framework.cases.operations;

import ij.IJ;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.grasppe.conres.io.model.ImageFolder;
import com.grasppe.lure.framework.GrasppeKit.FileSelectionMode;

/**
 * Class description
 *
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCaseImagesFolder extends FileChooserOperation {

    /* (non-Javadoc)
	 * @see com.grasppe.conres.framework.cases.operations.FileChooserOperation#verifySelection(java.io.File)
	 */
	@Override
	protected boolean verifySelection(File selectedFile) {
		if (!selectedFile.isDirectory()) selectedFile = selectedFile.getParentFile();
		boolean isVerified = ImageFolder.validate(selectedFile);
		return isVerified;  //super.verifySelection(selectedFile);
	}

	protected static final String	name = "SelectCaseImagesFolder";

//    protected static final String	defaultChooserPath =
//        "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS";

    protected boolean	executable = true;

    protected final FileSelectionMode	fileSelectionMode =
        FileSelectionMode.DIRECTORIES_ONLY;

    /**
     */
    public SelectCaseImagesFolder() {
        super(name);
    }
    
    @Override
    protected boolean confirmSelectionInvalid() {
    	return IJ.showMessageWithCancel(name,
                "This is not a case images folder.\n\n" +
                "Please select a folder and ensure that the scanned images \n" +
                "filenames end with *i.tif.");
    }
    
//    /**
//	 * @return the defaultChooserPath
//	 */
//	public String getDefaultChooserPath() {
//		return defaultChooserPath;
//	}
    
}