/**
 *  Image.java
 *
Copyright 2020 Innovatics Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.pdfjet;

import java.io.*;
import java.util.*;


/**
 *  Used to create image objects and draw them on a page.
 *  The image type can be one of the following:
 *      ImageType.JPG, ImageType.PNG, ImageType.BMP or ImageType.PNG_STREAM
 *
 *  Please see Example_03 and Example_24.
 */
public class Image implements Drawable {

    protected int objNumber;

    protected float x = 0f; // Position of the image on the page
    protected float y = 0f;
    protected float w;      // Image width
    protected float h;      // Image height

    protected String uri;
    protected String key;

    private float xBox;
    private float yBox;

    private int degrees = 0;
    private boolean flipUpsideDown = false;

    private String language = null;
    private String actualText = Single.space;
    private String altDescription = Single.space;


    /**
     *  The main constructor for the Image class.
     *
     *  @param pdf the PDF to which we add this image.
     *  @param inputStream the input stream to read the image from.
     *  @param imageType ImageType.JPG, ImageType.PNG and ImageType.BMP.
     *  @throws Exception  If an input or output exception occurred
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
            if (png.getColorType() == 0) {
                addImage(pdf, data, null, imageType, "DeviceGray", png.getBitDepth());
            }
            else {
                if (png.getBitDepth() == 16) {
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
        else if (imageType == ImageType.PNG_STREAM) {
            addImage(pdf, inputStream);
        }

        inputStream.close();
    }


    /**
     *  Constructor used to attach images to existing PDF.
     *
     *  @param objects the map to which we add this image.
     *  @param inputStream the input stream to read the image from.
     *  @param imageType ImageType.JPG, ImageType.PNG and ImageType.BMP.
     *  @throws Exception  If an input or output exception occurred
     *
     */
    public Image(List<PDFobj> objects, InputStream inputStream, int imageType) throws Exception {
        byte[] data;
        if (imageType == ImageType.JPG) {
            JPGImage jpg = new JPGImage(inputStream);
            data = jpg.getData();
            w = jpg.getWidth();
            h = jpg.getHeight();
            if (jpg.getColorComponents() == 1) {
                addImageToObjects(objects, data, null, imageType, "DeviceGray", 8);
            }
            else if (jpg.getColorComponents() == 3) {
                addImageToObjects(objects, data, null, imageType, "DeviceRGB", 8);
            }
            else if (jpg.getColorComponents() == 4) {
                addImageToObjects(objects, data, null, imageType, "DeviceCMYK", 8);
            }
        }
        else if (imageType == ImageType.PNG) {
            PNGImage png = new PNGImage(inputStream);
            data = png.getData();
            w = png.getWidth();
            h = png.getHeight();
            if (png.getColorType() == 0) {
                addImageToObjects(objects, data, null, imageType, "DeviceGray", png.getBitDepth());
            }
            else {
                if (png.getBitDepth() == 16) {
                    addImageToObjects(objects, data, null, imageType, "DeviceRGB", 16);
                }
                else {
                    addImageToObjects(objects, data, png.getAlpha(), imageType, "DeviceRGB", 8);
                }
            }
        }
        else if (imageType == ImageType.BMP) {
            BMPImage bmp = new BMPImage(inputStream);
            data = bmp.getData();
            w = bmp.getWidth();
            h = bmp.getHeight();
            addImageToObjects(objects, data, null, imageType, "DeviceRGB", 8);
        }
        inputStream.close();
    }


    // Creates new image from PDFobj
    public Image(PDF pdf, PDFobj obj) throws Exception {
        w = Float.parseFloat(obj.getValue("/Width"));
        h = Float.parseFloat(obj.getValue("/Height"));
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /XObject\n");
        pdf.append("/Subtype /Image\n");
        pdf.append("/Filter ");
        pdf.append(obj.getValue("/Filter"));
        pdf.append("\n");
        pdf.append("/Width ");
        pdf.append(w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append(h);
        pdf.append('\n');
        String colorSpace = obj.getValue("/ColorSpace");
        if (!colorSpace.equals("")) {
            pdf.append("/ColorSpace ");
            pdf.append(colorSpace);
            pdf.append("\n");
        }
        pdf.append("/BitsPerComponent ");
        pdf.append(obj.getValue("/BitsPerComponent"));
        pdf.append("\n");
        String decodeParms = obj.getValue("/DecodeParms");
        if (!decodeParms.equals("")) {
            pdf.append("/DecodeParms ");
            pdf.append(decodeParms);
            pdf.append("\n");
        }
        String imageMask = obj.getValue("/ImageMask");
        if (!imageMask.equals("")) {
            pdf.append("/ImageMask ");
            pdf.append(imageMask);
            pdf.append("\n");
        }
        pdf.append("/Length ");
        pdf.append(obj.stream.length);
        pdf.append('\n');
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(obj.stream, 0, obj.stream.length);
        pdf.append("\nendstream\n");
        pdf.endobj();
        pdf.images.add(this);
        objNumber = pdf.getObjNumber();
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
     *  Sets the position of this image on the page to (x, y).
     *
     *  @param x the x coordinate of the top left corner of the image.
     *  @param y the y coordinate of the top left corner of the image.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location of this image on the page to (x, y).
     *
     *  @param x the x coordinate of the top left corner of the image.
     *  @param y the y coordinate of the top left corner of the image.
     *  @return this Image object.
     */
    public Image setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    public Image setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Scales this image by the specified factor.
     *
     *  @param factor the factor used to scale the image.
     *  @return this Image object.
     */
    public Image scaleBy(double factor) {
        return this.scaleBy((float) factor, (float) factor);
    }


    /**
     *  Scales this image by the specified factor.
     *
     *  @param factor the factor used to scale the image.
     *  @return this Image object.
     */
    public Image scaleBy(float factor) {
        return this.scaleBy(factor, factor);
    }


    /**
     *  Scales this image by the specified width and height factor.
     *  <p><i>Author:</i> <strong>Pieter Libin</strong>, pieter@emweb.be</p>
     *
     *  @param widthFactor the factor used to scale the width of the image
     *  @param heightFactor the factor used to scale the height of the image
     *  @return this Image object.
     */
    public Image scaleBy(float widthFactor, float heightFactor) {
        this.w *= widthFactor;
        this.h *= heightFactor;
        return this;
    }


    public Image resizeWidth(float width) {
        float factor = width / getWidth();
        return this.scaleBy(factor, factor);
    }


    public Image resizeHeight(float height) {
        float factor = height / getHeight();
        return this.scaleBy(factor, factor);
    }


    /**
     *  Places this image in the specified box.
     *
     *  @param box the specified box.
     */
    public void placeIn(Box box) {
        xBox = box.x;
        yBox = box.y;
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
        if (rotate90) {
            this.degrees = 90;
        }
        else {
            this.degrees = 0;
        }
    }


    /**
     *  Sets the image rotation to the specified number of degrees.
     *
     *  @param degrees the number of degrees.
     */
    public void setRotate(int degrees) {
        this.degrees = degrees;
    }


    /**
     *  Sets the alternate description of this image.
     *
     *  @param altDescription the alternate description of the image.
     *  @return this Image.
     */
    public Image setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    /**
     *  Sets the actual text for this image.
     *
     *  @param actualText the actual text for the image.
     *  @return this Image.
     */
    public Image setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    /**
     *  Draws this image on the specified page.
     *
     *  @param page the page to draw this image on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        page.addBMC(StructElem.SPAN, language, actualText, altDescription);

        x += xBox;
        y += yBox;
        page.append("q\n");

        if (degrees == 0) {
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
        else if (degrees == 90) {
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
            page.append("0 -1 1 0 0 0 cm\n");
        }
        else if (degrees == 180) {
            page.append(w);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(h);
            page.append(' ');
            page.append(x + w);
            page.append(' ');
            page.append(page.height - y);
            page.append(" cm\n");
            page.append("-1 0 0 -1 0 0 cm\n");
        }
        else if (degrees == 270) {
            page.append(h);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(0f);
            page.append(' ');
            page.append(w);
            page.append(' ');
            page.append(x + h);
            page.append(' ');
            page.append(page.height - (y + w));
            page.append(" cm\n");
            page.append("0 1 -1 0 0 0 cm\n");
        }

        if (flipUpsideDown) {
            page.append("1 0 0 -1 0 0 cm\n");
        }

        page.append("/Im");
        page.append(objNumber);
        page.append(" Do\n");
        page.append("Q\n");

        page.addEMC();

        if (uri != null || key != null) {
            page.addAnnotation(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    y,
                    x + w,
                    y + h,
                    language,
                    actualText,
                    altDescription));
        }

        return new float[] {x + w, y + h};
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
        pdf.append((int) w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append((int) h);
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
        objNumber = pdf.getObjNumber();
    }


    private void addImage(
            PDF pdf,
            byte[] data,
            byte[] alpha,
            int imageType,
            String colorSpace,
            int bitsPerComponent) throws Exception {
        if (alpha != null) {
            addSoftMask(pdf, alpha, "DeviceGray", bitsPerComponent);
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
        pdf.append((int) w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append((int) h);
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
        objNumber = pdf.getObjNumber();
    }


    private void addImage(PDF pdf, InputStream inputStream) throws Exception {

        w = getInt(inputStream);            // Width
        h = getInt(inputStream);            // Height
        byte c = (byte) inputStream.read(); // Color Space
        byte a = (byte) inputStream.read(); // Alpha

        if (a != 0) {
            pdf.newobj();
            pdf.append("<<\n");
            pdf.append("/Type /XObject\n");
            pdf.append("/Subtype /Image\n");
            pdf.append("/Filter /FlateDecode\n");
            pdf.append("/Width ");
            pdf.append(w);
            pdf.append('\n');
            pdf.append("/Height ");
            pdf.append(h);
            pdf.append('\n');
            pdf.append("/ColorSpace /DeviceGray\n");
            pdf.append("/BitsPerComponent 8\n");
            int length = getInt(inputStream);
            pdf.append("/Length ");
            pdf.append(length);
            pdf.append('\n');
            pdf.append(">>\n");
            pdf.append("stream\n");
            byte[] buf1 = new byte[length];
            inputStream.read(buf1, 0, length);
            pdf.append(buf1, 0, length);
            pdf.append("\nendstream\n");
            pdf.endobj();
            objNumber = pdf.getObjNumber();
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /XObject\n");
        pdf.append("/Subtype /Image\n");
        pdf.append("/Filter /FlateDecode\n");
        if (a != 0) {
            pdf.append("/SMask ");
            pdf.append(objNumber);
            pdf.append(" 0 R\n");
        }
        pdf.append("/Width ");
        pdf.append(w);
        pdf.append('\n');
        pdf.append("/Height ");
        pdf.append(h);
        pdf.append('\n');
        pdf.append("/ColorSpace /");
        if (c == 1) {
            pdf.append("DeviceGray");
        }
        else if (c == 3 || c == 6) {
            pdf.append("DeviceRGB");
        }
        pdf.append('\n');
        pdf.append("/BitsPerComponent 8\n");
        pdf.append("/Length ");
        pdf.append(getInt(inputStream));
        pdf.append('\n');
        pdf.append(">>\n");
        pdf.append("stream\n");
        byte[] buf2 = new byte[4096];
        int count;
        while ((count = inputStream.read(buf2, 0, buf2.length)) > 0) {
            pdf.append(buf2, 0, count);
        }
        pdf.append("\nendstream\n");
        pdf.endobj();
        pdf.images.add(this);
        objNumber = pdf.getObjNumber();
    }


    private int getInt(InputStream inputStream) throws Exception {
        byte[] buf = new byte[4];
        inputStream.read(buf, 0, 4);
        int val = 0;
        val |= buf[0] & 0xff;
        val <<= 8;
        val |= buf[1] & 0xff;
        val <<= 8;
        val |= buf[2] & 0xff;
        val <<= 8;
        val |= buf[3] & 0xff;
        return val;
    }


    private void addSoftMask(
            List<PDFobj> objects,
            byte[] data,
            String colorSpace,
            int bitsPerComponent) {
        PDFobj obj = new PDFobj();
        obj.dict.add("<<");
        obj.dict.add("/Type");
        obj.dict.add("/XObject");
        obj.dict.add("/Subtype");
        obj.dict.add("/Image");
        obj.dict.add("/Filter");
        obj.dict.add("/FlateDecode");
        obj.dict.add("/Width");
        obj.dict.add(String.valueOf((int) w));
        obj.dict.add("/Height");
        obj.dict.add(String.valueOf((int) h));
        obj.dict.add("/ColorSpace");
        obj.dict.add("/" + colorSpace);
        obj.dict.add("/BitsPerComponent");
        obj.dict.add(String.valueOf(bitsPerComponent));
        obj.dict.add("/Length");
        obj.dict.add(String.valueOf(data.length));
        obj.dict.add(">>");
        obj.setStream(data);
        obj.number = objects.size() + 1;
        objects.add(obj);
        objNumber = obj.number;
    }


    private void addImageToObjects(
            List<PDFobj> objects,
            byte[] data,
            byte[] alpha,
            int imageType,
            String colorSpace,
            int bitsPerComponent) {
        if (alpha != null) {
            addSoftMask(objects, alpha, "DeviceGray", bitsPerComponent);
        }
        PDFobj obj = new PDFobj();
        obj.dict.add("<<");
        obj.dict.add("/Type");
        obj.dict.add("/XObject");
        obj.dict.add("/Subtype");
        obj.dict.add("/Image");
        if (imageType == ImageType.JPG) {
            obj.dict.add("/Filter");
            obj.dict.add("/DCTDecode");
        }
        else if (imageType == ImageType.PNG || imageType == ImageType.BMP) {
            obj.dict.add("/Filter");
            obj.dict.add("/FlateDecode");
            if (alpha != null) {
                obj.dict.add("/SMask");
                obj.dict.add(String.valueOf(objNumber));
                obj.dict.add("0");
                obj.dict.add("R");
            }
        }
        obj.dict.add("/Width");
        obj.dict.add(String.valueOf((int) w));
        obj.dict.add("/Height");
        obj.dict.add(String.valueOf((int) h));
        obj.dict.add("/ColorSpace");
        obj.dict.add("/" + colorSpace);
        obj.dict.add("/BitsPerComponent");
        obj.dict.add(String.valueOf(bitsPerComponent));
        if (colorSpace.equals("DeviceCMYK")) {
            // If the image was created with Photoshop - invert the colors:
            obj.dict.add("/Decode");
            obj.dict.add("[");
            obj.dict.add("1.0");
            obj.dict.add("0.0");
            obj.dict.add("1.0");
            obj.dict.add("0.0");
            obj.dict.add("1.0");
            obj.dict.add("0.0");
            obj.dict.add("1.0");
            obj.dict.add("0.0");
            obj.dict.add("]");
        }
        obj.dict.add("/Length");
        obj.dict.add(String.valueOf(data.length));
        obj.dict.add(">>");
        obj.setStream(data);
        obj.number = objects.size() + 1;
        objects.add(obj);
        objNumber = obj.number;
    }


    public void resizeToFit(Page page, boolean keepAspectRatio) {
        if (keepAspectRatio) {
            this.scaleBy(Math.min((page.width - x)/w, (page.height - y)/h));
        }
        else {
            this.scaleBy((page.width - x)/w, (page.height - y)/h);
        }
    }


    public void flipUpsideDown(boolean flipUpsideDown) {
        this.flipUpsideDown = flipUpsideDown;
    }

}   // End of Image.java
