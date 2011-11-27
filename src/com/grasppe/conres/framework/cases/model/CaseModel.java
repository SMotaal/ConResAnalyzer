package com.grasppe.conres.framework.cases.model;

import com.grasppe.conres.io.model.CaseFolder;
import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.conres.io.model.TargetDefinitionFile;

/**
 * Class description
 *
 * @version        $Revision: 1.0, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class CaseModel {

    /** Field description */
    public String	path;

    /** Field description */
    public String	title;
    
    public CaseFolder caseFolder;
    
    public TargetDefinitionFile targetDefinitionFile;
    
    public ImageFile[] imageFiles;
    
    public boolean filesLoaded = false;
    
}