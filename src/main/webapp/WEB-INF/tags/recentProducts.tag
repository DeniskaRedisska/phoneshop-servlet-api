<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="recentProducts" required="true" type="java.util.ArrayList" %>

<h2>
    Recently viewed
</h2>
<div class="wrapper">
    <c:forEach var="product" items="${recentProducts}">
        <div class="phone">
            <img class="product-tile" src=${product.imageUrl}>
            <a href="${pageContext.request.contextPath}/products/${product.id}"> ${product.description}</a>
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </div>
    </c:forEach>
</div>