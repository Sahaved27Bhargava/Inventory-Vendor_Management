function login() {
    var userId = document.getElementById("userid").value;
    var password = document.getElementById("password").value;

    $.ajax({
        type: "POST",
        url: "LoginServlet",
        data: {
            userid: userId,
            password: password
        },
        success: function(response) {
            if (response.trim() === "success") {
                // Redirect to the form_selection page after successful login
                window.location.href = "form_selection.html";
            } else {
                $("#login-error").text(response.trim());
            }
        }
    });
}


function signup() {
    var newUserid = document.getElementById("newUserid").value;
    var newPassword = document.getElementById("newPassword").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    if (newPassword !== confirmPassword) {
        $("#signup-error").text("Passwords do not match");
        return;
    }

    $.ajax({
        type: "POST",
        url: "SignupServlet",
        data: {
            newUserid: newUserid,
            newPassword: newPassword,
            confirmPassword: confirmPassword
        },
        success: function(response) {
            if (response.trim() === "success") {
                // Redirect to the login page after successful signup
                window.location.href = "login.html";
            } else {
                // Display error message
                $("#signup-error").text(response);
            }
        }
    });
}
