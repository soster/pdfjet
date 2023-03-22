/**
 *  TextUtils.java
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

import java.util.*;


public class TextUtils {

    public static String[] splitTextIntoTokens(
            String text,
            Font font,
            Font fallbackFont,
            float width) {
        List<String> tokens2 = new ArrayList<String>();

        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            if (font.stringWidth(fallbackFont, token) <= width) {
                tokens2.add(token);
            }
            else {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < token.length(); i++) {
                    String ch = String.valueOf(token.charAt(i));
                    if (font.stringWidth(fallbackFont, buf.toString() + ch) <= width) {
                        buf.append(ch);
                    }
                    else {
                        tokens2.add(buf.toString());
                        buf.setLength(0);
                        buf.append(ch);
                    }
                }
                String str = buf.toString();
                if (!str.equals("")) {
                    tokens2.add(str);
                }
            }
        }

        return tokens2.toArray(new String[] {});
    }

}
