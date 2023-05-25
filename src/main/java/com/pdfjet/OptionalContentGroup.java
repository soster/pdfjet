/**
 *  OptionalContentGroup.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * Container for drawable objects that can be drawn on a page as part of Optional Content Group.
 * Please see the PDF specification and Example_30 for more details.
 *
 * @author Mark Paxton
 */
public class OptionalContentGroup {
    protected String name;
    protected int ocgNumber;
    protected int objNumber;
    protected boolean visible;
    protected boolean printable;
    protected boolean exportable;
    private List<Drawable> components;

    /**
     * Creates OptionalContentGroup object
     *
     * @param name the name of the group
     */
    public OptionalContentGroup(String name) {
        this.name = name;
        this.components = new ArrayList<Drawable>();
    }

    /**
     * Add drawable object to the group
     *
     * @param drawable the drawable object
     */
    public void add(Drawable drawable) {
        components.add(drawable);
    }

    /**
     * Sets the visibility of this group
     *
     * @param visible flag
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the printability of this group
     *
     * @param printable flag
     */
    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    /**
     * Sets the exportability of this group
     *
     * @param exportable flag
     */
    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    /**
     * Draws this content group on a page
     *
     * @param page the page to draw on
     * @throws Exception if there is a problem
     */
    public void drawOn(Page page) throws Exception {
        if (!components.isEmpty()) {
            page.pdf.groups.add(this);
            ocgNumber = page.pdf.groups.size();

            page.pdf.newobj();
            page.pdf.append("<<\n");
            page.pdf.append("/Type /OCG\n");
            page.pdf.append("/Name (" + name + ")\n");
            page.pdf.append("/Usage <<\n");
            if (visible) {
                page.pdf.append("/View << /ViewState /ON >>\n");
            } else {
                page.pdf.append("/View << /ViewState /OFF >>\n");
            }
            if (printable) {
                page.pdf.append("/Print << /PrintState /ON >>\n");
            } else {
                page.pdf.append("/Print << /PrintState /OFF >>\n");
            }
            if (exportable) {
                page.pdf.append("/Export << /ExportState /ON >>\n");
            } else {
                page.pdf.append("/Export << /ExportState /OFF >>\n");
            }
            page.pdf.append(">>\n");
            page.pdf.append(">>\n");
            page.pdf.endobj();

            objNumber = page.pdf.getObjNumber();

            page.append("/OC /OC");
            page.append(ocgNumber);
            page.append(" BDC\n");
            for (Drawable component : components) {
                component.drawOn(page);
            }
            page.append("\nEMC\n");
        }
    }
}   // End of OptionalContentGroup.java
