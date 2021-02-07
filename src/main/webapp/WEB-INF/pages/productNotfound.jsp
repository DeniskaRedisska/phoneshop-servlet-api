<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<tags:master pageTitle="Product Not Found">
    <h1>Sorry</h1>
    <h2>Product With Id: ${requestScope.get("javax.servlet.error.exception").getId()} Was Not Found</h2>
</tags:master>
