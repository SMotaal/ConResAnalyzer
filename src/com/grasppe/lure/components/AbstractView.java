/*
 * @(#)AbstractView.java   11/12/02
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.components;

import com.grasppe.lure.framework.GrasppeKit.Observer;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *     Views handle the user interface portion of a component. A view directly accesses a controller. A view indirectly accesses a model through the controller. The controller is responsible for all attach/detach calls.
 *
 *     @version        $Revision: 0.1, 11/11/08
 *     @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class AbstractView extends ObservableObject implements Observer {

    static JFrame						debugViewFrame;
    static TreeSet<JPanel>				debugViewPanels = new TreeSet<JPanel>();
    static String						debugViewString = "Debug";
    protected static String				debugSeparator  = ": ";
    protected AbstractController		controller;
    protected HashMap<String, JLabel>	debugLabels = new HashMap<String, JLabel>();
    protected JPanel					debugPanel  = null;

    /**
     * Constructs ...
     *
     * @param controller
     */
    public AbstractView(AbstractController controller) {
        super();
        this.controller = controller;

        if (getModel() != null) {
            getModel().attachView(this);
            update();
        }

        prepareDebugView();
    }

    /**
     *  @param name
     */
    protected void createDebugLabel(String name) {
        if (debugPanel == null) return;

        JLabel	newLabel = new JLabel(name + debugSeparator);

        debugPanel.add(newLabel);
        debugLabels.put(name.toLowerCase(), newLabel);

        // return newLabel;
    }

    /**
     */
    protected void createDebugView() {
        if ((debugPanel != null) && debugViewPanels.contains(debugPanel)) return;

        if (debugPanel == null) {
            debugPanel = new JPanel();

            JLabel	titleLabel = new JLabel("<html><b>" + getClass().getSimpleName()
                                           + "</b></html>");

            debugPanel.add(titleLabel);

            BoxLayout	layout = new BoxLayout(debugPanel, BoxLayout.Y_AXIS);

            debugPanel.setLayout(layout);
        }

        debugViewPanels.add(debugPanel);
    }

    /**
     */
    protected void prepareDebugView() {

        if (debugViewFrame != null) return;
        debugViewFrame = new JFrame(debugViewString);
        
//        debugViewFrame.setResizable(false);
        debugViewFrame.setUndecorated(true);
        debugViewFrame.setEnabled(false);

        Container	pane   = debugViewFrame.getContentPane();

        BoxLayout	layout = new BoxLayout(pane, BoxLayout.Y_AXIS);

        debugViewFrame.setLayout(layout);

        EmptyBorder	border = new EmptyBorder(10, 10, 10, 10);

//        debugViewFrame.setAlwaysOnTop(true);
        debugViewFrame.setMinimumSize(new Dimension(800, 150));
        debugViewFrame.setMaximumSize(new Dimension(800, 400));

        Iterator<JPanel>	panelIterator = debugViewPanels.iterator();
        JPanel				panel         = null;

        if (debugViewPanels.size() == 0) return;

        while (panelIterator.hasNext()) {		// (panel = panelIterator.next()) != null)
            panel = panelIterator.next();
            panel.setBorder(border);
            pane.add(panel);
        }

        updateDebugView();
        updateDebugFrame();
    }

    /**
     * Method called by observable object during notifyObserver calls.
     */

    public void update() {
        if (debugPanel != null) updateDebugView();
    }

    /**
     *  @param name
     *  @param object
     */
    protected void updateDebugLabel(String name, Object object) {
        updateDebugLabel(name, getString(object));
    }

    /**
     *  @param name
     *  @param text
     */
    protected void updateDebugLabel(String name, String text) {
        if (!debugLabels.containsKey(name.toLowerCase())) return;
        if (text.trim()=="") {
        	debugLabels.get(name.toLowerCase()).setText("");
        	debugLabels.get(name.toLowerCase()).setVisible(false);
        } else {
            debugLabels.get(name.toLowerCase()).setText("\t\t\t" + name + debugSeparator + text);
            debugLabels.get(name.toLowerCase()).setVisible(true);
        }
            if (debugViewFrame!=null)
            	debugViewFrame.pack();
    }

    /**
     *  @param name
     *  @param expression
     *  @param trueText
     *  @param falseText
     */
    protected void updateDebugLabel(String name, boolean expression, String trueText,
                                    String falseText) {
        if (expression) updateDebugLabel(name, trueText);		// updateDebugLabel(name, (expression) ? trueText : falseText);
        else updateDebugLabel(name, falseText);

//        } catch (Exception exception)
    }

    /**
     */
    protected void updateDebugLabels() {
        return;
    }

    /**
     */
    protected void updateDebugView() {
        try {
        	updateDebugFrame();
        } catch (Exception exception) {
            prepareDebugView();
        }
    }
    
    private void updateDebugFrame() {
        updateDebugLabels();

        debugViewFrame.pack();

        DisplayMode	displayMode =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
                .getDisplayMode();
        int	displayWidth  = displayMode.getWidth();
        int	displayHeight = displayMode.getHeight();
        int	frameWidth    = debugViewFrame.getWidth();
        int	frameHeight   = debugViewFrame.getHeight();

        debugViewFrame.setLocation(25, displayHeight - frameHeight - 25);
        debugViewFrame.setVisible(true);    	
    }

    /**
     * Returns model from view's controller
     * @return
     */
    protected AbstractModel getModel() {
        return controller.getModel();
    }

    /**
     *  @param object
     *  @return
     */
    protected String getString(Object object) {
        if (object == null) return "";

        String	objectString = object.toString();

        if (objectString == null) objectString = "";

        return objectString;
    }
}
