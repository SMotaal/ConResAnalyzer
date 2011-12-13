/*
 * @(#)AbstractRate.java   11/10/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the properyty of it's owner.
 */



/**
 */
package com.grasppe.morie.units;

import java.io.InvalidClassException;

/**
 * @author daflair
 */
public abstract class AbstractRate extends AbstractValue {

    /** Field description */
    public static final double	factor = 1;

    /** Field description */
    protected AbstractValue	numeratorValue;

    /** Field description */
    protected AbstractValue	denominatorValue;

    /**
     * Enum description
     */
    public enum RateRelations {
        IDENTICAL, MIRRORED, DIRECT, INVERSE, NUMERATORS, DENOMINATORS, CROSSNUMERATOR,
        CROSSDENOMINATOR, NONE
    }

    /**
     */
    protected AbstractRate() {
        super();
    }

    /**
     * @param inputValue
     * @throws InvalidClassException
     */
    protected AbstractRate(AbstractRate inputValue) throws InvalidClassException {
        super();
        convertFromRate(inputValue);
    }

    /**
     * @param value
     */
    protected AbstractRate(double value) {
        super(value);		// setValue(value);
    }

    /**
     * @param rate1
     * @param rate2
     * @return
     */
    protected static boolean areCompatible(AbstractRate rate1, AbstractRate rate2) {
        RateRelations	relation = checkRelation(rate1, rate2);

        return relation != RateRelations.NONE;
    }

    /**
     * @param rate1
     * @param rate2
     * @return
     */
    protected static boolean areInversed(AbstractRate rate1, AbstractRate rate2) {
        RateRelations	relation = checkRelation(rate1, rate2);

        return (relation == RateRelations.INVERSE) || (relation == RateRelations.MIRRORED);
    }

    // protected static RelationTypes checkRelation (boolean  eqvNumerator, boolean eqvDenominator, boolean revNumerator, boolean revDenominator, boolean idtNumeratorType){

    /**
     * @param eqvNumerator
     * @param eqvDenominator
     * @param revNumerator
     * @param revDenominator
     * @return
     */
    protected static RateRelations checkContigency(boolean eqvNumerator, boolean eqvDenominator,
            boolean revNumerator, boolean revDenominator) {
        if (eqvNumerator &&!eqvDenominator) return RateRelations.NUMERATORS;
        else if (eqvDenominator &&!eqvNumerator) return RateRelations.DENOMINATORS;
        else if (revNumerator &&!revDenominator) return RateRelations.CROSSNUMERATOR;
        else if (revDenominator &&!revNumerator) return RateRelations.CROSSDENOMINATOR;
        else return RateRelations.NONE;
    }

    /**
     * @param rate1
     * @param rate2
     * @return
     */
    protected static RateRelations checkRelation(AbstractRate rate1, AbstractRate rate2) {

        /*
         *  * Rates have two components, both of which are some AbstractValue subclass.
         *  * Each component has a valueType, which all together define the relation between rates.
         *  * Identical relation is when both rates have the exact same components, in same component order.
         *  * Mirrored relation is when both rates have the exact same components, in reversed component order.
         *  * Direct relation is when rates have compatible components, up to one is exact, in same component order.
         *  * Inverse relation is when rates have compatible components, up to one is exact, in reversed component order.
         *  * Contingent is when only one component is of same or identical commandMenu.
         *  * None is when rates have no compatible components.
         */

        // RelationTypes relationType;
        AbstractValue	numeratorValue1   = rate1.getNumeratorValue();
        AbstractValue	numeratorValue2   = rate2.getNumeratorValue();
        AbstractValue	denominatorValue1 = rate1.getDenominatorValue();
        AbstractValue	denominatorValue2 = rate2.getDenominatorValue();

        // Are equivalent
        boolean	eqvNumeratorType   = isSameType(numeratorValue1, numeratorValue2);
        boolean	eqvDenominatorType = isSameType(denominatorValue1, denominatorValue2);
        boolean	eqvType            = (eqvNumeratorType && eqvDenominatorType);

        // Are reversed?
        boolean	revNumeratorType   = isSameType(numeratorValue1, denominatorValue2);
        boolean	revDenominatorType = isSameType(denominatorValue1, numeratorValue2);
        boolean	revType            = (revNumeratorType && revDenominatorType);

        // Are identical (and reversed)
        boolean	idtNumeratorType = (revType)
                                   ? numeratorValue1.getClass() == denominatorValue2.getClass()
                                   : numeratorValue1.getClass() == numeratorValue2.getClass();
        boolean	idtDenominatorType = (revType)
                                     ? denominatorValue1.getClass() == numeratorValue2.getClass()
                                     : denominatorValue1.getClass() == denominatorValue2.getClass();
        boolean	idtType = (idtNumeratorType && idtDenominatorType);

        if (idtType & !revType) return RateRelations.IDENTICAL;
        if (idtType & revType) return RateRelations.MIRRORED;
        if (eqvType & !revType) return RateRelations.DIRECT;
        if (revType) return RateRelations.INVERSE;

        // Still here, is contingent?
        boolean	cntType = (eqvNumeratorType ^ revNumeratorType)
                          ^ (eqvDenominatorType ^ revDenominatorType);

        // Debug!
        String	pfx = "\t\t";

//        System.out.println(pfx + "Comparison Details:");
//        System.out.println(pfx + "\tidtType: " + idtType + " <== " + idtNumeratorType + " / "
//                           + idtDenominatorType);
//        System.out.println(pfx + "\teqvType: " + eqvType + " <== " + eqvNumeratorType + " / "
//                           + eqvDenominatorType);
//        System.out.println(pfx + "\trevType: " + revType + " <== " + revNumeratorType + " / "
//                           + revDenominatorType);

        if (cntType) {
            RateRelations	relation = checkContigency(eqvNumeratorType, eqvDenominatorType,
                                         revNumeratorType, revDenominatorType);

//            System.out.println(pfx + "Contingency relation: " + relation);

            return relation;
        } else {
            return RateRelations.NONE;
        }
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#clone()
     */

    /**
     * @return
     */
    @Override
    public AbstractRate clone() {
        AbstractRate	newRate = (AbstractRate)super.clone();

        try {
            newRate.convertFromRate(this);
        } catch (InvalidClassException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newRate;
    }

    /**
     * @param numeratorValue
     * @param denominatorValue
     * @return
     */
    protected static String combineSingular(AbstractValue numeratorValue,
            AbstractValue denominatorValue) {
        return numeratorValue.getDefinition().singular + " per "
               + denominatorValue.getDefinition().singular;
    }

    /**
     * @param numeratorValue
     * @param denominatorValue
     * @return
     */
    protected static String combineSuffix(AbstractValue numeratorValue,
            AbstractValue denominatorValue) {
        return numeratorValue.getDefinition().suffix + " per "
               + denominatorValue.getDefinition().singular;
    }

    /**
     * @param numeratorValue
     * @param denominatorValue
     * @return
     */
    protected static String combineSymbol(AbstractValue numeratorValue,
            AbstractValue denominatorValue) {
        return numeratorValue.getDefinition().symbol + "/"
               + denominatorValue.getDefinition().symbol;
    }

    /**
     * @param inputRate
     * @throws InvalidClassException
     */
    public void convertFromRate(AbstractRate inputRate) throws InvalidClassException {
        AbstractValue[][]	components = new AbstractValue[2][2];
        RateRelations		relation   = checkRelation(this, inputRate);
        double				newValue   = 1.0;
        String				pfx        = "\t\t";

        components[0] = new AbstractValue[] { this.getNumeratorValue(),
                this.getDenominatorValue() };
        components[1] = new AbstractValue[] { inputRate.getNumeratorValue(),
                inputRate.getDenominatorValue() };

        switch (relation) {

        case IDENTICAL :	// Example: spi ==> spi
            setValue(inputRate.getValue());
            break;

        case INVERSE :		// Example: spi ==> mm/sp
        case MIRRORED :		// Example: sp/mm ==> mm/sp

            // flip inputRate components (before direct conversion)
            components[1] = new AbstractValue[] { components[1][1], components[1][0] };
            newValue      = 1.0 / inputRate.getValue();
        case DIRECT :		// Example: spi ==> sp/mm
            newValue = newValue * components[1][0].getStandardValue()
                       / components[0][0].getStandardValue() * components[0][1].getStandardValue()
                       / components[1][1].getStandardValue();
            setValue(newValue);
            break;

        default :
//            System.out.println(pfx + "\tFrom:\t" + components[1][0].toString() + " / "
//                               + components[1][1].toString());
//            System.out.println(pfx + "\tTo:\t" + components[0][0].toString() + " / "
//                               + components[0][1].toString());
            throw new InvalidClassException(
                "Cannot conver between incompatible rates with non-equivalent components." + "\n\t"
                + inputRate.toString() + " is not compatible with " + this.toString() + " (found "
                + relation.toString() + ")");
        }

//        System.out.println(pfx + "Converting between compatible rates with equivalent components."
//                           + "\n" + pfx + "\t" + inputRate.toString() + " converts to "
//                           + this.toString() + " (found " + relation.toString() + ")");
    }

    /**
     * @return the denominatorValue
     */
    protected AbstractValue getDenominatorValue() {
        return denominatorValue;
    }

//  /**
//   * @param numeratorValue the numeratorValue to set
//   */
//  protected void setNumeratorValue(AbstractValue numeratorValue) {
//      this.numeratorValue = numeratorValue;
//  }
//    /**
//     * @return the factor
//     */
//    public double getFactor() {
//        return factor;
//    }
//  /**
//   * @param denominatorValue the denominatorValue to set
//   */
//  protected void setDenominatorValue(AbstractValue denominatorValue) {
//      this.denominatorValue = denominatorValue;
//  }

    /**
     * @return the numeratorValue
     */
    protected AbstractValue getNumeratorValue() {
        return numeratorValue;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#getStandardValue()
     */

    /**
     * @return
     */
    @Override
    public double getStandardValue() {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

        // return numeratorValue.getStandardValue();
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#getSuffix()
     */

//  /**
//   * Method description
//   *
//   *
//   * @return
//   */
//  @Override
//  public String getSuffix() {
//      return getSuffix(getValue());
//  }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#getSuffix(double)
     */

    /**
     * @param value
     * @return
     */
    @Override
    public String getSuffix(double value) {
        String	classSuffix;

        try {
            if (getDefinition().suffix.isEmpty())
                classSuffix = (value == 1)
                              ? combineSingular(getNumeratorValue(), getDenominatorValue())
                              : combineSuffix(getNumeratorValue(), getDenominatorValue());
            else classSuffix = this.getDefinition().suffix;

//          System.out.println("classSuffix: " + classSuffix);
        } catch (Exception e) {
            classSuffix = super.getSuffix(value);

//          System.out.println("classSuffix: " + classSuffix);
        }

        return classSuffix;
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#getSymbol()
     */

    /**
     * @return
     */
    @Override
    public String getSymbol() {
        String	classSymbol;

        try {
            classSymbol = getDefinition().symbol;

//          System.out.println("classSymbol: " + classSymbol);
        } catch (Exception e) {
            classSymbol = combineSymbol(getNumeratorValue(), getDenominatorValue());

//          System.out.println("classSymbol: " + classSymbol);
        }

        return classSymbol;
    }

//  /**
//   * Constructs ...
//   *
//   *
//   * @return
//   */
//  public String getUnitPrefix() {
//      return getNumeratorValue().getPrefix();
//  }
//
//  /**
//   * Constructs ...
//   * @return
//   *
//   */
//  public String getUnitSuffix() {
//      return getNumeratorValue().getSuffix();
//  }
//
//  /**
//   * Constructs ...
//   *
//   *
//   * @return
//   */
//  public String getUnitSymbol() {
//      return getNumeratorValue().getSymbol();
//  }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#getValue()
     */

//  /**
//   * Method description
//   *
//   *
//   * @return
//   */
//  @Override
//  public double getValue() {
//      return getNumeratorValue().getValue();
//  }

    /**
     * @param inputRate
     * @return
     */
    public boolean isCompatible(AbstractRate inputRate) {
        return AbstractRate.areCompatible(this, inputRate);
    }

//  /**
//   * Method description
//   *
//   *
//   * @param value1
//   * @param value2
//   *
//   * @return
//   */
//  protected static boolean isSameType(AbstractValue value1, AbstractValue value2) {
//      try {
//          if (value1.getDefinition().valueType == value2.getDefinition().valueType) {
//              if (value1.getDefinition().valueType == ValueTypes.ABSTRACT)
//                  return value1.getDefinition().abstractType
//                         == value2.getDefinition().abstractType;
//              else return true;
//          } else {
////              if (value1.getClass())
//              if ((value1.getDefinition().valueType != ValueTypes.UNITS)
//                      && (value1.getDefinition().valueType != ValueTypes.UNITS))
//                  return false;
//              else return true;
//          }
//      } catch (Exception e) {}
//
//      return false;
//  }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#setStandardValue(double)
     */

    /**
     * @param inputValue
     */
    @Override
    public void setStandardValue(double inputValue) {
        throw new UnsupportedOperationException();

        // TODO Auto-generated method stub
        // numeratorValue.setStandardValue(inputValue);
    }

    /*
     *  (non-Javadoc)
     * @see com.grasppe.morie.units.AbstractValue#setValue(double)
     */

//  /**
//   * Method description
//   *
//   *
//   * @param value
//   */
//  @Override
//  public void setValue(double value) {
//      //AbstractValue   numeratorValue = getNumeratorValue();
//
//      //numeratorValue.setValue(value);
//      //super.value = numeratorValue.getValue();
//
//      // super.setValue(numeratorValue.getValue());
//  }
}
