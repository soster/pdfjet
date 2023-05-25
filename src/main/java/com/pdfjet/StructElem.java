/**
 *  StructElem.java
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
 *  Defines the StructElem types.
 */
public class StructElem {
    public static final String DOCUMENT = "Document";
    public static final String DOCUMENTFRAGMENT = "DocumentFragment";
    public static final String PART = "Part";
    public static final String DIV = "Div";
    public static final String ASIDE = "Aside";
    public static final String P = "P";
    public static final String H1 = "H1";
    public static final String H2 = "H2";
    public static final String H3 = "H3";
    public static final String H4 = "H4";
    public static final String H5 = "H5";
    public static final String H6 = "H6";
    public static final String H = "H";
    public static final String TITLE = "Title";
    public static final String FENOTE = "FENote";
    public static final String SUB = "Sub";
    public static final String LBL = "Lbl";
    public static final String SPAN = "Span";
    public static final String EM = "Em";
    public static final String STRONG = "Strong";
    public static final String LINK = "Link";
    public static final String ANNOT = "Annot";
    public static final String FORM = "Form";
    public static final String RUBY = "Ruby";
    public static final String RB = "RB";
    public static final String RT = "RT";
    public static final String RP = "RP";
    public static final String WARUCHI = "Waruchi";
    public static final String WT = "WT";
    public static final String WP = "WP";
    public static final String L = "L";
    public static final String LI = "LI";
    public static final String LBODY = "LBody";
    public static final String TABLE = "Table";
    public static final String TR = "TR";
    public static final String TH = "TH";
    public static final String TD = "TD";
    public static final String THEAD = "THead";
    public static final String TBODY = "TBody";
    public static final String TFOOT = "TFoot";
    public static final String CAPTION = "Caption";
    public static final String FIGURE = "Figure";
    public static final String FORMULA = "Formula";
    public static final String ARTIFACT = "Artifact";

    protected int objNumber;
    protected String structure = null;
    protected int pageObjNumber;
    protected int mcid = 0;
    protected String language = null;
    protected String actualText = null;
    protected String altDescription = null;
    protected Annotation annotation = null;
}
