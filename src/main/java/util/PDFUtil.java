package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;

public class PDFUtil {
    public static void createPDF(String data) {
        Document document = new Document();
        try {
            // Folder check aur create
            File folder = new File("D:\\GeneratedPDFs");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // PDF file ka path
            String filePath = "D:\\GeneratedPDFs\\Record_" + System.currentTimeMillis() + ".pdf";
            
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph(data));
            document.close();

            System.out.println("PDF created successfully at: " + filePath);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
