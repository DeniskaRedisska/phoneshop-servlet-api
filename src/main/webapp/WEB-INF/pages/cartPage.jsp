<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Product List">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/popup.css">
    <head>
        <title>Title</title>
    </head>
    <body>
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
    <form method="post" action="${pageContext.request.contextPath}/cart">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
                </td>
                <td>quantity</td>
            </tr>
            </thead>
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
                    <td class="price">
                        <a href="#popup${item.product.id}">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
                        </a>
                        <div id="popup${item.product.id}" class="overlay">
                            <tags:priceHistory product="${item.product}"/>
                        </div>
                    </td>
                    <td>
                        <input name="quantity" class="quantity"
                               value="${not empty errors ? paramValues.quantity[status.index] : item.quantity}">
                        <input name="productId" type="hidden" value="${item.product.id}">
                        <c:if test="${not empty errors[item.product.id]}">
                            <div class="error">
                                    ${errors[item.product.id]}
                            </div>
                        </c:if>
                    </td>
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.request.contextPath}/cart/deleteCartItem/${item.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${not empty cart.items}">
                <tr>
                    <td></td>
                    <td></td>
                    <td> Total Price:
                        <fmt:formatNumber value="${cart.totalPrice}" type="currency"
                                          currencySymbol="${cart.items.get(0).product.currency.symbol}"/><%--todo solve this--%>
                    </td>
                    <td>Total Quantity: ${cart.totalQuantity}</td>
                </tr>
            </c:if>
        </table>
        <p>
            <button>Update cart</button>
        </p>
    </form>
    <form id="deleteCartItem" method="post"></form>
    </body>
</tags:master>
