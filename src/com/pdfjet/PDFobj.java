/**
 *  PDFobj.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class PDFobj {
    protected int number;
    protected int offset;
    protected List<String> dict;
    protected int stream_offset;
    protected byte[] stream;
    protected byte[] data;

    public PDFobj(int offset) {
        this.offset = offset;
        this.dict = new ArrayList();
    }

    protected PDFobj() {
        this.dict = new ArrayList();
    }

    public int getNumber() {
        return this.number;
    }


    public List<String> getDict() {
        return this.dict;
    }


    public byte[] getData() {
        return this.data;
    }

    protected void setStream(byte[] pdf, int length) {
        this.stream = new byte[length];
        System.arraycopy(pdf, this.stream_offset, this.stream, 0, length);
    }

    protected void setStream(byte[] stream) {
        this.stream = stream;
    }

    protected void setNumber(int number) {
        this.number = number;
    }

    /**
     *  Returns the dictionary value for the specified key.
     *
     *  @param key the specified key.
     *
     *  @return the value.
     */
    public String getValue(String key) {
        for (int i = 0; i < this.dict.size(); i++) {
            String token = this.dict.get(i);
            if (token.equals(key)) {
                return this.dict.get(i + 1);
            }
        }
        return "";
    }

    protected List<Integer> getObjectNumbers(String key) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < dict.size(); i++) {
            String token = dict.get(i);
            if (token.equals(key)) {
                String str = dict.get(++i);
                if (str.equals("[")) {
                    for (; ; ) {
                        str = dict.get(++i);
                        if (str.equals("]")) {
                            break;
                        }
                        numbers.add(Integer.valueOf(str));
                        ++i; // 0
                        ++i; // R
                    }
                } else {
                    numbers.add(Integer.valueOf(str));
                }
                break;
            }
        }
        return numbers;
    }

    public void addContent(byte[] content, Map<Integer, PDFobj> objects) {
        PDFobj obj = new PDFobj();
        obj.setNumber(Collections.max(objects.keySet()) + 1);
        obj.setStream(content);
        objects.put(obj.getNumber(), obj);

        int index = -1;
        boolean single = false;
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Contents")) {
                String str = dict.get(++i);
                if (str.equals("[")) {
                    for (;;) {
                        str = dict.get(++i);
                        if (str.equals("]")) {
                            index = i;
                            break;
                        }
                        ++i; // 0
                        ++i; // R
                    }
                } else {
                    // Single content object
                    index = i;
                    single = true;
                }
                break;
            }
        }

        if (single) {
            dict.add(index, "[");
            dict.add(index + 4, "]");
            dict.add(index + 4, "R");
            dict.add(index + 4, "0");
            dict.add(index + 4, String.valueOf(obj.number));
        } else {
            dict.add(index, "R");
            dict.add(index, "0");
            dict.add(index, String.valueOf(obj.number));
        }
    }

    /**
     * Adds a content before the existing content.
     * @param content
     * @param objects
     */
    public void addPrefixContent(byte[] content, Map<Integer, PDFobj> objects) {
        PDFobj obj = new PDFobj();
        obj.setNumber(Collections.max(objects.keySet()) + 1);
        obj.setStream(content);
        objects.put(obj.getNumber(), obj);

        int index = -1;
        boolean single = false;
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Contents")) {
                String str = dict.get(++i);
                if (str.equals("[")) {
                    index = i + 1;
                } else {
                    // Single content object
                    index = i;
                    single = true;
                }
                break;
            }
        }

        if (single) {
            dict.add(index + 3, "]");
            dict.add(index, "R");
            dict.add(index, "0");
            dict.add(index, String.valueOf(obj.number));
            dict.add(index, "[");

        } else {
            dict.add(index, "R");
            dict.add(index, "0");
            dict.add(index, String.valueOf(obj.number));
        }
    }

    public float[] getPageSize() {
        for (int i = 0; i < this.dict.size(); i++) {
            if ((this.dict.get(i)).equals("/MediaBox")) {
                return new float[]{Float.valueOf(this.dict.get(i + 4)).floatValue(), Float.valueOf(this.dict.get(i + 5)).floatValue()};
            }
        }


        return Letter.PORTRAIT;
    }

    protected int getLength(List<PDFobj> objects) {
        for (int i = 0; i < this.dict.size(); i++) {
            String str = this.dict.get(i);
            if (str.equals("/Length")) {
                int j = Integer.valueOf(this.dict.get(i + 1)).intValue();
                if (((this.dict.get(i + 2)).equals("0")) && ((this.dict.get(i + 3)).equals("R"))) {
                    return getLength(objects, j);
                }

                return j;
            }
        }

        return 0;
    }

    protected int getLength(List<PDFobj> objects, int number) {
        for (PDFobj localPDFobj : objects) {
            if (localPDFobj.number == number) {
                return Integer.valueOf((String) localPDFobj.dict.get(3)).intValue();
            }
        }
        return 0;
    }

    public PDFobj getContentsObject(Map<Integer, PDFobj> objects) {
        for (int i = 0; i < this.dict.size(); i++) {
            if ((this.dict.get(i)).equals("/Contents")) {
                if ((this.dict.get(i + 1)).equals("[")) {
                    return objects.get(Integer.valueOf(this.dict.get(i + 2)));
                }
                return objects.get(Integer.valueOf(this.dict.get(i + 1)));
            }
        }
        return null;
    }

    public PDFobj getResourcesObject(Map<Integer, PDFobj> objects) {
        for (int i = 0; i < this.dict.size(); i++) {
            if ((this.dict.get(i)).equals("/Resources")) {
                return objects.get(Integer.valueOf(this.dict.get(i + 1)));
            }
        }
        return null;
    }

    public Font addFontResource(CoreFont paramCoreFont, Map<Integer, PDFobj> paramMap) {
        Font localFont = new Font(paramCoreFont);
        localFont.fontID = localFont.name.replace('-', '_').toUpperCase();

        PDFobj localPDFobj = new PDFobj();
        localPDFobj.number = ((Collections.max(paramMap.keySet())).intValue() + 1);
        localPDFobj.dict.add("<<");
        localPDFobj.dict.add("/Type");
        localPDFobj.dict.add("/Font");
        localPDFobj.dict.add("/Subtype");
        localPDFobj.dict.add("/Type1");
        localPDFobj.dict.add("/BaseFont");
        localPDFobj.dict.add("/" + localFont.name);
        if ((!localFont.name.equals("Symbol")) && (!localFont.name.equals("ZapfDingbats"))) {
            localPDFobj.dict.add("/Encoding");
            localPDFobj.dict.add("/WinAnsiEncoding");
        }
        localPDFobj.dict.add(">>");

        paramMap.put(Integer.valueOf(localPDFobj.number), localPDFobj);

        for (int i = 0; i < this.dict.size(); i++) {
            if ((this.dict.get(i)).equals("/Resources")) {
                String str = this.dict.get(++i);
                if (str.equals("<<")) {
                    addFontResource(this, paramMap, localFont.fontID, localPDFobj.number);
                } else if (Character.isDigit(str.charAt(0))) {
                    addFontResource( paramMap.get(Integer.valueOf(str)), paramMap, localFont.fontID, localPDFobj.number);
                }
            }
        }

        return localFont;
    }


    private void addFontResource(PDFobj obj, Map<Integer, PDFobj> objects, String fontID, int number) {
        for (int i = 0; i < obj.dict.size(); i++) {
            String str = null;
            if ((obj.dict.get(i)).equals("/Font")) {
                str = obj.dict.get(++i);
                if (str.equals("<<")) {
                    obj.dict.add(++i, "/" + fontID);
                    obj.dict.add(++i, String.valueOf(number));
                    obj.dict.add(++i, "0");
                    obj.dict.add(++i, "R");
                    break;
                }
                if (Character.isDigit(str.charAt(0))) {
                    PDFobj localPDFobj = (PDFobj) objects.get(Integer.valueOf(str));
                    for (int j = 0; j < localPDFobj.dict.size(); j++) {
                        str = localPDFobj.dict.get(j);
                        if (str.equals("<<")) {
                            localPDFobj.dict.add(++j, "/" + fontID);
                            localPDFobj.dict.add(++j, String.valueOf(number));
                            localPDFobj.dict.add(++j, "0");
                            localPDFobj.dict.add(++j, "R");
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds alpha groups also to existing pdf files.
     * FIXME does not work in all cases yet!
     * @param states
     * @param objects
     */
    public void addAlphaGroups(Map<String, Integer> states, Map<Integer, PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(++i);
                if (token.equals("<<")) { // Direct resources object
                    boolean extexists = false;
                    //FIXME harmful...
                    for (int j = i; j < dict.size(); j++) {
                        if (dict.get(j).equals("/ExtGState")) {
                            String token2 = dict.get(++j);
                            if (token2.equals("<<")) {
                                extexists = true;
                                i = j;
                            }
                        }
                    }
                    if (!extexists) {
                        dict.add(++i, "/ExtGState");
                        dict.add(++i, "<<");
                    }
                    for (Iterator localIterator2 = states.keySet().iterator(); localIterator2.hasNext(); ) {
                        String key = (String) localIterator2.next();
                        dict.add(++i, "/GS"+String.valueOf(states.get(key).intValue()));
                        dict.add(++i, "<<");
                        dict.add(++i, key);
                        dict.add(++i, ">>");
                    }
                    if (!extexists) {
                        dict.add(++i, ">>");
                    }
                    break;
                } else if (Character.isDigit(token.charAt(0))) { // Indirect resources
                    PDFobj o2 = objects.get(Integer.valueOf(token));// object
                    addAlphaGroups(o2, states, objects);
                    break;
                }
            }
        }
    }

    protected void addAlphaGroups(PDFobj obj, Map<String, Integer> states, Map<Integer, PDFobj> objects) {
        boolean added = false;
        for (int i = 0; i < obj.dict.size(); i++) {
            String token = null;
            if (obj.dict.get(i).equals("/ExtGState")) {
                token = obj.dict.get(++i);
                if (token.equals("<<")) {
                    for (Iterator localIterator2 = states.keySet().iterator(); localIterator2.hasNext(); ) {
                        String key = (String) localIterator2.next();
                        obj.dict.add(++i, "/GS"+String.valueOf(states.get(key).intValue()));
                        obj.dict.add(++i, "<<");
                        obj.dict.add(++i, key);
                        obj.dict.add(++i, ">>");
                    }
                    added = true;
                    break;
                } else if (Character.isDigit(token.charAt(0))) {
                    PDFobj o2 = objects.get(Integer.valueOf(token));
                    addAlphaGroups(o2, states, objects);
                    added = true;
                }
            }
        }

        if (!added) {
            obj.dict.add("/ExtGState");
            obj.dict.add("/<<");
            for (Iterator localIterator2 = states.keySet().iterator(); localIterator2.hasNext(); ) {
                String key = (String) localIterator2.next();
                obj.dict.add("/GS"+String.valueOf(states.get(key).intValue()));
                obj.dict.add("<<");
                obj.dict.add(key);
                obj.dict.add(">>");
            }
            obj.dict.add(">>");

        }

    }
}
