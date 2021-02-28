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
                    <td> Subtotal: <tags:price order="${order}" paramName="subTotal"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>Delivery cost: <tags:price order="${order}" paramName="deliveryCost"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>Total cost: <tags:price order="${order}" paramName="totalCost"/>
                    </td>
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
    </body>
</tags:master>
