/*
 * @(#)GrasppeIJ.java   11/11/25
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.lure.framework;

import com.grasppe.lure.framework.GrasppeKit.IIntegerValue;

import ij.io.FileInfo;

/**
 * @author daflair
 */
@Deprecated
public class GrasppeIJ {

    /**
     */
    public enum CompressionModes implements IIntegerValue {
        UNKNOWN(FileInfo.COMPRESSION_UNKNOWN, ""), NONE(FileInfo.COMPRESSION_NONE, ""),
        LZW(FileInfo.LZW, ""), LZW_WITH_DIFFERENCING(FileInfo.LZW_WITH_DIFFERENCING, ""),
        JPEG(FileInfo.JPEG, ""), PACK_BITS(FileInfo.PACK_BITS, ""), ZIP(FileInfo.ZIP, "");

        private int		value;
        private String	description;

        /**
         *  @param value
         *  @param description
         */
        private CompressionModes(int value, String description) {
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
            return GrasppeKit.lookupByEnumValue(value, CompressionModes.class);
        }
    }

    /**
     */
    public enum FileFormats implements IIntegerValue {
        UNKNOWN(FileInfo.UNKNOWN, ""), RAW(FileInfo.RAW, ""), TIFF(FileInfo.TIFF, ""),
        GIF_OR_JPG(FileInfo.GIF_OR_JPG, ""), FITS(FileInfo.FITS, ""), BMP(FileInfo.BMP, ""),
        DICOM(FileInfo.DICOM, ""), ZIP_ARCHIVE(FileInfo.ZIP_ARCHIVE, ""), PGM(FileInfo.PGM, ""),
        IMAGEIO(FileInfo.IMAGEIO, "");

        private int		value;
        private String	description;

        /**
         *  @param value
         *  @param description
         */
        private FileFormats(int value, String description) {
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
            return GrasppeKit.lookupByEnumValue(value, FileFormats.class);
        }
    }

//  private int value; private String description;
//  private %(int value, String description) { this.value = value; this.description = description; }
//  public int value() { return value; }
//  public String description() { return description; }
//  public static IIntegerValue get(int value) { return GrasppeKit.lookupByEnumValue(value, %.class); }   

    /**
     */
    public enum ImageTypes implements IIntegerValue {
        GRAY8(FileInfo.GRAY8, "8-bit unsigned integer (0-255). "),
        GRAY16_SIGNED(
            FileInfo.GRAY16_SIGNED,
            "16-bit signed integer (-32768-32767). Imported signed images are converted to unsigned by adding 32768. "), GRAY16_UNSIGNED(
                FileInfo.GRAY16_UNSIGNED, "16-bit unsigned integer (0-65535). "), GRAY32_INT(
                FileInfo.GRAY32_INT,
                "32-bit signed integer. Imported 32-bit integer images are converted to floating-point. "), GRAY32_FLOAT(
                    FileInfo.GRAY32_FLOAT, "32-bit floating-point. "), COLOR8(
                    FileInfo.COLOR8, "8-bit unsigned integer with color lookup table. "), RGB(
                    FileInfo.RGB, "24-bit interleaved RGB. Import/export only. "), RGB_PLANAR(
                    FileInfo.RGB_PLANAR, "24-bit planer RGB. Import only. "), BITMAP(
                    FileInfo.BITMAP, "1-bit black and white. Import only. "), ARGB(
                    FileInfo.ARGB, "32-bit interleaved ARGB. Import only. "), BGR(
                    FileInfo.BGR, "24-bit interleaved BGR. Import only. "), GRAY32_UNSIGNED(
                    FileInfo.GRAY32_UNSIGNED,
                    "32-bit unsigned integer. Imported 32-bit integer images are converted to floating-point. "), RGB48(
                        FileInfo.RGB48, "48-bit interleaved RGB. "), GRAY12_UNSIGNED(
                        FileInfo.GRAY12_UNSIGNED,
                        "12-bit unsigned integer (0-4095). Import only. "), GRAY24_UNSIGNED(
                            FileInfo.GRAY24_UNSIGNED,
                            "24-bit unsigned integer. Import only. "), BARG(
                                FileInfo.BARG,
                                "32-bit interleaved BARG (MCID). Import only. "), GRAY64_FLOAT(
                                    FileInfo.GRAY64_FLOAT,
                                    "64-bit floating-point. Import only."), RGB48_PLANAR(
                                        FileInfo.RGB48_PLANAR,
                                        "48-bit planar RGB. Import only. "), ABGR(
                                            FileInfo.ABGR,
                                                "32-bit interleaved ABGR. Import only. ");

        private int		value;
        private String	description;

        /**
         *  @param value
         *  @param description
         */
        private ImageTypes(int value, String description) {
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
            return GrasppeKit.lookupByEnumValue(value, ImageTypes.class);
        }
    }
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        // TODO Auto-generated method stub
//        System.out.println(CompressionModes.get(1) + " isa "
//                           + CompressionModes.get(0).getClass().getName());
//    }
}
