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

            //  Delete se pehle email fetch karo DB se
            String email = null;
            String emailSql = "SELECT EMAIL FROM aditi.ACADEMIC_RECORDS WHERE ID=?";
            PreparedStatement psEmail = con.prepareStatement(emailSql);
            psEmail.setInt(1, id);
            ResultSet rsEmail = psEmail.executeQuery();
            if (rsEmail.next()) {
                email = rsEmail.getString("EMAIL");
            }

            // Delete record
            String sql = "DELETE FROM aditi.ACADEMIC_RECORDS WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h2>Record Deleted Successfully ✅</h2>");
                out.println("<p>ID: " + id + "</p>");

                //  PDF generate
                String data = "Record Deleted Successfully\n\nDeleted ID: " + id;
                PDFUtil.createPDF(data);
                out.println("<h3>PDF Generated Successfully </h3>");

                //Email send
                if (email != null && !email.isEmpty()) {
                    String subject = "Academic Record Deleted";
                    String message = "Hello,\n\nYour record with ID " + id + " has been deleted successfully.\n\n" + data;
                    EmailUtil.sendEmail(email, subject, message);
                    out.println("<h3>Email sent to: " + email + " </h3>");
                } else {
                    out.println("<h3>Email not found for this ID </h3>");
                }

            } else {
                out.println("<h2>No Record Found with ID: " + id + "</h2>");
            }

            out.println("<a href='Record.html'>Go Back</a>");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
