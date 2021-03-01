<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="paramName" required="true" type="java.lang.String" %>
<%@attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@attribute name="errors" required="true" type="java.util.Map" %>
<%@ attribute name="label" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<tr>
    <td>${label}<span style="color: red">*</span></td>
    <td>
        <input name="${paramName}"
               value="${not empty errors[paramName] ?  param[paramName] : order[paramName]}"type="${type}">
        <c:if test="${not empty errors[paramName]}">
            <div class="error">
                    ${errors[paramName]}
            </div>
        </c:if>
    </td>
</tr>