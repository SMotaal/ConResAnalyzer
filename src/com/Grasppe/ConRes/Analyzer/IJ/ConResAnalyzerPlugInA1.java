/*
 * @(#)ConResAnalyzerPlugInA1.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe.ConRes.Analyzer.IJ;

import com.Grasppe.Common.StopWatch;
import com.Grasppe.GrasppeKit;
import com.Grasppe.GrasppeKit.AbstractCommand;
import com.Grasppe.GrasppeKit.AbstractController;
import com.Grasppe.GrasppeKit.AbstractModel;
import com.Grasppe.GrasppeKit.AbstractOperation;
import com.Grasppe.GrasppeKit.AbstractView;
import com.Grasppe.GrasppeKit.FileSelectionMode;

import ij.IJ;
import ij.ImagePlus;

import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;

import ij.io.Opener;

import ij.plugin.PlugIn;

import ij.util.Java2;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;

/**
 * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 *
 */

/**
 * @author daflair
 *
 */
public class ConResAnalyzerPlugInA1 implements PlugIn {

    protected static String			name      = ConResAnalyzerPlugInA1.class.getSimpleName();
    protected static int			exitDelay = 10 * 1000;
    protected static ConResAnalyzer	analyzer;

    /**
     * @deprecated  Java exits on last window anyway!
     */
    @Deprecated
    public static void delayedExit() {

        GrasppeKit.debugText("Delayed Exit (" + name + ")",
                             "Exiting if no windows open in the next " + (exitDelay / 1000)
                             + " seconds!", 2);

        int				delay         = exitDelay;		// milliseconds
        ActionListener	taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Frame[]	frames = Frame.getFrames();

                for (Frame frame : frames)
                    if (frame.isVisible()) return;		// visibleFrames++;

                System.exit(0);
            }
        };

        new Timer(delay, taskPerformer).start();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        new ConResAnalyzerPlugInA1().run("");

    }

    /**
     * @param x
     * @param y
     */
    public static void moveFrame(int x, int y) {
        if (Testing.jFrame == null) return;

        JFrame		frame       = Testing.jFrame;
        ImageWindow	imageWindow = Testing.imageWindow;

        if (imageWindow.getCanvas().getBounds().contains(x - imageWindow.getX(),
                y - imageWindow.getY())) {
            frame.setLocation(x - frame.getWidth() / 2, y - frame.getHeight() / 2);
            if (!frame.isVisible()) frame.setVisible(true);
        } else {
            frame.setLocation(25 - frame.getWidth(), 25 - frame.getHeight());
            if (frame.isVisible()) frame.setVisible(false);
        }
    }

    /**
     * Method description
     */
    public static void prepareFrame() {
        Testing.jFrame = new JFrame();		// "SpringLayout");
        Testing.jFrame.setUndecorated(true);
        Testing.jFrame.setSize(300, 300);
        Testing.jFrame.setMaximumSize(Testing.jFrame.getSize());
        Testing.jFrame.setAlwaysOnTop(true);
        Testing.jFrame.setFocusableWindowState(false);
        Testing.jFrame.setResizable(false);

        Testing.jFrame.setBackground(Color.black);

        MagnifierCanvas	zoomCanvas = new MagnifierCanvas(Testing.imageWindow.getImagePlus());

        zoomCanvas.addMouseListener(TestingListeners.IJMouseListener);
        zoomCanvas.addMouseMotionListener(TestingListeners.IJMotionListener);
        zoomCanvas.addMouseWheelListener(TestingListeners.IJWheelListener);
        zoomCanvas.setBackground(Color.black);

        //Testing.imageWindow.getCanvas().zoomIn(0, 0);

        Container	contentPane = Testing.jFrame.getContentPane();

        contentPane.setBackground(Color.BLACK);

        /** {@link http://www.java2s.com/Tutorial/Java/0240__Swing/SpringLayout.htm} */

        // SpringLayout layout = new SpringLayout();
        // contentPane.setLayout(layout);

        contentPane.add(zoomCanvas);

        int	padding = 0;

        GrasppeKit.debugText("Zoom Frame", "Content Pane " + contentPane.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Frame " + Testing.jFrame.getBounds().toString(), 3);
        GrasppeKit.debugText("Zoom Frame", "Zoom Canvas " + zoomCanvas.getBounds().toString(), 3);

//      layout.putConstraint(SpringLayout.NORTH, zoomCanvas, padding, SpringLayout.NORTH, contentPane);
//      layout.putConstraint(SpringLayout.EAST, zoomCanvas, padding, SpringLayout.EAST, contentPane);
//      layout.putConstraint(SpringLayout.SOUTH, zoomCanvas, padding, SpringLayout.SOUTH, contentPane);
//        layout.putConstraint(SpringLayout.WEST, zoomCanvas, padding, SpringLayout.WEST, contentPane);

        // zoomCanvas.setSize(contentPane.getWidth() - 2*padding, contentPane.getHeight() - 2*padding);
        // zoomCanvas.setLocation(0, 0);

        double	windowZoom = Testing.imageWindow.getCanvas().getMagnification();

        zoomCanvas.setMagnification(windowZoom * 2.0);

        Testing.jFrame.setVisible(true);

        Testing.jFrame.pack();
        Testing.jFrame.setSize(300, 300);

        Testing.zoomCanvas = zoomCanvas;

    }

    /**
     * Method description
     */
    public static void redrawFrame() {
        if (Testing.jFrame == null) return;
        
        if (Testing.showZoom = false) {Testing.jFrame.setVisible(false); return;}

        ImageWindow		imageWindow = Testing.imageWindow;
        ImageCanvas		imageCanvas = imageWindow.getCanvas();
        JFrame			zoomWindow  = Testing.jFrame;
        MagnifierCanvas	zoomCanvas  = Testing.zoomCanvas;
        
        Point	zoomWindowLocation, zoomLocation = null; 

        if (zoomWindow.isVisible()) {
            zoomWindowLocation = zoomWindow.getLocationOnScreen();		// Location on screen
            zoomLocation       = zoomCanvas.getLocationOnScreen();		// Location on screen
        }

        Point		zoomPosition        = zoomCanvas.getLocation();		// Position within component
        Dimension	zoomSize            = zoomCanvas.getSize();
        Double		zoomScale           = zoomCanvas.getMagnification();

        Point		imageWindowLocation = imageWindow.getLocationOnScreen();
        Point		imageLocation       = imageCanvas.getLocationOnScreen();
        Point		imagePosition       = imageCanvas.getLocation();
        Dimension	imageSize           = imageCanvas.getSize();
        Double		imageScale          = imageCanvas.getMagnification();

        Point		pointerCenter;

        try {
        /* Pointer Center: pointer location in image canvas in screen units */
        if (zoomWindow.isVisible()) {
        	pointerCenter = zoomCanvas.getMousePosition();
        	pointerCenter = new Point(zoomLocation.x - imageLocation.x + pointerCenter.x,
        			zoomLocation.y - imageLocation.y + pointerCenter.y);
        }
        else 
        	pointerCenter = imageCanvas.getMousePosition();
        } catch (Exception exception){
        	zoomWindow.setVisible(false);
        	pointerCenter = null;
        }
        
        if(pointerCenter == null) return;
        
        if (!zoomWindow.isVisible()) zoomWindow.setVisible(true); 

        /* Source Center: pointer location in source coordinate space in source units */
        Point	sourceCenter = new Point((int)(pointerCenter.x / imageScale),
                                       (int)(pointerCenter.y / imageScale));

        /* Source Size: source rectangle size in source units */
        Dimension	sourceSize = new Dimension((int)(zoomSize.width / zoomScale),
                                   (int)(zoomSize.height / zoomScale));
        
        /* Source Corner: source rectangle top-left in source units */
        Point	sourceCorner = new Point((int)(sourceCenter.x - sourceSize.width/2),
        								(int)(sourceCenter.y - sourceSize.height/2));
        
        /* Source Rectangle: source rectangle in source units */
        Rectangle sourceRectangle = new Rectangle(sourceCorner,sourceSize);
        
        String debugString = "";        		
        		debugString += "\t" + GrasppeKit.lastSplit(pointerCenter.toString());
        		debugString += "\t" + GrasppeKit.lastSplit(zoomSize.toString());
        		debugString += "\t" + GrasppeKit.lastSplit(sourceRectangle.toString());
        
        //GrasppeKit.debugText("Magnifier", GrasppeKit.lastSplit(pointerCenter.toString()));
        
        Testing.zoomCanvas.setSourceRect(sourceRectangle);
        
        Testing.zoomCanvas.repaint(0,0,zoomSize.width, zoomSize.height);

    }

    /*
     *  Center Image Position: Find zoom
     * /            if(mousePosition==null) return;
     * /        Point mouseLocation = new Point(mousePosition.x + zoomWindowLocation.x,
     * /                                        mousePosition.y + zoomWindowLocation.y);
     *
     * /        // Canvas Offset: Find top-left/zoom canvas relative top-left/image canvas in screen units
     * /        Point canvasOffset = new Point(zoomLocation.x - imageLocation.x,
     * /                                       zoomLocation.y - imageLocation.y);
     *
     * // Center Offset: Find center/zoom canvas relative to top-left/image canvas in screen units
     * Point   centerOffset = new Point(canvasOffset.x + zoomSize.width / 2,
     *                              canvasOffset.y + zoomSize.height / 2);
     *
     * // Image Coordinates: Find image coordinates for the centerOffset in image units
     * Point   imageCenter = new Point((int)(centerOffset.x / imageScale),
     *                              (int)(centerOffset.y / imageScale));
     *
     * // Zoom Center: Find zoom coordinates for source center in screen units
     * Point targetCenter = new Point((int)(imageCenter.x * zoomScale),(int)(imageCenter.y * zoomScale));
     *
     * // Zoom Offset: Find top-left/zoom canvas relative to zoom center coordinates in screen units
     * Point   targetOffset = new Point(targetCenter.x - zoomSize.width / 2,
     *                               targetCenter.y - zoomSize.height / 2);
     *
     * //GrasppeKit.debugText("Magnifier", zoomCanvas.getSrcRect().toString());
     *
     * /        zoomCanvas.setSourceRect(r)
     * // Testing.zoomCanvas.setSourceRect(new Rectangle(sX,sY, (int)(dW/zoom),(int)(dH/zoom)));
     *
     *
     * /      int[] sc = {cursorLocation.x - ds[0]/2, cursorLocation.y - ds[1]/2};
     * /      int[] sx = {sc[0], sc[0]+ds[0]};
     * /      int[] sy = {sc[1], sc[1]+ds[1]}
     * /      Point       cursorLocation = imageCanvas.getCursorLoc();
     * /      int cX = cursorLocation.x;
     * /      int cY = cursorLocation.y;
     * /      String cXY = "cXY: " + cX + ", " + cY;
     *
     * /      double zoom = Testing.zoomCanvas.getMagnification();
     * /      double wZoom = Testing.imageWindow.getCanvas().getMagnification();
     *
     *
     * /      //Testing.zoomCanvas.setSize(dW, dH);
     * /      imageWindow
     *
     * /      ImageWindow.
     *
     * /      if(frameCursor==null) return;
     * /      double mX = cursorPosition.
     * /      double mY = frameCursor.getY();
     * /      String mXY = "mXY: " + frameCursor.getX() + ", " + frameCursor.getY();
     *
     * /      Point windowPosition = Testing.imageWindow.getCanvas().getLocationOnScreen(); // .getLocation();//.getLocationOnScreen();
     * /      double wX = windowPosition.getX();
     * /      double wY = windowPosition.getY();
     * /      String fXY = "fXY: " + wX + ", " + wY;
     *
     * /      Point zoomPosition = Testing.zoomCanvas.getLocationOnScreen(); //Testing.jFrame.getLocation();  //.getLocationOnScreen();
     * /      double zX = zoomPosition.getX();
     * /      double zY = zoomPosition.getY();
     * /      String zXY = "zXY: " + zX + ", " + zY;
     *
     * /      double zRatio1 = zoom/wZoom;
     * /      double zRatio2 = wZoom/zoom;
     * /      double sW = dW/2; //(dW/2/zoom);
     * /      double sH = dH/4;
     * /      int sX = (int)(zX-wX);//(zX+(dW/2/zoom)-wX); //-windowPosition.getX())); //+mX-windowPosition.getX();
     * /      sX =(int)(sX/wZoom);
     * /      int sY = (int)(zY-wY);
     * /      sY =(int)(sY/wZoom);
     * /      //int sY = 0; //imageCanvas.offScreenY((int)(zY+(dH/2/zoom)-wY)); //-windowPosition.getY())); //+mY;
     * /      String sXY = "sXY: " + sX + ", " + sY;
     *
     * /      //int nX = Testing.zoomCanvas.offScreenX((int)sX); //Testing.zoomCanvas.screenX(
     * /      //int nW = Testing.zoomCanvas.offScreenX((int)ds[0]);
     * /      //int nY = imageCanvas.offScreenY((int)sY); //Testing.zoomCanvas.screenY(
     * ///        String nXY = "nXY: " + nX + ", " + nY;
     *
     * /      //Testing.zoomCanvas.setRect(nX, nY);
     *
     * /      Testing.zoomCanvas.setSourceRect(new Rectangle(sX,sY, (int)(dW/zoom),(int)(dH/zoom)));
     * /      //Testing.zoomCanvas.setImageUpdated();
     * /      Testing.zoomCanvas.repaint(0,0,dW, dH);
     * /      //Testing.jFrame.getContentPane().repaint();
     * /      //Testing.jFrame.repaint();
     * /      //Testing.zoomCanvas.zoomOut(cursorLocation.x, cursorLocation.y);
     * /      //Testing.zoomCanvas.zoomIn(cursorLocation.x, cursorLocation.y);
     * /      //Testing.zoomCanvas.adju //setCursor(0, 0, (int)sX, (int)sY); //, dx[1]/2, dy[1]/2);
     *
     * // Testing.zoomCanvas.repaint();
     *
     * // String zR = Testing.zoomCanvas.getSrcRect().toString(); //Testing.zoomCanvas.getBounds().toString();
     *
     * /        GrasppeKit.debugText("Magnifier", sXY + "\t" + fXY + "\t" + zXY + "\t" + cXY, 3);     // fXY + "\t" + mXY + "\t" + sXY, 3);
     *
     * /      Image zoomImage = imageCanvas.getImage().
     *
     * // Image newImage = frame.createImage(ds[0], ds[1]);
     * // newImage.getGraphics().dr
     *
     *
     * /        ImageWindow   imageWindow    = Testing.imageWindow;
     *
     *
     *
     * /        frame.repaint();
     * /        frame.getGraphics().drawImage(imageWindow.getImagePlus().getImage(), 0, 0, dWidth, dHeight,
     * /                                      cursorLocation.x - dWidth / 2,
     * /                                      cursorLocation.y - dHeight / 2,
     * /                                      cursorLocation.x + dWidth / 2,
     * /                                      cursorLocation.y + dHeight / 2, frame);
     *
     * // Testing.jFrame.paint(Testing.jFrame.getGraphics());
     * // Testing.jFrame.update(Testing.jFrame.getGraphics());
     *
     * @param arg
     */

    /**
     * Method description
     *
     * @param arg
     */
    public void run(String arg) {
        IJ.showMessage(name, "Hello world!");
        
        
        KeyboardFocusManager	manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        manager.addKeyEventDispatcher(new MyDispatcher());

        GrasppeKit.debugLevel = 4;

        // testAnalyzerMenu();
        testImageWindow();
        testImageJMouseListeners();
        testMagnifier();

//      JFrame    Testing.jFrame = new JFrame();
//
//      Testing.jFrame.setSize(500, 500);
//      Testing.jFrame.setVisible(true);
//      JComponent
//      GestureUtilities.registerListener(Testing.jFrame, new GesturePhaseListener() {
//
//          @Override
//          public void gestureEnded(GesturePhaseEvent e) {
//              String    eventLabel     = "Phase Ended";
//              Point cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();
//
//              GrasppeKit.debugText("Image Window Gesture Event",
//                                   "Gesture " + eventLabel + "\t" + e.toString(), 3);
//          }
//          @Override
//          public void gestureBegan(GesturePhaseEvent e) {
//              String    eventLabel     = "Phase Began";
//              Point cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();
//
//              GrasppeKit.debugText("Image Window Gesture Event",
//                                   "Gesture " + eventLabel + "\t" + e.toString(), 3);
//          }
//
//      });
        // Testing.imageWindow.getCanvas().addMouseListener();
        // Testing.imageWindow.add
        // Macro macro = new Macro();
        // Macro_Runner macroRunner = new MacroRunner();
        // analyzerView.show();
        // analyzerView.close();
    }

    /**
     * Method description
     */
    public void testAnalyzerMenu() {
        if (analyzer == null) analyzer = new ConResAnalyzer();

        /* Static Variables */

        /* Static Members */
        WindowListener	windowListener = TestingListeners.WindowEventListener;

        /* Local Variables */
        ConResAnalyzerModel	analyzerModel = analyzer.getModel();
        ConResAnalyzerView	analyzerView  = new ConResAnalyzerView(analyzer);

        /* Test Statements */
        analyzerView.prepareView(windowListener);

        /* Static Updates */

    }

    /**
     * Method description
     */
    public void testImageJMouseListeners() {

        /* Static Variables */

        /* Static Members */
        MouseMotionListener	motionListener = TestingListeners.IJMotionListener;
        MouseWheelListener	wheelListener  = TestingListeners.IJWheelListener;
        MouseListener		mouseListener  = TestingListeners.IJMouseListener;
        ImageWindow			imageWindow    = Testing.imageWindow;

        /* Local Variables */

        /* Test Statements */
        imageWindow.getCanvas().addMouseListener(mouseListener);
        imageWindow.getCanvas().addMouseMotionListener(motionListener);
        imageWindow.getCanvas().addMouseWheelListener(wheelListener);

        /* Static Updates */

    }

    /**
     * Opens an ImagePlus image using Opener and creates and displays it in an ImageWindow;
     */
    public void testImageWindow() {

        /* Static Variables */

        /* Static Members */
        ImageWindow		imageWindow    = Testing.imageWindow;
        WindowListener	windowListener = TestingListeners.WindowEventListener;

        /* Local Variables */
        Opener		opener;
        ImagePlus	imagePlus;

        /* Test Statements */

        Testing.startTimer();

        opener    = new Opener();
        imagePlus = opener.openImage(Testing.inputPath);	// .openURL(inputURL);
        Testing.checkTimer("Opened ImagePlus " + imagePlus.getTitle());

//      imagePlus.getProcessor().autoThreshold();     // 128);
//      Testing.checkTimer("Auto Threshold " + imagePlus.getTitle());

        Testing.imageWindow = new ImageWindow(imagePlus);		// Initialize static variable here
        imageWindow         = Testing.imageWindow;
        Testing.checkTimer("Created ImageWindow " + imagePlus.getTitle());

        // imageWindow.getCanvas().zoom100Percent();
        // Testing.checkTimer("Zoomed " + imagePlus.getTitle());

        Testing.imageWindow.getCanvas().fitToWindow();

        imageWindow.setVisible(true);
        Testing.checkTimer("ImageWindow Displayed " + imagePlus.getTitle());

        imageWindow.addWindowListener(windowListener);

        /* Static Updates */

    }

    /**
     * Method description
     */
    public void testMagnifier() {
        prepareFrame();

        /* Static Variables */

        /* Static Members */
        JFrame				frame          = Testing.jFrame;
        ImageWindow			imageWindow    = Testing.imageWindow;
        MouseMotionListener	motionListener = TestingListeners.IJMotionListener;

        /* Local Variables */

        /* Test Statements */
        frame.addMouseMotionListener(motionListener);
        imageWindow.getCanvas().addMouseMotionListener(motionListener);

        /* Static Updates */
    }

    /**
     * Method description
     */
    public void testProgessBar() {

        /* Static Variables */

        /* Static Members */

        /* Local Variables */

        /* Test Statements */

        /* Static Updates */

    }

    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseFileManager {}


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseFileManagerModel {}


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseFileManagerViews {}


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManager extends AbstractController implements ActionListener {

        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected ActionListener							actionListener;
        protected AbstractController						commandHandler = this;

        /**
         * Constructs ...
         */
        public CaseManager() {
            this(new CaseManagerModel());
        }

        /**
         * Constructs ...
         *
         * @param model
         */
        public CaseManager(CaseManagerModel model) {
            GrasppeKit.getInstance().super(model);
        }

//      /**
//       * Method description
//       *
//       * @param e
//       */
//      @Override
//      public void actionPerformed(ActionEvent e) {
//          try {
//              getCommand(e.getActionCommand()).execute();
//          } catch (Exception exception) {
//              IJ.showMessage(this.getClass().getSimpleName(),
//                             this.getClass().getSimpleName() + " Command Not Found: "
//                             + e.getActionCommand());
//          }
//
//          if (actionListener != null) actionListener.actionPerformed(e);
//      }
//
//      /**
//       * Method description
//       *
//       * @param command
//       */
//      public void putCommand(AbstractCommand command) {
//          commands.put(command.getName(), command);
//          IJ.showMessage(this.getClass().getSimpleName(),
//                         this.getClass().getSimpleName() + " Command Added: " + command.getName()
//                         + " :: " + command.toString());
//      }

        /**
         * Create and populate all commands from scratch.
         */
        public void createCommands() {
            commands = new LinkedHashMap<String, GrasppeKit.AbstractCommand>();
            putCommand(new NewCase(this));
            putCommand(new OpenCase(this));
            putCommand(new CloseCase(this));
        }

//
//      /**
//       * Method description
//       *
//       * @param name
//       *
//       * @return
//       */
//      public AbstractCommand getCommand(String name) {
//          return commands.get(name);
//      }
//
//      /**
//       * @return the commands
//       */
//      public LinkedHashMap<String, AbstractCommand> getCommands() {
//          return commands;
//      }

        /**
         * Return the controller's correctly-cast model
         *
         * @return
         */
        @Override
        public CaseManagerModel getModel() {
            return (CaseManagerModel)super.getModel();
        }

        /**
         * Sets controller's model to a CaseManagerModel and not any AbstractModel.
         *
         * @param newModel
         *
         * @throws IllegalAccessException
         */
        public void setModel(CaseManagerModel newModel) throws IllegalAccessException {
            super.setModel(newModel);
        }

        /**
         * Defines Case Manager's Close Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class CaseManagerCommand extends AbstractCommand {

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             * @param name
             */
            public CaseManagerCommand(ActionListener listener, String name) {
                GrasppeKit.getInstance().super(listener, name, false);
                setModel(((CaseManager)listener).getModel());
            }

            /**
             * Returns the correctly-cast model.
             *
             * @return
             */
            public CaseManagerModel getModel() {
                return (CaseManagerModel)model;
            }
        }


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/09
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class CaseManagerOperation extends AbstractOperation {

            /**
             * @param name
             */
            public CaseManagerOperation(String name) {
                GrasppeKit.getInstance().super(name);
            }
        }


        /**
         * Defines Case Manager's Close Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class CloseCase extends CaseManagerCommand {

            protected static final String	name        = "CloseCase";
            protected static final int		mnemonicKey = KeyEvent.VK_C;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public CloseCase(ActionListener listener) {
                super(listener, name);
                super.mnemonicKey = mnemonicKey;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                boolean	canProceed = !isCaseClosed();		// canExecute();

                GrasppeKit.debugText("Close Case Attempt", "will be checking isCaseClosed()", 4);
                if (!canProceed) return true;		// Action responded to in alternative scenario
                if (!altPressed())
                    canProceed = IJ.showMessageWithCancel(name,
                        "Do you want to close the current case?");
                if (!canProceed) return true;		// Action responded to in alternative scenario
                GrasppeKit.debugText("Close Case Proceeds", "User confirmed close.", 3);
                getModel().backgroundCase = getModel().currentCase;
                getModel().currentCase    = null;
                GrasppeKit.debugText("Closed Case Success",
                                     "Moved current case to background and cleared current case.",
                                     4);
                getModel().notifyObservers();

                // update();
                return true;	// Action responded to in intended scenario
            }

            /**
             * Method description
             *
             * @param keyEvent
             *
             * @return
             */
            public boolean quickClose(KeyEvent keyEvent) {
                boolean	canProceed;

                try {
                    canProceed = execute(true, keyEvent);		// Don't care if a case was closed
                    canProceed = isCaseClosed();				// Only care that no case is open!

                    // getModel().notifyObservers();
                } catch (Exception e) {

                    // forget about current case!
                    GrasppeKit.debugText("Close Case Attempt",
                                         "Failed to close case or no case was open!" + "\n\n"
                                         + e.toString(), 2);
                    canProceed = false;
                }

                return canProceed;
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();

                // TODO: Enable if open case, else disable
                canExecute(!isCaseClosed());	//

                // notifyObservers();
            }

            /**
             * Method description
             *
             * @return
             */
            public boolean isCaseClosed() {
                boolean	value = !(getModel().hasCurrentCase());

                GrasppeKit.debugText("isCaseClose", "" + value, 3);

                return value;
            }
        }


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/10
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class FileChooserOperation extends CaseManagerOperation {

            String	defaultChooserPath =
                "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";
            File				selectedFile;
            protected boolean	executable = true;
            JFileChooser		fileChooser;
            FileSelectionMode	fileSelectionMode = FileSelectionMode.FILES_AND_DIRECTORIES;
            TreeSet<FileFilter>	filters           = new TreeSet<FileFilter>();

            /**
             * Constructs ...
             *
             * @param name
             */
            public FileChooserOperation(String name) {
                super(name);
                prepareFileChooser();
            }

            /*
             *  (non-Javadoc)
             * @see com.Grasppe.GrasppeKit.AbstractOperation#perfomOperation()
             */

            /**
             * Method description
             *
             * @return
             */
            @Override
            protected boolean perfomOperation() {
                prepareFileChooser();
                if (fileChooser.showOpenDialog(GrasppeKit.commonFrame)
                        == JFileChooser.CANCEL_OPTION)
                    return false;

                // TODO: Inspect & Verify Scan Images / TDF are in selectedFile
                selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.isDirectory()) selectedFile = selectedFile.getParentFile();

                return true;
            }

            /**
             * Method description
             */
            public void prepareFileChooser() {
                fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(fileSelectionMode.value());

                // Add filters
                while (filters.iterator().hasNext())
                    fileChooser.addChoosableFileFilter(filters.iterator().next());

                // Setting initial chooser selection
                try {
                    File	defaultPath = new File(defaultChooserPath);

                    fileChooser.setSelectedFile(defaultPath);
                } catch (NullPointerException exception) {

                    // Not setting initial chooser selection
                }
            }

            /**
             * Method description
             *
             * @return
             */
            public boolean quickSelect() {
                boolean	canProceed = false;
                String	finalName  = GrasppeKit.humanCase(getName());

                try {
                    canProceed = execute(true);
                } catch (Exception e) {
                    GrasppeKit.debugText(finalName + " Failed",
                                         finalName + " threw a " + e.getClass().getSimpleName()
                                         + "\n\n" + e.toString(), 2);
                }

                return canProceed;
            }

            /**
             * @return the selectedFile
             */
            protected File getSelectedFile() {
                return selectedFile;
            }

            /**
             * @param selectedFile the selectedFile to set
             */
            protected void setSelectedFile(File selectedFile) {
                this.selectedFile = selectedFile;
            }
        }


        /**
         * Defines Case Manager's New Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class NewCase extends CaseManagerCommand {

            protected static final String	name        = "NewCase";
            protected static final int		mnemonicKey = KeyEvent.VK_N;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public NewCase(ActionListener listener) {
                super(listener, name);
                super.mnemonicKey = mnemonicKey;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                boolean	canProceed = canExecute();

                // if (!canProceed) return true;     // Action responded to in alternative scenario
                // TODO: Show imageFolderChooser, if can create new case with images, confirm and close case before creating new case
                // TODO: Show imageFolderChooser
                // TODO: Validate imageFolder structure (if not Show imageFolderChooser)
//              try {
//                  CloseCase closeCase = (CloseCase)commandHandler.getCommand("CloseCase");
//
//                  canProceed = closeCase.execute(false, getKeyEvent());     // Don't care if a case was closed
//                  canProceed = closeCase.isCaseClosed();        // Only care that no case is open!
//                  getModel().notifyObservers();
//              } catch (IllegalStateException e) {
//                  GrasppeKit.debugText("New Case Attempt",
//                                       "Failed to close case or no case was open!", 3);
//                  canProceed = true;
//              } catch (Exception e) {
//
//                  // forget about current case!
//                  GrasppeKit.debugText("New Case Attempt",
//                                       "Failed to close case or no case was open!" + "\n\n"
//                                       + e.toString(), 2);
//                  canProceed = false;
//              }
                // TODO: Confirm and close current case before attempting to switching cases
                canProceed =
                    ((CloseCase)commandHandler.getCommand("CloseCase")).quickClose(getKeyEvent());
                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                // TODO: Create new case in metadata entry state
                GrasppeKit.debugText("New Case Creation",
                                     "New case will be created and passed for metadata entry", 4);

                try {
                    getModel().newCase = getModel().newCaseModel();		// getModel().Case//new getModel()canProceed..CaseModel();
                    getModel().notifyObservers();
                    canProceed = true;
                } catch (Exception e) {
                    GrasppeKit.debugText("New Case Failure",
                                         "Failed to create new case" + "\n\n" + e.toString(), 2);
                    canProceed = false;
                }

                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                try {
                    getModel().currentCase    = getModel().newCase;		// Make current the new case
                    getModel().newCase        = null;					// Clear new case
                    getModel().backgroundCase = null;					// clear background case
                    canProceed                = true;
                    getModel().notifyObservers();
                } catch (Exception e) {
                    GrasppeKit.debugText("New Case Failure",
                                         "Failed to reorganize cases in the case manager model."
                                         + "\n\n" + e.toString(), 2);
                    canProceed = false;
                }

                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario
                GrasppeKit.debugText(
                    "New Case Success",
                    "Created new case and reorganize cases in the case manager model.", 3);

                return true;	// Action responded to in intended scenario
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();

                // TODO: Enable if open case, else disable
                canExecute(true);		// getModel().hasCurrentCase());
            }

//          /**
//           * @return the mnemonicKey
//           */
//          @Override
//          public int getMnemonicKey() {
//              return mnemonicKey;
//          }
        }


        /**
         * Defines Case Manager's New Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class OpenCase extends CaseManagerCommand {

            protected static final String	name        = "OpenCase";
            protected static final int		mnemonicKey = KeyEvent.VK_O;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public OpenCase(ActionListener listener) {
                super(listener, name);
                super.mnemonicKey = mnemonicKey;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                boolean				canProceed       = canExecute();
                SelectCaseFolder	selectCaseFolder = new SelectCaseFolder();

                GrasppeKit.debugText("Open Case Attempt", "Call SelectCaseFolder", 3);

//              try {
//                  canProceed = selectCaseFolder.execute(true);
//              } catch (Exception e) {
//                  GrasppeKit.debugText("Open Case Failed",
//                                       "SelectCaseFolder threw a " + e.getClass().getSimpleName()
//                                       + "\n\n" + e.toString(), 2);
//              }
                canProceed = selectCaseFolder.quickSelect();
                if (canProceed)
                    GrasppeKit.debugText("Open Case Selected",
                                         "SelectCaseFolder returned "
                                         + selectCaseFolder.getSelectedFile().getAbsolutePath(), 3);
                else
                    GrasppeKit.debugText("Open Case Cancled", "SelectCaseFolder was not completed",
                                         3);
                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                // TODO: Verify case folder!
                // TODO: Confirm and close current case before attempting to switching cases
                canProceed =
                    ((CloseCase)commandHandler.getCommand("CloseCase")).quickClose(getKeyEvent());
                if (!canProceed) return canExecute(true);		// Action responded to in alternative scenario

                // if a folder is selected!
                getModel().newCase = getModel().newCaseModel();		// getModel().Case//new getModel()canProceed..CaseModel();
                getModel().newCase.path  = selectCaseFolder.getSelectedFile().getAbsolutePath();
                getModel().newCase.title = selectCaseFolder.getSelectedFile().getName();
                getModel().currentCase   = getModel().newCase;
                getModel().notifyObservers();
                GrasppeKit.debugText("Open Case Success",
                                     "Created new CaseModel for " + getModel().currentCase.title
                                     + " and reorganize cases in the case manager model.", 3);

                return true;
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();
                canExecute(true);		// getModel().hasCurrentCase());
            }

//          /**
//           * Method description
//           */
//          public void execute() {
//              super.execute();
//              if (!canExecute()) return;
//
//              // TODO: Show caseChooser, then confirm and close case before opening chosen case
//              // TODO: Show caseFolderChooser, if can create open case, confirm and close case before opening the new case
//          }
        }


        /**
         * Class description
         *
         * @version        $Revision: 1.0, 11/11/09
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class SelectCaseFolder extends FileChooserOperation {

            protected static final String	name = "SelectCaseFolder";

//          String                            defaultCaseFolder =
//              "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";
//          File              selectedFile;
//          protected boolean executable = true;
            protected static final String	defaultChooserPath =
                "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples/Approval_Scans_ConRes26_FS";

            // File              selectedFile;
            protected boolean	executable = true;

            // JFileChooser      fileChooser;
            protected final FileSelectionMode	fileSelectionMode =
                FileSelectionMode.DIRECTORIES_ONLY;

            // TreeSet<FileFilter>   filters           = new TreeSet<FileFilter>();

            /**
             * Constructs ...
             */
            public SelectCaseFolder() {
                super(name);
            }
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManagerModel extends AbstractModel {

        /** Holds a case that is about to close until currentCase is not null */
        public CaseModel	backgroundCase = null;

        /** Field description */
        public CaseModel	currentCase = null;

        /** Field description */
        public CaseModel	newCase = null;

        /**
         * Constructs a new model object with no predefined controller.
         */
        public CaseManagerModel() {
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public CaseManagerModel(CaseManager controller) {
            GrasppeKit.getInstance().super(controller);
        }

        /**
         * Method description
         *
         * @return
         */
        public CaseModel newCaseModel() {
            return new CaseModel();
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean hasCurrentCase() {
            if (currentCase != null)
                GrasppeKit.debugText("Current Case", currentCase.toString(), 3);
            else GrasppeKit.debugText("Current Case", "null!", 3);

            return (currentCase != null);
        }

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
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class CaseManagerView extends AbstractView {

        /**
         * Constructs a new ConResAnalyzerView with a predefined controller.
         *
         * @param controller
         */
        public CaseManagerView(CaseManager controller) {
            GrasppeKit.getInstance().super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class ConResAnalyzer extends AbstractController implements ActionListener {

        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected CaseManager								caseManager;

        /**
         * Constructs and attaches a new controller and a new model.
         */
        public ConResAnalyzer() {
            this(new ConResAnalyzerModel());
            caseManager = (CaseManager)new CaseManager().withActionListener(this);

            // GrasppeKit.setDebugTimeStamp(3);
        }

        /**
         * Constructs a new controller and attaches it to the unattached model.
         *
         * @param model
         */
        public ConResAnalyzer(ConResAnalyzerModel model) {
            GrasppeKit.getInstance().super(model);

            // TODO Auto-generated constructor stub
        }

        /**
         * Create and populate all commands from scratch.
         */
        public void createCommands() {

            // commands = new LinkedHashMap<String, GrasppeKit.AbstractCommand>();
            putCommand(new Quit(this));
        }

        /**
         * Method description
         *
         * @return
         */
        public LinkedHashMap<String, AbstractCommand> getCommands() {
            return appendCommands(caseManager);
        }

        /**
         * Method description
         *
         * @return
         */
        @Override
        public ConResAnalyzerModel getModel() {

            // TODO Auto-generated method stub
            return (ConResAnalyzerModel)super.getModel();
        }

        /**
         * Method description
         *
         * @param newModel
         *
         * @throws IllegalAccessException
         */
        public void setModel(ConResAnalyzerModel newModel) throws IllegalAccessException {

            // TODO Auto-generated method stub
            super.setModel(newModel);
        }

        /**
         * Defines Case Manager's Close Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public abstract class ConResAnalyzerCommand extends AbstractCommand {

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             * @param name
             */
            public ConResAnalyzerCommand(ActionListener listener, String name) {
                GrasppeKit.getInstance().super(listener, name, false);
                setModel(((ConResAnalyzer)listener).getModel());
            }

            /**
             * Returns the correctly-cast model.
             *
             * @return
             */
            public ConResAnalyzerModel getModel() {
                return (ConResAnalyzerModel)model;
            }
        }


        /**
         * Defines Case Manager's Close Case actions and command, using the EAC pattern.
         *
         * @version        $Revision: 1.0, 11/11/08
         * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
         */
        public class Quit extends ConResAnalyzerCommand {

            protected static final String	name        = "Quit";
            protected static final int		mnemonicKey = KeyEvent.VK_Q;

            /**
             * Constructs a realization of AbstractCommand.
             *
             * @param listener
             */
            public Quit(ActionListener listener) {
                super(listener, name);
                super.mnemonicKey = mnemonicKey;
                executable        = true;
                update();
            }

            /**
             * Performs the command operations when called by execute().
             *
             * @return
             */
            public boolean perfomCommand() {
                if (altPressed() || IJ.showMessageWithCancel(name, "Do you really want to quit?"))
                    System.exit(0);

                return true;	// Action responded to in intended scenario
            }

            /**
             * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
             */
            @Override
            public void update() {
                super.update();

                // TODO: Enable if open case, else disable
                canExecute(true);
            }
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class ConResAnalyzerModel extends AbstractModel {

        /**
         * Constructs a new model object with no predefined controller.
         */
        public ConResAnalyzerModel() {
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public ConResAnalyzerModel(ConResAnalyzer controller) {
            GrasppeKit.getInstance().super(controller);
        }
    }

    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/09
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    private static class MyDispatcher implements KeyEventDispatcher {

        /** Field description */
        public int	lastKey;

        /** Field description */
        public int	lastModifier;

        /**
         * Method description
         *
         * @param e
         *
         * @return
         */
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
        	
//        	zoomFrame(e);

//            // if ((e.getKeyCode() < 65) || (e.getKeyCode() > 90)) return false;     // () >= 65 &&) return false;
//            if (e.getID() == KeyEvent.KEY_PRESSED) {			// System.out.println("tester");
//                //return keyPressed(e);
//            	zoomFrame(e);
//            } else if (e.getID() == KeyEvent.KEY_RELEASED) {	// System.out.println("2test2");
//            } else if (e.getID() == KeyEvent.KEY_TYPED) {
//
//                // System.out.println("3test3");
//            }

            return false;
        }
    }
    
    public static void zoomFrame (KeyEvent e) {
    	
//		if (e.getKeyCode() == KeyEvent.VK_ALT)
//			if (e.getID() == KeyEvent.KEY_PRESSED)
//    			Testing.showZoom = true;
//			else 
//				Testing.showZoom = false;
//		redrawFrame();
    }

    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class ConResAnalyzerView extends AbstractView {

        /** Field description */
        JFrame				frame;
        ConResAnalyzerMenu	menu;
        String				name        = "ConResAnalyzer";
        boolean				finalizable = true;
        int					activeCalls = 0;

        /**
         * Constructs a new ConResAnalyzerView with a predefined controller.
         *
         * @param controller
         */
        public ConResAnalyzerView(ConResAnalyzer controller) {
            GrasppeKit.getInstance().super(controller);

            // TODO Auto-generated constructor stub
        }

        /**
         * Method description
         *
         * @param listener
         */
        public void addWindowListener(WindowListener listener) {
            frame.addWindowListener(listener);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean canFinalize() {
            finalizable = (activeCalls == 0);

            // IJ.showMessage(name + " activeCalls: " + activeCalls);
            GrasppeKit.debugText("Finalize / Active Calls", activeCalls + " remaining.");

            return finalizable;
        }

        /**
         * Method description
         */
        public void close() {
            if (finalizeView()) frame.dispose();
        }

        /**
         * Completes graphical user interface operations before closing.
         *
         * @return
         */
        public boolean finalizeView() {
            if (!canFinalize()) return false;

            return true;
        }

        /**
         * Hides the graphical frame
         */
        public void hide() {
            frame.setVisible(false);	// frame.toBack();
        }

        /**
         * Builds the graphical user interface window.
         */
        public void prepareFrame() {
            frame = new JFrame(name);
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    close();
                }

            });
        }

        /**
         * Method description
         */
        public void prepareMenu() {
            menu = new ConResAnalyzerMenu();

            LinkedHashMap<String, AbstractCommand>	commands        = controller.getCommands();
            Collection<AbstractCommand>				commandSet      = commands.values();
            Iterator<AbstractCommand>				commandIterator = commandSet.iterator();

            while (commandIterator.hasNext()) {
                AbstractCommand	command = commandIterator.next();

                GrasppeKit.debugText("Command Button Creation",
                                     GrasppeKit.lastSplit(command.toString()), 3);

//              IJ.showMessage(this.getClass().getSimpleName(),
//                             this.getClass().getSimpleName() + " Command Added: "
//                             + command.toString());
                menu.createButton(command);
            }
        }

        /**
         * Builds the graphical user interface window and elements.
         */
        public void prepareView() {
            this.prepareMenu();
            this.prepareFrame();
        }

        /**
         * Builds the graphical user interface window and elements and adds the specified WindowListener to the frame.
         *
         * @param listener
         */
        public void prepareView(WindowListener listener) {
            this.prepareView();
            this.addWindowListener(listener);
        }

        /**
         * Shows the graphical frame
         */
        public void show() {
            frame.setVisible(true);		// frame.toFront();
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        public void update() {}

        /**
         * @return the title
         */
        protected String getTitle() {
            return frame.getTitle();
        }

        /**
         * @param title the title to set
         */
        protected void setTitle(String title) {
            frame.setTitle(title);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class MagnifierCanvas extends ImageCanvas {

        /**
         * Constructs ...
         *
         * @param imp
         */
        public MagnifierCanvas(ImagePlus imp) {
            super(imp);
        }

        /**
         * Method description
         *
         * @param g
         */
        public void paint(Graphics g) {
            try {
                Java2.setBilinearInterpolation(g, true);
                Java2.setAntialiased(g, true);

                Image	img = imp.getImage();

                if (img != null) g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                g.drawImage(img, 0, 0, (int)(srcRect.width * magnification),
                            (int)(srcRect.height * magnification), srcRect.x, srcRect.y,
                            srcRect.x + srcRect.width, srcRect.y + srcRect.height, null);

            } catch (OutOfMemoryError e) {
                IJ.outOfMemory("Paint");
            }
        }

        /**
         * Method description
         *
         * @param x
         * @param y
         */
        public void setRect(int x, int y) {

            double	newMag = 1;

            int		sx     = super.screenX(x);
            int		sy     = super.screenY(y);

            /* super.adjustSourceRect(1.0, sx, sy); */

            // IJ.log("adjustSourceRect1: "+newMag+" "+dstWidth+"  "+dstHeight);
            int	w = (int)Math.round(dstWidth / newMag);

            if (w * newMag < dstWidth) w++;

            int	h = (int)Math.round(dstHeight / newMag);

            if (h * newMag < dstHeight) h++;
            x = offScreenX(x);
            y = offScreenY(y);

            Rectangle	r = new Rectangle(x - w / 2, y - h / 2, w, h);

            if (r.x < 0) r.x = 0;
            if (r.y < 0) r.y = 0;
            if (r.x + w > imageWidth) r.x = imageWidth - w;
            if (r.y + h > imageHeight) r.y = imageHeight - h;
            srcRect = r;

            // setMagnification(newMag);
            repaint();

            // IJ.log("adjustSourceRect2: "+srcRect+" "+dstWidth+"  "+dstHeight);

        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManager extends AbstractController {

        /**
         * Constructs and attaches a new controller and a new model.
         */
        public TargetManager() {
            this(new TargetManagerModel());
        }

        /**
         * Constructs a new controller and attaches it to the unattached model.
         *
         * @param model
         */
        public TargetManager(TargetManagerModel model) {
            GrasppeKit.getInstance().super(model);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerModel extends AbstractModel {

        /**
         * Constructs a new model object with no predefined controller.
         */
        public TargetManagerModel() {
            GrasppeKit.getInstance().super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public TargetManagerModel(TargetManager controller) {
            GrasppeKit.getInstance().super(controller);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class TargetManagerView extends AbstractView {

        /**
         * Constructs ...
         *
         * @param controller
         */
        public TargetManagerView(AbstractController controller) {
            GrasppeKit.getInstance().super(controller);

            // TODO Auto-generated constructor stub
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class Testing {

        static ImageWindow		imageWindow;
        static JFrame			jFrame;
        static MagnifierCanvas	zoomCanvas;
        static StopWatch		timer      = new StopWatch();
        static String			rootFolder = "/Users/daflair/Documents/MATLAB/ConResAlpha/data/samples";
        static String			caseFolder = "Approval_Scans_ConRes26_FS";
        
        static boolean showZoom = false;

//        static String			imageName  = "CirRe27U_50t.png";
        static String     imageName  = "CirRe27U_50i.tif";

        static String	inputPath = (rootFolder + "/" + caseFolder + "/"
                                   + imageName).replaceAll("//", "/");

        /**
         * Outputs elapsed time with generic description without stopping or reseting the timer.
         * @param description
         */
        static void checkTimer(String description) {
            GrasppeKit.debugText("Elapsed Time",
                                 description + " in " + timer.getElapsedTime() + " ms.", 3);
        }

        /**
         * Outputs elapsed time with generic description and resets timer.
         */
        static void markTimer() {
            markTimer("Task finished in");
        }

        /**
         * Outputs elapsed time with specified description and resets timer.
         * @param description
         */
        static void markTimer(String description) {
            checkTimer(description);

//          GrasppeKit.debugText("Elapsed Time",
//                               description + " in " + timer.getElapsedTime() + " ms.", 3);
            startTimer();
        }

        /**
         * Starts timer.
         */
        static void startTimer() {
            timer.start();
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public static class TestingListeners {

        static int	debugginLevel = 5;

        /** Field description */
        public static MouseListener	IJMouseListener = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                debugEvent("IJMouseListener", e);
                e.consume();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                debugEvent("IJMouseListener", e);
                e.consume();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                debugEvent("IJMouseListener", e);
                redrawFrame();
                e.consume();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                debugEvent("IJMouseListener", e);
                e.consume();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                debugEvent("IJMouseListener", e);
                e.consume();
            }
        };

        /** Field description */
        public static MouseWheelListener	IJWheelListener = new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                debugEvent("IJWheelListener", e);
                e.consume();
            }
        };

        // Testing.imageWindow.getCanvas().addMouseMotionListener(

        /** Field description */
        public static MouseMotionListener	IJMotionListener = new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {

                // e.getSource().getClass().equals(ImageCanvas.class);
                moveFrame(e.getXOnScreen(), e.getYOnScreen());
                redrawFrame();
                debugEvent("IJMotionListener", e);
                e.consume();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                debugEvent("IJMotionListener", e);
                e.consume();
            }
        };

        /** Field description */
        public static WindowListener	WindowEventListener = new WindowListener() {

            @Override
            public void windowActivated(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Activated (" + name + ")", e.toString());
            }
            @Override
            public void windowClosed(WindowEvent e) {
                Frame[]	frames        = Frame.getFrames();

                int		visibleFrames = 0;

                for (Frame frame : frames)
                    if (frame.isVisible()) visibleFrames++;

                debugEvent("Window", e);	// GrasppeKit.debugText("Window Closed (" + name + ")", e.toString());
                if ((visibleFrames == 1) && Testing.jFrame.isVisible()) delayedExit();
                if (visibleFrames == 0) delayedExit();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Closing (" + name + ")", e.toString());
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Deactivated (" + name + ")", e.toString());
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Deiconified (" + name + ")", e.toString());
            }
            @Override
            public void windowIconified(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Iconified (" + name + ")", e.toString());
            }
            @Override
            public void windowOpened(WindowEvent e) {
                debugEvent("Window", e);	// GrasppeKit.debugText("Window Opened (" + name + ")", e.toString());
            }
        };

        /**
         * Outputs debug information and event details
         * @param grouping
         * @param e
         */
        static void debugEvent(String grouping, MouseEvent e) {
            debugEvent(GrasppeKit.getCaller().methodName, grouping, e, debugginLevel);
        }

        /**
         * Outputs debug information and event details
         * @param grouping
         * @param e
         */
        static void debugEvent(String grouping, WindowEvent e) {
            debugEvent(GrasppeKit.getCaller().methodName, grouping, e, debugginLevel);
        }

        /**
         * Outputs debug information and event details
         * @param label
         * @param grouping
         * @param e
         * @param level
         */
        static void debugEvent(String label, String grouping, MouseEvent e, int level) {
            String	cursorString = "";

            try {
                Point	cursorLocation = Testing.imageWindow.getCanvas().getCursorLoc();

                cursorString = "\t" + cursorLocation.toString();
            } catch (Exception exception) {}

            GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + cursorString,
                                 level);
        }

        /**
         * Outputs debug information and event details
         * @param label
         * @param grouping
         * @param e
         * @param level
         */
        static void debugEvent(String label, String grouping, WindowEvent e, int level) {
            String	testString = "";

            GrasppeKit.debugText((grouping + " Event").trim(), "Mouse " + label + testString,
                                 level);
        }
    }
}
