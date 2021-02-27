<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
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
            <c:if test="${empty order.items}">Cart is empty</c:if>
            <c:forEach var="item" items="${order.items}" varStatus="status">
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
            <c:if test="${not empty order.items}">
                <tr>
                    <td></td>
                    <td></td>
                    <td>Total Quantity: ${order.totalQuantity}</td>
                    <td> Subtotal:
                        <fmt:formatNumber value="${order.totalCost}" type="currency"
                                          currencySymbol="${order.items.get(0).product.currency.symbol}"/>
                        Delivery Cost:
                    </td>
                    <td>Total cost:</td>
                </tr>
            </c:if>
        </table>
        <h2>Your Details</h2>
            <table>
                <tr>
                    <td>First name</td>
                    <td>${order.firstName}</td>
                </tr>
                <tr>
                    <td>Last name</td>
                    <td>${order.lastName}</td>
                </tr>
                <tr>
                    <td>Phone number</td>
                    <td>${order.phoneNumber}</td>
                </tr>
                <tr>
                    <td>Delivery date</td>
                    <td>${order.deliveryDate}</td>
                </tr>
                <tr>
                    <td>Delivery address</td>
                    <td>${order.deliveryAddress}</td>
                </tr>
                <tr>
                    <td>Payment method</td>
                    <td>${order.paymentMethod}</td>
                </tr>
            </table>
        <p>
            <button>Buy</button>
        </p>
    </form>
    </body>
</tags:master>
