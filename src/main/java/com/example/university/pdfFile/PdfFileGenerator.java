package com.example.university.pdfFile;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfFileGenerator {

    private static final String FONT_PATH = "src/main/resources/fonts/Outfit-VariableFont_wght.ttf";
    private static final Font TITLE_FONT;
    private static final Font NORMAL_FONT;
    private static final Font SUBTITLE_FONT;

    static {
        try {
            BaseFont baseFont = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            TITLE_FONT = new Font(baseFont, 24, Font.BOLD, BaseColor.DARK_GRAY);
            SUBTITLE_FONT = new Font(baseFont, 16, Font.BOLD, BaseColor.DARK_GRAY);
            NORMAL_FONT = new Font(baseFont, 13, Font.BOLD, BaseColor.DARK_GRAY);
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Failed to load font", e);
        }
    }

    public byte[] generatePdf(String firstName, String lastName, String address, String email, String abilities, String skills, String predictionResult) throws IOException, DocumentException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            addTitle(document);
            addLineSeparator(document);
            addLineSeparator(document);
            addSummary(document);
            String formattedBasicInfo = String.format("Ime i prezime: %s %s\n\nGrad: %s\n\nEmail: %s", firstName, lastName, address, email);
            addSection(document, "Osnovne informacije", formattedBasicInfo, new BaseColor(136, 197, 204));
            addSection(document, "Tvoje sposobnosti", abilities, new BaseColor(136, 197, 204));
            addSection(document, "Tvoje vještine", skills, new BaseColor(136, 197, 204));
            addSection(document, "Rezultat predviđanja", predictionResult, new BaseColor(136, 197, 204));

            document.close();

            return byteArrayOutputStream.toByteArray();
        }
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Rezultati predviđanja fakulteta", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addLineSeparator(Document document) throws DocumentException {
        document.add(new LineSeparator(1, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -2));
    }

    private void addSummary(Document document) throws DocumentException {
        Paragraph summaryTitle = new Paragraph("Opis", SUBTITLE_FONT);
        summaryTitle.setSpacingBefore(20);
        document.add(summaryTitle);

        Paragraph summaryText = new Paragraph(
                "U prilogu se nalaze rezultati dobijeni na osnovu tvojih sposobnosti i vještina. Ovi rezultati ti mogu poslužiti kao smjernica za odabir akademskih studija na kojima ćeš imati mogućnost da iskoristiš i unaprijediš svoj potencijal.",
                NORMAL_FONT
        );
        summaryText.setSpacingBefore(10);
        summaryText.setSpacingAfter(20);
        document.add(summaryText);
    }

    private void addSection(Document document, String sectionTitle, String content, BaseColor backgroundColor) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);

        PdfPCell titleCell = new PdfPCell(new Paragraph(sectionTitle, SUBTITLE_FONT));
        titleCell.setBackgroundColor(backgroundColor);
        titleCell.setPadding(10);
        table.addCell(titleCell);

        PdfPCell contentCell = new PdfPCell(new Paragraph(content, NORMAL_FONT));
        contentCell.setPadding(10);
        contentCell.setBackgroundColor(BaseColor.WHITE);
        table.addCell(contentCell);

        document.add(table);
    }
}