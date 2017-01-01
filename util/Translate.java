/**
 * Translate.java
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
 * Translate.java
 * Translates the documentation created by JavaDoc to match the C# code.
 */
public class Translate {

    public Translate(String readDir, String fileName, String writeDir) {

        // Load the Java to C# dictionary
        List<Pair> dict = new ArrayList<Pair>();
        try {
            BufferedReader in =
                    new BufferedReader(new FileReader("util/java-to-csharp.docs"));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (!line.equals("") && !line.startsWith("#")) {
                    Pair pair = new Pair();
                    pair.key = line.substring(0, line.indexOf('='));
                    pair.value = line.substring(line.indexOf('=') + 1);
                    dict.add(pair);
                }
            }
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }

        StringBuffer data  = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader(readDir + "/" + fileName));
            int ch;
            while ((ch = in.read()) != -1) {
                data.append((char) ch);
            }

            BufferedWriter out = new BufferedWriter(
                    new FileWriter(writeDir + "/" + fileName));

            for (int i = 0; i < dict.size(); i++) {
                Pair pair = dict.get(i);
                searchAndReplace(data, pair.key, pair.value);
            }

            out.write(data.toString(), 0, data.length());
            out.close();

            in.close();
        }
        catch (Exception e) {
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
        String readDir = "docs/java/com/pdfjet";
        String writeDir = "docs/_net/net/pdfjet";
        File file = new File(readDir);
        String[] fileNames = file.list(new HtmlFilenameFilter());
        for (int i = 0; i < fileNames.length; i++) {
            new Translate(readDir, fileNames[i], writeDir);
        }

        readDir = "docs/java";
        writeDir = "docs/_net";
        file = new File(readDir);
        fileNames = file.list(new HtmlFilenameFilter());
        for (int i = 0; i < fileNames.length; i++) {
            new Translate(readDir, fileNames[i], writeDir);
        }
    }

}   // End of Translate.java


class HtmlFilenameFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        if (name.endsWith(".html")) {
            return true;
        }
        return false;
    }

}   // End of Translate.java
