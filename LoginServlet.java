import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/LoginServlet")
public class LoginServlet<HttpSession> extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/productmanagement";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "WalkingAlive#11";

	@SuppressWarnings("unused")
	private Object loggedInUserId;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String userId = request.getParameter("userid").trim();
    	String password = request.getParameter("password").trim();
    	 System.out.println("Received User ID: " + userId);
    	    System.out.println("Received Password: " + password);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Establish the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Prepare the SQL statement
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, password);

            // Execute the query
            rs = stmt.executeQuery();

            
            if (rs.next()) {
            	
            	response.getWriter().write("success");

               
            } else {
                // Invalid user credentials
                PrintWriter out = response.getWriter();
                out.println("Invalid user ID or password");
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
