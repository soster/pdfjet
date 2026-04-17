# PDFjet Library — Agent Context

**Version**: 7.07.3 (Innovatics Inc., MIT License)  
**Package**: `com.pdfjet`  
**Module path**: `scribmasterlib/pdfjet/`

This is a vendored/bundled Java PDF generation and parsing library used by the HandWrite drawing engine.

## Purpose in HandWrite

Used exclusively by `PdfExporter` (`scribmasterlib/src/main/java/net/thoster/scribmasterlib/export/PdfExporter.java`) to:
- **Create** new PDF files from drawn content (SVG node tree → PDF page commands)
- **Read and annotate** existing PDFs (overlay drawings on top of a background PDF, e.g. Google Docs exports)

## Core Classes

| Class | Role |
|---|---|
| `PDF` | Root document object. Owns fonts, images, pages. Write with `PDF(OutputStream)`, read with `pdf.read(InputStream)` |
| `Page` | Represents one PDF page. Carries drawing commands in an internal `ByteArrayOutputStream buf` |
| `PDFobj` | Low-level parsed PDF object (dict + stream) — used when reading/modifying existing PDFs |
| `Token` | Static byte[] constants for PDF syntax tokens (`<<`, `>>`, `stream`, etc.) |
| `Font` | Font resource, created per-PDF, referenced by object number |
| `Image` | Bitmap image resource (JPG or PNG), embedded in the PDF |
| `GraphicsState` | Alpha/transparency state pushed via `page.setGraphicsState(gs)` |
| `Compliance` | Constants for PDF/A and PDF/UA compliance levels |
| `ICCBlackScaled` | Embedded sRGB ICC profile used for PDF/A output intent |
| `Salsa20` | Random UUID generator used to create PDF document IDs |

## PDF Generation Flow

```
PDF(OutputStream os)
  → for each page: Page(pdf, float[] pageSize)  // auto-adds to pdf
      → page.drawLine / moveTo / lineTo / bezierCurveTo / strokePath / fillPath / ...
  → pdf.complete()   // writes xref table and closes stream (not the OutputStream)
```

`Page` uses a lazy write model: page content accumulates in `page.buf` (ByteArrayOutputStream), then `addPageContent(page)` deflates and serialises it when the next page is added (or at `complete()`).

## PDF Reading + Overlay Flow

```
List<PDFobj> objects = pdf.read(InputStream)
List<PDFobj> pages   = pdf.getPageObjects(objects)

// For each page:
Page page = new Page(pdf, pageObjects.get(i))   // wraps existing page object
// ... draw on page ...
page.complete(objects)   // appends new content stream to existing page

// Finalise:
pdf.addObjects(objects)
pdf.complete()
```

## Coordinate System

- Origin `(0, 0)` is **top-left** of the page.
- Units are **PDF points** (1 pt = 1/72 inch).
- `Page.height` is used internally by `addPageBox` to flip y-coordinates from PDF's native bottom-left origin to the top-left model exposed by the API.

## Key Page Drawing Methods

```java
page.moveTo(x, y)
page.lineTo(x, y)
page.bezierCurveTo(Point cp1, Point cp2, Point end)
page.strokePath()
page.fillPath()
page.closePath()
page.save()           // push graphics state
page.restore()        // pop graphics state
page.transform(float[] matrix)   // 6-element affine matrix [a,b,c,d,e,f]
page.setPenColor(int argbColor)
page.setBrushColor(int argbColor)
page.setPenWidth(float w)
page.setLineCapStyle(CapStyle)
page.setLineJoinStyle(JoinStyle)
page.setGraphicsState(GraphicsState gs)   // for alpha
page.invertYAxis()    // used for Google Docs upside-down PDFs
page.rotatePage(int degrees, float w, float h)
```

## Alpha / Transparency

Alpha is set through `GraphicsState`:
```java
GraphicsState gs = new GraphicsState();
gs.setAlphaStroking(alpha);       // 0.0–1.0
gs.setAlphaNonStroking(alpha);
page.setGraphicsState(gs);
// reset after drawing:
page.setGraphicsState(new GraphicsState());
```

**Existing PDF flow** — use `PDFobj.setGraphicsState` to register the entry in the page's resource dict, then write the operator inline with `page.applyGraphicsState(gsNum)`. Do NOT call `page.setGraphicsState(gs)` in this path — it registers the state in `pdf.states` which is only written for new-PDF resources objects.

```java
// Existing PDF:
int gsNum = pageObject.setGraphicsState(gs, objects);  // adds to page resource dict, returns GS number
if (gsNum > 0) page.applyGraphicsState(gsNum);         // writes "/GSn gs\n" inline in overlay stream

// New PDF:
page.setGraphicsState(gs);  // registers in pdf.states + writes "/GSn gs\n" inline
```

`PDFobj.setGraphicsState` deduplicates: repeated calls with the same CA/ca values reuse the existing entry.

## Google Docs PDF Workaround

Google Docs-exported PDFs render content upside-down (bottom-left origin, not transformed). Detection is in `PdfExporter.testForGoogleDocumentRenderedPDF()` — looks for tokens `"Google"`, `"Docs"`, `"Renderer"` in the first object's dict. When detected, `page.invertYAxis()` is called to compensate.

## Landscape PDF Handling

When `page.height > page.width` but `pageParameter.height < pageParameter.width`, `PdfExporter` sets `landscapeSpecialMode = true` and calls `page.rotatePage(270, w, h)` on each element to rotate content.

## PDF/A Compliance

Pass `Compliance.PDF_A_1B` (or similar) to `PDF(OutputStream, int compliance)`. This adds:
- XMP metadata object
- sRGB ICC output intent (`ICCBlackScaled`)
- Embedded font requirement (all fonts must be embedded)

Standard usage in HandWrite does not use compliance mode.

## Important Notes

- `pdf.complete()` **closes the OutputStream**. Do not close it separately.
- The library is a pure Java module (`java-library` plugin) — no Android dependencies. The `Page` class references Android `Matrix` constants by name only (`MSCALE_X`, etc.) but does not import Android classes.
- `.class` files are committed alongside `.java` files in the repo (pre-compiled artefacts present).
- Hardcoded timezone offset `"-05:00"` in XMP metadata (`addMetadataObject`) and `"-05'00'"` in the Info object — this is a known issue in the upstream library.
