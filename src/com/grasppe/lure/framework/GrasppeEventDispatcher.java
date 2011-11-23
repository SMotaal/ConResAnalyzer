/*
 * @(#)GrasppeEventDispatcher.java   11/11/14
 * 
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.framework;



//~--- JDK imports ------------------------------------------------------------

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import com.grasppe.lure.components.Observers;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.GrasppeKit.KeyEventID;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

/**
 * Class description
 *
 * @version $Revision: 1.0, 11/11/09
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class GrasppeEventDispatcher implements KeyEventDispatcher, Observable {

    /** Field description */
    public static HashSet<KeyCode>	pressedKeys = new HashSet<KeyCode>();

    /** Field description */
    public static boolean	newCombination = true;

    /** Field description */
    public static boolean			consumedCombination = false;
    public KeyboardFocusManager	manager             = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    protected Observers				observers           = new Observers(this);

    /**
     * Constructs ...
     */
    private GrasppeEventDispatcher() {
        super();
        manager.addKeyEventDispatcher(this);
    }

    /**
     * Method description
     *
     * @param handler
     */
    public void attachEventHandler(GrasppeEventHandler handler) {
        attachObserver(handler);
    }

    /**
     * Method description
     *
     * @param observer
     */
    public void attachObserver(Observer observer) {
        observers.attachObserver(observer);

    }

    /**
     * Method description
     *
     * @param observer
     */
    public void detachObserver(Observer observer) {
        observers.detachObserver(observer);
    }

    /**
     * Method description
     *
     * @param e
     *
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent e) {

        // TODO: consume key combinations;
        // if (pressedKeys.isEmpty()) consumedCombination = false;
        processKey(e);

        // if (!consumedCombination && pressedKeys.isEmpty()) {
        if (!pressedKeys.isEmpty()) {
        	consumedCombination = false;
            Iterator<Observer>	observerIterator = observers.getIterator();
            boolean				eventHandled     = false;

            while (observerIterator.hasNext()) {
                Observer	observer = observerIterator.next();

                if (observer instanceof GrasppeEventHandler) {
                    eventHandled |= ((GrasppeEventHandler)observer).dispatchedKeyEvent(e);
                    if (eventHandled) consumedCombination = true;
                }
            }

            if (eventHandled)
                GrasppeKit.debugText("Key Event Handled",
                                     GrasppeKit.keyEventString(e) + " (PressedKeys "
                                     + pressedKeyString() + ")", 4);
            else
                GrasppeKit.debugText("Key Event Ignored",
                                     GrasppeKit.keyEventString(e) + " (PressedKeys "
                                     + pressedKeyString() + ")", 5);

        }

        return false;
    }

    /**
     * Method description
     *
     * @param observer
     */
    public void notifyObserver(Observer observer) {
        observers.notifyObserver(observer);
    }

    /**
     * Method description
     */

    public void notifyObservers() {
        observers.notifyObservers();
    }

    /**
     * Method description
     *
     * @return
     */
    public static String pressedKeyString() {
        if (pressedKeys.isEmpty()) return "";

        String[]	keysString = new String[pressedKeys.size()];

        int			i          = 0;

        for (KeyCode keyCode : pressedKeys) {
            keysString[i] = keyCode.toString();
            i             += 1;
        }

        return GrasppeKit.cat(keysString, "+");
    }

    /**
     * Method description
     *
     * @param e
     */
    public static void processKey(KeyEvent e) {
        KeyEventID	eventID = KeyEventID.get(e.getID());

        try {
            switch (eventID) {

            case PRESSED :
                pressedKeys.add(KeyCode.get(e.getKeyCode()));
                break;

            case RELEASED :
                pressedKeys.remove(KeyCode.get(e.getKeyCode()));
                break;
            }
        } catch (Exception exception) {}

    }

    /**
     * Returns a lazy initialized singleton instance of this class using the private static SingletonHolder class adopting Bill Pugh's implementation of Singleton in Java.
     * {@link http://en.wikipedia.org/wiki/Singleton_pattern}
     * @return
     */
    public static GrasppeEventDispatcher getInstance() {
        return SingletonHolder.grasppeEventDispatcher;
    }

    /**
     * Method description
     *
     * @param keyCode
     *
     * @return
     */
    public static boolean isDown(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /**
     * Method description
     * @param keyCode
     * @return
     */
    public static boolean isPressed(KeyCode keyCode) {
    	return isPressed(asSet(keyCode), false);
    }
    
    /**
     * Method description
     * @param keyCode
     * @return
     */
    public static boolean isPressedExclusive(KeyCode keyCode) {
    	return isPressed(asSet(keyCode), true);
    }


    /**
     * Method description
     * @param keyCodes
     * @return
     */
    public static boolean isPressed(KeyCode[] keyCodes) {
    	return isPressed(asSet(keyCodes), false);
    }    
    
    public static boolean isPressedExclusive(KeyCode[] keyCodes) {
    	return isPressed(asSet(keyCodes), true);
    }
    
    public static boolean isPressed(HashSet<KeyCode> keyCodes, boolean exclusive) {
    	boolean allNeededPressed = pressedKeys.containsAll(keyCodes);
    	if (exclusive) {
    		boolean allPressedNeeded = pressedKeys.containsAll(keyCodes);
    		return allPressedNeeded && allNeededPressed;
    	}
    	return allNeededPressed;
    }
    
    public static HashSet<KeyCode> asSet (KeyCode[] keyCodes){
    	return new HashSet<KeyCode>(Arrays.asList(keyCodes));
    }
    
    public static HashSet<KeyCode> asSet (KeyCode keyCode) {
    	return new HashSet<KeyCode>(Arrays.asList(new KeyCode[]{keyCode}));
    }
    
    public static void consumeCombination() {
    	consumedCombination = true;
    }
    
    public static boolean isConsumed() {
    	return consumedCombination;
    }

//    public static boolean isPressed(KeyCode[] keyCodes) {
//        if (pressedKeys.isEmpty()) return false;
//
//        if (Arrays.asList(keyCodes).containsAll(pressedKeys) && pressedKeys.containsAll(Arrays.asList(keyCodes))) {
//            GrasppeKit.debugText("Key Combination Pressed", pressedKeyString(), 3);
//
//            // consumedCombination = true;
//            return true;
//        }
//
//        return false;
//    }
    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before. Bill Pugh's implementation of Singleton in Java.
     * {@link http://en.wikipedia.org/wiki/Singleton_pattern}
     */
    private static class SingletonHolder {

        /** Field description */
        public static final GrasppeEventDispatcher	grasppeEventDispatcher =
            new GrasppeEventDispatcher();
    }
}
