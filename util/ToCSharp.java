/**
 *  ToCSharp.java
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
 * ToCSharp.java
 *
 * Converts the Java source files to C# source files.
 * The mappings are in java-to-csharp.code
 */
public class ToCSharp {

    public ToCSharp(String fileName) {

        // Load the Java to C# dictionary
        List<Pair> dict = new ArrayList<Pair>();
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader("util/java-to-csharp.code"));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.equals("")) continue;
                if (line.startsWith("#")) continue;
                Pair pair = new Pair();
                pair.key = line.substring(0, line.indexOf('='));
                pair.value = line.substring(line.indexOf('=') + 1);
                dict.add(pair);
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }

        StringBuffer data  = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            int ch;
            while ((ch = in.read()) != -1) {
                data.append((char) ch);
            }

            BufferedWriter out = null;
            if (fileName.indexOf('/') != -1) {
                out = new BufferedWriter(new FileWriter(
                        "net" + fileName.substring(
                                fileName.indexOf('/'),
                                fileName.lastIndexOf('.')) + ".cs"));
            } else {
                out = new BufferedWriter(new FileWriter(fileName.substring(
                        0, fileName.lastIndexOf('.')) + ".cs"));
            }

            for (int i = 0; i < dict.size(); i++) {
                Pair pair = dict.get(i);
                searchAndReplace(data, pair.key, pair.value);
            }

            out.write(data.toString(), 0, data.length());
            out.close();

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Searches the buffer for 'key' and replaces it with 'value'
    private void searchAndReplace(
            StringBuffer buf, String key, String value) {

        String str = buf.toString();
        buf.setLength(0);

        int i = 0;
        int j = 0;

        while (true) {
            j = str.indexOf(key, i);
            if (j == -1) {
                buf.append(str.substring(i));
                break;
            }

            buf.append(str.substring(i, j));
            buf.append(value);

            j += key.length();
            i = j;
        }
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage:");
            System.err.println("    java ToCSharp Test01.java");
            System.err.println("or:");
            System.err.println("    java ToCSharp com/pdfjet/");
            System.err.println(
                    "To convert all .java files in the com/pdfjet/ directory");
            System.exit(1);
        } else if (args.length == 1) {
            if (args[0].indexOf('/') != -1) {
                File file = new File(args[0]);
                String[] fileNames = file.list(new JavaFilenameFilter());
                for (int i = 0; i < fileNames.length; i++) {
                    if (fileNames[i].equals("PDF.java") ||
                            fileNames[i].equals("Font.java") ||
                            fileNames[i].equals("OpenTypeFont.java") ||
                            fileNames[i].startsWith("Courier") ||
                            fileNames[i].startsWith("Helvetica") ||
                            fileNames[i].startsWith("Times") ||
                            fileNames[i].startsWith("Symbol") ||
                            fileNames[i].startsWith("ZapfDingbats") ||
                            fileNames[i].startsWith("IoStream") ||
                            fileNames[i].startsWith("Salsa20") ||
                            fileNames[i].startsWith("SHA1") ||
                            fileNames[i].startsWith("BMPImage") ||
                            fileNames[i].startsWith("JPEGImage") ||
                            fileNames[i].startsWith("PNGImage") ||
                            fileNames[i].startsWith("Chunk") ||
                            fileNames[i].startsWith("Compressor") ||
                            fileNames[i].startsWith("Decompressor")) {
                        // skip it ...
                    } else {
                        new ToCSharp(args[0] + fileNames[i]);
                    }
                }
            } else {
                new ToCSharp(args[0]);
            }
        }
        System.exit(0);
    }

}   // End of ToCSharp.java


class JavaFilenameFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        if (name.endsWith(".java")) {
            return true;
        }
        return false;
    }
}
