/*
 * @(#)CallbackHook.java   12/12/04
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.framework.matlab.trash;

/**
 * Class description
 * 	@version        $Revision: 1.0, 12/12/04
 * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
 */
public class CallbackHook {

  private java.util.Vector hooks = new java.util.Vector();

  /**
   * 	@param lis
   */
  public synchronized void addCallbackListener(CallbackListener lis) {
    hooks.addElement(lis);
  }

  /**
   */
  public void notifyCallback() {
    java.util.Vector dataCopy;

    synchronized (this) {
      dataCopy = (java.util.Vector)hooks.clone();
    }

    for (int i = 0; i < dataCopy.size(); i++) {
      CallbackEvent event = new CallbackEvent(this, null, null);

      ((CallbackListener)dataCopy.elementAt(i)).callbackEvent(event);
    }
  }
  
  public void notifyCallback(Object data) {
	    java.util.Vector hooksCopy;

	    synchronized (this) {
	      hooksCopy = (java.util.Vector)hooks.clone();
	    }

	    for (int i = 0; i < hooksCopy.size(); i++) {
	      CallbackEvent event = new CallbackEvent(this, data, null);

	      ((CallbackListener)hooksCopy.elementAt(i)).callbackEvent(event);
	    }
	  }
  
  public void notifyCallback(Object data, Object metadata) {
	    java.util.Vector hooksCopy;

	    synchronized (this) {
	      hooksCopy = (java.util.Vector)hooks.clone();
	    }

	    for (int i = 0; i < hooksCopy.size(); i++) {
	      CallbackEvent event = new CallbackEvent(this, data, metadata);

	      ((CallbackListener)hooksCopy.elementAt(i)).callbackEvent(event);
	    }
	  }  
  

  /**
   * 	@param lis
   */
  public synchronized void removeCallbackListener(CallbackListener lis) {
    hooks.removeElement(lis);
  }

  /**

   * 	@version        $Revision: 1.0, 12/12/04
   * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
   */
  public interface CallbackListener extends java.util.EventListener {

    /**
     * 	@param event
     */
    void callbackEvent(CallbackEvent event);
  }


  /**
   * Class description
   * 	@version        $Revision: 1.0, 12/12/04
   * 	@author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>    
   */
  public class CallbackEvent extends java.util.EventObject {

    private static final long serialVersionUID = 1L;

    /** Field description */
    public Object data, metadata;

    /**
     * 	@param obj
     * 	@param data
     * 	@param metadata
     */
    CallbackEvent(Object obj, Object data, Object metadata) {
      super(obj);
      this.data 		= data;
      this.metadata 	= metadata;
    }
  }
}
