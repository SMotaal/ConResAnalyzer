/**
 * 
 */
package com.grasppe.lure.components;

/**
 * @author daflair
 *
 */
public interface IAuxiliaryCaseManager {
	
    public void backgroundCurrentCase() throws IllegalAccessException;
    
    public void restoreBackgroundCase() throws IllegalAccessException;
    
    public void discardBackgroundCase() throws IllegalAccessException;

}
