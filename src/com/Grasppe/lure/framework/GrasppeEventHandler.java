/**
 * 
 */
package com.grasppe.lure.framework;

import java.awt.event.KeyEvent;

import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * @author daflair
 *
 */
public interface GrasppeEventHandler extends GrasppeKit.Observer {

//	public boolean dispatchedEvent();
	public boolean dispatchedKeyEvent(KeyEvent e);
	
}
