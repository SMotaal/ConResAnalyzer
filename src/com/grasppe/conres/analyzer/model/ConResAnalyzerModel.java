package com.grasppe.conres.analyzer.model;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.lure.components.AbstractModel;

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
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     *
     * @param controller
     */
    public ConResAnalyzerModel(ConResAnalyzer controller) {
        super(controller);
    }
}