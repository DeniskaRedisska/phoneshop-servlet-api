<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag %>
<%@ attribute name="product" required="true" type="com.es.phoneshop.model.product.Product" rtexprvalue="true" %>

<div class="popup">
    <h2 align="center">Price History</h2>
    <a class="close" href="#">&times;</a>
    <div align="center">
        <h3><b>${product.description}</b></h3>
        <h2>
            <c:if test="${product.priceHistory==null or product.priceHistory.size()==0}">History is absent</c:if>
        </h2>
        <table style="border: 0">
            <c:forEach var="priceInfo" items="${product.priceHistory}">
                <tr>
                    <td style="border: 0">${priceInfo.date}</td>
                    <td style="border: 0">
                        <fmt:formatNumber value="${priceInfo.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
