/**
 *  Bidi.java
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


/**
 *  Provides simple BIDI processing for Hebrew.
 *  Please see Example_27.
 *  Note that the base level must be right to left.
 *  This means you can only use Hebrew text with a few Latin words and numbers embedded in it.
 *  Not the other way around.
 */
public class Bidi {

    /**
     *  Reorders the string so that Hebrew text flows from right to left while numbers and Latin text flow from left to right.
     *
     *  @param str the input string.
     *  @return the processed string.
     */
    public static String reorderVisually(String str) {

        StringBuilder sb1 = new StringBuilder(str).reverse();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();

        for (int i = 0; i < sb1.length(); i++) {
            int ch = sb1.charAt(i);
            if (ch >= 0x591 && ch <= 0x05F4) {
                if (sb2.length() != 0) {
                    sb3.append(sb2.reverse());
                    sb2 = new StringBuilder();
                }
                sb3.append((char) ch);
            }
            else {
                sb2.append((char) ch);
            }
        }
        sb3.append(sb2.reverse());

        return sb3.toString();
    }

}
