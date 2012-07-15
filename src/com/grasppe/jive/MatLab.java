/*
 * @(#)MatLab.java   12/07/15
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



/**
 *
 */
package com.grasppe.jive;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

/**
 * @author daflair
 *
 */
public class MatLab {

  private static MatLab                    instance = null;
  private static MatlabProxyFactoryOptions options  = new MatlabProxyFactoryOptions.Builder().setUsePreviouslyControlledSession(
                                                         true).build();

  // Create a proxy, which we will use to control MATLAB
  private static MatlabProxyFactory factory = null;			// new MatlabProxyFactory(options);
  private static MatlabProxy        proxy   = null;			// factory.getProxy();

///**
// * 
// */

  /**
   * 	@throws MatlabConnectionException
   */
  private MatLab() throws MatlabConnectionException {
    if (proxy == null) {
      factory = new MatlabProxyFactory(options);
      proxy   = factory.getProxy();
    }
  }

  /**
   * 	@throws MatlabConnectionException
   */
  public static void Initialize() throws MatlabConnectionException {
    if (instance == null) instance = new MatLab();
  }

  /**
   * @return the factory
   */
  public static MatlabProxyFactory getFactory() {
    return factory;
  }

  /**
   * @return the options
   */
  public static MatlabProxyFactoryOptions getOptions() {
    return options;
  }

  /**
   * @return the proxy
   */
  public static MatlabProxy getProxy() {
    return proxy;
  }
}
