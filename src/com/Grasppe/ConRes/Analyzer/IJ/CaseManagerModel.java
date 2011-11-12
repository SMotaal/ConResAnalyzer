package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.GrasppeKit;
import com.Grasppe.ConRes.Analyzer.IJ.CaseManagerModel.CaseModel;
import com.Grasppe.GrasppeKit.AbstractModel;

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

    /**
     * Constructs a new model object with no predefined controller.
     */
    public CaseManagerModel() {
        GrasppeKit.getInstance().super();
    }

    /**
     * Constructs a new model with a predefined controller.
     *
     * @param controller
     */
    public CaseManagerModel(CaseManager controller) {
        GrasppeKit.getInstance().super(controller);
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

    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseModel {

        /** Field description */
        public String	path;

        /** Field description */
        public String	title;
    }
}