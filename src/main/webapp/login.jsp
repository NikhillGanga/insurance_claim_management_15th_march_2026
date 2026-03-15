<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<title>Login</title>

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>

<body class="container mt-5">

	<h2>Insurance Claim Login</h2>

	<div class="card p-4">

		<label>Username</label> <input type="text" id="username"
			class="form-control"> <br> <label>Password</label> <input
			type="password" id="password" class="form-control"> <br>

		<button class="btn btn-primary" onclick="login()">Login</button>

		<div id="msg" class="text-danger mt-2"></div>

	</div>

	<script>
		function login() {

			$.ajax({

				url : "login.action",
				type : "POST",

				data : {
					username : $("#username").val(),
					password : $("#password").val()
				},

				success : function() {

					window.location = "claimList.jsp";

				},

				error : function() {

					$("#msg").text("Invalid login");

				}

			});

		}
	</script>

</body>
</html>