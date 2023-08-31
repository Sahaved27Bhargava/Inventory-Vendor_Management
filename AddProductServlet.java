import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

@WebServlet("/AddProductServlet")
public class AddProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter out = response.getWriter();

        int productID = Integer.parseInt(request.getParameter("ProductID"));
        String productName = request.getParameter("ProductName");
        String category = request.getParameter("Category");
        String description = request.getParameter("Description");
        BigDecimal price = new BigDecimal(request.getParameter("Price"));
        int quantityAvailable = Integer.parseInt(request.getParameter("QuantityAvailable"));

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/productmanagement", "root", "WalkingAlive#11");

            // Check if the product ID already exists
            String checkQuery = "SELECT ProductID FROM Products WHERE ProductID = ?";
            preparedStatement = conn.prepareStatement(checkQuery);
            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                out.println("Error: Product ID already exists in the database.");
            } else {
                String insertQuery = "INSERT INTO Products (ProductID, ProductName, Category, Description, Price, QuantityAvailable) VALUES (?, ?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setInt(1, productID);
                preparedStatement.setString(2, productName);
                preparedStatement.setString(3, category);
                preparedStatement.setString(4, description);
                preparedStatement.setBigDecimal(5, price);
                preparedStatement.setInt(6, quantityAvailable);

                preparedStatement.executeUpdate();
                out.println("Product added successfully!");
            }
        } catch (Exception e) {
            out.println("An error occurred: " + e.getMessage());
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
