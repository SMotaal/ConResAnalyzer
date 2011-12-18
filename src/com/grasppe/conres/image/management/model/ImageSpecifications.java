/*
 * @(#)ImageSpecifications.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.image.management.model;

import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/12/03
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ImageSpecifications extends AbstractModel {

    protected float	minimumDPI = 600F;

    /**
     */
    public ImageSpecifications() {
    }

    /**
     *  @param controller
     */
    public ImageSpecifications(AbstractController controller) {
        super(controller);
    }
}
