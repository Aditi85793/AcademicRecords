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

@WebServlet("/updateRecord")
public class UpdateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String course = request.getParameter("course");

            Connection con = DBConnection.getConnection();

            String sql = "UPDATE aditi.ACADEMIC_RECORDS SET NAME=?, EMAIL=?, COURSE=? WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, course);
            ps.setInt(4, id);
            ResultSet rs =null;
            PreparedStatement fetchps=null;
            int rows = ps.executeUpdate();

            if (rows > 0) {
                out.println("<h2>Record Updated Successfully ✅</h2>");
                out.println("<p>ID: " + id + "</p>");

                String selectSQL = "SELECT * FROM aditi.ACADEMIC_RECORDS ";
                PreparedStatement psSelect = con.prepareStatement(selectSQL);
              
                rs = psSelect.executeQuery();
                // 🔹 PDF generate (TABLE FORMAT)
                String pdfPath = PDFUtil.createPDF(rs);
                out.println("<h3>PDF Generated Successfully</h3>");
            

                // ✅ Email with PDF attachment
                if (email != null && !email.isEmpty()) {
                    String subject = "Academic Record Updated";
                    String message =
                            "Hello " + name + ",\n\n" +
                            "Your academic record has been updated successfully.\n\n" +
                            "ID: " + id + "\n" +
                            "Course: " + course;

                    EmailUtil.sendEmailWithAttachment(
                            email,
                            subject,
                            message,
                            pdfPath
                    );

                    out.println("<h3>Email sent to: " + email + "</h3>");
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
