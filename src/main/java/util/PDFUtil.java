package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;

public class PDFUtil {

    // 🔹 Multi-record PDF method
    public static String createPDF(ResultSet rs) {
        String folderPath = "D:\\Generated pdf";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String pdfPath = folderPath + "\\Generated_pdf_" + System.currentTimeMillis() + ".pdf";
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // 🔹 Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph title = new Paragraph("Student Record Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // 🔹 Optional Image
            try {
                String imagePath = "C:\\Users\\user\\Downloads\\Aditi.jpeg";
                Image img = Image.getInstance(imagePath);
                img.scaleToFit(100, 120);
                img.setAlignment(Image.ALIGN_CENTER);
                document.add(img);
            } catch (Exception e) {
                // image optional
            }

            document.add(new Paragraph("\n"));

            // 🔹 TABLE (4 columns)
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // 🔹 Table Header
            addHeaderCell(table, "ID");
            addHeaderCell(table, "Name");
            addHeaderCell(table, "Course");
            addHeaderCell(table, "Email");

            // 🔹 Table Data
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("name"));
                table.addCell(rs.getString("course"));
                table.addCell(rs.getString("email"));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pdfPath;
    }

    // 🔹 Header cell styling
    private static void addHeaderCell(PdfPTable table, String text) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}
