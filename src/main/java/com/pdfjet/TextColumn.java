/**
 *  TextColumn.java
 *
Copyright 2023 Innovatics Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.pdfjet;

import java.util.*;
/**
 *  Used to create text column objects and draw them on a page.
 *
 *  Please see Example_10.
 */
public class TextColumn implements Drawable {
    protected int alignment = Align.LEFT;
    protected int rotate;
    private float x;    // This variable is set in the beginning and only reset after the drawOn
    private float y;    // This variable is set in the beginning and only reset after the drawOn
    private float w;
    private float h;
    private float x1;
    private float y1;
    private float lineHeight;
    private float spaceBetweenLines = 1f;
    private float spaceBetweenParagraphs = 2f;
    private final List<Paragraph> paragraphs;
    private boolean lineBetweenParagraphs = false;

    /**
     *  Create a text column object.
     *
     */
    public TextColumn() {
        this.paragraphs = new ArrayList<Paragraph>();
    }

    /**
     *  Create a text column object and set the rotation angle.
     *
     *  @param rotateByDegrees the specified rotation angle in degrees.
     *  @throws Exception  If an input or output exception occurred
     */
    public TextColumn(int rotateByDegrees) throws Exception {
        this.rotate = rotateByDegrees;
        if (rotate == 0 || rotate == 90 || rotate == 270) {
        } else {
            throw new Exception(
                    "Invalid rotation angle. Please use 0, 90 or 270 degrees.");
        }
        this.paragraphs = new ArrayList<Paragraph>();
    }

    /**
     *  Sets the lineBetweenParagraphs private variable value.
     *  If the value is set to true - an empty line will be inserted between the current and next paragraphs.
     *
     *  @param lineBetweenParagraphs the specified boolean value.
     */
    public void setLineBetweenParagraphs(boolean lineBetweenParagraphs) {
        this.lineBetweenParagraphs = lineBetweenParagraphs;
    }

    public void setSpaceBetweenLines(float spaceBetweenLines) {
        this.spaceBetweenLines = spaceBetweenLines;
    }

    public void setSpaceBetweenParagraphs(float spaceBetweenParagraphs) {
        this.spaceBetweenParagraphs = spaceBetweenParagraphs;
    }

    /**
     *  Sets the position of this text column on the page.
     *
     *  @param x the x coordinate of the top left corner of this text column when drawn on the page.
     *  @param y the y coordinate of the top left corner of this text column when drawn on the page.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Sets the position of this text column on the page.
     *
     *  @param x the x coordinate of the top left corner of this text column when drawn on the page.
     *  @param y the y coordinate of the top left corner of this text column when drawn on the page.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }

    /**
     *  Sets the position of this text column on the page.
     *
     *  @param x the x coordinate of the top left corner of this text column when drawn on the page.
     *  @param y the y coordinate of the top left corner of this text column when drawn on the page.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        this.x1 = x;
        this.y1 = y;
    }

    /**
     *  Sets the position of this text column on the page.
     *
     *  @param x the x coordinate of the top left corner of this text column when drawn on the page.
     *  @param y the y coordinate of the top left corner of this text column when drawn on the page.
     */
    public void setLocation(double x, double y) {
        setLocation((float) x, (float) y);
    }

    /**
     *  Sets the size of this text column.
     *
     *  @param w the width of this text column.
     *  @param h the height of this text column.
     */
    public void setSize(double w, double h) {
        setSize((float) w, (float) h);
    }

    /**
     *  Sets the size of this text column.
     *
     *  @param w the width of this text column.
     *  @param h the height of this text column.
     */
    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }

    /**
     *  Sets the desired width of this text column.
     *
     *  @param w the width of this text column.
     */
    public void setWidth(float w) {
        this.w = w;
    }

    /**
     *  Sets the text alignment.
     *
     *  @param alignment the specified alignment code. Supported values: Align.LEFT, Align.RIGHT. Align.CENTER and Align.JUSTIFY
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    /**
     *  Sets the spacing between the lines in this text column.
     *
     *  @param spacing the specified spacing value.
     */
    public void setLineSpacing(double spacing) {
        this.spaceBetweenLines = (float) spacing;
    }

    /**
     *  Sets the spacing between the lines in this text column.
     *
     *  @param spacing the specified spacing value.
     */
    public void setLineSpacing(float spacing) {
        this.spaceBetweenLines = spacing;
    }

    /**
     *  Adds a new paragraph to this text column.
     *
     *  @param paragraph the new paragraph object.
     */
    public void addParagraph(Paragraph paragraph) {
        this.paragraphs.add(paragraph);
    }

    /**
     *  Removes the last paragraph added to this text column.
     *
     */
    public void removeLastParagraph() {
        if (this.paragraphs.size() >= 1) {
            this.paragraphs.remove(this.paragraphs.size() - 1);
        }
    }

    /**
     *  Returns dimension object containing the width and height of this component.
     *  Please see Example_29.
     *
     *  @return dimension object containing the width and height of this component.
     *  @throws Exception  If an input or output exception occurred
     */
    public Dimension getSize() throws Exception {
        float[] xy = drawOn(null);
        return new Dimension(this.w, xy[1] - this.y);
    }

    /**
     *  Draws this text column on the specified page if the 'draw' boolean value is 'true'.
     *
     *  @param page the page to draw this text column on.
     *  @return the point with x and y coordinates of the location where to draw the next component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        float[] xy = null;
        for (int i = 0; i < paragraphs.size(); i++) {
            Paragraph paragraph = paragraphs.get(i);
            this.alignment = paragraph.alignment;
            xy = drawParagraphOn(page, paragraph);
        }
        // Restore the original location
        setLocation(this.x, this.y);
        return xy;
    }

    private float[] drawParagraphOn(Page page, Paragraph paragraph) throws Exception {
        List<TextLine> list = new ArrayList<TextLine>();
        float runLength = 0f;
        for (int i = 0; i < paragraph.lines.size(); i++) {
            TextLine line = paragraph.lines.get(i);
            if (i == 0) {
                lineHeight = line.font.bodyHeight + spaceBetweenLines;
                if (rotate == 0) {
                    y1 += line.font.ascent;
                } else if (rotate == 90) {
                    x1 += line.font.ascent;
                } else if (rotate == 270) {
                    x1 -= line.font.ascent;
                }
            }

            String[] tokens = line.text.split("\\s+");
            TextLine text = null;
            for (int j = 0; j < tokens.length; j++) {
                String str = tokens[j];
                text = new TextLine(line.font, str);
                text.setColor(line.getColor());
                text.setUnderline(line.getUnderline());
                text.setStrikeout(line.getStrikeout());
                text.setVerticalOffset(line.getVerticalOffset());
                text.setURIAction(line.getURIAction());
                text.setGoToAction(line.getGoToAction());
                text.setFallbackFont(line.getFallbackFont());
                runLength += line.font.stringWidth(line.fallbackFont, str);
                if (runLength < w) {
                    list.add(text);
                    runLength += line.font.stringWidth(line.fallbackFont, Single.space);
                } else {
                    drawLineOfText(page, list);
                    moveToNextLine();
                    list.clear();
                    list.add(text);
                    runLength = line.font.stringWidth(line.fallbackFont, str + Single.space);
                }
            }
            if (line.getTrailingSpace() == false) {
                runLength -= line.font.stringWidth(line.fallbackFont, Single.space);
                text.setTrailingSpace(false);
            }
        }
        drawNonJustifiedLine(page, list);
        if (lineBetweenParagraphs) {
            moveToNextLine();
        }

        return moveToNextParagraph(this.spaceBetweenParagraphs);
    }

    private float[] moveToNextLine() {
        if (rotate == 0) {
            this.x1 = x;
            this.y1 += lineHeight;
        } else if (rotate == 90) {
            this.x1 += lineHeight;
            this.y1 = y;
        } else if (rotate == 270) {
            this.x1 -= lineHeight;
            this.y1 = y;
        }
        return new float[] {x1, y1};
    }

    private float[] moveToNextParagraph(float spaceBetweenParagraphs) {
        if (rotate == 0) {
            x1 = x;
            y1 += spaceBetweenParagraphs;
        } else if (rotate == 90) {
            x1 += spaceBetweenParagraphs;
            y1 = y;
        } else if (rotate == 270) {
            x1 -= spaceBetweenParagraphs;
            y1 = y;
        }
        return new float[] {x1, y1};
    }

    private float[] drawLineOfText(Page page, List<TextLine> list) throws Exception {
        if (alignment == Align.JUSTIFY) {
            float sumOfWordWidths = 0f;
            for (int i = 0; i < list.size(); i++) {
                TextLine textLine = list.get(i);
                sumOfWordWidths += textLine.font.stringWidth(textLine.fallbackFont, textLine.text);
            }
            float dx = (w - sumOfWordWidths) / (list.size() - 1);
            for (int i = 0; i < list.size(); i++) {
                TextLine textLine = list.get(i);
                textLine.setLocation(x1, y1 + textLine.getVerticalOffset());

                if (textLine.getGoToAction() != null) {
                    page.addAnnotation(new Annotation(
                            null,                       // The URI
                            textLine.getGoToAction(),   // The destination name
                            x,
                            y - textLine.font.ascent,
                            x + textLine.font.stringWidth(textLine.fallbackFont, textLine.text),
                            y + textLine.font.descent,
                            null,
                            null,
                            null));
                }

                if (rotate == 0) {
                    textLine.setTextDirection(0);
                    textLine.drawOn(page);
                    x1 += textLine.font.stringWidth(textLine.fallbackFont, textLine.text) + dx;
                } else if (rotate == 90) {
                    textLine.setTextDirection(90);
                    textLine.drawOn(page);
                    y1 -= textLine.font.stringWidth(textLine.fallbackFont, textLine.text) + dx;
                } else if (rotate == 270) {
                    textLine.setTextDirection(270);
                    textLine.drawOn(page);
                    y1 += textLine.font.stringWidth(textLine.fallbackFont, textLine.text) + dx;
                }
            }
        } else {
            return drawNonJustifiedLine(page, list);
        }
        return new float[] {x1, y1};
    }

    private float[] drawNonJustifiedLine(Page page, List<TextLine> list) throws Exception {
        float runLength = 0f;
        for (int i = 0; i < list.size(); i++) {
            TextLine textLine = list.get(i);
            if (i < (list.size() - 1)) {
                if (textLine.getTrailingSpace()) {
                    textLine.text += Single.space;
                }
            }
            runLength += textLine.font.stringWidth(textLine.fallbackFont, textLine.text);
        }

        if (alignment == Align.CENTER) {
            if (rotate == 0) {
                x1 = x + ((w - runLength) / 2);
            } else if (rotate == 90) {
                y1 = y - ((w - runLength) / 2);
            } else if (rotate == 270) {
                y1 = y + ((w - runLength) / 2);
            }
        } else if (alignment == Align.RIGHT) {
            if (rotate == 0) {
                x1 = x + (w - runLength);
            } else if (rotate == 90) {
                y1 = y - (w - runLength);
            } else if (rotate == 270) {
                y1 = y + (w - runLength);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            TextLine textLine = list.get(i);
            textLine.setLocation(x1, y1 + textLine.getVerticalOffset());
            if (textLine.getGoToAction() != null) {
                page.addAnnotation(new Annotation(
                        null,                       // The URI
                        textLine.getGoToAction(),   // The destination name
                        x,
                        y - textLine.font.ascent,
                        x + textLine.font.stringWidth(textLine.fallbackFont, textLine.text),
                        y + textLine.font.descent,
                        null,
                        null,
                        null));
            }

            if (rotate == 0) {
                textLine.setTextDirection(0);
                textLine.drawOn(page);
                x1 += textLine.font.stringWidth(textLine.fallbackFont, textLine.text);
            } else if (rotate == 90) {
                textLine.setTextDirection(90);
                textLine.drawOn(page);
                y1 -= textLine.font.stringWidth(textLine.fallbackFont, textLine.text);
            } else if (rotate == 270) {
                textLine.setTextDirection(270);
                textLine.drawOn(page);
                y1 += textLine.font.stringWidth(textLine.fallbackFont, textLine.text);
            }
        }
        return new float[] {x1, y1};
    }

    /**
     *  Adds a new paragraph with Chinese text to this text column.
     *
     *  @param font the font used by this paragraph.
     *  @param chinese the Chinese text.
     */
    public void addChineseParagraph(Font font, String chinese) {
        Paragraph paragraph;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < chinese.length(); i++) {
            char ch = chinese.charAt(i);
            if (font.stringWidth(buf.toString() + ch) > w) {
                paragraph = new Paragraph();
                paragraph.add(new TextLine(font, buf.toString()));
                addParagraph(paragraph);
                buf.setLength(0);
            }
            buf.append(ch);
        }
        paragraph = new Paragraph();
        paragraph.add(new TextLine(font, buf.toString()));
        addParagraph(paragraph);
    }

    /**
     *  Adds a new paragraph with Japanese text to this text column.
     *
     *  @param font the font used by this paragraph.
     *  @param japanese the Japanese text.
     */
    public void addJapaneseParagraph(Font font, String japanese) {
        addChineseParagraph(font, japanese);
    }
}   // End of TextColumn.java
