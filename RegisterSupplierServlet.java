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

@WebServlet("/RegisterSupplierServlet")
public class RegisterSupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int supplierID = Integer.parseInt(request.getParameter("SupplierID"));
        String supplierName = request.getParameter("SupplierName");
        String contactName = request.getParameter("ContactName");
        String email = request.getParameter("Email");
        String phone = request.getParameter("Phone");

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/productmanagement", "root", "WalkingAlive#11");

            // Check if the supplier ID already exists
            String checkQuery = "SELECT SupplierID FROM Suppliers WHERE SupplierID = ?";
            preparedStatement = conn.prepareStatement(checkQuery);
            preparedStatement.setInt(1, supplierID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                out.println("Error: Supplier ID already exists in the database.");
            } else {
                String insertQuery = "INSERT INTO Suppliers (SupplierID, SupplierName, ContactName, Email, Phone) VALUES (?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setInt(1, supplierID);
                preparedStatement.setString(2, supplierName);
                preparedStatement.setString(3, contactName);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phone);

                preparedStatement.executeUpdate();
                out.println("Supplier registered successfully!");
            }
        } catch (Exception e) {
            out.println("An error occurred: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
