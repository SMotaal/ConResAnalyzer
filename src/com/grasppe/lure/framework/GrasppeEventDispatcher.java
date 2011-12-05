/*
 * @(#)GrasppeEventDispatcher.java   11/11/14
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.lure.framework;

import com.grasppe.lure.components.Observers;
import com.grasppe.lure.framework.GrasppeKit.KeyCode;
import com.grasppe.lure.framework.GrasppeKit.KeyEventID;
import com.grasppe.lure.framework.GrasppeKit.Observable;
import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 * Class description
 * @version $Revision: 1.0, 11/11/09
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class GrasppeEventDispatcher implements KeyEventDispatcher, Observable {

    /** Field description */
    public static HashSet<KeyCode>	pressedKeys = new HashSet<KeyCode>();

    /** Field description */
    public static boolean	newCombination = true;

    /** Field description */
    public static boolean	consumedCombination = false;
    
    public static boolean globalKeys = false;

    /** Field description */
    public KeyboardFocusManager	manager     = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    protected Observers			observers   = new Observers(this);
    Action						closeWindow = new AbstractAction("Close Window") {

        public void actionPerformed(ActionEvent e) {

            // window closing code here
            Frame[]	frames = Frame.getFrames();

            for (Frame frame : frames)
                if (frame.isActive()) {
                    frame.setVisible(false);
                    frame.dispose();
                }
        }
    };
    KeyStroke	closeKey = KeyStroke.getKeyStroke(KeyEvent.VK_W,
                             Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

    /**
     */
    private GrasppeEventDispatcher() {
        super();

        if (globalKeys)
        	manager.addKeyEventDispatcher(this);
    }

    /**
     *  @param keyCode
     *  @return
     */
    public static HashSet<KeyCode> asSet(KeyCode keyCode) {
        return new HashSet<KeyCode>(Arrays.asList(new KeyCode[] { keyCode }));
    }

    /**
     *  @param keyCodes
     *  @return
     */
    public static HashSet<KeyCode> asSet(KeyCode[] keyCodes) {
        return new HashSet<KeyCode>(Arrays.asList(keyCodes));
    }

    /**
     * @param handler
     */
    public void attachEventHandler(GrasppeEventHandler handler) {
        attachObserver(handler);
    }

    /**
     * @param observer
     */
    public void attachObserver(Observer observer) {
        observers.attachObserver(observer);

    }

    /**
     */
    public static void consumeCombination() {
        consumedCombination = true;
    }

    /**
     *  @param heading
     *  @param keyEvent
     *  @param level
     */
    public void debugPressedKey(String heading, KeyEvent keyEvent, int level) {
        GrasppeKit.debugText(heading,
                             GrasppeKit.keyEventString(keyEvent) + " (PressedKeys "
                             + pressedKeyString() + ")", level);
    }

    /**
     * @param observer
     */
    public void detachObserver(Observer observer) {
        observers.detachObserver(observer);
    }

    /**
     * @param e
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        processKey(e);

        if (!pressedKeys.isEmpty()) {
            consumedCombination = false;

            boolean		eventHandled = false;

            Object[]	allObservers = observers.getObserverSet().toArray();

            for (Object observerObject : allObservers) {
                Observer	observer = (Observer)observerObject;

                try {
                    if (!(observer instanceof GrasppeEventHandler)) continue;
                } catch (NullPointerException exception) {
                    observers.detachObserver(observer);
                    continue;
                }

                eventHandled |= ((GrasppeEventHandler)observer).dispatchedKeyEvent(e);
                if (eventHandled) consumedCombination = true;
            }

            if (eventHandled) debugPressedKey("Key Event Handled", e, 4);
            else debugPressedKey("Key Event Ignored", e, 5);

        }

        return false;
    }

    /**
     *  @param frame
     */
    public void makeClosable(JFrame frame) {

        // ref: http://stackoverflow.com/questions/4209975/how-to-have-command-w-close-a-window-on-mac-os-in-java-or-clojure

        // TODO: Figure out how to implement closeWindow!
        // TODO: Attach closeWindow to frame!
        return;
    }

    /**
     * @param observer
     */
    public void notifyObserver(Observer observer) {
        observers.notifyObserver(observer);
    }

    /**
     */

    public void notifyObservers() {
        observers.notifyObservers();
    }

    /**
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
     *  @return
     */
    public static boolean isConsumed() {
        return consumedCombination;
    }

    /**
     * @param keyCode
     * @return
     */
    public static boolean isDown(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }

    /**
     * @param keyCode
     * @return
     */
    public static boolean isPressed(KeyCode keyCode) {
        return isPressed(asSet(keyCode), false);
    }

    /**
     * @param keyCodes
     * @return
     */
    public static boolean isPressed(KeyCode[] keyCodes) {
        return isPressed(asSet(keyCodes), false);
    }

    /**
     *  @param keyCodes
     *  @param exclusive
     *  @return
     */
    public static boolean isPressed(HashSet<KeyCode> keyCodes, boolean exclusive) {
        boolean	allNeededPressed = pressedKeys.containsAll(keyCodes);

        if (exclusive) {
            boolean	allPressedNeeded = pressedKeys.containsAll(keyCodes);

            return allPressedNeeded && allNeededPressed;
        }

        return allNeededPressed;
    }

    /**
     * @param keyCode
     * @return
     */
    public static boolean isPressedExclusive(KeyCode keyCode) {
        return isPressed(asSet(keyCode), true);
    }

    /**
     *  @param keyCodes
     *  @return
     */
    public static boolean isPressedExclusive(KeyCode[] keyCodes) {
        return isPressed(asSet(keyCodes), true);
    }

//  public static boolean isPressed(KeyCode[] keyCodes) {
//      if (pressedKeys.isEmpty()) return false;
//
//      if (Arrays.asList(keyCodes).containsAll(pressedKeys) && pressedKeys.containsAll(Arrays.asList(keyCodes))) {
//          GrasppeKit.debugText("Key Combination Pressed", pressedKeyString());
//
//          // consumedCombination = true;
//          return true;
//      }
//
//      return false;
//  }

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
