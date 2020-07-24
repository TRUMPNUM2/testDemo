package com.example.test.pdfTest;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class PdfUitls {
    public static void main(String[] args) throws IOException {
        String html = PDFKit.readFileByUrl("https://www.cnblogs.com/blueskyli/p/9921832.html"); // 将html代码读取到html字符串中

        try {
            Document document = new Document();
            PdfWriter mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(new File("D:\\data\\3.pdf")));
            document.open();
            ByteArrayInputStream bin = new ByteArrayInputStream(html.getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document, bin, null, new ChinaFontProvide());
            System.out.println("生成完毕");
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class ChinaFontProvide implements FontProvider {

        @Override
        public boolean isRegistered(String s) {
            return false;
        }

        @Override
        public Font getFont(String arg0, String arg1, boolean arg2, float arg3, int arg4, BaseColor arg5) {
            BaseFont bfChinese = null;
            try {
                bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
            return FontChinese;
        }
    }

    public static final String FILE_DIR = "";

    /**
     * 生成一个pdf
     *
     * @throws Exception
     */
    public static void genPdfDoc() throws Exception {
        Document document = new Document();
//Step 2—Get a PdfWriter instance.
        PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "createSamplePDF.pdf"));
//Step 3—Open the Document.
        document.open();
//Step 4—Add content.
        document.add(new Paragraph("Hello World"));
//Step 5—Close the Document.
        document.close();
    }

    /**
     * 页面大小,页面背景色,页边空白,Title,Author,Subject,Keywords
     *
     * @throws Exception
     */
    public static void setPdfSytleSizePageColorTitle(OutputStream out) throws Exception {
        //页面大小
        Rectangle rect = new Rectangle(PageSize.B5.rotate());
//页面背景色
        rect.setBackgroundColor(BaseColor.ORANGE);

        Document doc = new Document(rect);

        PdfWriter writer = PdfWriter.getInstance(doc, out);

//PDF版本(默认1.4)
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_2);

//文档属性
        doc.addTitle("Title@sample");
        doc.addAuthor("Author@rensanning");
        doc.addSubject("Subject@iText sample");
        doc.addKeywords("Keywords@iText");
        doc.addCreator("Creator@iText");

//页边空白
        doc.setMargins(10, 20, 30, 40);

        doc.open();
        doc.add(new Paragraph("Hello World"));
    }

    /**
     * 设置密码
     *
     * @param doc
     * @param out
     * @throws Exception
     */
    public static void setPassword(Document doc, OutputStream out) throws Exception {
        PdfWriter writer = PdfWriter.getInstance(doc, out);

// 设置密码为："World"
        writer.setEncryption("Hello".getBytes(), "World".getBytes(),
                PdfWriter.ALLOW_SCREENREADERS,
                PdfWriter.STANDARD_ENCRYPTION_128);

        doc.open();
        doc.add(new Paragraph("Hello World"));
    }

    /**
     * 添加Page
     *
     * @param document
     * @param writer
     * @throws Exception
     */
    public static void addPage(Document document, PdfWriter writer) throws Exception {
        document.open();
        document.add(new Paragraph("First page"));
        document.add(new Paragraph(2.2f));

        document.newPage();
        writer.setPageEmpty(false);

        document.newPage();
        document.add(new Paragraph("New page"));
    }

    /**
     * 添加Page
     *
     * @param document
     * @param writer
     * @throws Exception
     */
    public static void addWatermark(Document document, PdfWriter writer) throws Exception {
        //图片水印
        PdfReader reader = new PdfReader(FILE_DIR + "setWatermark.pdf");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR
                + "setWatermark2.pdf"));

        Image img = Image.getInstance("resource/watermark.jpg");
        img.setAbsolutePosition(200, 400);
        PdfContentByte under = stamp.getUnderContent(1);
        under.addImage(img);

//文字水印
        PdfContentByte over = stamp.getOverContent(2);
        over.beginText();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                BaseFont.EMBEDDED);
        over.setFontAndSize(bf, 18);
        over.setTextMatrix(30, 30);
        over.showTextAligned(Element.ALIGN_LEFT, "DUPLICATE", 230, 430, 45);
        over.endText();

//背景图
        Image img2 = Image.getInstance("resource/test.jpg");
        img2.setAbsolutePosition(0, 0);
        PdfContentByte under2 = stamp.getUnderContent(3);
        under2.addImage(img2);

        stamp.close();
        reader.close();
    }

    public static void addChunkPhraseParagraph(Document document, PdfWriter writer) throws Exception {
        //Chunk对象: a String, a Font, and some attributes
        document.add(new Chunk("China"));
        document.add(new Chunk(" "));
        Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
        Chunk id = new Chunk("chinese", font);
        id.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        id.setTextRise(6);
        document.add(id);
        document.add(Chunk.NEWLINE);

        document.add(new Chunk("Japan"));
        document.add(new Chunk(" "));
        Font font2 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE);
        Chunk id2 = new Chunk("japanese", font2);
        id2.setBackground(BaseColor.BLACK, 1f, 0.5f, 1f, 1.5f);
        id2.setTextRise(6);
        id2.setUnderline(0.2f, -2f);
        document.add(id2);
        document.add(Chunk.NEWLINE);

//Phrase对象: a List of Chunks with leading
        document.newPage();
        document.add(new Phrase("Phrase page"));

        Phrase director = new Phrase();
        Chunk name = new Chunk("China");
        name.setUnderline(0.2f, -2f);
        director.add(name);
        director.add(new Chunk(","));
        director.add(new Chunk(" "));
        director.add(new Chunk("chinese"));
        director.setLeading(24);
        document.add(director);

        Phrase director2 = new Phrase();
        Chunk name2 = new Chunk("Japan");
        name2.setUnderline(0.2f, -2f);
        director2.add(name2);
        director2.add(new Chunk(","));
        director2.add(new Chunk(" "));
        director2.add(new Chunk("japanese"));
        director2.setLeading(24);
        document.add(director2);

//Paragraph对象: a Phrase with extra properties and a newline
        document.newPage();
        document.add(new Paragraph("Paragraph page"));

        Paragraph info = new Paragraph();
        info.add(new Chunk("China "));
        info.add(new Chunk("chinese"));
        info.add(Chunk.NEWLINE);
        info.add(new Phrase("Japan "));
        info.add(new Phrase("japanese"));
        document.add(info);

//List对象: a sequence of Paragraphs called ListItem
        document.newPage();
        List list = new List(List.ORDERED);
        for (int i = 0; i < 10; i++) {
            ListItem item = new ListItem(String.format("%s: %d movies",
                    "country" + (i + 1), (i + 1) * 100), new Font(
                    Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.WHITE));
            List movielist = new List(List.ORDERED, List.ALPHABETICAL);
            movielist.setLowercase(List.LOWERCASE);
            for (int j = 0; j < 5; j++) {
                ListItem movieitem = new ListItem("Title" + (j + 1));
                List directorlist = new List(List.UNORDERED);
                for (int k = 0; k < 3; k++) {
                    directorlist.add(String.format("%s, %s", "Name1" + (k + 1),
                            "Name2" + (k + 1)));
                }
                movieitem.add(directorlist);
                movielist.add(movieitem);
            }
            item.add(movielist);
            list.add(item);
        }
        document.add(list);

    }

    /**
     * 插入Anchor, Image, Chapter, Section
     *
     * @param document
     * @param writer
     * @throws Exception
     */
    public static void addAnchorImageChapterSection(Document document, PdfWriter writer) throws Exception {
        //Anchor对象: internal and external links
        Paragraph country = new Paragraph();
        Anchor dest = new Anchor("china", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
        dest.setName("CN");
        dest.setReference("http://www.china.com");//external
        country.add(dest);
        country.add(String.format(": %d sites", 10000));
        document.add(country);

        document.newPage();
        Anchor toUS = new Anchor("Go to first page.", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
        toUS.setReference("#CN");//internal
        document.add(toUS);

//Image对象
        document.newPage();
        Image img = Image.getInstance("resource/test.jpg");
        img.setAlignment(Image.LEFT | Image.TEXTWRAP);
        img.setBorder(Image.BOX);
        img.setBorderWidth(10);
        img.setBorderColor(BaseColor.WHITE);
        img.scaleToFit(1000, 72);//大小
        img.setRotationDegrees(-30);//旋转
        document.add(img);

//Chapter, Section对象（目录）
        document.newPage();
        Paragraph title = new Paragraph("Title");
        Chapter chapter = new Chapter(title, 1);

        title = new Paragraph("Section A");
        Section section = chapter.addSection(title);
        section.setBookmarkTitle("bmk");
        section.setIndentation(30);
        section.setBookmarkOpen(false);
        section.setNumberStyle(
                Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

        Section subsection = section.addSection(new Paragraph("Sub Section A"));
        subsection.setIndentationLeft(20);
        subsection.setNumberDepth(1);

        document.add(chapter);
    }

    /**
     * 设置段落
     *
     * @param document
     * @throws DocumentException
     */
    public static void drawImage(Document document) throws DocumentException {
        //左右箭头
        document.add(new VerticalPositionMark() {

            public void draw(PdfContentByte canvas, float llx, float lly,
                             float urx, float ury, float y) {
                canvas.beginText();
                BaseFont bf = null;
                try {
                    bf = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                canvas.setFontAndSize(bf, 12);

                // LEFT
                canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), llx - 10, y, 0);
                // RIGHT
                canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), urx + 10, y + 8, 180);

                canvas.endText();
            }
        });

//直线
        Paragraph p1 = new Paragraph("LEFT");
        p1.add(new Chunk(new LineSeparator()));
        p1.add("R");
        document.add(p1);
//点线
        Paragraph p2 = new Paragraph("LEFT");
        p2.add(new Chunk(new DottedLineSeparator()));
        p2.add("R");
        document.add(p2);
//下滑线
        LineSeparator UNDERLINE = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
        Paragraph p3 = new Paragraph("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
        p3.add(UNDERLINE);
        document.add(p3);
    }

    /**
     * 设置段落
     *
     * @param document
     * @throws DocumentException
     */
    public static void setParagraph(Document document) throws DocumentException {
        Paragraph p = new Paragraph("In the previous example, you added a header and footer with the showTextAligned() method. This example demonstrates that it’s sometimes more interesting to use PdfPTable and writeSelectedRows(). You can define a bottom border for each cell so that the header is underlined. This is the most elegant way to add headers and footers, because the table mechanism allows you to position and align lines, images, and text.");

//默认
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(p);

        document.newPage();
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setIndentationLeft(1 * 15f);
        p.setIndentationRight((5 - 1) * 15f);
        document.add(p);

//居右
        document.newPage();
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setSpacingAfter(15f);
        document.add(p);

//居左
        document.newPage();
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingBefore(15f);
        document.add(p);

//居中
        document.newPage();
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(15f);
        p.setSpacingBefore(15f);
        document.add(p);

    }

    /**
     * 删除page
     */
    public static void delPage(Document document) throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "deletePage.pdf");


        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("First page"));
//        document.add(new Paragraph(Document.getVersion()));

        document.newPage();
        writer.setPageEmpty(false);

        document.newPage();
        document.add(new Paragraph("New page"));

        document.close();

        PdfReader reader = new PdfReader(FILE_DIR + "deletePage.pdf");
        reader.selectPages("1,3");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR
                + "deletePage2.pdf"));
        stamp.close();
        reader.close();
    }

    /**
     * 删除page
     */
    public static void addPage(Document document) throws Exception {
        FileOutputStream out = new FileOutputStream(FILE_DIR + "insertPage.pdf");

        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("1 page"));

        document.newPage();
        document.add(new Paragraph("2 page"));

        document.newPage();
        document.add(new Paragraph("3 page"));

        document.close();

        PdfReader reader = new PdfReader(FILE_DIR + "insertPage.pdf");
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR
                + "insertPage2.pdf"));

        stamp.insertPage(2, reader.getPageSize(1));

        ColumnText ct = new ColumnText(null);
        ct.addElement(new Paragraph(24, new Chunk("INSERT PAGE")));
        ct.setCanvas(stamp.getOverContent(2));
        ct.setSimpleColumn(36, 36, 559, 770);

        stamp.close();
        reader.close();
    }

    /**
     * 排序page
     */
    public static void addPage(Document doc, OutputStream out) throws Exception {
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        writer.setLinearPageMode();

        doc.open();
        doc.add(new Paragraph("1 page"));
        doc.newPage();
        doc.add(new Paragraph("2 page"));
        doc.newPage();
        doc.add(new Paragraph("3 page"));
        doc.newPage();
        doc.add(new Paragraph("4 page"));
        doc.newPage();
        doc.add(new Paragraph("5 page"));

        int[] order = {4, 3, 2, 1};
        writer.reorderPages(order);
    }

    /**
     * @param document
     * @param writer
     * @throws Exception
     */
    public static void setCatalog(Document document, PdfWriter writer) throws Exception {
// Code 1
        document.add(new Chunk("Chapter 1").setLocalDestination("1"));

        document.newPage();
        document.add(new Chunk("Chapter 2").setLocalDestination("2"));
        document.add(new Paragraph(new Chunk("Sub 2.1").setLocalDestination("2.1")));
        document.add(new Paragraph(new Chunk("Sub 2.2").setLocalDestination("2.2")));

        document.newPage();
        document.add(new Chunk("Chapter 3").setLocalDestination("3"));

// Code 2
        PdfContentByte cb = writer.getDirectContent();
        PdfOutline root = cb.getRootOutline();

// Code 3
        @SuppressWarnings("unused")
        PdfOutline oline1 = new PdfOutline(root, PdfAction.gotoLocalPage("1", false), "Chapter 1");

        PdfOutline oline2 = new PdfOutline(root, PdfAction.gotoLocalPage("2", false), "Chapter 2");
        oline2.setOpen(false);

        @SuppressWarnings("unused")
        PdfOutline oline2_1 = new PdfOutline(oline2, PdfAction.gotoLocalPage("2.1", false), "Sub 2.1");
        @SuppressWarnings("unused")
        PdfOutline oline2_2 = new PdfOutline(oline2, PdfAction.gotoLocalPage("2.2", false), "Sub 2.2");

        @SuppressWarnings("unused")
        PdfOutline oline3 = new PdfOutline(root, PdfAction.gotoLocalPage("3", false), "Chapter 3");
    }

    /**
     * 设置标题头
     *
     * @param doc
     * @throws Exception
     */
    public static void setHeaderFooter(Document doc) throws Exception {
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(FILE_DIR + "setHeaderFooter.pdf"));
        writer.setPageEvent(new PdfPageEventHelper() {

            public void onEndPage(PdfWriter writer, Document document) {

                PdfContentByte cb = writer.getDirectContent();
                cb.saveState();

                cb.beginText();
                BaseFont bf = null;
                try {
                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cb.setFontAndSize(bf, 10);

                //Header
                float x = document.top(-20);

                //左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                        "H-Left",
                        document.left(), x, 0);
                //中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                        writer.getPageNumber() + " page",
                        (document.right() + document.left()) / 2,
                        x, 0);
                //右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                        "H-Right",
                        document.right(), x, 0);

                //Footer
                float y = document.bottom(-20);

                //左
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                        "F-Left",
                        document.left(), y, 0);
                //中
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                        writer.getPageNumber() + " page",
                        (document.right() + document.left()) / 2,
                        y, 0);
                //右
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                        "F-Right",
                        document.right(), y, 0);

                cb.endText();

                cb.restoreState();
            }
        });

        doc.open();
        doc.add(new Paragraph("1 page"));
        doc.newPage();
        doc.add(new Paragraph("2 page"));
        doc.newPage();
        doc.add(new Paragraph("3 page"));
        doc.newPage();
        doc.add(new Paragraph("4 page"));
    }

    /**
     * 分割PDF
     *
     * @param document
     * @throws Exception
     */
    public static void setFontStyle(Document document, OutputStream out) throws Exception {
        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        Phrase phrase1 = new Phrase("This is a test!left");
        Phrase phrase2 = new Phrase("This is a test!right");
        Phrase phrase3 = new Phrase("This is a test!center");
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase1, 10, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase2, 10, 536, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase3, 10, 572, 0);
    }

    /**
     * 设置标题头
     *
     * @param document
     * @throws Exception
     */
    public static void splitePdf(Document document, OutputStream out) throws Exception {

        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("1 page"));

        document.newPage();
        document.add(new Paragraph("2 page"));

        document.newPage();
        document.add(new Paragraph("3 page"));

        document.newPage();
        document.add(new Paragraph("4 page"));

        document.close();

        PdfReader reader = new PdfReader(FILE_DIR + "splitPDF.pdf");

        Document dd = new Document();
        PdfWriter writer = PdfWriter.getInstance(dd, new FileOutputStream(FILE_DIR + "splitPDF1.pdf"));
        dd.open();
        PdfContentByte cb = writer.getDirectContent();
        dd.newPage();
        cb.addTemplate(writer.getImportedPage(reader, 1), 0, 0);
        dd.newPage();
        cb.addTemplate(writer.getImportedPage(reader, 2), 0, 0);
        dd.close();
        writer.close();

        Document dd2 = new Document();
        PdfWriter writer2 = PdfWriter.getInstance(dd2, new FileOutputStream(FILE_DIR + "splitPDF2.pdf"));
        dd2.open();
        PdfContentByte cb2 = writer2.getDirectContent();
        dd2.newPage();
        cb2.addTemplate(writer2.getImportedPage(reader, 3), 0, 0);
        dd2.newPage();
        cb2.addTemplate(writer2.getImportedPage(reader, 4), 0, 0);
        dd2.close();
        writer2.close();
    }


    /**
     * 合并PDF
     *
     * @param document
     * @throws Exception
     */
    public static void mergeePdf(Document document, OutputStream out) throws Exception {
        PdfReader reader1 = new PdfReader(FILE_DIR + "splitPDF1.pdf");
        PdfReader reader2 = new PdfReader(FILE_DIR + "splitPDF2.pdf");


        PdfWriter writer = PdfWriter.getInstance(document, out);

        document.open();
        PdfContentByte cb = writer.getDirectContent();

        int totalPages = 0;
        totalPages += reader1.getNumberOfPages();
        totalPages += reader2.getNumberOfPages();

        java.util.List<PdfReader> readers = new ArrayList<PdfReader>();
        readers.add(reader1);
        readers.add(reader2);

        int pageOfCurrentReaderPDF = 0;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

// Loop through the PDF files and add to the output.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();

            // Create a new page in the target for each source page.
            while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                document.newPage();
                pageOfCurrentReaderPDF++;
                PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                cb.addTemplate(page, 0, 0);
            }
            pageOfCurrentReaderPDF = 0;
        }
        out.flush();
        document.close();
        out.close();
    }


    /**
     * 设置备注
     *
     * @param doc
     * @throws Exception
     */
    public static void addAnnotation(Document doc, OutputStream out) throws Exception {
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        writer.setLinearPageMode();

        doc.open();
        doc.add(new Paragraph("1 page"));
        doc.add(new Annotation("Title", "This is a annotation!"));

        doc.newPage();
        doc.add(new Paragraph("2 page"));
        Chunk chunk = new Chunk("\u00a0");
        chunk.setAnnotation(PdfAnnotation.createText(writer, null, "Title", "This is a another annotation!", false, "Comment"));
        doc.add(chunk);

//添加附件
        doc.newPage();
        doc.add(new Paragraph("3 page"));
        Chunk chunk2 = new Chunk("\u00a0\u00a0");
        PdfAnnotation annotation = PdfAnnotation.createFileAttachment(
                writer, null, "Title", null,
                "resource/test2.jpg",
                "img.jpg");
        annotation.put(PdfName.NAME,
                new PdfString("Paperclip"));
        chunk2.setAnnotation(annotation);
        doc.add(chunk2);
    }

    /**
     * 设置备注
     *
     * @param doc
     * @throws Exception
     */
    public static void addTable(Document doc, OutputStream out) throws Exception {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");
        doc.add(table);
    }


    /**
     * 添加嵌套表格
     *
     * @param doc
     * @throws Exception
     */
    public static void addNestingTable(Document doc, OutputStream out) throws Exception {
        PdfPTable table = new PdfPTable(4);

//1行2列
        PdfPTable nested1 = new PdfPTable(2);
        nested1.addCell("1.1");
        nested1.addCell("1.2");

//2行1列
        PdfPTable nested2 = new PdfPTable(1);
        nested2.addCell("2.1");
        nested2.addCell("2.2");

//将表格插入到指定位置
        for (int k = 0; k < 24; ++k) {
            if (k == 1) {
                table.addCell(nested1);
            } else if (k == 20) {
                table.addCell(nested2);
            } else {
                table.addCell("cell " + k);
            }
        }

        doc.add(table);
    }


    /**
     * 添加嵌套表格
     *
     * @param doc
     * @throws Exception
     */
    public static void setTableWidth(Document document, OutputStream out) throws Exception {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("row 1; cell 1");
        table.addCell("row 1; cell 2");
        table.addCell("row 2; cell 1");
        table.addCell("row 2; cell 2");

//100%
        table.setWidthPercentage(100);
        document.add(table);
        document.add(new Paragraph("\n\n"));

//宽度50% 居左
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        document.add(table);
        document.add(new Paragraph("\n\n"));

//宽度50% 居中
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        document.add(table);
        document.add(new Paragraph("\n\n"));

//宽度50% 居右
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        document.add(table);
        document.add(new Paragraph("\n\n"));

//固定宽度
        table.setTotalWidth(300);
        table.setLockedWidth(true);
        document.add(table);
    }


    /**
     * 添加嵌套表格
     *
     * @param document
     * @throws Exception
     */
    public static void setTableMargin(Document document, Font fontZH) throws Exception {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell(new Paragraph("合并3个单元格", fontZH));
        cell.setColspan(3);
        table.addCell(cell);
        table.addCell("1.1");
        table.addCell("2.1");
        table.addCell("3.1");
        table.addCell("1.2");
        table.addCell("2.2");
        table.addCell("3.2");

        cell = new PdfPCell(new Paragraph("红色边框", fontZH));
        cell.setBorderColor(new BaseColor(255, 0, 0));
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("合并单2个元格", fontZH));
        cell.setColspan(2);
        cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
        table.addCell(cell);

        table.setWidthPercentage(50);

        document.add(new Paragraph("追加2个表格", fontZH));
        document.add(table);
        document.add(table);

        document.newPage();
        document.add(new Paragraph("使用'SpacingBefore'和'setSpacingAfter'", fontZH));
        table.setSpacingBefore(15f);
        document.add(table);
        document.add(table);
        document.add(new Paragraph("这里没有间隔", fontZH));
        table.setSpacingAfter(15f);
    }


    /**
     * 添加嵌套表格
     *
     * @param document
     * @throws Exception
     */
    public static void setCellWidth(Document document, Font fontZH) throws Exception {
        //按比例设置单元格宽度
        float[] widths = {0.1f, 0.1f, 0.05f, 0.75f};
        PdfPTable table = new PdfPTable(widths);
        table.addCell("10%");
        table.addCell("10%");
        table.addCell("5%");
        table.addCell("75%");
        table.addCell("aa");
        table.addCell("aa");
        table.addCell("a");
        table.addCell("aaaaaaaaaaaaaaa");
        table.addCell("bb");
        table.addCell("bb");
        table.addCell("b");
        table.addCell("bbbbbbbbbbbbbbb");
        table.addCell("cc");
        table.addCell("cc");
        table.addCell("c");
        table.addCell("ccccccccccccccc");
        document.add(table);
        document.add(new Paragraph("\n\n"));

//调整比例
        widths[0] = 20f;
        widths[1] = 20f;
        widths[2] = 10f;
        widths[3] = 50f;
        table.setWidths(widths);
        document.add(table);

//按绝对值设置单元格宽度
        widths[0] = 40f;
        widths[1] = 40f;
        widths[2] = 20f;
        widths[3] = 300f;
        Rectangle r = new Rectangle(PageSize.A4.getRight(72), PageSize.A4.getTop(72));
        table.setWidthPercentage(widths, r);
        document.add(new Paragraph("\n\n"));
        document.add(table);
    }


    /**
     * 添加嵌套表格
     *
     * @param document
     * @throws Exception
     */
    public static void setCellHeight(Document document, Font fontZH) throws Exception {
        PdfPTable table = new PdfPTable(2);

        PdfPCell cell;

//折行
        table.addCell(new PdfPCell(new Paragraph("折行", fontZH)));
        cell = new PdfPCell(new Paragraph("blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah"));
        cell.setNoWrap(false);
        table.addCell(cell);

//不折行
        table.addCell(new PdfPCell(new Paragraph("不折行", fontZH)));
        cell.setNoWrap(true);
        table.addCell(cell);

//设置高度
        table.addCell(new PdfPCell(new Paragraph("任意高度", fontZH)));
        cell = new PdfPCell(new Paragraph("1. blah blah\n2. blah blah blah\n3. blah blah\n4. blah blah blah\n5. blah blah\n6. blah blah blah\n7. blah blah\n8. blah blah blah"));
        table.addCell(cell);

//固定高度
        table.addCell(new PdfPCell(new Paragraph("固定高度", fontZH)));
        cell.setFixedHeight(50f);
        table.addCell(cell);

//最小高度
        table.addCell(new PdfPCell(new Paragraph("最小高度", fontZH)));
        cell = new PdfPCell(new Paragraph("最小高度：50", fontZH));
        cell.setMinimumHeight(50f);
        table.addCell(cell);

//最后一行拉长到page底部
        table.setExtendLastRow(true);
        table.addCell(new PdfPCell(new Paragraph("拉长最后一行", fontZH)));
        cell = new PdfPCell(new Paragraph("最后一行拉长到page底部", fontZH));
        table.addCell(cell);

        document.add(table);
    }


    /**
     * 添加单元格颜色
     *
     * @param document
     * @throws Exception
     */
    public static void setCellColor(Document document, Font fontZH) throws Exception {
        PdfPTable table = new PdfPTable(4);
        PdfPCell cell;
        cell = new PdfPCell(new Paragraph("颜色测试", fontZH));
        table.addCell(cell);

//红色背景，无边框
        cell = new PdfPCell(new Paragraph("红色背景，无边框", fontZH));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.RED);
        table.addCell(cell);

//绿色背景，下边框
        cell = new PdfPCell(new Paragraph("绿色背景，下边框", fontZH));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColorBottom(BaseColor.MAGENTA);
        cell.setBorderWidthBottom(5f);
        cell.setBackgroundColor(BaseColor.GREEN);
        table.addCell(cell);

//蓝色背景，上边框
        cell = new PdfPCell(new Paragraph("蓝色背景，上边框", fontZH));
        cell.setBorder(Rectangle.TOP);
        cell.setUseBorderPadding(true);
        cell.setBorderWidthTop(5f);
        cell.setBorderColorTop(BaseColor.CYAN);
        cell.setBackgroundColor(BaseColor.BLUE);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("背景灰色度", fontZH));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("0.25"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setGrayFill(0.25f);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("0.5"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setGrayFill(0.5f);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("0.75"));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setGrayFill(0.75f);
        table.addCell(cell);

        document.add(table);
    }

    /**
     * 添加单元格颜色
     *
     * @param document
     * @throws Exception
     */
    public static void insertImage(Document document, Font fontZH) throws Exception {
        Image image = Image.getInstance("resource/test2.jpg");
        float[] widths = {1f, 4f};

        PdfPTable table = new PdfPTable(widths);

//插入图片
        table.addCell(new PdfPCell(new Paragraph("图片测试", fontZH)));
        table.addCell(image);

//调整图片大小
        table.addCell("This two");
        table.addCell(new PdfPCell(image, true));

//不调整
        table.addCell("This three");
        table.addCell(new PdfPCell(image, false));
        document.add(table);
    }

    /**
     * 设置表头
     *
     * @param document
     * @throws Exception
     */
    public static void setTableHeader(Document document, Font fontZH) throws Exception {
        String[] bogusData = {"M0065920", "SL", "FR86000P", "PCGOLD",
                "119000", "96 06", "2001-08-13", "4350", "6011648299",
                "FLFLMTGP", "153", "119000.00"};
        int NumColumns = 12;
// 12
        PdfPTable datatable = new PdfPTable(NumColumns);
        int headerwidths[] = {9, 4, 8, 10, 8, 11, 9, 7, 9, 10, 4, 10}; // percentage
        datatable.setWidths(headerwidths);
        datatable.setWidthPercentage(100);
        datatable.getDefaultCell().setPadding(3);
        datatable.getDefaultCell().setBorderWidth(2);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        datatable.addCell("Clock #");
        datatable.addCell("Trans Type");
        datatable.addCell("Cusip");
        datatable.addCell("Long Name");
        datatable.addCell("Quantity");
        datatable.addCell("Fraction Price");
        datatable.addCell("Settle Date");
        datatable.addCell("Portfolio");
        datatable.addCell("ADP Number");
        datatable.addCell("Account ID");
        datatable.addCell("Reg Rep ID");
        datatable.addCell("Amt To Go ");

        datatable.setHeaderRows(1);

//边框
        datatable.getDefaultCell().setBorderWidth(1);

//背景色
        for (int i = 1; i < 1000; i++) {
            for (int x = 0; x < NumColumns; x++) {
                datatable.addCell(bogusData[x]);
            }
        }

        document.add(datatable);
    }


    /**
     * 设置表头
     *
     * @param document
     * @throws Exception
     */
    public static void spliteTable(Document document, OutputStream out) throws Exception {
        //横向分割
        PdfWriter writer = PdfWriter.getInstance(document, out);
        PdfContentByte cb = writer.getDirectContent();
        PdfPTable table = new PdfPTable(10);
        for (int k = 1; k <= 100; ++k) {
            table.addCell("The number " + k);
        }
        table.setTotalWidth(400);

        table.writeSelectedRows(0, 5, 0, -1, 5, 700, cb);
        table.writeSelectedRows(5, -1, 0, -1, 210, 700, cb);
    }

    /**
     * 设置表格空白
     *
     * @param document
     * @throws Exception
     */
    public static void setTableBlank(Document document, Font fontZH) throws Exception {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        Paragraph p = new Paragraph("Quick brown fox jumps over the lazy dog. Quick brown fox jumps over the lazy dog.");
        table.addCell(new PdfPCell(new Paragraph("默认", fontZH)));
        table.addCell(p);
        table.addCell(new PdfPCell(new Paragraph("Padding：10", fontZH)));
        cell = new PdfPCell(p);
        cell.setPadding(10f);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Paragraph("Padding：0", fontZH)));
        cell = new PdfPCell(p);
        cell.setPadding(0f);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Paragraph("上Padding：0 左Padding：20", fontZH)));
        cell = new PdfPCell(p);
        cell.setPaddingTop(0f);
        cell.setPaddingLeft(20f);
        table.addCell(cell);
        document.add(table);

        document.newPage();
        table = new PdfPTable(2);
        table.addCell(new PdfPCell(new Paragraph("没有Leading", fontZH)));
        table.getDefaultCell().setLeading(0f, 0f);
        table.addCell("blah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\n");
        table.getDefaultCell().setLeading(14f, 0f);
        table.addCell(new PdfPCell(new Paragraph("固定Leading：14pt", fontZH)));
        table.addCell("blah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\n");
        table.addCell(new PdfPCell(new Paragraph("相对于字体", fontZH)));
        table.getDefaultCell().setLeading(0f, 1.0f);
        table.addCell("blah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\nblah blah\nblah blah blah\n");
        document.add(table);
    }

    /**
     * 、设置单元格边框
     *
     * @param document
     * @param out
     * @throws Exception
     */
    public static void setCellBorder(Document document, OutputStream out) throws Exception {
        //没有边框
        PdfPTable table1 = new PdfPTable(3);
        table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table1.addCell(new Paragraph("Cell 1"));
        table1.addCell(new Paragraph("Cell 2"));
        table1.addCell(new Paragraph("Cell 3"));
        document.add(table1);

//边框粗细颜色
        document.newPage();
        Rectangle b1 = new Rectangle(0f, 0f);
        b1.setBorderWidthLeft(6f);
        b1.setBorderWidthBottom(5f);
        b1.setBorderWidthRight(4f);
        b1.setBorderWidthTop(2f);
        b1.setBorderColorLeft(BaseColor.RED);
        b1.setBorderColorBottom(BaseColor.ORANGE);
        b1.setBorderColorRight(BaseColor.YELLOW);
        b1.setBorderColorTop(BaseColor.GREEN);
        PdfPTable table2 = new PdfPTable(1);
        PdfPCell cell = new PdfPCell(new Paragraph("Cell 1"));
        cell.cloneNonPositionParameters(b1);
        table2.addCell(cell);
        document.add(table2);
    }

    /**
     * 、生成Barcode QRCode
     *
     * @param doc
     * @param cb
     * @throws Exception
     */
    public static void setCellBorder(Document doc, PdfContentByte cb) throws Exception {
        String myString = "http://www.google.com";

        Barcode128 code128 = new Barcode128();
        code128.setCode(myString.trim());
        code128.setCodeType(Barcode128.CODE128);
        Image code128Image = code128.createImageWithBarcode(cb, null, null);
        code128Image.setAbsolutePosition(10,700);
        code128Image.scalePercent(125);
        doc.add(code128Image);

        BarcodeQRCode qrcode = new BarcodeQRCode(myString.trim(), 1, 1, null);
        Image qrcodeImage = qrcode.getImage();
        qrcodeImage.setAbsolutePosition(10,600);
        qrcodeImage.scalePercent(200);
        doc.add(qrcodeImage);
    }



}
