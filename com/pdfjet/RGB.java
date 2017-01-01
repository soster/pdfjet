/**
 *  RGB.java
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

package com.pdfjet;


/**
 *  Used to specify the pen and brush colors. In new code please use the Color class - it contains a lot more predefined values that are W3C compliant.
 *
 *
 */
public class RGB {
    public static final float[] BLACK = {0.0f, 0.0f, 0.0f};
    public static final float[] WHITE = {1.0f, 1.0f, 1.0f};

    public static final float[] RED = {1.0f, 0.0f, 0.0f};
    public static final float[] GREEN = {0.0f, 1.0f, 0.0f};
    public static final float[] BLUE = {0.0f, 0.0f, 1.0f};

    public static final float[] YELLOW = {1.0f, 1.0f, 0.0f};
    public static final float[] CYAN = {0.0f, 1.0f, 1.0f};
    public static final float[] MAGENTA = {1.0f, 0.0f, 1.0f};

    public static final float[] LIGHT_GRAY = {0.75f, 0.75f, 0.75f};
    public static final float[] GRAY = {0.5f, 0.5f, 0.5f};
    public static final float[] DARK_GRAY = {0.25f, 0.25f, 0.25f};

    public static final float[] LIGHT_GREY = {0.75f, 0.75f, 0.75f};
    public static final float[] GREY = {0.5f, 0.5f, 0.5f};
    public static final float[] DARK_GREY = {0.25f, 0.25f, 0.25f};

    public static final float[] OLD_GLORY_BLUE = {0.0f, 49.0f/255.0f, 104.0f/255.0f};
    public static final float[] OLD_GLORY_RED = {191.0f/255.0f, 10.0f/255.0f, 48.0f/255.0f};
}
