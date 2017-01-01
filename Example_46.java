import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_46.java
 *
 */
public class Example_46 {

    public Example_46() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_46.pdf")),
                        Compliance.PDF_UA);

        Font f1 = null;
        Font f2 = null;

        f1 = new Font(
                pdf,
                getClass().getResourceAsStream(
                        "fonts/DejaVu/ttf/DejaVuLGCSerif-Bold.ttf.stream"),
                Font.STREAM);

        f2 = new Font(
                pdf,
                getClass().getResourceAsStream(
                        "fonts/DejaVu/ttf/DejaVuLGCSerif.ttf.stream"),
                Font.STREAM);
/*
Used for performance testing:
        String path = "fonts/Android/";
        f1 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans-Bold.ttf"));

        f2 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans.ttf"));

        f1 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans-Bold.ttf.stream"),
                Font.STREAM);

        f2 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans.ttf.stream"),
                Font.STREAM);
*/
        f1.setSize(14f);
        f2.setSize(14f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        List<Paragraph> paragraphs = new ArrayList<Paragraph>();

        Paragraph paragraph = new Paragraph()
                .add(new TextLine(f1,
"Όταν ο Βαρουφάκης δήλωνε κατηγορηματικά πως δεν θα ασχοληθεί ποτέ με την πολιτική (Video)"));
        paragraphs.add(paragraph);

        paragraph = new Paragraph()
                .add(new TextLine(f2,
"Τις τελευταίες μέρες αδιαμφισβήτητα ο Γιάνης Βαρουφάκης είναι  ο πιο πολυσυζητημένος πολιτικός στην Ευρώπη και όχι μόνο. Κι όμως, κάποτε ο νέος υπουργός Οικονομικών δήλωνε κατηγορηματικά πως δεν πρόκειται ποτέ να εμπλακεί σε αυτό το χώρο."));
        paragraphs.add(paragraph);

        paragraph = new Paragraph()
                .add(new TextLine(f2,
"Η συγκεκριμένη του δήλωση ήταν σε συνέντευξή που είχε παραχωρήσει στην εκπομπή «Ευθέως» και στον Κωνσταντίνο Μπογδάνο, όταν στις 13 Δεκεμβρίου του 2012 ο νυν υπουργός Οικονομικών δήλωνε ότι δεν υπάρχει περίπτωση να ασχοληθεί με την πολιτική, γιατί θα τον διέγραφε οποιοδήποτε κόμμα."))
                .add(new TextLine(f2,
"Συγκεκριμένα, μετά από το 43ο λεπτό του βίντεο, ο δημοσιογράφος τον ρώτησε αν θα ασχολιόταν ποτέ με την πολιτική, με την απάντηση του κ. Βαρουφάκη να είναι κατηγορηματική: «Όχι, ποτέ, ποτέ»."));
        paragraphs.add(paragraph);

        paragraph = new Paragraph()
                .add(new TextLine(f2,
"«Μα ποτέ δεν ασχολήθηκα με την πολιτική. Ασχολούμαι ως πολίτης και ως συμμετέχων στον δημόσιο διάλογο. Και είναι κάτι που θα κάνω πάντα. Καταρχήν, αν μπω σε ένα πολιτικό κόμμα μέσα σε μία εβδομάδα θα με έχει διαγράψει, όποιο κι αν είναι αυτό», εξηγούσε τότε, ενώ πρόσθετε ότι δεν μπορεί να ακολουθήσει κομματική γραμμή."));
        paragraphs.add(paragraph);

        Text textArea = new Text(paragraphs);
        textArea.setLocation(70f, 70f);
        textArea.setWidth(500f);
        textArea.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        long t0 = System.currentTimeMillis();
        new Example_46();
        long t1 = System.currentTimeMillis();
        // System.out.println(t1 - t0);
    }

}   // End of Example_46.java
