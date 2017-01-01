/**
 *  GenerateICCProfile.java
 *
Copyright (c) 2013, Innovatics Inc.
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

package util;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;


/**
 *  This program will convert ICC profile binary data file to class that has byte array containing the ICC data.
 *
 */
public class GenerateICCProfile {

    public static void main(String[] args) throws Exception {
        BufferedOutputStream bos1 =
                new BufferedOutputStream(
                        new FileOutputStream("com/pdfjet/ICCBlackScaled.java"));
        BufferedOutputStream bos2 =
                new BufferedOutputStream(
                        new FileOutputStream("net/pdfjet/ICCBlackScaled.cs"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(
                        "icc-profiles/sRGB_IEC61966-2-1_black_scaled.icc"));
        int ch;
        while ((ch = bis.read()) != -1) {
            baos.write(ch);
        }
        bis.close();
        byte[] sRGB = baos.toByteArray();

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        DeflaterOutputStream dos =
                new DeflaterOutputStream(baos2, new Deflater());
        dos.write(sRGB, 0, sRGB.length);
        dos.finish();

        byte[] buf = baos2.toByteArray();
        StringBuilder sb = new StringBuilder();

        sb.append("package com.pdfjet;\n\n\n");
        sb.append("public class ICCBlackScaled {\n");
        sb.append("    public static byte[] profile = {\n");
        for (int i = 0; i < buf.length; i++) {
            sb.append("(byte) 0x" + String.format("%02X", buf[i]) + ",\n");
        }
        sb.append("    };\n");
        sb.append("}\n");

        byte[] buf1 = sb.toString().getBytes();
        bos1.write(buf1, 0, buf1.length);
        bos1.flush();
        bos1.close();

        sb.setLength(0);
        sb.append("using System;\n\n\n");
        sb.append("namespace PDFjet.NET {\n");
        sb.append("public class ICCBlackScaled {\n");
        sb.append("    public static byte[] profile = {\n");
        for (int i = 0; i < buf.length; i++) {
            sb.append("(byte) 0x" + String.format("%02X", buf[i]) + ",\n");
        }
        sb.append("    };\n");
        sb.append("}\n");
        sb.append("}   // End of namespace PDFjet.NET");

        byte[] buf2 = sb.toString().getBytes();
        bos2.write(buf2, 0, buf2.length);
        bos2.flush();
        bos2.close();
    }

}   // End of GenerateICCProfile.java
