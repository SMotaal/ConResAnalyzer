package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.GrasppeKit;
import com.Grasppe.GrasppeKit.AbstractModel;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ConResAnalyzerModel extends AbstractModel {

    /**
     * Constructs a new model object with no predefined controller.
     */
    public ConResAnalyzerModel() {
        GrasppeKit.getInstance().super();
    }

    /**
     * Constructs a new model with a predefined controller.
     *
     * @param controller
     */
    public ConResAnalyzerModel(ConResAnalyzer controller) {
        GrasppeKit.getInstance().super(controller);
    }
}