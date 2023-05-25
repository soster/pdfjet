/**
 *  PNGImage.java
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

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * Used to embed PNG images in the PDF document.
 * <p>
 * <strong>Please note:</strong>
 * <p>
 *     Interlaced images are not supported.
 * <p>
 *     To convert interlaced image to non-interlaced image use OptiPNG:
 * <p>
 *     optipng -i0 -o7 myimage.png
 */
public class PNGImage {
    int w;                      // Image width in pixels
    int h;                      // Image height in pixels

    byte[] iDAT;                // The compressed data in the IDAT chunks
    byte[] pLTE;                // The palette data
    byte[] tRNS;                // The alpha for the palette data

    byte[] deflatedImageData;   // The deflated image data
    byte[] deflatedAlphaData;   // The deflated alpha channel data

    private byte bitDepth = 8;
    private byte colorType = 0;

    /**
     * Used to embed PNG images in the PDF document.
     *
     * @param inputStream the inputStream.
     * @throws Exception  If an input or output exception occurred.
     */
    public PNGImage(InputStream inputStream) throws Exception {
        validatePNG(inputStream);

        List<Chunk> chunks = processPNG(inputStream);
        for (int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.get(i);
            String chunkType = new String(chunk.type);
            if (chunkType.equals("IHDR")) {
                this.w = toIntValue(chunk.getData(), 0);    // Width
                this.h = toIntValue(chunk.getData(), 4);    // Height
                this.bitDepth = chunk.getData()[8];         // Bit Depth
                this.colorType = chunk.getData()[9];        // Color Type
                // System.out.println(
                //         "Bit Depth == " + chunk.getData()[8]);
                // System.out.println(
                //         "Color Type == " + chunk.getData()[9]);
                // System.out.println(chunk.getData()[10]);
                // System.out.println(chunk.getData()[11]);
                // System.out.println(chunk.getData()[12]);
                if (chunk.getData()[12] == 1) {
                    System.out.println("Interlaced PNG images are not supported.\nConvert the image using OptiPNG:\noptipng -i0 -o7 myimage.png\n");
                }
            } else if (chunkType.equals("IDAT")) {
                iDAT = appendIdatChunk(iDAT, chunk.getData());
            } else if (chunkType.equals("PLTE")) {
                pLTE = chunk.getData();
                if (pLTE.length % 3 != 0) {
                    throw new Exception("Incorrect palette length.");
                }
            } else if (chunkType.equals("gAMA")) {
                // System.out.println("gAMA chunk found!");
            } else if (chunkType.equals("tRNS")) {
                if (colorType == 3) {
                    tRNS = chunk.getData();
                }
            } else if (chunkType.equals("cHRM")) {
                // System.out.println("cHRM chunk found!");
            } else if (chunkType.equals("sBIT")) {
                // System.out.println("sBIT chunk found!");
            } else if (chunkType.equals("bKGD")) {
                // System.out.println("bKGD chunk found!");
            }
        }

        byte[] inflatedImageData = Decompressor.inflate(iDAT);
        byte[] image;
        if (colorType == 0) {
            // Grayscale Image
            if (bitDepth == 16) {
                image = getImageColorType0BitDepth16(inflatedImageData);
            } else if (bitDepth == 8) {
                image = getImageColorType0BitDepth8(inflatedImageData);
            } else if (bitDepth == 4) {
                image = getImageColorType0BitDepth4(inflatedImageData);
            } else if (bitDepth == 2) {
                image = getImageColorType0BitDepth2(inflatedImageData);
            } else if (bitDepth == 1) {
                image = getImageColorType0BitDepth1(inflatedImageData);
            } else {
                throw new Exception("Image with unsupported bit depth == " + bitDepth);
            }
        } else if (colorType == 6) {
            if (bitDepth == 8) {
                image = getImageColorType6BitDepth8(inflatedImageData);
            } else {
                throw new Exception("Image with unsupported bit depth == " + bitDepth);
            }
        } else {
            // Color Image
            if (pLTE == null) {
                // Trucolor Image
                if (bitDepth == 16) {
                    image = getImageColorType2BitDepth16(inflatedImageData);
                } else {
                    image = getImageColorType2BitDepth8(inflatedImageData);
                }
            } else {
                // Indexed Image
                if (bitDepth == 8) {
                    image = getImageColorType3BitDepth8(inflatedImageData);
                } else if (bitDepth == 4) {
                    image = getImageColorType3BitDepth4(inflatedImageData);
                } else if (bitDepth == 2) {
                    image = getImageColorType3BitDepth2(inflatedImageData);
                } else if (bitDepth == 1) {
                    image = getImageColorType3BitDepth1(inflatedImageData);
                } else {
                    throw new Exception("Image with unsupported bit depth == " + bitDepth);
                }
            }
        }

        deflatedImageData = Compressor.deflate(image);
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public int getColorType() {
        return this.colorType;
    }

    public int getBitDepth() {
        return this.bitDepth;
    }

    public byte[] getData() {
        return this.deflatedImageData;
    }

    public byte[] getAlpha() {
        return this.deflatedAlphaData;
    }

    private List<Chunk> processPNG(InputStream inputStream) throws Exception {
        List<Chunk> chunks = new ArrayList<Chunk>();
        while (true) {
            Chunk chunk = getChunk(inputStream);
            if ((new String(chunk.type)).equals("IEND")) {
                break;
            }
            chunks.add(chunk);
        }
        return chunks;
    }

    private void validatePNG(InputStream stream) throws Exception {
        byte[] buf = new byte[8];
        if (stream.read(buf, 0, buf.length) == -1) {
            throw new Exception("File is too short!");
        }
        if ((buf[0] & 0xFF) == 0x89 &&
                buf[1] == 0x50 &&
                buf[2] == 0x4E &&
                buf[3] == 0x47 &&
                buf[4] == 0x0D &&
                buf[5] == 0x0A &&
                buf[6] == 0x1A &&
                buf[7] == 0x0A) {
            // The PNG signature is correct.
        } else {
            throw new Exception("Wrong PNG signature.");
        }
    }

    private Chunk getChunk(InputStream inputStream) throws Exception {
        Chunk chunk = new Chunk();
        chunk.length = getLong(inputStream);                // The length of the data chunk.
        chunk.type = getNBytes(inputStream, 4);             // The chunk type.
        chunk.data = getNBytes(inputStream, chunk.length);  // The chunk data.
        chunk.crc = getLong(inputStream);                   // CRC of the type and data chunks.

        CRC32 crc = new CRC32();
        crc.update(chunk.type, 0, 4);
        crc.update(chunk.data, 0, (int) chunk.length);
        if (crc.getValue() != chunk.crc) {
            throw new Exception("Chunk has bad CRC.");
        }
        return chunk;
    }

    private long getLong(InputStream inputStream) throws Exception {
        byte[] buf = getNBytes(inputStream, 4);
        return (toIntValue(buf, 0) & 0x00000000ffffffffL);
    }

    private byte[] getNBytes(InputStream inputStream, long length) throws Exception {
        byte[] buf = new byte[(int) length];
        if (inputStream.read(buf, 0, buf.length) == -1) {
            throw new Exception("Error reading input stream!");
        }
        return buf;
    }

    private int toIntValue(byte[] buf, int off) {
        return (buf[off] & 0xff) << 24 |
                (buf[off + 1] & 0xff) << 16 |
                (buf[off + 2] & 0xff) << 8 |
                (buf[off + 3] & 0xff);
    }

    // Truecolor Image with Bit Depth == 16
    private byte[] getImageColorType2BitDepth16(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        byte[] filters = new byte[this.h];
        int bytesPerLine = 6 * this.w + 1;
        int k = 0;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                filters[j++] = buf[i];
            } else {
                image[k++] = buf[i];
            }
        }
        applyFilters(filters, image, this.w, this.h, 6);
        return image;
    }

    // Truecolor Image with Bit Depth == 8
    private byte[] getImageColorType2BitDepth8(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        byte[] filters = new byte[this.h];
        int bytesPerLine = 3 * this.w + 1;
        int k = 0;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                filters[j++] = buf[i];
            } else {
                image[k++] = buf[i];
            }
        }
        applyFilters(filters, image, this.w, this.h, 3);
        return image;
    }

    // Truecolor Image with Alpha Transparency
    private byte[] getImageColorType6BitDepth8(byte[] buf) {
        byte[] idata = new byte[3 * this.w * this.h];   // Image data
        byte[] alpha = new byte[this.w * this.h];       // Alpha values
        byte[] image = new byte[4 * this.w * this.h];
        byte[] filters = new byte[this.h];
        int bytesPerLine = 4 * this.w + 1;
        int k = 0;
        int j = 0;
        int i = 0;
        for (; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                filters[j++] = buf[i];
            } else {
                image[k++] = buf[i];
            }
        }
        applyFilters(filters, image, this.w, this.h, 4);
        k = 0;
        j = 0;
        i = 0;
        while (i < image.length) {
            idata[j++] = image[i++];
            idata[j++] = image[i++];
            idata[j++] = image[i++];
            alpha[k++] = image[i++];
        }
        deflatedAlphaData = Compressor.deflate(alpha);
        return idata;
    }

    // Indexed-color image with bit depth == 8
    // Each value is a palette index; a PLTE chunk shall appear.
    private byte[] getImageColorType3BitDepth8(byte[] buf) {
        byte[] image = new byte[3 * (this.w * this.h)];
        byte[] filters = new byte[this.h];
        byte[] alpha = null;
        if (tRNS != null) {
            alpha = new byte[this.w * this.h];
            Arrays.fill(alpha, (byte) 0xff);
        }

        int bytesPerLine = this.w + 1;
        int m = 0;
        int n = 0;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                filters[m++] = buf[i];
            } else {
                int k = ((int) buf[i]) & 0xff;
                if (tRNS != null && k < tRNS.length) {
                    alpha[n] = tRNS[k];
                }
                n++;
                image[j++] = pLTE[3*k];
                image[j++] = pLTE[3*k + 1];
                image[j++] = pLTE[3*k + 2];
            }
        }
        applyFilters(filters, image, this.w, this.h, 3);
        if (tRNS != null) {
            deflatedAlphaData = Compressor.deflate(alpha);
        }

        return image;
    }

    // Indexed Image with Bit Depth == 4
    private byte[] getImageColorType3BitDepth4(byte[] buf) {
        byte[] image = new byte[6 * (buf.length - this.h)];
        int bytesPerLine = this.w / 2 + 1;
        if (this.w % 2 > 0) {
            bytesPerLine += 1;
        }

        int k = 0;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                // Skip the filter byte.
                continue;
            }

            int l = buf[i];
            k = 3 * ((l >> 4) & 0x0000000f);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;
            k = 3 * (l & 0x0000000f);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];
        }

        return image;
    }

    // Indexed Image with Bit Depth == 2
    private byte[] getImageColorType3BitDepth2(byte[] buf) {
        byte[] image = new byte[12 * (buf.length - this.h)];
        int bytesPerLine = this.w / 4 + 1;
        if (this.w % 4 > 0) {
            bytesPerLine += 1;
        }

        int j = 0;
        int k;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                // Skip the filter byte.
                continue;
            }

            int l = buf[i];

            k = 3 * ((l >> 6) & 0x00000003);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 4) & 0x00000003);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 2) & 0x00000003);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * (l & 0x00000003);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];
        }

        return image;
    }

    // Indexed Image with Bit Depth == 1
    private byte[] getImageColorType3BitDepth1(byte[] buf) {
        byte[] image = new byte[24 * (buf.length - this.h)];
        int bytesPerLine = this.w / 8 + 1;
        if (this.w % 8 > 0) {
            bytesPerLine += 1;
        }

        int k;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                // Skip the filter byte.
                continue;
            }

            int l = buf[i];

            k = 3 * ((l >> 7) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 6) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 5) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 4) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 3) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 2) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * ((l >> 1) & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];

            if (j % (3 * this.w) == 0) continue;

            k = 3 * (l & 0x00000001);
            image[j++] = pLTE[k];
            image[j++] = pLTE[k + 1];
            image[j++] = pLTE[k + 2];
        }

        return image;
    }

    // Grayscale Image with Bit Depth == 16
    private byte[] getImageColorType0BitDepth16(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        byte[] filters = new byte[this.h];
        int bytesPerLine = 2 * this.w + 1;
        int k = 0;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                filters[j] = buf[i];
                j += 1;
            } else {
                image[k] = buf[i];
                k += 1;
            }
        }
        applyFilters(filters, image, this.w, this.h, 2);
        return image;
    }

    // Grayscale Image with Bit Depth == 8
    private byte[] getImageColorType0BitDepth8(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        byte[] filters = new byte[this.h];
        int bytesPerLine = this.w + 1;
        int k = 0;
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine == 0) {
                filters[j++] = buf[i];
            } else {
                image[k++] = buf[i];
            }
        }
        applyFilters(filters, image, this.w, this.h, 1);
        return image;
    }

    // Grayscale Image with Bit Depth == 4
    private byte[] getImageColorType0BitDepth4(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        int bytesPerLine = this.w / 2 + 1;
        if (this.w % 2 > 0) {
            bytesPerLine += 1;
        }
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine != 0) {
                image[j++] = buf[i];
            }
        }
        return image;
    }

    // Grayscale Image with Bit Depth == 2
    private byte[] getImageColorType0BitDepth2(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        int bytesPerLine = this.w / 4 + 1;
        if (this.w % 4 > 0) {
            bytesPerLine += 1;
        }
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine != 0) {
                image[j++] = buf[i];
            }
        }
        return image;
    }

    // Grayscale Image with Bit Depth == 1
    private byte[] getImageColorType0BitDepth1(byte[] buf) {
        byte[] image = new byte[buf.length - this.h];
        int bytesPerLine = this.w / 8 + 1;
        if (this.w % 8 > 0) {
            bytesPerLine += 1;
        }
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            if (i % bytesPerLine != 0) {
                image[j++] = buf[i];
            }
        }
        return image;
    }

    private void applyFilters(
            byte[] filters,
            byte[] image,
            int width,
            int height,
            int bytesPerPixel) {
        int bytesPerLine = width * bytesPerPixel;
        byte filter = 0x00;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < bytesPerLine; col++) {
                if (col == 0) {
                    filter = filters[row];
                }
                if (filter == 0x00) {           // None
                    continue;
                }

                int a = 0;                      // The pixel on the left
                if (col >= bytesPerPixel) {
                    a = image[(bytesPerLine * row + col) - bytesPerPixel] & 0xff;
                }
                int b = 0;                      // The pixel above
                if (row > 0) {
                    b = image[bytesPerLine * (row - 1) + col] & 0xff;
                }
                int c = 0;                      // The pixel diagonally left above
                if (col >= bytesPerPixel && row > 0) {
                    c = image[(bytesPerLine * (row - 1) + col) - bytesPerPixel] & 0xff;
                }

                int index = bytesPerLine * row + col;
                if (filter == 0x01) {           // Sub
                    image[index] += (byte) a;
                } else if (filter == 0x02) {      // Up
                    image[index] += (byte) b;
                } else if (filter == 0x03) {      // Average
                    image[index] += (byte) Math.floor((a + b) / 2.0);
                } else if (filter == 0x04) {      // Paeth
                    int p = a + b - c;
                    int pa = Math.abs(p - a);
                    int pb = Math.abs(p - b);
                    int pc = Math.abs(p - c);
                    if (pa <= pb && pa <= pc) {
                        image[index] += (byte) a;
                    } else if (pb <= pc) {
                        image[index] += (byte) b;
                    } else {
                        image[index] += (byte) c;
                    }
                }
            }
        }
    }

    private byte[] appendIdatChunk(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return array2;
        } else if (array2 == null) {
            return array1;
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }
/*
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream(args[0]);
        PNGImage png = new PNGImage(fis);
        byte[] image = png.getData();
        byte[] alpha = png.getAlpha();
        int w = png.getWidth();
        int h = png.getHeight();
        int c = png.getColorType();
        fis.close();

        String fileName = args[0].substring(0, args[0].lastIndexOf("."));
        FileOutputStream fos = new FileOutputStream(fileName + ".jet");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        writeInt(w, bos);   // Width
        writeInt(h, bos);   // Height
        bos.write(c);       // Color Space
        if (alpha != null) {
            bos.write(1);
            writeInt(alpha.length, bos);
            bos.write(alpha);
        } else {
            bos.write(0);
        }
        writeInt(image.length, bos);
        bos.write(image);
        bos.flush();
        bos.close();
    }

    private static void writeInt(int i, OutputStream os) throws IOException {
        os.write((i >> 24) & 0xff);
        os.write((i >> 16) & 0xff);
        os.write((i >>  8) & 0xff);
        os.write((i >>  0) & 0xff);
    }
*/
}   // End of PNGImage.java
