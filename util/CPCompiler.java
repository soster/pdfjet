/**
 *  CPCompiler.java
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

package util;

import java.io.*;
import java.util.*;


/**
 * CPCompiler.java
 * Creates the code page .java and .cs files
 */
public class CPCompiler {

    public CPCompiler(String fileName, String lang) {

        try {
            BufferedReader in = new BufferedReader(
                    new FileReader("code-pages/" + fileName));
            String className =
                    fileName.substring(0, fileName.lastIndexOf('.'));
            BufferedWriter out = null;
            if (lang.equals("Java")) {
                out = new BufferedWriter(new FileWriter(
                        "com/pdfjet/CP" + className + ".java"));
                out.write("package com.pdfjet;\n\n");
                out.write("import java.lang.*;\n\n");
            }
            else {
                out = new BufferedWriter(new FileWriter(
                        "net/pdfjet/CP" + className + ".cs"));
                out.write("using System;\n\n");
                out.write("namespace PDFjet.NET {\n");
            }
            out.write("class CP" + className + " {\n");

            if (lang.equals("Java")) {
                out.write("public static final int[] codes = {\n");
            }
            else {
                out.write("public static readonly int[] codes = {\n");
            }
            out.write("0x0020,\n");
            List<String> list = new ArrayList<String>();
            List<String> unicodes = new ArrayList<String>();
            int count = 0x80;
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("#")) continue;
                list.clear();
                StringTokenizer st = new StringTokenizer(line, " =:");
                while (st.hasMoreTokens()) list.add(st.nextToken());
                int code = Integer.parseInt(list.get(0), 16);
                if (code < 0x80) continue;
                while (count < code) {
                    unicodes.add("0020");
                    out.write("0x0020,\n");
                    count++;
                }
                String unicode = (list.get(1)).substring(2);
                unicodes.add(unicode);
                out.write("0x" + unicode + ",\n");
                count++;
            }
            out.write("};\n");

            in.close();
            in = new BufferedReader(
                    new FileReader("code-pages/aglfn.txt"));
            Map<String, String> map = new HashMap<String, String>();
            while ((line = in.readLine()) != null) {
                if (line.startsWith("#")) continue;
                list.clear();
                StringTokenizer st = new StringTokenizer(line, ";");
                while (st.hasMoreTokens()) list.add(st.nextToken());
                map.put(list.get(0), list.get(1));
            }

            out.write("public static String[] names = {\n");
            out.write("\"/space\",\n");
            for (int i = 0; i < unicodes.size(); i++) {
                if (unicodes.get(i) == null) continue;
                out.write("\"/");
                if (unicodes.get(i).equals("00A0")) {
                    out.write("space");
                } else if (unicodes.get(i).equals("00AD")) {
                    out.write("hyphen");
                } else if (unicodes.get(i).equals("00B2")) {
                    out.write("twosuperior");
                } else if (unicodes.get(i).equals("00B3")) {
                    out.write("threesuperior");
                } else if (unicodes.get(i).equals("00B9")) {
                    out.write("onesuperior");
                } else if (unicodes.get(i).equals("00B5")) {
                    out.write("mu");
                } else if (unicodes.get(i).equals("2015")) {
                    out.write("endash");
                } else {
                    out.write(map.get(unicodes.get(i)));
                }
                out.write("\",\n");
            }
            out.write("};\n");
            out.write("}\n");

            if (lang.equals("C#")) {
                out.write("}\n");
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    public static void main(String[] args) {
        new CPCompiler("1250.txt", "Java"); // Central Europe
        new CPCompiler("1251.txt", "Java"); // Cyrillic
        new CPCompiler("1252.txt", "Java"); // Latin I
        new CPCompiler("1253.txt", "Java"); // Greek
        new CPCompiler("1254.txt", "Java"); // Turkish
        new CPCompiler("1257.txt", "Java"); // Baltic

        new CPCompiler("1250.txt", "C#");   // Central Europe
        new CPCompiler("1251.txt", "C#");   // Cyrillic
        new CPCompiler("1252.txt", "C#");   // Latin I
        new CPCompiler("1253.txt", "C#");   // Greek
        new CPCompiler("1254.txt", "C#");   // Turkish
        new CPCompiler("1257.txt", "C#");   // Baltic
    }

}   // End of CPCompiler.java
