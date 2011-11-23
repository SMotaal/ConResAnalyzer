package com.grasppe.conres.framework.targets;

import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.lure.components.AbstractController;

/**
 * Class description
 *
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetManager extends AbstractController {

    /**
     * Constructs and attaches a new controller and a new model.
     */
    public TargetManager() {
        this(new TargetManagerModel());
    }

    /**
     * Constructs a new controller and attaches it to the unattached model.
     *
     * @param model
     */
    public TargetManager(TargetManagerModel model) {
        super(model);
    }
}