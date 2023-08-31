import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (isValidUser(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            out.print("success");
        } else {
            out.print("failure");
        }
    }

    private boolean isValidUser(String username, String password) {
        // Replace this logic with your actual authentication logic.
        // In a real-world scenario, you would validate against a database.
        // Here's a basic example using hardcoded values for demonstration.
        if ("Sahaved".equals(username) && "WalkingAlive#11".equals(password)) {
            return true;
        }
        return false;
    }
    }
