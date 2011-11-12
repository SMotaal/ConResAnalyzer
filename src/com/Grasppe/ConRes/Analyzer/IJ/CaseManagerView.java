package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.GrasppeKit;
import com.Grasppe.GrasppeKit.AbstractView;

/**
 * Class description
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseManagerView extends AbstractView {

    /**
     * Constructs a new ConResAnalyzerView with a predefined controller.
     *
     * @param controller
     */
    public CaseManagerView(CaseManager controller) {
        GrasppeKit.getInstance().super(controller);
    }
}