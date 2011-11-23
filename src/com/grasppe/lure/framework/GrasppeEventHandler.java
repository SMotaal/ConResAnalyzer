/**
 * 
 */
package com.grasppe.lure.framework;

import java.awt.event.KeyEvent;

/**
 * @author daflair
 *
 */
public interface GrasppeEventHandler extends GrasppeKit.Observer {

	public boolean dispatchedKeyEvent(KeyEvent e);
	
}
