<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>

<title>Claim Form</title>

<link rel="stylesheet"
 href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>

.field-error{
    color:red;
    font-size:14px;
    margin-top:3px;
}

</style>

</head>

<body class="container mt-4">

<h2>Create / Edit Claim</h2>

<input type="hidden" id="id">

<div class="card p-4">

<label>Accident Date</label>
<input type="date" id="accidentDate" class="form-control">
<div class="field-error" id="accidentDateError"></div>
<br>

<label>Accident Address</label>
<input type="text" id="accidentAddress" class="form-control">
<div class="field-error" id="accidentAddressError"></div>
<br>

<label>Claimant Name</label>
<input type="text" id="claimantName" class="form-control">
<div class="field-error" id="claimantNameError"></div>
<br>

<label>Claimant DOB</label>
<input type="date" id="claimantDob" class="form-control">
<div class="field-error" id="claimantDobError"></div>
<br>

<button type="button" class="btn btn-success" onclick="saveOrUpdate()">Save</button>
<a href="claimList.jsp" class="btn btn-secondary">Back</a>

</div>

<script>

$(document).ready(function(){

    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    if(id){
        loadClaim(id);
    }

});


function loadClaim(id){

    $.ajax({

        url:"getClaimById.action",
        data:{id:id},

        success:function(c){

            console.log("Loaded claim:",c);

            $("#id").val(c.id);
            $("#accidentAddress").val(c.accidentAddress);
            $("#claimantName").val(c.claimantName);

            if(c.accidentDate)
                $("#accidentDate").val(c.accidentDate.split("T")[0]);

            if(c.claimantDob)
                $("#claimantDob").val(c.claimantDob.split("T")[0]);

        }

    });

}


function clearErrors(){
    $(".field-error").text("");
}


function validateForm(){

    clearErrors();

    let valid = true;

    let accidentDate = $("#accidentDate").val();
    let accidentAddress = $("#accidentAddress").val().trim();
    let claimantName = $("#claimantName").val().trim();
    let claimantDob = $("#claimantDob").val();

    let today = new Date();
    today.setHours(0,0,0,0);


    if(accidentAddress === ""){
        $("#accidentAddressError").text("Accident Address is required");
        valid = false;
    }

    if(claimantName === ""){
        $("#claimantNameError").text("Claimant Name is required");
        valid = false;
    }

    if(accidentDate === ""){
        $("#accidentDateError").text("Accident Date is required");
        valid = false;
    }
    else{

        let accDate = new Date(accidentDate);

        if(accDate > today){
            $("#accidentDateError").text("Accident date cannot be in the future");
            valid = false;
        }

    }

    if(claimantDob === ""){
        $("#claimantDobError").text("Date of Birth is required");
        valid = false;
    }
    else{

        let dob = new Date(claimantDob);

        if(dob > today){
            $("#claimantDobError").text("Date of Birth cannot be in the future");
            valid = false;
        }
        else{

            let age = today.getFullYear() - dob.getFullYear();
            let m = today.getMonth() - dob.getMonth();

            if(m < 0 || (m === 0 && today.getDate() < dob.getDate())){
                age--;
            }

            if(age < 18){
                $("#claimantDobError").text("Claimant must be at least 18 years old");
                valid = false;
            }

        }

    }

    return valid;

}


function saveOrUpdate(){

    if(!validateForm()){
        return;
    }

    console.log("Submitting form...");

    let id = $("#id").val();
    let url = id ? "updateClaim.action" : "saveClaim.action";


    $.ajax({

        url:url,
        type:"POST",

        data:{

            "claim.id":id,
            "claim.accidentAddress":$("#accidentAddress").val(),
            "claim.accidentDate":$("#accidentDate").val(),
            "claim.claimantName":$("#claimantName").val(),
            "claim.claimantDob":$("#claimantDob").val()

        },

        success:function(res){

            console.log("Response:",res);

            if(res.fieldErrors){

                $.each(res.fieldErrors,function(field,messages){

                    let fieldName = field.split(".")[1];
                    $("#"+fieldName+"Error").text(messages[0]);

                });

            }
            else{

                alert("Claim saved successfully");
                window.location="claimList.jsp";

            }

        },

        error:function(err){
            console.log("Error:",err);
            alert("Something went wrong");
        }

    });

}

</script>

</body>
</html>