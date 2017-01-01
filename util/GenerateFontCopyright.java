/**
 * GenerateFontCopyright.java
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

import java.io.*;


public class GenerateFontCopyright {

    public GenerateFontCopyright(String fileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            if (fileName.endsWith(".java")) {
                bw.write("package com.pdfjet;\n\n");
            }
            else {
                bw.write("using System;\n\n");
                bw.write("namespace PDFjet.NET {\n\n");
            }
            bw.write("class AndroidFontsCopyright {\n\n");
            if (fileName.endsWith(".java")) {
                bw.write("    protected static final String NOTICE = \n\"");
            }
            else {
                bw.write("    internal const String NOTICE = \n\"");
            }
            bw.write("\\n");
            bw.write("-----------------------------------------------------------------------------\\n");
            bw.write("The following PDF object stream contains font from The Android Open Source\\n");
            bw.write("Project:\\n");
            bw.write("\\n");
            bw.write("    http://developer.android.com/index.html\\n");
            bw.write("\\n");
            bw.write("The font was compressed using the RFC 1951 DEFLATE algorithm and it is\\n");
            bw.write("still licensed under its original Apache License, Version 2.0:\\n");
            bw.write("-----------------------------------------------------------------------------\\n");

            FileInputStream fis =
                    new FileInputStream("fonts/Android/NOTICE");
            int ch;
            while ((ch = fis.read()) != -1) {
                if (ch == '\n') {
                    bw.write('\\');
                    bw.write('n');
                }
                else if (ch == '"') {
                    bw.write('\\');
                    bw.write('"');
                }
                else {
                    bw.write(ch);
                }
            }
            fis.close();

            bw.write("-----------------------------------------------------------------------------\\n");
            bw.write("\";");
            bw.write("\n}\n");

            if (fileName.endsWith(".cs")) {
                bw.write("}\n");
            }

            bw.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void main(String[] args) {
        new GenerateFontCopyright("com/pdfjet/AndroidFontsCopyright.java");
        new GenerateFontCopyright("net/pdfjet/AndroidFontsCopyright.cs");
    }
}
