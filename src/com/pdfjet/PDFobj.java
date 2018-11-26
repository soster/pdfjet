/**
 *  PDFobj.java
 *
Copyright (c) 2018, Innovatics Inc.
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

import java.io.*;
import java.util.*;


/**
 *  Used to create Java or .NET objects that represent the objects in PDF document.
 *  See the PDF specification for more information.
 *
 */
public class PDFobj {

    protected int number;           // The object number
    protected int offset;           // The object offset
    protected List<String> dict;
    protected int stream_offset;
    protected byte[] stream;        // The compressed stream
    protected byte[] data;          // The decompressed data
    protected int gsNumber = -1;


    /**
     *  Used to create Java or .NET objects that represent the objects in PDF document.
     *  See the PDF specification for more information.
     *  Also see Example_19.
     *
     *  @param offset the object offset in the offsets table.
     */
    public PDFobj(int offset) {
        this.offset = offset;
        this.dict = new ArrayList<String>();
    }


    protected PDFobj() {
        this.dict = new ArrayList<String>();
    }


    public int getNumber() {
        return this.number;
    }


    /**
     *  Returns the object dictionary.
     *
     *  @return the object dictionary.
     */
    public List<String> getDict() {
        return this.dict;
    }


    public void setDict(List<String> dict) {
        this.dict = dict;
    }


    /**
     *  Returns the uncompressed stream data.
     *
     *  @return the uncompressed stream data.
     */
    public byte[] getData() {
        return this.data;
    }


    protected void setStream(byte[] pdf, int length) {
        stream = new byte[length];
        System.arraycopy(pdf, this.stream_offset, stream, 0, length);
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
        for (int i = 0; i < dict.size(); i++) {
            String token = dict.get(i);
            if (token.equals(key)) {
                if (dict.get(i + 1).equals("<<")) {
                    StringBuilder buffer = new StringBuilder();
                    buffer.append("<<");
                    buffer.append(" ");
                    i += 2;
                    while (!dict.get(i).equals(">>")) {
                        buffer.append(dict.get(i));
                        buffer.append(" ");
                        i += 1;
                    }
                    buffer.append(">>");
                    return buffer.toString();
                }
                if (dict.get(i + 1).equals("[")) {
                    StringBuilder buffer = new StringBuilder();
                    buffer.append("[");
                    buffer.append(" ");
                    i += 2;
                    while (!dict.get(i).equals("]")) {
                        buffer.append(dict.get(i));
                        buffer.append(" ");
                        i += 1;
                    }
                    buffer.append("]");
                    return buffer.toString();
                }
                return dict.get(i + 1);
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
                    while (true) {
                        str = dict.get(++i);
                        if (str.equals("]")) {
                            break;
                        }
                        numbers.add(Integer.valueOf(str));
                        ++i;    // 0
                        ++i;    // R
                    }
                }
                else {
                    numbers.add(Integer.valueOf(str));
                }
                break;
            }
        }
        return numbers;
    }


    public float[] getPageSize() {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/MediaBox")) {
                return new float[] {
                        Float.valueOf(dict.get(i + 4)),
                        Float.valueOf(dict.get(i + 5)) };
            }
        }
        return Letter.PORTRAIT;
    }


    protected int getLength(List<PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            String token = dict.get(i);
            if (token.equals("/Length")) {
                int number = Integer.valueOf(dict.get(i + 1));
                if (dict.get(i + 2).equals("0") &&
                        dict.get(i + 3).equals("R")) {
                    return getLength(objects, number);
                }
                else {
                    return number;
                }
            }
        }
        return 0;
    }


    protected int getLength(List<PDFobj> objects, int number) {
        for (PDFobj obj : objects) {
            if (obj.number == number) {
                return Integer.valueOf(obj.dict.get(3));
            }
        }
        return 0;
    }


    public PDFobj getContentsObject(Map<Integer, PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Contents")) {
                if (dict.get(i + 1).equals("[")) {
                    return objects.get(Integer.valueOf(dict.get(i + 2)));
                }
                return objects.get(Integer.valueOf(dict.get(i + 1)));
            }
        }
        return null;
    }


    public PDFobj getResourcesObject(Map<Integer, PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {
                    PDFobj obj = new PDFobj();
                    obj.dict.add("0");
                    obj.dict.add("0");
                    obj.dict.add("obj");
                    obj.dict.add(token);
                    int level = 1;
                    i++;
                    while (i < dict.size() && level > 0) {
                        token = dict.get(i);
                        obj.dict.add(token);
                        if (token.equals("<<")) {
                            level++;
                        }
                        else if (token.equals(">>")) {
                            level--;
                        }
                        i++;
                    }
                    return obj;
                }
                return objects.get(Integer.valueOf(token));
            }
        }
        return null;
    }

/*
TODO: Test well this method and use instead of the method above.
    public PDFobj getResourcesObject(Map<Integer, PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {
                    return this;
                }
                return objects.get(Integer.valueOf(token));
            }
        }
        return null;
    }
*/

    public Font addResource(CoreFont coreFont, Map<Integer, PDFobj> objects) {
        Font font = new Font(coreFont);
        font.fontID = font.name.replace('-', '_').toUpperCase();

        PDFobj obj = new PDFobj();
        obj.number = Collections.max(objects.keySet()) + 1;
        obj.dict.add("<<");
        obj.dict.add("/Type");
        obj.dict.add("/Font");
        obj.dict.add("/Subtype");
        obj.dict.add("/Type1");
        obj.dict.add("/BaseFont");
        obj.dict.add("/" + font.name);
        if (!font.name.equals("Symbol") && !font.name.equals("ZapfDingbats")) {
            obj.dict.add("/Encoding");
            obj.dict.add("/WinAnsiEncoding");
        }
        obj.dict.add(">>");

        objects.put(obj.number, obj);

        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(++i);
                if (token.equals("<<")) {                       // Direct resources object
                    addFontResource(this, objects, font.fontID, obj.number);
                }
                else if (Character.isDigit(token.charAt(0))) {  // Indirect resources object
                    addFontResource(objects.get(Integer.valueOf(token)), objects, font.fontID, obj.number);
                }
            }
        }

        return font;
    }


    private void addFontResource(
            PDFobj obj, Map<Integer, PDFobj> objects, String fontID, int number) {

        boolean fonts = false;
        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals("/Font")) {
                fonts = true;
            }
        }
        if (!fonts) {
            for (int i = 0; i < obj.dict.size(); i++) {
                if (obj.dict.get(i).equals("/Resources")) {
                    obj.dict.add(i + 2, "/Font");
                    obj.dict.add(i + 3, "<<");
                    obj.dict.add(i + 4, ">>");
                    break;
                }
            }
        }

        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals("/Font")) {
                String token = obj.dict.get(i + 1);
                if (token.equals("<<")) {
                    obj.dict.add(i + 2, "/" + fontID);
                    obj.dict.add(i + 3, String.valueOf(number));
                    obj.dict.add(i + 4, "0");
                    obj.dict.add(i + 5, "R");
                    return;
                }
                else if (Character.isDigit(token.charAt(0))) {
                    PDFobj o2 = objects.get(Integer.valueOf(token));
                    for (int j = 0; j < o2.dict.size(); j++) {
                        if (o2.dict.get(j).equals("<<")) {
                            o2.dict.add(j + 1, "/" + fontID);
                            o2.dict.add(j + 2, String.valueOf(number));
                            o2.dict.add(j + 3, "0");
                            o2.dict.add(j + 4, "R");
                            return;
                        }
                    }
                }
            }
        }
    }


    private void insertNewObject(
            List<String> dict, List<String> list, String type) {
        for (String token : dict) {
            if (token.equals(list.get(0))) {
                return;
            }
        }
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals(type)) {
                dict.addAll(i + 2, list);
                return;
            }
        }
        if (dict.get(3).equals("<<")) {
            dict.addAll(4, list);
            return;
        }
    }


    private void addResource(
            String type, PDFobj obj, Map<Integer, PDFobj> objects, int objNumber) {
        String tag = type.equals("/Font") ? "/F" : "/Im";
        String number = String.valueOf(objNumber);
        List<String> list = Arrays.asList(tag + number, number, "0", "R");
        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals(type)) {
                String token = obj.dict.get(i + 1);
                if (token.equals("<<")) {
                    insertNewObject(obj.dict, list, type);
                }
                else {
                    insertNewObject(objects.get(Integer.valueOf(token)).dict, list, type);
                }
                return;
            }
        }

        // Handle the case where the page originally does not have any font resources.
        list = Arrays.asList(type, "<<", tag + number, number, "0", "R", ">>");
        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals("/Resources")) {
                obj.dict.addAll(i + 2, list);
                return;
            }
        }
        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals("<<")) {
                obj.dict.addAll(i + 1, list);
                return;
            }
        }
    }


    public void addResource(Image image, Map<Integer, PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {       // Direct resources object
                    addResource("/XObject", this, objects, image.objNumber);
                }
                else {                          // Indirect resources object
                    addResource("/XObject", objects.get(Integer.valueOf(token)), objects, image.objNumber);
                }
                return;
            }
        }
    }


    public void addResource(Font font, Map<Integer, PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {       // Direct resources object
                    addResource("/Font", this, objects, font.objNumber);
                }
                else {                          // Indirect resources object
                    addResource("/Font", objects.get(Integer.valueOf(token)), objects, font.objNumber);
                }
                return;
            }
        }
    }


    public void addContent(byte[] content, Map<Integer, PDFobj> objects) {
        PDFobj obj = new PDFobj();
        obj.setNumber(Collections.max(objects.keySet()) + 1);
        obj.setStream(content);
        objects.put(obj.getNumber(), obj);

        String objNumber = String.valueOf(obj.number);
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Contents")) {
                i += 1;
                String token = dict.get(i);
                if (token.equals("[")) {
                    // Array of content objects
                    while (true) {
                        i += 1;
                        token = dict.get(i);
                        if (token.equals("]")) {
                            dict.add(i, "R");
                            dict.add(i, "0");
                            dict.add(i, objNumber);
                            return;
                        }
                        i += 2;     // Skip the 0 and R
                    }
                }
                else {
                    // Single content object
                    PDFobj obj2 = objects.get(Integer.valueOf(token));
                    if (obj2.data == null && obj2.stream == null) {
                        // This is not a stream object!
                        for (int j = 0; j < obj2.dict.size(); j++) {
                            if (obj2.dict.get(j).equals("]")) {
                                obj2.dict.add(j, "R");
                                obj2.dict.add(j, "0");
                                obj2.dict.add(j, objNumber);
                                return;
                            }
                        }
                    }
                    dict.add(i, "[");
                    dict.add(i + 4, "]");
                    dict.add(i + 4, "R");
                    dict.add(i + 4, "0");
                    dict.add(i + 4, objNumber);
                    return;
                }
            }
        }
    }


    /**
     * Adds new content object before the existing content objects.
     * The original code was provided by Stefan Ostermann author of ScribMaster and HandWrite Pro.
     * Additional code to handle PDFs with indirect array of stream objects was written by EDragoev.
     *
     * @param content
     * @param objects
     */
    public void addPrefixContent(byte[] content, Map<Integer, PDFobj> objects) {
        PDFobj obj = new PDFobj();
        obj.setNumber(Collections.max(objects.keySet()) + 1);
        obj.setStream(content);
        objects.put(obj.getNumber(), obj);

        String objNumber = String.valueOf(obj.number);
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Contents")) {
                i += 1;
                String token = dict.get(i);
                if (token.equals("[")) {
                    // Array of content object streams
                    i += 1;
                    dict.add(i, "R");
                    dict.add(i, "0");
                    dict.add(i, objNumber);
                    return;
                }
                else {
                    // Single content object
                    PDFobj obj2 = objects.get(Integer.valueOf(token));
                    if (obj2.data == null && obj2.stream == null) {
                        // This is not a stream object!
                        for (int j = 0; j < obj2.dict.size(); j++) {
                            if (obj2.dict.get(j).equals("[")) {
                                j += 1;
                                obj2.dict.add(j, "R");
                                obj2.dict.add(j, "0");
                                obj2.dict.add(j, objNumber);
                                return;
                            }
                        }
                    }
                    dict.add(i, "[");
                    dict.add(i + 4, "]");
                    i += 1;
                    dict.add(i, "R");
                    dict.add(i, "0");
                    dict.add(i, objNumber);
                    return;
                }
            }
        }
    }


    private int getMaxGSNumber(PDFobj obj) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (String token : obj.dict) {
            if (token.startsWith("/GS")) {
                numbers.add(Integer.valueOf(token.substring(3)));
            }
        }
        if (numbers.isEmpty()) {
            return 0;
        }
        return Collections.max(numbers);
    }


    public void setGraphicsState(GraphicsState gs, Map<Integer, PDFobj> objects) {
        PDFobj obj = null;
        int index = -1;
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {
                    obj = this;
                    index = i + 2;
                }
                else {
                    obj = objects.get(Integer.valueOf(token));
                    for (int j = 0; j < obj.dict.size(); j++) {
                        if (obj.dict.get(j).equals("<<")) {
                            index = j + 1;
                            break;
                        }
                    }
                }
                break;
            }
        }

        gsNumber = getMaxGSNumber(obj);
        if (gsNumber == 0) {                    // No existing ExtGState dictionary
            obj.dict.add(index, "/ExtGState");  // Add ExtGState dictionary
            obj.dict.add(++index, "<<");
        }
        else {
            while (index < obj.dict.size()) {
                String token = obj.dict.get(index);
                if (token.equals("/ExtGState")) {
                    index += 1;
                    break;
                }
                index += 1;
            }
        }
        obj.dict.add(++index, "/GS" + String.valueOf(gsNumber + 1));
        obj.dict.add(++index, "<<");
        obj.dict.add(++index, "/CA");
        obj.dict.add(++index, String.valueOf(gs.get_CA()));
        obj.dict.add(++index, "/ca");
        obj.dict.add(++index, String.valueOf(gs.get_ca()));
        obj.dict.add(++index, ">>");
        if (gsNumber == 0) {
            obj.dict.add(++index, ">>");
        }

        StringBuilder buf = new StringBuilder();
        buf.append("q\n");
        buf.append("/GS" + String.valueOf(gsNumber + 1) + " gs\n");
        addPrefixContent(buf.toString().getBytes(), objects);
    }

}
