/**
 *  EmbeddedFile.java
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;


/**
 *  Used to embed file objects.
 *  The file objects must added to the PDF before drawing on the first page.
 *
 */
public class EmbeddedFile {

    protected int objNumber = -1;
    protected String fileName;


    public EmbeddedFile(PDF pdf, String fileName, InputStream stream, boolean compress) throws Exception {
        this.fileName = fileName;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int number;
        while ((number = stream.read(buf, 0, buf.length)) > 0) {
            baos.write(buf, 0, number);
        }
        stream.close();

        if (compress) {
            buf = baos.toByteArray();
            baos = new ByteArrayOutputStream();
            DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater());
            dos.write(buf, 0, buf.length);
            dos.finish();
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /EmbeddedFile\n");
        if (compress) {
            pdf.append("/Filter /FlateDecode\n");
        }
        pdf.append("/Length ");
        pdf.append(baos.size());
        pdf.append("\n");
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(baos);
        pdf.append("\nendstream\n");
        pdf.endobj();

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Filespec\n");
        pdf.append("/F (");
        pdf.append(fileName);
        pdf.append(")\n");
        pdf.append("/EF <</F ");
        pdf.append(pdf.getObjNumber() - 1);
        pdf.append(" 0 R>>\n");
        pdf.append(">>\n");
        pdf.endobj();

        this.objNumber = pdf.getObjNumber();
    }


    public String getFileName() {
        return fileName;
    }

}   // End of EmbeddedFile.java
