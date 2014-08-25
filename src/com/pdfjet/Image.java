/**
 *  Image.java
 *
Copyright (c) 2014, Innovatics Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and / or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.pdfjet;

import java.io.*;


/**
 *  Used to create image objects and draw them on a page.
 *  The image type can be one of the following: ImageType.JPG, ImageType.PNG, ImageType.BMP or ImageType.PDF.
 *
 *  Please see Example_03.
 */
public class Image implements Drawable {

    protected int objNumber;

    protected float x;	    // Position of the image on the page
    protected float y;
    protected float w;      // Image width
    protected float h;      // Image height

    protected long size;    // Image file size

    protected String uri;
    protected String key;

    private float box_x;
    private float box_y;

    private boolean rotate90 = false;


    /**
     *  Use of this constructor will result in reduced memory consumption and faster processing, however it currently only supports JPG images.
     *  Please see Example_24
     */
    public Image(PDF pdf, JPGImage jpg) throws Exception {
        this.w = jpg.getWidth();
        this.h = jpg.getHeight();
        this.size = jpg.getFileSize();
        InputStream stream = jpg.getInputStream();
        if (jpg.getColorComponents() == 1) {
            addImage(pdf, stream, ImageType.JPG, w, h, size, "DeviceGray", 8);
        }
        else if (jpg.getColorComponents() == 3) {
            addImage(pdf, stream, ImageType.JPG, w, h, size, "DeviceRGB", 8);
        }
        else if (jpg.getColorComponents() == 4) {
            addImage(pdf, stream, ImageType.JPG, w, h, size, "DeviceCMYK", 8);
        }
        stream.close();
    }


    /**
     *  Use of this constructor will result in reduced memory consumption and faster processing, however it currently only supports deflated raw PDF images.
     *  Please see Example_24
     */
    public Image(PDF pdf, PDFImage raw) throws Exception {
        this.w = raw.getWidth();
        this.h = raw.getHeight();
        this.size = raw.getSize();
        InputStream stream = raw.getInputStream();
        if (raw.getColorComponents() == 1) {
            addImage(pdf, stream, ImageType.PDF, w, h, size, "DeviceGray", 8);
        }
        else if (raw.getColorComponents() == 3) {
            addImage(pdf, stream, ImageType.PDF, w, h, size, "DeviceRGB", 8);
        }
        stream.close();
    }


    /**
     *  The main constructor for the Image class.
     *
     *  @param pdf the page to draw this image on.
     *  @param inputStream the input stream to read the image from.
     *  @param imageType ImageType.JPG, ImageType.PNG and ImageType.BMP.
     *
     */
    public Image(PDF pdf, InputStream inputStream, int imageType)
            throws Exception {
        byte[] data;
        if (imageType == ImageType.JPG) {
            JPGImage jpg = new JPGImage(inputStream);
            data = jpg.getData();
            w = jpg.getWidth();
            h = jpg.getHeight();
            if (jpg.getColorComponents() == 1) {
                addImage(pdf, data, null, imageType, "DeviceGray", 8);
            }
            else if (jpg.getColorComponents() == 3) {
                addImage(pdf, data, null, imageType, "DeviceRGB", 8);
            }
            else if (jpg.getColorComponents() == 4) {
                addImage(pdf, data, null, imageType, "DeviceCMYK", 8);
            }
        }
        else if (imageType == ImageType.PNG) {
            PNGImage png = new PNGImage(inputStream);
            data = png.getData();
            w = png.getWidth();
            h = png.getHeight();
            if (png.colorType == 0) {
                addImage(pdf, data, null, imageType, "DeviceGray", png.bitDepth);
            }
            else {
                if (png.bitDepth == 16) {
                    addImage(pdf, data, null, imageType, "DeviceRGB", 16);
                }
                else {
                    addImage(pdf, data, png.getAlpha(), imageType, "DeviceRGB", 8);
                }
            }
        }
        else if (imageType == ImageType.BMP) {
            BMPImage bmp = new BMPImage(inputStream);
            data = bmp.getData();
            w = bmp.getWidth();
            h = bmp.getHeight();
            addImage(pdf, data, null, imageType, "DeviceRGB", 8);
        }

        inputStream.close();
    }


    /**
     *  Sets the position of this image on the page to (x, y).
     *
     *  @param x the x coordinate of the top left corner of the image.
     *  @param y the y coordinate of the top left corner of the image.
     */
    public void setPosition(double x, double y) {
    	setPosition((float) x, (float) y);
    }


    /**
     *  Sets the position of this image on the page to (x, y).
     *
     *  @param x the x coordinate of the top left corner of the image.
     *  @param y the y coordinate of the top left corner of the image.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location of this image on the page to (x, y).
     *
     *  @param x the x coordinate of the top left corner of the image.
     *  @param y the y coordinate of the top left corner of the image.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     *  Scales this image by the specified factor.
     *
     *  @param factor the factor used to scale the image.
     */
    public void scaleBy(double factor) {
     	this.scaleBy((float) factor, (float) factor);
    }

    
    /**
     *  Scales this image by the specified factor.
     *
     *  @param factor the factor used to scale the image.
     */
    public void scaleBy(float factor) {
     	this.scaleBy(factor, factor);
    }


    /**
     *  Scales this image by the specified width and height factor.
     *  <p><i>Author:</i> <strong>Pieter Libin</strong>, pieter@emweb.be</p>
     *
     *  @param widthFactor the factor used to scale the width of the image
     *  @param heightFactor the factor used to scale the height of the image
     */
    public void scaleBy(float widthFactor, float heightFactor) {
        this.w *= widthFactor;
        this.h *= heightFactor;
    }


    /**
     *  Places this image in the specified box.
     *
     *  @param box the specified box.
     */
    public void placeIn(Box box) throws Exception {
        box_x = box.x;
        box_y = box.y;
    }
    
    
    /**
     *  Sets the URI for the "click box" action.
     *
     *  @param uri the URI
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }


    /**
     *  Sets the destination key for the action.
     *
     *  @param key the destination name.
     */
    public void setGoToAction(String key) {
        this.key = key;
    }


    /**
     *  Sets the rotate90 flag.
     *  When the flag is true the image is rotated 90 degrees clockwise.
     *
     *  @param rotate90 the flag.
     */
    public void setRotateCW90(boolean rotate90) {
        this.rotate90 = rotate90;
    }


    /**
     *  Draws this image on the specified page.
     *
     *  @param page the page to draw this image on.
     */
    public void drawOn(Page page) throws Exception {
        x += box_x;
        y += box_y;
        page.append("q\n");

        if (rotate90) {
            page.append(h);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(w);
            page.append(' ');
            page.append(x);
            page.append(' ');
            page.append(page.height - y);
            page.append(" cm\n");

            // Rotate the image 2x45 degrees clockwise. The magic number is Math.sqrt(0.5):
            page.append("0.7071067811 -0.7071067811 0.7071067811 0.7071067811 0.0 0.0 cm\n");
            page.append("0.7071067811 -0.7071067811 0.7071067811 0.7071067811 0.0 0.0 cm\n");
        }
        else {
            page.append(w);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(h);
            page.append(' ');
            page.append(x);
            page.append(' ');
            page.append(page.height - (y + h));
            page.append(" cm\n");
        }

        page.append("/Im");
        page.append(objNumber);
        page.append(" Do\n");
        page.append("Q\n");
        
        if (uri != null || key != null) {
            page.annots.add(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    page.height - y,
                    x + w,
                    page.height - (y + h)));
        }
    }


    /**
     *  Returns the width of this image when drawn on the page.
     *  The scaling is take into account.
     *
     *  @return w - the width of this image.
     */
    public float getWidth() {
        return this.w;
    }


    /**
     *  Returns the height of this image when drawn on the page.
     *  The scaling is take into account.
     *
     *  @return h - the height of this image.
     */
    public float getHeight() {
        return this.h;
    }


    private void addSoftMask(
            PDF pdf,
            byte[] data,
            String colorSpace,
            int bitsPerComponent) throws Exception {
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /XObject\n");
        pdf.append("/Subtype /Image\n");
        pdf.append("/Filter /FlateDecode\n");
        pdf.append("/Width ");
        pdf.append(( int ) w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append(( int ) h);
        pdf.append('\n');
        pdf.append("/ColorSpace /");
        pdf.append(colorSpace);
        pdf.append('\n');
        pdf.append("/BitsPerComponent ");
        pdf.append(bitsPerComponent);
        pdf.append('\n');
        pdf.append("/Length ");
        pdf.append(data.length);
        pdf.append('\n');
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(data, 0, data.length);
        pdf.append("\nendstream\n");
        pdf.endobj();
        objNumber = pdf.objNumber;
    }


    private void addImage(
            PDF pdf,
            byte[] data,
            byte[] alpha,
            int imageType,
            String colorSpace,
            int bitsPerComponent) throws Exception {
        if (alpha != null) {
            addSoftMask(pdf, alpha, "DeviceGray", 8);
        }
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /XObject\n");
        pdf.append("/Subtype /Image\n");
        if (imageType == ImageType.JPG) {
            pdf.append("/Filter /DCTDecode\n");
        }
        else if (imageType == ImageType.PNG || imageType == ImageType.BMP) {
            pdf.append("/Filter /FlateDecode\n");
            if (alpha != null) {
                pdf.append("/SMask ");
                pdf.append(objNumber);
                pdf.append(" 0 R\n");
            }
        }
        pdf.append("/Width ");
        pdf.append(( int ) w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append(( int ) h);
        pdf.append('\n');
        pdf.append("/ColorSpace /");
        pdf.append(colorSpace);
        pdf.append('\n');
        pdf.append("/BitsPerComponent ");
        pdf.append(bitsPerComponent);
        pdf.append('\n');
        if (colorSpace.equals("DeviceCMYK")) {
            // If the image was created with Photoshop - invert the colors:
            pdf.append("/Decode [1.0 0.0 1.0 0.0 1.0 0.0 1.0 0.0]\n");
        }
        pdf.append("/Length ");
        pdf.append(data.length);
        pdf.append('\n');
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(data, 0, data.length);
        pdf.append("\nendstream\n");
        pdf.endobj();
        pdf.images.add(this);
        objNumber = pdf.objNumber;
    }


    private void addImage(
            PDF pdf,
            InputStream is,
            int imageType,
            float w,
            float h,
            long length,
            String colorSpace,
            int bitsPerComponent) throws Exception {
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /XObject\n");
        pdf.append("/Subtype /Image\n");
        if (imageType == ImageType.JPG) {
            pdf.append("/Filter /DCTDecode\n");
        }
        else if (imageType == ImageType.PDF) {
            pdf.append("/Filter /FlateDecode\n");
        }
        pdf.append("/Width ");
        pdf.append(( int ) w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append(( int ) h);
        pdf.append('\n');
        pdf.append("/ColorSpace /");
        pdf.append(colorSpace);
        pdf.append('\n');
        pdf.append("/BitsPerComponent ");
        pdf.append(bitsPerComponent);
        pdf.append('\n');
        if (colorSpace.equals("DeviceCMYK")) {
            // If the image was created with Photoshop - invert the colors:
            pdf.append("/Decode [1.0 0.0 1.0 0.0 1.0 0.0 1.0 0.0]\n");
        }
        pdf.append("/Length ");
        pdf.append((int) length);
        pdf.append('\n');
        pdf.append(">>\n");
        pdf.append("stream\n");
        byte[] buf = new byte[2048];
        int count;
        while ((count = is.read(buf, 0, buf.length)) != -1) {
            pdf.append(buf, 0, count);
        }
        pdf.append("\nendstream\n");
        pdf.endobj();
        pdf.images.add(this);
        objNumber = pdf.objNumber;
    }
    
}   // End of Image.java
