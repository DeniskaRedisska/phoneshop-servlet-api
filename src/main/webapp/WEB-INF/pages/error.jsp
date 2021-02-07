<jsp:useBean id="errorMsg" scope="request" type="java.lang.String"/>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" isErrorPage="true" %>
<tags:master pageTitle="An Error Occurred">
    <h1>Sorry, an error occurred</h1>
    <h2><c:if test="${initParam.get('showError')==true}">${errorMsg}</c:if></h2>
</tags:master>
