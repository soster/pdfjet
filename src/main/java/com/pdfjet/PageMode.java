/**
 *  PageMode.java
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
 *  Used to specify the PDF page mode.
 *
 */
public class PageMode {
    public static final String USE_NONE = "UseNone";            // Neither document outline nor thumbnail images visible
    public static final String USE_OUTLINES = "UseOutlines";    // Document outline visible
    public static final String USE_THUMBS = "UseThumbs";        // Thumbnail images visible
    public static final String FULL_SCREEN = "FullScreen";      // Full-screen mode
    public static final String USE_OC = "UseOC";                // (PDF 1.5) Optional content group panel visible
    public static final String USE_ATTACHMENTS = "UseAttachements";
}
