package com.grasppe.jive.fields;

/**
   * Class description
   *    @version        $Revision: 1.0, 12/07/13
   *    @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
   */
  public class GroupOptions {
	  
	  public static GroupOptions LONG_TEXT_OPTIONS = new GroupOptions(300, 25);	  
	  public static GroupOptions SHORT_TEXT_OPTIONS = new GroupOptions(100, 25);
	  //public static GroupOptions SHORT_TEXT_OPTIONS = new GroupOptions(100, 25);
	  
	  /**
	 * 
	 */

	public GroupOptions() {
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
	public GroupOptions( int labelWidth, int fieldWidth, int suffixWidth,
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
	public GroupOptions( int labelWidth, int fieldWidth, int suffixWidth) {
		super();
		
		this.labelWidth = labelWidth;
		this.fieldWidth = fieldWidth;
		this.suffixWidth = suffixWidth;
	}
	
	/**
	 * @param fieldWidth
	 * @param suffixWidth
	 */
	public GroupOptions( int fieldWidth, int suffixWidth) {
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
    public int labelWidth = 75;

    /** Field description */
    public int fieldWidth = 150;

    /** Field description */
    public int suffixWidth = 50;

    /** Field description */
    public int marginWidth   = 5,
               marginHeight  = 5,
               paddingWidth  = 5,
               paddingHeight = 5;
    

  }