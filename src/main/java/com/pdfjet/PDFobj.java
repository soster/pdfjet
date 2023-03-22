/**
 *  PDFobj.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 *  Used to create Java or .NET objects that represent the objects in PDF document.
 *  See the PDF specification for more information.
 */
public class PDFobj {
    protected int number;           // The object number
    protected int offset;           // The object offset
    protected List<String> dict;
    protected int streamOffset;
    protected byte[] stream;        // The compressed stream
    protected byte[] data;          // The decompressed data
    protected int gsNumber = -1;

    /**
     *  Used to create Java or .NET objects that represent the objects in PDF document.
     *  See the PDF specification for more information.
     *  Also see Example_19.
     */
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


    /**
     *  Returns the uncompressed stream data.
     *
     *  @return the uncompressed stream data.
     */
    public byte[] getData() {
        return this.data;
    }


    protected void setStreamAndData(byte[] buf, int length) throws Exception {
        if (this.stream == null) {
            this.stream = new byte[length];
            System.arraycopy(buf, streamOffset, stream, 0, length);
            if (getValue("/Filter").equals("/FlateDecode")) {
                this.data = Decompressor.inflate(stream);
            }
            else {
                // Assume no compression for now.
                // In the future we may handle LZW compression ...
                this.data = stream;
            }
        }
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
            if (dict.get(i).equals(key)) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {
                    StringBuilder buffer = new StringBuilder();
                    buffer.append("<< ");
                    i += 2;
                    while (!dict.get(i).equals(">>")) {
                        buffer.append(dict.get(i));
                        buffer.append(" ");
                        i += 1;
                    }
                    buffer.append(">>");
                    return buffer.toString();
                }
                else if (token.equals("[")) {
                    StringBuilder buffer = new StringBuilder();
                    buffer.append("[ ");
                    i += 2;
                    while (!dict.get(i).equals("]")) {
                        buffer.append(dict.get(i));
                        buffer.append(" ");
                        i += 1;
                    }
                    buffer.append("]");
                    return buffer.toString();
                }
                else {
                    return token;
                }
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
                        Float.parseFloat(dict.get(i + 4)),
                        Float.parseFloat(dict.get(i + 5)) };
            }
        }
        return Letter.PORTRAIT;
    }


    protected int getLength(List<PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            String token = dict.get(i);
            if (token.equals("/Length")) {
                int number = Integer.parseInt(dict.get(i + 1));
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
                return Integer.parseInt(obj.dict.get(3));
            }
        }
        return 0;
    }


    public PDFobj getContentsObject(List<PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Contents")) {
                if (dict.get(i + 1).equals("[")) {
                    String token = dict.get(i + 2);
                    return objects.get(Integer.parseInt(token) - 1);
                }
                else {
                    String token = dict.get(i + 1);
                    return objects.get(Integer.parseInt(token) - 1);
                }
            }
        }
        return null;
    }

    public PDFobj getResourcesObject(List<PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {
                    return this;
                }
                return objects.get(Integer.parseInt(token) - 1);
            }
        }
        return null;
    }

    public Font addResource(CoreFont coreFont, List<PDFobj> objects) {
        Font font = new Font(coreFont);
        font.fontID = font.name.replace('-', '_').toUpperCase();

        PDFobj obj = new PDFobj();
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
        obj.number = objects.size() + 1;
        objects.add(obj);

        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(++i);
                if (token.equals("<<")) {                       // Direct resources object
                    addFontResource(this, objects, font.fontID, obj.number);
                }
                else if (Character.isDigit(token.charAt(0))) {  // Indirect resources object
                    addFontResource(objects.get(Integer.parseInt(token) - 1), objects, font.fontID, obj.number);
                }
            }
        }

        return font;
    }


    private void addFontResource(
            PDFobj obj, List<PDFobj> objects, String fontID, int number) {

        boolean fonts = false;
        for (int i = 0; i < obj.dict.size(); i++) {
            if (obj.dict.get(i).equals("/Font")) {
                fonts = true;
                break;
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
                    PDFobj o2 = objects.get(Integer.parseInt(token) - 1);
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
            String type, PDFobj obj, List<PDFobj> objects, int objNumber) {
        String tag = type.equals("/Font") ? "/F" : "/Im";
        String number = String.valueOf(objNumber);
        List<String> list = Arrays.asList(tag + number, number, "0", "R");
        for (int i = 0; i < obj.dict.size(); i++) {
            String token = obj.dict.get(i);
            if (token.equals(type)) {
                token = obj.dict.get(i + 1);
                if (token.equals("<<")) {
                    insertNewObject(obj.dict, list, type);
                }
                else {
                    insertNewObject(objects.get(Integer.parseInt(token) - 1).dict, list, type);
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


    public void addResource(Image image, List<PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {       // Direct resources object
                    addResource("/XObject", this, objects, image.objNumber);
                }
                else {                          // Indirect resources object
                    addResource("/XObject", objects.get(Integer.parseInt(token) - 1), objects, image.objNumber);
                }
                return;
            }
        }
    }


    public void addResource(Font font, List<PDFobj> objects) {
        for (int i = 0; i < dict.size(); i++) {
            if (dict.get(i).equals("/Resources")) {
                String token = dict.get(i + 1);
                if (token.equals("<<")) {       // Direct resources object
                    addResource("/Font", this, objects, font.objNumber);
                }
                else {                          // Indirect resources object
                    addResource("/Font", objects.get(Integer.parseInt(token) - 1), objects, font.objNumber);
                }
                return;
            }
        }
    }


    public void addContent(byte[] content, List<PDFobj> objects) {
        PDFobj obj = new PDFobj();
        obj.setNumber(objects.size() + 1);
        obj.setStream(content);
        objects.add(obj);

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
                    PDFobj obj2 = objects.get(Integer.parseInt(token) - 1);
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
     * @param content the new content object to be added
     * @param objects the existing list of content objects
     */
    public void addPrefixContent(byte[] content, List<PDFobj> objects) {
        PDFobj obj = new PDFobj();
        obj.setNumber(objects.size() + 1);
        obj.setStream(content);
        objects.add(obj);

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
                    PDFobj obj2 = objects.get(Integer.parseInt(token) - 1);
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


    public void setGraphicsState(GraphicsState gs, List<PDFobj> objects) {
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
                    obj = objects.get(Integer.parseInt(token) - 1);
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
        obj.dict.add(++index, String.valueOf(gs.getAlphaStroking()));
        obj.dict.add(++index, "/ca");
        obj.dict.add(++index, String.valueOf(gs.getAlphaNonStroking()));
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
