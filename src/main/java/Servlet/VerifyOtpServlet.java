package Servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/verifyOtp")
public class VerifyOtpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userOtp = Integer.parseInt(request.getParameter("otp"));

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.getWriter().println("Session expired. Please login again.");
            return;
        }

        int sessionOtp = (int) session.getAttribute("otp");
        long otpTime = (long) session.getAttribute("otpTime");

        // 🔹 OTP expiry (5 minutes)
        if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
            response.getWriter().println("OTP expired.");
            return;
        }

        if (userOtp == sessionOtp) {
            session.setAttribute("loggedIn", true);
            response.sendRedirect("Record.html");
        } else {
            response.getWriter().println("Invalid OTP");
        }
    }
}
