/*
 * @(#)AbstractView.java   11/12/02
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;
import java.awt.Container;

import java.util.ArrayList;

import javax.swing.JFrame;

/**
 *     Views handle the user interface portion of a component. A view directly accesses a controller. A view indirectly accesses a model through the controller. The controller is responsible for all attach/detach calls.
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public abstract class AbstractView extends DebuggableComponent implements Observer {

  protected ArrayList<Component> viewComponents = new ArrayList<Component>();
  int                            dbg            = 0;

//private static JMenuBar                           menuBar             = null;

  /**
   * @param controller
   */
  public AbstractView(AbstractController controller) {
    super();
    this.controller = controller;

    if (getModel() != null) {
      getModel().attachView(this);
      updateDebugView();
      update();
    }

  }

  /**
   *    @throws Throwable
   */
  public void detatch() throws Throwable {
    GrasppeKit.debugText("Detatching", getClass().getSimpleName(), 1);

    try {
      if (!viewComponents.isEmpty()) {
        for (Component component : viewComponents) {
          GrasppeKit.debugText("Detatching", component.getClass().getSimpleName(), 1);

          try {
            if (component instanceof JFrame) {
              ((JFrame)component).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
              component.setVisible(false);
            }

            Container parent = component.getParent();

            if (parent != null) {
              component.getParent().remove(component);
            }
          } catch (Exception exception) {
            GrasppeKit.debugError("Detatching View Components", exception, 1);
          }
        }
      }

      getModel().detachView(this);
      detachObservers();
    } catch (Exception exception) {
      GrasppeKit.debugError("Detatching View", exception, 1);
    }

    super.detatch();

//  notifyObservers();
  }

  /*
   *  (non-Javadoc)
   * @see java.lang.Object#finalize()
   */

  /**
   *  @throws Throwable
   */
  @Override
  public void finalize() throws Throwable {
    GrasppeKit.debugText("Finalizing", getClass().getSimpleName(), 1);

//  for (Component component : viewComponents) {
//      try {
//          if (component instanceof JFrame) {
//              ((JFrame)component).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//              component.setVisible(false);
//          }
//
//          Container parent = component.getParent();
//
//          if (parent != null) {
//              component.getParent().remove(component);
//          }
//      } catch (Exception exception) {
//          GrasppeKit.debugError("Terminating View Components", exception, 2);
//      }
//  }
//
//  getModel().detachView(this);
//
////notifyObservers();
//  detachObservers();
//
////Iterator<Observer> observerIterator = observers.getIterator();
////
////while (observerIterator.hasNext()) {
////    try {
////        detachObserver(observerIterator.next());
////    } catch (Exception exception) {
////        GrasppeKit.debugError("Detaching View Observers", exception, 2);
////    }
////}

    detatch();

    super.finalize();
  }

  /**
   * Method called by observable object during notifyObserver calls.
   */

  public void update() {
    if (debugPanel != null) updateDebugView();
  }

  /**
   *  @return
   */
  protected final AbstractModel getControllerModel() {
    return controller.getModel();
  }

  /**
   *    @param container
   */
  protected void setComponentFocus(Container container) {
    for (Component component : container.getComponents()) {
      if (component instanceof Container) setComponentFocus((Container)component);
      component.setFocusable(false);
    }
  }

///**
// *    @param frame
// */
//public void setFrameMenu(JFrame frame) {
//  if (!frame.isFocused()) return;
//  if (frame == null || (getMenuBar() == null && menuBar==null)) return;
////      if (frame.getJMenuBar()!=getMenuBar())
//  JMenuBar thisBar = null;
//  if (getMenuBar() == null && menuBar!=null)
//      thisBar = menuBar; // frame.setJMenuBar(menuBar);
//  else if (getMenuBar() != null && menuBar==null)
//      thisBar = getMenuBar(); // frame.setJMenuBar(getMenuBar());
//  else if (getMenuBar() != null && menuBar!=null)
//      thisBar = getMenuBar();
////      else
////          return;
////      Container container = thisBar.getTopLevelAncestor();
////      if (container!=null)
////          container.remove(thisBar);
//  
//  JMenuBar frameBar = new JMenuBar();
//  for(int i = 0; i < thisBar.getMenuCount(); i ++) {
//      JMenu thisMenu = thisBar.getMenu(i);
//      JMenu frameMenu = new JMenu(frameBar.add(thisBar.getMenu(i)).getText());
//      for (int c = 0; c < thisMenu.getMenuComponentCount(); c++) {
//          frameMenu.add(thisMenu.getMenuComponent(c));
//          
//      }
//      frameBar.add(frameMenu);
//  }
//  
//  frame.setJMenuBar(frameBar);
//}
//
///**
// * @param menuBar the menuBar to set
// */
//public void setMenuBar(JMenuBar menuBar) {
//    this.menuBar = menuBar;
//}
}
