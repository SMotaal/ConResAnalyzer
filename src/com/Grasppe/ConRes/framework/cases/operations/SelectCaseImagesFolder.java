package com.grasppe.conres.framework.cases.operations;

import java.io.File;

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
		return super.verifySelection(selectedFile);
	}

	protected static final String	name = "SelectCaseFolder";

    protected static final String	defaultChooserPath =
        "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";

    protected boolean	executable = true;

    protected final FileSelectionMode	fileSelectionMode =
        FileSelectionMode.DIRECTORIES_ONLY;

    /**
     * Constructs ...
     */
    public SelectCaseImagesFolder() {
        super(name);
    }
    
    
}