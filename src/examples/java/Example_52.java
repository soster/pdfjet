import java.io.*;

import com.pdfjet.*;


/**
 *  Example_52.java
 *
 */
public class Example_52 {

    public Example_52() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_52.pdf")),
                        Compliance.PDF_UA);

        Font f1 = new Font(
                pdf,
                getClass().getResourceAsStream(
                        "fonts/DejaVu/ttf/DejaVuLGCSerif.ttf.stream"),
                Font.STREAM);

        Page page = new Page(pdf, Letter.PORTRAIT);

        Bookmark toc = new Bookmark(pdf);
        Title title = null;

        float x = 70f;
        float y = 50f;
        float offset = 50f;

        y += 30f;
        title = new Title(f1, "This is a test!", x, y);
        toc.addBookmark(page, title);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "General", x, y).setOffset(offset);
        toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "File Header", x, y).setOffset(offset);
        toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "File Body", x, y).setOffset(offset);
        toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "Cross-Reference Table", x, y).setOffset(offset);
        toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        page = new Page(pdf, Letter.PORTRAIT);

        y = 50f;
        title = new Title(f1, "File Trailer", x, y).setOffset(offset);
        toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "Incremental Updates", x, y).setOffset(offset);
        Bookmark bm = toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "Hello", x, y).setOffset(offset);
        bm = bm.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "World", x, y).setOffset(offset);
        bm = bm.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "Yahoo!!", x, y).setOffset(offset);
        bm.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "Test Test Test ...", x, y).setOffset(offset);
        bm.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        bm = bm.getParent();
        title = new Title(f1, "Let's see ...", x, y).setOffset(offset);
        bm.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "One more item.", x, y).setOffset(offset);
        toc.addBookmark(page, title).autoNumber(title.prefix);
        title.drawOn(page);

        y += 30f;
        title = new Title(f1, "The End :)", x, y);
        toc.addBookmark(page, title);
        title.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_52();
    }

}   // End of Example_52.java
