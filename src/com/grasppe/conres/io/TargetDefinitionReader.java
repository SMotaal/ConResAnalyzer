/*
 * @(#)TargetDefinitionReader.java   11/11/24
 * Copyright (c) 2011 Saleh Abdel Motaal
 * This code is not licensed for use and is the property of it's owner.
 */



/**
 */
package com.grasppe.conres.io;

import com.grasppe.conres.framework.targets.model.TargetMeasurements;
import com.grasppe.conres.io.model.IConResTargetDefinition;
import com.grasppe.conres.io.model.TargetDefinitionFile;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Reference: Kushal Paudyal http://sanjaal.com/java/tag/java-tab-delimited-file-read-sample-example
 * @author daflair
 */
public class TargetDefinitionReader extends BufferedReader implements IGrasppeFileReader {

    protected String[]	sectionHeads = new String[] { "target", "fiducials", "measurements",
            "options" };
    protected List<String>				sectionList    = Arrays.asList(sectionHeads);
    protected HashSet<String>			sections       = new HashSet<String>(Arrays.asList(sectionHeads));
    protected String					currentSection = "";
    protected ArrayList<String>			sectionLines   = new ArrayList<String>();
    protected IConResTargetDefinition	file;
    protected Reader					reader;

    /**
     * @param file
     *  @throws IOException
     */
    public TargetDefinitionReader(File file) throws IOException {
        this(new FileReader(file));
        this.file = (IConResTargetDefinition)file;
        parseFile();
    }

    /**
     * @param in
     */
    protected TargetDefinitionReader(Reader in) {
        super(in);
        reader = in;
    }

    /**
     * @param file
     * @throws FileNotFoundException
     */
    protected TargetDefinitionReader(String file) throws FileNotFoundException {
        super(new FileReader(file));
    }

    /**
     * @param in
     * @param sz
     */
    protected TargetDefinitionReader(Reader in, int sz) {
        super(in, sz);
        reader = in;
    }

//    /**
//     * @param args
//     * @throws Exception
//     * @throws FileNotFoundException
//     */
//    public static void main(String[] args) throws FileNotFoundException, Exception {
//        String	path =
//            "/Users/daflair/Documents/data/conres/Approval_Scans_ConRes26_FS/CirRe27U.log";
//        TargetDefinitionFile	file   = new TargetDefinitionFile(path);
//        TargetDefinitionReader	reader = new TargetDefinitionReader(file);
//
//        System.out.println(file);
//    }

    /**
     * @param lines
     */
    protected void parseFiducialsSection(ArrayList<String> lines) {
        Iterator<String>	iterator    = lines.iterator();

        float[][]			fiducials   = new float[4][2];
        List<String>		fiducialIDs = Arrays.asList(new String[] { "ULC", "URC", "LRC", "LLC" });

        int					readCount   = 0;

        while (iterator.hasNext() && (readCount < 4)) {
            String	line     = iterator.next();

            String	fields[] = line.split("\t");

            try {
                int	i = fiducialIDs.indexOf(fields[1].trim().toUpperCase());

                if (i > -1) {
                    readCount++;
                    fiducials[i][0] = new Float(fields[2]).floatValue();
                    fiducials[i][1] = new Float(fields[3]).floatValue();
                }
            } catch (NumberFormatException exception) {}
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        file.setFiducials(fiducials);

//        System.out.println("Fiducials:\t" + fiducials.toString());
    }

    /**
     *  @throws IOException
     */
    public void parseFile() throws IOException {
        String	line;

        while ((line = super.readLine()) != null) {
            parseLine(line);

            String	fields[] = line.split("\t");

//          for (String field : fields)
//              System.out.print(field + "\t");
//          System.out.println();
        }
    }

    /**
     * @param line
     */
    protected void parseLine(String line) {
        String	fields[] = line.split("\t");
        String	head     = fields[0].trim().toLowerCase();

        if ((head.length() > 2)) currentSection = head;

        if (line.trim().isEmpty()) {
            if (sectionList.contains(currentSection)) {
                if (sectionCheck("target")) parseTargetSection(sectionLines);
                if (sectionCheck("fiducials")) parseFiducialsSection(sectionLines);
                if (sectionCheck("measurements")) parseMeasurementsSection(sectionLines);
                if (sectionCheck("options")) parseOptionsSection(sectionLines);
            }

            sectionLines.clear();
            currentSection = "";
        } else
            sectionLines.add(line);
    }

    /**
     * @param lines
     */
    protected void parseMeasurementsSection(ArrayList<String> lines) {
        Iterator<String>	iterator     = lines.iterator();

        TargetMeasurements	measurements = new TargetMeasurements();

        ArrayList<Float>	x0           = new ArrayList<Float>();
        ArrayList<Float>	y0           = new ArrayList<Float>();
        ArrayList<Float>	dX           = new ArrayList<Float>();
        ArrayList<Float>	dY           = new ArrayList<Float>();
        ArrayList<Float>	vX           = new ArrayList<Float>();
        ArrayList<Float>	vY           = new ArrayList<Float>();
        int					nX           = 0,
							nY           = 0;

        String				lastLine     = "";

        while (iterator.hasNext()) {
            String	line     = iterator.next();
            String	fields[] = line.split("\t");

            try {
                int	head = new Integer(fields[0]).intValue();

                if (head > 0) {
                    if (nX == 0) nX = head;
                    else if ((nX > 0) && (nY == 0)) nY = head;
                }
            } catch (NumberFormatException exception) {}
            catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                if (!fields[2].trim().isEmpty()) x0.add(new Float(fields[2]));
                if (!fields[3].trim().isEmpty()) y0.add(new Float(fields[3]));
                if (!fields[4].trim().isEmpty()) dX.add(new Float(fields[4]));
                if (!fields[5].trim().isEmpty()) dY.add(new Float(fields[5]));
                if (!fields[6].trim().isEmpty()) vX.add(new Float(fields[6]));
                if (!fields[7].trim().isEmpty()) vY.add(new Float(fields[7]));
            } catch (NumberFormatException exception) {}
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        float[]	mX = new float[nX],
				mY = new float[nY];
        float[]	aX = new float[nX],
				aY = new float[nY];

        for (int i = 0; i < nX; i++) {
            mX[i] = x0.get(i).floatValue();
            aX[i] = vX.get(i).floatValue();
        }

        for (int i = 0; i < nY; i++) {
            mY[i] = y0.get(i).floatValue();
            aY[i] = vY.get(i).floatValue();
        }

        // TODO: Change to handle multiple xSpan / ySpan if needed later
        float	dXF = dX.get(0).floatValue();
        float	dYF = dY.get(0).floatValue();
        
        float[][] bXY = file.getFiducials();
        float[] bX = new float[4];
        float[] bY = new float[4];
        
        for (int i = 0; i<4; i++) {
        	bX[i] = bXY[i][0];
			bY[i] = bXY[i][1];
        }

        measurements.setDimensions(mX, mY, dXF, dYF, new float[] {bX[0],bX[2]}, new float[] {bY[0],bY[2]});
        measurements.setValues(aX, aY);

        file.setMeasurements(measurements);

//        System.out.println("Measurements:\t" + measurements);

    }

    /**
     * @param lines
     */
    protected void parseOptionsSection(ArrayList<String> lines) {
        int[]	nLevels = new int[] {};
        int		nStart  = 3;

        try {
            if (!lines.get(0).startsWith("Options")) return;

            String	fields[] = lines.get(1).split("\t");
            int		nFields  = fields.length;

            nLevels = new int[nFields - nStart];

            for (int i = nStart; i < nFields; i++)
                nLevels[i - nStart] = new Float(fields[i]).intValue();
            file.setBlockToneValues(nLevels);
        } catch (NumberFormatException exception) {}
        catch (Exception exception) {
            exception.printStackTrace();
        }

//        System.out.println("Nlevels:\t" + nLevels.toString());
    }

    /**
     * @param lines
     */
    protected void parseSection(ArrayList<String> lines) {
        Iterator<String>	iterator = lines.iterator();

        while (iterator.hasNext()) {
            String	line     = iterator.next();
            String	fields[] = line.split("\t");

        }
    }

    /**
     * @param lines
     */
    protected void parseTargetSection(ArrayList<String> lines) {
        String	name = lines.get(1).trim();

        file.setName(name);
//        System.out.println("Target Name:\t" + lines.get(1).trim());
    }

    /**
     *  @param head
     *  @return
     */
    protected boolean sectionCheck(String head) {
        String	sectionHead = sectionLines.get(0).toLowerCase();

        if (sectionHead.trim().isEmpty() ||!sectionHead.startsWith(head.toLowerCase()))
            return false;

        return currentSection.equals(head.toLowerCase());
    }
}
