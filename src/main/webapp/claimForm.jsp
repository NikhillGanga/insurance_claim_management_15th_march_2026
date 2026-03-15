<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
<title>Claim Form</title>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>
    .field-error {
        color: red;
        font-size: 0.9rem;
        margin-top: 2px;
    }
</style>

</head>
<body class="container mt-4">

<h2>Create / Edit Claim</h2>

<input type="hidden" id="id">

<div class="card p-4">

    <label>Claim Number</label> 
    <input type="text" id="claimNumber" class="form-control">
    <s:fielderror fieldName="claim.claimNumber" cssClass="field-error" /><br>

    <label>Accident Date</label>
    <input type="date" id="accidentDate" class="form-control">
    <s:fielderror fieldName="claim.accidentDate" cssClass="field-error" /><br>

    <label>Accident Address</label>
    <input type="text" id="accidentAddress" class="form-control">
    <s:fielderror fieldName="claim.accidentAddress" cssClass="field-error" /><br>

    <label>Claimant Name</label>
    <input type="text" id="claimantName" class="form-control">
    <s:fielderror fieldName="claim.claimantName" cssClass="field-error" /><br>

    <label>Claimant DOB</label>
    <input type="date" id="claimantDob" class="form-control">
    <s:fielderror fieldName="claim.claimantDob" cssClass="field-error" /><br>

    <button class="btn btn-success" onclick="saveOrUpdate()">Save</button>
    <a href="claimList.jsp" class="btn btn-secondary">Back</a>

</div>

<script>
$(document).ready(function() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");
    if (id) loadClaim(id);
});

function loadClaim(id) {
    $.ajax({
        url : "getClaimById.action",
        data : { id : id },
        success : function(c) {
            $("#id").val(c.id);
            $("#claimNumber").val(c.claimNumber);
            $("#accidentAddress").val(c.accidentAddress);
            $("#claimantName").val(c.claimantName);
            if (c.accidentDate) $("#accidentDate").val(c.accidentDate.split("T")[0]);
            if (c.claimantDob) $("#claimantDob").val(c.claimantDob.split("T")[0]);
        }
    });
}

function saveOrUpdate() {
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
            if (res.fieldErrors) {
                // Clear previous errors
                $(".field-error").text("");

                // Show field errors dynamically
                $.each(res.fieldErrors, function(field, messages) {
                    let inputField = "#"+field.split(".")[1];
                    $(inputField).next(".field-error").text(messages[0]);
                });
            } else {
                alert("Claim Saved");
                window.location = "claimList.jsp";
            }
        }
    });
}
</script>

</body>
</html>