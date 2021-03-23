/**
 *  FileAttachment.java
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


    public FileAttachment(PDF pdf, EmbeddedFile file) {
        this.pdf = pdf;
        this.embeddedFile = file;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(double x, double y) {
        setPosition((float) x, (float) y);
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(double x, double y) {
        setLocation((float) x, (float) y);
    }


    public void setIconPushPin() {
        this.icon = "PushPin";
    }


    public void setIconPaperclip() {
        this.icon = "Paperclip";
    }


    public void setIconSize(float height) {
        this.h = height;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setDescription(String description) {
        this.contents = description;
    }


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
