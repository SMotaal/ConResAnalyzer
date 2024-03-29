/*
 * @(#)SelectBlockFunction.java   11/11/26
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.conres.framework.targets.model.grid.ConResBlock;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.components.ObservableObject;
import com.grasppe.lure.framework.FloatingAlert;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import com.sun.snippets.ListDialog;

import java.util.HashMap;

import javax.sound.midi.ControllerEventListener;
import javax.swing.JFrame;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public class SelectBlockFunction extends TargetManagerFunction implements Observer {

    /**
	 * @return the controller
	 */
	protected TargetManager getController() {
		return controller;
	}

	protected static final String	name = "SelectBlock";
    AbstractModel					model;
    TargetManager controller;

    /**
     */
    public SelectBlockFunction() {
        super(name);
    }

    /**
     *  @param controller
     */
    public SelectBlockFunction(TargetManager controller) {
        this();
        setModel(controller.getModel());
        this.controller = controller;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.lure.components.AbstractFunction#perfomOperation()
     */

    /**
     *  @return
     */
    @Override
    protected boolean perfomOperation() {

        // return super.perfomOperation();
        // int       i           = 0;
        // TODO: Enumerate TargetManager's ActiveTarget's Blocks
        ConResBlock[]					blocks      = getModel().getActiveTarget().getTargetBlocks();

        ConResBlock						activeBlock = getModel().getActiveBlock();
        String							activeItem  = "";

        HashMap<String, ConResBlock>	targetBlockMap    = new HashMap<String, ConResBlock>();

        String							listItems[] = new String[blocks.length];
        
        String listLabel = "<html>Select Block:</html>";
        
        boolean validTDF = getModel().getController().getCaseManager().getModel().getCurrentCase().caseFolder.isTdfIndexValid();
        
        if (!validTDF)
        	listLabel =  "<html>Select Block:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
        			"<font color=red>Check TDF Index!</font>" +
        			"</html>";

        for (int i = 0; i < blocks.length; i++) {		// ConResBlock block : blocks) {
            ConResBlock	block = blocks[i];

            String prefix = ""+(int)block.getZValue().getValue() + "%"; //Integer.toHexString(i+1).toUpperCase();
            if (block.getZValue().getValue()<10) prefix = "0" + (int)block.getZValue().getValue() + "%";
            
            listItems[i] = prefix +  "\t\t\t\tReference Tone"; // + block.getZValue().getValue() + "%";//"Block " + (i + 1);
            targetBlockMap.put(listItems[i], block);
            if ((activeBlock != null) && (activeBlock == block)) activeItem = listItems[i];
            else activeItem = listItems[0];
        }

        JFrame	frame = null;
        		
        		try {
        			frame = getController().getAnalyzer().getMainFrame();
        		} catch (Exception exception) {
        			frame = new JFrame();
        		}
        
//        controller.getView().setFrameMenu(frame);

        // Ref: http://www.java2s.com/Code/Java/Swing-Components/Usethismodaldialogtolettheuserchooseonestringfromalonglist.htm
        String	selectedItem = ListDialog.showDialog(frame, frame, listLabel,
                                  "Block Chooser", listItems, activeItem, "   " + listItems[0]);

        
        if (!selectedItem.isEmpty()) {
        	ConResBlock	selectedBlock = targetBlockMap.get(selectedItem);
        	getController().setActiveBlock(selectedBlock);
        } else {
	        if (!validTDF)
	        	new FloatingAlert("<html><p>TDF block index out of sync!</p>" +
	        			"<p>Please add the missing block values to the NLevels sections of the TDF file.</p></html>").flashView(1000);
        }
        

        return true;
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

	@Override
	public void detatch(Observable oberservableObject) {
		// TODO Auto-generated method stub
		
	}
}
