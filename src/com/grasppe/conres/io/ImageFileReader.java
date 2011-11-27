/*
 * @(#)ImageFileReader.java   11/11/25
 *
 * Copyright (c) 2011 Saleh Abdel Motaal
 *
 * This code is not licensed for use and is the property of it's owner.
 *
 */



package com.grasppe.conres.io;

import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.resolution.PixelsPerInch;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Class description
 *  @version        $Revision: 1.0, 11/11/25
 *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
 */
public class ImageFileReader implements IGrasppeFileReader {

    TreeMap<Integer, SanselanImageTag>	tagMap = new TreeMap<Integer, SanselanImageTag>();
    protected ImageFile					file;

    /**
     * @param file
     * @throws Exception
     */
    public ImageFileReader(File file) throws Exception {
        this.file = (ImageFile)file;
        readInformation();
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String	path =
            "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS/CirRe27U_10i.tif";

//      "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS/CirRe27U_10t.png";

        ImageFile		file   = new ImageFile(path);
        ImageFileReader	reader = new ImageFileReader(file);

        System.out.println(file);
    }

    /**
     *  @throws IOException
     *  @throws ImageReadException
     */
    public void readInformation() throws IOException, ImageReadException {

        // Ref: http://commons.apache.org/sanselan/xref-test/org/apache/sanselan/sampleUsage/MetadataExample.html

        // TODO: Finalize tiff tags and add support for PNG

        ImageInfo	imageInfo   = Sanselan.getImageInfo(file);

        ArrayList	tagList     = imageInfo.getComments();
        Iterator	tagIterator = tagList.iterator();

        while (tagIterator.hasNext()) {
            String	tagText = (String)tagIterator.next();
            int		tagID   = new Integer(tagText.substring(0, tagText.indexOf(" "))).intValue();
            String	tagName = tagText.substring(tagText.indexOf(": ") + 2, tagText.indexOf("): "));		// tagText[2].split(")")[0].trim();
            String		tagContents = tagText.substring(tagText.indexOf("): ") + 3);
            String		tagValue    = tagContents.substring(0, tagContents.lastIndexOf("(") - 1);
            String[]	tagFormat   = tagContents.substring(tagContents.lastIndexOf("(") + 1,
                                     tagContents.lastIndexOf(")")).split(" ");
            int		tagLength = new Integer(tagFormat[0]).intValue();
            String	tagType   = tagFormat[1];

            tagMap.put(tagID, new SanselanImageTag(tagID, tagName, tagValue, tagType, tagLength));

        }

        file.setChannels(tagMap.get(277).getIntegerValue().intValue());
        file.setBitDepth(tagMap.get(258).getIntegerValue().intValue());
        file.setWidth(tagMap.get(256).getIntegerValue().intValue());
        file.setHeight(tagMap.get(257).getIntegerValue().intValue());

        int					unit       = tagMap.get(296).getIntegerValue().intValue();
        double				xValue     = tagMap.get(282).getDoubleValue().doubleValue();
        double				yValue     = tagMap.get(282).getDoubleValue().doubleValue();
        SpatialFrequency	resolution = getResolutionValue(unit, xValue, yValue);

        file.setResolution(resolution);
        GrasppeKit.debugText("Read Image", "Saneslan Metadata: \t" + file);
    }

    /**
     *  @param unit
     *  @param xValue
     *  @param yValue
     *  @return
     */
    public SpatialFrequency getResolutionValue(int unit, double xValue, double yValue) {

        /*
         * [296]    ResolutionUnit              1 =  No absolute  unit of  measurement.  2 = Inch.   3 = Centimeter.
         * [282, 3] XResolution, YResolution    The number of pixels per ResolutionUnit
         */

        if (xValue != yValue) return null;		// Images must have fixed-ratio resolution values

        double	resolution = xValue;

        if (unit == 2) return new PixelsPerInch(resolution);

        return null;
    }

    /**
     * Class description
     *  @version        $Revision: 1.0, 11/11/25
     *  @author         <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class SanselanImageTag {

        /** Field description */
        public int	index;

        /** Field description */
        public String	name;

        /** Field description */
        public Object	value;

        /** Field description */
        public String	type;

        /** Field description */
        public int	length;

        /**
         *  @param index
         * @param name
         * @param value
         * @param type
         * @param length
         */
        public SanselanImageTag(int index, String name, Object value, String type, int length) {
            super();
            this.index  = index;
            this.name   = name;
            this.value  = value;
            this.type   = type;
            this.length = length;
        }

        /**
         *  @return
         */
        @Override
        public String toString() {

            return (index + " " + name + " - " + type + "[" + length + "]:\t" + value);
        }

        /**
         *  @return
         */
        public Double getDoubleValue() {
            try {
                return new Double(getStringValue().replace(",", "").trim());
            } catch (Exception exception) {
                return null;
            }
        }

        /**
         *  @return
         */
        public Integer getIntegerValue() {
            try {
                return new Integer(getStringValue());
            } catch (Exception exception) {
                return null;
            }
        }

        /**
         *  @return
         */
        public String getStringValue() {
            try {
                return (String)value;
            } catch (Exception exception) {
                return null;
            }
        }
    }
}
