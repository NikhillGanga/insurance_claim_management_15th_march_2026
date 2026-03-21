<%@ page import="com.claim.model.User"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<%
User user = (User) session.getAttribute("user");
boolean isManager = user != null && "MANAGER".equalsIgnoreCase(user.getRole());
boolean isCSR = user != null && "CSR".equalsIgnoreCase(user.getRole());

if (user == null) {
	response.sendRedirect("unauthorized.jsp");
	return;
}
%>

<!DOCTYPE html>
<html>
<head>
<title>Insurance Claim Dashboard</title>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body class="bg-light">

	<!-- NAVBAR -->
	<nav class="navbar navbar-dark bg-primary">
		<div class="container-fluid">
			<span class="navbar-brand mb-0 h1">Insurance Claim Management</span>
			<button class="btn btn-light" onclick="logout()">Logout</button>
		</div>
	</nav>

	<div class="container mt-4">

		<!-- DASHBOARD STATS -->
		<div class="row mb-4">

			<div class="col-md-3">
				<div class="card text-center shadow-sm">
					<div class="card-body">
						<h6>Total Claims</h6>
						<h3 id="totalClaims">0</h3>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="card text-center shadow-sm">
					<div class="card-body">
						<h6>New Claims</h6>
						<h3 id="newClaims">0</h3>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="card text-center shadow-sm">
					<div class="card-body">
						<h6>Open Claims</h6>
						<h3 id="openClaims">0</h3>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="card text-center shadow-sm">
					<div class="card-body">
						<h6>Approved</h6>
						<h3 id="approvedClaims">0</h3>
					</div>
				</div>
			</div>

		</div>

		<!-- ACTION BAR -->
		<div class="row mb-3">
			<div class="col-md-6">
				<% if (isCSR) { %>
					<a href="claimForm.jsp" class="btn btn-primary">Create Claim</a>
				<% } %>
			</div>

			<div class="col-md-6">
				<input type="text" id="searchBox" class="form-control"
					placeholder="Search claims (Name, Claim Number, Address, Status)">
			</div>
		</div>

		<!-- CLAIM TABLE -->
		<div class="card shadow-sm">
			<div class="card-header bg-white">
				<h5 class="mb-0">Claims List</h5>
			</div>

			<div class="card-body p-0">
				<table class="table table-hover table-bordered mb-0">
					<thead class="table-light">
						<tr>
							<th>ID</th>
							<th>Claim Number</th>
							<th>Name</th>
							<th>Address</th>
							<th>Status</th>
							<th>Accident Date</th>
							<th>Accident Days</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody id="claimTable"></tbody>
				</table>
			</div>
		</div>

	</div>

<script>
let allClaims = [];

$(document).ready(function() {

	loadClaims();

	// Search filter
	$("#searchBox").on("keyup", function() {
		let value = $(this).val().toLowerCase();
		let filtered = allClaims.filter(function(c) {
			return (
				String(c.id).toLowerCase().includes(value) ||
				(c.claimNumber && c.claimNumber.toLowerCase().includes(value)) ||
				(c.claimantName && c.claimantName.toLowerCase().includes(value)) ||
				(c.accidentAddress && c.accidentAddress.toLowerCase().includes(value)) ||
				(c.status && c.status.toLowerCase().includes(value))
			);
		});
		renderTable(filtered);
	});

});

// Load claims from server
function loadClaims() {
	$.ajax({
		url: "getAllClaims.action",
		success: function(res) {
			allClaims = res.claims || [];
			renderTable(allClaims);
			updateDashboard(allClaims);
		},
		error: function() {
			alert("Failed to load claims");
		}
	});
}

// Update dashboard stats
function updateDashboard(claims) {
	$("#totalClaims").text(claims.length);
	$("#newClaims").text(claims.filter(c => c.status === "NEW").length);
	$("#openClaims").text(claims.filter(c => c.status === "OPEN").length);
	$("#approvedClaims").text(claims.filter(c => c.status === "APPROVED").length);
}

// Render table rows
function renderTable(claims) {
	let rows = "";

	claims.forEach(function(c) {
		let badge = "";
		if (c.status === "NEW") badge = "<span class='badge bg-warning'>NEW</span>";
		else if (c.status === "OPEN") badge = "<span class='badge bg-info'>OPEN</span>";
		else if (c.status === "APPROVED") badge = "<span class='badge bg-success'>APPROVED</span>";

		let accidentDateObj = c.accidentDate ? new Date(c.accidentDate) : null;
		let formattedDate = accidentDateObj ? accidentDateObj.toLocaleDateString() : "N/A";
		let diffDays = accidentDateObj ? Math.floor((new Date() - accidentDateObj)/(1000*60*60*24)) : "N/A";

		rows += "<tr>";
		rows += "<td>" + c.id + "</td>";
		rows += "<td>" + (c.claimNumber || "-") + "</td>";
		rows += "<td>" + (c.claimantName || "-") + "</td>";
		rows += "<td>" + (c.accidentAddress || "-") + "</td>";
		rows += "<td>" + badge + "</td>";
		rows += "<td>" + formattedDate + "</td>";
		rows += "<td>" + diffDays + "</td>";
		rows += "<td>";

		<% if (isCSR) { %>
			if (c.status === "NEW") {
				rows += "<button class='btn btn-sm btn-secondary me-2' onclick='editClaim(" + c.id + ")'>Edit</button>";
				rows += "<button class='btn btn-sm btn-primary me-2' onclick='submitClaim(" + c.id + ")'>Submit</button>";
				rows += "<button class='btn btn-sm btn-danger' onclick='deleteClaim(" + c.id + ")'>Delete</button>";
			} else {
				rows += "<span class='text-danger'>Not Allowed</span>";
			}
		<% } %>

		<% if (isManager) { %>
			if (c.status !== "APPROVED") {
				rows += "<button class='btn btn-sm btn-warning me-2' onclick='editClaim(" + c.id + ")'>Edit</button>";
				rows += "<button class='btn btn-sm btn-success me-2' onclick='approveClaim(" + c.id + ")'>Approve</button>";
				rows += "<button class='btn btn-sm btn-danger' onclick='deleteClaim(" + c.id + ")'>Delete</button>";
			} else {
				rows += "<span class='text-success'>Approved</span>";
			}
		<% } %>

		rows += "</td>";
		rows += "</tr>";
	});

	$("#claimTable").html(rows);
}

// Action functions
function editClaim(id){ window.location="claimForm.jsp?id=" + id; }

function submitClaim(id){
	$.ajax({ url: "submitClaim.action", type: "POST", data: {id:id}, success: loadClaims });
}

function approveClaim(id){
	$.ajax({ url: "approveClaim.action", type: "POST", data: {id:id}, success: loadClaims });
}

function deleteClaim(id){
	if(!confirm("Delete claim?")) return;
	$.ajax({ url: "deleteClaim.action", type: "POST", data: {id:id}, success: loadClaims });
}

function logout(){
	$.ajax({ url: "logout.action", success: function(){ window.location="login.jsp"; } });
}
</script>

</body>
</html>