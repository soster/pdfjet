import java.io.*;

import com.pdfjet.*;


/**
 *  Example_25.java
 *
 */
public class Example_25 {

    public Example_25() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_25.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f3 = new Font(pdf, CoreFont.HELVETICA);
        Font f4 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f5 = new Font(pdf, CoreFont.HELVETICA);
        Font f6 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        
        Page page = new Page(pdf, Letter.PORTRAIT);
        
        CompositeTextLine composite = new CompositeTextLine(50f, 50f);
        composite.setFontSize(14f);

        TextLine text1 = new TextLine(f1, "C");
        TextLine text2 = new TextLine(f2, "6");
        TextLine text3 = new TextLine(f3, "H");        
        TextLine text4 = new TextLine(f4, "12");
        TextLine text5 = new TextLine(f5, "O");        
        TextLine text6 = new TextLine(f6, "6");

        text1.setColor(Color.dodgerblue);
        text3.setColor(Color.dodgerblue);
        text5.setColor(Color.dodgerblue);

        text2.setTextEffect(Effect.SUBSCRIPT);
        text4.setTextEffect(Effect.SUBSCRIPT);
        text6.setTextEffect(Effect.SUBSCRIPT);
        
        composite.addComponent(text1);
        composite.addComponent(text2);
        composite.addComponent(text3);
        composite.addComponent(text4);
        composite.addComponent(text5);
        composite.addComponent(text6);

        float[] xy = composite.drawOn(page);

        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);

        CompositeTextLine composite2 = new CompositeTextLine(50f, 100f);
        composite2.setFontSize(14f);

        text1 = new TextLine(f1, "SO");
        text2 = new TextLine(f2, "4");
        text3 = new TextLine(f4, "2-"); // Use bold font here        

        text2.setTextEffect(Effect.SUBSCRIPT);
        text3.setTextEffect(Effect.SUPERSCRIPT);

        composite2.addComponent(text1);
        composite2.addComponent(text2);
        composite2.addComponent(text3);

        composite2.drawOn(page);
        composite2.setLocation(100f, 150f);
        composite2.drawOn(page);

        float[] yy = composite2.getMinMax();
        Line line1 = new Line(50f, yy[0], 200f, yy[0]);
        Line line2 = new Line(50f, yy[1], 200f, yy[1]);
        line1.drawOn(page);
        line2.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_25();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_25.java
