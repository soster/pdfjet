import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_27.java
 *
 */
public class Example_27 {

    public Example_27() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_27.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        // Thai font
        Font f1 = new Font(pdf,
                getClass().getResourceAsStream("fonts/Android/DroidSansThai.ttf"));
        f1.setSize(14f);

        // Latin font
        Font f2 = new Font(pdf,
                getClass().getResourceAsStream("fonts/Android/DroidSans.ttf"));
        f2.setSize(12f);

        // Hebrew font
        Font f3 = new Font(pdf,
                getClass().getResourceAsStream("fonts/Android/DroidSansHebrew-Regular.ttf"));
        f3.setSize(12f);

        // Arabic font
        Font f4 = new Font(pdf,
                // getClass().getResourceAsStream("fonts/Android/DroidSansArabic.ttf"));
                getClass().getResourceAsStream("fonts/Android/DroidNaskh-Regular.ttf"));
        f4.setSize(12f);

        float x = 50f;
        float y = 50f;

        TextLine text = new TextLine(f1);
        text.setFallbackFont(f2);
        text.setLocation(x, y);

        StringBuilder buf = new StringBuilder();
        for (int i = 0x0E01; i < 0x0E5B; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x, y += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            if (i > 0x0E30 && i < 0x0E3B) {
                buf.append("\u0E01");
            }
            if (i > 0x0E46 && i < 0x0E4F) {
                buf.append("\u0E2D");
            }
            buf.append((char) i);
        }

        text.setText(buf.toString());
        text.setLocation(x, y += 20f);
        text.drawOn(page);

        y += 20f;
        
        String str = "\u0E1C\u0E1C\u0E36\u0E49\u0E07 abc 123";
        text.setText(str);
        text.setLocation(x, y);
        text.drawOn(page);

        y += 20f;

        str = "כך נראית תחתית הטבלה עם סיום הפלייאוף התחתון:";
        str = Bidi.reorderVisually(str);
        TextLine textLine = new TextLine(f3, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f3.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = "10. הפועל כפר סבא 38 נקודות (הפרש שערים 14-)";
        str = Bidi.reorderVisually(str);
        textLine = new TextLine(f3, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f3.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = "11. הפועל קריית שמונה 36 נקודות (הפרש שערים 7-)";
        str = Bidi.reorderVisually(str);
        textLine = new TextLine(f3, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f3.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = "12. הפועל חיפה 34 נקודות (הפרש שערים 10-)";
        str = Bidi.reorderVisually(str);
        textLine = new TextLine(f3, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f3.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = "13. הפועל עכו 34 נקודות (הפרש שערים 21-)";
        str = Bidi.reorderVisually(str);
        textLine = new TextLine(f3, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f3.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        y += 40f;

        str = Bidi.reorderVisually(
                "قالت شركة PSA بيجو ستروين الفرنسية وشريكتها الصينية شركة دونغفينغ موترز الاربعاء إنهما اتفقتا");
        textLine = new TextLine(f4, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f4.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = Bidi.reorderVisually(
                "على التعاون في تطوير السيارات التي تعمل بالطاقة الكهربائية اعتبارا من عام 2019.");
        textLine = new TextLine(f4, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f4.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = Bidi.reorderVisually(
                "وجاء في تصريح اصدرته في باريس الشركة الفرنسية ان الشركتين ستنتجان نموذجا كهربائيا مشتركا تستخدمه كل");
        textLine = new TextLine(f4, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f4.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = Bidi.reorderVisually(
                "من بيجو وسيتروين ودونغفينغ.");
        textLine = new TextLine(f4, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f4.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        str = Bidi.reorderVisually(
                "وقالت إن الخطة تهدف الى تحقيق عائد يزيد على 100 مليار يوان (15,4 مليار دولار) بحلول عام 2020.");
        textLine = new TextLine(f4, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(600f - f4.stringWidth(f2, str), y += 20f);
        textLine.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_27();
    }

}   // End of Example_27.java
