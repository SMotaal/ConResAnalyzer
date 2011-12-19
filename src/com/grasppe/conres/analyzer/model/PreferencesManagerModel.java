/*
 * @(#)ConResAnalyzerModel.java   11/12/03
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.analyzer.model;

import com.grasppe.conres.analyzer.PreferencesManager;
import com.grasppe.conres.analyzer.view.PreferencesManagerView;
import com.grasppe.lure.components.AbstractModel;

/**
 * Class description
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class PreferencesManagerModel extends AbstractModel {

  /**
   *   Constructs a new model object with no predefined controller.
   */
  public PreferencesManagerModel() {
    super();
  }

  /**
   * Constructs a new model with a predefined controller.
   * @param controller
   */
  public PreferencesManagerModel(PreferencesManager controller) {
    super(controller);
  }

  /*
   *  (non-Javadoc)
   *   @see com.grasppe.lure.components.AbstractComponent#getView()
   */

  /**
   * 	@return
   */
  @Override
  public PreferencesManagerView getView() {

    // TODO Auto-generated method stub
    return (PreferencesManagerView)super.getView();
  }
}
