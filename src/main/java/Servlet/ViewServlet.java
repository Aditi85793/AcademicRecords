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

            //  Fetch record
            String sql = "SELECT * FROM aditi.ACADEMIC_RECORDS WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            String email = null; 

            StringBuilder dataForPDF = new StringBuilder();

            while (rs.next()) {
                found = true;
                email = rs.getString("EMAIL"); 
                out.println("<hr>");
                out.println("ID: " + rs.getInt("ID") + "<br>");
                out.println("Name: " + rs.getString("NAME") + "<br>");
                out.println("Email: " + email + "<br>");
                out.println("Course: " + rs.getString("COURSE") + "<br>");

                // PDF content
                dataForPDF.append("ID: ").append(rs.getInt("ID")).append("\n")
                          .append("Name: ").append(rs.getString("NAME")).append("\n")
                          .append("Email: ").append(email).append("\n")
                          .append("Course: ").append(rs.getString("COURSE")).append("\n\n");
            }

            if (!found) {
                out.println("<h3>No Record Found </h3>");
            } else {
                //  PDF generate
                PDFUtil.createPDF(dataForPDF.toString());

                //  Email send
                if (email != null && !email.isEmpty()) {
                    String subject = "Your Academic Record Details";
                    String message = "Hello,\n\nHere are your academic record details:\n\n" + dataForPDF.toString();
                    EmailUtil.sendEmail(email, subject, message);
                    out.println("<h3>Email sent to: " + email + " </h3>");
                }
            }

            out.println("<br><a href='Record.html'>Go Back</a>");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
