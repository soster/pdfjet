package com.pdfjet;

class StandardFont {
    protected String name;
    protected int bBoxLLx;
    protected int bBoxLLy;
    protected int bBoxURx;
    protected int bBoxURy;
    protected int underlinePosition;
    protected int underlineThickness;
    protected int[][] metrics;

    protected static StandardFont getInstance(CoreFont paramCoreFont) {
        StandardFont localStandardFont = new StandardFont();
        switch (paramCoreFont) {
            case COURIER:
                localStandardFont.name = "Courier";
                localStandardFont.bBoxLLx = -23;
                localStandardFont.bBoxLLy = 65286;
                localStandardFont.bBoxURx = 715;
                localStandardFont.bBoxURy = 805;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Courier.metrics;
                break;

            case COURIER_BOLD:
                localStandardFont.name = "Courier-Bold";
                localStandardFont.bBoxLLx = -113;
                localStandardFont.bBoxLLy = 65286;
                localStandardFont.bBoxURx = 749;
                localStandardFont.bBoxURy = 801;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Courier_Bold.metrics;
                break;

            case COURIER_OBLIQUE:
                localStandardFont.name = "Courier-Oblique";
                localStandardFont.bBoxLLx = -27;
                localStandardFont.bBoxLLy = 65286;
                localStandardFont.bBoxURx = 849;
                localStandardFont.bBoxURy = 805;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Courier_Oblique.metrics;
                break;

            case COURIER_BOLD_OBLIQUE:
                localStandardFont.name = "Courier-BoldOblique";
                localStandardFont.bBoxLLx = -57;
                localStandardFont.bBoxLLy = 65286;
                localStandardFont.bBoxURx = 869;
                localStandardFont.bBoxURy = 801;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Courier_BoldOblique.metrics;
                break;

            case HELVETICA:
                localStandardFont.name = "Helvetica";
                localStandardFont.bBoxLLx = 65370;
                localStandardFont.bBoxLLy = 65311;
                localStandardFont.bBoxURx = 1000;
                localStandardFont.bBoxURy = 931;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Helvetica.metrics;
                break;

            case HELVETICA_BOLD:
                localStandardFont.name = "Helvetica-Bold";
                localStandardFont.bBoxLLx = 65366;
                localStandardFont.bBoxLLy = 65308;
                localStandardFont.bBoxURx = 1003;
                localStandardFont.bBoxURy = 962;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Helvetica_Bold.metrics;
                break;

            case HELVETICA_OBLIQUE:
                localStandardFont.name = "Helvetica-Oblique";
                localStandardFont.bBoxLLx = 65366;
                localStandardFont.bBoxLLy = 65311;
                localStandardFont.bBoxURx = 1116;
                localStandardFont.bBoxURy = 931;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Helvetica_Oblique.metrics;
                break;

            case HELVETICA_BOLD_OBLIQUE:
                localStandardFont.name = "Helvetica-BoldOblique";
                localStandardFont.bBoxLLx = 65362;
                localStandardFont.bBoxLLy = 65308;
                localStandardFont.bBoxURx = 1114;
                localStandardFont.bBoxURy = 962;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Helvetica_BoldOblique.metrics;
                break;

            case TIMES_ROMAN:
                localStandardFont.name = "Times-Roman";
                localStandardFont.bBoxLLx = 65368;
                localStandardFont.bBoxLLy = 65318;
                localStandardFont.bBoxURx = 1000;
                localStandardFont.bBoxURy = 898;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Times_Roman.metrics;
                break;

            case TIMES_BOLD:
                localStandardFont.name = "Times-Bold";
                localStandardFont.bBoxLLx = 65368;
                localStandardFont.bBoxLLy = 65318;
                localStandardFont.bBoxURx = 1000;
                localStandardFont.bBoxURy = 935;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Times_Bold.metrics;
                break;

            case TIMES_ITALIC:
                localStandardFont.name = "Times-Italic";
                localStandardFont.bBoxLLx = 65367;
                localStandardFont.bBoxLLy = 65319;
                localStandardFont.bBoxURx = 1010;
                localStandardFont.bBoxURy = 883;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Times_Italic.metrics;
                break;

            case TIMES_BOLD_ITALIC:
                localStandardFont.name = "Times-BoldItalic";
                localStandardFont.bBoxLLx = 65336;
                localStandardFont.bBoxLLy = 65318;
                localStandardFont.bBoxURx = 996;
                localStandardFont.bBoxURy = 921;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Times_BoldItalic.metrics;
                break;

            case SYMBOL:
                localStandardFont.name = "Symbol";
                localStandardFont.bBoxLLx = 65356;
                localStandardFont.bBoxLLy = 65243;
                localStandardFont.bBoxURx = 1090;
                localStandardFont.bBoxURy = 1010;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = Symbol.metrics;
                break;

            case ZAPF_DINGBATS:
                localStandardFont.name = "ZapfDingbats";
                localStandardFont.bBoxLLx = -1;
                localStandardFont.bBoxLLy = 65393;
                localStandardFont.bBoxURx = 981;
                localStandardFont.bBoxURy = 820;
                localStandardFont.underlinePosition = -100;
                localStandardFont.underlineThickness = 50;
                localStandardFont.metrics = ZapfDingbats.metrics;
        }


        return localStandardFont;
    }
}


