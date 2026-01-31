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
import util.PDFUtil;
import util.EmailUtil;

@WebServlet("/viewRecord")
public class ViewServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Connection con = DBConnection.getConnection();

            // 🔹 1️⃣ Browser display → only this ID
            String sql = "SELECT * FROM aditi.ACADEMIC_RECORDS WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            StringBuilder dataForEmail = new StringBuilder();
            String userEmail = null;

            while (rs.next()) {
                found = true;

                int recordId = rs.getInt("ID");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");
                String course = rs.getString("COURSE");

                if (userEmail == null) userEmail = email;

                out.println("<hr>");
                out.println("ID: " + recordId + "<br>");
                out.println("Name: " + name + "<br>");
                out.println("Email: " + email + "<br>");
                out.println("Course: " + course + "<br>");

                dataForEmail.append("ID: ").append(recordId).append("\n")
                            .append("Name: ").append(name).append("\n")
                            .append("Email: ").append(email).append("\n")
                            .append("Course: ").append(course).append("\n\n");
            }

            if (found) {

                // 🔹 2️⃣ Fetch full DB for PDF
                String fullDbSql = "SELECT * FROM aditi.ACADEMIC_RECORDS";
                PreparedStatement psFull = con.prepareStatement(
                        fullDbSql,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                );
                ResultSet rsFull = psFull.executeQuery();

                // 🔹 Generate PDF with all DB records
                String pdfPath = PDFUtil.createPDF(rsFull);

                // 🔹 Send email with full DB PDF
                if (userEmail != null && !userEmail.isEmpty()) {
                    String subject = "Complete Academic Records PDF";
                    String message = "Hello,\n\nAttached is the PDF with all academic records from the database.";

                    EmailUtil.sendEmailWithAttachment(userEmail, subject, message, pdfPath);
                    out.println("<h3>Email with full DB PDF sent to: " + userEmail + "</h3>");
                }

            } else {
                out.println("<h3>No Record Found</h3>");
            }

            out.println("<br><a href='Record.html'>Go Back</a>");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
