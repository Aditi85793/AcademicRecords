package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/google-callback")
public class GoogleCallbackServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String code = request.getParameter("code");

        if (code != null) {
            // yahan normally access token generate hota hai
            response.sendRedirect("Record.html"); // tumhara main system
        } else {
            response.getWriter().println("Google Login Failed");
        }
    }
}
