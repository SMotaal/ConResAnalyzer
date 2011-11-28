package com.grasppe.conres.framework.targets.operations;

import javax.management.openmbean.InvalidOpenTypeException;

import com.grasppe.conres.framework.imagej.CornerSelectorView;
import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.TargetManager;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.model.TargetManagerModel;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.components.AbstractOperation;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * Class description
 *
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCornersOperation extends TargetManagerOperation implements Observer {

	protected static final String	name = "SelectCorners";
	AbstractModel model;
	CornerSelectorView view;// = new CornerSelectorView();
	CornerSelector controller;
	boolean openWindow = false;
	
	public SelectCornersOperation() {
        super(name);
	}

    /**
     * @param name
     */
    public SelectCornersOperation(CornerSelector controller) {
        this();
        this.controller = controller;
        setModel(controller.getModel());
        setView(controller.getSelectorView());
    }
    
    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)model;
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
    
    /**
     * Attaches the command to the specified model and calls update() to reflect the state of the model.
     * @param model
     */
    public void setView(AbstractView view) {
        model.attachObserver(this);
        if (!(view instanceof CornerSelectorView))
        	throw new InvalidOpenTypeException();
        
        this.view = (CornerSelectorView) view;
        	
        update();
    }     
    
    public void update() {
    	GrasppeKit.debugText("OBSERVED",2);
    	//boolean cornersValid = view.blockROI!=null && view.blockROI.getNCoordinates()==4;
    	//boolean centresVaiid = view.patchSetROI!=null && view.patchSetROI.getNCoordinates() >10;
//    	if (getModel().isValidSelection()) windowClosed();
    }
    
    public void prepareView() {
    	view = new CornerSelectorView(null);
    	//view.attachObserver(this);
    }
    
    public void setCornerSelectorVisibile(boolean visible){
    	   if (visible)
    	    	view.run(null);
//		else
//			try {
//				view.terminate();
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
    }
    
    public void showCornerSelector() {

    }
    
    /**
     * Method description
     *
     * @return
     */
    @Override
    protected boolean perfomOperation() {

    	//prepareView();
//    	new CornerSelectorView(controller).run(null);
    	
    	view.run(null);
//    	setCornerSelectorVisibile(true);
//    	
//        new Thread() {
//        	public void run() {
//        	}
//        }.start();
//        
//        try {
//            synchronized (threadLock) {
//                threadLock.wait();
//            }
//        } catch (InterruptedException e) {
//
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    	while(openWindow) {
//    		try {
//				wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//    	}
    	return true;
    }
    
    public void windowClosed() {
		openWindow = false;
    }
//    
//    public static void main(String[] args) {
//
//        (new SelectCornersOperation()).perfomOperation();
//        return;
//    }
//    
    
}