/**
 *  FileAttachment.java
 *
Copyright 2023 Innovatics Inc.

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

/**
 *  Used to attach file objects.
 *
 */
public class FileAttachment implements Drawable {

    protected int objNumber = -1;
    protected PDF pdf;
    protected EmbeddedFile embeddedFile;
    protected String icon = "PushPin";
    protected String title = "";
    protected String contents = "Right mouse click or double click on the icon to save the attached file.";
    protected float x = 0f;
    protected float y = 0f;
    protected float h = 24f;

    /**
     * Create file attachement object
     * 
     * @param pdf the PDF that the object is attached to
     * @param file the enbedded file object
     */
    public FileAttachment(PDF pdf, EmbeddedFile file) {
        this.pdf = pdf;
        this.embeddedFile = file;
    }

    /**
     * Sets the position of the file attachment on the page
     * 
     * @param x the horizontal position of the attachement
     * @param y the vertical position of the attachement
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the position of the file attachment on the page
     * 
     * @param x the horizontal position of the attachement
     * @param y the vertical position of the attachement
     */
    public void setPosition(double x, double y) {
        setPosition((float) x, (float) y);
    }


    /**
     * Sets the location of the file attachment on the page
     * 
     * @param x the horizontal location of the attachement
     * @param y the vertical location of the attachement
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Sets the location of the file attachment on the page
     * 
     * @param x the horizontal location of the attachement
     * @param y the vertical location of the attachement
     */
    public void setLocation(double x, double y) {
        setLocation((float) x, (float) y);
    }


    /**
     * Sets the icon for the attachment to be "PushPin"
     */
    public void setIconPushPin() {
        this.icon = "PushPin";
    }


    /**
     * Sets the icon for the attachment to be "Paperclip"
     */
    public void setIconPaperclip() {
        this.icon = "Paperclip";
    }


    /**
     * Sets the icon size
     * 
     * @param height the vertical icon size
     */
    public void setIconSize(float height) {
        this.h = height;
    }


    /**
     * Sets the title for this attachment
     * 
     * @param title the attachment title
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Sets the attachment description
     * 
     * @param description the description for the attachment
     */
    public void setDescription(String description) {
        this.contents = description;
    }


    /**
     * Draw the attachment on the page.
     * 
     * @param page the page to draw on
     */
    public float[] drawOn(Page page) throws Exception {
        Annotation annotation = new Annotation(
                null,
                null,
                x,
                y,
                x + h,
                y + h,
                null,
                null,
                null);
        annotation.fileAttachment = this;
        page.addAnnotation(annotation);
        return new float[] {this.x + this.h, this.y + this.h};
    }

}   // End of FileAttachment.java
