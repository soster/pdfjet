/**
 *  EmbeddedFile.java
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 *  Used to embed file objects.
 *  The file objects must added to the PDF before drawing on the first page.
 */
public class EmbeddedFile {
    protected int objNumber = -1;
    protected String fileName;

    public EmbeddedFile(PDF pdf, String fileName, boolean compress) throws Exception {
        this(pdf, fileName.substring(fileName.lastIndexOf("/") + 1),
                new BufferedInputStream(new FileInputStream(fileName)), compress);
    }

    public EmbeddedFile(PDF pdf, String fileName, InputStream stream, boolean compress) throws Exception {
        this.fileName = fileName;
        byte[] buf = Contents.getFromStream(stream);

        if (compress) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater());
            dos.write(buf, 0, buf.length);
            dos.finish();
            buf = baos.toByteArray();
        }

        pdf.newobj();
        pdf.append(Token.beginDictionary);
        pdf.append("/Type /EmbeddedFile\n");
        if (compress) {
            pdf.append("/Filter /FlateDecode\n");
        }
        pdf.append(Token.length);
        pdf.append(buf.length);
        pdf.append(Token.newline);
        pdf.append(Token.endDictionary);
        pdf.append(Token.stream);
        pdf.append(buf);
        pdf.append(Token.endstream);
        pdf.endobj();

        pdf.newobj();
        pdf.append(Token.beginDictionary);
        pdf.append("/Type /Filespec\n");
        pdf.append("/F (");
        pdf.append(fileName);
        pdf.append(")\n");
        pdf.append("/EF <</F ");
        pdf.append(pdf.getObjNumber() - 1);
        pdf.append(" 0 R>>\n");
        pdf.append(Token.endDictionary);
        pdf.endobj();

        this.objNumber = pdf.getObjNumber();
    }

    public String getFileName() {
        return fileName;
    }
}   // End of EmbeddedFile.java
