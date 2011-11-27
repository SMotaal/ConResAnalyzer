package com.grasppe.conres.framework.cases.model;

import com.grasppe.conres.framework.cases.CaseManager;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.framework.GrasppeKit;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManagerModel extends AbstractModel {

    /** Holds a case that is about to close until currentCase is not null */
    public CaseModel	backgroundCase = null;

    /** Field description */
    public CaseModel	currentCase = null;

    /** Field description */
    public CaseModel	newCase = null;
    
    public static String	defaultChooserPath =
            "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS";

    /**
     * Constructs a new model object with no predefined controller.
     */
    public CaseManagerModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     *
     * @param controller
     */
    public CaseManagerModel(CaseManager controller) {
        super(controller);
    }

    /**
     * Method description
     *
     * @return
     */
    public CaseModel newCaseModel() {
        return new CaseModel();
    }

    /**
     * Method description
     *
     * @return
     */
    public boolean hasCurrentCase() {
        if (currentCase != null)
            GrasppeKit.debugText("Current Case", currentCase.toString(), 3);
        else GrasppeKit.debugText("Current Case", "null!", 3);

        return (currentCase != null);
    }
}