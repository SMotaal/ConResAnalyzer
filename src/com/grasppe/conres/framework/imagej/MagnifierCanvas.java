package com.grasppe.conres.framework.imagej;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.Roi;
import ij.util.Java2;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
     * Class description
     *
     * @version $Revision: 1.0, 11/11/11
     * @author <a href=Ómailto:saleh.amr@mac.comÓ>Saleh Abdel Motaal</a>
     */
    public class MagnifierCanvas extends ImageCanvas {

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
        @Override
		public void paint(Graphics g) {
        	Roi roi = imp.getRoi();
            try {
                Java2.setBilinearInterpolation(g, true);
                Java2.setAntialiased(g, true);

                Image	img = imp.getImage();

                if (img != null) g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
//                super.paint(g);
                g.drawImage(img, 0, 0, (int)(srcRect.width * magnification),
                            (int)(srcRect.height * magnification), srcRect.x, srcRect.y,
                            srcRect.x + srcRect.width, srcRect.y + srcRect.height, null);
//                //if (overlay!=null) drawOverlay(g);
//                //if (showAllROIs) drawAllROIs(g);
//                //if (roi!=null) drawRoi(roi, g);                

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

            repaint();

        }
    }