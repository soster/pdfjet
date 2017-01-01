import java.io.*;

import com.pdfjet.*;


/**
 *  Example_07.java
 *
 */
public class Example_07 {

    public Example_07() throws Exception {

        boolean commercial = false;

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_07.pdf")),
                        Compliance.PDF_A_1B);
        pdf.setPageLayout(PageLayout.SINGLE_PAGE);
        pdf.setPageMode(PageMode.FULL_SCREEN);

        String path = "fonts/DejaVu/ttf/";
        Font f1 = new Font(
                pdf,
                getClass().getResourceAsStream(path + "DejaVuLGCSerif.ttf.stream"),
                Font.STREAM);

        Font f2 = new Font(
                pdf,
                getClass().getResourceAsStream(path + "DejaVuLGCSerif-Italic.ttf.stream"),
                Font.STREAM);

        Page page = new Page(pdf, Letter.PORTRAIT);

        f1.setSize(15f);
        f2.setSize(15f);
        
        float x_pos = 70f;
        float y_pos = 70f;
        TextLine text = new TextLine(f1);
        text.setLocation(x_pos, y_pos);
        StringBuilder buf = new StringBuilder();

        for (int i = 0x20; i < 0x7F; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x_pos, y_pos += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            buf.append((char) i);
        }

        text.setText(buf.toString());
        text.setLocation(x_pos, y_pos += 24f);
        text.drawOn(page);

        y_pos += 24f;
        buf = new StringBuilder();
        for (int i = 0x390; i < 0x3EF; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x_pos, y_pos += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            if (i == 0x3A2
                    || i == 0x3CF
                    || i == 0x3D0
                    || i == 0x3D3
                    || i == 0x3D4
                    || i == 0x3D5
                    || i == 0x3D7
                    || i == 0x3D8
                    || i == 0x3D9
                    || i == 0x3DA
                    || i == 0x3DB
                    || i == 0x3DC
                    || i == 0x3DD
                    || i == 0x3DE
                    || i == 0x3DF
                    || i == 0x3E0
                    || i == 0x3EA
                    || i == 0x3EB
                    || i == 0x3EC
                    || i == 0x3ED
                    || i == 0x3EF) {
                // Replace .notdef with space to generate PDF/A compliant PDF
                buf.append((char) 0x0020);
            }
            else {
                buf.append((char) i);
            }
        }

        y_pos += 24f;
        buf = new StringBuilder();
        for (int i = 0x410; i <= 0x46F; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x_pos, y_pos += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            buf.append((char) i);
        }


        x_pos = 370f;
        y_pos = 70f;
        text = new TextLine(f2);
        text.setLocation(x_pos, y_pos);
        buf = new StringBuilder();
        for (int i = 0x20; i < 0x7F; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x_pos, y_pos += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            buf.append((char) i);
        }
        text.setText(buf.toString());
        text.setLocation(x_pos, y_pos += 24f);
        text.drawOn(page);

        y_pos += 24f;
        buf = new StringBuilder();
        for (int i = 0x390; i < 0x3EF; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x_pos, y_pos += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            if (i == 0x3A2
                    || i == 0x3CF
                    || i == 0x3D0
                    || i == 0x3D3
                    || i == 0x3D4
                    || i == 0x3D5
                    || i == 0x3D7
                    || i == 0x3D8
                    || i == 0x3D9
                    || i == 0x3DA
                    || i == 0x3DB
                    || i == 0x3DC
                    || i == 0x3DD
                    || i == 0x3DE
                    || i == 0x3DF
                    || i == 0x3E0
                    || i == 0x3EA
                    || i == 0x3EB
                    || i == 0x3EC
                    || i == 0x3ED
                    || i == 0x3EF) {
                // Replace .notdef with space to generate PDF/A compliant PDF
                buf.append((char) 0x0020);
            }
            else {
                buf.append((char) i);
            }
        }
        
        y_pos += 24f;
        buf = new StringBuilder();
        for (int i = 0x410; i < 0x46F; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x_pos, y_pos += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            buf.append((char) i);
        }

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        long time0 = System.currentTimeMillis();
        new Example_07();
        long time1 = System.currentTimeMillis();
        // System.out.println(time1 - time0);
    }

}   // End of Example_07.java
