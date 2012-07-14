package com.grasppe;

import com.grasppe.lure.components.Observers;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

public class EventTest implements Observable
{
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
  
  
  protected Observers observers = new Observers(this);
  
	/**
	   * Attaches an observer through the observers object which will include the observer in future update() notify calls.
	   * @param observer
	   */
	public void attachObserver(Observer observer) {
	    observers.attachObserver(observer);
	  }

	/**
	   * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
	   * @param observer
	   */
	public void detachObserver(Observer observer) {
	    observers.detachObserver(observer);
	  }

	/**
	   * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
	   */
	public void detachObservers() {
	    observers.detachObservers();
	  }

	/**
	   *  @param observer
	   */
	public void notifyObserver(Observer observer) {
	    observer.update();
	  }

	/**
	   * Notifies all observer through the observers object which calls update().
	   */
	public void notifyObservers() {
	    observers.notifyObservers();
	  }
}
