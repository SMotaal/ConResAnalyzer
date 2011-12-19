package com.grasppe.lure.framework;

import com.grasppe.lure.framework.GrasppeKit.IStringDescription;
import com.grasppe.lure.framework.GrasppeKit.IStringKey;

/**
 *
 *  @version        $Revision: 1.0, 11/12/17
 *  @author         <a href=�mailto:saleh.amr@mac.com�>Saleh Abdel Motaal</a>
 */
public interface IPreferencesEnum extends IStringKey, IStringDescription {

    /**
     *  @return
     */
    Object defaultValue();
}