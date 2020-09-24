import java.io.*;

import com.pdfjet.*;


/**
 *  Example_51.java
 *
 */
public class Example_51 {

    public Example_51() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_51.pdf")),
                        Compliance.PDF_UA);

        Font f1 = new Font(
                pdf,
                getClass().getResourceAsStream(
                        "fonts/DejaVu/ttf/DejaVuLGCSerif.ttf.stream"),
                Font.STREAM);

        Page page = new Page(pdf, Letter.PORTRAIT);

        Bookmark toc = new Bookmark(pdf);
        TextLine pref = null;
        TextLine text = null;

        float x = 70f;
        float y = 50f;
        float offset = 50f;

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "This is a test!");
        // text.setLocation(x + offset, y);
        text.setLocation(x, y);
        // toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        toc.addBookmark(page, text.getY(), text.getText());
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "General");
        text.setLocation(x + offset, y);
        toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "File Header");
        text.setLocation(x + offset, y);
        toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "File Body");
        text.setLocation(x + offset, y);
        toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "Cross-Reference Table");
        text.setLocation(x + offset, y);
        toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        page = new Page(pdf, Letter.PORTRAIT);

        y = 50f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "File Trailer");
        text.setLocation(x + offset, y);
        toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "Incremental Updates");
        text.setLocation(x + offset, y);
        Bookmark bm = toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "Hello");
        text.setLocation(x + offset, y);
        bm = bm.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "World");
        text.setLocation(x + offset, y);
        bm = bm.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "Yahoo!!");
        text.setLocation(x + offset, y);
        bm.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "Test Test Test ...");
        text.setLocation(x + offset, y);
        bm.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        bm = bm.getParent();
        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "Let's see ...");
        text.setLocation(x + offset, y);
        bm.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "One more item.");
        text.setLocation(x + offset, y);
        toc.addBookmark(page, text.getY(), text.getText()).autoNumber(pref);
        pref.drawOn(page);
        text.drawOn(page);

        y += 30f;
        pref = new TextLine(f1);
        pref.setLocation(x, y);
        text = new TextLine(f1, "The End :)");
        text.setLocation(x, y);
        toc.addBookmark(page, text.getY(), text.getText());
        pref.drawOn(page);
        text.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_51();
    }

}   // End of Example_51.java
