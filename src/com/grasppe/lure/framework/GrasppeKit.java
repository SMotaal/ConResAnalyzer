/*
 * @(#)GrasppeCommon.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.lure.framework;

import ij.IJ;

import ij.io.FileInfo;

//~--- JDK imports ------------------------------------------------------------

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import java.security.Permission;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Abstract super-classes with common design patterns.
 *
 * @version        $Revision: 0.1, 11/11/08
 * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
 */
public class GrasppeKit {

    /** Field description */
    private static int	debugLevel = 3;		// default level is 3
    private static int	debugDefault = 4;		// default level is 3

    /** Field description */
    private static int	timestampLevel = 4;		// default level is 3

    /** Field description */
    public static final JFrame	commonFrame = new JFrame();

    /** Field description */
    public static boolean	debugNatively = true;
    private static boolean	isHooked      = false;

    /**
     * Constructs an instance of this class but is meant to be used internally only, it is made public for convenience.
     */
    private GrasppeKit() {
        super();
        setDebugTimeStamp(timestampLevel);

    }

    /**
     */
    public enum ExitCodes implements IIntegerValue {
        PENDING(-1, "Operation still running"),				// Asking about exit code when still not finished!
        SUCCESS(0, "Done"),									// Normal completion of operation
        FAILED(1, "Failed"),								// Interrupted execution with unrecoverable internal error
        INACCESSIBLERESOURCE(2, "Failed Resource Busy"),	// Interrupted operation with unrecoverable due to an inaccessible resource
        UNEXPECTED(3, "Invalid or unverfied selection"),	// User selection was not be validated / verified
        CANCELED(10, "Cancled"),		// User canceled the operation
        FORCED(20, "Interrupted");		// Interrupted execution initiated externally

        private int		value;
        private String	description;

        /**
         *  @param value
         *  @param description
         */
        private ExitCodes(int value, String description) {
            this.value       = value;
            this.description = description;
        }

        /**
         *  @return
         */
        public String description() {
            return description;
        }

        /**
         *  @return
         */
        public int value() {
            return value;
        }

        /**
         *  @param value
         *  @return
         */
        public static IIntegerValue get(int value) {
            return GrasppeKit.lookupByEnumValue(value, ExitCodes.class);
        }
    }

    /**
     * Enumeration of javax.swing.JFileChooser file selection mode constants
     */
    public enum FileSelectionMode {
        FILES_ONLY(JFileChooser.FILES_ONLY),
        FILES_AND_DIRECTORIES(JFileChooser.FILES_AND_DIRECTORIES),
        DIRECTORIES_ONLY(JFileChooser.DIRECTORIES_ONLY);

        private int	code;

        /**
         * Private constructor
         * @param code   integer or constant variable for the specific enumeration
         */
        private FileSelectionMode(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static FileSelectionMode get(int code) {
            return values()[code];
        }
    }

    /**
     * Enumeration of java.awt.event.KeyEvent event id constants
     */
    public enum IJCompression {		// fileInfo.compression
        NONE(FileInfo.COMPRESSION_NONE), UNKNOWN(FileInfo.COMPRESSION_UNKNOWN);

        private static final Map<Integer, IJCompression>	lookup = new HashMap<Integer,
                                                                      IJCompression>();

        static {
            for (IJCompression s : EnumSet.allOf(IJCompression.class))
                lookup.put(s.value(), s);
        }

        private int	code;

        /**
         * @param code   integer or constant variable for the specific enumeration
         */
        private IJCompression(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static IJCompression get(int code) {
            return lookup.get(code);
        }
    }

    /**
     * Enumeration of java.awt.event.KeyEvent virtual key code constants
     * {@link http://download.oracle.com/javase/1.4.2/docs/api/java/awt/event/KeyEvent.html}
     */
    public enum KeyCode {
        VK_0(KeyEvent.VK_0),							// VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39)
        VK_1(KeyEvent.VK_1), VK_2(KeyEvent.VK_2), VK_3(KeyEvent.VK_3), VK_4(KeyEvent.VK_4),
        VK_5(KeyEvent.VK_5), VK_6(KeyEvent.VK_6), VK_7(KeyEvent.VK_7), VK_8(KeyEvent.VK_8),
        VK_9(KeyEvent.VK_9), VK_A(KeyEvent.VK_A),		// VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A)
        VK_ACCEPT(KeyEvent.VK_ACCEPT),						// Constant for the Accept or Commit function key.
        VK_ADD(KeyEvent.VK_ADD),							//
        VK_AGAIN(KeyEvent.VK_AGAIN),						//
        VK_ALL_CANDIDATES(KeyEvent.VK_ALL_CANDIDATES),		// Constant for the All Candidates function key.
        VK_ALPHANUMERIC(KeyEvent.VK_ALPHANUMERIC),						// Constant for the Alphanumeric function key.
        VK_ALT(KeyEvent.VK_ALT),										//
        VK_ALT_GRAPH(KeyEvent.VK_ALT_GRAPH),							// Constant for the AltGraph function key.
        VK_AMPERSAND(KeyEvent.VK_AMPERSAND),							//
        VK_ASTERISK(KeyEvent.VK_ASTERISK),								//
        VK_AT(KeyEvent.VK_AT),											// Constant for the "@" key.
        VK_B(KeyEvent.VK_B),											//
        VK_BACK_QUOTE(KeyEvent.VK_BACK_QUOTE),							//
        VK_BACK_SLASH(KeyEvent.VK_BACK_SLASH),							//
        VK_BACK_SPACE(KeyEvent.VK_BACK_SPACE),							//
        VK_BRACELEFT(KeyEvent.VK_BRACELEFT),							//
        VK_BRACERIGHT(KeyEvent.VK_BRACERIGHT),							//
        VK_C(KeyEvent.VK_C),											//
        VK_CANCEL(KeyEvent.VK_CANCEL),									//
        VK_CAPS_LOCK(KeyEvent.VK_CAPS_LOCK),							//
        VK_CIRCUMFLEX(KeyEvent.VK_CIRCUMFLEX),							// Constant for the "^" key.
        VK_CLEAR(KeyEvent.VK_CLEAR),									//
        VK_CLOSE_BRACKET(KeyEvent.VK_CLOSE_BRACKET),					//
        VK_CODE_INPUT(KeyEvent.VK_CODE_INPUT),							// Constant for the Code Input function key.
        VK_COLON(KeyEvent.VK_COLON),									// Constant for the ":" key.
        VK_COMMA(KeyEvent.VK_COMMA),									//
        VK_COMPOSE(KeyEvent.VK_COMPOSE),								// Constant for the Compose function key.
        VK_CONTROL(KeyEvent.VK_CONTROL),								//
        VK_CONVERT(KeyEvent.VK_CONVERT),								// Constant for the Convert function key.
        VK_COPY(KeyEvent.VK_COPY),										//
        VK_CUT(KeyEvent.VK_CUT),										//
        VK_D(KeyEvent.VK_D),											//
        VK_DEAD_ABOVEDOT(KeyEvent.VK_DEAD_ABOVEDOT),					//
        VK_DEAD_ABOVERING(KeyEvent.VK_DEAD_ABOVERING),					//
        VK_DEAD_ACUTE(KeyEvent.VK_DEAD_ACUTE),							//
        VK_DEAD_BREVE(KeyEvent.VK_DEAD_BREVE),							//
        VK_DEAD_CARON(KeyEvent.VK_DEAD_CARON),							//
        VK_DEAD_CEDILLA(KeyEvent.VK_DEAD_CEDILLA),						//
        VK_DEAD_CIRCUMFLEX(KeyEvent.VK_DEAD_CIRCUMFLEX),				//
        VK_DEAD_DIAERESIS(KeyEvent.VK_DEAD_DIAERESIS),					//
        VK_DEAD_DOUBLEACUTE(KeyEvent.VK_DEAD_DOUBLEACUTE),				//
        VK_DEAD_GRAVE(KeyEvent.VK_DEAD_GRAVE),							//
        VK_DEAD_IOTA(KeyEvent.VK_DEAD_IOTA),							//
        VK_DEAD_MACRON(KeyEvent.VK_DEAD_MACRON),						//
        VK_DEAD_OGONEK(KeyEvent.VK_DEAD_OGONEK),						//
        VK_DEAD_SEMIVOICED_SOUND(KeyEvent.VK_DEAD_SEMIVOICED_SOUND),	//
        VK_DEAD_TILDE(KeyEvent.VK_DEAD_TILDE),							//
        VK_DEAD_VOICED_SOUND(KeyEvent.VK_DEAD_VOICED_SOUND),			//
        VK_DECIMAL(KeyEvent.VK_DECIMAL),								//
        VK_DELETE(KeyEvent.VK_DELETE),									//
        VK_DIVIDE(KeyEvent.VK_DIVIDE),									//
        VK_DOLLAR(KeyEvent.VK_DOLLAR),									// Constant for the "$" key.
        VK_DOWN(KeyEvent.VK_DOWN),										// Constant for the non-numpad down arrow key.
        VK_E(KeyEvent.VK_E),											//
        VK_END(KeyEvent.VK_END),										//
        VK_ENTER(KeyEvent.VK_ENTER),									//
        VK_EQUALS(KeyEvent.VK_EQUALS),									//
        VK_ESCAPE(KeyEvent.VK_ESCAPE),									//
        VK_EURO_SIGN(KeyEvent.VK_EURO_SIGN),							// Constant for the Euro currency sign key.
        VK_EXCLAMATION_MARK(KeyEvent.VK_EXCLAMATION_MARK),				// Constant for the "!" key.
        VK_F(KeyEvent.VK_F),											//
        VK_F1(KeyEvent.VK_F1),											// Constant for the F1 function key.
        VK_F10(KeyEvent.VK_F10),										// Constant for the F10 function key.
        VK_F11(KeyEvent.VK_F11),										// Constant for the F11 function key.
        VK_F12(KeyEvent.VK_F12),										// Constant for the F12 function key.
        VK_F13(KeyEvent.VK_F13),										// Constant for the F13 function key.
        VK_F14(KeyEvent.VK_F14),										// Constant for the F14 function key.
        VK_F15(KeyEvent.VK_F15),										// Constant for the F15 function key.
        VK_F16(KeyEvent.VK_F16),										// Constant for the F16 function key.
        VK_F17(KeyEvent.VK_F17),										// Constant for the F17 function key.
        VK_F18(KeyEvent.VK_F18),										// Constant for the F18 function key.
        VK_F19(KeyEvent.VK_F19),										// Constant for the F19 function key.
        VK_F2(KeyEvent.VK_F2),											// Constant for the F2 function key.
        VK_F20(KeyEvent.VK_F20),										// Constant for the F20 function key.
        VK_F21(KeyEvent.VK_F21),										// Constant for the F21 function key.
        VK_F22(KeyEvent.VK_F22),										// Constant for the F22 function key.
        VK_F23(KeyEvent.VK_F23),										// Constant for the F23 function key.
        VK_F24(KeyEvent.VK_F24),										// Constant for the F24 function key.
        VK_F3(KeyEvent.VK_F3),											// Constant for the F3 function key.
        VK_F4(KeyEvent.VK_F4),											// Constant for the F4 function key.
        VK_F5(KeyEvent.VK_F5),											// Constant for the F5 function key.
        VK_F6(KeyEvent.VK_F6),											// Constant for the F6 function key.
        VK_F7(KeyEvent.VK_F7),											// Constant for the F7 function key.
        VK_F8(KeyEvent.VK_F8),											// Constant for the F8 function key.
        VK_F9(KeyEvent.VK_F9),											// Constant for the F9 function key.
        VK_FINAL(KeyEvent.VK_FINAL),									//
        VK_FIND(KeyEvent.VK_FIND),										//
        VK_FULL_WIDTH(KeyEvent.VK_FULL_WIDTH),							// Constant for the Full-Width Characters function key.
        VK_G(KeyEvent.VK_G),						//
        VK_GREATER(KeyEvent.VK_GREATER),			//
        VK_H(KeyEvent.VK_H),						//
        VK_HALF_WIDTH(KeyEvent.VK_HALF_WIDTH),		// Constant for the Half-Width Characters function key.
        VK_HELP(KeyEvent.VK_HELP),									//
        VK_HIRAGANA(KeyEvent.VK_HIRAGANA),							// Constant for the Hiragana function key.
        VK_HOME(KeyEvent.VK_HOME),									//
        VK_I(KeyEvent.VK_I),										//
        VK_INPUT_METHOD_ON_OFF(KeyEvent.VK_INPUT_METHOD_ON_OFF),	// Constant for the input method on/off key.
        VK_INSERT(KeyEvent.VK_INSERT),											//
        VK_INVERTED_EXCLAMATION_MARK(KeyEvent.VK_INVERTED_EXCLAMATION_MARK),	// Constant for the inverted exclamation mark key.
        VK_J(KeyEvent.VK_J),									//
        VK_JAPANESE_HIRAGANA(KeyEvent.VK_JAPANESE_HIRAGANA),	// Constant for the Japanese-Hiragana function key.
        VK_JAPANESE_KATAKANA(KeyEvent.VK_JAPANESE_KATAKANA),	// Constant for the Japanese-Katakana function key.
        VK_JAPANESE_ROMAN(KeyEvent.VK_JAPANESE_ROMAN),		// Constant for the Japanese-Roman function key.
        VK_K(KeyEvent.VK_K),										//
        VK_KANA(KeyEvent.VK_KANA),									//
        VK_KANA_LOCK(KeyEvent.VK_KANA_LOCK),						// Constant for the locking Kana function key.
        VK_KANJI(KeyEvent.VK_KANJI),								//
        VK_KATAKANA(KeyEvent.VK_KATAKANA),							// Constant for the Katakana function key.
        VK_KP_DOWN(KeyEvent.VK_KP_DOWN),							// Constant for the numeric keypad down arrow key.
        VK_KP_LEFT(KeyEvent.VK_KP_LEFT),							// Constant for the numeric keypad left arrow key.
        VK_KP_RIGHT(KeyEvent.VK_KP_RIGHT),							// Constant for the numeric keypad right arrow key.
        VK_KP_UP(KeyEvent.VK_KP_UP),								// Constant for the numeric keypad up arrow key.
        VK_L(KeyEvent.VK_L),										//
        VK_LEFT(KeyEvent.VK_LEFT),									// Constant for the non-numpad left arrow key.
        VK_LEFT_PARENTHESIS(KeyEvent.VK_LEFT_PARENTHESIS),			// Constant for the "(" key.
        VK_LESS(KeyEvent.VK_LESS),									//
        VK_M(KeyEvent.VK_M),										//
        VK_META(KeyEvent.VK_META),									//
        VK_MINUS(KeyEvent.VK_MINUS),								// Constant for the "-" key.
        VK_MODECHANGE(KeyEvent.VK_MODECHANGE),						//
        VK_MULTIPLY(KeyEvent.VK_MULTIPLY),							//
        VK_N(KeyEvent.VK_N),										//
        VK_NONCONVERT(KeyEvent.VK_NONCONVERT),						// Constant for the Don't Convert function key.
        VK_NUM_LOCK(KeyEvent.VK_NUM_LOCK),							//
        VK_NUMBER_SIGN(KeyEvent.VK_NUMBER_SIGN),					// Constant for the "#" key.
        VK_NUMPAD0(KeyEvent.VK_NUMPAD0),							//
        VK_NUMPAD1(KeyEvent.VK_NUMPAD1),							//
        VK_NUMPAD2(KeyEvent.VK_NUMPAD2),							//
        VK_NUMPAD3(KeyEvent.VK_NUMPAD3),							//
        VK_NUMPAD4(KeyEvent.VK_NUMPAD4),							//
        VK_NUMPAD5(KeyEvent.VK_NUMPAD5),							//
        VK_NUMPAD6(KeyEvent.VK_NUMPAD6),							//
        VK_NUMPAD7(KeyEvent.VK_NUMPAD7),							//
        VK_NUMPAD8(KeyEvent.VK_NUMPAD8),							//
        VK_NUMPAD9(KeyEvent.VK_NUMPAD9),							//
        VK_O(KeyEvent.VK_O),										//
        VK_OPEN_BRACKET(KeyEvent.VK_OPEN_BRACKET),					//
        VK_P(KeyEvent.VK_P),										//
        VK_PAGE_DOWN(KeyEvent.VK_PAGE_DOWN),						//
        VK_PAGE_UP(KeyEvent.VK_PAGE_UP),							//
        VK_PASTE(KeyEvent.VK_PASTE),								//
        VK_PAUSE(KeyEvent.VK_PAUSE),								//
        VK_PERIOD(KeyEvent.VK_PERIOD),								//
        VK_PLUS(KeyEvent.VK_PLUS),									// Constant for the "+" key.
        VK_PREVIOUS_CANDIDATE(KeyEvent.VK_PREVIOUS_CANDIDATE),		// Constant for the Previous Candidate function key.
        VK_PRINTSCREEN(KeyEvent.VK_PRINTSCREEN),				//
        VK_PROPS(KeyEvent.VK_PROPS),							//
        VK_Q(KeyEvent.VK_Q),									//
        VK_QUOTE(KeyEvent.VK_QUOTE),							//
        VK_QUOTEDBL(KeyEvent.VK_QUOTEDBL),						//
        VK_R(KeyEvent.VK_R),									//
        VK_RIGHT(KeyEvent.VK_RIGHT),							// Constant for the non-numpad right arrow key.
        VK_RIGHT_PARENTHESIS(KeyEvent.VK_RIGHT_PARENTHESIS),	// Constant for the ")" key.
        VK_ROMAN_CHARACTERS(KeyEvent.VK_ROMAN_CHARACTERS),		// Constant for the Roman Characters function key.
        VK_S(KeyEvent.VK_S),						//
        VK_SCROLL_LOCK(KeyEvent.VK_SCROLL_LOCK),	//
        VK_SEMICOLON(KeyEvent.VK_SEMICOLON),		//
        VK_SEPARATER(KeyEvent.VK_SEPARATER),		// This constant is obsolete, and is included only for backwards compatibility.
        VK_SEPARATOR(KeyEvent.VK_SEPARATOR),	// Constant for the Numpad Separator key.
        VK_SHIFT(KeyEvent.VK_SHIFT),			//
        VK_SLASH(KeyEvent.VK_SLASH),			//
        VK_SPACE(KeyEvent.VK_SPACE),			//
        VK_STOP(KeyEvent.VK_STOP),				//
        VK_SUBTRACT(KeyEvent.VK_SUBTRACT),		//
        VK_T(KeyEvent.VK_T),					//
        VK_TAB(KeyEvent.VK_TAB),				//
        VK_U(KeyEvent.VK_U),					//
        VK_UNDEFINED(KeyEvent.VK_UNDEFINED),	// This value is used to indicate that the keyCode is unknown.
        VK_UNDERSCORE(KeyEvent.VK_UNDERSCORE),		// Constant for the "_" key.
        VK_UNDO(KeyEvent.VK_UNDO),					//
        VK_UP(KeyEvent.VK_UP),						// Constant for the non-numpad up arrow key.
        VK_V(KeyEvent.VK_V),						//
        VK_W(KeyEvent.VK_W),						//
        VK_X(KeyEvent.VK_X),						//
        VK_Y(KeyEvent.VK_Y),						//
        VK_Z(KeyEvent.VK_Z);

        private static final Map<Integer, KeyCode>	lookup = new HashMap<Integer, KeyCode>();

        static {
            for (KeyCode s : EnumSet.allOf(KeyCode.class))
                lookup.put(s.value(), s);
        }

        private int	code;

        /**
         * Private constructor
         * @param code   integer or constant variable for the specific enumeration
         */
        private KeyCode(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static KeyCode get(int code) {
            return lookup.get(code);
        }
    }

    /**
     * Enumeration of java.awt.event.KeyEvent event id constants
     */
    public enum KeyEventID {
        TYPED(KeyEvent.KEY_TYPED), PRESSED(KeyEvent.KEY_PRESSED), RELEASED(KeyEvent.KEY_RELEASED);

        private static final Map<Integer, KeyEventID>	lookup = new HashMap<Integer, KeyEventID>();

        static {
            for (KeyEventID s : EnumSet.allOf(KeyEventID.class))
                lookup.put(s.value(), s);
        }

        private int	code;

        /**
         * Private constructor
         * @param code   integer or constant variable for the specific enumeration
         */
        private KeyEventID(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static KeyEventID get(int code) {
            return lookup.get(code);
        }
    }

    /**
     * Enumeration of java.awt.event.KeyEvent key location constants
     */
    public enum KeyLocation {
        STANDARD(KeyEvent.KEY_LOCATION_STANDARD), LEFT(KeyEvent.KEY_LOCATION_LEFT),
        RIGHT(KeyEvent.KEY_LOCATION_RIGHT), NUMPAD(KeyEvent.KEY_LOCATION_NUMPAD),
        UNKNOWN(KeyEvent.KEY_LOCATION_UNKNOWN);

        private static final Map<Integer, KeyLocation>	lookup = new HashMap<Integer, KeyLocation>();

        static {
            for (KeyLocation s : EnumSet.allOf(KeyLocation.class))
                lookup.put(s.value(), s);
        }

        private int	code;

        /**
         * Private constructor
         * @param code   integer or constant variable for the specific enumeration
         */
        private KeyLocation(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static KeyLocation get(int code) {
            return lookup.get(code);
        }
    }

    /**
     */
    public enum OperatingSystem implements IIntegerValue {
        UNKNOWN(0, "Unknown"), LINUX(1, "Linux"), UNIX(2, "Unix"), MAC(3, "Mac currentOSString"),
        WINDOWS(4, "Windows");

        private static String			currentOSString = null;
        private static OperatingSystem	currentOS       = null;
        private int						value;
        private String					description;

        /**
         *  @param value
         *  @param description
         */
        private OperatingSystem(int value, String description) {
            this.value       = value;
            this.description = description;
        }

        /**
         *  @return
         */
        public static OperatingSystem currentSystem() {
            if (currentOS == null) {
                if (isOther()) return OperatingSystem.UNKNOWN;
                if (isUnix()) return OperatingSystem.LINUX;
                if (isUnix()) return OperatingSystem.UNIX;
                if (isMac()) return OperatingSystem.MAC;
                if (isWindows()) return OperatingSystem.WINDOWS;
            }

            return currentOS;
        }

        /**
         *  @return
         */
        public String description() {
            return description;
        }

        /**
         *  @return
         */
        public int value() {
            return value;
        }

        /**
         *  @param value
         *  @return
         */
        public static IIntegerValue get(int value) {
            return GrasppeKit.lookupByEnumValue(value, ExitCodes.class);
        }

        /**
         *  @return
         */
        public static String getOsName() {
            if (currentOSString == null) {
                currentOSString = System.getProperty("os.name");
            }

            return currentOSString;
        }

        /**
         *  @return
         */
        public static boolean isLinux() {
            return getOsName().contains("Linux");
        }

        /**
         *  @return
         */
        public static boolean isMac() {
            return getOsName().startsWith("Mac");
        }

        /**
         *  @return
         */
        public static boolean isOther() {
            return !isWindows() &&!isMac() &&!isUnix() &&!isLinux();
        }

        /**
         *  @return
         */
        public static boolean isUnix() {
            return getOsName().startsWith("Unix");
        }

        /**
         *  @return
         */
        public static boolean isWindows() {
            return getOsName().startsWith("Windows");
        }
    }

    /**
     * Enumeration of java.awt.event.WindowEvent constants
     */
    public enum WindowEventType {								// #(WindowEvent.#), //
        WINDOW_ACTIVATED(WindowEvent.WINDOW_ACTIVATED),			// The window-activated event type.
        WINDOW_CLOSED(WindowEvent.WINDOW_CLOSED),				// The window closed event.
        WINDOW_CLOSING(WindowEvent.WINDOW_CLOSING),				// The "window is closing" event.
        WINDOW_DEACTIVATED(WindowEvent.WINDOW_DEACTIVATED),		// The window-deactivated event type.
        WINDOW_DEICONIFIED(WindowEvent.WINDOW_DEICONIFIED),		// The window deiconified event type.
        WINDOW_FIRST(WindowEvent.WINDOW_FIRST),					// The first number in the range of ids used for window events.
        WINDOW_GAINED_FOCUS(WindowEvent.WINDOW_GAINED_FOCUS),		// The window-gained-focus event type.
        WINDOW_ICONIFIED(WindowEvent.WINDOW_ICONIFIED),				// The window iconified event.
        WINDOW_LAST(WindowEvent.WINDOW_LAST),						// The last number in the range of ids used for window events.
        WINDOW_LOST_FOCUS(WindowEvent.WINDOW_LOST_FOCUS),		// The window-lost-focus event type.
        WINDOW_OPENED(WindowEvent.WINDOW_OPENED),				// The window opened event.
        WINDOW_STATE_CHANGED(WindowEvent.WINDOW_STATE_CHANGED);		// The window-state-changed event type.

        private int	code;

        /**
         * Private constructor
         * @param code   integer or constant variable for the specific enumeration
         */
        private WindowEventType(int code) {
            this.code = code;
        }

        /**
         * @return the integer value for a specific enumeration
         */
        public int value() {
            return code;
        }

        /**
         * @param code   integer or constant variable for the specific enumeration
         * @return enumeration object
         */
        public static WindowEventType get(int code) {
            return values()[code];		// lookup.get(code);
        }
    }

    /**
     * Method description
     *
     * @param words
     *
     * @return
     */
    public static String cat(LinkedHashSet<String> words) {
        return cat(words, " ");
    }

    /**
     * Method description
     *
     * @param words
     *
     * @return
     */
    public static String cat(String[] words) {		// cat(new String[]{"a", "b"});
        return cat(words, " ");
    }

    /**
     * Method description
     *
     * @param words
     * @param delimiter
     *
     * @return
     */
    public static String cat(LinkedHashSet<String> words, String delimiter) {
        String				text     = "";
        Iterator<String>	iterator = words.iterator();

        while (iterator.hasNext())
            text = cat(text, iterator.next(), delimiter);

        return text;
    }

    /**
     * Method description
     *
     * @param word1
     * @param word2
     *
     * @return
     */
    public static String cat(String word1, String word2) {
        return cat(word1, word2, " ");
    }

    /**
     * Method description
     *
     * @param words
     * @param delimiter
     *
     * @return
     */
    public static String cat(String[] words, String delimiter) {
        return cat(new LinkedHashSet<String>(Arrays.asList(words)), delimiter);
    }

    /**
     * Method description
     *
     * @param word1
     * @param word2
     * @param delimiter
     *
     * @return
     */
    public static String cat(String word1, String word2, String delimiter) {
        String	text = (word1!=null) ? word1.trim() : "";

        if (text.isEmpty()) return word2.trim();

        if (word2!=null && !word2.trim().isEmpty()) text += delimiter + word2.trim();

        return text;
    }

    /**
     *  @param words
     *  @param delimiter
     *  @param lastDelimiter
     *  @return
     */
    public static String cat(String[] words, String delimiter, String lastDelimiter) {
        LinkedList<String>	wordsList = new LinkedList<String>(Arrays.asList(words));

//      while (wordsList.contains(""))
//          wordsList.remove("");
//      while (wordsList.contains(" "))
//          wordsList.remove(" ");
//      while (wordsList.contains("  "))
//          wordsList.remove("  ");
        String	lastWord = "";

        while (lastWord.trim() == "")
            lastWord = wordsList.removeLast();

        String	text = cat(new LinkedHashSet<String>(wordsList), delimiter) + lastDelimiter
                      + lastWord;		// new LinkedHashSet<String>(Arrays.asList(words)), delimiter);

//      text = text.substring(0, text.lastIndexOf(delimiter)-1) & lastIndexOf &;
        return text;
    }

    /**
     *  @param words
     *  @return
     */
    public static String catWords(String[] words) {
        return cat(words, ", ", " and ");
    }

    /**
     *  @return
     */
    private static boolean debugDefaultChecks() {
        return debugLevelChecks(debugDefault);
    }

    /**
     *  @param level
     *  @return
     */
    private static boolean debugLevelChecks(int level) {
        return (level < debugLevel);
    }

    /**
     *  @param headline
     *  @param text
     *  @return
     */
    private static String debugString(String headline, String text) {
        return (headline + ": " + text);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     */
    public static void debugText(String text) {
        if (!debugDefaultChecks()) return;
        debugTextOut(text, 0);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     * @param text  the body of the debug text
     * @param level
     */
    public static void debugText(String text, int level) {
        if (!debugLevelChecks(level)) return;
        debugTextOut(text, level);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     * @param headline
     * @param text
     */
    public static void debugText(String headline, String text) {
        if (!debugDefaultChecks()) return;
        debugTextOut(debugString(headline, text),0);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     * @param text  the body of the debug text
     * @param headline  the parent component or operation
     * @param level
     */
    public static void debugText(String headline, String text, int level) {
        if (!debugLevelChecks(level)) return;
        debugTextOut(debugString(headline, text), level);
    }

    /**
     *  @param text
     */
    private static void debugTextOut(String text, int level) {
        Caller	caller = getCaller(5);
        String lText = (level==0) ? "L-D" : ("L-" + level);
        String	output = (lText + "\t" + text + "\t\t[" + getCallerString(caller) + "]");

        if (debugNatively) System.out.println(output);
        else IJ.showMessage(output);
    }

    /**
     * Method description
     *
     * @param obj
     *
     * @return
     */
    public static String humanCase(Object obj) {
        try {
            return humanCase(obj.toString());
        } catch (Exception exception) {
            return humanCase(obj.getClass().getSimpleName());
        }
    }

    /**
     * Converts from camel case to human readable string.
     * {@link http://www.malethan.com/article/humanise_camel_case_in_java.html}
     * @param text
     * @return
     */
    public static String humanCase(String text) {
        return humanCase(text, false);
    }

    /**
     * Converts from camel case to human readable string.
     * {@link http://www.malethan.com/article/humanise_camel_case_in_java.html}
     * @param text
     * @param titleCase
     *
     * @return
     */
    public static String humanCase(String text, boolean titleCase) {
        Pattern			pattern = Pattern.compile("([A-Z]|[a-z])[a-z]*");
        Vector<String>	tokens  = new Vector<String>();
        Matcher			matcher = pattern.matcher(text);
        String			acronym = "";

        while (matcher.find()) {
            String	found = matcher.group();

            if (found.matches("^[A-Z]$")) acronym += found;
            else {
                if (acronym.length() > 0) tokens.add(acronym);		// we have an acronym to add before we continue
                acronym = "";
                tokens.add(found.toLowerCase());
            }
        }

        if (acronym.length() > 0) tokens.add(acronym);
        if (tokens.size() > 0) text = StringUtils.capitalize(tokens.remove(0));
        for (String s : tokens)
            text += (titleCase) ? " " + titleCase(s)
                                : " " + s;

        return text;
    }

    /**
     * Processes and generates intuitive breakdowns for key events
     *
     *     Low-level KeyEvent is generated by a component object (such as a
     *     text field) when a key is pressed, released, or typed. The event is
     *     passed to every KeyListener or KeyAdapter object which registered to
     *     receive such events using the component's addKeyListener method.
     *     (KeyAdapter objects implement the KeyListener interface.) Each such
     *     listener object gets this KeyEvent when the event occurs.
     *
     *     "Key typed" events are higher-level and generally do not depend on
     *     the platform or keyboard layout. They are generated when a Unicode
     *     character is entered, and are the preferred way to find out about
     *     character input. In the simplest case, a key typed event is produced
     *     by a single key press (e.g., 'a'). Often, however, characters are
     *     produced by series of key presses (e.g., 'shift' + 'a'), and the
     *     mapping from key pressed events to key typed events may be
     *     many-to-one or many-to-many. Key releases are not usually necessary
     *     to generate a key typed event, but there are some cases where the key
     *     typed event is not generated until a key is released (e.g., entering
     *     ASCII sequences via the Alt-Numpad method in Windows). No key typed
     *     events are generated for keys that don't generate Unicode characters
     *     (e.g., action keys, modifier keys, etc.). The getKeyChar method
     *     always returns a valid Unicode character or CHAR_UNDEFINED. For key
     *     pressed and key released events, the getKeyCode method returns the
     *     event's keyCode. For key typed events, the getKeyCode method always
     *     returns VK_UNDEFINED.
     *
     *     "Key pressed" and "key released" events are lower-level and depend on
     *     the platform and keyboard layout. They are generated whenever a key
     *     is pressed or released, and are the only way to find out about keys
     *     that don't generate character input (e.g., action keys, modifier
     *     keys, etc.). The key being pressed or released is indicated by the
     *     getKeyCode method, which returns a virtual key code.
     *
     *     Virtual key codes are used to report which keyboard key has been
     *     pressed, rather than a character generated by the combination of one
     *     or more keystrokes (such as "A", which comes from shift and "a").
     *
     *     For example, pressing the Shift key will cause a KEY_PRESSED event
     *     with a VK_SHIFT keyCode, while pressing the 'a' key will result in a
     *     VK_A keyCode. After the 'a' key is released, a KEY_RELEASED event
     *     will be fired with VK_A. Separately, a KEY_TYPED event with a keyChar
     *     value of 'A' is generated.
     *
     *     Notes:
     *
     *     Key combinations which do not result in Unicode characters, such as
     *     action keys like F1 and the HELP key, do not generate KEY_TYPED
     *     events.
     *
     *     Not all keyboards or systems are capable of generating all virtual
     *     key codes. No attempt is made in Java to generate these keys
     *     artificially.
     *
     *     Virtual key codes do not identify a physical key: they depend on the
     *     platform and keyboard layout. For example, the key that generates
     *     VK_Q when using a U.S. keyboard layout will generate VK_A when using
     *     a French keyboard layout.
     *
     *     Not all characters have a keycode associated with them. For example,
     *     there is no keycode for the question mark because there is no
     *     keyboard for which it appears on the primary layer
     *
     *     In order to support the platform-independent handling of action keys,
     *     the Java platform uses a few additional virtual key constants for
     *     functions that would otherwise have to be recognized by interpreting
     *     virtual key codes and modifiers. For example, for Japanese Windows
     *     keyboards, VK_ALL_CANDIDATES is returned instead of VK_CONVERT with
     *     the ALT modifier.
     *
     *     WARNING: Aside from those keys that are defined by the Java language
     *     (VK_ENTER, VK_BACK_SPACE, and VK_TAB), do not rely on the values of
     *     the VK_ constants. Sun reserves the right to change these values as
     *     needed to accomodate a wider range of keyboards in the future.
     *
     * @param e
     *
     * @return
     */
    public static String keyEventString(KeyEvent e) {

        char		keyChar        = e.getKeyChar();

        KeyEventID	eventID        = KeyEventID.get(e.getID());

        KeyCode		keyCode        = KeyCode.get(e.getKeyCode());

        KeyLocation	keyLocation    = KeyLocation.get(e.getKeyLocation());

        String		strID          = cat("Key", titleCase(eventID.toString()));
        String		strKeyCode     = titleCase(keyCode.toString().replace("VK_", ""));
        String		strKeyLocation = "[" + titleCase(keyLocation.toString()) + "]";
        String		strAction      = (e.isActionKey()) ? "[Action]"
                : "";
        String		strKeyChar     = "'" + keyChar + "'";
        String		strModifiers   = keyModifierString(e);

        String		strCombination = (strModifiers.isEmpty()) ? strKeyCode
                : cat(strModifiers, strKeyCode, "+");

        return cat(new String[] {
            strID, strKeyLocation, strCombination, ":", strKeyChar, strAction
        });

        /*
         * KEY_TYPED events are higher-level and are platform safe. KEY_TYPED is
         * generated when a Unicode character is entered which is accessed
         * through getKeyChar(). KEY_TYPED are not generated when non-Unicode
         * characters are generated at which case getKeyChar() returns
         * CHAR_UNDEFINED (ex. actions, modifiers… etc.). KEY_TYPED always
         * return VK_UNDEFINED for getKeyCode().
         *
         * KEY_PRESSED / KEY_RELEASED events are lower-level, and are keyboard
         * layout and platform specific. KEY_PRESSED / KEY_RELEASED event key
         * code is accessed through getKeyCode() which returns virtual key code
         * (ex. VK_SHIFT or VK_A as separate events for Shift+A). KEY_PRESSED
         * KEY_RELEASED is generated for each of the pressing / releasing of
         * every key, independently, especially non-Unicode generating keys,
         * where getKeyChar() returns CHAR_UNDEFINED (ex. actions, modifiers…
         * etc.).
         */

        // Event Specific Breakdown
//      switch (eventID) {
//      case TYPED:
//        
//      case PRESSED:
//      case RELEASED:
//      }
    }

    /**
     * Method description
     *
     * @param e
     *
     * @return
     */
    public static String keyModifierString(KeyEvent e) {

        // String strAction = e.isActionKey() ? "ALT" : ""
        String	strShift   = e.isShiftDown() ? getModifierSymbol(KeyCode.VK_SHIFT)
                : "";
        String	strAlt     = e.isAltDown() ? getModifierSymbol(KeyCode.VK_ALT)
                                           : "";
        String	strControl = e.isControlDown() ? getModifierSymbol(KeyCode.VK_CONTROL)
                : "";
        String	strMeta    = e.isMetaDown() ? getModifierSymbol(KeyCode.VK_CONTROL)
                : "";

        return cat(new String[] { strShift, strControl, strAlt, strMeta }, "+");
    }

    /**
     *  @param object
     *  @return
     */
    public static String lastSplit(Object object) {
        return lastSplit(object.toString());
    }

    /**
     * Method description
     *
     * @param text
     *
     * @return
     */
    public static String lastSplit(String text) {
        return lastSplit(text, "\\.");
    }

    /**
     * Method description
     *
     * @param text
     * @param regex
     *
     * @return
     */
    public static String lastSplit(String text, String regex) {
        String[]	splitText = text.split(regex);

        if (splitText.length == 0) return text;

        return splitText[splitText.length - 1];
    }

    /**
     * Method description
     *
     * @param value
     * @param enumClass
     * @param <E>
     *
     * @return
     */
    public static <E extends Enum<E>> IIntegerValue lookupByEnumValue(int value,
            Class<E> enumClass) {
        Map<Integer, IIntegerValue>	lookup = new HashMap<Integer, IIntegerValue>();

        for (E s : EnumSet.allOf(enumClass))
            lookup.put(((IIntegerValue)s).value(), (IIntegerValue)s);

        return lookup.get(value);
    }

    /**
     */
    public static void setupHooks() {
        if (isHooked) return;
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                super.run();
                System.out.println("Shutdown hook called... thread is running!");
            }

        });

        System.setSecurityManager(new SecurityManager() {

            @Override
            public void checkPermission(Permission perm) {

                /* Allow everything else. */
            	if (debugLevelChecks(5))
                System.out.println("Check " + perm.getName() + ": "+ perm.getActions().trim()
                                   + "\t" + perm.toString());
            }

            @Override
            public void checkExit(int status) {

                /* Don't allow exit with any status code. */
//            	throw new GrasppeKit.RuntimeExitException();
            }

        });

//      System.err.println("I'm dying!");
//
//      try {
//          System.exit(0);
//      } finally {
//          System.err.println("I'm not dead yet!");
//          System.exit(1);
//      }

        isHooked = true;
    }

//  /**
//   * Traverse the call stack to determine and return where a method was called from.
//   *
//   * @return fourth stack trace element
//   */
//  public static StackTraceElement myCaller() {
//      StackTraceElement[]   stackTraceElements = Thread.currentThread().getStackTrace();
//      StackTraceElement caller             = stackTraceElements[4];
//
//      return caller;
//  }
//
//  /**
//   * Traverse the call stack to determine and return where a method was called from.
//   *
//   *
//   * @param index
//   * @return fourth stack trace element
//   */
//  public static StackTraceElement myCaller(int index) {
//      StackTraceElement[]   stackTraceElements = Thread.currentThread().getStackTrace();
//      StackTraceElement caller             = stackTraceElements[index];
//
//      return caller;
//  }

    /**
     * Traverse and output the call stack.
     */
    public static void showCallStack() {
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();
        String				str                = "";

        for (int i = 2; i < stackTraceElements.length; i++) {
            StackTraceElement	ste        = stackTraceElements[i];
            String				classname  = lastSplit(ste.getClassName());
            String				methodName = ste.getMethodName();
            int					lineNumber = ste.getLineNumber();

            str += "\n\t\t" + classname + "." + methodName + ":" + lineNumber;
        }

        debugText("Call Stack", str, 3);
    }

    /**
     *  @param object
     *  @return
     */
    public static String simpleName(Object object) {
        return object.getClass().getSimpleName();
    }

    /**
     * Method description
     *
     * @return
     */
    public static String timeStamp() {
        return "[" + getTimeString() + "]\t";
    }

    /**
     * Method description
     *
     * @param word
     *
     * @return
     */
    public static String titleCase(String word) {
        if (word.length() == 1) return word.toUpperCase();
        if (word.length() > 1)
            return word.toUpperCase().charAt(0) + word.toLowerCase().substring(1);

        return word;
    }

    /**
     * Method description
     *
     * @return
     */
    public static Caller getCaller() {
        return getCaller(4);
    }

    /**
     * Method description
     *
     * @param traversals
     *
     * @return
     */
    public static Caller getCaller(int traversals) {
    	try {
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement	caller             = stackTraceElements[traversals];
        String				className          = caller.getClassName();
        String				methodName         = caller.getMethodName();
        int					lineNumber         = caller.getLineNumber();

        return new Caller(stackTraceElements, caller, className, methodName, lineNumber);
    	} catch (Exception exception) {
    		return null;
    	}
    }

    /**
     * Traverse the call stack to determine and return where a method was called from.
     * @return the class name, method name, and line number of the fourth stack trace element
     */
    public static String getCallerString() {
        return getCallerString(getCaller());
    }

    /**
     * Traverse the call stack to determine and return where a method was called from.
     *
     * @param caller
     * @return the class name, method name, and line number of the fourth stack trace element
     */
    public static String getCallerString(Caller caller) {
        return caller.simpleName + "." + caller.methodName + ":" + caller.lineNumber;
    }

    /**
     * Returns a lazy initialized singleton instance of this class using the private static SingletonHolder class adopting Bill Pugh's implementation of Singleton in Java.
     * {@link http://en.wikipedia.org/wiki/Singleton_pattern}
     * @return
     */
    public static GrasppeKit getInstance() {
        return SingletonHolder.grasppeKit;
    }

    /**
     * Method description
     *
     * @param keyCode
     *
     * @return
     */
    public static String getModifierSymbol(KeyCode keyCode) {
        boolean	isMac = (System.getProperty("os.name").toLowerCase().indexOf("mac") > 0);

        switch (keyCode) {

        case VK_SHIFT :
            return "\u21E7";

        case VK_ALT :
            return (isMac) ? "\u2325"
                           : "Alt";

        case VK_ALT_GRAPH :
            return (isMac) ? "\u2325"
                           : "AltGr";

        case VK_CONTROL :
            return (isMac) ? "\u2303"
                           : "Ctrl";

        case VK_META :
            return (isMac) ? "\u2318"
                           : "\u2756";
        }

        return "";
    }

    /**
     * Method description
     *
     * @return
     */
    public static String getTimeString() {
        return UniversalDateFormat.getTimeString();
    }

    /**
     * @param level
     */
    public static void setDebugTimeStamp(final int level) {
        int	delay = 10000;		// milliseconds

        if (level > debugLevel) {
            GrasppeKit.debugText("Console Timestamp Not Initialized",
                                 timeStamp() + "Interval: " + delay
                                 + " milliseconds\tDebug Level: " + level + "/" + debugLevel, 2);

            return;
        }

        GrasppeKit.debugText("Initiating Console Timestamp",
                             timeStamp() + "Interval: " + delay + " milliseconds\tDebug Level: "
                             + level + "/" + debugLevel, level);

        ActionListener	taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GrasppeKit.debugText(
                    "\n" + getTimeString()
                    + "\t\t--------------------------------------------------", level);
            }
        };

        new Timer(delay, taskPerformer).start();
    }

    /**
     * Interface description
     *
     * @version        $Revision: 1.0, 11/11/25
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public interface IIntegerValue {

        /**
         * Method description
         *
         * @return
         */
        public int value();
    }


    /**
     * @author <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     *
     */
    public interface Observable {

        /**
         * Method description
         *
         * @param observer
         */
        public void attachObserver(Observer observer);

        /**
         * Method description
         *
         * @param observer
         */
        public void detachObserver(Observer observer);

        /**
         * Method description
         *
         * @param observer
         */
        public void notifyObserver(Observer observer);

        /**
         * Method description
         *
         */
        public void notifyObservers();
    }


    /**
     * @author <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     *
     */
    public interface Observer {

        /**
         * Method called by observable object during notifyObserver calls.
         */
        public void update();
    }


    /**
     * Class description
     * 	@version        $Revision: 1.0, 11/11/28
     * 	@author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>    
     */
    public static class RuntimeExitException extends SecurityException {}


    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before. Bill Pugh's implementation of Singleton in Java.
     * {@link http://en.wikipedia.org/wiki/Singleton_pattern}
     */
    private static class SingletonHolder {

        /** Field description */
        public static final GrasppeKit	grasppeKit = new GrasppeKit();
    }


    // public static SimpleDateFormat    dateFormat = new UniversalDateFormat
    // Private constructor prevents instantiation from other classes

    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/10
     */
    public static class UniversalDateFormat extends SimpleDateFormat {

        /**
         * Constructs ...
         */
        public UniversalDateFormat() {
            super("yyyy-MM-dd HH:mm:ss");
            super.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        /**
         * Method description
         *
         * @return
         */
        public static String getTimeString() {
            return new UniversalDateFormat().format(Calendar.getInstance().getTime());		// date.getTime()) + "]";
        }
    }
}
