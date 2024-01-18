package org.example.tools.reportCreator;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Employee;
import org.example.entity.Room;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class priceListCreator {
    private static final Logger logger = Logger.getLogger(priceListCreator.class.getName());
    private static Document document;
    private static PdfDocument pdfDocument;
    private String date;

    /**
     * PDF report constructor
     * <p>
     * creates a PDF document and places information elements on it:
     * title page, information fields, fields to fill, etc.
     * </p>
     */
    public priceListCreator() {

        // Month is strange -1??
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String path = "./examplesPDF/Price Lists/Price List " + date + ".pdf";

        PdfWriter pdfWriter;

        try { pdfWriter = new PdfWriter(path); }
        catch (FileNotFoundException e) { throw new RuntimeException(e); }

        pdfDocument = new PdfDocument(pdfWriter);
        PdfPage page = pdfDocument.addNewPage();

        document = new Document(pdfDocument);
        PdfCanvas pdfCanvas = new PdfCanvas(page);

        addTitle(pdfCanvas);
        addEmployeeTable();
        addBottom();

        document.close();

        logger.info("PriceList was created");
    }

    /**
     * title constructor
     * <p>
     * creates a title for the document
     * </p>
     */
    private void addTitle(PdfCanvas pdfCanvas) {
        Rectangle rectangle = new Rectangle(100, 720, 400, 100);
        pdfCanvas.saveState()
                .setFillColor(Color.WHITE)
                .rectangle(rectangle)
                .fill()
                .restoreState();
        pdfCanvas.stroke();
        Canvas canvas = new Canvas(pdfCanvas, pdfDocument, rectangle);

        String dest = "./img/logo.png";
        Image image;
        try { image = new Image(ImageDataFactory.create(dest)); }
        catch (MalformedURLException e) { throw new RuntimeException(e); }

        PdfFont bold;
        try { bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD); }
        catch (IOException e) { throw new RuntimeException(e); }
        Text title = new Text("\nPRICE LIST")
                .setFont(bold);
        Paragraph paragraph = new Paragraph()
                .add(image.scale(0.15f, 0.15f))
                .add(title)
                .setTextAlignment(TextAlignment.CENTER);

        canvas.add(paragraph);
        canvas.close();
        document.add(new Paragraph("\n\n\n\n\n"));
    }

    /**
     * document's bottom constructor
     * <p>
     * creates fields for filling in the name of the director
     * or other managing person, their signature, etc.
     * </p>
     */
    private void addBottom() {
        Paragraph footer = new Paragraph("Information is current as " + date);
        for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
            document.showTextAligned(footer, 297.5f, 20, page,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        }
    }

    /**
     * table of employees constructor
     * <p>
     * creates a table for convenient placement of data about each employee of the company
     * </p>
     */
    private void addEmployeeTable() {

        List<Room> rooms = DataBaseConnection.getAllRooms();
        Table table = new Table(new float[]{3, 3, 3})
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER)
                .setWidth(400);

        table.addHeaderCell(new Cell().add(new Paragraph("Number of room")));
        table.addHeaderCell(new Cell().add(new Paragraph("Cost")));
        table.addHeaderCell(new Cell().add(new Paragraph("Size")));

        for (Room room : rooms) {
            table.addCell(new Cell().add(new Paragraph(room.getNumberOfRoom())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(room.getCost()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(room.getSize()))));
        }

        document.add(table);
    }
}
