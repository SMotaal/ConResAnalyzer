package com.grasppe.conres.framework.targets.model;

import java.io.File;

import com.grasppe.conres.framework.targets.TargetEdgeDetector;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 *
 * @version $Revision: 0.1, 11/11/08
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class TargetEdgeDetectorModel extends AbstractModel {
	
	/* Sample target image */
	protected File targetImageFile;
	
	/* Target Definition File */
	protected File targetDefinitionFile;
	
	/* Physical dimensions per target definition file*/
	protected TargetDimensions targetDimensions;
	
	/* Pixel dimensions per Sample target image */
	protected TargetDimensions imageDimensions;

    /**
     * Constructs a new model object with no predefined controller.
     */
    public TargetEdgeDetectorModel() {
        super();
    }

    /**
     * Constructs a new model with a predefined controller.
     * @param controller
     */
    public TargetEdgeDetectorModel(TargetEdgeDetector controller) {
        super(controller);
    }
}