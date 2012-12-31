/*
 * @(#)AnalysisCommand.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.matlab.operations;

import com.grasppe.conres.framework.analysis.AnalysisManager;
import com.grasppe.conres.framework.analysis.model.AnalysisManagerModel;
import com.grasppe.conres.framework.matlab.MatLabManager;
import com.grasppe.conres.framework.matlab.model.MatLabManagerModel;
import com.grasppe.lure.components.AbstractCommand;
import com.mathworks.jmi.types.MLArrayRef;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionListener;

/**
 * Defines Case Manager's Close Case actions and command, using the EAC pattern.
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class MatLabCommand extends AbstractCommand {

    /**
     * Constructs a realization of AbstractCommand.
     * @param listener
     * @param name
     */
    public MatLabCommand(ActionListener listener, String name) {
        super(listener, name, false);
        setModel(((MatLabManager)listener).getModel());
    }

    /**
     * Returns the correctly-cast model.
     * @return
     */
    @Override
    public MatLabManagerModel getModel() {
        return (MatLabManagerModel)model;
    }
    
    public boolean hasMatLabController(){
    	return getManager().hasMatLabController();
//    	MLArrayRef matlabController = getMatLabController();
//    	
////    	System.out.println(matlabController);
////    	System.out.println(matlabController.isObject());
////    	System.out.println(matlabController.isUnknown());
////    	System.out.println(matlabController.getN());
////    	System.out.println(matlabController.getM());
////    	System.out.println(matlabController.getNDimensions());
////    	System.out.println(matlabController.getData());
//    	
//    	// (matlabController.isObject() | matlabController.isUnknown()) & 
//    	
//    	return matlabController.getN()==1 & matlabController.getM()==1 & matlabController.getNDimensions()==2; 
    	
    }
    
    public com.mathworks.jmi.types.MLArrayRef getMatLabController() {
    	return getManager().getMatLabController();
    }
    
    public MatLabManager getManager(){
    	return (MatLabManager)actionListener;
    }
}
