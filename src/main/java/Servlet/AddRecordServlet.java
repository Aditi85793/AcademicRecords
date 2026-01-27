package Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DBConnection;
import util.EmailUtil;
import util.PDFUtil;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@WebServlet("/addRecord")
public class AddRecordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // 1. Form se data lena
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");

        try {
            // 2. DB connection
            Connection con = DBConnection.getConnection();

            // 3. SQL query
            String sql = "INSERT INTO aditi.ACADEMIC_RECORDS(id, name, email, course) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, course);

            // 4. Execute
            int i = ps.executeUpdate();

            if (i > 0) {
                out.println("<h3>Record Inserted Successfully </h3>");
            } else {
                out.println("<h3>Record Insert Failed </h3>");
            }
            out.println("<a href='Record.html'>Go Back</a>");
            if (i > 0) {
                     String data =
                    "Record Added Successfully\n\n" +
                    "ID: " + id + "\n" +
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Course: " + course;
         
                PDFUtil.createPDF(data);
                out.println("<h3>PDF Generated Successfully </h3>");
           
                if (email != null && !email.isEmpty()) {
                    String subject = "Academic Record Added";
                    String message = "Hello " + name + ",\n\nYour record has been added successfully.\n\n" + data;
                    EmailUtil.sendEmail(email, subject, message);
                    out.println("<h3>Email sent to: " + email + "</h3>");
                }
            }

            con.close();
            } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }
}
