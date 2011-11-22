package com.grasppe.conres.framework.cases.operations;

import com.grasppe.lure.framework.GrasppeKit.FileSelectionMode;

/**
 * Class description
 *
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCaseFolder extends FileChooserOperation {

    protected static final String	name = "SelectCaseFolder"; 

    protected boolean	executable = true;

    protected final FileSelectionMode	fileSelectionMode =
        FileSelectionMode.DIRECTORIES_ONLY;

    /**
     * Constructs ...
     */
    public SelectCaseFolder() {
        super(name);
    }
}