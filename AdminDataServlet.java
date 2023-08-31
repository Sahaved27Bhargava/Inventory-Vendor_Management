import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AdminDataServlet")
public class AdminDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/productmanagement";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "WalkingAlive#11";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Request received");

        String selectedTable = request.getParameter("selectedTable");
        String tableData = fetchTableData(selectedTable);

        response.getWriter().write(tableData);
    }

    private String fetchTableData(String tableName) {
        StringBuilder tableHtml = new StringBuilder();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);

            System.out.println("query:" + statement);

            ResultSet resultSet = statement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Start the table header row
            tableHtml.append("<table border='1'><tr>");
            for (int j = 1; j <= columnCount; j++) {
                // Append each column name as a table header cell
                tableHtml.append("<th>").append(metaData.getColumnName(j)).append("</th>");
            }
            tableHtml.append("</tr>");

            while (resultSet.next()) {
                tableHtml.append("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    tableHtml.append("<td>").append(resultSet.getString(i)).append("</td>");
                }
                tableHtml.append("</tr>");
            }

            tableHtml.append("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return tableHtml.toString();
    }
}
