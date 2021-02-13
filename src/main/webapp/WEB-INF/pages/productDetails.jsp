<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.product.Cart" scope="request"/>
<tags:master pageTitle="Product List">
    <head>
        <title>Product Details</title>
    </head>
    <body>
    <h2><b>Product Details</b></h2>
    <h3><b>${product.description}</b></h3>
    <p>
        Cart: ${cart}
    </p>

    <div>
    <c:choose>
        <c:when test="${not empty error}">
            <div class="error">
                An error occurred adding product to cart
            </div>
        </c:when>
        <c:otherwise>
            <div class="success">
                ${param.message}
            </div>
        </c:otherwise>
    </c:choose>
    </div>

    <form method="post">
    <table>
        <tr>
            <td>Image</td>
            <td><img src="${product.imageUrl}"></td>
        </tr>
        <tr>
            <td>Code</td>
            <td>${product.code}</td>
        </tr>
        <tr>
            <td>Stock</td>
            <td>${product.stock}</td>
        </tr>
        <tr>
            <td>Price</td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>quantity</td>
            <td>
                <input name="quantity" class="quantity" value="${not empty error ? param.quantity : 1}">
                <c:if test="${not empty error}">
                    <div class="error">
                        ${error}
                    </div>
                </c:if>
            </td>
        </tr>
    </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
    </body>
</tags:master>

