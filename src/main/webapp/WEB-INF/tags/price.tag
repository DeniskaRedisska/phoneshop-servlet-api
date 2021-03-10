<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" rtexprvalue="true" %>
<%@ attribute name="paramName" required="true" type="java.lang.String" rtexprvalue="true" %>
<fmt:formatNumber value="${order[paramName]}" type="currency"
                  currencySymbol="${order.items.get(0).product.currency.symbol}"/>