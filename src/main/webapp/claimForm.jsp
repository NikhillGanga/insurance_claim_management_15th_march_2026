<%@ page import="com.claim.model.User"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
User user = (User) session.getAttribute("user");

if(user == null){
    response.sendRedirect("unauthorized.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>

<title>Claim Form</title>

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>
.field-error {
	color: red;
	font-size: 14px;
	margin-top: 3px;
}
</style>

</head>

<body class="container mt-4">

	<h2>Create / Edit Claim</h2>

	<input type="hidden" id="id">

	<div class="card p-4">

		<label>Claim Number</label> <input type="text" id="claimNumber"
			class="form-control">
		<div class="field-error" id="claimNumberError"></div>

		<br> <label>Accident Date</label> <input type="date"
			id="accidentDate" class="form-control">
		<div class="field-error" id="accidentDateError"></div>

		<br> <label>Accident Address</label> <input type="text"
			id="accidentAddress" class="form-control">
		<div class="field-error" id="accidentAddressError"></div>

		<br> <label>Claimant Name</label> <input type="text"
			id="claimantName" class="form-control">
		<div class="field-error" id="claimantNameError"></div>

		<br> <label>Claimant DOB</label> <input type="date"
			id="claimantDob" class="form-control">
		<div class="field-error" id="claimantDobError"></div>

		<br>

		<button type="button" class="btn btn-success" onclick="saveOrUpdate()">Save</button>
		<a href="claimList.jsp" class="btn btn-secondary">Back</a>

	</div>


	<script>
		$(document).ready(function() {

			const params = new URLSearchParams(window.location.search);
			const id = params.get("id");

			if (id) {
				loadClaim(id);
			}

		});

		function loadClaim(id) {

			$.ajax({

				url : "getClaimById.action",
				data : {
					id : id
				},

				success : function(res) {

					let c = res.claim || res;

					$("#id").val(c.id);
					$("#claimNumber").val(c.claimNumber);
					$("#accidentAddress").val(c.accidentAddress);
					$("#claimantName").val(c.claimantName);

					// make claim number readonly during edit
					$("#claimNumber").prop("readonly", true);

					if (c.accidentDate) {
						$("#accidentDate").val(c.accidentDate.split("T")[0]);
					}

					if (c.claimantDob) {
						$("#claimantDob").val(c.claimantDob.split("T")[0]);
					}

				}

			});

		}

		function clearErrors() {
			$(".field-error").text("");
		}

		function saveOrUpdate() {

			clearErrors();

			let id = $("#id").val();
			let url = id ? "updateClaim.action" : "saveClaim.action";

			$.ajax({

				url : url,
				type : "POST",

				data : {
					"claim.id" : id,
					"claim.claimNumber" : $("#claimNumber").val(),
					"claim.accidentAddress" : $("#accidentAddress").val(),
					"claim.accidentDate" : $("#accidentDate").val(),
					"claim.claimantName" : $("#claimantName").val(),
					"claim.claimantDob" : $("#claimantDob").val()
				},

				success : function(res) {

					if (res.apiResponse && res.apiResponse.success === false) {

						if (res.apiResponse.fieldErrors) {

							$.each(res.apiResponse.fieldErrors, function(field,
									messages) {

								let fieldName = field.split(".")[1];
								$("#" + fieldName + "Error").text(messages[0]);

							});

						}

						return;
					}

					alert("Claim saved successfully");
					window.location = "claimList.jsp";

				},

				error : function() {
					alert("Something went wrong");
				}

			});

		}
	</script>

</body>
</html>