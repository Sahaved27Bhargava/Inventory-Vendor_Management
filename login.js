$(document).ready(function() {
    $("#loginForm").submit(function(e) {
        e.preventDefault();

        var username = $("#username").val();
        var password = $("#password").val();

        $.ajax({
            type: "POST",
            url: "AdminLoginServlet",
            data: {
                username: username,
                password: password
            },
            success: function(response) {
                if (response === "success") {
                    // Redirect to the dashboard if login is successful
                    window.location.href = "admin_dashboard.html";
                } else {
                    // Display error message if login fails
                    $("#response").text("Invalid credentials. Please try again.");
                }
            },
            error: function(xhr, status, error) {
                // Log any errors to the console
                console.log("Error:", error);
            }
        });
    });
});
