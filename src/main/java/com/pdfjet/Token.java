package com.pdfjet;

public class Token {
    public static final byte[] beginDictionary = "<<\n".getBytes();
    public static final byte[] endDictionary = ">>\n".getBytes();
    public static final byte[] stream = "stream\n".getBytes();
    public static final byte[] endstream = "\nendstream\n".getBytes();
    public static final byte[] newobj = " 0 obj\n".getBytes();
    public static final byte[] endobj = "endobj\n".getBytes();
    public static final byte[] objRef = " 0 R\n".getBytes();
    public static final byte[] beginText = "BT\n".getBytes();
    public static final byte[] endText = "ET\n".getBytes();
    public static final byte[] count = "/Count ".getBytes();
    public static final byte[] length = "/Length ".getBytes();
    public static final byte space = ' ';
    public static final byte newline = '\n';
    public static final byte[] beginStructElem = "<<\n/Type /StructElem /S /".getBytes();
    public static final byte[] endStructElem = ">\n>>\n".getBytes();
    public static final byte[] beginAnnotation = "/K <</Type /OBJR /Obj ".getBytes();
    public static final byte[] endAnnotation = " 0 R>>".getBytes();
    public static final byte[] actualText = ">\n/ActualText <".getBytes();
    public static final byte[] altDescription = ")\n/Alt <".getBytes();

    public static final byte[] P = "\n/P ".getBytes();
    public static final byte[] objRefPg = " 0 R /Pg ".getBytes();
    public static final byte[] K = "/K ".getBytes();
    public static final byte[] lang = "\n/Lang (".getBytes();
}
