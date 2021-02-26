<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<jsp:useBean id="paymentMethods" scope="request" type="java.util.List"/>
<tags:master pageTitle="Product List">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/popup.css">
    <head>
        <title>Title</title>
    </head>
    <body>
        <%--    todo include this part--%>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            Problem occurred updating a cart
        </div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/checkout">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td>Quantity</td>
                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:if test="${empty cart.items}">Cart is empty</c:if>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src=${item.product.imageUrl}>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td>
                            ${item.quantity}
                            <%--                        <input name="quantity" class="quantity"--%>
                            <%--                               value="${not empty errors[item.product.id] ? paramValues.quantity[status.index] : item.quantity}">--%>
                            <%--                        <input name="productId" type="hidden" value="${item.product.id}">--%>
                            <%--                        <c:if test="${not empty errors[item.product.id]}">--%>
                            <%--                            <div class="error">--%>
                            <%--                                    ${errors[item.product.id]}--%>
                            <%--                            </div>--%>
                            <%--                        </c:if>--%>
                    </td>
                    <td class="price">
                        <a href="#popup${item.product.id}">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                        <div id="popup${item.product.id}" class="overlay">
                            <tags:priceHistory product="${item.product}"/>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${not empty cart.items}">
                <tr>
                    <td></td>
                    <td></td>
                    <td>Total Quantity: ${cart.totalQuantity}</td>
                    <td> Subtotal:
                        <fmt:formatNumber value="${cart.totalCost}" type="currency"
                                          currencySymbol="${cart.items.get(0).product.currency.symbol}"/>
                        Delivery Cost:
                    </td>
                    <td>Total cost:</td>
                </tr>
            </c:if>
        </table>
        <h2>Your Details</h2>
            <table>
                <tags:setParameter paramName="firstName" order="${order}" label="First name" errors="${errors}"/>
                <tags:setParameter paramName="lastName" order="${order}" label="Last name" errors="${errors}"/>
                <tags:setParameter paramName="phoneNumber" order="${order}" label="Phone" errors="${errors}"/>
                <tags:setParameter paramName="deliveryDate" order="${order}" label="Delivery date" errors="${errors}"/>
                <tags:setParameter paramName="deliveryAddress" order="${order}" label="Delivery address" errors="${errors}"/>
                <tr>
                    <td>
                        Payment method
                    </td>
                    <td>
                        <select name="paymentMethod" >
                            <option></option>
                            <c:forEach items="${paymentMethods}" var="paymentMethod">
                                <option ${paymentMethod==order.paymentMethod ? ' selected': ''}>
                                        ${paymentMethod}
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </table>
        <p>
            <button>Buy</button>
        </p>
    </form>
    </body>
</tags:master>
