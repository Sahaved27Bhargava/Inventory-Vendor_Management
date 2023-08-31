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

@WebServlet("/AddTransactionServlet")
public class AddTransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        int transactionID = Integer.parseInt(request.getParameter("transactionID"));
        int productID = Integer.parseInt(request.getParameter("productID"));
        int supplierID = Integer.parseInt(request.getParameter("supplierID"));
        String transactionType = request.getParameter("transactionType");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String transactionDate = request.getParameter("transactionDate");

        if (checkIfTransactionExists(transactionID)) {
            out.println("Error: Transaction ID already exists.");
        } else {
            if (insertTransaction(transactionID, productID, supplierID, transactionType, quantity, transactionDate)) {
                out.println("Transaction added successfully!");
            } else {
                out.println("Error: Failed to add transaction.");
            }
        }
    }

    private boolean checkIfTransactionExists(int transactionID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/productmanagement","root","WalkingAlive#11");

            String query = "SELECT TransactionID FROM InventoryTransactions WHERE TransactionID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, transactionID);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Return true if a transaction with the given ID exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // An error occurred
        } finally {
            // Close resources properly
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    



    private boolean insertTransaction(int transactionID, int productID, int supplierID,
            String transactionType, int quantity, String transactionDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/productmanagement","root","WalkingAlive#11");

            String insertQuery = "INSERT INTO InventoryTransactions (TransactionID, ProductID, SupplierID, TransactionType, Quantity, TransactionDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, transactionID);
            preparedStatement.setInt(2, productID);
            preparedStatement.setInt(3, supplierID);
            preparedStatement.setString(4, transactionType);
            preparedStatement.setInt(5, quantity);
            preparedStatement.setString(6, transactionDate);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // An error occurred
        } finally {
            // Close resources properly
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
