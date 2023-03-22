/**
 *  ColorMap.java
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

import java.util.HashMap;
import java.util.Map;

/**
 * Used to specify the pen and brush colors.
 * @see <a href="http://www.w3.org/TR/css3-color/#svg-color">http://www.w3.org/TR/css3-color/#svg-color</a>
 *
 */
 public class ColorMap {
    Map<String, Integer> map = null;

    public ColorMap() {
        map = new HashMap<String, Integer>();
        map.put("transparent", -1);
        map.put("none", -1);
        map.put("aliceblue", 0xf0f8ff);
        map.put("antiquewhite", 0xfaebd7);
        map.put("aqua", 0x00ffff);
        map.put("aquamarine", 0x7fffd4);
        map.put("azure", 0xf0ffff);
        map.put("beige", 0xf5f5dc);
        map.put("bisque", 0xffe4c4);
        map.put("black", 0x000000);
        map.put("blanchedalmond", 0xffebcd);
        map.put("blue", 0x0000ff);
        map.put("blueviolet", 0x8a2be2);
        map.put("brown", 0xa52a2a);
        map.put("burlywood", 0xdeb887);
        map.put("cadetblue", 0x5f9ea0);
        map.put("chartreuse", 0x7fff00);
        map.put("chocolate", 0xd2691e);
        map.put("coral", 0xff7f50);
        map.put("cornflowerblue", 0x6495ed);
        map.put("cornsilk", 0xfff8dc);
        map.put("crimson", 0xdc143c);
        map.put("cyan", 0x00ffff);
        map.put("darkblue", 0x00008b);
        map.put("darkcyan", 0x008b8b);
        map.put("darkgoldenrod", 0xb8860b);
        map.put("darkgray", 0xa9a9a9);
        map.put("darkgreen", 0x006400);
        map.put("darkgrey", 0xa9a9a9);
        map.put("darkkhaki", 0xbdb76b);
        map.put("darkmagenta", 0x8b008b);
        map.put("darkolivegreen", 0x556b2f);
        map.put("darkorange", 0xff8c00);
        map.put("darkorchid", 0x9932cc);
        map.put("darkred", 0x8b0000);
        map.put("darksalmon", 0xe9967a);
        map.put("darkseagreen", 0x8fbc8f);
        map.put("darkslateblue", 0x483d8b);
        map.put("darkslategray", 0x2f4f4f);
        map.put("darkslategrey", 0x2f4f4f);
        map.put("darkturquoise", 0x00ced1);
        map.put("darkviolet", 0x9400d3);
        map.put("deeppink", 0xff1493);
        map.put("deepskyblue", 0x00bfff);
        map.put("dimgray", 0x696969);
        map.put("dimgrey", 0x696969);
        map.put("dodgerblue", 0x1e90ff);
        map.put("firebrick", 0xb22222);
        map.put("floralwhite", 0xfffaf0);
        map.put("forestgreen", 0x228b22);
        map.put("fuchsia", 0xff00ff);
        map.put("gainsboro", 0xdcdcdc);
        map.put("ghostwhite", 0xf8f8ff);
        map.put("gold", 0xffd700);
        map.put("goldenrod", 0xdaa520);
        map.put("gray", 0x808080);
        map.put("green", 0x008000);
        map.put("greenyellow", 0xadff2f);
        map.put("grey", 0x808080);
        map.put("honeydew", 0xf0fff0);
        map.put("hotpink", 0xff69b4);
        map.put("indianred", 0xcd5c5c);
        map.put("indigo", 0x4b0082);
        map.put("ivory", 0xfffff0);
        map.put("khaki", 0xf0e68c);
        map.put("lavender", 0xe6e6fa);
        map.put("lavenderblush", 0xfff0f5);
        map.put("lawngreen", 0x7cfc00);
        map.put("lemonchiffon", 0xfffacd);
        map.put("lightblue", 0xadd8e6);
        map.put("lightcoral", 0xf08080);
        map.put("lightcyan", 0xe0ffff);
        map.put("lightgoldenrodyellow", 0xfafad2);
        map.put("lightgray", 0xd3d3d3);
        map.put("lightgreen", 0x90ee90);
        map.put("lightgrey", 0xd3d3d3);
        map.put("lightpink", 0xffb6c1);
        map.put("lightsalmon", 0xffa07a);
        map.put("lightseagreen", 0x20b2aa);
        map.put("lightskyblue", 0x87cefa);
        map.put("lightslategray", 0x778899);
        map.put("lightslategrey", 0x778899);
        map.put("lightsteelblue", 0xb0c4de);
        map.put("lightyellow", 0xffffe0);
        map.put("lime", 0x00ff00);
        map.put("limegreen", 0x32cd32);
        map.put("linen", 0xfaf0e6);
        map.put("magenta", 0xff00ff);
        map.put("maroon", 0x800000);
        map.put("mediumaquamarine", 0x66cdaa);
        map.put("mediumblue", 0x0000cd);
        map.put("mediumorchid", 0xba55d3);
        map.put("mediumpurple", 0x9370db);
        map.put("mediumseagreen", 0x3cb371);
        map.put("mediumslateblue", 0x7b68ee);
        map.put("mediumspringgreen", 0x00fa9a);
        map.put("mediumturquoise", 0x48d1cc);
        map.put("mediumvioletred", 0xc71585);
        map.put("midnightblue", 0x191970);
        map.put("mintcream", 0xf5fffa);
        map.put("mistyrose", 0xffe4e1);
        map.put("moccasin", 0xffe4b5);
        map.put("navajowhite", 0xffdead);
        map.put("navy", 0x000080);
        map.put("oldlace", 0xfdf5e6);
        map.put("olive", 0x808000);
        map.put("olivedrab", 0x6b8e23);
        map.put("orange", 0xffa500);
        map.put("orangered", 0xff4500);
        map.put("orchid", 0xda70d6);
        map.put("palegoldenrod", 0xeee8aa);
        map.put("palegreen", 0x98fb98);
        map.put("paleturquoise", 0xafeeee);
        map.put("palevioletred", 0xdb7093);
        map.put("papayawhip", 0xffefd5);
        map.put("peachpuff", 0xffdab9);
        map.put("peru", 0xcd853f);
        map.put("pink", 0xffc0cb);
        map.put("plum", 0xdda0dd);
        map.put("powderblue", 0xb0e0e6);
        map.put("purple", 0x800080);
        map.put("red", 0xff0000);
        map.put("rosybrown", 0xbc8f8f);
        map.put("royalblue", 0x4169e1);
        map.put("saddlebrown", 0x8b4513);
        map.put("salmon", 0xfa8072);
        map.put("sandybrown", 0xf4a460);
        map.put("seagreen", 0x2e8b57);
        map.put("seashell", 0xfff5ee);
        map.put("sienna", 0xa0522d);
        map.put("silver", 0xc0c0c0);
        map.put("skyblue", 0x87ceeb);
        map.put("slateblue", 0x6a5acd);
        map.put("slategray", 0x708090);
        map.put("slategrey", 0x708090);
        map.put("snow", 0xfffafa);
        map.put("springgreen", 0x00ff7f);
        map.put("steelblue", 0x4682b4);
        map.put("tan", 0xd2b48c);
        map.put("teal", 0x008080);
        map.put("thistle", 0xd8bfd8);
        map.put("tomato", 0xff6347);
        map.put("turquoise", 0x40e0d0);
        map.put("violet", 0xee82ee);
        map.put("wheat", 0xf5deb3);
        map.put("white", 0xffffff);
        map.put("whitesmoke", 0xf5f5f5);
        map.put("yellow", 0xffff00);
        map.put("yellowgreen", 0x9acd32);
        map.put("oldgloryred", 0xb22234);
        map.put("oldgloryblue", 0x3c3b6e);
    }

    public int getColor(String colorName) {
        if (colorName.startsWith("#")) {
            if (colorName.length() == 7) {
                return Integer.valueOf(colorName.substring(1), 16);
            } else if (colorName.length() == 4) {
                String str = new String(new char[] {
                        colorName.charAt(1), colorName.charAt(1),
                        colorName.charAt(2), colorName.charAt(2),
                        colorName.charAt(3), colorName.charAt(3)
                });
                return Integer.valueOf(str, 16);
            } else {
                return Color.transparent;
            }
        }
        Integer color = map.get(colorName);
        if (color == null) {
            return Color.transparent;
        }
        return color;
    }
}
