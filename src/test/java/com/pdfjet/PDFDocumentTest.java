package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class PDFDocumentTest extends PDFTestBase {

    @Test
    public void testBasicPDFCreation() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Font font = new Font(pdf, CoreFont.HELVETICA);
        page.setTextFont(font);
        page.drawString(font, "Hello PDFjet", 100f, 100f);

        pdf.complete();

        String content = new String(os.toByteArray(), "ISO-8859-1");
        assertTrue("PDF must start with %PDF-", content.startsWith("%PDF-"));
        assertTrue("PDF must end with %%EOF", content.trim().endsWith("%%EOF"));
        assertTrue("PDF must reference Helvetica font", content.contains("/Helvetica"));
    }

    @Test
    public void testMultiPagePDF() throws Exception {
        ByteArrayOutputStream singlePage = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(singlePage);
        new Page(pdf1, A4.PORTRAIT);
        pdf1.complete();

        ByteArrayOutputStream multiPage = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(multiPage);
        Font font = new Font(pdf2, CoreFont.HELVETICA);
        for (int i = 0; i < 3; i++) {
            Page page = new Page(pdf2, A4.PORTRAIT);
            page.drawString(font, "Page " + (i + 1), 100f, 100f);
        }
        pdf2.complete();

        assertTrue("Multi-page PDF should be larger than single-page",
                multiPage.toByteArray().length > singlePage.toByteArray().length);

        String content = new String(multiPage.toByteArray(), "ISO-8859-1");
        assertTrue("Multi-page PDF references Helvetica", content.contains("/Helvetica"));
    }

    @Test
    public void testPDFMetadata() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        pdf.setTitle("Test Document");
        pdf.setAuthor("Test Author");
        pdf.setSubject("Test Subject");
        new Page(pdf, Letter.PORTRAIT);
        pdf.complete();

        String content = new String(os.toByteArray(), "ISO-8859-1");
        assertTrue("Title written to info object", content.contains("Test Document"));
        assertTrue("Author written to info object", content.contains("Test Author"));
        assertTrue("Subject written to info object", content.contains("Test Subject"));
    }

}
