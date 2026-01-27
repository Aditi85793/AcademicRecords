package Servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Record")
public class RecordControllerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            request.getRequestDispatcher("addRecord").forward(request, response);
        }
        else if ("update".equals(action)) {
            request.getRequestDispatcher("updateRecord").forward(request, response);
        }
        else if ("delete".equals(action)) {
            request.getRequestDispatcher("deleteRecord").forward(request, response);
        }
        else if ("view".equals(action)) {
            request.getRequestDispatcher("viewRecord").forward(request, response);
        }

        else {
            response.setContentType("text/html");
            response.getWriter().println("<h3>No Action Selected</h3>");
        }
    }
}