<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty param.message}">
    <div class="success">
            ${param.message}
    </div>
</c:if>
<c:if test="${not empty errorMsg}">
    <div class="error">
        ${errorMsg}
    </div>
</c:if>