/**
 *  PDF.java
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Used to create PDF objects that represent PDF documents.
 * 
 * 
 */
public class PDF {

  protected int objNumber = 0;
  protected int metadataObjNumber = 0;
  protected int outputIntentObjNumber = 0;
  protected List<Font> fonts = new ArrayList<Font>();
  protected List<Image> images = new ArrayList<Image>();
  protected List<Page> pages = new ArrayList<Page>();
  protected List<Integer> alphaGroups = new ArrayList<Integer>();
  protected HashMap<String, Destination> destinations = new HashMap<String, Destination>();
  protected List<OptionalContentGroup> groups = new ArrayList<OptionalContentGroup>();
  protected static final DecimalFormat df = new DecimalFormat("0.###", new DecimalFormatSymbols(Locale.US));

  private static final int CR_LF = 0;
  private static final int CR = 1;
  private static final int LF = 2;

  private int compliance = 0;
  private OutputStream os = null;
  private List<Integer> objOffset = new ArrayList<Integer>();
  private String producer = "ScribMaster / HandWrite Engine";
  private String creationDate;
  private String createDate;
  private String title = "";
  private String subject = "";
  private String author = "";
  private int byte_count = 0;
  private int endOfLine = CR_LF;
  private int resObjNumber = -1;
  private int pagesObjNumber = -1;

  /**
   * The default constructor - use when reading PDF files.
   * 
   * @throws Exception
   */
  public PDF() throws Exception {
  }

  /**
   * Creates a PDF object that represents a PDF document.
   * 
   * @param os
   *          the associated output stream.
   */
  public PDF(OutputStream os) throws Exception {
    this(os, 0);
  }

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
  // Pages
  // Page1
  // Page2
  // ...
  // PageN
  // Info
  // Root
  // xref table
  // Trailer
  /**
   * Creates a PDF object that represents a PDF document. Use this constructor
   * to create PDF/A compliant PDF documents. Please note: PDF/A compliance
   * requires all fonts to be embedded in the PDF.
   * 
   * @param os
   *          the associated output stream.
   * @param compliance
   *          must be: Compliance.PDF_A_1B
   */
  public PDF(OutputStream os, int compliance) throws Exception {

    this.os = os;
    this.compliance = compliance;

    Date date = new Date();
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    creationDate = sdf1.format(date);
    createDate = sdf2.format(date);

    append("%PDF-1.4\n");
    append('%');
    append((byte) 0x00F2);
    append((byte) 0x00F3);
    append((byte) 0x00F4);
    append((byte) 0x00F5);
    append((byte) 0x00F6);
    append('\n');

    if (compliance == Compliance.PDF_A_1B) {
      metadataObjNumber = addMetadataObject("", true);
      outputIntentObjNumber = addOutputIntentObject();
    }

  }

  protected void addAlphaGroup(int ag) {
    for (int i : alphaGroups) {
      if (i == ag) {
        return;
      }
    }
    alphaGroups.add(ag);
  }

  public String addAndGetAlphaGroup(float alpha) {
    int value = (int) (alpha * 10f);
    if (value > 9) {
      return null;
    }

    if (value == 0) {
      value = 1;
    }
    addAlphaGroup(value);
    return "GS" + value;
  }

  protected void newobj() throws IOException {
    objOffset.add(byte_count);
    append(++objNumber);
    append(" 0 obj\n");
  }

  protected void endobj() throws IOException {
    append("endobj\n");
  }

  protected int addMetadataObject(String notice, boolean padding) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("<?xpacket begin='\uFEFF' id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n");
    sb.append("<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n");
    sb.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n");

    sb.append("<rdf:Description rdf:about=\"\" xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\" pdf:Producer=\"");
    sb.append(producer);
    sb.append("\"></rdf:Description>\n");

    sb.append("<rdf:Description rdf:about=\"\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n");
    sb.append("<dc:format>application/pdf</dc:format>\n");
    sb.append("<dc:title><rdf:Alt><rdf:li xml:lang=\"x-default\">");
    sb.append(title);
    sb.append("</rdf:li></rdf:Alt></dc:title>\n");

    sb.append("<dc:creator><rdf:Seq><rdf:li>");
    sb.append(author);
    sb.append("</rdf:li></rdf:Seq></dc:creator>\n");

    sb.append("<dc:description><rdf:Alt><rdf:li xml:lang=\"en-US\">");
    sb.append(notice);
    sb.append("</rdf:li></rdf:Alt></dc:description>\n");

    sb.append("</rdf:Description>\n");

    sb.append("<rdf:Description rdf:about=\"\" xmlns:pdfaid=\"http://www.aiim.org/pdfa/ns/id/\">");
    sb.append("<pdfaid:part>1</pdfaid:part>");
    sb.append("<pdfaid:conformance>B</pdfaid:conformance>");
    sb.append("</rdf:Description>");

    sb.append("<rdf:Description rdf:about=\"\" xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\">\n");
    sb.append("<xmp:CreateDate>");
    sb.append(createDate);
    sb.append("</xmp:CreateDate>\n");
    sb.append("</rdf:Description>\n");
    sb.append("</rdf:RDF>\n");
    sb.append("</x:xmpmeta>\n");

    if (padding) {
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

    // This is the metadata object
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

    return objNumber;
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

    // OutputIntent object
    newobj();
    append("<<\n");
    append("/Type /OutputIntent\n");
    append("/S /GTS_PDFA1\n");
    append("/OutputCondition (sRGB IEC61966-2.1)\n");
    append("/OutputConditionIdentifier (sRGB IEC61966-2.1)\n");
    append("/Info (sRGB IEC61966-2.1)\n");
    append("/DestOutputProfile ");
    append(objNumber - 1);
    append(" 0 R\n");
    append(">>\n");
    endobj();

    return objNumber;
  }

  private int addResourcesObject() throws Exception {
    newobj();
    append("<<\n");

    if (!fonts.isEmpty()) {
      append("/Font\n");
      append("<<\n");
      for (int i = 0; i < fonts.size(); i++) {
        Font font = fonts.get(i);
        append("/F");
        append(font.objNumber);
        append(' ');
        append(font.objNumber);
        append(" 0 R\n");
      }
      append(">>\n");
    }

    if (!images.isEmpty()) {
      append("/XObject\n");
      append("<<\n");
      for (int i = 0; i < images.size(); i++) {
        Image image = images.get(i);
        append("/Im");
        append(image.objNumber);
        append(' ');
        append(image.objNumber);
        append(" 0 R\n");
      }
      append(">>\n");
    }

    if (!groups.isEmpty()) {
      append("/Properties\n");
      append("<<\n");
      for (int i = 0; i < groups.size(); i++) {
        OptionalContentGroup ocg = groups.get(i);
        append("/OC");
        append(i + 1);
        append(' ');
        append(ocg.objNumber);
        append(" 0 R\n");
      }
      append(">>\n");
    }

    // hart coded transparency groups
    // http://www.p2501.ch/pdf-howto/grafik/transparenz
    if (alphaGroups.size() > 0) {
      append("/ExtGState <<\n");
      for (int i : alphaGroups) {
        append("/GS");
        append(i);
        append(" << /CA 0.");
        append(i);
        append(" /ca 0.");
        append(i);
        append(" >>\n");
      }
      append(">>\n");
    }
    append(">>\n");
    endobj();
    return objNumber;
  }

  protected int addPagesObject() throws Exception {
    newobj();
    append("<<\n");
    append("/Type /Pages\n");
    append("/Kids [ ");
    int pageObjNumber = objNumber + 1;
    for (int i = 0; i < pages.size(); i++) {
      Page page = pages.get(i);
      page.setDestinationsPageObjNumber(pageObjNumber);
      append(pageObjNumber);
      append(" 0 R ");
      pageObjNumber += (page.annots.size() + 1);
    }
    append("]\n");
    append("/Count ");
    append(pages.size());
    append('\n');
    append(">>\n");
    endobj();
    return objNumber;
  }

  private int addInfoObject() throws Exception {
    // Add the info object
    newobj();
    append("<<\n");
    append("/Title (");
    append(title);
    append(")\n");
    append("/Subject (");
    append(subject);
    append(")\n");
    append("/Author (");
    append(author);
    append(")\n");
    append("/Producer (");
    append(producer);
    append(")\n");

    if (compliance != Compliance.PDF_A_1B) {
      append("/CreationDate (D:");
      append(creationDate);
      append(")\n");
    }

    append(">>\n");
    endobj();
    return objNumber;
  }

  private int addRootObject() throws Exception {
    // Add the root object
    newobj();
    append("<<\n");
    append("/Type /Catalog\n");

    addOCProperties();

    append("/Pages ");
    append(pagesObjNumber);
    append(" 0 R\n");

    if (compliance == Compliance.PDF_A_1B) {
      append("/Metadata ");
      append(metadataObjNumber);
      append(" 0 R\n");

      append("/OutputIntents [");
      append(outputIntentObjNumber);
      append(" 0 R]\n");
    }

    append(">>\n");
    endobj();
    return objNumber;
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

  private void addAllPages(int pagesObjNumber, int resObjNumber) throws Exception {
    for (int i = 0; i < pages.size(); i++) {
      Page page = pages.get(i);

      // Page object
      newobj();
      append("<<\n");
      append("/Type /Page\n");
      append("/Parent ");
      append(pagesObjNumber);
      append(" 0 R\n");
      append("/MediaBox [0.0 0.0 ");
      append(page.width);
      append(' ');
      append(page.height);
      append("]\n");

      if (page.cropBox != null) {
        addPageBox("CropBox", page, page.cropBox);
      }
      if (page.bleedBox != null) {
        addPageBox("BleedBox", page, page.bleedBox);
      }
      if (page.trimBox != null) {
        addPageBox("TrimBox", page, page.trimBox);
      }
      if (page.artBox != null) {
        addPageBox("ArtBox", page, page.artBox);
      }

      append("/Resources ");
      append(resObjNumber);
      append(" 0 R\n");

      append("/Contents [ ");
      for (Integer n : page.contents) {
        append(n);
        append(" 0 R ");
      }
      append("]\n");
      if (page.annots.size() > 0) {
        append("/Annots [ ");
        for (int j = 0; j < page.annots.size(); j++) {
          append(objNumber + j + 1);
          append(" 0 R ");
        }
        append("]\n");
      }
      append(">>\n");
      endobj();

      addAnnotDictionaries(page);
    }
  }

  private void addPageContent(Page page) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater());
    byte[] buf = page.buf.toByteArray();
    dos.write(buf, 0, buf.length);
    dos.finish();
//    page.buf = null; // Release the page content memory!

    newobj();
    append("<<\n");
    append("/Filter /FlateDecode\n");
    append("/Length ");
    append(baos.size());
    append("\n");
    append(">>\n");
    append("stream\n");
    append(baos);
    append("\nendstream\n");
    endobj();
    page.contents.add(objNumber);
  }

  protected void addAnnotDictionaries(Page page) throws Exception {
    for (int i = 0; i < page.annots.size(); i++) {
      Annotation annot = page.annots.get(i);
      newobj();
      append("<<\n");
      append("/Type /Annot\n");
      append("/Subtype /Link\n");
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
        Destination destination = destinations.get(annot.key);
        if (destination != null) {
          append("/Dest [");
          append(destination.pageObjNumber);
          append(" 0 R /XYZ 0 ");
          append(destination.yPosition);
          append(" 0]\n");
        }
      }
      append(">>\n");
      endobj();
    }
  }

  private void addOCProperties() throws Exception {
    if (!groups.isEmpty()) {
      append("/OCProperties\n");
      append("<<\n");
      append("/OCGs [");
      for (OptionalContentGroup ocg : this.groups) {
        append(' ');
        append(ocg.objNumber);
        append(" 0 R");
      }
      append(" ]\n");
      append("/D <<\n");
      append("/BaseState /OFF\n");
      append("/ON [");
      for (OptionalContentGroup ocg : this.groups) {
        if (ocg.visible) {
          append(' ');
          append(ocg.objNumber);
          append(" 0 R");
        }
      }
      append(" ]\n");

      append("/AS [\n");
      append("<< /Event /Print /Category [/Print] /OCGs [");
      for (OptionalContentGroup ocg : this.groups) {
        if (ocg.printable) {
          append(' ');
          append(ocg.objNumber);
          append(" 0 R");
        }
      }
      append(" ] >>\n");
      append("<< /Event /Export /Category [/Export] /OCGs [");
      for (OptionalContentGroup ocg : this.groups) {
        if (ocg.exportable) {
          append(' ');
          append(ocg.objNumber);
          append(" 0 R");
        }
      }
      append(" ] >>\n");
      append("]\n");

      append("/Order [[ ()");
      for (OptionalContentGroup ocg : this.groups) {
        append(' ');
        append(ocg.objNumber);
        append(" 0 R");
      }
      append(" ]]\n");
      append(">>\n");
      append(">>\n");
    }
  }

  public void addPage(Page page) throws Exception {
    int n = pages.size();
    if (n > 0) {
      addPageContent(pages.get(n - 1));
    }
    pages.add(page);
  }

  /**
   * Writes the PDF object to the output stream. Does not close the underlying
   * output stream.
   */
  public void flush() throws Exception {
    flush(false);
  }

  /**
   * Writes the PDF object to the output stream and closes it.
   */
  public void close() throws Exception {
    flush(true);
  }

  private void flush(boolean close) throws Exception {
    if (pagesObjNumber == -1) {
      addPageContent(pages.get(pages.size() - 1));
      resObjNumber = addResourcesObject();
      pagesObjNumber = addPagesObject();
      addAllPages(pagesObjNumber, resObjNumber);
    }

    int infoObjNumber = addInfoObject();
    int rootObjNumber = addRootObject();

    int startxref = byte_count;

    // Create the xref table
    append("xref\n");
    append("0 ");
    append(rootObjNumber + 1);
    append('\n');

    append("0000000000 65535 f \n");
    for (int i = 0; i < objOffset.size(); i++) {
      int offset = objOffset.get(i);
      String str = Integer.toString(offset);
      for (int j = 0; j < 10 - str.length(); j++) {
        append('0');
      }
      append(str);
      append(" 00000 n \n");
    }
    append("trailer\n");
    append("<<\n");
    append("/Size ");
    append(rootObjNumber + 1);
    append('\n');

    String id = (new Salsa20()).getID();
    append("/ID[<");
    append(id);
    append("><");
    append(id);
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

    os.flush();
    if (close) {
      os.close();
    }
  }

  /**
   * Set the "Title" document property of the PDF file.
   * 
   * @param title
   *          The title of this document.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Set the "Subject" document property of the PDF file.
   * 
   * @param subject
   *          The subject of this document.
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * Set the "Author" document property of the PDF file.
   * 
   * @param author
   *          The author of this document.
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  protected void append(int num) throws IOException {
    append(Integer.toString(num));
  }

  protected void append(float val) throws IOException {
    append(PDF.df.format(val));
  }

  protected void append(String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
      os.write((byte) str.charAt(i));
    }
    byte_count += len;
  }

  protected void append(char ch) throws IOException {
    append((byte) ch);
  }

  protected void append(byte b) throws IOException {
    os.write(b);
    byte_count += 1;
  }

  protected void append(byte[] buf, int off, int len) throws IOException {
    os.write(buf, off, len);
    byte_count += len;
  }

  protected void append(ByteArrayOutputStream baos) throws IOException {
    baos.writeTo(os);
    byte_count += baos.size();
  }

} // End of PDF.java
