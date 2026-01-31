package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/google-login")
public class GoogleLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_ID = "1060433082517-h7bkbvqjs5tv0lh0eagh1sq0jul927v7.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:6060/AcademicRecords/google-callback";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String scope = "openid email profile https://www.googleapis.com/auth/drive.file";
        scope = java.net.URLEncoder.encode(scope, "UTF-8");
        String redirect = java.net.URLEncoder.encode(REDIRECT_URI, "UTF-8");

        String googleAuthURL =
                "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + redirect
                + "&response_type=code"
                + "&scope=" + scope
                + "&access_type=offline"
                + "&prompt=consent";

        response.sendRedirect(googleAuthURL);
    }
}
