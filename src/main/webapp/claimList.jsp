<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.claim.model.User"%>

<%
User user = (User) session.getAttribute("user");
boolean isManager = user != null && "MANAGER".equalsIgnoreCase(user.getRole());
boolean isCSR = user != null && "CSR".equalsIgnoreCase(user.getRole());
%>

<!DOCTYPE html>
<html>
<head>
<title>Claims</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body class="container mt-4">

<h2>Claim Management</h2>

<c:if test="${isCSR}">
    <a href="claimForm.jsp" class="btn btn-primary">Create Claim</a>
</c:if>

<button class="btn btn-danger float-end" onclick="logout()">Logout</button>
<br><br>

<table class="table table-bordered">
    <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Claim Number</th>
            <th>Name</th>
            <th>Address</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody id="claimTable"></tbody>
</table>

<script>
$(document).ready(function() {
    loadClaims();
});

function loadClaims() {
    $.ajax({
        url : "getAllClaims.action",
        success : function(res) {
            let rows = "";
            res.claims.forEach(function(c) {
                let badge = "";
                if (c.status === "NEW") badge = "<span class='badge bg-warning'>NEW</span>";
                if (c.status === "OPEN") badge = "<span class='badge bg-info'>OPEN</span>";
                if (c.status === "APPROVED") badge = "<span class='badge bg-success'>APPROVED</span>";

                rows += "<tr>";
                rows += "<td>" + c.id + "</td>";
                rows += "<td>" + c.claimNumber + "</td>";
                rows += "<td>" + c.claimantName + "</td>";
                rows += "<td>" + c.accidentAddress + "</td>";
                rows += "<td>" + badge + "</td>";
                rows += "<td>";

                <% if (isCSR) { %>
                    if (c.status === "NEW") {
                        rows += "<button class='btn btn-secondary btn-sm me-2' onclick='editClaim("+c.id+")'>Edit</button>";
                        rows += "<button class='btn btn-primary btn-sm me-2' onclick='submitClaim("+c.id+")'>Submit</button>";
                    }
                <% } %>

                <% if (isManager) { %>
                    if (c.status !== "APPROVED") {
                        rows += "<button class='btn btn-warning btn-sm me-2' onclick='editClaim("+c.id+")'>Edit</button>";
                        rows += "<button class='btn btn-success btn-sm me-2' onclick='approveClaim("+c.id+")'>Approve</button>";
                    }
                <% } %>

                rows += "<button class='btn btn-danger btn-sm' onclick='deleteClaim("+c.id+")'>Delete</button>";
                rows += "</td>";
                rows += "</tr>";
            });

            $("#claimTable").html(rows);
        }
    });
}

function editClaim(id) { window.location = "claimForm.jsp?id=" + id; }

function submitClaim(id) {
    $.ajax({
        url : "submitClaim.action",
        type : "POST",
        data : { id: id },
        success : function() { alert("Claim Submitted"); loadClaims(); }
    });
}

function approveClaim(id) {
    $.ajax({
        url : "approveClaim.action",
        type : "POST",
        data : { id: id },
        success : function() { alert("Claim Approved"); loadClaims(); }
    });
}

function deleteClaim(id) {
    if (!confirm("Delete claim?")) return;
    $.ajax({
        url : "deleteClaim.action",
        type : "POST",
        data : { id: id },
        success : function() { alert("Claim Deleted"); loadClaims(); }
    });
}

function logout() {
    $.ajax({
        url : "logout.action",
        success : function() { window.location = "login.jsp"; }
    });
}
</script>
</body>
</html>