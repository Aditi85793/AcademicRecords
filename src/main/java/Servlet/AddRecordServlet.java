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

@WebServlet("/addRecord")
public class AddRecordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
        ResultSet rs =null;
        PreparedStatement fetchps=null;
        try {
            Connection con = DBConnection.getConnection();

            String sql =
                "INSERT INTO aditi.ACADEMIC_RECORDS(id, name, email, course) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
       
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, course);

            int i = ps.executeUpdate();

            if (i > 0) {

                out.println("<h3>Record Inserted Successfully</h3>");
                String selectSQL = "SELECT * FROM aditi.ACADEMIC_RECORDS ";
                PreparedStatement psSelect = con.prepareStatement(selectSQL);
              
                rs = psSelect.executeQuery();
                // 🔹 PDF generate (TABLE FORMAT)
                String pdfPath = PDFUtil.createPDF(rs);
                out.println("<h3>PDF Generated Successfully</h3>");

                // 🔹 Email body (simple)
                String message =
                    "Hello " + name + ",\n\n" +
                    "Your academic record has been added successfully.\n\n" +
                    "ID: " + id + "\n" +
                    "Name: " + name + "\n" +
                    "Course: " + course + "\n" +
                    "Email: " + email;

                // 🔹 EMAIL WITH PDF ATTACHMENT
                if (email != null && !email.isEmpty()) {
                    EmailUtil.sendEmailWithAttachment(
                        email,
                        "Academic Record Added",
                        message,
                        pdfPath
                    );
                    out.println("<h3>Email with PDF sent to: " + email + "</h3>");
                }

            } else {
                out.println("<h3>Record Insert Failed</h3>");
            }

            out.println("<a href='Record.html'>Go Back</a>");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
