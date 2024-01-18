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
import org.example.entity.Room;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

public class monthlyHotelWorkReport {
    private static final Logger logger = Logger.getLogger(monthlyHotelWorkReport.class.getName());
    private static Document document;
    private static PdfDocument pdfDocument;
    private final Map<Room, Integer> incomePerRoom;
    private final Map<Room, Integer> lengthOfStayInRoom;
    private final int month;
    private final int year;
    private final int countOfClients;
    public monthlyHotelWorkReport(Map<Room, Integer> incomePerRoom, Map<Room, Integer> lengthOfStayInRoom,
                                  int month, int year, int countOfClients) {
        this.incomePerRoom = incomePerRoom;
        this.lengthOfStayInRoom = lengthOfStayInRoom;
        this.month = month;
        this.year = year;
        this.countOfClients = countOfClients;

        String path = "./examplesPDF/Monthly Hotel Work Reports/MonthlyReport  " + month + "-" + year + ".pdf";

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

        logger.info("MonthlyReport was created");
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
        Text title = new Text("\nMONTHLY HOTEL WORK REPORT")
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
        Paragraph footer = new Paragraph("Information is current as " + month + "-" + year);
        for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
            document.showTextAligned(footer, 297.5f, 20, page,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        }
    }

    private void addContent() {

        int income = 0;

        Table table = new Table(new float[]{3, 3, 3})
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER)
                .setWidth(400);

        table.addHeaderCell(new Cell().add(new Paragraph("Number of room")));
        table.addHeaderCell(new Cell().add(new Paragraph("Room occupancy this month (days)")));
        table.addHeaderCell(new Cell().add(new Paragraph("Income of the room ($)")));

        for (Room room : incomePerRoom.keySet()) {
            table.addCell(new Cell().add(new Paragraph(room.getNumberOfRoom())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(lengthOfStayInRoom.get(room)))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(incomePerRoom.get(room)))));

            income += incomePerRoom.get(room);
        }
        table.addCell(new Cell().add(new Paragraph("Total:")));
        table.addCell(new Cell().add(new Paragraph("-")));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(income))));

        document.add(new Paragraph("Number of clients in the current month: " + countOfClients));

        document.add(table);
    }
}
