using System;
using System.IO;
using System.Text;
using System.Collections.Generic;
using System.IO.Compression;

using PDFjet.NET;


namespace util {
public class PNGtoJET {

    public static void Main(String[] args) {
        FileStream fis = new FileStream(args[0], FileMode.Open, FileAccess.Read);
        PNGImage png = new PNGImage(fis);
        byte[] image = png.GetData();
        byte[] alpha = png.GetAlpha();
        int w = png.GetWidth();
        int h = png.GetHeight();
        int c = png.GetColorType();
        fis.Dispose();

        String fileName = args[0].Substring(0, args[0].LastIndexOf("."));
        FileStream fos = new FileStream(fileName + ".jet", FileMode.Create);
        BufferedStream bos = new BufferedStream(fos);
        WriteInt(w, bos);           // Width
        WriteInt(h, bos);           // Height
        bos.WriteByte((byte) c);    // Color Space
        if (alpha != null) {
            bos.WriteByte((byte) 1);
            WriteInt(alpha.Length, bos);
            bos.Write(alpha, 0, alpha.Length);
        }
        else {
            bos.WriteByte((byte) 0);
        }
        WriteInt(image.Length, bos);
        bos.Write(image, 0, image.Length);
        bos.Flush();
        bos.Dispose();
    }


    private static void WriteInt(int i, Stream os) {
        os.WriteByte((byte) (i >> 24));
        os.WriteByte((byte) (i >> 16));
        os.WriteByte((byte) (i >>  8));
        os.WriteByte((byte) (i >>  0));
    }

}   // End of PNGtoJET.cs
}   // End of namespace util
