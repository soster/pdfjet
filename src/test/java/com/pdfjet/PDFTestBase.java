package com.pdfjet;

import java.io.*;

public abstract class PDFTestBase {

    /** Creates a writable PDF backed by a ByteArrayOutputStream for tests that don't check output bytes. */
    protected PDF writablePDF() throws Exception {
        return new PDF(new ByteArrayOutputStream());
    }

}
