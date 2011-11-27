/*
 * @(#)SelectBlockOperation.java   11/11/26
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * Class description
 *
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class SelectBlockOperation extends TargetManagerOperation implements Observer {

    protected static final String	name = "SelectBlock";
    AbstractModel					model;

    /**
     */
    public SelectBlockOperation() {
        super(name);
    }

    /**
     * 	@param controller
     */
    public SelectBlockOperation(CornerSelector controller) {
        this();
        setModel(controller.getModel());
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractOperation#perfomOperation()
     */

    /**
     * 	@return
     */
    @Override
    protected boolean perfomOperation() {

        // TODO Auto-generated method stub
        return super.perfomOperation();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.framework.GrasppeKit.Observer#update()
     */

    /**
     */
    public void update() {

        // TODO Auto-generated method stub

    }

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public TargetManagerModel getModel() {
        return (TargetManagerModel)model;
    }

    /**
     * Attaches the command to the specified model and calls update() to reflect the state of the model.
     * @param model
     */
    public void setModel(AbstractModel model) {
        model.attachObserver(this);
        this.model = model;
        update();
    }
}
