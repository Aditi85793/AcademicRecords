package Servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.EmailUtil;

@WebServlet("/sendOtp")
public class SendOtpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        // 🔹 Generate 6-digit OTP
        int otp = (int)(Math.random() * 900000) + 100000;

        // 🔹 Store in session
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);
        session.setAttribute("otpTime", System.currentTimeMillis());

        // 🔹 Email content
        String subject = "Your Login OTP";
        String message = "Your OTP is: " + otp + "\nValid for 5 minutes.";

        EmailUtil.sendSimpleEmail(email, subject, message);

        response.sendRedirect("verifyOtp.html");
    }
}


