/**
 *  AfmCompiler.java
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
import java.util.*;


/**
 *  AfmCompiler.java
 *  Extracts information from .afm file and creates
 *  .java and .cs files containing the font metrics.
 */
public class AfmCompiler {

    public AfmCompiler(String path, String fileName, String lang) {

        boolean languageIsJava = false;
        if (lang.equals("Java")) {
            languageIsJava = true;
        }

        StringBuffer[] data = new StringBuffer[256];
        for (int i = 32; i < 256; i++) {
            data[i] = new StringBuffer();
        }

        Map<String, String> map = new HashMap<String, String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path + fileName));
            String fontName = fileName.substring(0, fileName.lastIndexOf('.'));
            String className = fontName.replace('-', '_');
            BufferedWriter out = null;
            if (languageIsJava) {
                out = new BufferedWriter(new FileWriter("com/pdfjet/" + className + ".java"));
                out.write("package com.pdfjet;\n\n");
                // out.write("class " + className + " extends CoreFont {\n");
                out.write("class " + className + " {\n");
            }
            else {
                out = new BufferedWriter(new FileWriter("net/pdfjet/" + className + ".cs"));
                out.write("using System;\n\n");
                out.write("namespace PDFjet.NET {\n");
                // out.write("class " + className + " : CoreFont {\n");
                out.write("class " + className + " {\n");
            }

            if (languageIsJava) {
                out.write("    protected static final String name = \"" + fontName + "\";\n");
            }
            else {
                out.write("    internal static readonly String name = \"" + fontName + "\";\n");
            }
            List<String> list = new ArrayList<String>();
            String line = null;
            while ((line = in.readLine()) != null) {
                list.clear();
                StringTokenizer st = new StringTokenizer(line, " ;");
                while (st.hasMoreTokens()) list.add(st.nextToken());

                int index = 0;
                if (list.get(0).equals("FontBBox")) {
                    // FontBBox -166 -225 1000 931
                    if (languageIsJava) {
                        out.write("    protected static final int bBoxLLx = " + list.get(1) + ";\n");
                        out.write("    protected static final int bBoxLLy = " + list.get(2) + ";\n");
                        out.write("    protected static final int bBoxURx = " + list.get(3) + ";\n");
                        out.write("    protected static final int bBoxURy = " + list.get(4) + ";\n");
                    }
                    else {
                        out.write("    internal static readonly int bBoxLLx = " + list.get(1) + ";\n");
                        out.write("    internal static readonly int bBoxLLy = " + list.get(2) + ";\n");
                        out.write("    internal static readonly int bBoxURx = " + list.get(3) + ";\n");
                        out.write("    internal static readonly int bBoxURy = " + list.get(4) + ";\n");
                    }
                }
                else if (list.get(0).equals("UnderlinePosition")) {
                    if (languageIsJava) {
                        out.write("    protected static final int underlinePosition = " + list.get(1) + ";\n");
                    }
                    else {
                        out.write("    internal static readonly int underlinePosition = " + list.get(1) + ";\n");
                    }
                }
                else if (list.get(0).equals("UnderlineThickness")) {
                    if (languageIsJava) {
                        out.write("    protected static final int underlineThickness = " + list.get(1) + ";\n");
                    }
                    else {
                        out.write("    internal static readonly int underlineThickness = " + list.get(1) + ";\n");
                    }
                }
                else if (list.get(0).equals("Notice")) {
                    if (languageIsJava) {
                        out.write("    protected static final String notice = \"");
                    }
                    else {
                        out.write("    internal static readonly string notice = \"");
                    }
                    out.write(line.substring(7));
                    out.write("\";\n");
                }
                else if (list.get(0).equals("StartCharMetrics")) {
                    if (languageIsJava) {
                        out.write("    protected static final int[][] metrics = {\n");
                    }
                    else {
                        out.write("    internal static readonly int[][] metrics = {\n");
                    }
                }
                else if (list.get(0).equals("C")) {
                    // C 95 ; WX 556 ; N underscore ; B 0 -125 556 -75 ;
                    String name = list.get(5);

                    boolean found = false;
                    for (int i = 32; i < 256; i++) {
                        if (name.equals(Glyph.names[i - 32])) {
                            index = i;
                            found = true;
                            break;
                        }
                    }
                    if (!found) continue;

                    map.put(name, String.valueOf(index));
                    if (languageIsJava) {
                        data[index].append("        {");
                    }
                    else {
                        data[index].append("        new int[] {");
                    }
                    data[index].append(String.valueOf(index));
                    data[index].append(',');
                    data[index].append(list.get(3));
                    data[index].append(',');
                }
                else if (list.get(0).equals("KPX")) {
                    // KPX o v -15
                    if (map.containsKey(list.get(1)) &&
                            map.containsKey(list.get(2))) {
                        index = Integer.parseInt(map.get(list.get(1)));
                        data[index].append(map.get(list.get(2)));
                        data[index].append(',');
                        data[index].append(list.get(3));
                        data[index].append(',');
                    }
                }
            }

            for (int i = 32; i < data.length; i++) {
                if (data[i].length() == 0) {
                    if (languageIsJava) {
                        out.write("        {");
                    }
                    else {
                        out.write("        new int[] {");
                    }
                    out.write(String.valueOf(i));
                    String bullet = data[183].toString();
                    out.write(bullet.substring(bullet.indexOf("183") + 3));
                }
                else {
                    out.write(data[i].toString());
                }
                out.write("},\n");
            }
            out.write("    };\n");
            out.write("}\n");

            if (!languageIsJava) {
                out.write("}   // End of namespace PDFjet.NET\n");
            }

            in.close();
            out.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    // Special compiler for the Symbol.afm and ZapfDingbats.afm
    public AfmCompiler(String path, String fileName, String lang, String symbol) {

        boolean languageIsJava = false;
        if (lang.equals("Java")) {
            languageIsJava = true;
        }

        StringBuffer[] data = new StringBuffer[256];
        for (int i = 32; i < 256; i++) {
            data[i] = new StringBuffer();
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(path + fileName));
            String fontName = fileName.substring(0, fileName.lastIndexOf('.'));

            BufferedWriter out = null;
            if (languageIsJava) {
                out = new BufferedWriter(new FileWriter("com/pdfjet/" + fontName + ".java"));
                out.write("package com.pdfjet;\n\n");
                // out.write("class " + fontName + " extends CoreFont {\n");
                out.write("class " + fontName + " {\n");
            }
            else {
                out = new BufferedWriter(new FileWriter("net/pdfjet/" + fontName + ".cs"));
                out.write("using System;\n\n");
                out.write("namespace PDFjet.NET {\n");
                // out.write("class " + fontName + " : CoreFont {\n");
                out.write("class " + fontName + " {\n");
            }

            if (languageIsJava) {
                out.write("    protected static final String name = \"" + fontName + "\";\n");
            }
            else {
                out.write("    internal static readonly String name = \"" + fontName + "\";\n");
            }
            List<String> list = new ArrayList<String>();
            String line = null;
            while ((line = in.readLine()) != null) {
                list.clear();
                StringTokenizer st = new StringTokenizer(line, " ;");
                while (st.hasMoreTokens()) list.add(st.nextToken());

                if (list.get(0).equals("FontBBox")) {
                    // FontBBox -166 -225 1000 931
                    if (languageIsJava) {
                        out.write("    protected static final int bBoxLLx = " + list.get(1) + ";\n");
                        out.write("    protected static final int bBoxLLy = " + list.get(2) + ";\n");
                        out.write("    protected static final int bBoxURx = " + list.get(3) + ";\n");
                        out.write("    protected static final int bBoxURy = " + list.get(4) + ";\n");
                    }
                    else {
                        out.write("    internal static readonly int bBoxLLx = " + list.get(1) + ";\n");
                        out.write("    internal static readonly int bBoxLLy = " + list.get(2) + ";\n");
                        out.write("    internal static readonly int bBoxURx = " + list.get(3) + ";\n");
                        out.write("    internal static readonly int bBoxURy = " + list.get(4) + ";\n");
                    }
                }
                else if (list.get(0).equals("UnderlinePosition")) {
                    if (languageIsJava) {
                        out.write("    protected static final int underlinePosition = " + list.get(1) + ";\n");
                    }
                    else {
                        out.write("    internal static readonly int underlinePosition = " + list.get(1) + ";\n");
                    }
                }
                else if (list.get(0).equals("UnderlineThickness")) {
                    if (languageIsJava) {
                        out.write("    protected static final int underlineThickness = " + list.get(1) + ";\n");
                    }
                    else {
                        out.write("    internal static readonly int underlineThickness = " + list.get(1) + ";\n");
                    }
                }
                else if (list.get(0).equals("Notice")) {
                    if (languageIsJava) {
                        out.write("    protected static final String notice = \"");
                    }
                    else {
                        out.write("    internal static readonly String notice = \"");
                    }
                    out.write(line.substring(7));
                    out.write("\";\n");
                }
                else if (list.get(0).equals("StartCharMetrics")) {
                    if (languageIsJava) {
                        out.write("    protected static final int[][] metrics = {\n");
                    }
                    else {
                        out.write("    internal static readonly int[][] metrics = {\n");
                    }
                }
                else if (list.get(0).equals("C")) {
                    // C 95 ; WX 556 ; N underscore ; B 0 -125 556 -75 ;
                    String ch = list.get(1);
                    String wx = list.get(3);

                    if (ch.equals("32")) {
                        for (int i = 32; i < 256; i++) {
                            if (languageIsJava) {
                                data[i].append("        {");
                            }
                            else {
                                data[i].append("        new int[] {");
                            }
                            data[i].append(i);
                            data[i].append(',');
                            data[i].append(wx);
                            data[i].append("},\n");
                        }
                    }
                    else {
                        int i = Integer.parseInt(ch);
                        if (i > 0) {
                            data[i] = new StringBuffer();
                            if (languageIsJava) {
                                data[i].append("        {");
                            }
                            else {
                                data[i].append("        new int[] {");
                            }
                            data[i].append(ch);
                            data[i].append(',');
                            data[i].append(wx);
                            data[i].append("},\n");
                        }
                    }
                }
            }

            for (int i = 32; i < 256; i++) {
                out.write(data[i].toString());
            }

            out.write("    };\n");
            out.write("}\n");

            if (!languageIsJava) {
                out.write("}   // End of namespace PDFjet.NET\n");
            }

            in.close();
            out.close();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }


    public static void main(String[] args) {
        File file = new File("fonts/Core/");
        String[] fileNames = file.list(new util.AfmFilenameFilter());
        for (int i = 0; i < fileNames.length; i++) {
            if (fileNames[i].equals("Symbol.afm") ||
                    fileNames[i].equals("ZapfDingbats.afm")) {
                new AfmCompiler("fonts/Core/", fileNames[i], "Java", "Symbol");
                new AfmCompiler("fonts/Core/", fileNames[i], "C#", "Symbol");
            }
            else {
                new AfmCompiler("fonts/Core/", fileNames[i], "Java");
                new AfmCompiler("fonts/Core/", fileNames[i], "C#");
            }
        }
    }

}   // End of AfmCompiler.java


class AfmFilenameFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        if (name.endsWith(".afm")) {
            return true;
        }
        return false;
    }
}   // End of AfmFilenameFilter.java
