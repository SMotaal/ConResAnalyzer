package com.grasppe.lure.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.lure.framework.GrasppeKit.Observer;

public abstract class DebuggableComponent extends AbstractComponent {

	static JFrame debugViewFrame;
	static HashSet<JPanel> debugViewPanels = new HashSet<JPanel>();
	static String debugViewString = "Debug";
	static float debugFontSize = 10F;
	static int debugPadding = 5;
	protected static String debugSeparator = ": ";
	protected HashMap<String, JLabel> debugLabels = new HashMap<String, JLabel>();
	protected JPanel debugPanel = null;
	JLabel debugTitleLabel = new JLabel("<html><b>" + getClass().getSimpleName() + "</b></html>");
	boolean debugViewFrameFaded = false;
	
	int dbg = 2;

	public DebuggableComponent() {
		super();
	}

	/**
	 *  @param panel
	 */
	protected void addDebugView(JPanel panel) {
	        if (debugViewFrame == null) prepareDebugView();
	
	        Container	pane   = debugViewFrame.getContentPane();
	        EmptyBorder	border = new EmptyBorder(debugPadding, debugPadding, debugPadding,
	                                 debugPadding);
	
	//      panel = panelIterator.next();
	        panel.setBorder(border);
	        pane.add(panel);
	    }

	/**
	 *  @param name
	 */
	protected void createDebugLabel(String name) {
	    if (debugPanel == null)		// return;
	        createDebugView();
	
	    JLabel	newLabel = new JLabel(name + debugSeparator);
	
	    newLabel.setFont(newLabel.getFont().deriveFont(debugFontSize));
	
	    debugPanel.add(newLabel);
	    debugLabels.put(name.toLowerCase(), newLabel);
	
	    prepareDebugView();
	
	    // return newLabel;
	}

	/**
	 */
	protected void createDebugView() {
	        if ((debugPanel != null) && debugViewPanels.contains(debugPanel)) return;
	
	        if (debugPanel == null) {
	            debugPanel = new JPanel();
	
	//          JLabel    titleLabel = new JLabel("<html><b>" + getClass().getSimpleName()
	//                                         + "</b></html>");
	
	            debugTitleLabel.setFont(debugTitleLabel.getFont().deriveFont(debugFontSize));
	
	            debugPanel.add(debugTitleLabel);
	
	            BoxLayout	layout = new BoxLayout(debugPanel, BoxLayout.Y_AXIS);
	
	            debugPanel.setLayout(layout);
	        }
	
	        try {
	            debugViewPanels.add(debugPanel);
	            addDebugView(debugPanel);
	        } catch (Exception exception) {
	            GrasppeKit.debugError("DebugView", exception, dbg);
	        }
	    }

	/**
	 */
	protected void prepareDebugView() {
	
	        if (debugViewFrame != null) return;
	        debugViewFrame = new JFrame(debugViewString);
	
	        debugViewFrame.setResizable(false);
	        debugViewFrame.setFocusable(false);
	
	        debugViewFrame.setUndecorated(true);
	
	        MouseAdapter	mouseAdapter = new MouseAdapter() {
	
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                if (debugViewFrameFaded) GrasppeKit.setFrameOpacity(debugViewFrame, 1.0F);		// debugViewFrame.hasFocus()
	                else GrasppeKit.setFrameOpacity(debugViewFrame, 0.5F);
	                debugViewFrameFaded = !debugViewFrameFaded;
	            }
	        };
	
	        debugViewFrame.addMouseListener(mouseAdapter);
	        GrasppeKit.setFrameOpacity(debugViewFrame, 0.5F);
	        debugViewFrameFaded = true;
	
	        GrasppeKit.setFrameOpacity(debugViewFrame, 0.5F);
	
	        Container	pane   = debugViewFrame.getContentPane();
	
	        BoxLayout	layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
	
	        debugViewFrame.setLayout(layout);
	
	//      debugViewFrame.setAlwaysOnTop(true);
	
	        DisplayMode	displayMode =
	            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
	                .getDisplayMode();
	        int	displayWidth  = displayMode.getWidth();
	        int	displayHeight = displayMode.getHeight();
	
	        debugViewFrame.setMinimumSize(new Dimension(800, 150));
	        debugViewFrame.setMaximumSize(new Dimension(displayWidth - 50, displayHeight - 75 - 55));
	
	        Iterator<JPanel>	panelIterator = debugViewPanels.iterator();
	
	        if (debugViewPanels.size() == 0) return;
	
	        while (panelIterator.hasNext()) {		// (panel = panelIterator.next()) != null)
	            addDebugView(panelIterator.next());
	        }
	
	        updateDebugView();
	
	//
	//      try {
	//          updateDebugLabels();
	//      } catch (Exception exception) {
	//          GrasppeKit.debugText("DebugView", exception.getMessage(), dbg);
	//      }
	//
	//      updateDebugFrame();
	    }

	/**
	 */
	private void updateDebugFrame() {
	
	        if (GrasppeKit.isRunningJar()) return;
	
	        debugViewFrame.pack();
	
	//      setFrameMenu(debugViewFrame);
	
	        DisplayMode	displayMode =
	            GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]
	                .getDisplayMode();
	        int	displayWidth  = displayMode.getWidth();
	        int	displayHeight = displayMode.getHeight();
	        int	frameWidth    = debugViewFrame.getWidth();
	        int	frameHeight   = debugViewFrame.getHeight();
	
	//      debugViewFrame.setLocation(displayWidth - frameWidth - 25, 55);
	        debugViewFrame.setLocation(25, displayHeight - frameHeight - 75);
	
	        if (!debugViewFrame.isVisible()) debugViewFrame.setVisible(true);
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
	    if (!debugLabels.containsKey(name.toLowerCase()))		// return;
	        createDebugLabel(name);
	
	    if (text.trim() == "") {
	        debugLabels.get(name.toLowerCase()).setText("");
	        debugLabels.get(name.toLowerCase()).setVisible(false);
	    } else {
	        debugLabels.get(name.toLowerCase()).setText("\t\t\t" + name + debugSeparator + text);
	        debugLabels.get(name.toLowerCase()).setVisible(true);
	    }
	
	    if (debugViewFrame != null) debugViewFrame.pack();
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
			
			//      } catch (Exception exception)
			    }

	/**
	 */
	protected void updateDebugLabels() {
	    if (GrasppeKit.isRunningJar()) return;
	
	    if (debugPanel == null)		// return;
	        createDebugView();
	
	    AbstractModel	model = getModel();
	
	    if (model == null) return;
	
	    try {
	        debugTitleLabel.setText("<html><b>" + getClass().getSimpleName() + "</b>&nbsp;("
	                                + getModel().observerString() + ")</html>");
	    } catch (Exception exception) {
	        debugTitleLabel.setText("<html><b>" + getClass().getSimpleName()
	                                + "</b>&nbsp;(?)</html>");
	        GrasppeKit.debugError("DebugView", exception, dbg);
	    }
	
	    return;
	}

	/**
	 */
	protected void updateDebugView() {
	    if (GrasppeKit.isRunningJar()) return;
	
	    try {
	        updateDebugLabels();
	    } catch (Exception exception) {
	        GrasppeKit.debugError("DebugView", exception, 10);
	        prepareDebugView();
	    }
	
	    updateDebugFrame();
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