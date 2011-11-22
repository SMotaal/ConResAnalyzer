/**
 * 
 */
package com.grasppe.conres.framework.targets.model;

import java.util.TreeMap;

import com.grasppe.lure.components.AbstractController;
import com.grasppe.lure.components.AbstractModel;

/**
 * @author daflair
 *
 */
public class TargetGrid extends AbstractModel {
	
	protected TreeMap<Integer, Integer> bMap;
	protected TreeMap<Integer, Integer> rMap;
	protected TreeMap<Integer, Integer> cMap;

	/**
	 * 
	 */
	public TargetGrid() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param controller
	 */
	public TargetGrid(AbstractController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

}
