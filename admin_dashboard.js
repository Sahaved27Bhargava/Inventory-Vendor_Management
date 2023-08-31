$(document).ready(function() {
    $("#fetchData").click(function() {
        var selectedTable = $("#tableSelect").val();
        fetchTableData(selectedTable);
    });
});

function fetchTableData(selectedTable) {
    $.ajax({
        type: "POST",
        url: "AdminDataServlet",
        data: {
            selectedTable: selectedTable
        },
        success: function(response) {
            console.log("Response:", response); // Add this line to see the response in the console
            displayTableData(response); // Call the function to display the data
        },
        error: function(xhr, status, error) {
            console.log("Error:", error);
        }
    });
}

function displayTableData(tableHtml) {
    $("#tableData").html(tableHtml);
}
