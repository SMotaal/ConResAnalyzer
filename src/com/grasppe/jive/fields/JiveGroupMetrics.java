package com.grasppe.jive.fields;

/**
   * Class description
   *    @version        $Revision: 1.0, 12/07/13
   *    @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  public class JiveGroupMetrics {
	  
	  
	  public static int DEFAULT_LABEL_WIDTH = 100;
	  public static int DEFAULT_FIELD_WIDTH = 125;
	  public static int DEFAULT_SUFFIX_WIDTH = 25;
	  
	  public static JiveGroupMetrics LONG_TEXT_OPTIONS = new JiveGroupMetrics(DEFAULT_LABEL_WIDTH, DEFAULT_FIELD_WIDTH + 175, DEFAULT_SUFFIX_WIDTH - 25);	  
	  public static JiveGroupMetrics SHORT_TEXT_OPTIONS = new JiveGroupMetrics(DEFAULT_LABEL_WIDTH, DEFAULT_FIELD_WIDTH - 25, DEFAULT_SUFFIX_WIDTH + 25);
	  public static JiveGroupMetrics TINY_TEXT_OPTIONS = new JiveGroupMetrics(DEFAULT_LABEL_WIDTH, DEFAULT_FIELD_WIDTH - 50, DEFAULT_SUFFIX_WIDTH + 25);
	  //public static JiveGroupMetrics SHORT_TEXT_OPTIONS = new JiveGroupMetrics(100, 25);
	  
	  /**
	 * 
	 */

	public JiveGroupMetrics() {
		  super();
		
	  }

    /**
	 * @param labelWidth
	 * @param fieldWidth
	 * @param suffixWidth
	 * @param marginWidth
	 * @param marginHeight
	 * @param paddingWidth
	 * @param paddingHeight
	 */
	public JiveGroupMetrics( int labelWidth, int fieldWidth, int suffixWidth,
			int marginWidth, int marginHeight, int paddingWidth,
			int paddingHeight) {
		super();
		
		this.labelWidth = labelWidth;
		this.fieldWidth = fieldWidth;
		this.suffixWidth = suffixWidth;
		this.marginWidth = marginWidth;
		this.marginHeight = marginHeight;
		this.paddingWidth = paddingWidth;
		this.paddingHeight = paddingHeight;
	}

	/**
	 * @param labelWidth
	 * @param fieldWidth
	 * @param suffixWidth
	 */
	public JiveGroupMetrics( int labelWidth, int fieldWidth, int suffixWidth) {
		super();
		
		this.labelWidth = labelWidth;
		this.fieldWidth = fieldWidth;
		this.suffixWidth = suffixWidth;
	}
	
	/**
	 * @param fieldWidth
	 * @param suffixWidth
	 */
	public JiveGroupMetrics( int fieldWidth, int suffixWidth) {
		super();
		
		this.fieldWidth = fieldWidth;
		this.suffixWidth = suffixWidth;
	}
	
	public int getMinimumWidth() {
		return marginWidth*2 + paddingWidth*2 + labelWidth + fieldWidth/2 + suffixWidth;
	}
	
	public int getMaximumWidth() {
		return marginWidth*2 + paddingWidth*2 + labelWidth + fieldWidth + suffixWidth;
	}	
	
    /** Field description */
    public int labelWidth = DEFAULT_LABEL_WIDTH;

    /** Field description */
    public int fieldWidth = DEFAULT_FIELD_WIDTH;

    /** Field description */
    public int suffixWidth = DEFAULT_SUFFIX_WIDTH;

    /** Field description */
    public int marginWidth   = 5,
               marginHeight  = 5,
               paddingWidth  = 5,
               paddingHeight = 5;
    

  }