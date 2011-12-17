/*
 * @(#)ImageFileReader.java   11/11/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



package com.grasppe.conres.io;

import com.grasppe.conres.io.model.ImageFile;
import com.grasppe.lure.framework.GrasppeKit;
import com.grasppe.morie.units.spatial.SpatialFrequency;
import com.grasppe.morie.units.spatial.resolution.PixelsPerInch;

import org.apache.commons.io.FilenameUtils;
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

    /**
	 * @return the file
	 */
	public ImageFile getFile() {
		return file;
	}

	TreeMap<Integer, SanselanImageTag>	tagMap = new TreeMap<Integer, SanselanImageTag>();
    protected ImageFile					file;
    int									dbg = 0;

    /**
     * @param file
     * @throws Exception
     */
    public ImageFileReader(ImageFile file) throws Exception {
        this.file = file;
        readInformation();
    }

    /**
     *  @throws IOException
     *  @throws ImageReadException
     */
    public void readInformation() throws IOException, ImageReadException {
        String	fileExtension = FilenameUtils.getExtension(file.getName()).toLowerCase();

        String	tiffEx        = "tif";
        String	pngEx         = "png";
        String	jpgEx         = "jpg";

        boolean	isTiff        = fileExtension.matches(tiffEx);
        boolean	isPng         = fileExtension.matches(pngEx);
        boolean	isJpg         = fileExtension.matches(jpgEx);

        if (isTiff) readImageInformation();
        else if (isPng) readImageInformation();
        else if (isJpg) readImageInformation();
        else throw new IOException("Could not read " + file.getName() + ". File extension " + fileExtension
                                   + " is not a supported image commandMenu.");
    }

    /**
     *  @throws IOException
     *  @throws ImageReadException
     */
    public void readImageInformation() throws IOException, ImageReadException {
    	
    	ImageInfo	imageInfo = null;

    	try {
    		if (file!=null)
    			imageInfo = Sanselan.getImageInfo(file);
    	} catch (Exception exception) {
    		throw new IOException("Could not read " + file.getName() + ". " + exception.getMessage());
    	}

        int			colorType = imageInfo.getColorType();
        int			channels, depth;
        String colorMode = "";

        switch (colorType) {

        case ImageInfo.COLOR_TYPE_BW :
        	if(colorMode.isEmpty()) colorMode = "B/W";
        case ImageInfo.COLOR_TYPE_GRAYSCALE :
        	if(colorMode.isEmpty()) colorMode = "Gray";
            channels = 1;
            break;

        case ImageInfo.COLOR_TYPE_RGB :
        	if(colorMode.isEmpty()) colorMode = "RGB";
            channels = 3;
            break;

        case ImageInfo.COLOR_TYPE_CMYK :
        	if(colorMode.isEmpty()) colorMode = "CMYK";
            channels = 4;
            break;
        case ImageInfo.COLOR_TYPE_OTHER :
        	if(colorMode.isEmpty()) colorMode = "Other";
        case ImageInfo.COLOR_TYPE_UNKNOWN :
        	if(colorMode.isEmpty()) colorMode = "Unknown";
        default :
            channels = 0;
        }
        
        if(colorMode.isEmpty()) colorMode = "File";
        
        if (channels<1 || channels>3)
            throw new IOException("Could not read " + file.getName() + ". " + colorMode + " color mode is not supported.");

        file.setChannels(channels);
        file.setBitDepth(imageInfo.getBitsPerPixel());
        file.setWidth(imageInfo.getWidth());
        file.setHeight(imageInfo.getHeight());
        
        int widthDPI = imageInfo.getPhysicalWidthDpi();
        int heightDPI = imageInfo.getPhysicalHeightDpi();
        
        if (widthDPI<550 || heightDPI < 550) {
        	String parentName = file.getParentFile().getName();
        	try {
        		int folderDPI = new Integer(parentName.substring(parentName.lastIndexOf("-")+1)).intValue();
        		widthDPI = folderDPI;
        		heightDPI = folderDPI;
        	} catch (Exception exception) {
        		GrasppeKit.debugError("Reading folder DPI", exception, 2);
        	}
        }
        
        if (widthDPI!=heightDPI)
        	throw new IOException("Could not read " + file.getName() + ". Images with non-square pixel ratios not supported.");

        SpatialFrequency	resolution = getResolutionValue(2, widthDPI, heightDPI);

        file.setResolution(resolution);
        GrasppeKit.debugText("Read Image", "Saneslan Image Info: \t" + file, dbg);
    }

    /**
     *  @throws IOException
     *  @throws ImageReadException
     */
    public void readTiffMetadata() throws IOException, ImageReadException {

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
        GrasppeKit.debugText("Read Image", "Saneslan Metadata: \t" + file, dbg);
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
         * @param commandMenu
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
