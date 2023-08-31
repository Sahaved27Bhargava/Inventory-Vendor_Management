import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/productmanagement";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "WalkingAlive#11";

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String newUserid = request.getParameter("newUserid");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            response.getWriter().write("Passwords do not match");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Check if the user already exists
            String checkUserExistsQuery = "SELECT * FROM users WHERE username = ?";
            stmt = conn.prepareStatement(checkUserExistsQuery);
            stmt.setString(1, newUserid);
            rs = stmt.executeQuery();

            if (rs.next()) {
                response.getWriter().write("User already exists");
            } else {
                // Insert the new user into the database
                String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
                stmt = conn.prepareStatement(insertUserQuery);
                stmt.setString(1, newUserid);
                stmt.setString(2, newPassword);
                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    response.getWriter().write("success"); // Send success response
                } else {
                    response.getWriter().write("Failed to create user");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
