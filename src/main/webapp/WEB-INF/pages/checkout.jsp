<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<jsp:useBean id="paymentMethods" scope="request" type="java.util.List"/>
<tags:master pageTitle="Checkout">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/popup.css">
    <head>
        <title>Title</title>
    </head>
    <body>
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
                <tr>
                    <td></td>
                    <td></td>
                    <td>Total Quantity: ${order.totalQuantity}</td>
                    <td class="price"> Subtotal: <tags:price order="${order}" paramName="subTotal"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="price">Delivery cost: <tags:price order="${order}" paramName="deliveryCost"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="price">Total cost: <tags:price order="${order}" paramName="totalCost"/>
                    </td>
                </tr>
        </table>
        <h2>Your Details</h2>
            <table>
                <tags:setParameter paramName="firstName" order="${order}" label="First name" errors="${errors}" type="text"/>
                <tags:setParameter paramName="lastName" order="${order}" label="Last name" errors="${errors}" type="text"/>
                <tags:setParameter paramName="phoneNumber" order="${order}" label="Phone" errors="${errors}" type="text"/>
                <tags:setParameter paramName="deliveryDate" order="${order}" label="Delivery date" errors="${errors}" type="date"/>
                <tags:setParameter paramName="deliveryAddress" order="${order}" label="Delivery address" errors="${errors}" type="text"/>
                <tr>
                    <td>
                        Payment method<span style="color: red">*</span>
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
                        <c:if test="${not empty errors['paymentMethod']}">
                            <div class="error">
                                    ${errors['paymentMethod']}
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
        <p>
            <button>Place order</button>
        </p>
    </form>
    </body>
</tags:master>
