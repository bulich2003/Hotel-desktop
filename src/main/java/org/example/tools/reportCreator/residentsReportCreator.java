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
import org.example.entity.Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class residentsReportCreator {
    private static final Logger logger = Logger.getLogger(residentsReportCreator.class.getName());
    private static Document document;
    private static PdfDocument pdfDocument;
    private final String date;

    /**
     * PDF report constructor
     * <p>
     * creates a PDF document and places information elements on it:
     * title page, information fields, fields to fill, etc.
     * </p>
     */
    public residentsReportCreator() {

        // Month is strange -1??
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String path = "./examplesPDF/Resident Reports/Residents " + date + ".pdf";

        PdfWriter pdfWriter;

        try { pdfWriter = new PdfWriter(path); }
        catch (FileNotFoundException e) { throw new RuntimeException(e); }

        pdfDocument = new PdfDocument(pdfWriter);
        PdfPage page = pdfDocument.addNewPage();

        document = new Document(pdfDocument);
        PdfCanvas pdfCanvas = new PdfCanvas(page);

        addTitle(pdfCanvas);
        addContent();
        addBottom();

        document.close();

        logger.info("ResidentsReport was created");
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
        Text title = new Text("\nRESIDENTS LIST")
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
     * table of residents constructor
     * <p>
     * creates a table for convenient placement of data about each current resident of the hotel
     * </p>
     */
    private void addContent() {

        List<Client> clients = DataBaseConnection.getAllClients();
        Table table = new Table(new float[]{3, 3, 3, 3, 3})
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER)
                .setWidth(400);

        table.addHeaderCell(new Cell().add(new Paragraph("Name")));
        table.addHeaderCell(new Cell().add(new Paragraph("Surname")));
        table.addHeaderCell(new Cell().add(new Paragraph("Room")));
        table.addHeaderCell(new Cell().add(new Paragraph("Check in date")));
        table.addHeaderCell(new Cell().add(new Paragraph("Check out date")));

        for (Client client : clients) {
            if (client.getLeftTime() >= 0) {
                table.addCell(new Cell().add(new Paragraph(client.getName())));
                table.addCell(new Cell().add(new Paragraph(client.getSurname())));
                table.addCell(new Cell().add(new Paragraph(client.getClientRoom().getNumberOfRoom())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(client.getCheckInDate().getTime()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(client.getCheckOutDate().getTime()))));
            }
        }

        document.add(table);
    }
}
