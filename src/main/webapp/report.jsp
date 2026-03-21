<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>Generate User Report</h2>
<s:form action="report">
    <s:textfield name="claimNumber" label="Enter Claim Number"/>
    <s:submit value="Generate PDF"/>
</s:form>
</body>
</html>