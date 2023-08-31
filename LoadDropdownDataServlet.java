import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoadDropdownDataServlet")
public class LoadDropdownDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/productmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "WalkingAlive#11";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        List<Product> products = fetchProductData();
        List<Supplier> suppliers = fetchSupplierData();

        Map<String, List<?>> data = new HashMap<>();
        data.put("products", products);
        data.put("suppliers", suppliers);

        Gson gson = new Gson();
        out.println(gson.toJson(data));
    }

    private List<Product> fetchProductData() {
        List<Product> products = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT productID, productName FROM Products";
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int productID = resultSet.getInt("productID");
                String productName = resultSet.getString("productName");
                products.add(new Product(productID, productName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet, preparedStatement, connection);
        }

        return products;
    }

    private List<Supplier> fetchSupplierData() {
        List<Supplier> suppliers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String query = "SELECT supplierID, supplierName FROM Suppliers";
            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int supplierID = resultSet.getInt("supplierID");
                String supplierName = resultSet.getString("supplierName");
                suppliers.add(new Supplier(supplierID, supplierName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(resultSet, preparedStatement, connection);
        }

        return suppliers;
    }

    private void closeResources(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public class Product {
        private int productID;
        private String productName;

        public Product(int productID, String productName) {
            this.productID = productID;
            this.productName = productName;
        }

        public int getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }
    }

    public class Supplier {
        private int supplierID;
        private String supplierName;

        public Supplier(int supplierID, String supplierName) {
            this.supplierID = supplierID;
            this.supplierName = supplierName;
        }

        public int getSupplierID() {
            return supplierID;
        }

        public String getSupplierName() {
            return supplierName;
        }
    }

    
}
