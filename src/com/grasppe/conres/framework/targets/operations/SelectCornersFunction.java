/*
 * @(#)SelectCornersFunction.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.framework.targets.operations;

import com.grasppe.conres.framework.targets.CornerSelector;
import com.grasppe.conres.framework.targets.model.CornerSelectorModel;
import com.grasppe.conres.framework.targets.view.CornerSelectorView;
import com.grasppe.lure.components.AbstractModel;
import com.grasppe.lure.components.AbstractView;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import javax.management.openmbean.InvalidOpenTypeException;

/**
 * Class description
 * @version        $Revision: 1.0, 11/11/09
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class SelectCornersFunction extends TargetManagerFunction implements Observer {

    protected static final String	name = "SelectCorners";
    AbstractModel					model;
    CornerSelectorView				view;		// = new CornerSelectorView();
    CornerSelector					controller;
    boolean							openWindow = false;
    
    int dbg = 0;

    /**
     */
    public SelectCornersFunction() {
        super(name);
    }

    /**
     *  @param controller
     */
    public SelectCornersFunction(CornerSelector controller) {
        this();
        this.controller = controller;
        setModel(controller.getModel());
        setView(controller.getSelectorView());
    }

    /**
     * @return
     */
    @Override
    protected boolean perfomOperation() {
    	
    	controller.showSelectorView();

        // prepareView();
//      new CornerSelectorView(controller).run(null);

//        view.run(null);

//      setCornerSelectorVisibile(true);
//      
//        new Thread() {
//          public void run() {
//          }
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
//      while(openWindow) {
//          try {
//              wait();
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }
//      }
        return true;
    }

    /**
     */
    public void prepareView() {
        view = new CornerSelectorView(null);

        // view.attachObserver(this);
    }

    /**
     */
    public void showCornerSelector() {}

    /**
     */
    public void update() {
        GrasppeKit.debugText("OBSERVED", dbg);

        // boolean cornersValid = view.blockROI!=null && view.blockROI.getNCoordinates()==4;
        // boolean centresVaiid = view.patchSetROI!=null && view.patchSetROI.getNCoordinates() >10;
//      if (getModel().isValidSelection()) windowClosed();
    }

    /**
     */
    public void windowClosed() {
        openWindow = false;
    }

//    
//  public static void main(String[] args) {
//
//      (new SelectCornersFunction()).perfomOperation();
//      return;
//  }
//  

    /**
     * Returns a correctly-cast model.
     * @return
     */
    public CornerSelectorModel getModel() {
        return (CornerSelectorModel)model;
    }

    /**
     *  @param visible
     */
    public void setCornerSelectorVisibile(boolean visible) {
        if (visible) view.run(null);

//      else
//          try {
//              view.terminate();
//          } catch (Throwable e) {
//              e.printStackTrace();
//          }
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
     *  @param view
     */
    public void setView(AbstractView view) {
        model.attachObserver(this);
        if (!(view instanceof CornerSelectorView)) throw new InvalidOpenTypeException();

        this.view = (CornerSelectorView)view;

        update();
    }
}
