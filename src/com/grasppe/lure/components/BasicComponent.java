/*
 * @(#)AbstractView.java   11/12/02
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 *     Views handle the user interface portion of a component. A view directly accesses a controller. A view indirectly accesses a model through the controller. The controller is responsible for all attach/detach calls.
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class BasicComponent extends AbstractComponent implements Observer {

  /**
   *  
   */
  public BasicComponent() {
    super();
    // update();
  }

//protected ArrayList<Component> viewComponents = new ArrayList<Component>();
// int                            dbg            = 0;

//private static JMenuBar                           menuBar             = null;

  /**
   * @param controller
   */
  public BasicComponent(AbstractController controller) {
    super();
    this.controller = controller;

    if (getModel() != null) {
      getModel().attachObserver(this);
      update();
    }

  }
  
  private java.util.Vector data = new java.util.Vector();
  public synchronized void addMyTestListener(MyTestListener lis) {
      data.addElement(lis);
  }
  public synchronized void removeMyTestListener(MyTestListener lis) {
      data.removeElement(lis);
  }
  public interface MyTestListener extends java.util.EventListener {
      void testEvent(MyTestEvent event);
  }
  public class MyTestEvent extends java.util.EventObject {
      private static final long serialVersionUID = 1L;
      public float oldValue,newValue;        
      MyTestEvent(Object obj, float oldValue, float newValue) {
          super(obj);
          this.oldValue = oldValue;
          this.newValue = newValue;
      }
  }
  public void notifyMyTest() {
      java.util.Vector dataCopy;
      synchronized(this) {
          dataCopy = (java.util.Vector)data.clone();
      }
      for (int i=0; i<dataCopy.size(); i++) {
          MyTestEvent event = new MyTestEvent(this, 0, 1);
          ((MyTestListener)dataCopy.elementAt(i)).testEvent(event);
      }
  }
  
  
//  private java.util.Vector data = new java.util.Vector();
//  
//  public synchronized void addUpdateEventListener(UpdateListener lis) {
//      data.addElement(lis);
//  }
//  public synchronized void removeUpdateEventListener(UpdateListener lis) {
//      data.removeElement(lis);
//  }
//  public interface UpdateListener extends java.util.EventListener {
//      void updateEvent(UpdateEvent event);
//  }
//  
//  
//  public class UpdateEvent extends java.util.EventObject {
//      private static final long serialVersionUID = 1L;
//      public float oldValue,newValue;        
//      UpdateEvent(Object obj, float oldValue, float newValue) {
//          super(obj);
//          this.oldValue = oldValue;
//          this.newValue = newValue;
//      }
//  }
//  
//  public void notifyUpdate() {
//      java.util.Vector dataCopy;
//      synchronized(this) {
//          dataCopy = (java.util.Vector)data.clone();
//      }
//      for (int i=0; i<dataCopy.size(); i++) {
//    	  UpdateEvent event = new UpdateEvent(this, 0, 1);
//          ((UpdateListener)dataCopy.elementAt(i)).updateEvent(event);
//      }
//  }
  
  

  /*
   *  (non-Javadoc)
   * @see java.lang.Object#finalize()
   */

  /**
   *  @throws Throwable
   */
  @Override
  public void finalize() throws Throwable {

//  for (Component component : viewComponents) {
//  try {
//      if (component instanceof JFrame) {
//          ((JFrame)component).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//          component.setVisible(false);
//      }
//  
//      Container parent = component.getParent();
//  
//      if (parent != null) {
//          component.getParent().remove(component);
//      }
//  } catch (Exception exception) {
//      GrasppeKit.debugError("Terminating View Components", exception, 2);
//  }
//  }

    getModel().detachObserver(this);

//  notifyObservers();
    detachObservers();

//  Iterator<Observer> observerIterator = observers.getIterator();
//
//  while (observerIterator.hasNext()) {
//      try {
//          detachObserver(observerIterator.next());
//      } catch (Exception exception) {
//          GrasppeKit.debugError("Detaching View Observers", exception, 2);
//      }
//  }
    super.finalize();
  }

  /**
   * Method called by observable object during notifyObserver calls.
   */

  public void update() {

    // if (debugPanel != null) updateDebugView();
  }

@Override
protected void finalizeUpdates() {
	// TODO Auto-generated method stub
}

///**
// *  @return
// */
//protected final AbstractModel getControllerModel() {
//  return controller.getModel();
//}

///**
// *  @param container
// */
//protected void setComponentFocus(Container container) {
//  for (Component component : container.getComponents()) {
//    if (component instanceof Container) setComponentFocus((Container)component);
//    component.setFocusable(false);
//  }
//}

///**
// *    @param frame
// */
//public void setFrameMenu(JFrame frame) {
//if (!frame.isFocused()) return;
//if (frame == null || (getMenuBar() == null && menuBar==null)) return;
////      if (frame.getJMenuBar()!=getMenuBar())
//JMenuBar thisBar = null;
//if (getMenuBar() == null && menuBar!=null)
//    thisBar = menuBar; // frame.setJMenuBar(menuBar);
//else if (getMenuBar() != null && menuBar==null)
//    thisBar = getMenuBar(); // frame.setJMenuBar(getMenuBar());
//else if (getMenuBar() != null && menuBar!=null)
//    thisBar = getMenuBar();
////      else
////          return;
////      Container container = thisBar.getTopLevelAncestor();
////      if (container!=null)
////          container.remove(thisBar);
//
//JMenuBar frameBar = new JMenuBar();
//for(int i = 0; i < thisBar.getMenuCount(); i ++) {
//    JMenu thisMenu = thisBar.getMenu(i);
//    JMenu frameMenu = new JMenu(frameBar.add(thisBar.getMenu(i)).getText());
//    for (int c = 0; c < thisMenu.getMenuComponentCount(); c++) {
//        frameMenu.add(thisMenu.getMenuComponent(c));
//        
//    }
//    frameBar.add(frameMenu);
//}
//
//frame.setJMenuBar(frameBar);
//}
//
///**
// * @param menuBar the menuBar to set
// */
//public void setMenuBar(JMenuBar menuBar) {
//  this.menuBar = menuBar;
//}
}
