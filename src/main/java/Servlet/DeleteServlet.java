package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DBConnection;
import util.EmailUtil;
import util.PDFUtil;

@WebServlet("/deleteRecord")
public class DeleteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Connection con = DBConnection.getConnection();

            // 🔹 1️⃣ Fetch record BEFORE delete (email content ke liye)
            String name = null;
            String course = null;
            String email = null;

            String fetchSql =
                "SELECT NAME, COURSE, EMAIL FROM aditi.ACADEMIC_RECORDS WHERE ID=?";
            PreparedStatement psFetch = con.prepareStatement(fetchSql);
            psFetch.setInt(1, id);
            ResultSet rsFetch = psFetch.executeQuery();

            if (rsFetch.next()) {
                name = rsFetch.getString("NAME");
                course = rsFetch.getString("COURSE");
                email = rsFetch.getString("EMAIL");
            }

            // 🔹 2️⃣ Delete record
            String deleteSql = "DELETE FROM aditi.ACADEMIC_RECORDS WHERE ID = ?";
            PreparedStatement psDelete = con.prepareStatement(deleteSql);
            psDelete.setInt(1, id);
            int rows = psDelete.executeUpdate();

            if (rows > 0) {

                out.println("<h2>Record Deleted Successfully ✅</h2>");
                out.println("<p>ID: " + id + "</p>");

                // 🔹 3️⃣ Fetch FULL DB after delete (PDF ke liye)
                String fullDbSql = "SELECT * FROM aditi.ACADEMIC_RECORDS";
                PreparedStatement psFull = con.prepareStatement(
                        fullDbSql,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                );
                ResultSet rsFull = psFull.executeQuery();

                // 🔹 4️⃣ Generate PDF with FULL DB
                String pdfPath = PDFUtil.createPDF(rsFull);
                out.println("<h3>PDF Generated Successfully</h3>");

                // 🔹 5️⃣ Email with PDF attachment
                if (email != null && !email.isEmpty()) {
                    String subject = "Academic Record Deleted";
                    String message =
                        "Hello " + name + ",\n\n" +
                        "Your academic record has been deleted successfully.\n\n" +
                        "Deleted Record Details:\n" +
                        "ID: " + id + "\n" +
                        "Name: " + name + "\n" +
                        "Course: " + course + "\n\n" +
                        "Attached PDF contains all remaining records.";

                    EmailUtil.sendEmailWithAttachment(
                        email,
                        subject,
                        message,
                        pdfPath
                    );

                    out.println("<h3>Email with full DB PDF sent to: " + email + "</h3>");
                }

            } else {
                out.println("<h2>No Record Found with ID: " + id + "</h2>");
            }

            out.println("<br><a href='Record.html'>Go Back</a>");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
