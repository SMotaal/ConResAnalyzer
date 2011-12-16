/**
 * 
 */
package com.grasppe.conres.preferences;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.infotrek.util.prefs.FilePreferencesFactory;

import com.grasppe.conres.analyzer.ConResAnalyzer;
import com.grasppe.lure.framework.GrasppePreferencesFactory;

/**
 * @author daflair
 *
 */
public class ConResPreferencesFactory extends GrasppePreferencesFactory {
	
//	public static final String SYSTEM_PROPERTY_FILE = "net.infotrek.util.prefs.FilePreferencesFactory.file";

	/**
	 * 
	 */
	public ConResPreferencesFactory() {
	}
	
	private void prepareEnvironment(){
	    System.setProperty("java.util.prefs.PreferencesFactory", getFactoryClass().getName());
	    System.setProperty(SYSTEM_PROPERTY_FILE, getUserPreferencesFile());		
	}
	
	public Preferences getPreferences() {
		prepareEnvironment();
		return Preferences.userNodeForPackage(getFactoryClass());
	}
	
//	public void something() throws BackingStoreException {
//
//	    Preferences p = Preferences.userNodeForPackage(getOwnerClass()); //my.class);
//	    
//	    for (String s : p.keys()) {
//	      System.out.println("p[" + s + "]=" + p.get(s, null));
//	    }
//	 
//	    p.putBoolean("hi", true);
//	    p.put("Number", String.valueOf(System.currentTimeMillis()));
//	    
//	}
	
//	  public static void main(String[] args) throws BackingStoreException
//	  {
////	    System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
////	    System.setProperty(SYSTEM_PROPERTY_FILE, "myprefs.txt");
////
////	    Preferences p = Preferences.userNodeForPackage(FilePreferencesFactory.class); //my.class);
//		  
//		  ConResPreferencesFactory preferencesFactory = new ConResPreferencesFactory(); 
//		  
//		  Preferences p = preferencesFactory.getPreferences();
//	 
//	    for (String s : p.keys()) {
//	      System.out.println("p[" + s + "]=" + p.get(s, null));
//	    }
//	 
//	    p.putBoolean("hi", true);
//	    p.put("Number", String.valueOf(System.currentTimeMillis()));
//	  }	
	
	
    public Class<?> getFactoryClass() {
    	return ConResPreferencesFactory.class;
    }

	@Override
	public Class<?> getOwnerClass() {
		return ConResAnalyzer.class;
	}


}
