/**
 *  PDF.java
 *
 Copyright (c) 2016, Innovatics Inc.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;



/**
 *  Used to create PDF objects that represent PDF documents.
 *
 *
 */
public class PDF {
    private boolean eval = true;

    protected int objNumber = 0;
    protected int metadataObjNumber = 0;
    protected int outputIntentObjNumber = 0;
    protected List<Font> fonts = new ArrayList();
    protected List<Image> images = new ArrayList();
    protected List<Page> pages = new ArrayList();
    protected Map<String, Destination> destinations = new HashMap();
    protected List<OptionalContentGroup> groups = new ArrayList();
    protected Map<String, Integer> states = new HashMap();

    public Map<String, Integer> getStates() {
        return states;
    }

    protected static final DecimalFormat df = new DecimalFormat("0.###", new DecimalFormatSymbols(Locale.US));
    protected int compliance = 0;
    protected List<EmbeddedFile> embeddedFiles = new ArrayList();

    private OutputStream os = null;
    private List<Integer> objOffset = new ArrayList();
    private String producer = "ScribMaster / HandWrite PDF engine";
    private String creationDate;
    private String createDate;
    private String title = "";
    private String subject = "";
    private String author = "";
    private int byte_count = 0;
    private int pagesObjNumber = -1;
    private String pageLayout = null;
    private String pageMode = null;
    private String language = "en-US";

    protected Bookmark toc = null;
    protected List<String> importedFonts = new ArrayList();
    protected String extGState = "";


    /**
     * The default constructor - use when reading PDF files.
     *
     * @throws Exception
     */
    public PDF() throws Exception {
    }


    /**
     *  Creates a PDF object that represents a PDF document.
     *
     *  @param os the associated output stream.
     */
    public PDF(OutputStream os) throws Exception { this(os, 0); }


    // Here is the layout of the PDF document:
    //
    // Metadata Object
    // Output Intent Object
    // Fonts
    // Images
    // Resources Object
    // Content1
    // Content2
    // ...
    // ContentN
    // Annot1
    // Annot2
    // ...
    // AnnotN
    // Page1
    // Page2
    // ...
    // PageN
    // Pages
    // StructElem1
    // StructElem2
    // ...
    // StructElemN
    // StructTreeRoot
    // Info
    // Root
    // xref table
    // Trailer
    /**
     *  Creates a PDF object that represents a PDF document.
     *  Use this constructor to create PDF/A compliant PDF documents.
     *  Please note: PDF/A compliance requires all fonts to be embedded in the PDF.
     *
     *  @param os the associated output stream.
     *  @param compliance must be: Compliance.PDF_A_1B
     */
    public PDF(OutputStream os, int compliance) throws Exception {

        this.os = os;
        this.compliance = compliance;

        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        creationDate = sdf1.format(date);
        createDate = sdf2.format(date);

        append("%PDF-1.5\n");
        append('%');
        append((byte) 0x00F2);
        append((byte) 0x00F3);
        append((byte) 0x00F4);
        append((byte) 0x00F5);
        append((byte) 0x00F6);
        append('\n');

        if (compliance == Compliance.PDF_A_1B ||
                compliance == Compliance.PDF_UA) {
            metadataObjNumber = addMetadataObject("", false);
            outputIntentObjNumber = addOutputIntentObject();
        }

    }

    protected void newobj()
            throws IOException {
        this.objOffset.add(Integer.valueOf(this.byte_count));
        append(++this.objNumber);
        append(" 0 obj\n");
    }

    protected void endobj() throws IOException {
        append("endobj\n");
    }

    protected int addMetadataObject(String notice, boolean fontMetadataObject) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xpacket begin='﻿' id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n");
        sb.append("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n");
        sb.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n");

        if (fontMetadataObject) {
            sb.append("<rdf:Description rdf:about=\"\" xmlns:xmpRights=\"http://ns.adobe.com/xap/1.0/rights/\">\n");
            sb.append("<xmpRights:UsageTerms>\n");
            sb.append("<rdf:Alt>\n");
            sb.append("<rdf:li xml:lang=\"x-default\">\n");
            sb.append(notice);
            sb.append("</rdf:li>\n");
            sb.append("</rdf:Alt>\n");
            sb.append("</xmpRights:UsageTerms>\n");
            sb.append("</rdf:Description>\n");
        } else {
            sb.append("<rdf:Description rdf:about=\"\" xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\" pdf:Producer=\"");
            sb.append(this.producer);
            sb.append("\">\n</rdf:Description>\n");

            sb.append("<rdf:Description rdf:about=\"\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n");
            sb.append("  <dc:format>application/pdf</dc:format>\n");
            sb.append("  <dc:title><rdf:Alt><rdf:li xml:lang=\"x-default\">");
            sb.append(this.title);
            sb.append("</rdf:li></rdf:Alt></dc:title>\n");
            sb.append("  <dc:creator><rdf:Seq><rdf:li>");
            sb.append(this.author);
            sb.append("</rdf:li></rdf:Seq></dc:creator>\n");
            sb.append("  <dc:description><rdf:Alt><rdf:li xml:lang=\"x-default\">");
            sb.append(this.subject);
            sb.append("</rdf:li></rdf:Alt></dc:description>\n");
            sb.append("</rdf:Description>\n");

            sb.append("<rdf:Description rdf:about=\"\" xmlns:pdfaid=\"http://www.aiim.org/pdfa/ns/id/\">\n");
            sb.append("  <pdfaid:part>1</pdfaid:part>\n");
            sb.append("  <pdfaid:conformance>B</pdfaid:conformance>\n");
            sb.append("</rdf:Description>\n");

            if (this.compliance == 2) {
                sb.append("<rdf:Description rdf:about=\"\" xmlns:pdfuaid=\"http://www.aiim.org/pdfua/ns/id/\">\n");
                sb.append("  <pdfuaid:part>1</pdfuaid:part>\n");
                sb.append("</rdf:Description>\n");
            }

            sb.append("<rdf:Description rdf:about=\"\" xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\">\n");
            sb.append("<xmp:CreateDate>");
            sb.append(this.createDate + "Z");
            sb.append("</xmp:CreateDate>\n");
            sb.append("</rdf:Description>\n");
        }

        sb.append("</rdf:RDF>\n");
        sb.append("</x:xmpmeta>\n");

        if (!fontMetadataObject) {
            // Add the recommended 2000 bytes padding
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 10; j++) {
                    sb.append("          ");
                }
                sb.append("\n");
            }
        }

        sb.append("<?xpacket end=\"w\"?>");

        byte[] xml = sb.toString().getBytes("UTF-8");


        newobj();
        append("<<\n");
        append("/Type /Metadata\n");
        append("/Subtype /XML\n");
        append("/Length ");
        append(xml.length);
        append("\n");
        append(">>\n");
        append("stream\n");
        append(xml, 0, xml.length);
        append("\nendstream\n");
        endobj();

        return this.objNumber;
    }

    protected int addOutputIntentObject() throws Exception {
        newobj();
        append("<<\n");
        append("/N 3\n");

        append("/Length ");
        append(ICCBlackScaled.profile.length);
        append("\n");

        append("/Filter /FlateDecode\n");
        append(">>\n");
        append("stream\n");
        append(ICCBlackScaled.profile, 0, ICCBlackScaled.profile.length);
        append("\nendstream\n");
        endobj();


        newobj();
        append("<<\n");
        append("/Type /OutputIntent\n");
        append("/S /GTS_PDFA1\n");
        append("/OutputCondition (sRGB IEC61966-2.1)\n");
        append("/OutputConditionIdentifier (sRGB IEC61966-2.1)\n");
        append("/Info (sRGB IEC61966-2.1)\n");
        append("/DestOutputProfile ");
        append(this.objNumber - 1);
        append(" 0 R\n");
        append(">>\n");
        endobj();

        return this.objNumber;
    }

    private int addResourcesObject() throws Exception {
        newobj();
        append("<<\n");

        if (!this.extGState.equals(""))
            append(this.extGState);
        Object obj;
        int i;
        if ((!this.fonts.isEmpty()) || (!this.importedFonts.isEmpty())) {
            append("/Font\n");
            append("<<\n");
            for (Iterator it = this.importedFonts.iterator(); it.hasNext(); ) {
                obj = (String) it.next();
                append((String) obj);
                if (((String) obj).equals("R")) {
                    append('\n');
                } else {
                    append(' ');
                }
            }
            for (i = 0; i < this.fonts.size(); i++) {
                obj = (Font) this.fonts.get(i);
                append("/F");
                append(((Font) obj).objNumber);
                append(' ');
                append(((Font) obj).objNumber);
                append(" 0 R\n");
            }
            append(">>\n");
        }

        if (!this.images.isEmpty()) {
            append("/XObject\n");
            append("<<\n");
            for (i = 0; i < this.images.size(); i++) {
                obj = (Image) this.images.get(i);
                append("/Im");
                append(((Image) obj).objNumber);
                append(' ');
                append(((Image) obj).objNumber);
                append(" 0 R\n");
            }
            append(">>\n");
        }

        if (!this.groups.isEmpty()) {
            append("/Properties\n");
            append("<<\n");
            for (i = 0; i < this.groups.size(); i++) {
                obj = (OptionalContentGroup) this.groups.get(i);
                append("/OC");
                append(i + 1);
                append(' ');
                append(((OptionalContentGroup) obj).objNumber);
                append(" 0 R\n");
            }
            append(">>\n");
        }


        if (!this.states.isEmpty()) {
            append("/ExtGState <<\n");
            for (Iterator it = this.states.keySet().iterator(); it.hasNext(); ) {
                obj = (String) it.next();
                append("/GS");
                append(((Integer) this.states.get(obj)).intValue());
                append(" << ");
                append((String) obj);
                append(" >>\n");
            }
            append(">>\n");
        }

        append(">>\n");
        endobj();
        return this.objNumber;
    }


    private int addPagesObject() throws Exception {
        newobj();
        append("<<\n");
        append("/Type /Pages\n");
        append("/Kids [\n");
        for (int i = 0; i < this.pages.size(); i++) {
            Page localPage = (Page) this.pages.get(i);
            if (this.compliance == 2) {
                localPage.setStructElementsPageObjNumber(localPage.objNumber);
            }
            append(localPage.objNumber);
            append(" 0 R\n");
        }
        append("]\n");
        append("/Count ");
        append(this.pages.size());
        append('\n');
        append(">>\n");
        endobj();
        return this.objNumber;
    }

    private int addInfoObject()
            throws Exception {
        newobj();
        append("<<\n");
        append("/Title (");
        append(this.title);
        append(")\n");
        append("/Subject (");
        append(this.subject);
        append(")\n");
        append("/Author (");
        append(this.author);
        append(")\n");
        append("/Producer (");
        append(this.producer);
        append(")\n");
        append("/CreationDate (D:");
        append(this.creationDate);
        append("Z)\n");
        append(">>\n");
        endobj();
        return this.objNumber;
    }

    private int addStructTreeRootObject() throws Exception {
        newobj();
        append("<<\n");
        append("/Type /StructTreeRoot\n");
        append("/K [\n");
        for (int i = 0; i < this.pages.size(); i++) {
            Page localPage = (Page) this.pages.get(i);
            for (int j = 0; j < localPage.structures.size(); j++) {
                append(((StructElem) localPage.structures.get(j)).objNumber);
                append(" 0 R\n");
            }
        }
        append("]\n");
        append("/ParentTree ");
        append(this.objNumber + 1);
        append(" 0 R\n");
        append(">>\n");
        endobj();
        return this.objNumber;
    }

    private void addStructElementObjects() throws Exception {
        int i = this.objNumber + 1;
        Page localPage;
        for (int j = 0; j < this.pages.size(); j++) {
            localPage = (Page) this.pages.get(j);
            i += localPage.structures.size();
        }

        for (int j = 0; j < this.pages.size(); j++) {
            localPage = (Page) this.pages.get(j);
            for (int k = 0; k < localPage.structures.size(); k++) {
                newobj();
                StructElem localStructElem = (StructElem) localPage.structures.get(k);
                localStructElem.objNumber = this.objNumber;
                append("<<\n");
                append("/Type /StructElem\n");
                append("/S /");
                append(localStructElem.structure);
                append("\n");
                append("/P ");
                append(i);
                append(" 0 R\n");
                append("/Pg ");
                append(localStructElem.pageObjNumber);
                append(" 0 R\n");
                if (localStructElem.annotation == null) {
                    append("/K ");
                    append(localStructElem.mcid);
                    append("\n");
                } else {
                    append("/K <<\n");
                    append("/Type /OBJR\n");
                    append("/Obj ");
                    append(localStructElem.annotation.objNumber);
                    append(" 0 R\n");
                    append(">>\n");
                }
                if (localStructElem.language != null) {
                    append("/Lang (");
                    append(localStructElem.language);
                    append(")\n");
                }
                append("/Alt (");
                append(escapeSpecialCharacters(localStructElem.altDescription));
                append(")\n");
                append("/ActualText (");
                append(escapeSpecialCharacters(localStructElem.actualText));
                append(")\n");
                append(">>\n");
                endobj();
            }
        }
    }

    private String escapeSpecialCharacters(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c == '(') || (c == ')') || (c == '\\')) {
                buf.append('\\');
            }
            buf.append(c);
        }
        return buf.toString();
    }

    private void addNumsParentTree() throws Exception {
        newobj();
        append("<<\n");
        append("/Nums [\n");
        for (int i = 0; i < this.pages.size(); i++) {
            Page localPage1 = (Page) this.pages.get(i);
            append(i);
            append(" [\n");
            for (int k = 0; k < localPage1.structures.size(); k++) {
                StructElem localStructElem1 = (StructElem) localPage1.structures.get(k);
                if (localStructElem1.annotation == null) {
                    append(localStructElem1.objNumber);
                    append(" 0 R\n");
                }
            }
            append("]\n");
        }

        int i = this.pages.size();
        for (int j = 0; j < this.pages.size(); j++) {
            Page localPage2 = (Page) this.pages.get(j);
            for (int m = 0; m < localPage2.structures.size(); m++) {
                StructElem localStructElem2 = (StructElem) localPage2.structures.get(m);
                if (localStructElem2.annotation != null) {
                    append(i);
                    append(" ");
                    append(localStructElem2.objNumber);
                    append(" 0 R\n");
                    i++;
                }
            }
        }
        append("]\n");
        append(">>\n");
        endobj();
    }


    private int addRootObject(int structTreeRootObjNumber, int outlineDictNumber)
            throws Exception {
        newobj();
        append("<<\n");
        append("/Type /Catalog\n");

        if (this.compliance == 2) {
            append("/Lang (");
            append(this.language);
            append(")\n");

            append("/StructTreeRoot ");
            append(structTreeRootObjNumber);
            append(" 0 R\n");

            append("/MarkInfo <</Marked true>>\n");
            append("/ViewerPreferences <</DisplayDocTitle true>>\n");
        }

        if (this.pageLayout != null) {
            append("/PageLayout /");
            append(this.pageLayout);
            append("\n");
        }

        if (this.pageMode != null) {
            append("/PageMode /");
            append(this.pageMode);
            append("\n");
        }

        addOCProperties();

        append("/Pages ");
        append(this.pagesObjNumber);
        append(" 0 R\n");

        if ((this.compliance == 1) || (this.compliance == 2)) {
            append("/Metadata ");
            append(this.metadataObjNumber);
            append(" 0 R\n");

            append("/OutputIntents [");
            append(this.outputIntentObjNumber);
            append(" 0 R]\n");
        }

        if (outlineDictNumber > 0) {
            append("/Outlines ");
            append(outlineDictNumber);
            append(" 0 R\n");
        }

        append(">>\n");
        endobj();
        return this.objNumber;
    }

    private void addPageBox(String boxName, Page page, float[] rect) throws Exception {
        append("/");
        append(boxName);
        append(" [");
        append(rect[0]);
        append(' ');
        append(page.height - rect[3]);
        append(' ');
        append(rect[2]);
        append(' ');
        append(page.height - rect[1]);
        append("]\n");
    }

    private void setDestinationObjNumbers() {
        int i = 0;
        Page localPage;
        for (int j = 0; j < this.pages.size(); j++) {
            localPage = (Page) this.pages.get(j);
            i += localPage.annots.size();
        }
        for (int j = 0; j < this.pages.size(); j++) {
            localPage = (Page) this.pages.get(j);
            for (Destination localDestination : localPage.destinations) {
                localDestination.pageObjNumber = (this.objNumber + i + j + 1);

                this.destinations.put(localDestination.name, localDestination);
            }
        }
    }

    private void addAllPages(int resObjNumber)
            throws Exception {
        setDestinationObjNumbers();
        addAnnotDictionaries();


        this.pagesObjNumber = (this.objNumber + this.pages.size() + 1);

        for (int i = 0; i < this.pages.size(); i++) {
            Page localPage = (Page) this.pages.get(i);


            newobj();
            localPage.objNumber = this.objNumber;
            append("<<\n");
            append("/Type /Page\n");
            append("/Parent ");
            append(this.pagesObjNumber);
            append(" 0 R\n");
            append("/MediaBox [0.0 0.0 ");
            append(localPage.width);
            append(' ');
            append(localPage.height);
            append("]\n");

            if (localPage.cropBox != null) {
                addPageBox("CropBox", localPage, localPage.cropBox);
            }
            if (localPage.bleedBox != null) {
                addPageBox("BleedBox", localPage, localPage.bleedBox);
            }
            if (localPage.trimBox != null) {
                addPageBox("TrimBox", localPage, localPage.trimBox);
            }
            if (localPage.artBox != null) {
                addPageBox("ArtBox", localPage, localPage.artBox);
            }

            append("/Resources ");
            append(resObjNumber);
            append(" 0 R\n");

            append("/Contents [ ");
            for (Iterator localIterator = localPage.contents.iterator(); localIterator.hasNext(); ) {
                Object localObject = (Integer) localIterator.next();
                append(((Integer) localObject).intValue());
                append(" 0 R ");
            }
            Object localObject;
            append("]\n");
            if (localPage.annots.size() > 0) {
                append("/Annots [ ");
                for (Iterator localIterator = localPage.annots.iterator(); localIterator.hasNext(); ) {
                    localObject = (Annotation) localIterator.next();
                    append(((Annotation) localObject).objNumber);
                    append(" 0 R ");
                }
                append("]\n");
            }

            if (this.compliance == 2) {
                append("/Tabs /S\n");
                append("/StructParents ");
                append(i);
                append("\n");
            }

            append(">>\n");
            endobj();
        }
    }


    private void addPageContent(Page page)
            throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DeflaterOutputStream dos = new DeflaterOutputStream((OutputStream) baos, new Deflater());


        byte[] buf = page.buf.toByteArray();
        dos.write((byte[]) buf, 0, buf.length);
        dos.finish();
        page.buf = null;

        newobj();
        append("<<\n");
        append("/Filter /FlateDecode\n");
        append("/Length ");
        append(((ByteArrayOutputStream) baos).size());
        append("\n");
        append(">>\n");
        append("stream\n");
        append((ByteArrayOutputStream) baos);
        append("\nendstream\n");
        endobj();
        page.contents.add(Integer.valueOf(this.objNumber));
    }


    private int addAnnotationObject(Annotation annot, int index)
            throws Exception {
        newobj();
        annot.objNumber = this.objNumber;
        append("<<\n");
        append("/Type /Annot\n");
        if (annot.fileAttachment != null) {
            append("/Subtype /FileAttachment\n");
            append("/T (");
            append(annot.fileAttachment.title);
            append(")\n");
            append("/Contents (");
            append(annot.fileAttachment.contents);
            append(")\n");
            append("/FS ");
            append(annot.fileAttachment.embeddedFile.objNumber);
            append(" 0 R\n");
            append("/Name /");
            append(annot.fileAttachment.icon);
            append("\n");
        } else {
            append("/Subtype /Link\n");
        }
        append("/Rect [");
        append(annot.x1);
        append(' ');
        append(annot.y1);
        append(' ');
        append(annot.x2);
        append(' ');
        append(annot.y2);
        append("]\n");
        append("/Border [0 0 0]\n");
        if (annot.uri != null) {
            append("/F 4\n");
            append("/A <<\n");
            append("/S /URI\n");
            append("/URI (");
            append(annot.uri);
            append(")\n");
            append(">>\n");
        } else if (annot.key != null) {
            Destination localDestination = (Destination) this.destinations.get(annot.key);
            if (localDestination != null) {
                append("/F 4\n");
                append("/Dest [");
                append(localDestination.pageObjNumber);
                append(" 0 R /XYZ 0 ");
                append(localDestination.yPosition);
                append(" 0]\n");
            }
        }
        if (index != -1) {
            append("/StructParent ");
            append(index++);
            append("\n");
        }
        append(">>\n");
        endobj();

        return index;
    }

    private void addAnnotDictionaries() throws Exception {
        int i = this.pages.size();
        for (int j = 0; j < this.pages.size(); j++) {
            Page page = (Page) this.pages.get(j);
            int index;
            Object element;
            if (page.structures.size() > 0) {
                for (index = 0; index < page.structures.size(); index++) {
                    element = (StructElem) page.structures.get(index);
                    if (((StructElem) element).annotation != null) {
                        i = addAnnotationObject(((StructElem) element).annotation, i);
                    }

                }
            } else if (page.annots.size() > 0) {
                for (index = 0; index < page.annots.size(); index++) {
                    element = (Annotation) page.annots.get(index);
                    if (element != null) {
                        addAnnotationObject((Annotation) element, -1);
                    }
                }
            }
        }
    }

    private void addOCProperties() throws Exception {
        if (!this.groups.isEmpty()) {
            append("/OCProperties\n");
            append("<<\n");
            append("/OCGs [");
            for (Iterator localIterator = this.groups.iterator(); localIterator.hasNext(); ) {
                OptionalContentGroup ocg = (OptionalContentGroup) localIterator.next();
                append(' ');
                append(ocg.objNumber);
                append(" 0 R");
            }
            OptionalContentGroup localOptionalContentGroup;
            append(" ]\n");
            append("/D <<\n");
            append("/BaseState /OFF\n");
            append("/ON [");
            for (Iterator localIterator = this.groups.iterator(); localIterator.hasNext(); ) {
                localOptionalContentGroup = (OptionalContentGroup) localIterator.next();
                if (localOptionalContentGroup.visible) {
                    append(' ');
                    append(localOptionalContentGroup.objNumber);
                    append(" 0 R");
                }
            }
            append(" ]\n");

            append("/AS [\n");
            append("<< /Event /Print /Category [/Print] /OCGs [");
            for (Iterator localIterator = this.groups.iterator(); localIterator.hasNext(); ) {
                localOptionalContentGroup = (OptionalContentGroup) localIterator.next();
                if (localOptionalContentGroup.printable) {
                    append(' ');
                    append(localOptionalContentGroup.objNumber);
                    append(" 0 R");
                }
            }
            append(" ] >>\n");
            append("<< /Event /Export /Category [/Export] /OCGs [");
            for (Iterator localIterator = this.groups.iterator(); localIterator.hasNext(); ) {
                localOptionalContentGroup = (OptionalContentGroup) localIterator.next();
                if (localOptionalContentGroup.exportable) {
                    append(' ');
                    append(localOptionalContentGroup.objNumber);
                    append(" 0 R");
                }
            }
            append(" ] >>\n");
            append("]\n");

            append("/Order [[ ()");
            for (Iterator localIterator = this.groups.iterator(); localIterator.hasNext(); ) {
                localOptionalContentGroup = (OptionalContentGroup) localIterator.next();
                append(' ');
                append(localOptionalContentGroup.objNumber);
                append(" 0 R");
            }
            append(" ]]\n");
            append(">>\n");
            append(">>\n");
        }
    }

    public void addPage(Page page) throws Exception {
        int i = this.pages.size();
        if (i > 0) {
            addPageContent((Page) this.pages.get(i - 1));
        }
        this.pages.add(page);
    }


    public void flush()
            throws Exception {
        flush(false);
    }


    public void close()
            throws Exception {
        flush(true);
    }

    private void flush(boolean close) throws Exception {
        if (this.pagesObjNumber == -1) {
            addPageContent((Page) this.pages.get(this.pages.size() - 1));
            addAllPages(addResourcesObject());
            addPagesObject();
        }

        int i = 0;
        if (this.compliance == 2) {
            addStructElementObjects();
            i = addStructTreeRootObject();
            addNumsParentTree();
        }

        int outlineDictNum = 0;
        if ((this.toc != null) && (this.toc.getChildren() != null)) {
            List tocs = this.toc.toArrayList();
            outlineDictNum = addOutlineDict(this.toc);
            for (int m = 1; m < tocs.size(); m++) {
                Bookmark localBookmark = (Bookmark) tocs.get(m);
                addOutlineItem(outlineDictNum, m, localBookmark);
            }
        }

        int infoObjNumber = addInfoObject();
        int rootObjNumber = addRootObject(i, outlineDictNum);

        int startxref = this.byte_count;


        append("xref\n");
        append("0 ");
        append(rootObjNumber + 1);
        append('\n');

        append("0000000000 65535 f \n");
        for (int i1 = 0; i1 < this.objOffset.size(); i1++) {
            int i2 = ((Integer) this.objOffset.get(i1)).intValue();
            String str2 = Integer.toString(i2);
            for (int i3 = 0; i3 < 10 - str2.length(); i3++) {
                append('0');
            }
            append(str2);
            append(" 00000 n \n");
        }
        append("trailer\n");
        append("<<\n");
        append("/Size ");
        append(rootObjNumber + 1);
        append('\n');

        String str1 = new Salsa20().getID();
        append("/ID[<");
        append(str1);
        append("><");
        append(str1);
        append(">]\n");

        append("/Info ");
        append(infoObjNumber);
        append(" 0 R\n");

        append("/Root ");
        append(rootObjNumber);
        append(" 0 R\n");

        append(">>\n");
        append("startxref\n");
        append(startxref);
        append('\n');
        append("%%EOF\n");

        this.os.flush();
        if (close) {
            this.os.close();
        }
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPageLayout(String pageLayout) {
        this.pageLayout = pageLayout;
    }

    public void setPageMode(String pageMode) {
        this.pageMode = pageMode;
    }

    protected void append(int num) throws IOException {
        append(Integer.toString(num));
    }

    protected void append(float val) throws IOException {
        append(df.format(val));
    }

    protected void append(String str) throws IOException {
        int i = str.length();
        for (int j = 0; j < i; j++) {
            this.os.write((byte) str.charAt(j));
        }
        this.byte_count += i;
    }

    protected void append(char ch) throws IOException {
        append((byte) ch);
    }

    protected void append(byte b) throws IOException {
        this.os.write(b);
        this.byte_count += 1;
    }

    protected void append(byte[] buf, int off, int len) throws IOException {
        this.os.write(buf, off, len);
        this.byte_count += len;
    }

    protected void append(ByteArrayOutputStream paramByteArrayOutputStream) throws IOException {
        paramByteArrayOutputStream.writeTo(this.os);
        this.byte_count += paramByteArrayOutputStream.size();
    }

    /**
     *  Returns a list of objects of type PDFobj read from input stream.
     *
     *  @param inputStream the PDF input stream.
     *
     *  @return List<PDFobj> the list of PDF objects.
     */
    public Map<Integer, PDFobj> read(InputStream inputStream)
            throws Exception {
        ArrayList<PDFobj> localArrayList = new ArrayList();

        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = inputStream.read()) != -1) {
            localByteArrayOutputStream.write(i);
        }
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();

        int j = getStartXRef(arrayOfByte);
        PDFobj localPDFobj1 = getObject(arrayOfByte, j);
        if ((localPDFobj1.dict.get(0)).equals("xref")) {
            getObjects1(arrayOfByte, localPDFobj1, localArrayList);
        } else {
            getObjects2(arrayOfByte, localPDFobj1, localArrayList);
        }

        TreeMap localTreeMap = new TreeMap();
        for (PDFobj localPDFobj2 : localArrayList) {
            if (localPDFobj2.dict.contains("stream")) {
                localPDFobj2.setStream(arrayOfByte, localPDFobj2.getLength(localArrayList));
                if (localPDFobj2.getValue("/Filter").equals("/FlateDecode")) {
                    Decompressor localDecompressor = new Decompressor(localPDFobj2.stream);
                    localPDFobj2.data = localDecompressor.getDecompressedData();
                } else {
                    localPDFobj2.data = localPDFobj2.stream;
                }
            }

            if (localPDFobj2.getValue("/Type").equals("/ObjStm")) {
                int k = Integer.valueOf(localPDFobj2.getValue("/N")).intValue();
                int m = Integer.valueOf(localPDFobj2.getValue("/First")).intValue();
                PDFobj localPDFobj3 = getObject(localPDFobj2.data, 0, m);
                for (int n = 0; n < localPDFobj3.dict.size(); n += 2) {
                    int i1 = Integer.valueOf(localPDFobj3.dict.get(n)).intValue();
                    int i2 = Integer.valueOf(localPDFobj3.dict.get(n + 1)).intValue();
                    int i3 = localPDFobj2.data.length;
                    if (n <= localPDFobj3.dict.size() - 4) {
                        i3 = m + Integer.valueOf((String) localPDFobj3.dict.get(n + 3)).intValue();
                    }
                    PDFobj localPDFobj4 = getObject(localPDFobj2.data, m + i2, i3);
                    localPDFobj4.dict.add(0, "obj");
                    localPDFobj4.dict.add(0, "0");
                    localPDFobj4.dict.add(0, Integer.toString(i1));
                    localTreeMap.put(Integer.valueOf(i1), localPDFobj4);
                }
            } else {
                localTreeMap.put(Integer.valueOf(localPDFobj2.number), localPDFobj2);
            }
        }

        return localTreeMap;
    }


    private boolean process(PDFobj obj, StringBuilder sb1, byte[] buf, int off) {
        String str = sb1.toString().trim();
        if (!str.equals("")) {
            obj.dict.add(str);
        }
        sb1.setLength(0);

        if (str.equals("endobj")) {
            return true;
        }
        if (str.equals("stream")) {
            obj.stream_offset = off;
            if (buf[off] == 10) {
                obj.stream_offset += 1;
            }
            return true;
        }
        if (str.equals("startxref")) {
            return true;
        }
        return false;
    }

    private PDFobj getObject(byte[] pdf, int off) {
        return getObject(pdf, off, pdf.length);
    }


    private PDFobj getObject(byte[] pdf, int off, int len) {
        PDFobj obj = new PDFobj(off);
        StringBuilder localStringBuilder = new StringBuilder();

        int p = 0;
        char c1 = ' ';
        boolean done = false;
        while ((!done) && (off < len)) {
            char c2 = (char) pdf[(off++)];
            if (c2 == '(') {
                if (p == 0) {
                    done = process(obj, localStringBuilder, pdf, off);
                }
                if (!done) {
                    localStringBuilder.append(c2);
                    p++;
                }
            } else if (c2 == ')') {
                localStringBuilder.append(c2);
                p--;
                if (p == 0) {
                    done = process(obj, localStringBuilder, pdf, off);
                }
            } else if ((c2 == 0) || (c2 == '\t') || (c2 == '\n') || (c2 == '\f') || (c2 == '\r') || (c2 == ' ')) {


                done = process(obj, localStringBuilder, pdf, off);
                if (!done) {
                    c1 = ' ';
                }
            } else if (c2 == '/') {
                done = process(obj, localStringBuilder, pdf, off);
                if (!done) {
                    localStringBuilder.append(c2);
                    c1 = c2;
                }
            } else if ((c2 == '<') || (c2 == '>') || (c2 == '%')) {
                if (c2 != c1) {
                    done = process(obj, localStringBuilder, pdf, off);
                    if (!done) {
                        localStringBuilder.append(c2);
                        c1 = c2;
                    }
                } else {
                    localStringBuilder.append(c2);
                    done = process(obj, localStringBuilder, pdf, off);
                    if (!done) {
                        c1 = ' ';
                    }
                }
            } else if ((c2 == '[') || (c2 == ']') || (c2 == '{') || (c2 == '}')) {
                done = process(obj, localStringBuilder, pdf, off);
                if (!done) {
                    obj.dict.add(String.valueOf(c2));
                    c1 = c2;
                }
            } else {
                localStringBuilder.append(c2);
                if (p == 0) {
                    c1 = c2;
                }
            }
        }

        return obj;
    }


    private int toInt(byte[] buf, int off, int len) {
        int i = 0;
        for (int j = 0; j < len; j++) {
            i |= buf[(off + j)] & 0xFF;
            if (j < len - 1) {
                i <<= 8;
            }
        }
        return i;
    }


    private void getObjects1(byte[] pdf, PDFobj obj, List<PDFobj> objects)
            throws Exception {
        String xref = obj.getValue("/Prev");
        if (!xref.equals("")) {
            getObjects1(pdf, getObject(pdf, Integer.valueOf(xref).intValue()), objects);
        }


        int i = 1;
        for (; ; ) {
            String token = (String) obj.dict.get(i++);
            if (token.equals("trailer")) {
                break;
            }

            int n = Integer.valueOf((String) obj.dict.get(i++)).intValue();
            for (int k = 0; k < n; k++) {
                String str3 = (String) obj.dict.get(i++);
                String str4 = (String) obj.dict.get(i++);
                String str5 = (String) obj.dict.get(i++);
                if (!str5.equals("f")) {
                    PDFobj localPDFobj = getObject(pdf, Integer.valueOf(str3).intValue());
                    // FIXME fixes error with some files
                    if (localPDFobj.dict.get(0).startsWith("%PDF")) {
                        continue;
                    }
                    localPDFobj.number = Integer.valueOf((String) localPDFobj.dict.get(0)).intValue();
                    objects.add(localPDFobj);
                }
            }
        }
    }


    private void getObjects2(byte[] pdf, PDFobj obj, List<PDFobj> objects)
            throws Exception {
        String str = obj.getValue("/Prev");
        if (!str.equals("")) {
            getObjects2(pdf, getObject(pdf, Integer.valueOf(str).intValue()), objects);
        }


        obj.setStream(pdf, obj.getLength(objects));
        if (obj.getValue("/Filter").equals("/FlateDecode")) {
            Decompressor localDecompressor = new Decompressor(obj.stream);
            obj.data = localDecompressor.getDecompressedData();
        } else {
            // Assume no compression.
            obj.data = obj.stream;
        }

        int p1 = 0; // Predictor byte
        int f1 = 0; // Field 1
        int f2 = 0; // Field 2
        int f3 = 0; // Field 3
        for (int n = 0; n < obj.dict.size(); n++) {
            String token = obj.dict.get(n);
            if ((( token).equals("/Predictor")) &&
                    (( obj.dict.get(n + 1)).equals("12"))) {
                p1 = 1;
            }


            if (((String) token).equals("/W")) {
                f1 = Integer.valueOf(obj.dict.get(n + 2)).intValue();
                f2 = Integer.valueOf(obj.dict.get(n + 3)).intValue();
                f3 = Integer.valueOf(obj.dict.get(n + 4)).intValue();
            }
        }

        int n = p1 + f1 + f2 + f3;
        byte[] entry = new byte[n];
        for (int i1 = 0; i1 < obj.data.length; i1 += n) {
            for (int i2 = 0; i2 < n; i2++) {
                int localNum = i2;
                byte[] buf = entry;
                buf[localNum] = ((byte) (buf[localNum] + obj.data[(i1 + i2)]));
            }


            if (entry[p1] == 1) {
                PDFobj o2 = getObject(pdf, toInt((byte[]) entry, p1 + f1, f2));
                o2.number = Integer.valueOf(o2.dict.get(0)).intValue();
                objects.add(o2);
            }
        }
    }


    /**
     * Taken from PDFView, pdfjet has problems with mixed line endings in some PDF files.
     *
     * @param buf
     * @return
     */
    private int getStartXRef(byte[] buf) {

        // Jetzt neuer Code, um startxref zu finden:
        ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
        byteBuffer.rewind();

        // back up about 32 characters from the end of the file to find
        // startxref\n
        byte[] scan = new byte[32];
        int scanPos = byteBuffer.remaining() - scan.length;
        int loc = 0;

        while (scanPos >= 0) {
            byteBuffer.position(scanPos);
            byteBuffer.get(scan);

            // find startxref in scan
            String scans = new String(scan);
            loc = scans.indexOf("startxref");
            if (loc > 0) {
                if (scanPos + loc + scan.length <= byteBuffer.limit()) {
                    scanPos = scanPos + loc;
                    loc = 0;
                }

                break;
            }
            scanPos -= scan.length - 10;
        }
    /*
     * if (scanPos < 0) { throw new IOException("This may not be a PDF File"); }
     */
        byteBuffer.position(scanPos);
        byteBuffer.get(scan);
        String scans = new String(scan);

        loc += 10; // skip over "startxref" and first EOL char
        if (scans.charAt(loc) < 32) {
            loc++;
        } // skip over possible 2nd EOL char
        while (scans.charAt(loc) == 32) {
            loc++;
        } // skip over possible leading blanks
        // read number
        int numstart = loc;
        while (loc < scans.length() && scans.charAt(loc) >= '0' && scans.charAt(loc) <= '9') {
            loc++;
        }
        int xrefpos = Integer.parseInt(scans.substring(numstart, loc));
        return xrefpos;
        // buf.position(xrefpos);

    }


    public int addOutlineDict(Bookmark toc) throws Exception {
        int numOfChildren = getNumOfChildren(0, toc);
        newobj();
        append("<<\n");
        append("/Type /Outlines\n");
        append("/First ");
        append(this.objNumber + 1);
        append(" 0 R\n");
        append("/Last ");
        append(this.objNumber + numOfChildren);
        append(" 0 R\n");
        append("/Count ");
        append(numOfChildren);
        append("\n");
        append(">>\n");
        endobj();
        return this.objNumber;
    }

    public void addOutlineItem(int parent, int value, Bookmark bm1)
            throws Exception {
        int prev = bm1.getPrevBookmark() == null ? 0 : parent + (value - 1);
        int next = bm1.getNextBookmark() == null ? 0 : parent + (value + 1);

        int first = 0;
        int last = 0;
        int count = 0;
        if ((bm1.getChildren() != null) && (bm1.getChildren().size() > 0)) {
            first = parent + bm1.getFirstChild().objNumber;
            last = parent + bm1.getLastChild().objNumber;
            count = -1 * getNumOfChildren(0, bm1);
        }

        newobj();
        append("<<\n");
        append("/Title (");
        append(escapeSpecialCharacters(bm1.getTitle()));
        append(")\n");
        append("/Parent ");
        append(parent);
        append(" 0 R\n");
        if (prev > 0) {
            append("/Prev ");
            append(prev);
            append(" 0 R\n");
        }
        if (next > 0) {
            append("/Next ");
            append(next);
            append(" 0 R\n");
        }
        if (first > 0) {
            append("/First ");
            append(first);
            append(" 0 R\n");
        }
        if (last > 0) {
            append("/Last ");
            append(last);
            append(" 0 R\n");
        }
        if (count != 0) {
            append("/Count ");
            append(count);
            append("\n");
        }
        append("/F 4\n");
        append("/Dest [");
        append(bm1.getDestination().pageObjNumber);
        append(" 0 R /XYZ 0 ");
        append(bm1.getDestination().yPosition);
        append(" 0]\n");
        append(">>\n");
        endobj();
    }

    private int getNumOfChildren(int numOfChildren, Bookmark bm1) {
        List<Bookmark> localList = bm1.getChildren();
        if (localList != null) {
            for (Bookmark localBookmark : localList) {
                numOfChildren = getNumOfChildren(++numOfChildren, localBookmark);
            }
        }
        return numOfChildren;
    }

    public void addObjects(Map<Integer, PDFobj> paramMap) throws Exception {
        for (PDFobj localPDFobj1 : paramMap.values()) {
            if ((localPDFobj1.getValue("/Type").equals("/Pages")) && (localPDFobj1.getValue("/Parent").equals(""))) {
                this.pagesObjNumber = Integer.valueOf((String)localPDFobj1.dict.get(0)).intValue();
            }
        }
        addObjectsToPDF(paramMap);
    }

    public PDFobj getPagesObject(Map<Integer, PDFobj> pagesMap)
            throws Exception {
        for (PDFobj obj : pagesMap.values()) {
            if ((obj.getValue("/Type").equals("/Pages")) && (obj.getValue("/Parent").equals(""))) {
                return obj;
            }
        }
        return null;
    }

    public List<PDFobj> getPageObjects(Map<Integer, PDFobj> pagesMap)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        getPageObjects(getPagesObject(pagesMap), pagesMap, localArrayList);
        return localArrayList;
    }


    private void getPageObjects(PDFobj obj, Map<Integer, PDFobj> map, List<PDFobj> objects)
            throws Exception {
        List<Integer> kids = obj.getObjectNumbers("/Kids");
        for (Integer localInteger : kids) {
            PDFobj localPDFobj = map.get(localInteger);
            if (isPageObject(localPDFobj)) {
                objects.add(localPDFobj);
            } else {
                getPageObjects(localPDFobj, map, objects);
            }
        }
    }

    private boolean isPageObject(PDFobj obj) {
        boolean isPageObject = false;
        for (int i = 0; i < obj.dict.size(); i++) {
            if (((obj.dict.get(i)).equals("/Type")) && (((String) obj.dict.get(i + 1)).equals("/Page"))) {
                isPageObject = true;
            }
        }
        return isPageObject;
    }


    private String getExtGState(PDFobj obj, Map<Integer, PDFobj> objects) {
        StringBuilder localStringBuilder = new StringBuilder();
        List localList = obj.getDict();
        int i = 0;
        for (int j = 0; j < localList.size(); j++) {
            if ((localList.get(j)).equals("/ExtGState")) {
                localStringBuilder.append("/ExtGState << ");
                j++;
                i++;
                while (i > 0) {
                    String str = (String) localList.get(++j);
                    if (str.equals("<<")) {
                        i++;
                    } else if (str.equals(">>")) {
                        i--;
                    }
                    localStringBuilder.append(str);
                    localStringBuilder.append(' ');
                }
            }
        }

        return localStringBuilder.toString().trim();
    }


    private List<PDFobj> getFontObjects(PDFobj obj, Map<Integer, PDFobj> objects) {
        ArrayList list = new ArrayList();
        List<String> dictEntries = obj.getDict();
        for (int i = 0; i < dictEntries.size(); i++) {
            if (((dictEntries.get(i)).equals("/Font")) &&
                    (!(dictEntries.get(i + 2)).equals(">>"))) {
                list.add(objects.get(Integer.valueOf(dictEntries.get(i + 3))));
            }
        }


        if (list.size() == 0) {
            return null;
        }

        int i = 4;
        for (; ; ) {
            if ((dictEntries.get(i)).equals("/Font")) {
                i += 2;
                break;
            }
            i++;
        }
        while (!(dictEntries.get(i)).equals(">>")) {
            this.importedFonts.add(dictEntries.get(i));
            i++;
        }

        return list;
    }


    private List<PDFobj> getDescendantFonts(PDFobj obj, Map<Integer, PDFobj> objects) {
        ArrayList localArrayList = new ArrayList();
        List localList = obj.getDict();
        for (int i = 0; i < localList.size(); i++) {
            if (((localList.get(i)).equals("/DescendantFonts")) &&
                    (!( localList.get(i + 2)).equals("]"))) {
                localArrayList.add(objects.get(Integer.valueOf((String) localList.get(i + 2))));
            }
        }

        return localArrayList;
    }


    private PDFobj getObject(String text, PDFobj object, Map<Integer, PDFobj> objects) {
        List list = object.getDict();
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i)).equals(text)) {
                return objects.get(Integer.valueOf((String) list.get(i + 1)));
            }
        }
        return null;
    }

    public void addResourceObjects(Map<Integer, PDFobj> objects) throws Exception {
        TreeMap treeMap = new TreeMap();

        List<PDFobj> localList1 = getPageObjects(objects);
        for (PDFobj pdfObj : localList1) {
            PDFobj pdfObj2 = pdfObj.getResourcesObject(objects);
            List<PDFobj> pdfObjects = getFontObjects(pdfObj2, objects);
            if (pdfObjects != null) {
                for (PDFobj localPDFobj3 : pdfObjects) {
                    treeMap.put(Integer.valueOf(localPDFobj3.getNumber()), localPDFobj3);
                    pdfObj2 = getObject("/ToUnicode", localPDFobj3, objects);
                    treeMap.put(Integer.valueOf(pdfObj2.getNumber()), pdfObj2);
                    List<PDFobj> localList3 = getDescendantFonts(localPDFobj3, objects);
                    for (PDFobj localPDFobj4 : localList3) {
                        treeMap.put(Integer.valueOf(localPDFobj4.getNumber()), localPDFobj4);
                        pdfObj2 = getObject("/FontDescriptor", localPDFobj4, objects);
                        treeMap.put(Integer.valueOf(pdfObj2.getNumber()), pdfObj2);
                        pdfObj2 = getObject("/FontFile2", pdfObj2, objects);
                        treeMap.put(Integer.valueOf(pdfObj2.getNumber()), pdfObj2);
                    }
                }
            }
            this.extGState = getExtGState(pdfObj2, objects);
        }

        if (treeMap.size() > 0) {
            addObjectsToPDF(treeMap);
        }
    }

    private void addObjectsToPDF(Map<Integer, PDFobj> objects)
            throws Exception {
        int i = (Collections.max(objects.keySet())).intValue();
        PDFobj object;
        for (int j = 1; j < i; j++) {
            if (objects.get(Integer.valueOf(j)) == null)
            {
                object = new PDFobj();
                object.number = j;
                objects.put(Integer.valueOf(object.number), object);
            }
        }
        for (Iterator it = objects.values().iterator(); it.hasNext();)
        {
            object = (PDFobj)it.next();
            this.objNumber = object.number;
            this.objOffset.add(Integer.valueOf(this.byte_count));
            int k;
            if (object.offset == 0)
            {
                append(object.number);
                append(" 0 obj\n");
                if (object.dict != null) {
                    for (k = 0; k < object.dict.size(); k++)
                    {
                        append(object.dict.get(k));
                        append(' ');
                    }
                }
                if (object.stream != null)
                {
                    append("<< /Length ");
                    append(object.stream.length);
                    append(" >>");
                    append("\nstream\n");
                    for (k = 0; k < object.stream.length; k++) {
                        append(object.stream[k]);
                    }
                    append("\nendstream\n");
                }
                append("endobj\n");
            }
            else
            {
                k = object.dict.size();
                String str = null;
                for (int m = 0; m < k; m++)
                {
                    str = object.dict.get(m);
                    append(str);
                    if (m < k - 1) {
                        append(' ');
                    } else {
                        append('\n');
                    }
                }
                if (object.stream != null)
                {
                    for (int m = 0; m < object.stream.length; m++) {
                        append(object.stream[m]);
                    }
                    append("\nendstream\n");
                }
                if (!str.equals("endobj")) {
                    append("endobj\n");
                }
            }
        }
    }
}
