/*
 * @(#)GrasppeCommon.java   11/11/08
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.Grasppe;

import ij.IJ;

//~--- JDK imports ------------------------------------------------------------

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import java.io.InvalidObjectException;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLEngineResult.Status;

import javax.swing.AbstractAction;
import javax.swing.Icon;
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
    public static int	debugLevel = 3;		// default level is 3

    /** Field description */
    public static int	timestampLevel = 4;		// default level is 3

    /** Field description */
    public static final JFrame	commonFrame = new JFrame();

    /** Field description */
    public static boolean	debugNatively = true;

    /**
     * Constructs an instance of this class but is meant to be used internally only, it is made public for convenience.
     */
    private GrasppeKit() {
        super();
        setDebugTimeStamp(timestampLevel);
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
        String	text = "";
        Iterator<String> iterator = words.iterator();

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
        String	text = word1.trim();

        if (!text.isEmpty()) text += " ";
        text += word2.trim();

        return text;
    }

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     */
    public static void debugText(String text) {
        debugText(text, 4);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     * @param level
     */
    public static void debugText(String text, int level) {
        if (level > debugLevel) return;

        Caller	caller = getCaller();
        String	output = (text + "\t\t[" + getCallerString(caller) + "]");

        if (debugNatively) System.out.println(output);
        else IJ.showMessage(output);
    }

    /**
     * Method description
     *
     * @param headline
     * @param text
     */
    public static void debugText(String headline, String text) {
        debugText(headline, text, 4);
    }

    /**
     * Output debug text with extended StackTraceElement details.
     *
     * @param text  the body of the debug text
     * @param headline  the parent component or operation
     * @param level
     */
    public static void debugText(String headline, String text, int level) {
        debugText(headline + ": " + text, level);
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
    
    public static String titleCase(String word){
    	if(word.length()==1) return word.toUpperCase();
    	if (word.length()>1) return word.toUpperCase().charAt(0) + word.toLowerCase().substring(1);
    	return word; 
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
        StackTraceElement[]	stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement	caller             = stackTraceElements[traversals];
        String				className          = caller.getClassName();
        String				methodName         = caller.getMethodName();
        int					lineNumber         = caller.getLineNumber();

        return new Caller(stackTraceElements, caller, className, methodName, lineNumber);
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
     *
     * @version        $Revision: 1.0, 11/11/08
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public class AbstractCommand extends AbstractAction implements Observer, Observable {

        protected ActionListener	actionListener;
        protected boolean			executable = false;
        protected boolean			executed   = false;
        protected boolean			executing  = false;
        protected boolean			useModel   = false;
        protected AbstractModel		model;
        protected Observers			observers = new Observers();
        protected KeyEvent			keyEvent;

//      protected GrasppeKit        grasppeKit     = GrasppeKit.getInstance();

        /** Field description */
        public int	mnemonicKey;

        /** Field description */
        public String	name = getClass().getSimpleName();

        /**
         * Constructs a new command with the specified listener.
         *
         * @param listener
         * @param text
         */
        public AbstractCommand(ActionListener listener, String text) {
            super(text);	// , icon);

//          debugText("Abstract Command",
//                    "mnemonicKey = char '" + mnemonicKey + "'; int '" + (int)mnemonicKey + "'",
//                    3);     // ; boolean '" + (boolean) mnemonicKey + "'",3);
//          if (mnemonicKey != '\u0000') setMnemonicKey(mnemonicKey);
            actionListener = listener;
        }

        /**
         * Constructs a new command with the specified listener.
         *
         * @param listener
         * @param text
         * @param executable
         */
        public AbstractCommand(ActionListener listener, String text, boolean executable) {
            this(listener, text);
            canExecute(executable);
        }

        /**
         * Called when action is triggered and passes the ActionEvent to actionListener, which is responsible for calling the execute() method.
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            executed = false;
            actionListener.actionPerformed(e);
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean altPressed() {
            if (getKeyEvent() != null) return getKeyEvent().isAltDown();

            return false;

//          boolean   altDown     = getKeyEvent().isAltDown();
//          boolean   controlDown = getKeyEvent().isControlDown();
//          boolean   shiftDown   = getKeyEvent().isShiftDown();
//          boolean   metaDown    = getKeyEvent().isMetaDown();
        }

        /**
         * Attaches an observer through the observers object which will include the observer in future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void attachObserver(Observer observer) {
            observers.attachObserver(observer);
        }

        /**
         * Returns the current executable state. Override this method to implement any additional checks needed to determine the executable state.
         *
         * @return  current executable state
         */
        public boolean canExecute() {

            // update();
            return executable;
        }

        /**
         * Sets and returns the current executable state. Observers will be notified only if the canExecute() returned a state different from the initial state. Override the canExecute() method to implement any additional checks when needed.
         *
         * @param state executable state
         *
         * @return  new executable state
         */
        public final boolean canExecute(boolean state) {
            boolean	initialState = executable;

            executable = state;

            boolean	newState = canExecute();

            if (initialState != newState) notifyObservers();

            return newState;
        }

        /**
         * Sets executed to true when called by execute() after perfomCommand() returns true.
         * @return  true only if executed changed from false to true!
         */
        public boolean completed() {
            if (!executed) {
                executed = true;
                debugText("Command Execution Succeeded", lastSplit(toString()), 3);

                return true;
            }

            debugText("Command Execution Duplicity Error", lastSplit(toString()), 2);

            return false;
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean controlPressed() {
            return getKeyEvent().isControlDown();
        }

        /**
         * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void detachObserver(Observer observer) {
            observers.detachObserver(observer);
        }

        /**
         * Called by the actionListener, following a call to actionPerformed(). Returns false if performCommand() or complete() return false.
         * @return  true if actions completed successfully!
         */
        public final boolean execute() {
            if (executing) return false;
            if (hasExecuted()) return false;
            if (!canExecute()) update();
            if (!canExecute())
                throw new IllegalStateException(getName()
                    + " could not execute in its current state.");
            debugText("Command Execution Started", lastSplit(toString()), 3);
            executing = true;

            if (!perfomCommand() ||!completed()) {
                debugText("Command Execution Failed", lastSplit(toString()), 2);

                return false;
            }

            executing = false;
            executed  = true;
            debugText("Command Execution Ends", lastSplit(toString()), 3);

            return executed;
        }

        /**
         * Method description
         *
         * @param forcedAction
         *
         * @return
         */
        public final boolean execute(boolean forcedAction) {
            debugText("Command Execution Forced", lastSplit(toString()), 3);

            // update();
            // if (forcedAction)
            executed = false;

            // if (forcedAction)
            canExecute(forcedAction);

            return execute();
        }

        /**
         * Method description
         *
         * @param e
         *
         * @return
         */
        public final boolean execute(KeyEvent e) {
            setKeyEvent(e);
            execute();
            setKeyEvent();

            return executed;
        }

        /**
         * Method description
         *
         *
         * @param forcedAction
         * @param e
         *
         * @return
         */
        public final boolean execute(boolean forcedAction, KeyEvent e) {
            setKeyEvent(e);
            execute(forcedAction);
            setKeyEvent();

            return executed;
        }

        /**
         * Detaches from the model when being finalize through garbage collection.
         *
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            try {
                model.detachObserver(this);
                debugText("Command Finalize/Detatch Succeeded",
                          model.getClass().getSimpleName() + " is no longer attached to "
                          + lastSplit(toString()), 3);
            } catch (Exception e) {

                // Command has no model and can finalize immediately
                debugText("Command Finalize/Detatch Unnecessary",
                          "No models were attached to " + lastSplit(toString()), 2);
            }

            super.finalize();
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean metaPressed() {
            return getKeyEvent().isMetaDown();
        }

        /**
         * Notifies all observer through the observers object which calls update().
         *
         */
        @Override
        public void notifyObservers() {
            observers.notifyObservers();
        }

        /**
         * Called by execute to complete execution of command actions. This method must be overloaded and return true for the action to complete.
         * @return  false unless otherwise overridden!
         */
        protected boolean perfomCommand() {
            return false;
        }

        /**
         * Method description
         *
         * @return
         */
        public boolean shiftPressed() {
            return getKeyEvent().isShiftDown();
        }

        /**
         * Called by the model indirectly during notify. It will set executable to false if using model is true and the model. This method may be overridden as long as super.update() is called first in order to preserve the model checking logic.
         */
        @Override
        public void update() {
            canExecute(!useModel || (model != null));		// either not using model or model is not empty!

            if (canExecute()) {
                debugText("Abstract Command Update", getName() + " can execute.");
            } else {
                debugText("Abstract Command Update", getName() + " cannot execute.");
            }

            notifyObservers();
        }

        /**
         * Method description
         *
         * @return
         */
        public KeyEvent getKeyEvent() {
            return keyEvent;
        }

        /**
         * @return the mnemonicKey
         */
        public int getMnemonicKey() {
            return mnemonicKey;
        }

        /**
         * Returns the current model. Override this method with a specific model type.
         *
         * @return
         */
        public AbstractModel getModel() {
            return model;
        }

        /**
         * Returns the name of the command.
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Called during execute() to determine if the event command has already executed. Override this method to change the execute-once behavior as needed.
         *
         * @return  executed since actionPerofmed()
         */
        public boolean hasExecuted() {
            return executed;
        }

        /**
         * Method description
         */
        public void setKeyEvent() {
            keyEvent = null;
        }

        /**
         * Method description
         *
         * @param e
         */
        public void setKeyEvent(KeyEvent e) {
            keyEvent = e;
        }

        /**
         * @param mnemonicKey the mnemonicKey to set
         */
        public void setMnemonicKey(int mnemonicKey) {
            this.mnemonicKey = mnemonicKey;
            debugText("Setting Action Mnemonic",
                      "The key '" + this.mnemonicKey + "' is assigned to " + getName(), 3);

            // super.putValue(Action.MNEMONIC_KEY, mnemonicKey);
        }

        /**
         * Attaches the command to the specified model and calls update() to reflect the state of the model.
         *
         * @param model
         */
        public void setModel(AbstractModel model) {
            model.attachObserver(this);
            this.model = model;
            useModel   = true;
            update();
        }
    }


    /**
     * Controllers handle the logic portion of a component. A controller directly accesses a model. The controller is responsible for all attach/detach calls. A model is responsible for safely handling attach/detach class for views.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public class AbstractController implements Observer, ActionListener {

        protected AbstractModel								model;
        protected boolean									detachable = true;
        protected LinkedHashMap<String, AbstractCommand>	commands;
        protected ActionListener							actionListener;
        protected AbstractController						commandHandler = this;

//      protected GrasppeKit                              grasppeKit     = GrasppeKit.getInstance();

        /**
         * Constructs ...
         *
         * @param model
         */
        public AbstractController(AbstractModel model) {
            super();
            this.attachModel(model);
            updateCommands();
        }

        /**
         * Called when action event is triggered. An attempt to locally handle AbstractCommand execute will be made using the getCommand(String).execute(). Finally, the action event is passed to actionListener if one is defined, to fulfill the chain of responsibility.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getCommand(e.getActionCommand()).execute();
                debugText("Command Event Handled", e.getActionCommand(), 3);
            } catch (Exception exception) {
                debugText("Command Event Ignored", e.getActionCommand(), 2);
            }

            if (actionListener != null) actionListener.actionPerformed(e);		// Pass up the chain of responsibility
        }

        /**
         * Method description
         *
         * @param controller
         *
         * @return
         */
        public LinkedHashMap<String,
                             AbstractCommand> appendCommands(AbstractController controller) {
            return appendCommands(null, controller.getCommands());
        }

        /**
         * Method description
         *
         * @param allCommands
         * @param controller
         *
         * @return
         */
        public LinkedHashMap<String, AbstractCommand> appendCommands(LinkedHashMap<String,
                             AbstractCommand> allCommands, AbstractController controller) {
            return appendCommands(allCommands, controller.getCommands());
        }

        /**
         * Appends specified commands map to the controllers commands maps. To use this, override getCommands() to pool commands from controllers down the chain of responsibility using the appendCommands(someController.getCommands())
         * @param   allCommands
         * @param   newCommands
         * @return  allCommands, starting with the controller's commands elements.
         */
        public LinkedHashMap<String, AbstractCommand> appendCommands(LinkedHashMap<String,
                             AbstractCommand> allCommands, LinkedHashMap<String,
                                 AbstractCommand> newCommands) {
            if (allCommands == null) allCommands = new LinkedHashMap<String, AbstractCommand>();
            if (allCommands.isEmpty())
                if ((commands != null) &&!commands.isEmpty()) allCommands.putAll(commands);
            if ((newCommands != null) &&!newCommands.isEmpty()) allCommands.putAll(newCommands);

            return allCommands;
        }

        /**
         * Attaches the controller to the current model object.
         */
        public void attachModel() {

            // TODO: update() on attachModel()
            // TODO: handle attach will fail if model is empty
            this.model.attachObserver(this);
        }

        /**
         * Attaches the controller to the specified model object.
         *
         * @param newModel
         */
        public void attachModel(AbstractModel newModel) {

            // TODO: update() on attachModel()
            // TODO: handle attach will fail if newModel is empty
            this.model = newModel;
            this.attachModel();
        }

        /**
         * Attaches the specified view to the current model.
         *
         * @param view
         */
        public void attachView(AbstractView view) {

            // TODO: update() on attachView()
            // TODO: handle attach will fail if view is empty
            // TODO: handle attach will fail if model is empty
            this.model.attachView(view);
        }

        /**
         * Returns the state of detachable.
         *
         * @return
         */
        public boolean canDetach() {
            return detachable;
        }

        /**
         * Needs to be overridden to populate commands with subclasses of the AbstractCommand using putCommand(new SomeCommand(this)).
         */
        public void createCommands() {

            // putCommand(new CloseCase(this));
            // TODO: Auto populate enclosed AbstractCommands class declarations using getClass().getDeclaredClasses()
        }

        /**
         * Detaches the controller from the current model object.
         */
        public void detachModel() {

            // TODO: update() on detachModel()
            this.model.detachObserver(this);
        }

        /**
         * Method description
         *
         * @param view
         */
        public void detachView(AbstractView view) {

            // TODO: update() on detachView()
            this.model.detachObserver(view);
        }

        /**
         * Outputs the controller's commands.keySet().
         */
        public void printCommands() {
            Iterator<String>	keyIterator = commands.keySet().iterator();		// Set<String> keys = commands.keySet();
            String	str = "";

            while (keyIterator.hasNext()) {
                if (str.length() > 0) str += ", ";
                str += (String)keyIterator.next();
            }

            debugText("Command Keys (" + getClass().getSimpleName() + ")", str, 2);
        }

        /**
         * Adds the specified AbstractCommand to the controller's commands map. Syntax: putCommand(new SomeCommand(this)).
         *
         * @param command
         */
        public void putCommand(AbstractCommand command) {
            commands.put(command.getName(), command);

//          debugText("Command Added", command.toString())
//          IJ.showMessage(this.getClass().getSimpleName(),
//                         this.getClass().getSimpleName() + " Command Added: " + command.getName()
//                         + " :: " + command.toString());
        }

        /**
         * Initializes controller's commands map with new LinkedHashMap of Strings and AbstractCommands. It is called during updateCommands if commands are null.
         */
        public void resetCommands() {
            commands = new LinkedHashMap<String, AbstractCommand>();
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        @Override
        public void update() {

            // TODO Auto-generated method stub
        }

        /**
         * Called on instantiation (and other situations in the future) calling both resetCommands() and createCommands() if commands is null or is empty. Updating commands should indirectly occur through observer notifications.
         */
        public void updateCommands() {

            // resetCommands(); // to test the logic
            if (commands == null) resetCommands();
            if (commands.isEmpty()) createCommands();
        }

        /**
         * Decorator-style assignment of a listener to an AbstractModel.
         *
         * @param listener
         *
         * @return
         */
        public AbstractController withActionListener(ActionListener listener) {
            actionListener = listener;

            return this;
        }

        /**
         * Returns the local command with the specified name from the commands map.
         *
         * @param   name is the case-sensitive class name of the AbstractCommand subclass nested within this controller.
         *
         * @return
         */
        public AbstractCommand getCommand(String name) {
            try {
                return commands.get(name.replaceAll(" ", ""));
            } catch (Exception exception) {
                return null;
            }
        }

        /**
         * Returns the commands map. Override this method to pool commands from controllers down the chain of responsibility using the appendCommands(someController.getCommands())
         * @return the commands
         */
        public LinkedHashMap<String, AbstractCommand> getCommands() {
            return commands;
        }

        /**
         * Return the controller's model object.
         *
         * @return
         */
        public AbstractModel getModel() {
            return this.model;
        }

        /**
         * Attaches a new model after safely detaching an existing one.
         *
         * @param newModel
         * @throws IllegalAccessException
         */
        public void setModel(AbstractModel newModel) throws IllegalAccessException {
            if (this.model != null & !this.canDetach())
                throw new IllegalAccessException("Cannot detach from current model");
            if (this.model != null) detachModel();
            attachModel(newModel);
        }
    }


    /**
     * Models handle the data portion of a component. A model indirectly notifies a controller and any number of views of any changes to the data. The controller is responsible for initiating all attach/detach calls, however the model keeps track of observer view objects with special implementation.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public class AbstractModel extends ObservableObject {

        protected AbstractController	controller;
        protected Set<AbstractView>		views = new HashSet<AbstractView>();

//      protected GrasppeKit            grasppeKit     = GrasppeKit.getInstance();

        /**
         * Constructs a new model object with no predefined controller.
         */
        public AbstractModel() {
            super();
        }

        /**
         * Constructs a new model with a predefined controller.
         *
         * @param controller
         */
        public AbstractModel(AbstractController controller) {
            this();
            attachController(controller);
        }

        /**
         * Method description
         *
         * @param controller
         */
        protected void attachController(AbstractController controller) {
            this.controller = controller;
        }

        /**
         * Method description
         *
         * @param view
         */
        protected void attachView(AbstractView view) {
            if (views.contains(view)) return;
            super.attachObserver(view);
            views.add(view);
        }

        /**
         * Detaches a properly attached view object. Detaching a view which has not been attached as a view object will cause an InvalidObjectException.
         *
         * @param view
         * @throws InvalidObjectException
         */
        protected void detachView(AbstractView view) throws InvalidObjectException {
            if (!views.contains(view))
                throw new InvalidObjectException("Cannot detach from an unattached view object.");
            super.detachObserver(view);
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/09
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public class AbstractOperation extends AbstractAction {		// implements Observer, Observable {

        protected boolean	executable = false;
        protected boolean	executed   = false;
        protected boolean	executing  = false;
        protected double	progress   = 0.0;

//      protected GrasppeKit    grasppeKit     = GrasppeKit.getInstance();

        /** Field description */
        public String	name = getClass().getSimpleName();

        /**
         * Constructs ...
         */
        public AbstractOperation() {
            super();
        }

        /**
         * @param name
         */
        public AbstractOperation(String name) {
            super(name);
        }

        /**
         * @param name
         * @param icon
         */
        public AbstractOperation(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Method description
         *
         * @param arg0
         */
        @Override
        public void actionPerformed(ActionEvent arg0) {
            execute();
        }

        /**
         * Called by an initiator to performOperation(). Returns false if performOperation() did not follow the intended scenario.
         * @return  true if execution follow intended scenario
         */
        public final boolean execute() {
            if (executing) return false;
            if (isExecuted()) return false;		// TODO: Implement duplicity resolution
            if (!isExecutable())
                throw new IllegalStateException(getName() + " is not currently executable.");
            debugText("Operation Execution Started", lastSplit(toString()), 3);
            executing = true;
            setExecuted(perfomOperation());
            executing = false;
            if (isExecuted()) debugText("Operation Execution Ends", lastSplit(toString()), 3);
            else debugText("Operation Execution Failed", lastSplit(toString()), 2);

            return isExecuted();
        }

        /**
         * Method description
         *
         * @param forcedAction
         *
         * @return
         */
        public final boolean execute(boolean forcedAction) {
            debugText("Operation Execution Forced", lastSplit(toString()), 3);

            // if (forcedAction)
            setExecuted(false);
            setExecutable(true);

            return execute();
        }

        /**
         * Called by execute to complete execution of command actions. This method must be overloaded and return true for the action to complete.
         * @return  false unless otherwise overridden!
         */
        protected boolean perfomOperation() {
            return false;
        }

        /**
         * Returns the name of the command.
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * @return the progress
         */
        protected double getProgress() {
            return progress;
        }

        /**
         * @return the executable
         */
        protected boolean isExecutable() {
            return executable;
        }

        /**
         * @return the executed
         */
        protected boolean isExecuted() {
            return executed;
        }

        /**
         * @param executable the executable to set
         */
        protected void setExecutable(boolean executable) {
            this.executable = executable;
        }

        /**
         * @param executed the executed to set
         */
        protected void setExecuted(boolean executed) {
            this.executed = executed;
        }

        /**
         * @param progress the progress to set
         */
        protected void setProgress(double progress) {
            this.progress = progress;
        }
    }


    /**
     * Views handle the user interface portion of a component. A view directly accesses a controller. A view indirectly accesses a model through the controller. The controller is responsible for all attach/detach calls.
     *
     * @version        $Revision: 0.1, 11/11/08
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public class AbstractView extends ObservableObject implements Observer {

        protected AbstractController	controller;
        protected AbstractModel			model;

//      protected GrasppeKit            grasppeKit     = GrasppeKit.getInstance();

        /**
         * Constructs ...
         *
         * @param controller
         */
        public AbstractView(AbstractController controller) {
            super();
            this.controller = controller;
        }

        /**
         * Method called by observable object during notifyObserver calls.
         */
        @Override
        public void update() {}

        /**
         * Returns model from view's controller
         * @return
         */
        protected AbstractModel getModel() {
            return controller.getModel();
        }
    }


    /**
     * Class description
     *
     * @version        $Revision: 1.0, 11/11/11
     * @author         <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     */
    public static class Caller {

        /** Field description */
        public StackTraceElement[]	stackTraceElements;

        /** Field description */
        public StackTraceElement	caller;

        /** Field description */
        public String	className;

        /** Field description */
        public String	simpleName;

        /** Field description */
        public String	methodName;

        /** Field description */
        public int	lineNumber;

        /**
         * Constructs ...
         */
        public Caller() {
            super();
        }

        /**
         * @param stackTraceElements
         * @param caller
         * @param className
         * @param methodName
         * @param lineNumber
         */
        public Caller(StackTraceElement[] stackTraceElements, StackTraceElement caller,
                      String className, String methodName, int lineNumber) {
            this();
            this.stackTraceElements = stackTraceElements;
            this.caller             = caller;
            this.className          = className;
            this.simpleName         = lastSplit(className);
            this.methodName         = methodName;
            this.lineNumber         = lineNumber;
        }

//      public static Caller  newCaller(StackTraceElement[] stackTraceElements, StackTraceElement caller,
//                    String className, String methodName, int lineNumber) {
//        return new Caller(stackTraceElements, caller, className, methodName, lineNumber);
//      }
    }


    /**
     * @author <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     *
     */
    public abstract class ObservableObject implements Observable {

        /** Field description */
        protected Observers	observers = new Observers();

        /**
         * Attaches an observer through the observers object which will include the observer in future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void attachObserver(Observer observer) {
            observers.attachObserver(observer);
        }

        /**
         * Detaches an observer through the observers object which will exclude the observer from future update() notify calls.
         *
         * @param observer
         */
        @Override
        public void detachObserver(Observer observer) {
            observers.detachObserver(observer);
        }

        /**
         * Notifies all observer through the observers object which calls update().
         *
         */
        @Override
        public void notifyObservers() {
            observers.notifyObservers();
        }
    }


    /**
     * @author <a href=”mailto:saleh.amr@mac.com”>Saleh Abdel Motaal</a>
     *
     */
    public class Observers implements Observable {

        /** Field description */
        protected Set<Observer>	observerSet = new HashSet<Observer>();

        /**
         * Method description
         *
         * @param observer
         */
        public void attachObserver(Observer observer) {
            debugText("Observer Attaching", lastSplit(observer.toString()));
            observerSet.add(observer);

            // TODO Implement throwing exceptions for attach of existing element
        }

        /**
         * Method description
         *
         * @param observer
         */
        public void detachObserver(Observer observer) {
            debugText("Observer Detaching" + lastSplit(observer.toString()));
            observerSet.remove(observer);

            // TODO Implement throwing exceptions for detach of missing element
        }

        /**
         * Method description
         *
         */
        public void notifyObservers() {
            Iterator<Observer>	observerIterator = observerSet.iterator();

            if (!observerIterator.hasNext()) debugText("Observer Update Failed", toString());

            while (observerIterator.hasNext()) {
                Observer	thisObserver = (Observer)observerIterator.next();

                debugText("Observer Update", " ==> " + lastSplit(thisObserver.toString()));
                thisObserver.update();
            }

            // TODO Implement throwing exceptions for update of missing element
        }
    }


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
